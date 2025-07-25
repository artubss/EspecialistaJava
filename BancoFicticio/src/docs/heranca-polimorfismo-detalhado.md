# Herança e Polimorfismo em Java

Este documento explora em profundidade os conceitos de herança e polimorfismo em Java, com exemplos detalhados do projeto BancoFicticio.

## Herança

### O que é Herança?

Herança é um dos pilares fundamentais da programação orientada a objetos. Ela permite que uma classe (subclasse) herde atributos e métodos de outra classe (superclasse), estabelecendo uma relação "é um" entre elas. A herança promove a reutilização de código e a criação de hierarquias de classes.

### Sintaxe da Herança em Java

Em Java, usamos a palavra-chave `extends` para estabelecer uma relação de herança:

```java
public class Superclasse {
    // atributos e métodos
}

public class Subclasse extends Superclasse {
    // atributos e métodos adicionais
}
```

### Exemplo no Projeto: Hierarquia de Contas

No projeto BancoFicticio, temos uma hierarquia de classes de conta:

```java
// Classe base (superclasse)
public abstract class Conta {
    private String numero;
    private BigDecimal saldo;
    private Cliente titular;
    // ...
}

// Subclasses
public class ContaCorrente extends Conta {
    private BigDecimal limiteChequeEspecial;
    // ...
}

public class ContaPoupanca extends Conta {
    private BigDecimal taxaRendimento;
    // ...
}

public class ContaInvestimento extends Conta {
    private TipoInvestimento tipo;
    // ...
}
```

Neste exemplo:
- `Conta` é a superclasse (classe pai)
- `ContaCorrente`, `ContaPoupanca` e `ContaInvestimento` são subclasses (classes filhas)
- Cada subclasse herda os atributos e métodos da superclasse
- Cada subclasse adiciona seus próprios atributos e comportamentos específicos

### Construtores e Herança

Quando criamos um objeto de uma subclasse, o construtor da superclasse é chamado primeiro:

```java
public class Conta {
    private String numero;
    private Cliente titular;
    
    public Conta(String numero, Cliente titular) {
        this.numero = Objects.requireNonNull(numero, "Número não pode ser nulo");
        this.titular = Objects.requireNonNull(titular, "Titular não pode ser nulo");
        this.saldo = BigDecimal.ZERO;
        this.dataCriacao = LocalDateTime.now();
        this.ativa = true;
    }
    // ...
}

public class ContaCorrente extends Conta {
    private BigDecimal limiteChequeEspecial;
    
    public ContaCorrente(String numero, Cliente titular, BigDecimal limiteChequeEspecial) {
        super(numero, titular); // Chama o construtor da superclasse
        this.limiteChequeEspecial = limiteChequeEspecial.setScale(2, RoundingMode.HALF_EVEN);
    }
    // ...
}
```

Pontos importantes:
1. A chamada ao construtor da superclasse (`super(...)`) deve ser a primeira instrução no construtor da subclasse
2. Se não chamarmos explicitamente o construtor da superclasse, Java tentará chamar o construtor padrão (sem argumentos)
3. Se a superclasse não tiver um construtor padrão, somos obrigados a chamar explicitamente um de seus construtores

### Modificador de Acesso `protected`

O modificador `protected` é especialmente útil em herança:

```java
public abstract class Conta {
    protected BigDecimal saldo; // Acessível para subclasses
    
    protected void setSaldo(BigDecimal saldo) {
        this.saldo = Objects.requireNonNull(saldo, "Saldo não pode ser nulo");
    }
    // ...
}

public class ContaCorrente extends Conta {
    @Override
    public void calcularTarifaMensal() {
        setSaldo(getSaldo().subtract(TARIFA_MENSAL)); // Acessa método protected
    }
    // ...
}
```

- Membros `protected` são acessíveis na própria classe, em subclasses e em classes do mesmo pacote
- É mais restritivo que `public` (acessível em qualquer lugar)
- É menos restritivo que `private` (acessível apenas na própria classe)

### Palavra-chave `super`

A palavra-chave `super` é usada para:

1. Chamar construtores da superclasse: `super(numero, titular);`
2. Acessar métodos da superclasse: `super.depositar(valor);`
3. Acessar atributos da superclasse: `super.saldo` (se acessível)

Exemplo prático:

```java
public class ContaCorrente extends Conta {
    @Override
    public void sacar(BigDecimal valor) {
        BigDecimal saldoTotal = getSaldo().add(limiteChequeEspecial);
        if (valor.compareTo(saldoTotal) > 0) {
            throw new SaldoInsuficienteException(
                "Saldo e limite insuficientes para saque",
                getSaldo(),
                valor
            );
        }
        super.sacar(valor); // Chama o método sacar da superclasse
    }
    // ...
}
```

Neste exemplo, a subclasse `ContaCorrente` sobrescreve o método `sacar`, mas ainda aproveita a implementação da superclasse usando `super.sacar(valor)`.

### Herança vs. Composição

Embora a herança seja poderosa, nem sempre é a melhor escolha. Compare:

**Herança:**
```java
public class ContaCorrente extends Conta {
    private BigDecimal limiteChequeEspecial;
    // ...
}
```

**Composição:**
```java
public class ContaCorrente {
    private Conta contaBase;
    private BigDecimal limiteChequeEspecial;
    
    public ContaCorrente(String numero, Cliente titular) {
        this.contaBase = new Conta(numero, titular);
        this.limiteChequeEspecial = BigDecimal.ZERO;
    }
    
    public void depositar(BigDecimal valor) {
        contaBase.depositar(valor);
    }
    // ...
}
```

Considerações:
- Use herança quando existe uma relação "é um" genuína
- Use composição quando a relação é mais "tem um" ou "usa um"
- A composição geralmente leva a um acoplamento mais fraco
- "Prefira composição sobre herança" é um princípio de design importante

## Classes Abstratas

### O que são Classes Abstratas?

Uma classe abstrata é uma classe que não pode ser instanciada diretamente e geralmente contém métodos abstratos (sem implementação) que devem ser implementados pelas subclasses.

### Sintaxe de Classes e Métodos Abstratos

```java
public abstract class Conta {
    // Métodos concretos (com implementação)
    public void depositar(BigDecimal valor) {
        // implementação
    }
    
    // Métodos abstratos (sem implementação)
    public abstract void calcularTarifaMensal();
}
```

### Exemplo no Projeto: Classe Abstrata Conta

No projeto BancoFicticio, `Conta` é uma classe abstrata:

```java
public abstract class Conta {
    private String numero;
    private BigDecimal saldo;
    private Cliente titular;
    // ...
    
    public void depositar(BigDecimal valor) {
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor do depósito deve ser maior que zero");
        }
        this.saldo = this.saldo.add(valor);
    }
    
    public void sacar(BigDecimal valor) {
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor do saque deve ser maior que zero");
        }
        if (valor.compareTo(saldo) > 0) {
            throw new SaldoInsuficienteException("Saldo insuficiente para saque");
        }
        this.saldo = this.saldo.subtract(valor);
    }
    
    // Método abstrato que deve ser implementado pelas subclasses
    public abstract void calcularTarifaMensal();
    
    // Outros métodos...
}
```

Características importantes:
1. A classe é marcada como `abstract`
2. Contém métodos concretos (com implementação): `depositar()`, `sacar()`
3. Contém um método abstrato (sem implementação): `calcularTarifaMensal()`
4. Não pode ser instanciada diretamente (`new Conta()` causaria erro de compilação)
5. Serve como base para outras classes mais específicas

### Implementação de Métodos Abstratos

Cada subclasse concreta deve implementar os métodos abstratos da superclasse:

```java
public class ContaCorrente extends Conta {
    private static final BigDecimal TARIFA_MENSAL = new BigDecimal("30.00");
    
    @Override
    public void calcularTarifaMensal() {
        setSaldo(getSaldo().subtract(TARIFA_MENSAL));
    }
    // ...
}

public class ContaPoupanca extends Conta {
    private static final BigDecimal TARIFA_MENSAL = BigDecimal.ZERO;
    
    @Override
    public void calcularTarifaMensal() {
        setSaldo(getSaldo().subtract(TARIFA_MENSAL));
    }
    // ...
}
```

Observe que:
- Cada subclasse fornece sua própria implementação do método `calcularTarifaMensal()`
- A anotação `@Override` indica que estamos sobrescrevendo um método da superclasse
- Se uma subclasse não implementar todos os métodos abstratos, ela também deve ser declarada como abstrata

## Polimorfismo

### O que é Polimorfismo?

Polimorfismo é a capacidade de um objeto assumir múltiplas formas. Em Java, isso significa que uma referência a uma superclasse pode apontar para um objeto de qualquer subclasse, e o método chamado será o da subclasse, não o da superclasse.

### Tipos de Polimorfismo

#### 1. Polimorfismo de Sobrescrita (Override)

Ocorre quando uma subclasse fornece uma implementação específica de um método já definido na superclasse.

```java
// Na superclasse
public abstract class Conta {
    public void sacar(BigDecimal valor) {
        // implementação padrão
    }
}

// Na subclasse
public class ContaCorrente extends Conta {
    @Override
    public void sacar(BigDecimal valor) {
        // implementação específica para ContaCorrente
    }
}
```

#### 2. Polimorfismo de Sobrecarga (Overload)

Ocorre quando múltiplos métodos têm o mesmo nome, mas diferentes parâmetros.

```java
public class ContaCorrente extends Conta {
    // Método original
    public void sacar(BigDecimal valor) {
        // implementação
    }
    
    // Método sobrecarregado
    public void sacar(BigDecimal valor, boolean usarLimite) {
        // implementação alternativa
    }
}
```

### Exemplo Prático de Polimorfismo

```java
// Lista que pode conter qualquer tipo de conta
List<Conta> contas = new ArrayList<>();
contas.add(new ContaCorrente("1111", cliente, new BigDecimal("1000.00")));
contas.add(new ContaPoupanca("2222", cliente));
contas.add(new ContaInvestimento("3333", cliente, TipoInvestimento.RENDA_FIXA));

// Polimorfismo em ação - cada conta usa sua própria implementação
for (Conta conta : contas) {
    conta.calcularTarifaMensal();
    System.out.println("Tarifa aplicada para " + conta.getClass().getSimpleName());
}
```

Neste exemplo:
1. Temos uma lista de `Conta`, mas ela contém objetos de diferentes subclasses
2. Quando chamamos `calcularTarifaMensal()`, o método executado depende do tipo real do objeto
3. `ContaCorrente` cobrará R$ 30,00
4. `ContaPoupanca` não cobrará tarifa (R$ 0,00)
5. `ContaInvestimento` cobrará R$ 15,00 mais um percentual do saldo

### Benefícios do Polimorfismo

1. **Extensibilidade:** Podemos adicionar novos tipos de conta sem modificar o código existente
2. **Flexibilidade:** O código que usa as contas não precisa saber o tipo específico
3. **Manutenibilidade:** Mudanças na implementação de uma subclasse não afetam outras partes do sistema
4. **Reutilização:** Comportamento comum pode ser definido na superclasse

### Casting de Tipos

Às vezes, precisamos acessar métodos específicos de uma subclasse:

```java
// Temos uma referência genérica
Conta conta = new ContaCorrente("1111", cliente, new BigDecimal("1000.00"));

// Para chamar métodos específicos de ContaCorrente, precisamos fazer casting
if (conta instanceof ContaCorrente) {
    ContaCorrente cc = (ContaCorrente) conta;
    BigDecimal limite = cc.getLimiteChequeEspecial();
    System.out.println("Limite do cheque especial: " + limite);
}
```

A partir do Java 16, podemos usar pattern matching para simplificar:

```java
// Pattern matching para instanceof (Java 16+)
if (conta instanceof ContaCorrente cc) {
    BigDecimal limite = cc.getLimiteChequeEspecial();
    System.out.println("Limite do cheque especial: " + limite);
}
```

## Interfaces

### O que são Interfaces?

Uma interface é um contrato que define um conjunto de métodos que uma classe deve implementar. Diferente de classes abstratas, interfaces não podem conter implementações de métodos (exceto métodos default e static a partir do Java 8).

### Sintaxe de Interfaces

```java
public interface Tributavel {
    BigDecimal calcularImposto();
    
    // Método default (Java 8+)
    default boolean isento() {
        return calcularImposto().compareTo(BigDecimal.ZERO) == 0;
    }
}
```

### Exemplo no Projeto: Interface Tributavel

```java
public interface Tributavel {
    BigDecimal calcularImposto();
    
    default boolean isento() {
        return calcularImposto().compareTo(BigDecimal.ZERO) == 0;
    }
}

public class ContaInvestimento extends Conta implements Tributavel {
    private TipoInvestimento tipo;
    
    @Override
    public BigDecimal calcularImposto() {
        return getSaldo().multiply(tipo.getAliquotaImposto())
                .setScale(2, RoundingMode.HALF_EVEN);
    }
    // ...
}
```

Neste exemplo:
1. A interface `Tributavel` define o método `calcularImposto()` e um método default `isento()`
2. A classe `ContaInvestimento` implementa a interface, fornecendo uma implementação para `calcularImposto()`
3. A classe herda automaticamente a implementação default de `isento()`

### Múltiplas Interfaces

Uma classe pode implementar múltiplas interfaces:

```java
public class ContaInvestimento extends Conta implements Tributavel, Rentavel {
    @Override
    public BigDecimal calcularImposto() {
        // implementação de Tributavel
    }
    
    @Override
    public BigDecimal calcularRendimento() {
        // implementação de Rentavel
    }
    // ...
}
```

### Interfaces vs. Classes Abstratas

| Característica | Interface | Classe Abstrata |
|----------------|-----------|-----------------|
| Instanciação | Não pode ser instanciada | Não pode ser instanciada |
| Métodos | Apenas assinaturas (+ default/static no Java 8+) | Métodos concretos e abstratos |
| Atributos | Apenas constantes (public static final) | Qualquer tipo de atributo |
| Herança múltipla | Uma classe pode implementar múltiplas interfaces | Uma classe só pode estender uma classe |
| Objetivo | Define um contrato | Define uma base comum |

### Quando Usar Cada Uma?

- **Use interfaces quando:**
  - Várias classes não relacionadas precisam implementar o mesmo comportamento
  - Você quer especificar o comportamento de um tipo sem se preocupar com a implementação
  - Você precisa de herança múltipla de tipo

- **Use classes abstratas quando:**
  - Você quer compartilhar código entre várias classes relacionadas
  - Você precisa de atributos não constantes
  - Você quer fornecer uma implementação parcial

## Exercícios Práticos

### Exercício 1: Criar uma Nova Subclasse

Crie uma nova subclasse de `Conta` chamada `ContaSalario`:

```java
public class ContaSalario extends Conta {
    private String cnpjEmpregador;
    private int limiteSaquesGratuitos;
    private int saquesRealizados;
    private static final BigDecimal TARIFA_SAQUE_EXCEDENTE = new BigDecimal("2.50");
    private static final BigDecimal TARIFA_MENSAL = BigDecimal.ZERO;
    
    public ContaSalario(String numero, Cliente titular, String cnpjEmpregador) {
        super(numero, titular);
        this.cnpjEmpregador = cnpjEmpregador;
        this.limiteSaquesGratuitos = 4;
        this.saquesRealizados = 0;
    }
    
    @Override
    public void sacar(BigDecimal valor) {
        super.sacar(valor);
        saquesRealizados++;
        
        if (saquesRealizados > limiteSaquesGratuitos) {
            setSaldo(getSaldo().subtract(TARIFA_SAQUE_EXCEDENTE));
        }
    }
    
    @Override
    public void calcularTarifaMensal() {
        setSaldo(getSaldo().subtract(TARIFA_MENSAL));
        saquesRealizados = 0; // Reinicia contagem de saques no mês
    }
    
    // Getters e setters
}
```

### Exercício 2: Criar uma Nova Interface

Crie uma interface `Rentavel` e implemente-a nas classes apropriadas:

```java
public interface Rentavel {
    BigDecimal calcularRendimento();
    void aplicarRendimento();
}

public class ContaPoupanca extends Conta implements Rentavel {
    private BigDecimal taxaRendimento;
    
    // Implementação existente...
    
    @Override
    public BigDecimal calcularRendimento() {
        return getSaldo().multiply(taxaRendimento)
                .setScale(2, RoundingMode.HALF_EVEN);
    }
    
    @Override
    public void aplicarRendimento() {
        BigDecimal rendimento = calcularRendimento();
        setSaldo(getSaldo().add(rendimento));
    }
}

public class ContaInvestimento extends Conta implements Tributavel, Rentavel {
    private TipoInvestimento tipo;
    
    // Implementação existente...
    
    @Override
    public BigDecimal calcularRendimento() {
        return getSaldo().multiply(tipo.getTaxaRendimento())
                .setScale(2, RoundingMode.HALF_EVEN);
    }
    
    @Override
    public void aplicarRendimento() {
        BigDecimal rendimento = calcularRendimento();
        BigDecimal imposto = rendimento.multiply(tipo.getAliquotaImposto());
        setSaldo(getSaldo().add(rendimento).subtract(imposto));
    }
}
```

### Exercício 3: Uso de Polimorfismo

Crie um método que aplica rendimento a todas as contas rentáveis:

```java
public class ProcessadorRendimentos {
    public void processarRendimentos(List<Conta> contas) {
        for (Conta conta : contas) {
            if (conta instanceof Rentavel) {
                Rentavel contaRentavel = (Rentavel) conta;
                contaRentavel.aplicarRendimento();
                System.out.println("Rendimento aplicado na conta " + conta.getNumero());
            }
        }
    }
}

// Uso
List<Conta> contas = cliente.getContas();
ProcessadorRendimentos processador = new ProcessadorRendimentos();
processador.processarRendimentos(contas);
```

## Conclusão

Herança e polimorfismo são conceitos fundamentais em programação orientada a objetos que permitem criar código mais flexível, extensível e reutilizável. No projeto BancoFicticio, vemos como esses conceitos são aplicados para criar uma hierarquia de contas bancárias com comportamentos específicos.

Pontos-chave:

1. **Herança** permite que uma classe herde atributos e métodos de outra, estabelecendo uma relação "é um"
2. **Classes abstratas** fornecem uma base comum com implementação parcial
3. **Métodos abstratos** definem comportamentos que as subclasses devem implementar
4. **Interfaces** definem contratos que as classes devem cumprir
5. **Polimorfismo** permite tratar objetos de diferentes subclasses de maneira uniforme

Ao dominar esses conceitos, você será capaz de criar sistemas mais robustos, flexíveis e fáceis de manter. 