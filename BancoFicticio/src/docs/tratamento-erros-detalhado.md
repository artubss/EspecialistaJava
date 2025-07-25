# Tratamento de Erros em Java

Este documento explora em detalhes o tratamento de erros em Java, com exemplos práticos do projeto BancoFicticio.

## Hierarquia de Exceções em Java

Em Java, todas as exceções derivam da classe `Throwable`, que possui duas subclasses principais: `Error` e `Exception`.

```
Throwable
├── Error (erros graves, geralmente irrecuperáveis)
│   ├── OutOfMemoryError
│   ├── StackOverflowError
│   └── ...
└── Exception (condições excepcionais recuperáveis)
    ├── IOException
    ├── SQLException
    ├── ClassNotFoundException
    ├── ...
    └── RuntimeException (exceções não verificadas)
        ├── NullPointerException
        ├── IllegalArgumentException
        ├── IndexOutOfBoundsException
        ├── ...
```

### Exceções Verificadas vs. Não Verificadas

Java divide as exceções em dois tipos:

1. **Exceções Verificadas (Checked Exceptions)**
   - Estendem `Exception` (mas não `RuntimeException`)
   - Devem ser declaradas na assinatura do método ou tratadas explicitamente
   - Representam condições excepcionais previsíveis e recuperáveis
   - Exemplos: `IOException`, `SQLException`

2. **Exceções Não Verificadas (Unchecked Exceptions)**
   - Estendem `RuntimeException` ou `Error`
   - Não precisam ser declaradas ou tratadas explicitamente
   - Geralmente representam erros de programação ou condições irrecuperáveis
   - Exemplos: `NullPointerException`, `IllegalArgumentException`

## Blocos try-catch-finally

O mecanismo básico para tratamento de exceções em Java é o bloco `try-catch-finally`.

### Estrutura Básica

```java
try {
    // Código que pode lançar exceções
} catch (TipoExcecao1 e) {
    // Tratamento para TipoExcecao1
} catch (TipoExcecao2 e) {
    // Tratamento para TipoExcecao2
} finally {
    // Código que sempre será executado, independentemente de exceção
}
```

### Exemplo no Projeto: Tratamento de Exceções

```java
public void realizarSaque(String numeroConta, BigDecimal valor) {
    try {
        Conta conta = buscarConta(numeroConta);
        conta.sacar(valor);
        System.out.println("Saque realizado com sucesso");
    } catch (ContaNaoEncontradaException e) {
        System.err.println("Erro: " + e.getMessage());
        registrarErro("Conta não encontrada", e);
    } catch (SaldoInsuficienteException e) {
        System.err.println("Erro: Saldo insuficiente");
        System.err.println("Saldo atual: " + e.getSaldoAtual());
        System.err.println("Valor solicitado: " + e.getValorSolicitado());
        registrarErro("Saldo insuficiente", e);
    } catch (Exception e) {
        System.err.println("Erro inesperado: " + e.getMessage());
        registrarErro("Erro inesperado", e);
    } finally {
        System.out.println("Operação finalizada");
        atualizarLog();
    }
}
```

### Multi-catch (Java 7+)

A partir do Java 7, é possível capturar múltiplos tipos de exceção em um único bloco `catch`:

```java
try {
    // Código que pode lançar exceções
} catch (IOException | SQLException e) {
    // Tratamento comum para ambas as exceções
    System.err.println("Erro de E/S ou banco de dados: " + e.getMessage());
} catch (Exception e) {
    // Tratamento para outras exceções
}
```

## Lançamento de Exceções

### Palavra-chave throw

A palavra-chave `throw` é usada para lançar explicitamente uma exceção:

```java
public void depositar(BigDecimal valor) {
    if (valor == null) {
        throw new IllegalArgumentException("Valor não pode ser nulo");
    }
    
    if (valor.compareTo(BigDecimal.ZERO) <= 0) {
        throw new IllegalArgumentException("Valor deve ser positivo");
    }
    
    this.saldo = this.saldo.add(valor);
}
```

### Declaração de Exceções com throws

A palavra-chave `throws` é usada na assinatura de um método para declarar que ele pode lançar exceções verificadas:

```java
public void transferir(Conta destino, BigDecimal valor) throws ContaInativaException {
    if (!this.isAtiva() || !destino.isAtiva()) {
        throw new ContaInativaException("Uma das contas está inativa");
    }
    
    this.sacar(valor);
    destino.depositar(valor);
}
```

### Relançamento de Exceções

É possível capturar uma exceção e relançá-la ou lançar uma nova:

```java
public void processarTransacao(String numeroConta, BigDecimal valor) throws TransacaoException {
    try {
        Conta conta = buscarConta(numeroConta);
        conta.sacar(valor);
    } catch (ContaNaoEncontradaException e) {
        // Relançar como uma nova exceção com contexto adicional
        throw new TransacaoException("Falha na transação: conta não encontrada", e);
    } catch (SaldoInsuficienteException e) {
        // Relançar a mesma exceção
        throw e;
    }
}
```

## Exceções Personalizadas

No projeto BancoFicticio, criamos exceções personalizadas para representar erros específicos do domínio.

### Exemplo no Projeto: SaldoInsuficienteException

```java
public class SaldoInsuficienteException extends RuntimeException {
    private final BigDecimal saldoAtual;
    private final BigDecimal valorSolicitado;
    
    public SaldoInsuficienteException(String message) {
        this(message, null, null);
    }
    
    public SaldoInsuficienteException(String message, BigDecimal saldoAtual, BigDecimal valorSolicitado) {
        super(message);
        this.saldoAtual = saldoAtual;
        this.valorSolicitado = valorSolicitado;
    }
    
    public BigDecimal getSaldoAtual() {
        return saldoAtual;
    }
    
    public BigDecimal getValorSolicitado() {
        return valorSolicitado;
    }
}
```

### Criando uma Hierarquia de Exceções

É uma boa prática criar uma hierarquia de exceções para seu domínio:

```java
// Exceção base para todas as exceções do banco
public abstract class BancoException extends RuntimeException {
    public BancoException(String message) {
        super(message);
    }
    
    public BancoException(String message, Throwable cause) {
        super(message, cause);
    }
}

// Exceções específicas
public class ContaNaoEncontradaException extends BancoException {
    private final String numeroConta;
    
    public ContaNaoEncontradaException(String numeroConta) {
        super("Conta não encontrada: " + numeroConta);
        this.numeroConta = numeroConta;
    }
    
    public String getNumeroConta() {
        return numeroConta;
    }
}

public class ContaInativaException extends BancoException {
    public ContaInativaException(String message) {
        super(message);
    }
}

public class LimiteExcedidoException extends BancoException {
    public LimiteExcedidoException(String message) {
        super(message);
    }
}
```

## Try-with-Resources (Java 7+)

O recurso try-with-resources, introduzido no Java 7, simplifica o uso de recursos que precisam ser fechados após o uso (como arquivos, conexões, etc.).

### Sintaxe Básica

```java
try (Recurso recurso = new Recurso()) {
    // Usar o recurso
} catch (Exception e) {
    // Tratar exceções
}
// O recurso será fechado automaticamente, mesmo em caso de exceção
```

### Exemplo: Geração de Extrato

```java
public void gerarExtrato(Conta conta, Path arquivo) throws IOException {
    try (BufferedWriter writer = Files.newBufferedWriter(arquivo)) {
        writer.write("EXTRATO DA CONTA " + conta.getNumero());
        writer.newLine();
        writer.write("Titular: " + conta.getTitular().getNome());
        writer.newLine();
        writer.write("Saldo: " + conta.getSaldo());
    }
    // BufferedWriter é fechado automaticamente
}
```

### Múltiplos Recursos

É possível declarar múltiplos recursos no mesmo bloco try-with-resources:

```java
public void transferirDados(Path origem, Path destino) throws IOException {
    try (InputStream in = Files.newInputStream(origem);
         OutputStream out = Files.newOutputStream(destino)) {
        
        byte[] buffer = new byte[1024];
        int bytesLidos;
        while ((bytesLidos = in.read(buffer)) != -1) {
            out.write(buffer, 0, bytesLidos);
        }
    }
    // Ambos os recursos são fechados automaticamente
}
```

## Boas Práticas no Tratamento de Exceções

### 1. Seja Específico nas Exceções Capturadas

```java
// Ruim: captura genérica
try {
    // código
} catch (Exception e) {
    // tratamento genérico
}

// Bom: captura específica
try {
    // código
} catch (SaldoInsuficienteException e) {
    // tratamento específico para saldo insuficiente
} catch (ContaInativaException e) {
    // tratamento específico para conta inativa
} catch (Exception e) {
    // tratamento para outros casos não previstos
}
```

### 2. Não Ignore Exceções

```java
// Ruim: exceção ignorada
try {
    conta.sacar(valor);
} catch (SaldoInsuficienteException e) {
    // vazio ou apenas e.printStackTrace()
}

// Bom: tratamento adequado
try {
    conta.sacar(valor);
} catch (SaldoInsuficienteException e) {
    logger.error("Saldo insuficiente para saque", e);
    notificarUsuario("Não foi possível realizar o saque por saldo insuficiente");
}
```

### 3. Preserve a Pilha de Chamadas

```java
// Ruim: perda da causa original
try {
    conta.sacar(valor);
} catch (SaldoInsuficienteException e) {
    throw new RuntimeException("Erro ao sacar");
}

// Bom: preserva a causa original
try {
    conta.sacar(valor);
} catch (SaldoInsuficienteException e) {
    throw new RuntimeException("Erro ao sacar", e);
}
```

### 4. Documente as Exceções Lançadas

```java
/**
 * Transfere um valor desta conta para a conta de destino.
 *
 * @param destino a conta de destino
 * @param valor o valor a ser transferido
 * @throws ContaInativaException se alguma das contas estiver inativa
 * @throws SaldoInsuficienteException se o saldo for insuficiente
 * @throws IllegalArgumentException se o valor for nulo ou negativo
 */
public void transferir(Conta destino, BigDecimal valor) throws ContaInativaException {
    // implementação
}
```

### 5. Use Exceções para Condições Excepcionais

```java
// Ruim: uso de exceção para controle de fluxo normal
public boolean temSaldoSuficiente(BigDecimal valor) {
    try {
        sacar(valor);
        depositar(valor); // desfaz o saque
        return true;
    } catch (SaldoInsuficienteException e) {
        return false;
    }
}

// Bom: verificação explícita
public boolean temSaldoSuficiente(BigDecimal valor) {
    return getSaldo().compareTo(valor) >= 0;
}
```

### 6. Crie Exceções Significativas

```java
// Ruim: exceção genérica com mensagem vaga
throw new RuntimeException("Erro");

// Bom: exceção específica com mensagem clara
throw new SaldoInsuficienteException(
    "Saldo insuficiente para saque. Saldo atual: " + saldo + ", Valor solicitado: " + valor,
    saldo,
    valor
);
```

### 7. Registre Exceções Adequadamente

```java
try {
    // código que pode lançar exceção
} catch (Exception e) {
    // Log com nível apropriado e contexto
    logger.error("Falha ao processar transação {} para a conta {}", 
                 transacao.getId(), conta.getNumero(), e);
}
```

## Logging de Exceções

O registro adequado de exceções é crucial para diagnóstico e solução de problemas.

### Exemplo com SLF4J e Logback

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransacaoService {
    private static final Logger logger = LoggerFactory.getLogger(TransacaoService.class);
    
    public void processarTransacao(Transacao transacao) {
        try {
            // Processamento da transação
            logger.info("Transação {} processada com sucesso", transacao.getId());
        } catch (SaldoInsuficienteException e) {
            logger.warn("Saldo insuficiente na transação {}: {}", 
                       transacao.getId(), e.getMessage());
            // Tratamento da exceção
        } catch (Exception e) {
            logger.error("Erro ao processar transação {}", transacao.getId(), e);
            // Tratamento da exceção
        }
    }
}
```

### Níveis de Log

Os diferentes níveis de log permitem categorizar a gravidade dos eventos:

1. **ERROR**: Erros que impedem o funcionamento correto da aplicação
2. **WARN**: Situações potencialmente problemáticas que não impedem o funcionamento
3. **INFO**: Informações gerais sobre o funcionamento da aplicação
4. **DEBUG**: Informações detalhadas úteis para depuração
5. **TRACE**: Informações extremamente detalhadas

```java
// Exemplo de uso dos diferentes níveis
logger.error("Falha crítica: {}", mensagem, excecao);
logger.warn("Alerta: {}", mensagem);
logger.info("Transação {} concluída", id);
logger.debug("Processando item {}", item);
logger.trace("Valor calculado: {}", valor);
```

## Assertions

Assertions são verificações em tempo de execução que podem ser ativadas durante o desenvolvimento e desativadas em produção.

```java
public void transferir(Conta destino, BigDecimal valor) {
    // Pré-condições
    assert destino != null : "Conta de destino não pode ser nula";
    assert valor != null : "Valor não pode ser nulo";
    assert valor.compareTo(BigDecimal.ZERO) > 0 : "Valor deve ser positivo";
    
    // Implementação
    this.sacar(valor);
    destino.depositar(valor);
    
    // Pós-condição
    assert this.getHistorico().getUltimaTransacao().getTipo() == TipoTransacao.SAIDA;
}
```

**Observação**: As assertions são desativadas por padrão em tempo de execução. Para ativá-las, use a opção `-ea` na JVM.

## Exemplo Completo: Sistema de Tratamento de Erros

Vamos ver um exemplo completo de como o tratamento de erros é implementado no projeto BancoFicticio:

### 1. Hierarquia de Exceções

```java
// Exceção base
public abstract class BancoException extends RuntimeException {
    public BancoException(String message) {
        super(message);
    }
    
    public BancoException(String message, Throwable cause) {
        super(message, cause);
    }
}

// Exceções específicas
public class SaldoInsuficienteException extends BancoException {
    private final BigDecimal saldoAtual;
    private final BigDecimal valorSolicitado;
    
    public SaldoInsuficienteException(String message, BigDecimal saldoAtual, BigDecimal valorSolicitado) {
        super(message);
        this.saldoAtual = saldoAtual;
        this.valorSolicitado = valorSolicitado;
    }
    
    // Getters
}

public class ContaInativaException extends BancoException {
    private final String numeroConta;
    
    public ContaInativaException(String numeroConta) {
        super("Conta inativa: " + numeroConta);
        this.numeroConta = numeroConta;
    }
    
    // Getters
}
```

### 2. Lançamento de Exceções nas Classes de Domínio

```java
public abstract class Conta {
    // ...
    
    public void sacar(BigDecimal valor) {
        if (!isAtiva()) {
            throw new ContaInativaException(numero);
        }
        
        if (valor == null) {
            throw new IllegalArgumentException("Valor não pode ser nulo");
        }
        
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor do saque deve ser maior que zero");
        }
        
        if (valor.compareTo(saldo) > 0) {
            throw new SaldoInsuficienteException(
                "Saldo insuficiente para saque",
                saldo,
                valor
            );
        }
        
        this.saldo = this.saldo.subtract(valor);
    }
    
    // ...
}
```

### 3. Tratamento de Exceções na Camada de Serviço

```java
public class TransacaoService {
    private static final Logger logger = LoggerFactory.getLogger(TransacaoService.class);
    private final ContaRepository contaRepository;
    
    public TransacaoService(ContaRepository contaRepository) {
        this.contaRepository = contaRepository;
    }
    
    public void transferir(String numeroOrigem, String numeroDestino, BigDecimal valor) {
        try {
            // Buscar contas
            Conta origem = contaRepository.findByNumero(numeroOrigem)
                .orElseThrow(() -> new ContaNaoEncontradaException(numeroOrigem));
                
            Conta destino = contaRepository.findByNumero(numeroDestino)
                .orElseThrow(() -> new ContaNaoEncontradaException(numeroDestino));
            
            // Validar valor
            if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Valor da transferência deve ser maior que zero");
            }
            
            // Realizar transferência
            origem.sacar(valor);
            destino.depositar(valor);
            
            // Salvar alterações
            contaRepository.save(origem);
            contaRepository.save(destino);
            
            logger.info("Transferência de {} realizada com sucesso de {} para {}", 
                       valor, numeroOrigem, numeroDestino);
                       
        } catch (ContaNaoEncontradaException e) {
            logger.error("Conta não encontrada na transferência", e);
            throw new TransacaoException("Não foi possível realizar a transferência: " + e.getMessage(), e);
            
        } catch (ContaInativaException e) {
            logger.error("Conta inativa na transferência", e);
            throw new TransacaoException("Não foi possível realizar a transferência: conta inativa", e);
            
        } catch (SaldoInsuficienteException e) {
            logger.warn("Saldo insuficiente na transferência de {} para {}: {} < {}", 
                       numeroOrigem, numeroDestino, e.getSaldoAtual(), e.getValorSolicitado());
            throw new TransacaoException("Saldo insuficiente para realizar a transferência", e);
            
        } catch (Exception e) {
            logger.error("Erro inesperado na transferência", e);
            throw new TransacaoException("Erro interno ao processar a transferência", e);
        }
    }
}
```

### 4. Tratamento de Exceções na Camada de Apresentação

```java
public class TransacaoController {
    private final TransacaoService transacaoService;
    
    public TransacaoController(TransacaoService transacaoService) {
        this.transacaoService = transacaoService;
    }
    
    public void realizarTransferencia(Scanner scanner) {
        try {
            System.out.print("Conta de origem: ");
            String origem = scanner.nextLine();
            
            System.out.print("Conta de destino: ");
            String destino = scanner.nextLine();
            
            System.out.print("Valor: ");
            BigDecimal valor = new BigDecimal(scanner.nextLine());
            
            transacaoService.transferir(origem, destino, valor);
            
            System.out.println("Transferência realizada com sucesso!");
            
        } catch (TransacaoException e) {
            System.err.println("Erro na transferência: " + e.getMessage());
            
            if (e.getCause() instanceof SaldoInsuficienteException) {
                SaldoInsuficienteException sie = (SaldoInsuficienteException) e.getCause();
                System.err.println("Saldo disponível: " + sie.getSaldoAtual());
                System.err.println("Valor solicitado: " + sie.getValorSolicitado());
            }
            
        } catch (NumberFormatException e) {
            System.err.println("Valor inválido. Digite um número válido.");
            
        } catch (Exception e) {
            System.err.println("Erro inesperado: " + e.getMessage());
        }
    }
}
```

## Conclusão

O tratamento adequado de exceções é essencial para criar aplicações Java robustas e confiáveis. No projeto BancoFicticio, utilizamos uma abordagem estruturada para tratar erros:

1. **Hierarquia de exceções personalizadas** para representar erros específicos do domínio
2. **Validações rigorosas** nas classes de domínio para garantir integridade dos dados
3. **Tratamento específico** para diferentes tipos de exceções
4. **Logging detalhado** para facilitar diagnóstico e solução de problemas
5. **Mensagens claras** para usuários e desenvolvedores

Seguindo estas práticas, conseguimos criar um sistema que lida graciosamente com situações excepcionais, mantendo a integridade dos dados e fornecendo feedback útil para os usuários. 