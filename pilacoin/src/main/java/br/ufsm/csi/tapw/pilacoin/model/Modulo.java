package br.ufsm.csi.tapw.pilacoin.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Modulo {
    
    @Id 
    @GeneratedValue
    private Long id;
    private String nome;
    private String descricao;
    private boolean ativo;
}