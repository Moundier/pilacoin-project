package br.ufsm.csi.tapw.pilacoin.model.json;

import lombok.Data;

@Data
public class MessageJson {
    private String msg;
    private String erro;
    private String nomeUsuario;
    private String nonce;
    private String queue;
}
