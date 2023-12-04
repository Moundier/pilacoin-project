package br.ufsm.csi.tapw.pilacoin.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder(alphabetic = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Transferencia {
    private Long id;
    private String status;
    private byte[] chaveUsuarioOrigem;
    private byte[] chaveUsuarioDestino;
    private String nomeUsuarioOrigem;
    private String nomeUsuarioDestino;
    private String noncePila;
    private byte[] assinatura;
    private Date dataTransacao;
}
