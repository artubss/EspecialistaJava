package com.banco;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.banco.domain.*;
import com.banco.exception.SaldoInsuficienteException;

public class Main {

    public static void main(String[] args) {
        try {
            // Criando um cliente
            Cliente cliente = new Cliente(
                    "João da Silva",
                    "123.456.789-00",
                    LocalDate.of(1990, 1, 1)
            );

            // Criando diferentes tipos de conta
            ContaCorrente cc = new ContaCorrente("1111", cliente, new BigDecimal("1000.00"));
            ContaPoupanca cp = new ContaPoupanca("2222", cliente);
            ContaInvestimento ci = new ContaInvestimento("3333", cliente, TipoInvestimento.RENDA_FIXA);

            // Adicionando as contas ao cliente
            cliente.adicionarConta(cc);
            cliente.adicionarConta(cp);
            cliente.adicionarConta(ci);

            // Realizando operações
            cc.depositar(new BigDecimal("5000.00"));
            cc.sacar(new BigDecimal("2000.00"));

            cp.depositar(new BigDecimal("1000.00"));
            cp.calcularRendimento();

            ci.investir(new BigDecimal("10000.00"));
            BigDecimal imposto = ci.calcularImposto();

            // Calculando tarifas mensais
            List<Conta> contas = cliente.getContas();
            contas.forEach(Conta::calcularTarifaMensal);

            // Exibindo informações
            System.out.println("Cliente: " + cliente.getNome());
            System.out.println("CPF: " + cliente.getCpf());
            System.out.println("\nContas:");
            contas.forEach(System.out::println);
            System.out.println("\nImposto sobre investimentos: " + imposto);

        } catch (SaldoInsuficienteException e) {
            System.err.println("Erro: " + e.getMessage());
            System.err.println("Saldo atual: " + e.getSaldoAtual());
            System.err.println("Valor solicitado: " + e.getValorSolicitado());

        } catch (IllegalArgumentException e) {
            System.err.println("Erro de validação: " + e.getMessage());

        } catch (Exception e) {
            System.err.println("Erro inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
