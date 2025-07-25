package com.bancopro.service;

import com.bancopro.model.Cliente;
import com.bancopro.model.Conta;
import com.bancopro.repository.ContaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class ContaService {

    private final ContaRepository contaRepository;
    private final ClienteService clienteService;

    @Autowired
    public ContaService(ContaRepository contaRepository, ClienteService clienteService) {
        this.contaRepository = contaRepository;
        this.clienteService = clienteService;
    }

    public List<Conta> listarTodas() {
        return contaRepository.findAll();
    }

    public List<Conta> listarPorCliente(Long clienteId) {
        Cliente cliente = clienteService.buscarPorId(clienteId);
        return contaRepository.findByCliente(cliente);
    }

    public Conta buscarPorId(Long id) {
        return contaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada com ID: " + id));
    }

    public Conta buscarPorNumero(String numero) {
        return contaRepository.findByNumero(numero)
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada com número: " + numero));
    }

    public Conta criarConta(Long clienteId, Conta.TipoConta tipoConta) {
        Cliente cliente = clienteService.buscarPorId(clienteId);

        Conta novaConta = new Conta();
        novaConta.setNumero(gerarNumeroConta());
        novaConta.setTipoConta(tipoConta);
        novaConta.setSaldo(BigDecimal.ZERO);
        novaConta.setDataCriacao(LocalDateTime.now());
        novaConta.setCliente(cliente);

        return contaRepository.save(novaConta);
    }

    public void encerrarConta(Long contaId) {
        Conta conta = buscarPorId(contaId);
        if (conta.getSaldo().compareTo(BigDecimal.ZERO) != 0) {
            throw new IllegalStateException("Conta precisa ter saldo zero para ser encerrada");
        }
        contaRepository.deleteById(contaId);
    }

    private String gerarNumeroConta() {
        Random random = new Random();
        StringBuilder numeroConta = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            numeroConta.append(random.nextInt(10));
        }
        return numeroConta.toString();
    }
}
