package br.ufsm.csi.tapw.pilacoin.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigInteger;
import java.util.Date;

@Data
@Builder
public class Difficulty {
    BigInteger dificuldade;
    Date inicio;
    Date validadeFinal;
}