# Conceitos Básicos de Java

## Classes e Objetos

Uma classe é um modelo ou template para criar objetos. Ela define os atributos (dados) e métodos (comportamentos) que os objetos terão.

### Exemplo Prático

```java
// Definição da classe
public class ContaBancaria {
    // Atributos
    private String numero;
    private BigDecimal saldo;

    // Métodos
    public void depositar(BigDecimal valor) {
        this.saldo = this.saldo.add(valor);
    }
}

// Criando um objeto (instância) da classe
ContaBancaria conta = new ContaBancaria();
```

## Métodos e Atributos

### Atributos

São as características ou estados de um objeto. Em Java, usamos o encapsulamento para proteger os atributos:

- `private`: apenas a própria classe acessa
- `protected`: a classe e suas subclasses acessam
- `public`: qualquer classe acessa

### Métodos

São os comportamentos ou ações que um objeto pode realizar. Podem:

- Receber parâmetros
- Retornar valores
- Modificar o estado do objeto
- Realizar operações

```java
public class ContaBancaria {
    private BigDecimal saldo;

    // Método com parâmetro e retorno
    public boolean sacar(BigDecimal valor) {
        if (valor.compareTo(saldo) <= 0) {
            this.saldo = this.saldo.subtract(valor);
            return true;
        }
        return false;
    }
}
```

## Construtores

São métodos especiais que inicializam objetos. Uma classe pode ter múltiplos construtores (sobrecarga).

```java
public class ContaBancaria {
    private String numero;
    private BigDecimal saldo;

    // Construtor padrão
    public ContaBancaria() {
        this.saldo = BigDecimal.ZERO;
    }

    // Construtor com parâmetros
    public ContaBancaria(String numero, BigDecimal saldoInicial) {
        this.numero = numero;
        this.saldo = saldoInicial;
    }
}
```

## Encapsulamento

É o princípio de esconder os detalhes internos da implementação e fornecer uma interface pública para interagir com o objeto.

```java
public class ContaBancaria {
    // Atributo privado
    private BigDecimal saldo;

    // Método público para acessar
    public BigDecimal getSaldo() {
        return saldo;
    }

    // Método público para modificar
    public void setSaldo(BigDecimal saldo) {
        if (saldo.compareTo(BigDecimal.ZERO) >= 0) {
            this.saldo = saldo;
        }
    }
}
```

## JavaBeans

É uma convenção de design para classes Java que inclui:

- Construtor sem argumentos
- Atributos privados
- Métodos getters e setters públicos
- Implementação de Serializable

```java
public class Cliente implements Serializable {
    private String nome;
    private String cpf;

    // Construtor padrão
    public Cliente() {
    }

    // Getters e Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
}
```

## Dicas de Boas Práticas

1. Sempre encapsule seus atributos (use private)
2. Forneça métodos públicos para acesso controlado
3. Valide dados nos setters
4. Use nomes significativos para classes, métodos e atributos
5. Mantenha responsabilidades bem definidas para cada classe
