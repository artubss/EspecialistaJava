# Tratamento de Erros em Java

## Exceções

Em Java, exceções são eventos que ocorrem durante a execução do programa que interrompem o fluxo normal de instruções.

### Hierarquia de Exceções

```
Throwable
├── Error         // Problemas sérios, normalmente não tratados
└── Exception     // Problemas que podem ser tratados
    └── RuntimeException  // Exceções não verificadas
```

### Tipos de Exceções

1. **Checked Exceptions** (Verificadas)

   - Devem ser declaradas ou tratadas
   - Estendem Exception (mas não RuntimeException)
   - Exemplo: IOException, SQLException

2. **Unchecked Exceptions** (Não Verificadas)
   - Não precisam ser declaradas
   - Estendem RuntimeException
   - Exemplo: NullPointerException, IllegalArgumentException

## Try/Catch/Finally

```java
public class ContaBancaria {
    public void sacar(BigDecimal valor) {
        try {
            // Código que pode lançar exceção
            if (valor.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Valor deve ser positivo");
            }

            if (valor.compareTo(saldo) > 0) {
                throw new SaldoInsuficienteException("Saldo insuficiente");
            }

            this.saldo = this.saldo.subtract(valor);

        } catch (IllegalArgumentException e) {
            // Tratamento específico para argumentos inválidos
            System.err.println("Valor inválido: " + e.getMessage());
            throw e; // Relançando a exceção

        } catch (SaldoInsuficienteException e) {
            // Tratamento específico para saldo insuficiente
            System.err.println("Erro ao sacar: " + e.getMessage());
            throw e;

        } finally {
            // Sempre executado, independente de exceção
            registrarTentativaSaque(valor);
        }
    }
}
```

## Try-with-Resources

Usado para recursos que precisam ser fechados (implementam AutoCloseable).

```java
public class GeradorExtrato {
    public void gerarExtrato(Conta conta, Path arquivo) {
        try (BufferedWriter writer = Files.newBufferedWriter(arquivo)) {
            writer.write("Extrato da conta: " + conta.getNumero());
            writer.newLine();
            writer.write("Saldo: " + conta.getSaldo());

        } catch (IOException e) {
            throw new ExtratoException("Erro ao gerar extrato", e);
        }
    }
}
```

## Exceções Personalizadas

Crie exceções específicas para seu domínio.

```java
// Exceção verificada (checked)
public class ExtratoException extends Exception {
    public ExtratoException(String message) {
        super(message);
    }

    public ExtratoException(String message, Throwable cause) {
        super(message, cause);
    }
}

// Exceção não verificada (unchecked)
public class SaldoInsuficienteException extends RuntimeException {
    private final BigDecimal saldoAtual;
    private final BigDecimal valorSolicitado;

    public SaldoInsuficienteException(String message,
            BigDecimal saldoAtual,
            BigDecimal valorSolicitado) {
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

## Multi-catch e Relançamento

```java
public class ServicoTransferencia {
    public void transferir(Conta origem, Conta destino, BigDecimal valor) {
        try {
            validarTransferencia(origem, destino, valor);
            origem.sacar(valor);
            destino.depositar(valor);

        } catch (SaldoInsuficienteException | ContaBloqueadaException e) {
            // Tratando múltiplas exceções de uma vez
            logger.error("Erro na transferência: " + e.getMessage());
            throw new TransferenciaException("Não foi possível realizar a transferência", e);

        } catch (Exception e) {
            // Capturando qualquer outra exceção
            logger.error("Erro inesperado: " + e.getMessage());
            throw new TransferenciaException("Erro interno", e);
        }
    }
}
```

## Boas Práticas

1. **Hierarquia de Exceções**

```java
// Base para todas as exceções do domínio
public abstract class BancoException extends RuntimeException {
    public BancoException(String message) {
        super(message);
    }
}

// Exceções específicas
public class ContaNaoEncontradaException extends BancoException {
    private final String numeroConta;

    public ContaNaoEncontradaException(String numeroConta) {
        super("Conta não encontrada: " + numeroConta);
        this.numeroConta = numeroConta;
    }
}
```

2. **Documentação de Exceções**

```java
/**
 * Realiza uma transferência entre contas.
 *
 * @param origem Conta de origem
 * @param destino Conta de destino
 * @param valor Valor a ser transferido
 * @throws SaldoInsuficienteException se a conta origem não tiver saldo
 * @throws ContaBloqueadaException se alguma das contas estiver bloqueada
 * @throws IllegalArgumentException se o valor for negativo ou zero
 */
public void transferir(Conta origem, Conta destino, BigDecimal valor) {
    // implementação
}
```

3. **Logging de Exceções**

```java
public class ServicoContas {
    private static final Logger logger =
        LoggerFactory.getLogger(ServicoContas.class);

    public void processarOperacao(Conta conta, BigDecimal valor) {
        try {
            // código que pode lançar exceção

        } catch (Exception e) {
            logger.error("Erro ao processar operação: {} para conta: {}",
                e.getMessage(), conta.getNumero(), e);
            throw e;
        }
    }
}
```

4. **Evite Catch Vazio**

```java
// Ruim
try {
    // código
} catch (Exception e) {
    // vazio ou apenas e.printStackTrace()
}

// Bom
try {
    // código
} catch (Exception e) {
    logger.error("Contexto do erro", e);
    throw new RuntimeException("Mensagem clara do erro", e);
}
```

5. **Use Exceções para Fluxos Excepcionais**

```java
// Ruim
if (conta != null) {
    // usar conta
} else {
    return null;
}

// Bom
if (conta == null) {
    throw new ContaNaoEncontradaException("Conta não pode ser nula");
}
// usar conta
```
