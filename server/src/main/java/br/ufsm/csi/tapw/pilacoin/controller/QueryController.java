package br.ufsm.csi.tapw.pilacoin.controller;

import br.ufsm.csi.tapw.pilacoin.model.json.QueryJson;
import br.ufsm.csi.tapw.pilacoin.model.json.QueryResponseJson;
import br.ufsm.csi.tapw.pilacoin.service.QueueService;
import br.ufsm.csi.tapw.pilacoin.util.Singleton;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/query")
@CrossOrigin(origins = "http://localhost:4200")
public class QueryController {
    
    private final QueueService queueService;
    private final Singleton sharedUtil;

    public QueryController(
        QueueService queueService, 
        Singleton sharedUtil
    ) {
        this.queueService = queueService;
        this.sharedUtil = sharedUtil;
    }

    @GetMapping("/usuarios")
    public QueryResponseJson getUsuarios() {
        QueryJson query = QueryJson
            .builder()
            .idQuery(System.nanoTime() - 100)
            .nomeUsuario(this.sharedUtil.getProperties().getUsername())
            .tipoQuery(QueryJson.TipoQuery.USUARIOS)
            .build();

        return this.queueService.requestQuery(query);
    }

    @GetMapping("/pilas")
    public QueryResponseJson getPilas(@RequestParam Map<String, String> filter) {
        QueryJson query = QueryJson
            .builder()
            .idQuery(System.nanoTime() - 200)
            .nomeUsuario(this.sharedUtil.getProperties().getUsername())
            .tipoQuery(QueryJson.TipoQuery.PILA)
            .build();

        if (filter.get("self") != null && filter.get("self").equals("true")) {
            query.setUsuarioMinerador(this.sharedUtil.getProperties().getUsername());
        } else {
            query.setUsuarioMinerador(filter.get("usuarioMinerador"));
        }

        return this.queueService.requestQuery(query);
    }

    @GetMapping("/blocos")
    public QueryResponseJson getBlocos(@RequestParam Map<String, String> filter) {
        QueryJson query = QueryJson
            .builder()
            .idQuery(System.nanoTime() - 300)
            .nomeUsuario(this.sharedUtil.getProperties().getUsername())
            .tipoQuery(QueryJson.TipoQuery.BLOCO)
            .build();

        if (filter.get("self") != null && filter.get("self").equals("true")) {
            query.setUsuarioMinerador(this.sharedUtil.getProperties().getUsername());
        } else {
            query.setUsuarioMinerador(filter.get("usuarioMinerador"));
        }

        return this.queueService.requestQuery(query);
    }
}
