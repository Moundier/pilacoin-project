package br.ufsm.csi.tapw.pilacoin.model.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder(alphabetic = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QueryJson {
    private Long idQuery;
    private String nomeUsuario;

    private TipoQuery tipoQuery;
    private String status;
    private String usuarioMinerador;
    private String nonce;
    private Long idBloco;

    public enum TipoQuery {
        PILA,
        BLOCO,
        USUARIOS
    }
}
