package br.ufsm.csi.tapw.pilacoin.service;

import br.ufsm.csi.tapw.pilacoin.model.BlocoValidado;
import br.ufsm.csi.tapw.pilacoin.model.PilaCoinValidado;
import br.ufsm.csi.tapw.pilacoin.model.Transferencia;
import br.ufsm.csi.tapw.pilacoin.model.json.*;
import br.ufsm.csi.tapw.pilacoin.util.JacksonUtil;
import lombok.SneakyThrows;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;


@Service
public class QueueService {
    private final RabbitTemplate rabbitTemplate;

    @Value("${queue.pila.minerado}")
    private String PILA_MINERADO;
    
    @Value("${queue.pila.validado}")
    private String PILA_VALIDADO;

    @Value("${queue.bloco.descobre}")
    private String BLOCO_DESCOBERTO;
    
    @Value("${queue.bloco.minerado}")
    private String BLOCO_MINERADO;
    
    @Value("${queue.bloco.validado}")
    private String BLOCO_VALIDADO;

    @Value("${queue.pila.transferir}")
    private String TRANSFERIR_PILA;


    public QueueService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @SneakyThrows
    public QueryResponseJson requestQuery(QueryJson queryJson) {
        String json = JacksonUtil.toString(queryJson);
        this.enqueue("query", json);

        ObjectMapper om = new ObjectMapper();
        ObjectWriter ow = om.writerWithDefaultPrettyPrinter();

        for (int tries = 0; ++tries != 10;) {
            String responseJson = (String) this.rabbitTemplate.receiveAndConvert(queryJson.getNomeUsuario() + "-query");

            String error = ow.writeValueAsString(responseJson);

            System.out.println("\n\nError in here: " + error + "\n\n");

            QueryResponseJson response = JacksonUtil.convert(responseJson, QueryResponseJson.class);

            if (response == null) {
                Thread.sleep(500);

                continue;
            }

            if (response.getIdQuery().equals(queryJson.getIdQuery())) {
                return response;
            }
        }

        return QueryResponseJson.builder()
            .idQuery(0L)
            .build();
    }

    private void enqueue(String queue, String message) {
        this.rabbitTemplate.convertAndSend(queue, message);
    }

    public void publishPilaCoinMinerado(PilaCoinJson pilaCoinJson) {
        this.enqueue(PILA_MINERADO, JacksonUtil.toString(pilaCoinJson));
    }

    public void publishPilaCoinValidado(PilaCoinValidado pilaCoinValidado) {
        this.enqueue(PILA_VALIDADO, JacksonUtil.toString(pilaCoinValidado));
    }

    public void publishBlocoDescoberto(BlocoJson blocoJson) {
        this.enqueue(BLOCO_DESCOBERTO, JacksonUtil.toString(blocoJson));
    }

    public void publishBlocoMinerado(BlocoJson blocoJson) {
        this.enqueue(BLOCO_MINERADO, JacksonUtil.toString(blocoJson));
    }

    public void publishBlocoValidado(BlocoValidado blocoValidado) {
        this.enqueue(BLOCO_VALIDADO, JacksonUtil.toString(blocoValidado));
    }

    public void publishTransferencia(Transferencia transferencia) {
        this.enqueue(TRANSFERIR_PILA, JacksonUtil.toString(transferencia));
    }
}