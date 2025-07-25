# Encapsulamento e Interfaces em Java

Este documento explora em detalhes os conceitos de encapsulamento e interfaces em Java, com exemplos práticos do projeto BancoFicticio.

## Encapsulamento

### O que é Encapsulamento?

Encapsulamento é um dos quatro pilares fundamentais da programação orientada a objetos (junto com herança, polimorfismo e abstração). Consiste em esconder os detalhes internos de implementação de uma classe e fornecer uma interface pública controlada para interagir com ela.

Os principais objetivos do encapsulamento são:

1. **Proteção de dados:** Impedir acesso direto e modificações não autorizadas aos atributos
2. **Controle de acesso:** Definir como os dados podem ser acessados e modificados
3. **Flexibilidade:** Permitir alterar a implementação interna sem afetar o código que usa a classe
4. **Validação:** Garantir que os dados estejam sempre em um estado válido

### Modificadores de Acesso em Java

Java oferece quatro níveis de controle de acesso:

| Modificador | Classe | Pacote | Subclasse | Mundo |
|-------------|--------|--------|-----------|-------|
| `private`   | ✓      | ✗      | ✗         | ✗     |
| `default`   | ✓      | ✓      | ✗         | ✗     |
| `protected` | ✓      | ✓      | ✓         | ✗     |
| `public`    | ✓      | ✓      | ✓         | ✓     |

### Exemplo no Projeto: Encapsulamento na Classe Cliente

```java
public class Cliente implements Serializable {
    // Atributos privados - não acessíveis diretamente de fora da classe
    private String nome;
    private String cpf;
    private LocalDate dataNascimento;
    private List<Conta> contas;

    // Construtor
    public Cliente(String nome, String cpf, LocalDate dataNascimento) {
        this.nome = nome;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.contas = new ArrayList<>();
    }

    // Métodos públicos para acesso controlado (getters)
    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    // Método público para modificação controlada (setter)
    public void setNome(String nome) {
        this.nome = Objects.requireNonNull(nome, "Nome não pode ser nulo");
    }

    // Método que retorna uma visão imutável da lista de contas
    public List<Conta> getContas() {
        return Collections.unmodifiableList(contas);
    }

    // Métodos para manipular a lista de contas de forma controlada
    public void adicionarConta(Conta conta) {
        Objects.requireNonNull(conta, "Conta não pode ser nula");
        this.contas.add(conta);
    }

    public void removerConta(Conta conta) {
        this.contas.remove(conta);
    }
}
```

Neste exemplo:

1. **Atributos privados:** `nome`, `cpf`, `dataNascimento` e `contas` são declarados como `private`
2. **Getters:** Métodos como `getNome()` permitem ler os valores dos atributos
3. **Setters com validação:** `setNome()` verifica se o valor não é nulo antes de atribuir
4. **Imutabilidade seletiva:** Não há setter para `cpf`, tornando-o imutável após a criação
5. **Coleção protegida:** `getContas()` retorna uma visão não modificável da lista
6. **Métodos especializados:** `adicionarConta()` e `removerConta()` controlam o acesso à coleção

### Benefícios do Encapsulamento Demonstrados

#### 1. Validação Centralizada

```java
public void setNome(String nome) {
    this.nome = Objects.requireNonNull(nome, "Nome não pode ser nulo");
}
```

Benefícios:
- Garante que `nome` nunca será nulo
- Centraliza a validação em um único lugar
- Se a regra mudar (ex: tamanho mínimo), só precisamos alterar aqui

#### 2. Ocultação de Detalhes de Implementação

```java
private List<Conta> contas;

public List<Conta> getContas() {
    return Collections.unmodifiableList(contas);
}
```

Benefícios:
- O tipo de coleção usado (`ArrayList`) está oculto
- Podemos mudar para outro tipo de lista sem afetar os usuários da classe
- A lista original não pode ser modificada diretamente

#### 3. Controle de Modificações

```java
public void adicionarConta(Conta conta) {
    Objects.requireNonNull(conta, "Conta não pode ser nula");
    this.contas.add(conta);
}
```

Benefícios:
- Controla como as contas são adicionadas
- Garante validação antes da adição
- Permite adicionar lógica extra no futuro (ex: verificar duplicatas)

#### 4. Imutabilidade Seletiva

```java
public String getCpf() {
    return cpf;
}
// Não há setCpf() - CPF não pode ser alterado após a criação
```

Benefícios:
- Protege dados que não devem mudar
- Evita alterações acidentais
- Torna o código mais previsível

### Padrão JavaBeans

O padrão JavaBeans é uma convenção de nomenclatura e design para classes Java:

1. Construtor sem argumentos (ou construtor explícito)
2. Propriedades acessíveis através de getters e setters
3. Métodos seguindo a convenção de nomenclatura: `getNomeDaPropriedade()` e `setNomeDaPropriedade()`
4. Implementação da interface `Serializable`

A classe `Cliente` segue parcialmente este padrão:
- Tem getters e setters convencionais
- Implementa `Serializable`
- Mas usa um construtor com argumentos em vez de um construtor sem argumentos

## Interfaces

### O que são Interfaces?

Uma interface em Java é um tipo de referência, similar a uma classe, que pode conter:
- Constantes
- Métodos abstratos (sem implementação)
- Métodos default (com implementação padrão, a partir do Java 8)
- Métodos estáticos (a partir do Java 8)
- Métodos privados (a partir do Java 9)

As interfaces definem um contrato que as classes implementadoras devem seguir, especificando quais métodos devem ser implementados, sem ditar como devem ser implementados.

### Sintaxe de Interfaces

```java
public interface NomeDaInterface {
    // Constantes (implicitamente public static final)
    int VALOR_MAXIMO = 100;
    
    // Métodos abstratos (implicitamente public abstract)
    void metodo1();
    String metodo2(int parametro);
    
    // Método default (Java 8+)
    default void metodoDefault() {
        // implementação padrão
    }
    
    // Método estático (Java 8+)
    static void metodoEstatico() {
        // implementação
    }
    
    // Método privado (Java 9+)
    private void metodoPrivado() {
        // implementação auxiliar
    }
}
```

### Exemplo no Projeto: Interface Tributavel

```java
public interface Tributavel {
    // Método abstrato - deve ser implementado pelas classes
    BigDecimal calcularImposto();
    
    // Método default - implementação padrão
    default boolean isento() {
        return calcularImposto().compareTo(BigDecimal.ZERO) == 0;
    }
}
```

### Implementação de Interfaces

Uma classe implementa uma interface usando a palavra-chave `implements`:

```java
public class ContaInvestimento extends Conta implements Tributavel {
    private TipoInvestimento tipo;
    private BigDecimal taxaAdministracao;
    
    // Implementação do método definido na interface
    @Override
    public BigDecimal calcularImposto() {
        return getSaldo().multiply(tipo.getAliquotaImposto())
                .setScale(2, RoundingMode.HALF_EVEN);
    }
    
    // Não precisamos implementar isento() porque ele tem uma implementação default
}
```

### Interfaces vs. Classes Abstratas

Embora interfaces e classes abstratas possam parecer similares, elas têm propósitos e características diferentes:

| Característica | Interface | Classe Abstrata |
|----------------|-----------|-----------------|
| Instanciação | Não pode ser instanciada | Não pode ser instanciada |
| Herança | Uma classe pode implementar múltiplas interfaces | Uma classe só pode estender uma classe |
| Atributos | Apenas constantes (public static final) | Qualquer tipo de atributo |
| Construtores | Não tem construtores | Pode ter construtores |
| Métodos | Abstratos, default, static, private | Qualquer tipo de método |
| Acesso | Todos os métodos são implicitamente públicos | Métodos podem ter qualquer modificador |
| Objetivo principal | Definir um contrato | Fornecer uma base comum |

### Evolução das Interfaces em Java

#### Java 7 e Anteriores
- Apenas constantes e métodos abstratos

```java
public interface Tributavel {
    BigDecimal calcularImposto();
}
```

#### Java 8
- Adicionados métodos default e static

```java
public interface Tributavel {
    BigDecimal calcularImposto();
    
    default boolean isento() {
        return calcularImposto().compareTo(BigDecimal.ZERO) == 0;
    }
    
    static Tributavel criarTributavelPadrao() {
        return () -> BigDecimal.ZERO;
    }
}
```

#### Java 9
- Adicionados métodos privados

```java
public interface Tributavel {
    BigDecimal calcularImposto();
    
    default boolean isento() {
        return verificarValor(calcularImposto());
    }
    
    // Método privado auxiliar
    private boolean verificarValor(BigDecimal valor) {
        return valor.compareTo(BigDecimal.ZERO) == 0;
    }
}
```

### Interfaces Funcionais

Uma interface funcional é uma interface que contém exatamente um método abstrato. Elas são a base para expressões lambda em Java.

```java
@FunctionalInterface
public interface Calculadora {
    BigDecimal calcular(BigDecimal valor1, BigDecimal valor2);
}

// Uso com classe anônima
Calculadora soma = new Calculadora() {
    @Override
    public BigDecimal calcular(BigDecimal valor1, BigDecimal valor2) {
        return valor1.add(valor2);
    }
};

// Uso com lambda
Calculadora multiplicacao = (valor1, valor2) -> valor1.multiply(valor2);

// Chamada
BigDecimal resultado1 = soma.calcular(new BigDecimal("10"), new BigDecimal("5"));        // 15
BigDecimal resultado2 = multiplicacao.calcular(new BigDecimal("10"), new BigDecimal("5")); // 50
```

### Interfaces Predefinidas em Java

Java fornece várias interfaces funcionais úteis no pacote `java.util.function`:

```java
// Predicate - recebe um objeto e retorna um boolean
Predicate<Conta> contaAtiva = conta -> conta.isAtiva();
List<Conta> contasAtivas = contas.stream().filter(contaAtiva).collect(Collectors.toList());

// Consumer - recebe um objeto e não retorna nada
Consumer<Conta> exibirSaldo = conta -> 
    System.out.println("Saldo da conta " + conta.getNumero() + ": " + conta.getSaldo());
contas.forEach(exibirSaldo);

// Function - recebe um objeto e retorna outro
Function<Conta, String> extrairNumero = Conta::getNumero;
List<String> numerosContas = contas.stream().map(extrairNumero).collect(Collectors.toList());

// Supplier - não recebe nada e retorna um objeto
Supplier<LocalDate> hoje = LocalDate::now;
LocalDate dataAtual = hoje.get();
```

### Herança Múltipla de Interfaces

Uma classe pode implementar múltiplas interfaces:

```java
public interface Tributavel {
    BigDecimal calcularImposto();
}

public interface Rentavel {
    BigDecimal calcularRendimento();
}

public class ContaInvestimento extends Conta implements Tributavel, Rentavel {
    @Override
    public BigDecimal calcularImposto() {
        // implementação
    }
    
    @Override
    public BigDecimal calcularRendimento() {
        // implementação
    }
}
```

Uma interface também pode estender múltiplas interfaces:

```java
public interface ContaEspecial extends Tributavel, Rentavel {
    void aplicarBeneficiosEspeciais();
}
```

### Conflitos em Interfaces

Quando uma classe implementa múltiplas interfaces que têm métodos default com o mesmo nome, ocorre um conflito que deve ser resolvido explicitamente:

```java
public interface A {
    default void metodo() {
        System.out.println("A");
    }
}

public interface B {
    default void metodo() {
        System.out.println("B");
    }
}

public class C implements A, B {
    // Precisamos resolver o conflito explicitamente
    @Override
    public void metodo() {
        A.super.metodo(); // Escolhemos a implementação de A
        // ou B.super.metodo(); // Ou a implementação de B
        // ou implementação própria
    }
}
```

## Exercícios Práticos

### Exercício 1: Melhorar o Encapsulamento

Melhore o encapsulamento da classe `ContaCorrente` adicionando validações:

```java
public class ContaCorrente extends Conta {
    private BigDecimal limiteChequeEspecial;
    
    public ContaCorrente(String numero, Cliente titular, BigDecimal limiteChequeEspecial) {
        super(numero, titular);
        setLimiteChequeEspecial(limiteChequeEspecial); // Usa o setter para validação
    }
    
    public BigDecimal getLimiteChequeEspecial() {
        return limiteChequeEspecial;
    }
    
    public void setLimiteChequeEspecial(BigDecimal limiteChequeEspecial) {
        // Validação: limite não pode ser negativo
        if (limiteChequeEspecial.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Limite do cheque especial não pode ser negativo");
        }
        this.limiteChequeEspecial = limiteChequeEspecial.setScale(2, RoundingMode.HALF_EVEN);
    }
}
```

### Exercício 2: Criar uma Interface Notificavel

Crie uma interface para notificações e implemente-a:

```java
public interface Notificavel {
    void enviarNotificacao(String mensagem);
    
    default void notificarMovimentacao(BigDecimal valor, String tipo) {
        String mensagem = String.format(
            "Sua conta teve uma %s de %s em %s",
            tipo,
            NumberFormat.getCurrencyInstance().format(valor),
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
        );
        enviarNotificacao(mensagem);
    }
}

public class Cliente implements Serializable, Notificavel {
    // Atributos existentes
    private String email;
    private String telefone;
    
    // Construtor e outros métodos
    
    @Override
    public void enviarNotificacao(String mensagem) {
        if (email != null && !email.isEmpty()) {
            System.out.println("Enviando e-mail para " + email + ": " + mensagem);
        } else if (telefone != null && !telefone.isEmpty()) {
            System.out.println("Enviando SMS para " + telefone + ": " + mensagem);
        } else {
            System.out.println("Não foi possível enviar notificação: contato não disponível");
        }
    }
    
    // Getters e setters para email e telefone
}
```

### Exercício 3: Usar Interface Funcional

Use interfaces funcionais para processar contas:

```java
public class ProcessadorContas {
    public void processarContas(List<Conta> contas, Predicate<Conta> filtro, Consumer<Conta> operacao) {
        contas.stream()
            .filter(filtro)
            .forEach(operacao);
    }
}

// Uso
ProcessadorContas processador = new ProcessadorContas();

// Aplicar rendimento a contas poupança com saldo > 1000
processador.processarContas(
    cliente.getContas(),
    conta -> conta instanceof ContaPoupanca && conta.getSaldo().compareTo(new BigDecimal("1000")) > 0,
    conta -> {
        ContaPoupanca cp = (ContaPoupanca) conta;
        cp.calcularRendimento();
        System.out.println("Rendimento aplicado na conta " + cp.getNumero());
    }
);

// Cobrar tarifa de todas as contas correntes
processador.processarContas(
    cliente.getContas(),
    conta -> conta instanceof ContaCorrente,
    Conta::calcularTarifaMensal
);
```

## Conclusão

Encapsulamento e interfaces são conceitos fundamentais em Java que promovem código mais seguro, flexível e reutilizável:

- **Encapsulamento** protege os dados, centraliza a validação e oculta detalhes de implementação
- **Interfaces** definem contratos, permitem polimorfismo e facilitam a extensão do código

Ao dominar esses conceitos, você poderá criar sistemas mais robustos e manuteníveis, com componentes bem definidos e baixo acoplamento entre eles.

Lembre-se:
1. Sempre encapsule seus dados usando atributos privados e métodos de acesso
2. Valide os dados nos setters e construtores
3. Use interfaces para definir comportamentos que podem ser implementados por diferentes classes
4. Aproveite os recursos modernos de interfaces (métodos default, static e private)
5. Considere interfaces funcionais para código mais conciso e expressivo 