package br.ufsm.csi.tapw.pilacoin.service;

import java.util.List;
import java.util.Objects;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;

import br.ufsm.csi.tapw.pilacoin.model.PilaCoin;
import br.ufsm.csi.tapw.pilacoin.model.json.MessageJson;
import br.ufsm.csi.tapw.pilacoin.model.json.ReportJson;
import br.ufsm.csi.tapw.pilacoin.util.JacksonUtil;
import br.ufsm.csi.tapw.pilacoin.util.JournalUtil;
import br.ufsm.csi.tapw.pilacoin.util.Singleton;

@Service
public class UserQueueService {
    
    private final PilaCoinService pilaCoinService;
    private final Singleton sharedUtil;

    @Value("${queue.pila.minerado}")
    private String PILA_MINERADO;
    @Value("${queue.pila.validado}")
    private String PILA_VALIDADO;

    private ReportJson lastReport;

    public UserQueueService(PilaCoinService pilaCoinService, Singleton sharedUtil) {
        this.pilaCoinService = pilaCoinService;
        this.sharedUtil = sharedUtil;
    }

    @RabbitListener(queues = "${pilacoin.username}")
    public void onResponse(@Payload String response) {
        MessageJson message = JacksonUtil.convert(response, MessageJson.class);

        if (Objects.isNull(message)) {
            return;
        }

        if (message.getErro() != null) {
            this.handleError(message);

            return;
        }

        if (message.getQueue().equals(PILA_MINERADO)) {
            PilaCoin pilaCoin = this.pilaCoinService.findByNonce(message.getNonce());

            this.pilaCoinService.changeStatus(pilaCoin, PilaCoin.Status.VALIDO);
        }

        if (message.getQueue().equals(PILA_VALIDADO)) {
            PilaCoin pilaCoin = this.pilaCoinService.findByNonce(message.getNonce());

            this.pilaCoinService.changeStatus(pilaCoin, PilaCoin.Status.AG_BLOCO);
        }

        JournalUtil.log(response);
    }

    @RabbitListener(queues = "report")
    public void onReport(@Payload String report) {
        List<ReportJson> reports = JacksonUtil.convert(report, new TypeReference<List<ReportJson>>() {
        });

        if (Objects.isNull(reports)) {
            return;
        }

        ReportJson newReport = reports.stream()
            .filter((r) -> r.getNomeUsuario() != null)
            .filter((r) -> r.getNomeUsuario().equals(this.sharedUtil.getProperties().getUsername()))
            .findFirst()
            .orElse(null);

        if (newReport != null) {
            if (this.lastReport == null || !newReport.compareTo(this.lastReport)) {
                this.lastReport = newReport;

                newReport.printReport();
            }
        }
    }

    private void handleError(MessageJson message) {
        if (message.getQueue().equals(PILA_MINERADO)) {
            PilaCoin pilaCoin = this.pilaCoinService.findByNonce(message.getNonce());

            this.pilaCoinService.changeStatus(pilaCoin, PilaCoin.Status.INVALIDO);
        }

        JournalUtil.log(message.getErro());
    }
}