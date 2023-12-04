package br.ufsm.csi.tapw.pilacoin.dto;

import lombok.Data;

@Data
public class TransferirPilaDTO {
    
    private String chaveUsuarioDestino;
    private String nomeUsuarioDestino;
    private String noncePila;
}
