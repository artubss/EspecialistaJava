package com.bancopro.service;

import com.bancopro.model.Conta;
import com.bancopro.model.Transacao;
import com.bancopro.repository.ContaRepository;
import com.bancopro.repository.TransacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransacaoService {

    private final TransacaoRepository transacaoRepository;
    private final ContaRepository contaRepository;
    private final ContaService contaService;

    @Autowired
    public TransacaoService(TransacaoRepository transacaoRepository, ContaRepository contaRepository, ContaService contaService) {
        this.transacaoRepository = transacaoRepository;
        this.contaRepository = contaRepository;
        this.contaService = contaService;
    }

    public List<Transacao> listarTransacoesPorConta(Long contaId) {
        Conta conta = contaService.buscarPorId(contaId);
        return transacaoRepository.findByConta(conta);
    }

    @Transactional
    public Transacao depositar(Long contaId, BigDecimal valor, String descricao) {
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor do depósito deve ser positivo");
        }

        Conta conta = contaService.buscarPorId(contaId);
        conta.setSaldo(conta.getSaldo().add(valor));
        contaRepository.save(conta);

        Transacao transacao = new Transacao();
        transacao.setTipo(Transacao.TipoTransacao.DEPOSITO);
        transacao.setValor(valor);
        transacao.setDataHora(LocalDateTime.now());
        transacao.setDescricao(descricao);
        transacao.setConta(conta);

        return transacaoRepository.save(transacao);
    }

    @Transactional
    public Transacao sacar(Long contaId, BigDecimal valor, String descricao) {
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor do saque deve ser positivo");
        }

        Conta conta = contaService.buscarPorId(contaId);
        if (conta.getSaldo().compareTo(valor) < 0) {
            throw new IllegalStateException("Saldo insuficiente");
        }

        conta.setSaldo(conta.getSaldo().subtract(valor));
        contaRepository.save(conta);

        Transacao transacao = new Transacao();
        transacao.setTipo(Transacao.TipoTransacao.SAQUE);
        transacao.setValor(valor);
        transacao.setDataHora(LocalDateTime.now());
        transacao.setDescricao(descricao);
        transacao.setConta(conta);

        return transacaoRepository.save(transacao);
    }

    @Transactional
    public Transacao transferir(Long contaOrigemId, Long contaDestinoId, BigDecimal valor, String descricao) {
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor da transferência deve ser positivo");
        }

        Conta contaOrigem = contaService.buscarPorId(contaOrigemId);
        Conta contaDestino = contaService.buscarPorId(contaDestinoId);

        if (contaOrigem.getSaldo().compareTo(valor) < 0) {
            throw new IllegalStateException("Saldo insuficiente");
        }

        contaOrigem.setSaldo(contaOrigem.getSaldo().subtract(valor));
        contaDestino.setSaldo(contaDestino.getSaldo().add(valor));

        contaRepository.save(contaOrigem);
        contaRepository.save(contaDestino);

        Transacao transacao = new Transacao();
        transacao.setTipo(Transacao.TipoTransacao.TRANSFERENCIA);
        transacao.setValor(valor);
        transacao.setDataHora(LocalDateTime.now());
        transacao.setDescricao(descricao);
        transacao.setConta(contaOrigem);
        transacao.setContaDestino(contaDestino);

        return transacaoRepository.save(transacao);
    }
}
