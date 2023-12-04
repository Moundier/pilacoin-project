package br.ufsm.csi.tapw.pilacoin.controller;

import br.ufsm.csi.tapw.pilacoin.model.Modulo;
import br.ufsm.csi.tapw.pilacoin.service.ModuloService;
import br.ufsm.csi.tapw.pilacoin.types.IModulo;
import lombok.SneakyThrows;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/modulo")
public class ModuloController {

    private final ModuloService moduloService;

    public ModuloController(ModuloService moduloService) {
        this.moduloService = moduloService;
    }

    @GetMapping("/{nome}")
    public Modulo getModulo(@PathVariable String nome) {
        return moduloService.getModuloEntity(nome);
    }

    @GetMapping
    public List<IModulo> getModulos() {
        return moduloService.getAllModulos();
    }

    @SneakyThrows
    @GetMapping(value = "/logs", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter getLogs() {
        return moduloService.getEmitter();
    }

    @PostMapping("/{nome}/toggle")
    public Modulo toggleModulo(@PathVariable String nome) {
        return moduloService.toggleModulo(nome);
    }
}
