package com.banco.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.banco.service.BancoService;
import com.banco.domain.TipoInvestimento;

import java.util.Arrays;

@Controller
public class WebController {

    @Autowired
    private BancoService bancoService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("clientes", bancoService.listarClientes());
        model.addAttribute("contas", bancoService.listarContas());
        return "index";
    }

    @GetMapping("/clientes")
    public String listarClientes(Model model) {
        model.addAttribute("clientes", bancoService.listarClientes());
        return "clientes";
    }

    @GetMapping("/clientes/{cpf}")
    public String detalheCliente(@PathVariable String cpf, Model model) {
        model.addAttribute("cliente", bancoService.buscarClientePorCpf(cpf));
        model.addAttribute("contas", bancoService.buscarContasPorCliente(cpf));
        return "cliente-detalhe";
    }

    @GetMapping("/contas")
    public String listarContas(Model model) {
        model.addAttribute("contas", bancoService.listarContas());
        return "contas";
    }

    @GetMapping("/contas/{numero}")
    public String detalheConta(@PathVariable String numero, Model model) {
        model.addAttribute("conta", bancoService.buscarContaPorNumero(numero));
        return "conta-detalhe";
    }

    @GetMapping("/novo-cliente")
    public String novoClienteForm() {
        return "novo-cliente";
    }

    @GetMapping("/nova-conta")
    public String novaContaForm(Model model) {
        model.addAttribute("clientes", bancoService.listarClientes());
        model.addAttribute("tiposInvestimento", Arrays.asList(TipoInvestimento.values()));
        return "nova-conta";
    }
}
