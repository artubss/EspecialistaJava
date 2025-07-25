package com.banco.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.banco.domain.Cliente;
import com.banco.domain.Conta;
import com.banco.domain.ContaCorrente;
import com.banco.domain.ContaInvestimento;
import com.banco.domain.ContaPoupanca;
import com.banco.domain.TipoInvestimento;
import com.banco.exception.SaldoInsuficienteException;

public class BancoServiceTest {

    private BancoService bancoService;

    @BeforeEach
    public void setUp() {
        bancoService = new BancoService();
    }

    @Test
    public void testListarClientes() {
        List<Cliente> clientes = bancoService.listarClientes();
        assertNotNull(clientes, "A lista de clientes não deve ser nula");
        assertEquals(2, clientes.size(), "A lista deve conter 2 clientes");
    }

    @Test
    public void testBuscarClientePorCpf() {
        Cliente cliente = bancoService.buscarClientePorCpf("123.456.789-00");
        assertNotNull(cliente);
        assertEquals("João da Silva", cliente.getNome());
    }

    @Test
    public void testListarContas() {
        List<Conta> contas = bancoService.listarContas();
        assertNotNull(contas);
        assertEquals(5, contas.size());
    }

    @Test
    public void testBuscarContaPorNumero() {
        Conta conta = bancoService.buscarContaPorNumero("1111");
        assertNotNull(conta);
        assertTrue(conta instanceof ContaCorrente);
    }

    @Test
    public void testDepositar() {
        Conta conta = bancoService.buscarContaPorNumero("1111");
        BigDecimal saldoAnterior = conta.getSaldo();
        BigDecimal valorDeposito = new BigDecimal("500.00");

        bancoService.depositar("1111", valorDeposito);

        assertEquals(saldoAnterior.add(valorDeposito), conta.getSaldo());
    }

    @Test
    public void testSacar() {
        Conta conta = bancoService.buscarContaPorNumero("1111");
        BigDecimal saldoAnterior = conta.getSaldo();
        BigDecimal valorSaque = new BigDecimal("100.00");

        bancoService.sacar("1111", valorSaque);

        assertEquals(saldoAnterior.subtract(valorSaque), conta.getSaldo());
    }

    @Test
    public void testSacarComSaldoInsuficiente() {
        Conta conta = bancoService.buscarContaPorNumero("2222"); // Conta poupança
        BigDecimal valorSaque = new BigDecimal("10000.00"); // Valor maior que o saldo

        assertThrows(SaldoInsuficienteException.class, () -> {
            bancoService.sacar("2222", valorSaque);
        });
    }

    @Test
    public void testCalcularRendimentoPoupanca() {
        ContaPoupanca conta = (ContaPoupanca) bancoService.buscarContaPorNumero("2222");
        BigDecimal saldoAnterior = conta.getSaldo();
        BigDecimal taxaRendimento = conta.getTaxaRendimento();

        bancoService.calcularRendimentoPoupanca("2222");

        BigDecimal rendimentoEsperado = saldoAnterior.multiply(taxaRendimento);
        BigDecimal saldoEsperado = saldoAnterior.add(rendimentoEsperado);

        assertEquals(saldoEsperado.setScale(2), conta.getSaldo().setScale(2));
    }

    @Test
    public void testCalcularImpostoInvestimento() {
        ContaInvestimento conta = (ContaInvestimento) bancoService.buscarContaPorNumero("3333");
        BigDecimal saldo = conta.getSaldo();
        TipoInvestimento tipo = conta.getTipo();

        BigDecimal impostoEsperado = saldo.multiply(tipo.getAliquotaImposto()).setScale(2);
        BigDecimal impostoCalculado = bancoService.calcularImpostoInvestimento("3333");

        assertEquals(impostoEsperado, impostoCalculado);
    }

    @Test
    public void testCriarCliente() {
        String nome = "Teste da Silva";
        String cpf = "111.222.333-44";
        LocalDate dataNascimento = LocalDate.of(1995, 5, 15);

        Cliente cliente = bancoService.criarCliente(nome, cpf, dataNascimento);

        assertNotNull(cliente);
        assertEquals(nome, cliente.getNome());
        assertEquals(cpf, cliente.getCpf());
        assertEquals(dataNascimento, cliente.getDataNascimento());

        Cliente clienteRecuperado = bancoService.buscarClientePorCpf(cpf);
        assertNotNull(clienteRecuperado);
        assertEquals(cliente, clienteRecuperado);
    }

    @Test
    public void testCriarContaCorrente() {
        String numero = "9999";
        String cpfCliente = "123.456.789-00";
        BigDecimal limite = new BigDecimal("2000.00");

        ContaCorrente conta = bancoService.criarContaCorrente(numero, cpfCliente, limite);

        assertNotNull(conta);
        assertEquals(numero, conta.getNumero());
        assertEquals(limite, conta.getLimiteChequeEspecial());
        assertEquals(cpfCliente, conta.getTitular().getCpf());

        Conta contaRecuperada = bancoService.buscarContaPorNumero(numero);
        assertNotNull(contaRecuperada);
        assertTrue(contaRecuperada instanceof ContaCorrente);
    }
}
