package br.ufsm.csi.tapw.pilacoin.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SseMessage {

  @Builder.Default
  private Long timestamp = System.currentTimeMillis();
  private String className;
  private String message;
  private SseMessageType messageType;

  public enum SseMessageType {
    MINED_BLOCK,
    VALID_BLOCK,
    MINED_PILA,
    VALID_PILA,
    TRANSFERRED_PILA,
  }
}
