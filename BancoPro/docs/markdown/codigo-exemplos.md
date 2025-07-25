# Exemplos de Código do BancoPro

Este documento apresenta exemplos de código das principais funcionalidades do BancoPro, demonstrando como as diferentes camadas da aplicação interagem entre si.

## 1. Camada de Controle (Controllers)

### 1.1 ClienteController

Responsável por gerenciar as requisições relacionadas aos clientes.

```java
@Controller
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    @Autowired
    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public String listarClientes(Model model) {
        model.addAttribute("clientes", clienteService.listarTodos());
        return "clientes/listar";
    }

    @GetMapping("/novo")
    public String novoClienteForm(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "clientes/form";
    }

    @PostMapping("/salvar")
    public String salvarCliente(@Valid @ModelAttribute("cliente") Cliente cliente, 
                              BindingResult result, 
                              RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "clientes/form";
        }

        try {
            clienteService.salvar(cliente);
            redirectAttributes.addFlashAttribute("mensagem", "Cliente salvo com sucesso!");
            return "redirect:/clientes";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
            return "redirect:/clientes/novo";
        }
    }
}
```

### 1.2 ContaController

Responsável por gerenciar as requisições relacionadas às contas bancárias.

```java
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
}
```

### 1.3 TransacaoController

Responsável por gerenciar as requisições relacionadas às transações financeiras.

```java
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
```

## 2. Camada de Serviço (Services)

### 2.1 ClienteService

Implementa a lógica de negócio relacionada aos clientes.

```java
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
```

### 2.2 ContaService

Implementa a lógica de negócio relacionada às contas bancárias.

```java
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
        return contaRepository.findByClienteId(clienteId);
    }

    public Conta buscarPorId(Long id) {
        return contaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada com ID: " + id));
    }

    public Conta criarConta(Long clienteId, Conta.TipoConta tipoConta) {
        Cliente cliente = clienteService.buscarPorId(clienteId);

        Conta novaConta = new Conta();
        novaConta.setCliente(cliente);
        novaConta.setTipoConta(tipoConta);
        novaConta.setSaldo(BigDecimal.ZERO);
        novaConta.setDataCriacao(LocalDateTime.now());
        novaConta.setNumero(gerarNumeroConta());

        return contaRepository.save(novaConta);
    }

    private String gerarNumeroConta() {
        // Lógica para gerar número único de conta
        return "C" + System.currentTimeMillis();
    }

    public void encerrarConta(Long id) {
        Conta conta = buscarPorId(id);
        if (conta.getSaldo().compareTo(BigDecimal.ZERO) != 0) {
            throw new IllegalStateException("Não é possível encerrar conta com saldo diferente de zero");
        }
        contaRepository.deleteById(id);
    }
}
```

### 2.3 TransacaoService

Implementa a lógica de negócio relacionada às transações financeiras.

```java
@Service
public class TransacaoService {

    private final TransacaoRepository transacaoRepository;
    private final ContaRepository contaRepository;
    private final ContaService contaService;

    @Autowired
    public TransacaoService(TransacaoRepository transacaoRepository, 
                           ContaRepository contaRepository,
                           ContaService contaService) {
        this.transacaoRepository = transacaoRepository;
        this.contaRepository = contaRepository;
        this.contaService = contaService;
    }

    public List<Transacao> listarTransacoesPorConta(Long contaId) {
        return transacaoRepository.findByContaIdOrderByDataHoraDesc(contaId);
    }

    @Transactional
    public Transacao depositar(Long contaId, BigDecimal valor, String descricao) {
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor do depósito deve ser maior que zero");
        }

        Conta conta = contaService.buscarPorId(contaId);
        conta.setSaldo(conta.getSaldo().add(valor));
        contaRepository.save(conta);

        Transacao transacao = new Transacao();
        transacao.setConta(conta);
        transacao.setValor(valor);
        transacao.setTipo(Transacao.TipoTransacao.DEPOSITO);
        transacao.setDescricao(descricao);
        transacao.setDataHora(LocalDateTime.now());

        return transacaoRepository.save(transacao);
    }

    @Transactional
    public Transacao sacar(Long contaId, BigDecimal valor, String descricao) {
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor do saque deve ser maior que zero");
        }

        Conta conta = contaService.buscarPorId(contaId);

        if (conta.getSaldo().compareTo(valor) < 0) {
            throw new IllegalStateException("Saldo insuficiente para realizar o saque");
        }

        conta.setSaldo(conta.getSaldo().subtract(valor));
        contaRepository.save(conta);

        Transacao transacao = new Transacao();
        transacao.setConta(conta);
        transacao.setValor(valor);
        transacao.setTipo(Transacao.TipoTransacao.SAQUE);
        transacao.setDescricao(descricao);
        transacao.setDataHora(LocalDateTime.now());

        return transacaoRepository.save(transacao);
    }

    @Transactional
    public void transferir(Long contaOrigemId, Long contaDestinoId, BigDecimal valor, String descricao) {
        if (contaOrigemId.equals(contaDestinoId)) {
            throw new IllegalArgumentException("Não é possível transferir para a mesma conta");
        }

        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor da transferência deve ser maior que zero");
        }

        Conta contaOrigem = contaService.buscarPorId(contaOrigemId);
        Conta contaDestino = contaService.buscarPorId(contaDestinoId);

        if (contaOrigem.getSaldo().compareTo(valor) < 0) {
            throw new IllegalStateException("Saldo insuficiente para realizar a transferência");
        }

        // Atualiza saldos
        contaOrigem.setSaldo(contaOrigem.getSaldo().subtract(valor));
        contaDestino.setSaldo(contaDestino.getSaldo().add(valor));

        contaRepository.save(contaOrigem);
        contaRepository.save(contaDestino);

        // Registra transação na conta de origem
        Transacao transacaoOrigem = new Transacao();
        transacaoOrigem.setConta(contaOrigem);
        transacaoOrigem.setContaDestino(contaDestino);
        transacaoOrigem.setValor(valor);
        transacaoOrigem.setTipo(Transacao.TipoTransacao.TRANSFERENCIA);
        transacaoOrigem.setDescricao(descricao);
        transacaoOrigem.setDataHora(LocalDateTime.now());

        transacaoRepository.save(transacaoOrigem);
    }
}
```

## 3. Camada de Repositório (Repositories)

### 3.1 ClienteRepository

Interface para acesso aos dados de clientes.

```java
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByCpf(String cpf);
    boolean existsByCpf(String cpf);
}
```

### 3.2 ContaRepository

Interface para acesso aos dados de contas.

```java
@Repository
public interface ContaRepository extends JpaRepository<Conta, Long> {
    List<Conta> findByClienteId(Long clienteId);
    Optional<Conta> findByNumero(String numero);
}
```

### 3.3 TransacaoRepository

Interface para acesso aos dados de transações.

```java
@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, Long> {
    List<Transacao> findByContaIdOrderByDataHoraDesc(Long contaId);
    List<Transacao> findByContaIdAndTipo(Long contaId, Transacao.TipoTransacao tipo);
}
```

## 4. Templates Thymeleaf (Views)

### 4.1 Listagem de Clientes

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Clientes - BancoPro</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="/css/styles.css">
</head>
<body>
    <header>
        <h1>Gerenciamento de Clientes</h1>
    </header>

    <div class="container">
        <div class="alert alert-success" th:if="${mensagem}" th:text="${mensagem}"></div>
        <div class="alert alert-danger" th:if="${erro}" th:text="${erro}"></div>

        <div class="actions">
            <a href="/clientes/novo" class="btn btn-primary">Novo Cliente</a>
        </div>

        <table class="table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Nome</th>
                    <th>CPF</th>
                    <th>Email</th>
                    <th>Telefone</th>
                    <th>Ações</th>
                </tr>
            </thead>
            <tbody>
                <tr th:each="cliente : ${clientes}">
                    <td th:text="${cliente.id}"></td>
                    <td th:text="${cliente.nome}"></td>
                    <td th:text="${cliente.cpf}"></td>
                    <td th:text="${cliente.email}"></td>
                    <td th:text="${cliente.telefone}"></td>
                    <td>
                        <a th:href="@{/contas/cliente/{id}(id=${cliente.id})}" class="btn btn-info">Contas</a>
                        <a th:href="@{/clientes/editar/{id}(id=${cliente.id})}" class="btn btn-warning">Editar</a>
                        <a th:href="@{/clientes/excluir/{id}(id=${cliente.id})}" class="btn btn-danger" 
                           onclick="return confirm('Tem certeza que deseja excluir este cliente?');">Excluir</a>
                    </td>
                </tr>
                <tr th:if="${#lists.isEmpty(clientes)}">
                    <td colspan="6">Nenhum cliente cadastrado</td>
                </tr>
            </tbody>
        </table>
    </div>

    <footer>
        <p>BancoPro - Sistema de Gerenciamento Bancário</p>
    </footer>
</body>
</html>
```

### 4.2 Formulário de Transferência

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Transferência - BancoPro</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="/css/styles.css">
</head>
<body>
    <header>
        <h1>Transferência Bancária</h1>
    </header>

    <div class="container">
        <div class="alert alert-danger" th:if="${erro}" th:text="${erro}"></div>

        <div class="card">
            <div class="card-header">
                <h2>Realizar Transferência</h2>
                <p>Conta Origem: <span th:text="${conta.numero}"></span></p>
                <p>Saldo Disponível: R$ <span th:text="${#numbers.formatDecimal(conta.saldo, 1, 'POINT', 2, 'COMMA')}"></span></p>
            </div>

            <div class="card-body">
                <form th:action="@{/transacoes/transferir}" method="post">
                    <input type="hidden" name="contaOrigemId" th:value="${conta.id}">

                    <div class="form-group">
                        <label for="contaDestinoId">Conta Destino:</label>
                        <select id="contaDestinoId" name="contaDestinoId" class="form-control" required>
                            <option value="">Selecione a conta destino</option>
                            <option th:each="contaOpt : ${contas}" 
                                    th:if="${contaOpt.id != conta.id}"
                                    th:value="${contaOpt.id}" 
                                    th:text="${contaOpt.numero + ' - ' + contaOpt.cliente.nome}"></option>
                        </select>
                    </div>

                    <div class="form-group">
                        <label for="valor">Valor:</label>
                        <input type="number" id="valor" name="valor" class="form-control" step="0.01" min="0.01" required>
                    </div>

                    <div class="form-group">
                        <label for="descricao">Descrição:</label>
                        <input type="text" id="descricao" name="descricao" class="form-control" required>
                    </div>

                    <div class="form-actions">
                        <button type="submit" class="btn btn-primary">Transferir</button>
                        <a th:href="@{/transacoes/conta/{id}(id=${conta.id})}" class="btn btn-secondary">Cancelar</a>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <footer>
        <p>BancoPro - Sistema de Gerenciamento Bancário</p>
    </footer>
</body>
</html>
```

## 5. Configurações da Aplicação

### 5.1 application.properties

Configurações do ambiente da aplicação.

```properties
# Configuração do Banco de Dados H2
spring.datasource.url=jdbc:h2:mem:bancoprodb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# Configuração JPA
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Configuração da aplicação
server.port=8081
spring.application.name=BancoPro
```

### 5.2 Classe Principal da Aplicação

```java
package com.bancopro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BancoProApplication {

    public static void main(String[] args) {
        SpringApplication.run(BancoProApplication.class, args);
    }
}
```
