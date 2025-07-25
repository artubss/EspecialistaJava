package com.banco.exception;

import java.math.BigDecimal;

public class SaldoInsuficienteException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final BigDecimal saldoAtual;
    private final BigDecimal valorSolicitado;

    public SaldoInsuficienteException(String message) {
        this(message, null, null);
    }

    public SaldoInsuficienteException(String message, BigDecimal saldoAtual, BigDecimal valorSolicitado) {
        super(message);
        this.saldoAtual = saldoAtual;
        this.valorSolicitado = valorSolicitado;
    }

    public BigDecimal getSaldoAtual() {
        return saldoAtual;
    }

    public BigDecimal getValorSolicitado() {
        return valorSolicitado;
    }
}
