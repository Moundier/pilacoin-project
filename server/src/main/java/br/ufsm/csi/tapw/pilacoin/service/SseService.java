package br.ufsm.csi.tapw.pilacoin.service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import br.ufsm.csi.tapw.pilacoin.model.SseMessage;

public class SseService {

  private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

  public void addEmitter(SseEmitter emitter) {
    emitters.add(emitter);
  }

  public void removeEmitter(SseEmitter emitter) {
    emitters.remove(emitter);
  }

  public void sendMessage(String className, String message) {
    SseMessage sseMessage = new SseMessage();
    sseMessage.setClassName(className);
    sseMessage.setMessage(message);

    for (SseEmitter sseEmitter : emitters) {
      try {
        sseEmitter.send(SseEmitter.event().data(sseMessage));
      } catch (IOException e) {
        sseEmitter.complete();
        emitters.remove(sseEmitter);
        e.printStackTrace();
      }
    }
  }
}
