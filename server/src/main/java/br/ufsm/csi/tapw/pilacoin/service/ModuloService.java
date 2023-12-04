package br.ufsm.csi.tapw.pilacoin.service;

import br.ufsm.csi.tapw.pilacoin.exception.CustomNotFoundException;
import br.ufsm.csi.tapw.pilacoin.model.Modulo;
import br.ufsm.csi.tapw.pilacoin.repository.ModuloRepository;
import br.ufsm.csi.tapw.pilacoin.types.IModulo;
import br.ufsm.csi.tapw.pilacoin.types.ModuloLogMessage;
import br.ufsm.csi.tapw.pilacoin.util.JournalUtil;
import lombok.Getter;
import lombok.SneakyThrows;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ModuloService {

    private final ModuloRepository moduloRepository;
    private final DifficultyService difficultyService;
    private final Map<String, IModulo> modulos = new HashMap<>();

    @Getter
    private SseEmitter emitter = new SseEmitter(-1L);

    public ModuloService(
        ModuloRepository moduloRepository,
        DifficultyService difficultyService,
        IModulo... modulos
    ) {
        this.moduloRepository = moduloRepository;
        this.difficultyService = difficultyService;

        for (IModulo modulo : modulos) {
            modulo.register(this.registerModulo(modulo));
        }
    }

    public List<IModulo> getAllModulos() {
        return this.modulos.values().stream().toList();
    }

    public Modulo getModuloEntity(String nome) {
        return moduloRepository.findOneByNome(nome);
    }

    public IModulo getModulo(String nome) {
        return this.modulos.get(nome);
    }

    public Modulo toggleModulo(String nome) {
        Modulo modulo = this.getModuloEntity(nome);

        modulo.setAtivo(!modulo.isAtivo());

        moduloRepository.save(modulo);

        IModulo moduloInterface = this.getModulo(nome);

        if (moduloInterface == null) {
            throw new CustomNotFoundException("modulo not found");
        }

        moduloInterface.register(modulo);
        moduloInterface.update(this.difficultyService.getDifficulty());

        JournalUtil.log("Modulo " + nome + " " + (modulo.isAtivo() ? "ativado" : "desativado"));
        ModuloLogMessage m = ModuloLogMessage.builder()
        .title("Status do Modulo " + nome)
        .message((modulo.isAtivo() ? "Ativado" : "Desativado"))
        .extra(modulo)
        .build(); 
        moduloInterface.log(m);

        return modulo;
    }

    public Modulo registerModulo(IModulo modulo) {
        Modulo foundModulo = moduloRepository.findOneByNome(modulo.getNome());

        modulo.setLogEmitter(this.emitter);
        this.modulos.put(modulo.getNome(), modulo);

        if (foundModulo != null) {
            return foundModulo;
        }

        Modulo moduloEntity = Modulo.builder()
            .nome(modulo.getNome())
            .ativo(true)
            .build();

        return moduloRepository.save(moduloEntity);
    }

    @SneakyThrows
    public void log(ModuloLogMessage message) {
        SseEmitter.SseEventBuilder sse = SseEmitter.event()
        .id("0")
        .name(message.getTopic())
        .data(message)
        .reconnectTime(10000); 
        this.emitter.send(sse);
    }
}
