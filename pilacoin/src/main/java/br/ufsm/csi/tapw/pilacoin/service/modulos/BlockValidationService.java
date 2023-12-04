package br.ufsm.csi.tapw.pilacoin.service.modulos;

import br.ufsm.csi.tapw.pilacoin.model.BlocoValidado;
import br.ufsm.csi.tapw.pilacoin.model.Difficulty;
import br.ufsm.csi.tapw.pilacoin.model.json.BlocoJson;
import br.ufsm.csi.tapw.pilacoin.service.QueueService;
import br.ufsm.csi.tapw.pilacoin.types.IModulo;
import br.ufsm.csi.tapw.pilacoin.util.CryptoUtil;
import br.ufsm.csi.tapw.pilacoin.util.JacksonUtil;
import br.ufsm.csi.tapw.pilacoin.util.JournalUtil;
import br.ufsm.csi.tapw.pilacoin.util.Singleton;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class BlockValidationService extends IModulo {
    private final QueueService queueService;
    private final Singleton sharedUtil;

    private Difficulty difficulty;

    public BlockValidationService(QueueService queueService, Singleton sharedUtil) {
        this.queueService = queueService;
        this.sharedUtil = sharedUtil;
    }

    @RabbitListener(queues = "${queue.bloco.minerado}")
    public void validarBloco(@Payload String json) {
        if (this.difficulty == null || json == null || json.isEmpty()) {
            return;
        }

        BlocoJson blocoJson = JacksonUtil.convert(json, BlocoJson.class);

        if (blocoJson == null) {
            return;
        }

        if (!this.modulo.isAtivo()) {
            this.queueService.publishBlocoMinerado(blocoJson);
            return;
        }

        boolean valid = CryptoUtil.compareHash(json, this.difficulty.getDificuldade());

        if (blocoJson.getNomeUsuarioMinerador().equals(this.sharedUtil.getProperties().getUsername()) || !valid) {
            this.queueService.publishBlocoMinerado(blocoJson);

            return;
        }

        BlocoValidado blocoValidado = BlocoValidado.builder()
            .nomeValidador(this.sharedUtil.getProperties().getUsername())
            .chavePublicaValidador(this.sharedUtil.getPublicKey().getEncoded())
            .assinaturaBloco(CryptoUtil.sign(json, this.sharedUtil.getPrivateKey()))
            .bloco(blocoJson)
            .build();

        this.queueService.publishBlocoValidado(blocoValidado);

        System.out.println("BLOCO VALIDADO\n---\n" + blocoJson.getNomeUsuarioMinerador());

        JournalUtil.log("Bloco de " + blocoJson.getNomeUsuarioMinerador() + " validado.");
    }

    @Override
    public void update(Difficulty subject) {
        this.difficulty = subject;
    }
}
