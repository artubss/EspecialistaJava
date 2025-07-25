# Glossário Completo de Java - Sistema Bancário

Este glossário apresenta os principais conceitos, estruturas, classes e funções de Java utilizados no projeto BancoFicticio, com explicações detalhadas e exemplos práticos.

## Conceitos Fundamentais de Programação Orientada a Objetos

### Classes e Objetos

**Definição:** Uma classe é um modelo ou template para criar objetos, definindo atributos (dados) e métodos (comportamentos). Um objeto é uma instância específica de uma classe.

**Exemplo no projeto:**

```java
// Definição da classe Conta (abstrata)
public abstract class Conta {
    private String numero;
    private BigDecimal saldo;
    // ...

    public void depositar(BigDecimal valor) {
        // implementação
    }
}

// Criação de objetos (instâncias)
Cliente cliente = new Cliente("João da Silva", "123.456.789-00", LocalDate.of(1990, 1, 1));
ContaCorrente cc = new ContaCorrente("1111", cliente, new BigDecimal("1000.00"));
```

**Explicação:** No exemplo acima, `Conta` é uma classe que define a estrutura de uma conta bancária. Na classe `Main`, criamos objetos específicos como um cliente chamado João e uma conta corrente com número "1111".

### Encapsulamento

**Definição:** Técnica que esconde os detalhes internos de implementação e protege os dados, fornecendo apenas uma interface pública para interagir com o objeto.

**Exemplo no projeto:**

```java
public class Cliente {
    // Atributo privado
    private String nome;

    // Método público para acessar (getter)
    public String getNome() {
        return nome;
    }

    // Método público para modificar (setter)
    public void setNome(String nome) {
        this.nome = Objects.requireNonNull(nome, "Nome não pode ser nulo");
    }
}
```

**Explicação:** A classe `Cliente` encapsula o atributo `nome` tornando-o privado (`private`). O acesso e modificação só são possíveis através dos métodos públicos `getNome()` e `setNome()`. O setter inclui validação para garantir que o nome não seja nulo, protegendo a integridade dos dados.

### Herança

**Definição:** Mecanismo que permite que uma classe herde atributos e métodos de outra classe, estabelecendo uma relação "é um" entre elas.

**Exemplo no projeto:**

```java
// Superclasse (classe pai)
public abstract class Conta {
    protected String numero;
    protected BigDecimal saldo;

    public void depositar(BigDecimal valor) {
        // implementação
    }
}

// Subclasse (classe filha)
public class ContaCorrente extends Conta {
    private BigDecimal limiteChequeEspecial;

    @Override
    public void sacar(BigDecimal valor) {
        // implementação específica
    }
}
```

**Explicação:** `ContaCorrente` herda de `Conta`, o que significa que uma conta corrente "é uma" conta. A subclasse herda os atributos (`numero`, `saldo`) e métodos (`depositar`) da superclasse, e pode adicionar seus próprios atributos (`limiteChequeEspecial`) e sobrescrever métodos (`sacar`).

### Polimorfismo

**Definição:** Capacidade de objetos de diferentes classes serem tratados como objetos de uma classe comum, permitindo que métodos se comportem de maneira diferente dependendo do tipo real do objeto.

**Exemplo no projeto:**

```java
// Diferentes implementações do mesmo método
public abstract class Conta {
    public abstract void calcularTarifaMensal();
}

public class ContaCorrente extends Conta {
    @Override
    public void calcularTarifaMensal() {
        setSaldo(getSaldo().subtract(TARIFA_MENSAL)); // Tarifa de R$ 30,00
    }
}

public class ContaPoupanca extends Conta {
    @Override
    public void calcularTarifaMensal() {
        setSaldo(getSaldo().subtract(TARIFA_MENSAL)); // Tarifa zero
    }
}

// Uso polimórfico
List<Conta> contas = cliente.getContas();
contas.forEach(Conta::calcularTarifaMensal); // Chama o método apropriado para cada tipo de conta
```

**Explicação:** Embora todas as contas tenham o método `calcularTarifaMensal()`, cada tipo de conta implementa esse método de forma diferente. Quando chamamos o método em uma lista de contas, o Java automaticamente executa a implementação correta para cada objeto, demonstrando polimorfismo.

### Classes Abstratas

**Definição:** Uma classe que não pode ser instanciada diretamente e pode conter métodos abstratos (sem implementação) que devem ser implementados pelas subclasses.

**Exemplo no projeto:**

```java
public abstract class Conta {
    // Método concreto (com implementação)
    public void depositar(BigDecimal valor) {
        this.saldo = this.saldo.add(valor);
    }

    // Método abstrato (sem implementação)
    public abstract void calcularTarifaMensal();
}
```

**Explicação:** A classe `Conta` é abstrata, o que significa que não podemos criar objetos diretamente dela (não podemos fazer `new Conta()`). Ela define o método abstrato `calcularTarifaMensal()` que todas as subclasses concretas (como `ContaCorrente` e `ContaPoupanca`) devem implementar.

### Interfaces

**Definição:** Um contrato que define um conjunto de métodos que uma classe deve implementar, permitindo que classes não relacionadas por herança compartilhem comportamentos comuns.

**Exemplo no projeto:**

```java
public interface Tributavel {
    BigDecimal calcularImposto();

    default boolean isento() {
        return calcularImposto().compareTo(BigDecimal.ZERO) == 0;
    }
}

public class ContaInvestimento extends Conta implements Tributavel {
    @Override
    public BigDecimal calcularImposto() {
        return getSaldo().multiply(tipo.getAliquotaImposto())
                .setScale(2, RoundingMode.HALF_EVEN);
    }
}
```

**Explicação:** A interface `Tributavel` define um contrato que qualquer classe tributável deve implementar o método `calcularImposto()`. A classe `ContaInvestimento` implementa esta interface, fornecendo sua própria lógica para cálculo de impostos. A interface também inclui um método `default` que fornece uma implementação padrão.

## Estruturas de Dados e Coleções

### Arrays

**Definição:** Estrutura que armazena elementos do mesmo tipo em posições contíguas de memória, com tamanho fixo definido na criação.

**Exemplo:**

```java
// Declaração e inicialização
String[] nomes = new String[3];
nomes[0] = "João";
nomes[1] = "Maria";
nomes[2] = "Pedro";

// Alternativa
String[] nomes = {"João", "Maria", "Pedro"};

// Iteração
for (String nome : nomes) {
    System.out.println(nome);
}
```

**Explicação:** O array `nomes` armazena 3 strings. Os elementos são acessados por índice (começando em 0). O tamanho do array é fixo após a criação.

### ArrayList

**Definição:** Implementação de lista baseada em array que pode crescer dinamicamente, parte do Java Collections Framework.

**Exemplo no projeto:**

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
```

**Explicação:** A classe `Cliente` usa um `ArrayList` para armazenar as contas do cliente. O método `add()` adiciona uma nova conta à lista. O método `getContas()` retorna uma visão não modificável da lista para proteger os dados.

### HashMap

**Definição:** Implementação de mapa que armazena pares chave-valor, permitindo acesso rápido aos valores através de suas chaves.

**Exemplo:**

```java
// Criação e uso de HashMap
Map<String, Conta> contasPorNumero = new HashMap<>();
contasPorNumero.put("1111", contaCorrente);
contasPorNumero.put("2222", contaPoupanca);

// Recuperação
Conta conta = contasPorNumero.get("1111");

// Verificação
if (contasPorNumero.containsKey("3333")) {
    // código
}
```

**Explicação:** O `HashMap` armazena contas indexadas por seu número. O método `put()` associa uma chave a um valor, e `get()` recupera o valor associado a uma chave. A busca é muito eficiente (tempo constante em média).

## Tipos e Valores

### Tipos Primitivos

**Definição:** Tipos básicos incorporados na linguagem Java que representam valores simples.

**Exemplo:**

```java
byte b = 127;         // 8 bits (-128 a 127)
short s = 32767;      // 16 bits (-32,768 a 32,767)
int i = 2147483647;   // 32 bits (-2^31 a 2^31-1)
long l = 9223372036854775807L; // 64 bits (-2^63 a 2^63-1)
float f = 3.14f;      // 32 bits, ponto flutuante
double d = 3.14159;   // 64 bits, ponto flutuante
boolean bool = true;  // true ou false
char c = 'A';         // 16 bits, caractere Unicode
```

**Explicação:** Estes são os 8 tipos primitivos em Java. Eles armazenam valores diretamente, não são objetos, e têm tamanhos fixos na memória.

### Wrapper Classes

**Definição:** Classes que encapsulam tipos primitivos em um objeto, permitindo que sejam usados onde objetos são necessários.

**Exemplo:**

```java
Integer numero = 42;  // Autoboxing (int -> Integer)
int valor = numero;   // Unboxing (Integer -> int)

List<Integer> numeros = new ArrayList<>();
numeros.add(10);      // Autoboxing acontece aqui
int primeiro = numeros.get(0); // Unboxing acontece aqui
```

**Explicação:** As wrapper classes (`Integer`, `Double`, etc.) permitem tratar tipos primitivos como objetos. Autoboxing e unboxing são conversões automáticas entre tipos primitivos e seus wrappers.

### BigDecimal

**Definição:** Classe para representação precisa de valores decimais, essencial para cálculos financeiros.

**Exemplo no projeto:**

```java
public class ContaCorrente extends Conta {
    private static final BigDecimal TARIFA_MENSAL = new BigDecimal("30.00");

    public void calcularTarifaMensal() {
        setSaldo(getSaldo().subtract(TARIFA_MENSAL));
    }
}
```

**Explicação:** `BigDecimal` é usado para representar valores monetários com precisão. Ao contrário de `float` ou `double`, não sofre de problemas de arredondamento. Note que o valor é criado a partir de uma string (`"30.00"`) para garantir a precisão exata.

### Enumerações (Enum)

**Definição:** Tipo especial que representa um conjunto fixo de constantes nomeadas.

**Exemplo no projeto:**

```java
public enum TipoInvestimento {
    RENDA_FIXA("RF", new BigDecimal("0.015"), new BigDecimal("0.001")),
    RENDA_VARIAVEL("RV", new BigDecimal("0.175"), new BigDecimal("0.002")),
    TESOURO_DIRETO("TD", new BigDecimal("0.225"), new BigDecimal("0.0005"));

    private final String codigo;
    private final BigDecimal aliquotaImposto;
    private final BigDecimal taxaAdministracao;

    // construtor e métodos
}
```

**Explicação:** O enum `TipoInvestimento` define três constantes (RENDA_FIXA, RENDA_VARIAVEL, TESOURO_DIRETO) com valores associados. Cada constante é um objeto único do tipo `TipoInvestimento`.

## Exceções e Tratamento de Erros

### Try-Catch-Finally

**Definição:** Estrutura para capturar e tratar exceções, permitindo que o programa continue executando mesmo após um erro.

**Exemplo no projeto:**

```java
try {
    // Código que pode lançar exceção
    cc.sacar(new BigDecimal("2000.00"));
} catch (SaldoInsuficienteException e) {
    // Tratamento específico para saldo insuficiente
    System.err.println("Erro: " + e.getMessage());
} catch (Exception e) {
    // Tratamento genérico para outras exceções
    System.err.println("Erro inesperado: " + e.getMessage());
} finally {
    // Código que sempre executa, independente de exceção
    System.out.println("Operação finalizada");
}
```

**Explicação:** O bloco `try` contém código que pode lançar exceções. Os blocos `catch` capturam e tratam exceções específicas. O bloco `finally` contém código que sempre executa, mesmo se houver uma exceção.

### Exceções Personalizadas

**Definição:** Classes de exceção criadas pelo desenvolvedor para representar erros específicos do domínio da aplicação.

**Exemplo no projeto:**

```java
public class SaldoInsuficienteException extends RuntimeException {
    private final BigDecimal saldoAtual;
    private final BigDecimal valorSolicitado;

    public SaldoInsuficienteException(String message, BigDecimal saldoAtual, BigDecimal valorSolicitado) {
        super(message);
        this.saldoAtual = saldoAtual;
        this.valorSolicitado = valorSolicitado;
    }

    // getters
}
```

**Explicação:** `SaldoInsuficienteException` é uma exceção personalizada que carrega informações específicas sobre o erro (saldo atual e valor solicitado). Estende `RuntimeException`, o que significa que é uma exceção não verificada (não precisa ser declarada ou capturada explicitamente).

## Programação Funcional

### Expressões Lambda

**Definição:** Funções anônimas que podem ser passadas como argumentos ou armazenadas em variáveis.

**Exemplo:**

```java
// Lambda como predicado
Predicate<Conta> contasAtivas = conta -> conta.isAtiva();

// Lambda como consumer
Consumer<Conta> exibirSaldo = conta ->
    System.out.println("Saldo da conta " + conta.getNumero() + ": " + conta.getSaldo());

// Uso com streams
List<Conta> contasComSaldoPositivo = contas.stream()
    .filter(conta -> conta.getSaldo().compareTo(BigDecimal.ZERO) > 0)
    .collect(Collectors.toList());
```

**Explicação:** As expressões lambda permitem escrever código mais conciso. A expressão `conta -> conta.isAtiva()` é uma função que recebe uma conta e retorna se ela está ativa.

### Stream API

**Definição:** API para processar sequências de elementos de forma declarativa, frequentemente combinada com lambdas.

**Exemplo:**

```java
// Filtrar contas com saldo positivo
List<String> numerosContasPositivas = contas.stream()
    .filter(c -> c.getSaldo().compareTo(BigDecimal.ZERO) > 0)
    .map(Conta::getNumero)
    .collect(Collectors.toList());

// Calcular saldo total
BigDecimal saldoTotal = contas.stream()
    .map(Conta::getSaldo)
    .reduce(BigDecimal.ZERO, BigDecimal::add);

// Agrupar contas por tipo
Map<Class<?>, List<Conta>> contasPorTipo = contas.stream()
    .collect(Collectors.groupingBy(Conta::getClass));
```

**Explicação:** A Stream API permite operações de alto nível como filtrar, mapear, reduzir e coletar elementos. O código fica mais legível e expressivo, focando no "o quê" em vez do "como".

### Optional

**Definição:** Container que pode ou não conter um valor não-nulo, ajudando a evitar NullPointerException.

**Exemplo:**

```java
// Buscar uma conta que pode não existir
Optional<Conta> contaOpt = contas.stream()
    .filter(c -> c.getNumero().equals(numero))
    .findFirst();

// Uso seguro do Optional
contaOpt.ifPresent(conta -> {
    System.out.println("Conta encontrada: " + conta.getNumero());
});

// Obter valor com fallback
Conta conta = contaOpt.orElse(new ContaPadrao());

// Lançar exceção se não existir
Conta conta = contaOpt.orElseThrow(() ->
    new ContaNaoEncontradaException("Conta não encontrada: " + numero));
```

**Explicação:** `Optional` ajuda a lidar explicitamente com valores que podem ser nulos. Em vez de verificar nulos com `if (conta != null)`, usamos métodos como `ifPresent`, `orElse` e `orElseThrow`.

## Conceitos Avançados

### Serialização

**Definição:** Processo de converter objetos em sequência de bytes para armazenamento ou transmissão.

**Exemplo no projeto:**

```java
public class Cliente implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nome;
    private String cpf;
    // ...
}
```

**Explicação:** A classe `Cliente` implementa a interface `Serializable`, permitindo que seus objetos sejam convertidos em bytes. O campo `serialVersionUID` é um identificador de versão usado durante a deserialização.

### Generics

**Definição:** Recurso que permite criar classes, interfaces e métodos que operam com tipos parametrizados.

**Exemplo:**

```java
// Uso de generics em coleções
List<Conta> contas = new ArrayList<>();
Map<String, Cliente> clientesPorCpf = new HashMap<>();

// Método genérico
public <T extends Conta> BigDecimal calcularSaldoTotal(List<T> contas) {
    return contas.stream()
        .map(Conta::getSaldo)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
}
```

**Explicação:** Generics permitem escrever código que funciona com diferentes tipos de forma type-safe. No exemplo, `List<Conta>` é uma lista que só aceita objetos do tipo `Conta` ou subclasses.

### Anotações

**Definição:** Metadados que podem ser adicionados ao código Java para fornecer informações adicionais ao compilador, ferramentas de desenvolvimento ou runtime.

**Exemplo:**

```java
@Override
public void calcularTarifaMensal() {
    setSaldo(getSaldo().subtract(TARIFA_MENSAL));
}

@Deprecated
public void metodoAntigo() {
    // implementação
}
```

**Explicação:** `@Override` indica que o método está sobrescrevendo um método da superclasse. `@Deprecated` marca um método como obsoleto, gerando um aviso quando é usado.

## Padrões de Projeto

### Repository Pattern

**Definição:** Padrão que separa a lógica de acesso a dados do resto da aplicação.

**Exemplo:**

```java
public interface ContaRepository {
    Optional<Conta> findByNumero(String numero);
    List<Conta> findByTitular(Cliente titular);
    void save(Conta conta);
    void delete(Conta conta);
}
```

**Explicação:** O padrão Repository encapsula a lógica de armazenamento, recuperação e busca de objetos. A interface define operações sem expor detalhes de implementação.

### Factory Pattern

**Definição:** Padrão criacional que fornece uma interface para criar objetos sem especificar suas classes concretas.

**Exemplo:**

```java
public class ContaFactory {
    public static Conta criarConta(TipoConta tipo, Cliente titular) {
        switch (tipo) {
            case CORRENTE:
                return new ContaCorrente(titular);
            case POUPANCA:
                return new ContaPoupanca(titular);
            case INVESTIMENTO:
                return new ContaInvestimento(titular);
            default:
                throw new IllegalArgumentException("Tipo de conta inválido");
        }
    }
}
```

**Explicação:** O Factory Method encapsula a criação de objetos. O cliente solicita um tipo de conta e a factory decide qual classe concreta instanciar.

### Builder Pattern

**Definição:** Padrão que separa a construção de um objeto complexo da sua representação.

**Exemplo:**

```java
public class ClienteBuilder {
    private String nome;
    private String cpf;
    private LocalDate dataNascimento;

    public ClienteBuilder comNome(String nome) {
        this.nome = nome;
        return this;
    }

    public ClienteBuilder comCPF(String cpf) {
        this.cpf = cpf;
        return this;
    }

    public Cliente build() {
        return new Cliente(nome, cpf, dataNascimento);
    }
}

// Uso
Cliente cliente = new ClienteBuilder()
    .comNome("João")
    .comCPF("123.456.789-00")
    .build();
```

**Explicação:** O Builder permite construir objetos complexos passo a passo. Cada método retorna o próprio builder, permitindo encadear chamadas (method chaining).
