# Classes e Objetos em Java

Este documento explora em detalhes os conceitos de classes e objetos em Java, com exemplos práticos do projeto BancoFicticio.

## O que são Classes?

Uma classe em Java é um modelo ou template que define a estrutura e o comportamento que os objetos desse tipo terão. Ela funciona como uma "planta baixa" que especifica:

- **Atributos:** Os dados que a classe armazena
- **Métodos:** As operações que a classe pode realizar
- **Construtores:** Como os objetos são inicializados
- **Modificadores de acesso:** Quem pode acessar seus membros

## O que são Objetos?

Um objeto é uma instância concreta de uma classe. Se a classe é o modelo, o objeto é o produto final criado a partir desse modelo. Cada objeto tem:

- **Estado:** Os valores atuais de seus atributos
- **Comportamento:** As ações que ele pode realizar (métodos)
- **Identidade:** Uma localização única na memória

## Anatomia de uma Classe em Java

Vamos analisar a estrutura da classe `Cliente` do nosso projeto:

```java
package com.banco.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Cliente implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nome;
    private String cpf;
    private LocalDate dataNascimento;
    private List<Conta> contas;

    public Cliente(String nome, String cpf, LocalDate dataNascimento) {
        this.nome = nome;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.contas = new ArrayList<>();
    }

    // Métodos...
}
```

Vamos decompor essa classe:

1. **Declaração de pacote:** `package com.banco.domain;` - Define o namespace da classe
2. **Importações:** `import java.io.Serializable;` etc. - Referencia classes externas necessárias
3. **Declaração da classe:** `public class Cliente implements Serializable` - Define o nome da classe e interfaces implementadas
4. **Atributos:** `private String nome;` etc. - Define os dados que a classe armazena
5. **Construtor:** `public Cliente(String nome, String cpf, LocalDate dataNascimento)` - Define como criar objetos

## Criando e Usando Objetos

### Instanciação

Para criar um objeto, usamos a palavra-chave `new` seguida do construtor da classe:

```java
// Criando um objeto Cliente
Cliente cliente = new Cliente(
    "João da Silva",
    "123.456.789-00",
    LocalDate.of(1990, 1, 1)
);

// Criando um objeto ContaCorrente
ContaCorrente cc = new ContaCorrente("1111", cliente, new BigDecimal("1000.00"));
```

Vamos analisar o que acontece neste código:

1. `new Cliente(...)` - Aloca memória para o objeto
2. O construtor `Cliente(String, String, LocalDate)` é chamado
3. Os atributos do objeto são inicializados com os valores fornecidos
4. Uma referência ao objeto é armazenada na variável `cliente`

### Acessando Atributos e Métodos

Uma vez criado o objeto, podemos acessar seus métodos e atributos (se acessíveis) usando a notação de ponto:

```java
// Acessando métodos
cliente.adicionarConta(cc);
String nomeDoCLiente = cliente.getNome();

// Chamando métodos no objeto ContaCorrente
cc.depositar(new BigDecimal("500.00"));
cc.sacar(new BigDecimal("200.00"));
```

## Construtores em Detalhes

Os construtores são métodos especiais que são chamados quando um objeto é criado. Eles têm o mesmo nome da classe e não têm tipo de retorno.

### Construtor Padrão

Se nenhum construtor for definido, Java fornece um construtor padrão sem parâmetros:

```java
public Cliente() {
    // Construtor padrão gerado pelo Java
}
```

### Construtores Personalizados

No nosso projeto, definimos construtores personalizados:

```java
public Cliente(String nome, String cpf, LocalDate dataNascimento) {
    this.nome = nome;
    this.cpf = cpf;
    this.dataNascimento = dataNascimento;
    this.contas = new ArrayList<>();
}
```

### Sobrecarga de Construtores

Podemos ter múltiplos construtores com diferentes parâmetros (sobrecarga):

```java
// Construtor principal
public ContaCorrente(String numero, Cliente titular, BigDecimal limiteChequeEspecial) {
    super(numero, titular);
    this.limiteChequeEspecial = limiteChequeEspecial.setScale(2, RoundingMode.HALF_EVEN);
}

// Construtor sobrecarregado que chama o principal
public ContaCorrente(String numero, Cliente titular) {
    this(numero, titular, BigDecimal.ZERO);
}
```

## Exemplo Prático: Ciclo de Vida de um Objeto Conta

Vamos acompanhar o ciclo de vida completo de um objeto `ContaCorrente`:

### 1. Criação

```java
// Criando um cliente
Cliente joao = new Cliente("João Silva", "123.456.789-00", LocalDate.of(1990, 5, 15));

// Criando uma conta corrente
ContaCorrente conta = new ContaCorrente("1111", joao, new BigDecimal("1000.00"));

// Associando a conta ao cliente
joao.adicionarConta(conta);
```

### 2. Uso

```java
// Depositando dinheiro
conta.depositar(new BigDecimal("2500.00"));
System.out.println("Saldo após depósito: " + conta.getSaldo()); // 2500.00

// Sacando dinheiro
conta.sacar(new BigDecimal("500.00"));
System.out.println("Saldo após saque: " + conta.getSaldo()); // 2000.00

// Cobrando tarifa mensal
conta.calcularTarifaMensal();
System.out.println("Saldo após tarifa: " + conta.getSaldo()); // 1970.00 (2000 - 30)
```

### 3. Modificação de Estado

```java
// Alterando o limite do cheque especial
conta.setLimiteChequeEspecial(new BigDecimal("2000.00"));

// Tentando saque que usa o limite
BigDecimal saldoAtual = conta.getSaldo(); // 1970.00
conta.sacar(new BigDecimal("3000.00")); // Vai funcionar porque temos 1970 + 2000 de limite

System.out.println("Saldo após saque grande: " + conta.getSaldo()); // -1030.00
```

### 4. Tratamento de Erros

```java
try {
    // Tentando sacar mais do que o saldo + limite permite
    conta.sacar(new BigDecimal("5000.00"));
} catch (SaldoInsuficienteException e) {
    System.err.println("Erro: " + e.getMessage());
    System.err.println("Saldo disponível: " + conta.getSaldo().add(conta.getLimiteChequeEspecial()));
}
```

## Variáveis de Referência vs. Objetos

Em Java, é importante entender a diferença entre variáveis de referência e os objetos em si:

```java
Cliente cliente1 = new Cliente("João", "111.111.111-11", LocalDate.now());
Cliente cliente2 = cliente1; // cliente2 aponta para o mesmo objeto que cliente1

cliente2.setNome("João da Silva");
System.out.println(cliente1.getNome()); // Imprime "João da Silva"
```

Neste exemplo:
- `cliente1` e `cliente2` são variáveis de referência
- Ambas apontam para o mesmo objeto na memória
- Alterar o objeto através de `cliente2` também afeta o que vemos através de `cliente1`

## Classes vs. Objetos: Membros Estáticos

Membros estáticos (com a palavra-chave `static`) pertencem à classe, não aos objetos individuais:

```java
public class ContaCorrente extends Conta {
    private static final BigDecimal TARIFA_MENSAL = new BigDecimal("30.00");
    
    // ...
    
    @Override
    public void calcularTarifaMensal() {
        setSaldo(getSaldo().subtract(TARIFA_MENSAL));
    }
}
```

Neste exemplo:
- `TARIFA_MENSAL` é um atributo estático (pertence à classe)
- Todas as instâncias de `ContaCorrente` compartilham o mesmo valor
- É uma constante (`final`), então não pode ser alterada

## Boas Práticas para Classes e Objetos

### 1. Responsabilidade Única

Cada classe deve ter uma única responsabilidade. Por exemplo, a classe `Conta` lida apenas com operações relacionadas a contas bancárias.

### 2. Encapsulamento Adequado

Atributos devem ser privados, com acesso controlado por métodos:

```java
public class Cliente {
    private String nome; // Atributo privado
    
    public String getNome() { // Acesso público controlado
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = Objects.requireNonNull(nome, "Nome não pode ser nulo");
    }
}
```

### 3. Validação nos Construtores e Setters

Sempre valide os dados recebidos:

```java
public Cliente(String nome, String cpf, LocalDate dataNascimento) {
    this.nome = Objects.requireNonNull(nome, "Nome não pode ser nulo");
    this.cpf = Objects.requireNonNull(cpf, "CPF não pode ser nulo");
    this.dataNascimento = dataNascimento;
    this.contas = new ArrayList<>();
}
```

### 4. Imutabilidade Quando Possível

Torne atributos imutáveis quando fizer sentido:

```java
public String getCpf() {
    return cpf;
}

// Não existe setCpf() porque o CPF não deve mudar após a criação
```

### 5. Métodos toString(), equals() e hashCode()

Implemente estes métodos para facilitar depuração e uso em coleções:

```java
@Override
public boolean equals(Object o) {
    if (this == o) {
        return true;
    }
    if (o == null || getClass() != o.getClass()) {
        return false;
    }
    Cliente cliente = (Cliente) o;
    return Objects.equals(cpf, cliente.cpf);
}

@Override
public int hashCode() {
    return Objects.hash(cpf);
}

@Override
public String toString() {
    return "Cliente{" +
            "nome='" + nome + '\'' +
            ", cpf='" + cpf + '\'' +
            ", dataNascimento=" + dataNascimento +
            '}';
}
```

## Exercícios Práticos

### Exercício 1: Criar uma Nova Classe

Crie uma classe `Endereco` para armazenar o endereço dos clientes:

```java
public class Endereco {
    private String logradouro;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
    
    // Construtor, getters, setters, equals, hashCode, toString
}
```

Depois, modifique a classe `Cliente` para incluir um endereço:

```java
public class Cliente implements Serializable {
    // Atributos existentes
    private Endereco endereco;
    
    // Métodos para acessar e modificar o endereço
    public Endereco getEndereco() {
        return endereco;
    }
    
    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }
}
```

### Exercício 2: Criar e Manipular Objetos

```java
// Criar endereço
Endereco endereco = new Endereco(
    "Rua das Flores", "123", "Apto 101",
    "Centro", "São Paulo", "SP", "01234-567"
);

// Criar cliente com endereço
Cliente cliente = new Cliente(
    "Maria Silva", "987.654.321-00", LocalDate.of(1985, 3, 10)
);
cliente.setEndereco(endereco);

// Criar conta para o cliente
ContaPoupanca cp = new ContaPoupanca("3333", cliente);
cliente.adicionarConta(cp);

// Realizar operações
cp.depositar(new BigDecimal("1500.00"));
cp.calcularRendimento(); // Aplica juros
cp.sacar(new BigDecimal("500.00"));

// Exibir informações
System.out.println("Cliente: " + cliente.getNome());
System.out.println("Endereço: " + cliente.getEndereco().getLogradouro() + ", " + 
                  cliente.getEndereco().getNumero());
System.out.println("Saldo: " + cp.getSaldo());
```

## Conclusão

Classes e objetos são os blocos fundamentais da programação orientada a objetos em Java. Uma classe bem projetada deve:

1. Ter uma única responsabilidade clara
2. Encapsular seus dados adequadamente
3. Validar suas entradas
4. Fornecer uma interface clara e intuitiva
5. Implementar corretamente os métodos fundamentais (equals, hashCode, toString)

No projeto BancoFicticio, vemos esses princípios aplicados nas classes como `Cliente`, `Conta` e suas subclasses, resultando em um código organizado, manutenível e robusto. 