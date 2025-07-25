package com.banco.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.banco.exception.SaldoInsuficienteException;

public class ContaCorrente extends Conta {

    private BigDecimal limiteChequeEspecial;
    private static final BigDecimal TARIFA_MENSAL = new BigDecimal("30.00");

    public ContaCorrente(String numero, Cliente titular) {
        this(numero, titular, BigDecimal.ZERO);
    }

    public ContaCorrente(String numero, Cliente titular, BigDecimal limiteChequeEspecial) {
        super(numero, titular);
        this.limiteChequeEspecial = limiteChequeEspecial.setScale(2, RoundingMode.HALF_EVEN);
    }

    @Override
    public void sacar(BigDecimal valor) {
        BigDecimal saldoTotal = getSaldo().add(limiteChequeEspecial);
        if (valor.compareTo(saldoTotal) > 0) {
            throw new SaldoInsuficienteException(
                    "Saldo e limite insuficientes para saque",
                    getSaldo(),
                    valor
            );
        }
        super.sacar(valor);
    }

    @Override
    public void calcularTarifaMensal() {
        setSaldo(getSaldo().subtract(TARIFA_MENSAL));
    }

    public BigDecimal getLimiteChequeEspecial() {
        return limiteChequeEspecial;
    }

    public void setLimiteChequeEspecial(BigDecimal limiteChequeEspecial) {
        this.limiteChequeEspecial = limiteChequeEspecial.setScale(2, RoundingMode.HALF_EVEN);
    }

    @Override
    public String toString() {
        return "ContaCorrente{"
                + "numero='" + getNumero() + '\''
                + ", saldo=" + getSaldo()
                + ", limiteChequeEspecial=" + limiteChequeEspecial
                + ", titular=" + getTitular().getNome()
                + '}';
    }
}
