# Análise Detalhada: Conta.java

## Visão Geral

A classe `Conta` é uma classe abstrata que serve como base para todos os tipos de conta no sistema bancário. Uma classe abstrata é como um modelo ou template que define a estrutura básica, mas não pode ser usada diretamente - precisamos criar classes específicas que a estendem (como ContaCorrente, ContaPoupanca, etc.).

## Pacote e Importações

```java
package com.banco.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import com.banco.exception.SaldoInsuficienteException;
```

Aqui temos importações cruciais para trabalhar com dados financeiros e temporais:

- `BigDecimal`: Uma classe especial para trabalhar com valores monetários. Diferente de `double` ou `float`, ela não tem problemas de arredondamento, o que é essencial para dinheiro
- `LocalDateTime`: Classe moderna do Java para trabalhar com data e hora juntos
- `Objects`: Fornece métodos utilitários para trabalhar com objetos de forma segura
- `SaldoInsuficienteException`: Nossa própria exceção para tratar casos de saldo insuficiente

## Declaração e Atributos

```java
public abstract class Conta {
    private String numero;
    private BigDecimal saldo;
    private Cliente titular;
    private LocalDateTime dataCriacao;
    private boolean ativa;
```

- `abstract`: Indica que esta é uma classe abstrata. Não podemos criar objetos diretamente dela, apenas de suas subclasses
- Todos os atributos são `private`: Isso é encapsulamento, significa que só podemos acessar ou modificar estes valores através dos métodos da classe
- `BigDecimal saldo`: Usamos BigDecimal para dinheiro porque ele evita erros de arredondamento que são comuns em float/double
- `LocalDateTime dataCriacao`: Armazena exatamente quando a conta foi criada
- `boolean ativa`: Um flag simples que indica se a conta está ativa ou não

## Construtor

```java
    public Conta(String numero, Cliente titular) {
        this.numero = Objects.requireNonNull(numero, "Número não pode ser nulo");
        this.titular = Objects.requireNonNull(titular, "Titular não pode ser nulo");
        this.saldo = BigDecimal.ZERO;
        this.dataCriacao = LocalDateTime.now();
        this.ativa = true;
    }
```

O construtor é especialmente interessante:

- `Objects.requireNonNull()`: Este é um método de segurança que verifica se um valor é nulo. Se for, lança uma exceção com a mensagem especificada. É melhor que fazer `if (numero == null)` porque é mais claro e padronizado
- `BigDecimal.ZERO`: É uma constante que representa zero. É melhor que criar um novo BigDecimal("0") porque é mais eficiente e não pode dar erro
- `LocalDateTime.now()`: Captura o momento exato da criação da conta
- `this.ativa = true`: Toda conta começa ativa por padrão

## Métodos de Operação

```java
    public void depositar(BigDecimal valor) {
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor do depósito deve ser maior que zero");
        }
        this.saldo = this.saldo.add(valor);
    }
```

O método depositar mostra várias coisas importantes:

- `valor.compareTo(BigDecimal.ZERO)`: Com BigDecimal, não usamos operadores como < > =. Em vez disso, usamos compareTo:
  - Retorna -1 se valor é menor
  - Retorna 0 se são iguais
  - Retorna 1 se valor é maior
- `throw new IllegalArgumentException`: Lançamos uma exceção quando algo está errado. Isso é melhor que retornar false porque:
  1. Força quem chama o método a tratar o erro
  2. Carrega uma mensagem explicativa
  3. Pode carregar mais informações sobre o erro
- `this.saldo.add(valor)`: BigDecimal é imutável, então operações como add retornam um novo objeto. Por isso precisamos reatribuir ao saldo

```java
    public void sacar(BigDecimal valor) {
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor do saque deve ser maior que zero");
        }
        if (valor.compareTo(saldo) > 0) {
            throw new SaldoInsuficienteException("Saldo insuficiente para saque");
        }
        this.saldo = this.saldo.subtract(valor);
    }
```

O método sacar é similar, mas com validações adicionais:

- Primeira validação: valor deve ser positivo
- Segunda validação: deve ter saldo suficiente
- `subtract`: É o oposto de add, subtrai o valor do saldo
- Note que usamos nossa própria exceção `SaldoInsuficienteException` para este caso específico

## Método Abstrato

```java
    public abstract void calcularTarifaMensal();
```

Este é um método abstrato:

- Não tem implementação (não tem corpo)
- Força todas as subclasses a implementarem sua própria versão
- É como dizer "toda conta precisa saber calcular sua tarifa, mas cada tipo de conta calcula de um jeito"

## Getters e Setters

```java
    public String getNumero() {
        return numero;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    protected void setSaldo(BigDecimal saldo) {
        this.saldo = Objects.requireNonNull(saldo, "Saldo não pode ser nulo");
    }
```

Observe os diferentes níveis de acesso:

- `public getNumero()`: Qualquer um pode ler o número da conta
- `protected setSaldo()`: Apenas a própria classe e suas subclasses podem modificar o saldo
- Não existe setNumero(): O número da conta não pode ser alterado após a criação

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
        Conta conta = (Conta) o;
        return Objects.equals(numero, conta.numero);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numero);
    }
```

Estes métodos são fundamentais para o funcionamento correto de coleções (List, Set, Map):

- `equals`: Define quando duas contas são consideradas iguais
  - `this == o`: Verifica se é literalmente o mesmo objeto na memória
  - `getClass() != o.getClass()`: Verifica se são do mesmo tipo exato de conta
  - `Objects.equals(numero, conta.numero)`: Compara os números das contas
- `hashCode`: Gera um código numérico que representa a conta
  - Deve ser consistente com equals
  - Usa o mesmo atributo (numero) que equals usa
  - `Objects.hash()` é um método utilitário que gera um bom hashCode

## ToString

```java
    @Override
    public String toString() {
        return "Conta{"
                + "numero='" + numero + '\''
                + ", saldo=" + saldo
                + ", titular=" + titular
                + ", ativa=" + ativa
                + '}';
    }
```

Este método é muito útil para depuração:

- Converte a conta em uma string legível
- Mostra todos os atributos importantes
- O formato `chave='valor'` torna fácil ler os dados
- É chamado automaticamente em vários contextos (println, debugger, etc.)

## Por Que Essas Escolhas?

1. **Uso de BigDecimal**

   - Evita erros de arredondamento
   - Essencial para valores monetários
   - Operações matemáticas precisas
   - Controle de escala e arredondamento

2. **Validações Rigorosas**

   - Valores nulos são barrados
   - Valores negativos são rejeitados
   - Saldo insuficiente é tratado apropriadamente
   - Exceções específicas para cada caso

3. **Encapsulamento Forte**

   - Atributos privados
   - Alguns setters protegidos
   - Alguns atributos imutáveis
   - Validações centralizadas

4. **Design para Extensão**
   - Classe abstrata permite especialização
   - Método abstrato força implementação
   - Protected permite acesso controlado
   - Cada tipo de conta pode ter seu comportamento
