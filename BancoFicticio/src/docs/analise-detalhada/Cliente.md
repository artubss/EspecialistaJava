# Análise Detalhada: Cliente.java

## Visão Geral

A classe `Cliente` representa um cliente do banco. Ela é um exemplo excelente de como estruturar uma classe em Java seguindo as melhores práticas de programação orientada a objetos.

## Pacote e Importações

```java
package com.banco.domain;
```

O `package` é como uma pasta que agrupa classes relacionadas. Neste caso, `com.banco.domain` indica que esta classe faz parte do domínio do negócio (regras e entidades do banco).

```java
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
```

As importações são como uma lista de ferramentas que vamos usar na nossa classe:

- `Serializable`: Permite que objetos dessa classe sejam convertidos em bytes (útil para salvar em arquivos ou enviar pela rede)
- `LocalDate`: Uma classe moderna do Java para trabalhar com datas (sem horário)
- `ArrayList/List`: Para criar e trabalhar com listas de objetos
- `Collections`: Fornece utilidades para trabalhar com coleções
- `Objects`: Oferece métodos úteis para trabalhar com objetos de forma segura

## Declaração da Classe

```java
public class Cliente implements Serializable {
    private static final long serialVersionUID = 1L;
```

- `public`: Significa que a classe pode ser usada por qualquer outra classe
- `implements Serializable`: Indica que objetos dessa classe podem ser convertidos em bytes
- `serialVersionUID`: É como um número de versão da classe, usado quando convertemos objetos em bytes e depois queremos recuperá-los

## Atributos

```java
    private String nome;
    private String cpf;
    private LocalDate dataNascimento;
    private List<Conta> contas;
```

Cada atributo é `private` (privado), o que significa que só podem ser acessados através de métodos da própria classe. Isso é chamado de encapsulamento e protege os dados do cliente.

- `String nome`: Armazena o nome como texto
- `String cpf`: Armazena o CPF como texto
- `LocalDate dataNascimento`: Armazena a data de nascimento usando a classe moderna do Java para datas
- `List<Conta> contas`: Uma lista que pode conter várias contas. O `<Conta>` é chamado de Generic e indica que esta lista só pode conter objetos do tipo `Conta`

## Construtor

```java
    public Cliente(String nome, String cpf, LocalDate dataNascimento) {
        this.nome = nome;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.contas = new ArrayList<>();
    }
```

O construtor é um método especial que é chamado quando criamos um novo cliente.

- `this` é uma referência ao objeto atual e é usado para diferenciar os atributos da classe dos parâmetros do método
- `new ArrayList<>()` cria uma nova lista vazia para armazenar as contas do cliente
- Quando escrevemos `this.nome = nome`, estamos dizendo "pegue o valor do parâmetro nome e guarde no atributo nome deste objeto"

## Getters e Setters

```java
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = Objects.requireNonNull(nome, "Nome não pode ser nulo");
    }
```

Getters e setters são métodos que permitem acessar e modificar os atributos de forma controlada:

- `getNome()`: Método que retorna o valor do nome
- `setNome()`: Método que permite alterar o nome
- `Objects.requireNonNull()`: Este é um método de segurança que verifica se o valor não é nulo. Se for nulo, lança uma exceção com a mensagem especificada. É uma forma de garantir que nunca teremos um cliente sem nome

```java
    public String getCpf() {
        return cpf;
    }
```

Note que para CPF só temos o getter. Isso é intencional: uma vez que o CPF é definido no construtor, ele não pode ser alterado (imutabilidade).

## Manipulação de Contas

```java
    public List<Conta> getContas() {
        return Collections.unmodifiableList(contas);
    }
```

Este método é interessante porque retorna uma versão não-modificável da lista de contas.

- `Collections.unmodifiableList()` cria uma espécie de "escudo" em volta da lista
- Se alguém tentar modificar a lista retornada, receberá um erro
- Isso protege os dados do cliente, garantindo que as contas só possam ser adicionadas ou removidas através dos métodos apropriados

```java
    public void adicionarConta(Conta conta) {
        Objects.requireNonNull(conta, "Conta não pode ser nula");
        this.contas.add(conta);
    }

    public void removerConta(Conta conta) {
        this.contas.remove(conta);
    }
```

Estes são os métodos corretos para manipular a lista de contas:

- `adicionarConta`: Verifica se a conta não é nula antes de adicionar
- `removerConta`: Remove uma conta da lista
- O método `add` é da interface `List` e adiciona um elemento ao final da lista
- O método `remove` procura e remove o elemento especificado da lista

## Equals e HashCode

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
```

Estes métodos são fundamentais quando precisamos comparar clientes ou usá-los em coleções:

- `@Override`: Esta anotação indica que estamos sobrescrevendo um método da classe pai (Object). É uma boa prática usar essa anotação pois o compilador verifica se estamos realmente sobrescrevendo um método existente
- O método `equals` define quando dois clientes são considerados iguais:
  1. Primeiro verifica se é o mesmo objeto na memória (`this == o`)
  2. Depois verifica se o outro objeto é nulo ou de uma classe diferente
  3. Por fim, compara os CPFs dos clientes
- O método `hashCode` gera um número que representa o objeto:
  - Objetos iguais (pelo equals) devem ter o mesmo hashCode
  - Usamos o CPF para gerar esse número pois é o que define a igualdade
  - `Objects.hash()` é um método utilitário que gera um bom hashCode

## ToString

```java
    @Override
    public String toString() {
        return "Cliente{"
                + "nome='" + nome + '\''
                + ", cpf='" + cpf + '\''
                + ", dataNascimento=" + dataNascimento
                + '}';
    }
```

Este método cria uma representação em texto do cliente:

- Útil para depuração e logs
- Não incluímos a lista de contas para evitar recursão (já que a conta também tem uma referência ao cliente)
- O formato `nome='valor'` torna fácil ler os valores
- Os caracteres `\'` são usados para escapar as aspas simples no texto

## Por Que Essas Escolhas?

1. **Encapsulamento Forte**

   - Atributos privados protegem os dados
   - Métodos públicos controlam o acesso
   - Lista imutável previne modificações acidentais

2. **Imutabilidade Parcial**

   - CPF não pode ser alterado após criação
   - Protege a integridade dos dados
   - Facilita o uso em sistemas concorrentes

3. **Validações de Dados**

   - Verificações de nulo previnem erros
   - Mensagens claras ajudam a identificar problemas
   - Validações centralizadas garantem consistência

4. **Padrões de Projeto**
   - JavaBeans para estrutura consistente
   - Builder poderia ser adicionado para construção complexa
   - Imutabilidade parcial para segurança
