package br.ufsm.csi.tapw.pilacoin.service;

import br.ufsm.csi.tapw.pilacoin.blueprints.Observer;
import br.ufsm.csi.tapw.pilacoin.model.Difficulty;
import br.ufsm.csi.tapw.pilacoin.model.SseMessage;
import br.ufsm.csi.tapw.pilacoin.model.SseMessage.SseMessageType;
import br.ufsm.csi.tapw.pilacoin.model.json.PilaCoinJson;
import br.ufsm.csi.tapw.pilacoin.util.JacksonUtil;
import br.ufsm.csi.tapw.pilacoin.util.JournalUtil;
import br.ufsm.csi.tapw.pilacoin.util.Singleton;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
public class PilaCoinMinecraftService implements Observer<Difficulty>  {
    private final QueueService queueService;
    private final PilaCoinService pilaCoinService;
    private final Singleton singleton;

    private final SseService sseService;

    public PilaCoinMinecraftService(QueueService queueService, PilaCoinService pilaCoinService, Singleton singleton, SseService sseService) {
        this.queueService = queueService;
        this.pilaCoinService = pilaCoinService;
        this.singleton = singleton;
        this.sseService = sseService;

        JournalUtil.log("Minerador inicializado");
    }

    public class PilaCoinMinerRunnable implements Runnable {

        private final Difficulty difficulty;
        private final SseService sseService;
        private boolean running = true;

        public PilaCoinMinerRunnable(Difficulty difficulty, SseService sseService) {
            this.difficulty = difficulty;
            this.sseService = sseService;
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

                    SseMessage sseMessage = SseMessage.builder()
                        .className(this.getClass().getName())
                        .message("Pila minerado em " + count + " tentativas")
                        .timestamp(System.currentTimeMillis())
                        .messageType(SseMessageType.MINED_PILA)
                        .build();

                    this.sseService.sendSseMessage(JacksonUtil.toString(sseMessage));

                    queueService.publishPilaCoinMinerado(pilaCoin);

                    count = 0; // counter reset for next pila
                }
            }
        }

        public void stop() {
            this.running = false;
        }
    }

    @Override
    public void update(Difficulty subject) {

        if (subject == null) {
            return;
        }
        int miningThreads = this.singleton.getProperties().getNumberOfThreads();
        for (int i = 0; i < miningThreads; i++) {
            PilaCoinMinerRunnable runnable = new PilaCoinMinerRunnable(subject, this.sseService);
            Thread thread = new Thread(runnable);
            thread.start();
        }

    }
}