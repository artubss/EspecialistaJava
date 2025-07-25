package com.banco.domain;
package com.banco.domain;
package com.banco.domain;

public enum TipoInvestimento {
    RENDA_FIXA(0.007), // 0.7% ao mês
    RENDA_VARIAVEL(0.015), // 1.5% ao mês
    TESOURO_DIRETO(0.008); // 0.8% ao mês

    private final double taxaRendimento;

    TipoInvestimento(double taxaRendimento) {
        this.taxaRendimento = taxaRendimento;
    }

    public double getTaxaRendimento() {
        return taxaRendimento;
    }
}
public enum TipoInvestimento {
    RENDA_FIXA(0.007), // 0.7% ao mês
    RENDA_VARIAVEL(0.015), // 1.5% ao mês
    TESOURO_DIRETO(0.008); // 0.8% ao mês

    private final double taxaRendimento;

    TipoInvestimento(double taxaRendimento) {
        this.taxaRendimento = taxaRendimento;
    }

    public double getTaxaRendimento() {
        return taxaRendimento;
    }
}
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
