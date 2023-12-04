package br.ufsm.csi.tapw.pilacoin.types;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.ufsm.csi.tapw.pilacoin.model.Difficulty;
import br.ufsm.csi.tapw.pilacoin.model.Modulo;
import lombok.Setter;
import lombok.SneakyThrows;

public abstract class IModulo implements Observer<Difficulty> {

    public Modulo modulo;

    @Setter
    @JsonIgnore
    public SseEmitter logEmitter;

    public String getNome() {
        return this.getClass().getSimpleName().replaceAll("Service", "");
    }

    public void register(Modulo modulo) {
        this.modulo = modulo;
    }

    @SneakyThrows
    public void log(ModuloLogMessage message) {

        SseEmitter.SseEventBuilder sseEmitter = SseEmitter.event()
        .id("0")
        .name(this.getNome())
        .data(message)
        .reconnectTime(10000);

        this.logEmitter.send(sseEmitter);
    }
}
