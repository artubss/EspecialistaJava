package com.bancopro.repository;

import com.bancopro.model.Cliente;
import com.bancopro.model.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Long> {

    List<Conta> findByCliente(Cliente cliente);

    Optional<Conta> findByNumero(String numero);
}
