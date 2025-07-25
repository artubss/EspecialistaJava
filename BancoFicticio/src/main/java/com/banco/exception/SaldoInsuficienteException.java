package com.banco.exception;
package com.banco.exception;
package com.banco.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SaldoInsuficienteException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public SaldoInsuficienteException(String message) {
        super(message);
    }

    public SaldoInsuficienteException(String message, Throwable cause) {
        super(message, cause);
    }
}
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SaldoInsuficienteException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public SaldoInsuficienteException(String message) {
        super(message);
    }

    public SaldoInsuficienteException(String message, Throwable cause) {
        super(message, cause);
    }
}
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
