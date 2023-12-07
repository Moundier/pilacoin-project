package br.ufsm.csi.tapw.pilacoin.service;

import br.ufsm.csi.tapw.blueprints.Observer;
import br.ufsm.csi.tapw.pilacoin.model.Difficulty;
import br.ufsm.csi.tapw.pilacoin.model.PilaCoinValidado;
import br.ufsm.csi.tapw.pilacoin.model.SseMessage;
import br.ufsm.csi.tapw.pilacoin.model.SseMessage.SseMessageType;
import br.ufsm.csi.tapw.pilacoin.model.json.PilaCoinJson;
import br.ufsm.csi.tapw.pilacoin.util.CryptoUtil;
import br.ufsm.csi.tapw.pilacoin.util.JacksonUtil;
import br.ufsm.csi.tapw.pilacoin.util.JournalUtil;
import br.ufsm.csi.tapw.pilacoin.util.Singleton;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class PilaCoinValidationService implements Observer<Difficulty>  {
    
    private final QueueService queueService;
    private final Singleton sharedUtil;
    private Difficulty difficulty;
    private final SseService sseService;

    public PilaCoinValidationService(QueueService queueService, Singleton sharedUtil, SseService sseService) {
        this.queueService = queueService;
        this.sharedUtil = sharedUtil;
        this.sseService = sseService;
    }

    @RabbitListener(queues = "${queue.pila.minerado}")
    public void validatePila(@Payload String json) {

        if (this.difficulty == null || json == null || json.isEmpty()) 
            return;

        PilaCoinJson pilaCoinJson = JacksonUtil.convert(json, PilaCoinJson.class);

        boolean valid = CryptoUtil.compareHash(json, this.difficulty.getDificuldade());

        if (pilaCoinJson.getNomeCriador().equals(this.sharedUtil.getProperties().getUsername()) || !valid) {
            this.queueService.publishPilaCoinMinerado(pilaCoinJson);
            return;
        }

        PilaCoinValidado pilaCoinValidado = PilaCoinValidado.builder()
            .nomeValidador(this.sharedUtil.getProperties().getUsername())
            .chavePublicaValidador(this.sharedUtil.getPublicKey().getEncoded())
            .assinaturaPilaCoin(CryptoUtil.sign(json, this.sharedUtil.getPrivateKey()))
            .pilaCoinJson(pilaCoinJson)
            .build();

        this.queueService.publishPilaCoinValidado(pilaCoinValidado);

        System.out.println("PILA VALIDADO\n---\n" + pilaCoinJson.getNomeCriador());

        SseMessage sseMessage = SseMessage.builder()
            .className(this.getClass().getName())
            .message("Pila validado " + pilaCoinJson.getNomeCriador())
            .timestamp(System.currentTimeMillis())
            .messageType(SseMessageType.MINED_PILA)
            .build();

        this.sseService.sendSseMessage(JacksonUtil.toString(sseMessage));

        JournalUtil.log("PilaCoin de " + pilaCoinJson.getNomeCriador() + " validado.");
    }

    @Override
    public void update(Difficulty subject) {
        this.difficulty = subject;
    }
}
