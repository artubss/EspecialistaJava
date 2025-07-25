package com.bancopro.service;

import com.bancopro.model.Cliente;
import com.bancopro.repository.ClienteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    @Autowired
    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com ID: " + id));
    }

    public Cliente buscarPorCpf(String cpf) {
        return clienteRepository.findByCpf(cpf)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com CPF: " + cpf));
    }

    public Cliente salvar(Cliente cliente) {
        // Validação básica
        if (cliente.getId() == null && clienteRepository.existsByCpf(cliente.getCpf())) {
            throw new IllegalArgumentException("CPF já cadastrado");
        }
        return clienteRepository.save(cliente);
    }

    public void excluir(Long id) {
        Cliente cliente = buscarPorId(id);
        if (!cliente.getContas().isEmpty()) {
            throw new IllegalStateException("Cliente possui contas ativas e não pode ser excluído");
        }
        clienteRepository.deleteById(id);
    }
}
