package com.banco.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class OperacaoInvalidaException extends RuntimeException {

    public OperacaoInvalidaException(String message) {
        super(message);
    }

    public OperacaoInvalidaException(String message, Throwable cause) {
        super(message, cause);
    }
}
