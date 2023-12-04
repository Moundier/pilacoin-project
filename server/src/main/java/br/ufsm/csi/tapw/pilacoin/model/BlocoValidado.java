package br.ufsm.csi.tapw.pilacoin.model;

import br.ufsm.csi.tapw.pilacoin.model.json.BlocoJson;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BlocoValidado {
    private String nomeValidador;
    private byte[] chavePublicaValidador;
    private byte[] assinaturaBloco;
    private BlocoJson bloco;
}
