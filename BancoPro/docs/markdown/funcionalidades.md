# Funcionalidades do BancoPro

## Visão Geral

O BancoPro oferece um conjunto completo de funcionalidades para gerenciamento bancário, desde o cadastro de clientes até operações financeiras complexas. Este documento detalha as principais funcionalidades do sistema.

## 1. Gerenciamento de Clientes

### 1.1 Cadastro de Clientes

Permite o registro de novos clientes no sistema, coletando informações essenciais:

- Nome completo
- CPF (com validação de unicidade)
- E-mail (com validação de formato)
- Telefone

**Endpoint**: `GET /clientes/novo` (formulário) e `POST /clientes/salvar` (processamento)

### 1.2 Listagem de Clientes

Exibe todos os clientes cadastrados no sistema, com opções para visualizar detalhes, editar ou excluir.

**Endpoint**: `GET /clientes`

### 1.3 Edição de Clientes

Permite a atualização das informações de um cliente existente.

**Endpoint**: `GET /clientes/editar/{id}` e `POST /clientes/salvar`

### 1.4 Exclusão de Clientes

Permite a remoção de um cliente do sistema, desde que não possua contas ativas.

**Endpoint**: `GET /clientes/excluir/{id}`

**Regra de Negócio**: Um cliente só pode ser excluído se não possuir contas ativas.

## 2. Gerenciamento de Contas

### 2.1 Criação de Contas

Permite a criação de novas contas bancárias para um cliente, com diferentes tipos disponíveis:

- Conta Corrente
- Conta Poupança
- Conta Salário
- Conta Investimento

**Endpoint**: `GET /contas/nova/{clienteId}` (formulário) e `POST /contas/criar` (processamento)

### 2.2 Listagem de Contas

Exibe todas as contas cadastradas no sistema ou as contas de um cliente específico.

**Endpoints**:
- `GET /contas` (todas as contas)
- `GET /contas/cliente/{clienteId}` (contas de um cliente)

### 2.3 Detalhes da Conta

Exibe informações detalhadas de uma conta, incluindo saldo atual e dados do titular.

**Endpoint**: `GET /contas/detalhes/{id}`

### 2.4 Encerramento de Contas

Permite encerrar uma conta bancária existente.

**Endpoint**: `GET /contas/encerrar/{id}`

**Regra de Negócio**: Uma conta só pode ser encerrada se possuir saldo zero.

## 3. Operações Financeiras

### 3.1 Depósitos

Permite realizar depósitos em uma conta, aumentando seu saldo.

**Endpoint**: `GET /transacoes/deposito/{contaId}` (formulário) e `POST /transacoes/depositar` (processamento)

**Parâmetros**:
- Valor do depósito
- Descrição da operação

### 3.2 Saques

Permite realizar saques de uma conta, reduzindo seu saldo.

**Endpoint**: `GET /transacoes/saque/{contaId}` (formulário) e `POST /transacoes/sacar` (processamento)

**Parâmetros**:
- Valor do saque
- Descrição da operação

**Regra de Negócio**: Não é permitido sacar um valor maior que o saldo disponível.

### 3.3 Transferências

Permite transferir valores entre contas do sistema.

**Endpoint**: `GET /transacoes/transferencia/{contaId}` (formulário) e `POST /transacoes/transferir` (processamento)

**Parâmetros**:
- Conta de destino
- Valor da transferência
- Descrição da operação

**Regra de Negócio**: 
- Não é permitido transferir um valor maior que o saldo disponível
- A transferência é registrada como uma transação na conta de origem e na conta de destino

### 3.4 Extrato de Transações

Exibe o histórico de transações de uma conta específica.

**Endpoint**: `GET /transacoes/conta/{contaId}`

## 4. Consultas e Relatórios

### 4.1 Consulta de Saldo

Permite verificar o saldo atual de uma conta.

**Endpoint**: Disponível nos detalhes da conta (`GET /contas/detalhes/{id}`)

### 4.2 Histórico de Transações

Exibe todas as transações realizadas em uma conta, com informações detalhadas.

**Endpoint**: `GET /transacoes/conta/{contaId}`

## 5. Validações e Regras de Negócio

### 5.1 Validação de Dados

- CPF deve ser único no sistema
- E-mail deve ter formato válido
- Valores monetários devem ser positivos

### 5.2 Regras de Transações

- Não é permitido saque ou transferência com valor superior ao saldo disponível
- Todas as transações são registradas com data e hora
- Transações são irreversíveis após confirmação

### 5.3 Regras de Conta

- Cada conta recebe um número único gerado pelo sistema
- Uma conta é associada a apenas um cliente
- Contas com saldo não podem ser encerradas

## 6. Interface de Usuário

### 6.1 Dashboard

Tela inicial com visão geral do sistema e acesso às principais funcionalidades.

**Endpoint**: `/` ou `/home`

### 6.2 Formulários

Interface para entrada de dados em operações como:
- Cadastro de clientes
- Criação de contas
- Operações financeiras (depósito, saque, transferência)

### 6.3 Tabelas e Listagens

Exibição de dados em formato tabular para facilitar a visualização de:
- Lista de clientes
- Lista de contas
- Extrato de transações

### 6.4 Mensagens de Feedback

O sistema fornece feedback para o usuário através de mensagens de:
- Sucesso (operação realizada com êxito)
- Erro (falha na operação com descrição do problema)
- Alerta (informações importantes sobre a operação)

## 7. Segurança

### 7.1 Validação de Dados

Todas as entradas de usuário são validadas para prevenir:
- Injeção de SQL
- Cross-Site Scripting (XSS)
- Entrada de dados inválidos

### 7.2 Registro de Operações

Todas as operações financeiras são registradas com:
- Data e hora
- Usuário responsável
- Detalhes da operação

## 8. Acesso ao Banco de Dados

O sistema inclui uma interface para acesso direto ao banco de dados H2 durante o desenvolvimento.

**Endpoint**: `/h2-console`

**Credenciais**:
- JDBC URL: jdbc:h2:mem:bancoprodb
- Usuário: sa
- Senha: (vazio)
