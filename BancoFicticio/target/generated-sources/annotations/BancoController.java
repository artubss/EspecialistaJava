package com.banco.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.banco.domain.Cliente;
import com.banco.domain.Conta;
import com.banco.domain.ContaCorrente;
import com.banco.domain.ContaInvestimento;
import com.banco.domain.ContaPoupanca;
import com.banco.domain.TipoInvestimento;
import com.banco.exception.SaldoInsuficienteException;
import com.banco.service.BancoService;

@RestController
@RequestMapping("/api/banco")
public class BancoController {

    @Autowired
    private BancoService bancoService;

    // Endpoints para clientes
    @GetMapping("/clientes")
    public List<Cliente> listarClientes() {
        return bancoService.listarClientes();
    }

    @GetMapping("/clientes/{cpf}")
    public ResponseEntity<Cliente> buscarCliente(@PathVariable String cpf) {
        Cliente cliente = bancoService.buscarClientePorCpf(cpf);
        if (cliente != null) {
            return ResponseEntity.ok(cliente);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/clientes")
    public Cliente criarCliente(
            @RequestParam String nome,
            @RequestParam String cpf,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataNascimento) {
        return bancoService.criarCliente(nome, cpf, dataNascimento);
    }

    // Endpoints para contas
    @GetMapping("/contas")
    public List<Conta> listarContas() {
        return bancoService.listarContas();
    }

    @GetMapping("/contas/{numero}")
    public ResponseEntity<Conta> buscarConta(@PathVariable String numero) {
        Conta conta = bancoService.buscarContaPorNumero(numero);
        if (conta != null) {
            return ResponseEntity.ok(conta);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/clientes/{cpf}/contas")
    public List<Conta> listarContasCliente(@PathVariable String cpf) {
        return bancoService.buscarContasPorCliente(cpf);
    }

    @PostMapping("/contas/corrente")
    public ContaCorrente criarContaCorrente(
            @RequestParam String numero,
            @RequestParam String cpfCliente,
            @RequestParam(required = false, defaultValue = "0.0") BigDecimal limiteChequeEspecial) {
        return bancoService.criarContaCorrente(numero, cpfCliente, limiteChequeEspecial);
    }

    @PostMapping("/contas/poupanca")
    public ContaPoupanca criarContaPoupanca(
            @RequestParam String numero,
            @RequestParam String cpfCliente) {
        return bancoService.criarContaPoupanca(numero, cpfCliente);
    }

    @PostMapping("/contas/investimento")
    public ContaInvestimento criarContaInvestimento(
            @RequestParam String numero,
            @RequestParam String cpfCliente,
            @RequestParam TipoInvestimento tipo) {
        return bancoService.criarContaInvestimento(numero, cpfCliente, tipo);
    }

    // Endpoints para operações
    @PostMapping("/contas/{numero}/depositar")
    public ResponseEntity<String> depositar(
            @PathVariable String numero,
            @RequestParam BigDecimal valor) {
        try {
            bancoService.depositar(numero, valor);
            return ResponseEntity.ok("Depósito realizado com sucesso");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/contas/{numero}/sacar")
    public ResponseEntity<String> sacar(
            @PathVariable String numero,
            @RequestParam BigDecimal valor) {
        try {
            bancoService.sacar(numero, valor);
            return ResponseEntity.ok("Saque realizado com sucesso");
        } catch (SaldoInsuficienteException e) {
            return ResponseEntity.badRequest().body("Saldo insuficiente: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/contas/{numero}/calcular-rendimento")
    public ResponseEntity<String> calcularRendimento(@PathVariable String numero) {
        try {
            bancoService.calcularRendimentoPoupanca(numero);
            return ResponseEntity.ok("Rendimento calculado com sucesso");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/contas/{numero}/calcular-imposto")
    public ResponseEntity<?> calcularImposto(@PathVariable String numero) {
        try {
            BigDecimal imposto = bancoService.calcularImpostoInvestimento(numero);
            return ResponseEntity.ok(Map.of("imposto", imposto));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/contas/{numero}/investir")
    public ResponseEntity<String> investir(
            @PathVariable String numero,
            @RequestParam BigDecimal valor) {
        try {
            bancoService.investir(numero, valor);
            return ResponseEntity.ok("Investimento realizado com sucesso");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/contas/{numero}/resgatar")
    public ResponseEntity<String> resgatar(
            @PathVariable String numero,
            @RequestParam BigDecimal valor) {
        try {
            bancoService.resgatar(numero, valor);
            return ResponseEntity.ok("Resgate realizado com sucesso");
        } catch (SaldoInsuficienteException e) {
            return ResponseEntity.badRequest().body("Saldo insuficiente: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/calcular-tarifas")
    public ResponseEntity<String> calcularTarifas() {
        bancoService.calcularTarifasMensais();
        return ResponseEntity.ok("Tarifas mensais calculadas com sucesso");
    }
}
