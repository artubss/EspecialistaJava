# Modelo de Dados do BancoPro

## Visão Geral

O modelo de dados do BancoPro foi projetado para representar as entidades fundamentais de um sistema bancário, permitindo o gerenciamento de clientes, contas e transações financeiras.

## Entidades Principais

### Cliente

Representa um cliente do banco, armazenando suas informações pessoais.

```java
@Entity
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String cpf;
    private String email;
    private String telefone;

    @OneToMany(mappedBy = "cliente")
    private List<Conta> contas;
}
```

#### Atributos

| Atributo | Tipo | Descrição |
|----------|------|------------|
| id | Long | Identificador único do cliente |
| nome | String | Nome completo do cliente |
| cpf | String | CPF do cliente (único) |
| email | String | E-mail do cliente |
| telefone | String | Telefone do cliente |

### Conta

Representa uma conta bancária associada a um cliente.

```java
@Entity
public class Conta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numero;
    private BigDecimal saldo;
    private LocalDateTime dataCriacao;

    @Enumerated(EnumType.STRING)
    private TipoConta tipoConta;

    @ManyToOne
    private Cliente cliente;

    @OneToMany(mappedBy = "conta")
    private List<Transacao> transacoes;

    public enum TipoConta {
        CORRENTE, POUPANCA, SALARIO, INVESTIMENTO
    }
}
```

#### Atributos

| Atributo | Tipo | Descrição |
|----------|------|------------|
| id | Long | Identificador único da conta |
| numero | String | Número da conta |
| saldo | BigDecimal | Saldo atual da conta |
| dataCriacao | LocalDateTime | Data e hora de criação da conta |
| tipoConta | TipoConta | Tipo da conta (Corrente, Poupança, etc.) |

### Transacao

Registra as operações financeiras realizadas nas contas.

```java
@Entity
public class Transacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal valor;
    private LocalDateTime dataHora;
    private String descricao;

    @Enumerated(EnumType.STRING)
    private TipoTransacao tipo;

    @ManyToOne
    private Conta conta;

    @ManyToOne
    private Conta contaDestino;

    public enum TipoTransacao {
        DEPOSITO, SAQUE, TRANSFERENCIA, PAGAMENTO
    }
}
```

#### Atributos

| Atributo | Tipo | Descrição |
|----------|------|------------|
| id | Long | Identificador único da transação |
| valor | BigDecimal | Valor da transação |
| dataHora | LocalDateTime | Data e hora da transação |
| descricao | String | Descrição da transação |
| tipo | TipoTransacao | Tipo de transação (Depósito, Saque, etc.) |

## Relacionamentos

### Cliente → Conta (1:N)

Um cliente pode ter múltiplas contas, mas cada conta pertence a apenas um cliente.

```java
// Na classe Cliente
@OneToMany(mappedBy = "cliente")
private List<Conta> contas;

// Na classe Conta
@ManyToOne
private Cliente cliente;
```

### Conta → Transacao (1:N)

Uma conta pode ter múltiplas transações, mas cada transação está associada a uma conta de origem.

```java
// Na classe Conta
@OneToMany(mappedBy = "conta")
private List<Transacao> transacoes;

// Na classe Transacao
@ManyToOne
private Conta conta;
```

### Transacao → Conta (Destino) (N:1)

No caso de transferências, uma transação também pode estar associada a uma conta de destino.

```java
// Na classe Transacao
@ManyToOne
private Conta contaDestino;
```

## Diagrama de Entidade-Relacionamento (ER)

```
+-------------+       +-------------+       +---------------+
|   Cliente   |       |    Conta    |       |   Transacao   |
+-------------+       +-------------+       +---------------+
| id          |<----->| id          |<----->| id            |
| nome        |  1:N  | numero      |  1:N  | valor         |
| cpf         |       | saldo       |       | dataHora      |
| email       |       | dataCriacao |       | descricao     |
| telefone    |       | tipoConta   |       | tipo          |
+-------------+       | cliente_id  |       | conta_id      |
                      +-------------+       | contaDestino_id|
                                           +---------------+
```

## Considerações sobre o Modelo de Dados

### Validações

- O CPF do cliente é único e obrigatório
- O saldo da conta não pode ser negativo (validado na lógica de negócio)
- Todas as transações são registradas com data/hora

### Tipos de Dados

- Valores monetários são representados como BigDecimal para evitar problemas de precisão
- Datas são representadas como LocalDateTime para facilitar o trabalho com timezone

### Enumerações

- TipoConta: Define os tipos de conta disponíveis (CORRENTE, POUPANCA, SALARIO, INVESTIMENTO)
- TipoTransacao: Define os tipos de transação possíveis (DEPOSITO, SAQUE, TRANSFERENCIA, PAGAMENTO)

### Banco de Dados

Este modelo é persistido em um banco de dados H2 em memória durante o desenvolvimento. Para um ambiente de produção, recomenda-se a migração para um banco de dados relacional persistente como PostgreSQL ou MySQL.
