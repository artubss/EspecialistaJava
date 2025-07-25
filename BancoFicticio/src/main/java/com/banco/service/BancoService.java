package com.banco.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.banco.domain.Cliente;
import com.banco.domain.Conta;
import com.banco.domain.ContaCorrente;
import com.banco.domain.ContaInvestimento;
import com.banco.domain.ContaPoupanca;
import com.banco.domain.TipoInvestimento;
import com.banco.domain.Tributavel;
import com.banco.exception.OperacaoInvalidaException;
import com.banco.exception.RecursoNaoEncontradoException;

@Service
public class BancoService {

    private final Map<String, Cliente> clientes = new HashMap<>();
    private final Map<String, Conta> contas = new HashMap<>();

    // Métodos para gerenciar clientes
    public Cliente criarCliente(String nome, String cpf, LocalDate dataNascimento) {
        if (clientes.containsKey(cpf)) {
            throw new OperacaoInvalidaException("Cliente com CPF " + cpf + " já existe");
        }

        Cliente cliente = new Cliente(nome, cpf, dataNascimento);
        clientes.put(cpf, cliente);
        return cliente;
    }

    public List<Cliente> listarClientes() {
        return new ArrayList<>(clientes.values());
    }

    public Cliente buscarClientePorCpf(String cpf) {
        Cliente cliente = clientes.get(cpf);
        if (cliente == null) {
            throw new RecursoNaoEncontradoException("Cliente com CPF " + cpf + " não encontrado");
        }
        return cliente;
    }

    // Métodos para gerenciar contas
    public ContaCorrente criarContaCorrente(String numero, String cpfCliente, BigDecimal limiteChequeEspecial) {
        Cliente cliente = buscarClientePorCpf(cpfCliente);

        if (contas.containsKey(numero)) {
            throw new OperacaoInvalidaException("Conta com número " + numero + " já existe");
        }

        ContaCorrente conta = new ContaCorrente(numero, cliente, limiteChequeEspecial);
        contas.put(numero, conta);
        cliente.adicionarConta(conta);
        return conta;
    }

    public ContaPoupanca criarContaPoupanca(String numero, String cpfCliente) {
        Cliente cliente = buscarClientePorCpf(cpfCliente);

        if (contas.containsKey(numero)) {
            throw new OperacaoInvalidaException("Conta com número " + numero + " já existe");
        }

        ContaPoupanca conta = new ContaPoupanca(numero, cliente);
        contas.put(numero, conta);
        cliente.adicionarConta(conta);
        return conta;
    }

    public ContaInvestimento criarContaInvestimento(String numero, String cpfCliente, TipoInvestimento tipo) {
        Cliente cliente = buscarClientePorCpf(cpfCliente);

        if (contas.containsKey(numero)) {
            throw new OperacaoInvalidaException("Conta com número " + numero + " já existe");
        }

        ContaInvestimento conta = new ContaInvestimento(numero, cliente, tipo);
        contas.put(numero, conta);
        cliente.adicionarConta(conta);
        return conta;
    }

    public List<Conta> listarContas() {
        return new ArrayList<>(contas.values());
    }

    public Conta buscarContaPorNumero(String numero) {
        Conta conta = contas.get(numero);
        if (conta == null) {
            throw new RecursoNaoEncontradoException("Conta com número " + numero + " não encontrada");
        }
        return conta;
    }

    public List<Conta> buscarContasPorCliente(String cpf) {
        Cliente cliente = buscarClientePorCpf(cpf);
        return cliente.getContas();
    }

    // Métodos para operações bancárias
    public void depositar(String numeroConta, BigDecimal valor) {
        Conta conta = buscarContaPorNumero(numeroConta);
        conta.depositar(valor);
    }

    public void sacar(String numeroConta, BigDecimal valor) {
        Conta conta = buscarContaPorNumero(numeroConta);
        conta.sacar(valor);
    }

    public void calcularRendimentoPoupanca(String numeroConta) {
        Conta conta = buscarContaPorNumero(numeroConta);
        if (!(conta instanceof ContaPoupanca)) {
            throw new OperacaoInvalidaException("Apenas contas poupança podem calcular rendimentos");
        }
        ((ContaPoupanca) conta).calcularRendimento();
    }

    public BigDecimal calcularImpostoInvestimento(String numeroConta) {
        Conta conta = buscarContaPorNumero(numeroConta);
        if (!(conta instanceof Tributavel)) {
            throw new OperacaoInvalidaException("Esta conta não é tributável");
        }
        return ((Tributavel) conta).calcularTributo();
    }

    public void investir(String numeroConta, BigDecimal valor) {
        Conta conta = buscarContaPorNumero(numeroConta);
        if (!(conta instanceof ContaInvestimento)) {
            throw new OperacaoInvalidaException("Apenas contas de investimento podem realizar investimentos");
        }
        ((ContaInvestimento) conta).investir(valor);
    }

    public void resgatar(String numeroConta, BigDecimal valor) {
        Conta conta = buscarContaPorNumero(numeroConta);
        if (!(conta instanceof ContaInvestimento)) {
            throw new OperacaoInvalidaException("Apenas contas de investimento podem realizar resgates");
        }
        ((ContaInvestimento) conta).resgatar(valor);
    }

    public void calcularTarifasMensais() {
        contas.values().forEach(Conta::calcularTarifaMensal);
    }

    // Métodos auxiliares
    public void encerrarConta(String numeroConta) {
        Conta conta = buscarContaPorNumero(numeroConta);
        if (conta.getSaldo().compareTo(BigDecimal.ZERO) > 0) {
            throw new OperacaoInvalidaException("Não é possível encerrar uma conta com saldo positivo");
        }
        conta.setAtiva(false);
    }

    public List<Conta> buscarContasAtivas() {
        return contas.values().stream()
                .filter(Conta::isAtiva)
                .collect(Collectors.toList());
    }
}
package com.banco.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.banco.domain.Cliente;
import com.banco.domain.Conta;
import com.banco.domain.ContaCorrente;
import com.banco.domain.ContaInvestimento;
import com.banco.domain.ContaPoupanca;
import com.banco.domain.TipoInvestimento;
import com.banco.domain.Tributavel;
import com.banco.exception.OperacaoInvalidaException;
import com.banco.exception.RecursoNaoEncontradoException;

@Service
public class BancoService {

    private final Map<String, Cliente> clientes = new HashMap<>();
    private final Map<String, Conta> contas = new HashMap<>();

    // Métodos para gerenciar clientes
    public Cliente criarCliente(String nome, String cpf, LocalDate dataNascimento) {
        if (clientes.containsKey(cpf)) {
            throw new OperacaoInvalidaException("Cliente com CPF " + cpf + " já existe");
        }

        Cliente cliente = new Cliente(nome, cpf, dataNascimento);
        clientes.put(cpf, cliente);
        return cliente;
    }

    public List<Cliente> listarClientes() {
        return new ArrayList<>(clientes.values());
    }

    public Cliente buscarClientePorCpf(String cpf) {
        Cliente cliente = clientes.get(cpf);
        if (cliente == null) {
            throw new RecursoNaoEncontradoException("Cliente com CPF " + cpf + " não encontrado");
        }
        return cliente;
    }

    // Métodos para gerenciar contas
    public ContaCorrente criarContaCorrente(String numero, String cpfCliente, BigDecimal limiteChequeEspecial) {
        Cliente cliente = buscarClientePorCpf(cpfCliente);

        if (contas.containsKey(numero)) {
            throw new OperacaoInvalidaException("Conta com número " + numero + " já existe");
        }

        ContaCorrente conta = new ContaCorrente(numero, cliente, limiteChequeEspecial);
        contas.put(numero, conta);
        cliente.adicionarConta(conta);
        return conta;
    }

    public ContaPoupanca criarContaPoupanca(String numero, String cpfCliente) {
        Cliente cliente = buscarClientePorCpf(cpfCliente);

        if (contas.containsKey(numero)) {
            throw new OperacaoInvalidaException("Conta com número " + numero + " já existe");
        }

        ContaPoupanca conta = new ContaPoupanca(numero, cliente);
        contas.put(numero, conta);
        cliente.adicionarConta(conta);
        return conta;
    }

    public ContaInvestimento criarContaInvestimento(String numero, String cpfCliente, TipoInvestimento tipo) {
        Cliente cliente = buscarClientePorCpf(cpfCliente);

        if (contas.containsKey(numero)) {
            throw new OperacaoInvalidaException("Conta com número " + numero + " já existe");
        }

        ContaInvestimento conta = new ContaInvestimento(numero, cliente, tipo);
        contas.put(numero, conta);
        cliente.adicionarConta(conta);
        return conta;
    }

    public List<Conta> listarContas() {
        return new ArrayList<>(contas.values());
    }

    public Conta buscarContaPorNumero(String numero) {
        Conta conta = contas.get(numero);
        if (conta == null) {
            throw new RecursoNaoEncontradoException("Conta com número " + numero + " não encontrada");
        }
        return conta;
    }

    public List<Conta> buscarContasPorCliente(String cpf) {
        Cliente cliente = buscarClientePorCpf(cpf);
        return cliente.getContas();
    }

    // Métodos para operações bancárias
    public void depositar(String numeroConta, BigDecimal valor) {
        Conta conta = buscarContaPorNumero(numeroConta);
        conta.depositar(valor);
    }

    public void sacar(String numeroConta, BigDecimal valor) {
        Conta conta = buscarContaPorNumero(numeroConta);
        conta.sacar(valor);
    }

    public void calcularRendimentoPoupanca(String numeroConta) {
        Conta conta = buscarContaPorNumero(numeroConta);
        if (!(conta instanceof ContaPoupanca)) {
            throw new OperacaoInvalidaException("Apenas contas poupança podem calcular rendimentos");
        }
        ((ContaPoupanca) conta).calcularRendimento();
    }

    public BigDecimal calcularImpostoInvestimento(String numeroConta) {
        Conta conta = buscarContaPorNumero(numeroConta);
        if (!(conta instanceof Tributavel)) {
            throw new OperacaoInvalidaException("Esta conta não é tributável");
        }
        return ((Tributavel) conta).calcularTributo();
    }

    public void investir(String numeroConta, BigDecimal valor) {
        Conta conta = buscarContaPorNumero(numeroConta);
        if (!(conta instanceof ContaInvestimento)) {
            throw new OperacaoInvalidaException("Apenas contas de investimento podem realizar investimentos");
        }
        ((ContaInvestimento) conta).investir(valor);
    }

    public void resgatar(String numeroConta, BigDecimal valor) {
        Conta conta = buscarContaPorNumero(numeroConta);
        if (!(conta instanceof ContaInvestimento)) {
            throw new OperacaoInvalidaException("Apenas contas de investimento podem realizar resgates");
        }
        ((ContaInvestimento) conta).resgatar(valor);
    }

    public void calcularTarifasMensais() {
        contas.values().forEach(Conta::calcularTarifaMensal);
    }

    // Métodos auxiliares
    public void encerrarConta(String numeroConta) {
        Conta conta = buscarContaPorNumero(numeroConta);
        if (conta.getSaldo().compareTo(BigDecimal.ZERO) > 0) {
            throw new OperacaoInvalidaException("Não é possível encerrar uma conta com saldo positivo");
        }
        conta.setAtiva(false);
    }

    public List<Conta> buscarContasAtivas() {
        return contas.values().stream()
                .filter(Conta::isAtiva)
                .collect(Collectors.toList());
    }

    // Método para inicialização de dados de exemplo
    public void inicializarDadosExemplo() {
        // Criar alguns clientes
        Cliente joao = criarCliente("João Silva", "12345678900", LocalDate.of(1980, 5, 15));
        Cliente maria = criarCliente("Maria Oliveira", "98765432100", LocalDate.of(1990, 10, 20));
        Cliente pedro = criarCliente("Pedro Santos", "45678912300", LocalDate.of(1985, 3, 8));

        // Criar algumas contas
        criarContaCorrente("CC-001", joao.getCpf(), new BigDecimal("1000.00"));
        criarContaPoupanca("CP-001", joao.getCpf());
        criarContaCorrente("CC-002", maria.getCpf(), new BigDecimal("2000.00"));
        criarContaInvestimento("CI-001", maria.getCpf(), TipoInvestimento.RENDA_FIXA);
        criarContaPoupanca("CP-002", pedro.getCpf());
        criarContaInvestimento("CI-002", pedro.getCpf(), TipoInvestimento.TESOURO_DIRETO);

        // Realizar algumas operações
        depositar("CC-001", new BigDecimal("500.00"));
        depositar("CP-001", new BigDecimal("1000.00"));
        depositar("CC-002", new BigDecimal("1500.00"));
        depositar("CI-001", new BigDecimal("3000.00"));
        depositar("CP-002", new BigDecimal("2000.00"));
        depositar("CI-002", new BigDecimal("5000.00"));

        // Investir em contas de investimento
        investir("CI-001", new BigDecimal("2000.00"));
        investir("CI-002", new BigDecimal("4000.00"));
    }
}
}
