package br.ufsm.csi.tapw.pilacoin.service.modulos;

import br.ufsm.csi.tapw.pilacoin.model.Difficulty;
import br.ufsm.csi.tapw.pilacoin.model.json.BlocoJson;
import br.ufsm.csi.tapw.pilacoin.service.QueueService;
import br.ufsm.csi.tapw.pilacoin.types.IModulo;
import br.ufsm.csi.tapw.pilacoin.util.CryptoUtil;
import br.ufsm.csi.tapw.pilacoin.util.JacksonUtil;
import br.ufsm.csi.tapw.pilacoin.util.JournalUtil;
import br.ufsm.csi.tapw.pilacoin.util.Singleton;
import io.micrometer.common.util.StringUtils;
import lombok.SneakyThrows;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BlockDiscoveryService extends IModulo {

    // FINDS AND MINES BLOCKS

    private final QueueService queueService;
    private final Singleton sharedUtil;
    private final List<BlockMinerRunnable> threads = new ArrayList<>();

    private Difficulty difficulty;

    public BlockDiscoveryService(QueueService queueService, Singleton sharedUtil) {
        this.queueService = queueService;
        this.sharedUtil = sharedUtil;
    }

    @RabbitListener(queues = "${queue.bloco.descobre}")
    public void descobreBloco(@Payload String json) {
        if (this.difficulty == null || StringUtils.isEmpty(json)) {
            return;
        }

        BlocoJson blocoJson = JacksonUtil.convert(json, BlocoJson.class);

        if (blocoJson == null || !this.modulo.isAtivo()) {
            this.queueService.publishBlocoDescoberto(blocoJson);
            return;
        }

        String nomeUsuarioMinerador = blocoJson.getNomeUsuarioMinerador();
        String username = sharedUtil.getProperties().getUsername();

        if (nomeUsuarioMinerador != null && nomeUsuarioMinerador.equals(username)) {
            return;
        }

        blocoJson.setNonceBlocoAnterior(blocoJson.getNonce());

        BlockMinerRunnable runnable = new BlockMinerRunnable(blocoJson, this.difficulty);
        Thread t = new Thread(runnable);
        t.setName("BlockMinerThread");

        this.threads.add(runnable);
    }

    @Override
    public void update(Difficulty subject) {
        this.difficulty = subject;

        this.threads.forEach(BlockMinerRunnable::stop);
        this.threads.clear();
    }

    public class BlockMinerRunnable implements Runnable {
        
        private final Difficulty difficulty;
        private final BlocoJson blocoJson;

        private boolean running = true;

        public BlockMinerRunnable(BlocoJson blocoJson, Difficulty difficulty) {
            this.difficulty = difficulty;
            this.blocoJson = blocoJson;

            JournalUtil.log("Minerando bloco... | " + JacksonUtil.toString(blocoJson));

            blocoJson.setChaveUsuarioMinerador(sharedUtil.getPublicKey().getEncoded());
            blocoJson.setNomeUsuarioMinerador(sharedUtil.getProperties().getUsername());
        }

        @Override
        @SneakyThrows
        public void run() {
            
            for (int count = 1; this.running != false; count++) {
                blocoJson.setNonce(CryptoUtil.getRandomNonce());

                String minedJson = JacksonUtil.toString(blocoJson);

                if (CryptoUtil.compareHash(minedJson, this.difficulty.getDificuldade())) {
                    JournalUtil.logDoubleLineBox("BLOCO MINERADO\n---\nEm " + count + " tentativas");
                    queueService.publishBlocoMinerado(blocoJson);
                    this.running = false;
                    break;
                }
            }

            Thread.currentThread().interrupt();
        }

        public void stop() {
            this.running = false;
        }
    }
}
