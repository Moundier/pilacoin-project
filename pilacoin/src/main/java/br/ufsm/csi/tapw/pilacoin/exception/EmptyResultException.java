package br.ufsm.csi.tapw.pilacoin.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Modulo not found")
public class EmptyResultException extends RuntimeException { }
