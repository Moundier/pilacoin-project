package br.ufsm.csi.tapw.pilacoin.model;

import br.ufsm.csi.tapw.pilacoin.model.json.PilaCoinJson;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PilaCoinValidado {
    private String nomeValidador;
    private byte[] chavePublicaValidador;
    private byte[] assinaturaPilaCoin;
    private PilaCoinJson pilaCoinJson;
}
