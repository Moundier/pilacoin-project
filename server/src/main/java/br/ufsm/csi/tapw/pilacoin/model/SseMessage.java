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

  private String className;

  @Builder.Default
  private Long timestamp = System.currentTimeMillis();

  private String message;
}
