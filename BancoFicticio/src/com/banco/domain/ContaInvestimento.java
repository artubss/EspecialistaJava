package com.banco.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ContaInvestimento extends Conta implements Tributavel {

    private TipoInvestimento tipo;
    private BigDecimal taxaAdministracao;
    private static final BigDecimal TARIFA_MENSAL = new BigDecimal("15.00");

    public ContaInvestimento(String numero, Cliente titular, TipoInvestimento tipo) {
        super(numero, titular);
        this.tipo = tipo;
        this.taxaAdministracao = tipo.getTaxaAdministracao();
    }

    public void investir(BigDecimal valor) {
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor do investimento deve ser maior que zero");
        }
        depositar(valor);
    }

    public void resgatar(BigDecimal valor) {
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor do resgate deve ser maior que zero");
        }
        sacar(valor);
    }

    @Override
    public void calcularTarifaMensal() {
        BigDecimal tarifa = TARIFA_MENSAL.add(
                getSaldo().multiply(taxaAdministracao)
                        .setScale(2, RoundingMode.HALF_EVEN)
        );
        setSaldo(getSaldo().subtract(tarifa));
    }

    @Override
    public BigDecimal calcularImposto() {
        return getSaldo().multiply(tipo.getAliquotaImposto())
                .setScale(2, RoundingMode.HALF_EVEN);
    }

    public TipoInvestimento getTipo() {
        return tipo;
    }

    public BigDecimal getTaxaAdministracao() {
        return taxaAdministracao;
    }

    @Override
    public String toString() {
        return "ContaInvestimento{"
                + "numero='" + getNumero() + '\''
                + ", saldo=" + getSaldo()
                + ", tipo=" + tipo
                + ", taxaAdministracao=" + taxaAdministracao
                + ", titular=" + getTitular().getNome()
                + '}';
    }
}
