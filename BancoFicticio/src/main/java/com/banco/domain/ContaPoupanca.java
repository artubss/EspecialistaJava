package com.banco.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ContaPoupanca extends Conta {

    private BigDecimal taxaRendimento;
    private static final BigDecimal TARIFA_MENSAL = BigDecimal.ZERO;

    public ContaPoupanca(String numero, Cliente titular) {
        this(numero, titular, new BigDecimal("0.005")); // 0.5% ao mês
    }

    public ContaPoupanca(String numero, Cliente titular, BigDecimal taxaRendimento) {
        super(numero, titular);
        this.taxaRendimento = taxaRendimento.setScale(4, RoundingMode.HALF_EVEN);
    }

    public void calcularRendimento() {
        BigDecimal rendimento = getSaldo().multiply(taxaRendimento)
                .setScale(2, RoundingMode.HALF_EVEN);
        setSaldo(getSaldo().add(rendimento));
    }

    @Override
    public void calcularTarifaMensal() {
        setSaldo(getSaldo().subtract(TARIFA_MENSAL));
    }

    public BigDecimal getTaxaRendimento() {
        return taxaRendimento;
    }

    public void setTaxaRendimento(BigDecimal taxaRendimento) {
        this.taxaRendimento = taxaRendimento.setScale(4, RoundingMode.HALF_EVEN);
    }

    @Override
    public String toString() {
        return "ContaPoupanca{"
                + "numero='" + getNumero() + '\''
                + ", saldo=" + getSaldo()
                + ", taxaRendimento=" + taxaRendimento
                + ", titular=" + getTitular().getNome()
                + '}';
    }
}
