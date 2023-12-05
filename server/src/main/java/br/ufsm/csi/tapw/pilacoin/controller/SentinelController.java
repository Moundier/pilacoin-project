package br.ufsm.csi.tapw.pilacoin.controller;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/sse")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class SentinelController {
  
    private final AtomicLong counter = new AtomicLong();

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public String stream() {
        System.out.println("Loggin if works");
        return "data: {\"message\":\"Update " + counter.incrementAndGet() + "\"}\n\n";
    }
}
