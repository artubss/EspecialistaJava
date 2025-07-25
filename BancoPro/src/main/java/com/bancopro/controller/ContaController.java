package com.bancopro.controller;

import com.bancopro.model.Conta;
import com.bancopro.service.ClienteService;
import com.bancopro.service.ContaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/contas")
public class ContaController {

    private final ContaService contaService;
    private final ClienteService clienteService;

    @Autowired
    public ContaController(ContaService contaService, ClienteService clienteService) {
        this.contaService = contaService;
        this.clienteService = clienteService;
    }

    @GetMapping
    public String listarContas(Model model) {
        model.addAttribute("contas", contaService.listarTodas());
        return "contas/listar";
    }

    @GetMapping("/cliente/{clienteId}")
    public String listarContasCliente(@PathVariable Long clienteId, Model model) {
        model.addAttribute("cliente", clienteService.buscarPorId(clienteId));
        model.addAttribute("contas", contaService.listarPorCliente(clienteId));
        return "contas/listar-cliente";
    }

    @GetMapping("/nova/{clienteId}")
    public String novaContaForm(@PathVariable Long clienteId, Model model) {
        model.addAttribute("clienteId", clienteId);
        model.addAttribute("tiposConta", Conta.TipoConta.values());
        return "contas/form";
    }

    @PostMapping("/criar")
    public String criarConta(@RequestParam Long clienteId, 
                           @RequestParam Conta.TipoConta tipoConta,
                           RedirectAttributes redirectAttributes) {
        try {
            Conta novaConta = contaService.criarConta(clienteId, tipoConta);
            redirectAttributes.addFlashAttribute("mensagem", 
                    "Conta " + novaConta.getNumero() + " criada com sucesso!");
            return "redirect:/contas/cliente/" + clienteId;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
            return "redirect:/contas/nova/" + clienteId;
        }
    }

    @GetMapping("/detalhes/{id}")
    public String detalhesConta(@PathVariable Long id, Model model) {
        Conta conta = contaService.buscarPorId(id);
        model.addAttribute("conta", conta);
        return "contas/detalhes";
    }

    @GetMapping("/encerrar/{id}")
    public String encerrarConta(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Conta conta = contaService.buscarPorId(id);
            Long clienteId = conta.getCliente().getId();
            contaService.encerrarConta(id);
            redirectAttributes.addFlashAttribute("mensagem", "Conta encerrada com sucesso!");
            return "redirect:/contas/cliente/" + clienteId;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
            return "redirect:/contas/detalhes/" + id;
        }
    }
}
