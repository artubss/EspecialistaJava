package com.bancopro.controller;

import com.bancopro.model.Conta;
import com.bancopro.service.ContaService;
import com.bancopro.service.TransacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;

@Controller
@RequestMapping("/transacoes")
public class TransacaoController {

    private final TransacaoService transacaoService;
    private final ContaService contaService;

    @Autowired
    public TransacaoController(TransacaoService transacaoService, ContaService contaService) {
        this.transacaoService = transacaoService;
        this.contaService = contaService;
    }

    @GetMapping("/conta/{contaId}")
    public String listarTransacoes(@PathVariable Long contaId, Model model) {
        model.addAttribute("conta", contaService.buscarPorId(contaId));
        model.addAttribute("transacoes", transacaoService.listarTransacoesPorConta(contaId));
        return "transacoes/listar";
    }

    @GetMapping("/deposito/{contaId}")
    public String depositoForm(@PathVariable Long contaId, Model model) {
        model.addAttribute("conta", contaService.buscarPorId(contaId));
        model.addAttribute("operacao", "depósito");
        return "transacoes/deposito-form";
    }

    @PostMapping("/depositar")
    public String depositar(@RequestParam Long contaId, 
                           @RequestParam BigDecimal valor, 
                           @RequestParam String descricao,
                           RedirectAttributes redirectAttributes) {
        try {
            transacaoService.depositar(contaId, valor, descricao);
            redirectAttributes.addFlashAttribute("mensagem", "Depósito realizado com sucesso!");
            return "redirect:/transacoes/conta/" + contaId;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
            return "redirect:/transacoes/deposito/" + contaId;
        }
    }

    @GetMapping("/saque/{contaId}")
    public String saqueForm(@PathVariable Long contaId, Model model) {
        model.addAttribute("conta", contaService.buscarPorId(contaId));
        model.addAttribute("operacao", "saque");
        return "transacoes/saque-form";
    }

    @PostMapping("/sacar")
    public String sacar(@RequestParam Long contaId, 
                        @RequestParam BigDecimal valor, 
                        @RequestParam String descricao,
                        RedirectAttributes redirectAttributes) {
        try {
            transacaoService.sacar(contaId, valor, descricao);
            redirectAttributes.addFlashAttribute("mensagem", "Saque realizado com sucesso!");
            return "redirect:/transacoes/conta/" + contaId;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
            return "redirect:/transacoes/saque/" + contaId;
        }
    }

    @GetMapping("/transferencia/{contaId}")
    public String transferenciaForm(@PathVariable Long contaId, Model model) {
        model.addAttribute("conta", contaService.buscarPorId(contaId));
        model.addAttribute("contas", contaService.listarTodas());
        return "transacoes/transferencia-form";
    }

    @PostMapping("/transferir")
    public String transferir(@RequestParam Long contaOrigemId, 
                            @RequestParam Long contaDestinoId, 
                            @RequestParam BigDecimal valor, 
                            @RequestParam String descricao,
                            RedirectAttributes redirectAttributes) {
        try {
            transacaoService.transferir(contaOrigemId, contaDestinoId, valor, descricao);
            redirectAttributes.addFlashAttribute("mensagem", "Transferência realizada com sucesso!");
            return "redirect:/transacoes/conta/" + contaOrigemId;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
            return "redirect:/transacoes/transferencia/" + contaOrigemId;
        }
    }
}
