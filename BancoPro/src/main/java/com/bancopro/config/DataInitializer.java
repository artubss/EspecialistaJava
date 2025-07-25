package com.bancopro.config;

import com.bancopro.model.Cliente;
import com.bancopro.model.Conta;
import com.bancopro.service.ClienteService;
import com.bancopro.service.ContaService;
import com.bancopro.service.TransacaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class DataInitializer {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ContaService contaService;

    @Autowired
    private TransacaoService transacaoService;

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            try {
                logger.info("Iniciando carregamento de dados de exemplo...");

                // Criar alguns clientes de exemplo
                Cliente cliente1 = new Cliente();
                cliente1.setNome("João Silva");
                cliente1.setCpf("12345678901");
                cliente1.setEmail("joao@email.com");
                cliente1.setTelefone("(11) 99999-8888");
                clienteService.salvar(cliente1);
                logger.info("Cliente João Silva criado com ID: {}", cliente1.getId());

                Cliente cliente2 = new Cliente();
                cliente2.setNome("Maria Oliveira");
                cliente2.setCpf("98765432101");
                cliente2.setEmail("maria@email.com");
                cliente2.setTelefone("(11) 97777-6666");
                clienteService.salvar(cliente2);
                logger.info("Cliente Maria Oliveira criado com ID: {}", cliente2.getId());

                Cliente cliente3 = new Cliente();
                cliente3.setNome("Carlos Santos");
                cliente3.setCpf("45678912301");
                cliente3.setEmail("carlos@email.com");
                cliente3.setTelefone("(11) 95555-4444");
                clienteService.salvar(cliente3);
                logger.info("Cliente Carlos Santos criado com ID: {}", cliente3.getId());

                // Criar contas para os clientes
                Conta contaJoao1 = contaService.criarConta(cliente1.getId(), Conta.TipoConta.CORRENTE);
                Conta contaJoao2 = contaService.criarConta(cliente1.getId(), Conta.TipoConta.POUPANCA);
                Conta contaMaria = contaService.criarConta(cliente2.getId(), Conta.TipoConta.CORRENTE);
                Conta contaCarlos = contaService.criarConta(cliente3.getId(), Conta.TipoConta.INVESTIMENTO);

                // Realizar algumas transações
                transacaoService.depositar(contaJoao1.getId(), new BigDecimal("1500.00"), "Depósito inicial");
                transacaoService.depositar(contaJoao2.getId(), new BigDecimal("2500.00"), "Depósito para poupança");
                transacaoService.depositar(contaMaria.getId(), new BigDecimal("3200.00"), "Depósito inicial");
                transacaoService.depositar(contaCarlos.getId(), new BigDecimal("5000.00"), "Depósito para investimento");

                transacaoService.sacar(contaJoao1.getId(), new BigDecimal("200.00"), "Saque para despesas");
                transacaoService.transferir(contaMaria.getId(), contaCarlos.getId(), new BigDecimal("500.00"), "Transferência para Carlos");

                logger.info("Dados de exemplo carregados com sucesso!");
            } catch (Exception e) {
                logger.error("Erro ao carregar dados de exemplo: {}", e.getMessage(), e);
            }
        };
    }
}
