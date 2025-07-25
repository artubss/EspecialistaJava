# Coleções e Programação Funcional em Java

Este documento explora em detalhes as coleções e os recursos de programação funcional em Java, com exemplos práticos do projeto BancoFicticio.

## Java Collections Framework

O Java Collections Framework (JCF) é uma arquitetura unificada para representar e manipular coleções de objetos. Ele fornece interfaces, implementações e algoritmos para trabalhar com grupos de objetos.

### Hierarquia de Interfaces

A hierarquia de interfaces do JCF é organizada da seguinte forma:

```
Collection
├── List (elementos ordenados, permite duplicatas)
│   ├── ArrayList
│   ├── LinkedList
│   └── Vector (legado)
│       └── Stack (legado)
├── Set (elementos únicos, sem duplicatas)
│   ├── HashSet
│   ├── LinkedHashSet
│   └── SortedSet
│       └── TreeSet
└── Queue (fila, processamento FIFO - First In, First Out)
    ├── PriorityQueue
    └── Deque (double-ended queue)
        ├── ArrayDeque
        └── LinkedList

Map (pares chave-valor)
├── HashMap
├── LinkedHashMap
├── Hashtable (legado)
└── SortedMap
    └── TreeMap
```

### List - Listas Ordenadas

As listas mantêm elementos em uma sequência ordenada e permitem elementos duplicados.

#### ArrayList

`ArrayList` é baseada em um array dinâmico que cresce conforme necessário. Oferece acesso rápido por índice, mas inserções e remoções no meio são mais lentas.

```java
// Criação
List<String> nomes = new ArrayList<>();

// Adição de elementos
nomes.add("João");
nomes.add("Maria");
nomes.add("Pedro");
nomes.add("João");  // Permite duplicatas

// Acesso por índice
String primeiro = nomes.get(0);  // "João"

// Iteração
for (String nome : nomes) {
    System.out.println(nome);
}

// Remoção
nomes.remove("Maria");
nomes.remove(0);  // Remove o primeiro elemento

// Verificação
boolean contem = nomes.contains("Pedro");  // true
int tamanho = nomes.size();  // 2
```

#### LinkedList

`LinkedList` é baseada em uma lista duplamente encadeada. Oferece inserções e remoções rápidas em qualquer posição, mas acesso por índice é mais lento.

```java
// Criação
List<String> nomes = new LinkedList<>();

// Métodos adicionais específicos de LinkedList
LinkedList<String> fila = new LinkedList<>();
fila.addFirst("Primeiro");
fila.addLast("Último");
String primeiro = fila.getFirst();
String ultimo = fila.getLast();
String removidoPrimeiro = fila.removeFirst();
String removidoUltimo = fila.removeLast();
```

### Set - Conjuntos de Elementos Únicos

Sets não permitem elementos duplicados e geralmente não mantêm uma ordem específica.

#### HashSet

`HashSet` é baseado em uma tabela hash. Oferece operações de adição, remoção e verificação em tempo constante (O(1)), mas não garante ordem.

```java
// Criação
Set<String> cpfs = new HashSet<>();

// Adição
cpfs.add("123.456.789-00");
cpfs.add("987.654.321-00");
cpfs.add("123.456.789-00");  // Não será adicionado (duplicata)

// Verificação
boolean contem = cpfs.contains("123.456.789-00");  // true
int tamanho = cpfs.size();  // 2 (não 3, pois duplicatas são ignoradas)

// Remoção
cpfs.remove("123.456.789-00");
```

#### LinkedHashSet

`LinkedHashSet` combina HashSet com uma lista duplamente encadeada, mantendo a ordem de inserção.

```java
Set<String> cpfsOrdenados = new LinkedHashSet<>();
cpfsOrdenados.add("123.456.789-00");
cpfsOrdenados.add("987.654.321-00");
cpfsOrdenados.add("555.666.777-88");

// Iteração mantém a ordem de inserção
for (String cpf : cpfsOrdenados) {
    System.out.println(cpf);
}
```

#### TreeSet

`TreeSet` mantém os elementos ordenados naturalmente ou por um comparador fornecido.

```java
// Ordem natural
Set<String> nomes = new TreeSet<>();
nomes.add("Carlos");
nomes.add("Ana");
nomes.add("Bruno");
// Iteração: Ana, Bruno, Carlos

// Com comparador personalizado
Set<Cliente> clientesPorNome = new TreeSet<>((c1, c2) -> c1.getNome().compareTo(c2.getNome()));
clientesPorNome.add(new Cliente("Carlos", "123", null));
clientesPorNome.add(new Cliente("Ana", "456", null));
```

### Map - Mapeamentos Chave-Valor

Maps armazenam pares chave-valor, onde cada chave é única.

#### HashMap

`HashMap` é baseado em tabela hash, oferecendo operações rápidas (O(1)) para busca, inserção e remoção.

```java
// Criação
Map<String, Cliente> clientesPorCpf = new HashMap<>();

// Adição de pares chave-valor
clientesPorCpf.put("123.456.789-00", new Cliente("João", "123.456.789-00", null));
clientesPorCpf.put("987.654.321-00", new Cliente("Maria", "987.654.321-00", null));

// Busca por chave
Cliente joao = clientesPorCpf.get("123.456.789-00");
Cliente inexistente = clientesPorCpf.get("111.111.111-11");  // null

// Verificação
boolean contemChave = clientesPorCpf.containsKey("123.456.789-00");  // true
boolean contemValor = clientesPorCpf.containsValue(joao);  // true

// Remoção
Cliente removido = clientesPorCpf.remove("123.456.789-00");

// Iteração sobre entradas
for (Map.Entry<String, Cliente> entrada : clientesPorCpf.entrySet()) {
    System.out.println("CPF: " + entrada.getKey() + ", Nome: " + entrada.getValue().getNome());
}

// Iteração sobre chaves
for (String cpf : clientesPorCpf.keySet()) {
    System.out.println("CPF: " + cpf);
}

// Iteração sobre valores
for (Cliente cliente : clientesPorCpf.values()) {
    System.out.println("Nome: " + cliente.getNome());
}
```

#### LinkedHashMap

`LinkedHashMap` mantém a ordem de inserção dos elementos.

```java
Map<String, Cliente> clientesOrdenados = new LinkedHashMap<>();
// A iteração seguirá a ordem de inserção
```

#### TreeMap

`TreeMap` mantém as chaves ordenadas naturalmente ou por um comparador fornecido.

```java
// Chaves em ordem natural
Map<String, Cliente> clientesOrdenados = new TreeMap<>();

// Com comparador personalizado
Map<Cliente, List<Conta>> contasPorCliente = new TreeMap<>((c1, c2) -> 
    c1.getNome().compareTo(c2.getNome()));
```

### Exemplo no Projeto: Uso de Coleções

```java
public class Cliente {
    private List<Conta> contas;
    
    public Cliente(String nome, String cpf, LocalDate dataNascimento) {
        // ...
        this.contas = new ArrayList<>();
    }
    
    public void adicionarConta(Conta conta) {
        this.contas.add(conta);
    }
    
    public List<Conta> getContas() {
        return Collections.unmodifiableList(contas);
    }
}

public class BancoRepository {
    private Map<String, Cliente> clientesPorCpf = new HashMap<>();
    private Map<String, Conta> contasPorNumero = new HashMap<>();
    private Set<String> cpfsVIP = new HashSet<>();
    
    // Métodos para manipular os dados...
}
```

### Classe Collections

A classe utilitária `Collections` fornece métodos estáticos para operações comuns em coleções:

```java
// Lista imutável
List<String> lista = Collections.unmodifiableList(arrayList);

// Lista sincronizada (thread-safe)
List<String> listaSincronizada = Collections.synchronizedList(arrayList);

// Lista vazia
List<String> listaVazia = Collections.emptyList();

// Lista com um elemento
List<String> listaSingleton = Collections.singletonList("único");

// Ordenação
Collections.sort(lista);
Collections.sort(lista, (s1, s2) -> s2.compareTo(s1));  // Ordem reversa

// Busca binária (lista deve estar ordenada)
int indice = Collections.binarySearch(lista, "elemento");

// Valores máximo e mínimo
String maximo = Collections.max(lista);
String minimo = Collections.min(lista);

// Preenchimento
Collections.fill(lista, "valor");

// Inversão
Collections.reverse(lista);

// Embaralhamento
Collections.shuffle(lista);
```

## Programação Funcional em Java

A partir do Java 8, foram introduzidos recursos que permitem um estilo de programação mais funcional, incluindo expressões lambda, interfaces funcionais e a Stream API.

### Interfaces Funcionais

Uma interface funcional é uma interface que possui exatamente um método abstrato. O Java 8 introduziu a anotação `@FunctionalInterface` para marcar essas interfaces.

#### Interfaces Funcionais Predefinidas

O pacote `java.util.function` contém várias interfaces funcionais predefinidas:

| Interface         | Método Abstrato           | Descrição                                       |
|-------------------|---------------------------|------------------------------------------------|
| `Predicate<T>`    | `boolean test(T t)`       | Testa um valor e retorna verdadeiro ou falso    |
| `Consumer<T>`     | `void accept(T t)`        | Consome um valor sem retornar nada              |
| `Function<T,R>`   | `R apply(T t)`            | Transforma um valor de tipo T em um valor de tipo R |
| `Supplier<T>`     | `T get()`                 | Fornece um valor sem receber nada               |
| `UnaryOperator<T>`| `T apply(T t)`            | Função que recebe e retorna o mesmo tipo        |
| `BinaryOperator<T>`| `T apply(T t1, T t2)`    | Função que recebe dois valores do mesmo tipo e retorna um valor desse tipo |

```java
// Exemplos de uso
Predicate<Conta> contaAtiva = conta -> conta.isAtiva();
Consumer<Conta> exibirSaldo = conta -> System.out.println("Saldo: " + conta.getSaldo());
Function<Conta, String> extrairNumero = conta -> conta.getNumero();
Supplier<LocalDate> hoje = () -> LocalDate.now();
UnaryOperator<BigDecimal> duplicar = valor -> valor.multiply(new BigDecimal("2"));
BinaryOperator<BigDecimal> somar = (a, b) -> a.add(b);
```

### Expressões Lambda

Expressões lambda são funções anônimas que podem ser passadas como argumentos ou armazenadas em variáveis.

#### Sintaxe Básica

```java
// Sem parâmetros
Runnable r1 = () -> System.out.println("Olá");

// Um parâmetro (parênteses opcionais)
Consumer<String> c1 = s -> System.out.println(s);
Consumer<String> c2 = (String s) -> System.out.println(s);

// Múltiplos parâmetros
BiFunction<Integer, Integer, Integer> soma = (a, b) -> a + b;

// Bloco de código
Comparator<String> comp = (s1, s2) -> {
    int resultado = s1.length() - s2.length();
    if (resultado == 0) {
        resultado = s1.compareTo(s2);
    }
    return resultado;
};
```

#### Referências a Métodos

Referências a métodos são uma forma mais concisa de expressões lambda quando a lambda apenas chama um método existente:

```java
// Método estático
Function<String, Integer> conversor1 = s -> Integer.parseInt(s);
Function<String, Integer> conversor2 = Integer::parseInt;  // Equivalente

// Método de instância de um objeto específico
Consumer<String> impressora1 = s -> System.out.println(s);
Consumer<String> impressora2 = System.out::println;  // Equivalente

// Método de instância de um objeto arbitrário
Function<String, Integer> tamanho1 = s -> s.length();
Function<String, Integer> tamanho2 = String::length;  // Equivalente

// Construtor
Supplier<List<String>> criador1 = () -> new ArrayList<>();
Supplier<List<String>> criador2 = ArrayList::new;  // Equivalente
```

### Stream API

A Stream API fornece uma maneira declarativa de processar coleções de objetos. Uma stream representa uma sequência de elementos que suporta operações agregadas.

#### Criação de Streams

```java
// A partir de uma coleção
List<Conta> contas = cliente.getContas();
Stream<Conta> stream1 = contas.stream();

// A partir de um array
String[] nomes = {"João", "Maria", "Pedro"};
Stream<String> stream2 = Arrays.stream(nomes);

// Stream vazia
Stream<String> stream3 = Stream.empty();

// Stream com elementos específicos
Stream<Integer> stream4 = Stream.of(1, 2, 3, 4, 5);

// Stream infinita (com limite)
Stream<Integer> stream5 = Stream.iterate(0, n -> n + 2).limit(10);  // 0, 2, 4, ..., 18
Stream<Double> stream6 = Stream.generate(Math::random).limit(5);    // 5 números aleatórios
```

#### Operações Intermediárias

Operações intermediárias retornam uma nova stream e são lazy (só executadas quando necessário):

```java
// Filtrar elementos
Stream<Conta> contasAtivas = contas.stream()
    .filter(conta -> conta.isAtiva());

// Transformar elementos
Stream<String> numerosContas = contas.stream()
    .map(Conta::getNumero);

// Achatar streams aninhadas
Stream<Conta> todasContas = clientes.stream()
    .flatMap(cliente -> cliente.getContas().stream());

// Ordenar elementos
Stream<Conta> contasOrdenadas = contas.stream()
    .sorted((c1, c2) -> c1.getNumero().compareTo(c2.getNumero()));

// Limitar número de elementos
Stream<Conta> primeirasContas = contas.stream()
    .limit(10);

// Pular elementos
Stream<Conta> contasSemPrimeiras = contas.stream()
    .skip(5);

// Remover duplicatas
Stream<String> tiposUnicos = contas.stream()
    .map(c -> c.getClass().getSimpleName())
    .distinct();

// Espiar elementos (debug)
Stream<Conta> contasComLog = contas.stream()
    .peek(c -> System.out.println("Processando: " + c.getNumero()));
```

#### Operações Terminais

Operações terminais produzem um resultado ou efeito colateral e encerram a stream:

```java
// Coletar em uma coleção
List<String> listaNumeros = contas.stream()
    .map(Conta::getNumero)
    .collect(Collectors.toList());

Set<String> conjuntoNumeros = contas.stream()
    .map(Conta::getNumero)
    .collect(Collectors.toSet());

// Coletar em uma string
String numerosCSV = contas.stream()
    .map(Conta::getNumero)
    .collect(Collectors.joining(", "));

// Redução
Optional<BigDecimal> saldoTotal = contas.stream()
    .map(Conta::getSaldo)
    .reduce(BigDecimal::add);

BigDecimal saldoTotalComInicial = contas.stream()
    .map(Conta::getSaldo)
    .reduce(BigDecimal.ZERO, BigDecimal::add);

// Contagem
long quantidadeContas = contas.stream().count();

// Verificações
boolean todasAtivas = contas.stream().allMatch(Conta::isAtiva);
boolean algumaAtiva = contas.stream().anyMatch(Conta::isAtiva);
boolean nenhumaAtiva = contas.stream().noneMatch(Conta::isAtiva);

// Encontrar elementos
Optional<Conta> primeiraConta = contas.stream().findFirst();
Optional<Conta> qualquerConta = contas.stream().findAny();

// Estatísticas (para streams numéricas)
IntSummaryStatistics stats = contas.stream()
    .mapToInt(c -> c.getSaldo().intValue())
    .summaryStatistics();
System.out.println("Média: " + stats.getAverage());
System.out.println("Soma: " + stats.getSum());
System.out.println("Máximo: " + stats.getMax());
```

#### Collectors

A classe `Collectors` fornece métodos para operações de redução comuns:

```java
// Coletar em diferentes tipos de coleções
List<Conta> lista = contas.stream().collect(Collectors.toList());
Set<Conta> conjunto = contas.stream().collect(Collectors.toSet());
LinkedList<Conta> linkedList = contas.stream()
    .collect(Collectors.toCollection(LinkedList::new));

// Transformar em mapa
Map<String, Conta> contasPorNumero = contas.stream()
    .collect(Collectors.toMap(Conta::getNumero, conta -> conta));

// Agrupar por alguma propriedade
Map<Boolean, List<Conta>> contasPorStatus = contas.stream()
    .collect(Collectors.groupingBy(Conta::isAtiva));

Map<String, List<Conta>> contasPorTipo = contas.stream()
    .collect(Collectors.groupingBy(c -> c.getClass().getSimpleName()));

// Particionar (caso especial de agrupamento com predicado)
Map<Boolean, List<Conta>> contasComSaldoPositivo = contas.stream()
    .collect(Collectors.partitioningBy(c -> c.getSaldo().compareTo(BigDecimal.ZERO) > 0));

// Estatísticas
DoubleSummaryStatistics estatisticas = contas.stream()
    .collect(Collectors.summarizingDouble(c -> c.getSaldo().doubleValue()));

// String concatenada
String numerosFormatados = contas.stream()
    .map(Conta::getNumero)
    .collect(Collectors.joining(", ", "Contas: [", "]"));
```

### Exemplo no Projeto: Uso de Streams

```java
public class RelatorioService {
    public BigDecimal calcularSaldoTotal(Cliente cliente) {
        return cliente.getContas().stream()
            .map(Conta::getSaldo)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    public List<Conta> filtrarContasAtivas(Cliente cliente) {
        return cliente.getContas().stream()
            .filter(Conta::isAtiva)
            .collect(Collectors.toList());
    }
    
    public Map<String, List<Conta>> agruparContasPorTipo(Cliente cliente) {
        return cliente.getContas().stream()
            .collect(Collectors.groupingBy(c -> c.getClass().getSimpleName()));
    }
    
    public String gerarRelatorioContas(Cliente cliente) {
        return cliente.getContas().stream()
            .map(conta -> String.format("Conta %s: %s", 
                conta.getNumero(), 
                conta.getSaldo().toString()))
            .collect(Collectors.joining("\n"));
    }
}
```

### Streams Paralelas

Para processamento paralelo de elementos, podemos usar streams paralelas:

```java
// Criar stream paralela a partir de uma coleção
Stream<Conta> streamParalela1 = contas.parallelStream();

// Converter stream sequencial para paralela
Stream<Conta> streamParalela2 = contas.stream().parallel();

// Exemplo de uso
BigDecimal saldoTotal = contas.parallelStream()
    .map(Conta::getSaldo)
    .reduce(BigDecimal.ZERO, BigDecimal::add);
```

**Observação:** Streams paralelas são úteis para coleções grandes e operações independentes, mas podem introduzir overhead para coleções pequenas. Use com cautela e apenas quando necessário.

## Optional

A classe `Optional` foi introduzida no Java 8 para representar um valor que pode estar presente ou ausente, evitando `NullPointerException`.

### Criação de Optional

```java
// Optional vazio
Optional<String> vazio = Optional.empty();

// Optional com valor (não pode ser null)
Optional<String> comValor = Optional.of("texto");

// Optional que pode ter valor null
Optional<String> talvez = Optional.ofNullable(texto);  // texto pode ser null
```

### Verificação e Acesso ao Valor

```java
// Verificar se há valor
if (optional.isPresent()) {
    // Há um valor
}

if (optional.isEmpty()) {  // Java 11+
    // Não há valor
}

// Obter valor (lança NoSuchElementException se vazio)
String valor = optional.get();

// Formas seguras de obter o valor
String valorOuPadrao = optional.orElse("padrão");
String valorOuCalculado = optional.orElseGet(() -> calcularValorPadrao());
String valorOuExcecao = optional.orElseThrow(() -> new Exception("Valor ausente"));
```

### Operações Funcionais com Optional

```java
// Executar ação se presente
optional.ifPresent(valor -> System.out.println(valor));

// Executar ação se presente, outra ação se ausente (Java 9+)
optional.ifPresentOrElse(
    valor -> System.out.println("Valor: " + valor),
    () -> System.out.println("Valor ausente")
);

// Transformar valor se presente
Optional<Integer> tamanho = optional.map(String::length);

// Transformar para outro Optional
Optional<Cliente> clienteOpt = contaOpt.flatMap(conta -> Optional.ofNullable(conta.getTitular()));

// Filtrar valor
Optional<String> filtrado = optional.filter(s -> s.length() > 5);
```

### Exemplo no Projeto: Uso de Optional

```java
public class ContaService {
    private Map<String, Conta> contasPorNumero;
    
    public Optional<Conta> buscarPorNumero(String numero) {
        return Optional.ofNullable(contasPorNumero.get(numero));
    }
    
    public BigDecimal obterSaldo(String numero) {
        return buscarPorNumero(numero)
            .map(Conta::getSaldo)
            .orElse(BigDecimal.ZERO);
    }
    
    public void processarConta(String numero) {
        buscarPorNumero(numero)
            .filter(Conta::isAtiva)
            .ifPresentOrElse(
                this::processarContaAtiva,
                () -> System.out.println("Conta não encontrada ou inativa")
            );
    }
    
    private void processarContaAtiva(Conta conta) {
        // Processamento da conta
    }
}
```

## Conclusão

As coleções e os recursos de programação funcional em Java fornecem ferramentas poderosas para manipular e processar dados de forma eficiente e expressiva. O Java Collections Framework oferece estruturas de dados para diferentes necessidades, enquanto lambdas, streams e optional permitem um estilo de programação mais declarativo e conciso.

No projeto BancoFicticio, esses recursos são utilizados para:
- Armazenar e organizar contas, clientes e outras entidades
- Processar dados de forma eficiente e expressiva
- Evitar erros comuns como NullPointerException
- Escrever código mais limpo e manutenível

Ao dominar esses conceitos, você poderá escrever código Java mais elegante, eficiente e menos propenso a erros. 