# Sistema Bancário Fictício

Este projeto implementa um sistema bancário simples com interface web para gerenciar clientes, contas e operações bancárias.

## Funcionalidades

- Cadastro e gerenciamento de clientes
- Criação de diferentes tipos de contas (Corrente, Poupança, Investimento)
- Operações bancárias (depósito, saque, investimento, resgate)
- Cálculo de rendimentos, tarifas e impostos
- Interface web responsiva para interação com o sistema

## Tecnologias Utilizadas

- Java 11
- Spring Boot 2.7.13
- Spring MVC
- Thymeleaf
- Bootstrap 5
- Maven

## Estrutura do Projeto

- `com.banco.domain`: Classes de domínio (Cliente, Conta, etc.)
- `com.banco.exception`: Exceções personalizadas
- `com.banco.service`: Serviços para regras de negócio
- `com.banco.controller`: Controladores REST e Web
- `resources/templates`: Templates HTML da interface web

## Como Executar

### Pré-requisitos

- Java 11 ou superior
- Maven 3.6 ou superior

### Passos para Execução

1. Clone o repositório:

   ```
   git clone https://github.com/artubss/banco-ficticio.git
   cd banco-ficticio
   ```

2. Compile o projeto com Maven:

   ```
   mvn clean package
   ```

3. Execute a aplicação:

   ```
   mvn spring-boot:run
   ```

4. Acesse a aplicação no navegador:
   ```
   http://localhost:8080
   ```

## API REST

A aplicação também disponibiliza uma API REST para integração com outros sistemas:

### Endpoints Principais

- `GET /api/banco/clientes`: Lista todos os clientes
- `GET /api/banco/contas`: Lista todas as contas
- `POST /api/banco/contas/{numero}/depositar?valor={valor}`: Realiza depósito
- `POST /api/banco/contas/{numero}/sacar?valor={valor}`: Realiza saque
- `POST /api/banco/contas/{numero}/calcular-rendimento`: Calcula rendimento (poupança)
- `GET /api/banco/contas/{numero}/calcular-imposto`: Calcula imposto (investimento)

## Testes

Para executar os testes da aplicação:

```
mvn test
```

## Interface Web

A interface web permite:

1. Visualizar dashboard com resumo de clientes e contas
2. Listar e cadastrar clientes
3. Criar diferentes tipos de contas
4. Realizar operações bancárias (depósito, saque, etc.)
5. Visualizar detalhes de contas e clientes
6. Calcular rendimentos, tarifas e impostos

## Exemplos de Uso

### Criar um Cliente

1. Acesse a página "Novo Cliente"
2. Preencha os dados do cliente (nome, CPF, data de nascimento)
3. Clique em "Cadastrar"

### Realizar um Depósito

1. Acesse a página de detalhes da conta
2. Informe o valor do depósito
3. Clique em "Depositar"
4. O saldo será atualizado automaticamente

### Calcular Rendimento de Poupança

1. Acesse a página de detalhes da conta poupança
2. Clique em "Calcular Rendimento"
3. O saldo será atualizado com o valor do rendimento
