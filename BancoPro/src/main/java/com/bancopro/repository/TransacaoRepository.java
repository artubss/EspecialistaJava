package com.bancopro.repository;

import com.bancopro.model.Conta;
import com.bancopro.model.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

    List<Transacao> findByConta(Conta conta);

    List<Transacao> findByContaAndDataHoraBetween(Conta conta, LocalDateTime inicio, LocalDateTime fim);
}
