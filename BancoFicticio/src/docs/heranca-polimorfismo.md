# Herança e Polimorfismo em Java

## Herança

Herança é um mecanismo que permite que uma classe herde atributos e métodos de outra classe. A classe que herda é chamada de subclasse (ou classe filha) e a classe da qual se herda é chamada de superclasse (ou classe pai).

```java
// Superclasse
public abstract class Conta {
    protected String numero;
    protected BigDecimal saldo;

    public void depositar(BigDecimal valor) {
        this.saldo = this.saldo.add(valor);
    }

    // Método abstrato que deve ser implementado pelas subclasses
    public abstract void calcularTarifaMensal();
}

// Subclasse
public class ContaCorrente extends Conta {
    private BigDecimal limiteChequeEspecial;

    @Override
    public void calcularTarifaMensal() {
        this.saldo = this.saldo.subtract(new BigDecimal("30.00"));
    }
}
```

## Classes Abstratas

Uma classe abstrata é uma classe que não pode ser instanciada diretamente. Ela serve como um modelo para outras classes e pode conter:

- Métodos abstratos (sem implementação)
- Métodos concretos (com implementação)
- Atributos
- Construtores

```java
public abstract class Funcionario {
    protected String nome;
    protected BigDecimal salarioBase;

    // Método abstrato
    public abstract BigDecimal calcularSalario();

    // Método concreto
    public void receberAumento(BigDecimal aumento) {
        this.salarioBase = this.salarioBase.add(aumento);
    }
}

public class Gerente extends Funcionario {
    private BigDecimal bonus;

    @Override
    public BigDecimal calcularSalario() {
        return this.salarioBase.add(bonus);
    }
}
```

## Interfaces

Uma interface é um contrato que define um conjunto de métodos que uma classe deve implementar. A partir do Java 8, interfaces podem ter métodos default e static.

```java
public interface Tributavel {
    BigDecimal calcularImposto();

    // Método default (Java 8+)
    default boolean isento() {
        return calcularImposto().compareTo(BigDecimal.ZERO) == 0;
    }
}

public class ContaInvestimento extends Conta implements Tributavel {
    private BigDecimal rendimento;

    @Override
    public BigDecimal calcularImposto() {
        return rendimento.multiply(new BigDecimal("0.15"));
    }
}
```

## Polimorfismo

Polimorfismo permite que objetos de diferentes classes sejam tratados como objetos de uma classe comum. Existem dois tipos principais:

### 1. Polimorfismo de Sobrescrita (Override)

```java
public abstract class Conta {
    public abstract void calcularTarifaMensal();
}

public class ContaPoupanca extends Conta {
    @Override
    public void calcularTarifaMensal() {
        // Implementação específica para conta poupança
    }
}

public class ContaCorrente extends Conta {
    @Override
    public void calcularTarifaMensal() {
        // Implementação específica para conta corrente
    }
}
```

### 2. Polimorfismo de Sobrecarga (Overload)

```java
public class ContaCorrente extends Conta {
    // Sobrecarga de métodos
    public void sacar(BigDecimal valor) {
        // Saque normal
    }

    public void sacar(BigDecimal valor, boolean usarLimite) {
        // Saque com opção de usar limite
    }
}
```

## Dicas Importantes

1. Use herança quando existe uma relação "é um" entre as classes
2. Prefira composição à herança quando possível
3. Interfaces são ideais para definir contratos
4. Use classes abstratas quando precisar compartilhar código entre classes relacionadas
5. Evite herança profunda (mais de 3 níveis)
6. Sempre documente o comportamento esperado dos métodos abstratos

## Exemplo de Uso Prático

```java
// Lista pode conter qualquer tipo de conta
List<Conta> contas = new ArrayList<>();
contas.add(new ContaCorrente());
contas.add(new ContaPoupanca());

// Polimorfismo em ação
for (Conta conta : contas) {
    conta.calcularTarifaMensal(); // Cada tipo de conta usa sua própria implementação
}
```
