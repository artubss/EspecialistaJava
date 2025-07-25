# Coleções e Programação Funcional em Java

## Collections Framework

O Java Collections Framework fornece uma arquitetura unificada para representar e manipular coleções.

### Principais Interfaces

```java
Collection  // Interface raiz
├── List    // Elementos ordenados, permite duplicatas
├── Set     // Elementos únicos, sem duplicatas
└── Queue   // Fila (FIFO - First In, First Out)

Map         // Pares chave-valor
```

### Implementações Comuns

```java
// Lists
ArrayList<E>       // Array dinâmico
LinkedList<E>      // Lista duplamente encadeada

// Sets
HashSet<E>         // Set baseado em hash table
TreeSet<E>         // Set ordenado (árvore rubro-negra)
LinkedHashSet<E>   // Set que mantém ordem de inserção

// Maps
HashMap<K,V>       // Map baseado em hash table
TreeMap<K,V>       // Map ordenado por chave
LinkedHashMap<K,V> // Map que mantém ordem de inserção
```

### Exemplo Prático

```java
public class BancoApp {
    // Lista de contas
    private List<Conta> contas = new ArrayList<>();

    // Map de clientes por CPF
    private Map<String, Cliente> clientesPorCpf = new HashMap<>();

    // Set de números de conta (únicos)
    private Set<String> numerosDeContas = new HashSet<>();

    public void adicionarConta(Conta conta) {
        contas.add(conta);
        numerosDeContas.add(conta.getNumero());
        clientesPorCpf.put(conta.getCliente().getCpf(), conta.getCliente());
    }

    public Optional<Conta> buscarContaPorNumero(String numero) {
        return contas.stream()
            .filter(c -> c.getNumero().equals(numero))
            .findFirst();
    }
}
```

## Generics

Generics permitem que você crie classes e métodos que podem trabalhar com diferentes tipos.

```java
// Classe genérica
public class Caixa<T> {
    private T conteudo;

    public void guardar(T item) {
        this.conteudo = item;
    }

    public T obter() {
        return conteudo;
    }
}

// Método genérico
public <T extends Comparable<T>> T encontrarMaior(List<T> lista) {
    if (lista.isEmpty()) {
        throw new IllegalArgumentException("Lista vazia");
    }

    return lista.stream()
        .reduce((a, b) -> a.compareTo(b) > 0 ? a : b)
        .get();
}
```

## Stream API

A Stream API permite processamento funcional de coleções.

```java
public class ExemploStream {
    private List<Conta> contas;

    // Filtrar e mapear
    public List<String> obterNumerosContasComSaldoPositivo() {
        return contas.stream()
            .filter(c -> c.getSaldo().compareTo(BigDecimal.ZERO) > 0)
            .map(Conta::getNumero)
            .collect(Collectors.toList());
    }

    // Redução
    public BigDecimal calcularSaldoTotal() {
        return contas.stream()
            .map(Conta::getSaldo)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Agrupamento
    public Map<TipoConta, List<Conta>> agruparPorTipo() {
        return contas.stream()
            .collect(Collectors.groupingBy(Conta::getTipo));
    }

    // Estatísticas
    public DoubleSummaryStatistics estatisticasSaldos() {
        return contas.stream()
            .mapToDouble(c -> c.getSaldo().doubleValue())
            .summaryStatistics();
    }
}
```

## Optional

Optional é um container que pode ou não conter um valor não-nulo.

```java
public class ServicoContas {
    private Map<String, Conta> contas;

    public Optional<Conta> buscarConta(String numero) {
        return Optional.ofNullable(contas.get(numero));
    }

    public void processarConta(String numero) {
        buscarConta(numero)
            .filter(c -> c.getSaldo().compareTo(BigDecimal.ZERO) > 0)
            .ifPresentOrElse(
                this::processarContaAtiva,
                () -> System.out.println("Conta não encontrada ou sem saldo")
            );
    }

    public BigDecimal obterSaldo(String numero) {
        return buscarConta(numero)
            .map(Conta::getSaldo)
            .orElse(BigDecimal.ZERO);
    }
}
```

## Interfaces Funcionais e Lambda

Interfaces funcionais têm exatamente um método abstrato e podem ser implementadas usando expressões lambda.

```java
// Interface funcional personalizada
@FunctionalInterface
public interface ValidadorConta {
    boolean validar(Conta conta);
}

public class ProcessadorContas {
    // Usando interface funcional do Java
    private Predicate<Conta> validadorSaldo =
        conta -> conta.getSaldo().compareTo(BigDecimal.ZERO) > 0;

    // Usando Consumer
    private Consumer<Conta> notificarCliente =
        conta -> System.out.println("Notificando cliente da conta: " + conta.getNumero());

    // Usando Function
    private Function<Conta, String> extrairNumero = Conta::getNumero;

    // Método que aceita lambda como parâmetro
    public List<Conta> filtrarContas(List<Conta> contas, Predicate<Conta> filtro) {
        return contas.stream()
            .filter(filtro)
            .collect(Collectors.toList());
    }

    // Exemplo de uso
    public void processarContasInativas() {
        List<Conta> contasInativas = filtrarContas(
            todasAsContas,
            conta -> !conta.isAtiva()
        );

        contasInativas.forEach(notificarCliente);
    }
}
```

## Dicas de Boas Práticas

1. Use a interface mais genérica possível ao declarar variáveis

   ```java
   // Bom
   List<String> lista = new ArrayList<>();
   // Evite
   ArrayList<String> lista = new ArrayList<>();
   ```

2. Prefira Optional a null para valores que podem não existir

3. Use streams para operações em coleções quando precisar:

   - Filtrar elementos
   - Transformar elementos
   - Reduzir a um único valor
   - Agrupar elementos

4. Aproveite métodos default de interfaces funcionais

   ```java
   Predicate<Conta> saldoPositivo = c -> c.getSaldo().compareTo(BigDecimal.ZERO) > 0;
   Predicate<Conta> contaAtiva = Conta::isAtiva;

   // Combinando predicados
   Predicate<Conta> contaValida = saldoPositivo.and(contaAtiva);
   ```

5. Use parallel streams com cautela e apenas quando necessário
   ```java
   // Parallel stream para operações intensivas
   BigDecimal total = contas.parallelStream()
       .map(Conta::getSaldo)
       .reduce(BigDecimal.ZERO, BigDecimal::add);
   ```
