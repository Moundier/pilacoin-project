package br.ufsm.csi.tapw.pilacoin.service.modulos;

import br.ufsm.csi.tapw.pilacoin.model.Difficulty;
import br.ufsm.csi.tapw.pilacoin.model.json.PilaCoinJson;
import br.ufsm.csi.tapw.pilacoin.service.PilaCoinService;
import br.ufsm.csi.tapw.pilacoin.service.QueueService;
import br.ufsm.csi.tapw.pilacoin.types.IModulo;
import br.ufsm.csi.tapw.pilacoin.util.JournalUtil;
import br.ufsm.csi.tapw.pilacoin.util.Singleton;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class PilaCoinMinecraftService extends IModulo {
    private final QueueService queueService;
    private final PilaCoinService pilaCoinService;
    private final Singleton sharedUtil;

    public PilaCoinMinecraftService(QueueService queueService, PilaCoinService pilaCoinService, Singleton sharedUtil) {
        this.queueService = queueService;
        this.pilaCoinService = pilaCoinService;
        this.sharedUtil = sharedUtil;

        JournalUtil.log("Minerador inicializado");
    }

    public class PilaCoinMinerRunnable implements Runnable {
        private final Difficulty difficulty;
        private boolean running = true;

        public PilaCoinMinerRunnable(Difficulty difficulty) {
            this.difficulty = difficulty;
        }

        @Override
        @SneakyThrows
        public void run() {
            JournalUtil.log("Minerando...");

            for (int count = 1; running; count++) {

                if (Thread.interrupted())
                    throw new InterruptedException();

                PilaCoinJson pilaCoin = pilaCoinService.generatePilaCoin(this.difficulty);

                if (pilaCoin != null) {

                    System.out.println("PILA MINERADO\n---\nEm " + count + " tentativas");

                    queueService.publishPilaCoinMinerado(pilaCoin);

                    count = 0;
                }
            }
        }

        public void stop() {
            this.running = false;
        }
    }

    @Override
    public void update(Difficulty subject) {

        if (!this.modulo.isAtivo() || subject == null) {
            return;
        }

        Integer miningThreads = this.sharedUtil.getProperties().getNumberOfThreads();
        ExecutorService executorService = Executors.newFixedThreadPool(miningThreads);
        for (int i = 0; i < miningThreads; i++) {
            Runnable runnable = new PilaCoinMinerRunnable(subject);
            executorService.execute(runnable);
        }

        // Shutdown the thread pool when done
        executorService.shutdown();
    }
}