package br.ufsm.csi.tapw.pilacoin.service;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import lombok.SneakyThrows;

@Service
public class SseService {

  public void scheduleToTransmit(SseEmitter sseEmitter) {

    ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
    ses.scheduleAtFixedRate(() -> transmit(sseEmitter, null), 1, 1, TimeUnit.SECONDS);
    sseEmitter.onCompletion(() -> ses.shutdown());
  }

  @SneakyThrows
  public void transmit(SseEmitter emitter, String message) {
    try {
      if (message == null) {
        emitter.send("ERROR_STRING_NULL: " + System.currentTimeMillis());
      } else {
        emitter.send(message);
      }
    } catch (IOException e) {
      emitter.complete();
      System.err.println("Error: AsyncRequestTimeoutException");
    }
  }

  // New emitters filtered by type on the sent json
  private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();

  public SseEmitter createEmitter() {
    SseEmitter emitter = new SseEmitter();
    emitters.add(emitter);
    emitter.onCompletion(() -> emitters.remove(emitter));
    return emitter;
  }

  public void sendSSE(String json) {
    
    String redString = "\u001B[31m" + json + "\u001B[0m";
    System.out.println(redString);

    for (var emitter : emitters) {
      try {
        emitter.send(SseEmitter.event().data(json));
      } catch (IOException e) {
        emitter.complete();
      }
    }
  }

}
