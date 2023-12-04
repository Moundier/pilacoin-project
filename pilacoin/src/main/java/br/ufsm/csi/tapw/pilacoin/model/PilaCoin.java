package br.ufsm.csi.tapw.pilacoin.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"dataCriacao", "chaveCriador", "nomeCriador", "nonce"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PilaCoin implements Cloneable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;
    private Date dataCriacao;
    private byte[] chaveCriador;
    private String nomeCriador;

    @Enumerated(EnumType.STRING)
    private Status status;
    @Column(unique = true)
    private String nonce;

    @Override
    public PilaCoin clone() {
        try {
            return (PilaCoin) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public enum Status {
        AG_VALIDACAO,
        AG_BLOCO,
        BLOCO_EM_VALIDACAO,
        VALIDO,
        INVALIDO
    }
}