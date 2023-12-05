package br.ufsm.csi.tapw.pilacoin.controller;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/sse")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class SentinelController {

    @GetMapping("/updates")
    public SseEmitter updates() {
        SseEmitter emitter = new SseEmitter(300000L);

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            try {
                emitter.send("Update from server: " + System.currentTimeMillis());
            } catch (IOException e) {
                emitter.complete();
                System.err.println("Erro: AsyncRequestTimeoutException");
            }
        }, 1, 1, TimeUnit.SECONDS);

        return emitter;
    }
}
