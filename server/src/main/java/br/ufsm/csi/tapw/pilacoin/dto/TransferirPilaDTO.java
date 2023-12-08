package br.ufsm.csi.tapw.pilacoin.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransferirPilaDTO {
    
    private String chaveUsuarioDestino;
    private String nomeUsuarioDestino;
    private String noncePila;
}
