package com.banco.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

import com.banco.exception.SaldoInsuficienteException;

public abstract class Conta {

    private String numero;
    private BigDecimal saldo;
    private Cliente titular;
    private LocalDateTime dataCriacao;
    private boolean ativa;

    public Conta(String numero, Cliente titular) {
        this.numero = Objects.requireNonNull(numero, "Número não pode ser nulo");
        this.titular = Objects.requireNonNull(titular, "Titular não pode ser nulo");
        this.saldo = BigDecimal.ZERO;
        this.dataCriacao = LocalDateTime.now();
        this.ativa = true;
    }

    public void depositar(BigDecimal valor) {
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor do depósito deve ser maior que zero");
        }
        this.saldo = this.saldo.add(valor);
    }

    public void sacar(BigDecimal valor) {
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor do saque deve ser maior que zero");
        }
        if (valor.compareTo(saldo) > 0) {
            throw new SaldoInsuficienteException("Saldo insuficiente para saque");
        }
        this.saldo = this.saldo.subtract(valor);
    }

    public abstract void calcularTarifaMensal();

    public String getNumero() {
        return numero;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    protected void setSaldo(BigDecimal saldo) {
        this.saldo = Objects.requireNonNull(saldo, "Saldo não pode ser nulo");
    }

    public Cliente getTitular() {
        return titular;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public boolean isAtiva() {
        return ativa;
    }

    public void setAtiva(boolean ativa) {
        this.ativa = ativa;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Conta conta = (Conta) o;
        return Objects.equals(numero, conta.numero);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numero);
    }

    @Override
    public String toString() {
        return "Conta{"
                + "numero='" + numero + '\''
                + ", saldo=" + saldo
                + ", titular=" + titular
                + ", ativa=" + ativa
                + '}';
    }
}
