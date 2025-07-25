package com.banco.domain;

import java.math.BigDecimal;

public enum TipoInvestimento {
    RENDA_FIXA("RF", new BigDecimal("0.015"), new BigDecimal("0.001")), // 1.5% IR, 0.1% taxa adm
    RENDA_VARIAVEL("RV", new BigDecimal("0.175"), new BigDecimal("0.002")), // 17.5% IR, 0.2% taxa adm
    TESOURO_DIRETO("TD", new BigDecimal("0.225"), new BigDecimal("0.0005")); // 22.5% IR, 0.05% taxa adm

    private final String codigo;
    private final BigDecimal aliquotaImposto;
    private final BigDecimal taxaAdministracao;

    TipoInvestimento(String codigo, BigDecimal aliquotaImposto, BigDecimal taxaAdministracao) {
        this.codigo = codigo;
        this.aliquotaImposto = aliquotaImposto;
        this.taxaAdministracao = taxaAdministracao;
    }

    public String getCodigo() {
        return codigo;
    }

    public BigDecimal getAliquotaImposto() {
        return aliquotaImposto;
    }

    public BigDecimal getTaxaAdministracao() {
        return taxaAdministracao;
    }
}
