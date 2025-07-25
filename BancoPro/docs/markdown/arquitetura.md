# Arquitetura do BancoPro

## Visão Geral

O BancoPro foi desenvolvido utilizando uma arquitetura em camadas baseada no padrão MVC (Model-View-Controller), com foco em separação de responsabilidades e manutenibilidade do código.

## Camadas da Aplicação

### 1. Camada de Apresentação (View e Controller)

#### Controllers

Responsáveis por receber as requisições HTTP, validar os dados de entrada, chamar os serviços apropriados e retornar as respostas adequadas.

- **ClienteController**: Gerencia as operações relacionadas aos clientes
- **ContaController**: Gerencia as operações relacionadas às contas bancárias
- **TransacaoController**: Gerencia as operações de transações financeiras

#### Views (Thymeleaf)

Templates que definem a interface de usuário da aplicação, renderizando os dados recebidos dos controllers.

### 2. Camada de Negócio (Service)

Contém a lógica de negócio da aplicação, implementando as regras e validações necessárias.

- **ClienteService**: Implementa a lógica de negócio relacionada aos clientes
- **ContaService**: Implementa a lógica de negócio relacionada às contas
- **TransacaoService**: Implementa a lógica de negócio relacionada às transações

### 3. Camada de Persistência (Repository)

Responsável pela comunicação com o banco de dados, utilizando Spring Data JPA.

- **ClienteRepository**: Operações de CRUD para a entidade Cliente
- **ContaRepository**: Operações de CRUD para a entidade Conta
- **TransacaoRepository**: Operações de CRUD para a entidade Transacao

### 4. Camada de Modelo (Model)

Define as entidades que representam os dados da aplicação.

- **Cliente**: Entidade que representa um cliente do banco
- **Conta**: Entidade que representa uma conta bancária
- **Transacao**: Entidade que representa uma transação financeira

## Fluxo de Processamento

1. O cliente faz uma requisição HTTP para um endpoint específico
2. O controller correspondente recebe a requisição
3. O controller valida os dados e chama o serviço apropriado
4. O serviço executa a lógica de negócio, utilizando os repositories quando necessário
5. O repository acessa o banco de dados
6. O resultado é retornado pelo serviço para o controller
7. O controller prepara os dados para a view
8. A view renderiza a página HTML com os dados
9. A resposta é enviada de volta para o cliente

## Tecnologias Utilizadas

- **Spring Boot**: Framework para criação de aplicações Java
- **Spring MVC**: Framework para desenvolvimento de aplicações web
- **Spring Data JPA**: Framework para acesso a dados
- **Hibernate**: ORM para mapeamento objeto-relacional
- **Thymeleaf**: Engine de templates para criação de views
- **H2 Database**: Banco de dados em memória para desenvolvimento
- **Lombok**: Biblioteca para redução de código boilerplate
- **Jakarta Validation**: Biblioteca para validação de dados

## Padrões de Projeto Utilizados

- **MVC (Model-View-Controller)**: Separa a aplicação em três componentes principais
- **Dependency Injection**: Utilizado para injeção de dependências através do Spring
- **Repository Pattern**: Abstrai o acesso aos dados
- **Service Layer**: Encapsula a lógica de negócio
- **DTO (Data Transfer Object)**: Utilizado para transferência de dados entre camadas

## Diagrama de Arquitetura

```
+----------------------------------+
|           Presentation           |
|  +---------------------------+   |
|  |        Controllers        |   |
|  +---------------------------+   |
|  |          Views           |   |
|  +---------------------------+   |
+----------------------------------+
                |
                v
+----------------------------------+
|            Business              |
|  +---------------------------+   |
|  |         Services         |   |
|  +---------------------------+   |
+----------------------------------+
                |
                v
+----------------------------------+
|           Persistence            |
|  +---------------------------+   |
|  |       Repositories       |   |
|  +---------------------------+   |
+----------------------------------+
                |
                v
+----------------------------------+
|           Database               |
|  +---------------------------+   |
|  |            H2            |   |
|  +---------------------------+   |
+----------------------------------+
```

## Configuração da Aplicação

A configuração da aplicação é realizada principalmente através do arquivo `application.properties`, que define:

- Configurações do banco de dados H2
- Configurações do JPA/Hibernate
- Porta do servidor (8081)
- Outras configurações do Spring Boot

## Considerações de Escalabilidade

A arquitetura do BancoPro foi projetada pensando em possível escalabilidade:

- **Separação de Responsabilidades**: Facilita a manutenção e evolução do código
- **Camadas Bem Definidas**: Permite a substituição de componentes sem afetar o restante da aplicação
- **Uso de Interfaces**: Permite diferentes implementações para os mesmos contratos

Para uma versão de produção, recomenda-se:

- Migração para um banco de dados persistente (PostgreSQL, MySQL)
- Implementação de cache para melhorar a performance
- Configuração de pools de conexão adequados
- Implementação de mecanismos de autenticação e autorização
- Configuração de balanceamento de carga para alta disponibilidade
