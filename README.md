# Boardcamp API

Boardcamp API é uma aplicação Spring Boot para gerenciar um sistema de aluguel de jogos de tabuleiro. A aplicação permite criar, listar, atualizar e deletar jogos, clientes e aluguéis.

## Tecnologias Utilizadas

- Java 11
- Spring Boot
- Spring Data JPA
- PostgreSQL
- H2 Database (para testes)
- JUnit 5 (para testes de integração)
- Maven

## Configuração do Ambiente

### Pré-requisitos

- Java 11 ou superior
- Maven
- PostgreSQL

### Configuração do Banco de Dados

1. Crie um banco de dados PostgreSQL chamado `boardcamp`.
2. Atualize as credenciais do banco de dados no arquivo `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/boardcamp
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation=true
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

### Configuração do Banco de Dados para Testes

1. Crie um banco de dados PostgreSQL chamado `boardcamp_test`.
2. Atualize as credenciais do banco de dados no arquivo `src/test/resources/application-test.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/boardcamp_test
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation=true
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

## Executando a Aplicação

Para executar a aplicação, use o seguinte comando:

```sh
mvn spring-boot:run
```

A aplicação estará disponível em [http://localhost:8080](http://localhost:8080).

## Executando os Testes

Para executar os testes de integração, use o seguinte comando:

```sh
mvn test
```

## Endpoints da API

### Jogos

- `POST /games`: Cria um novo jogo.
- `GET /games`: Lista todos os jogos.
- `GET /games/{id}`: Busca um jogo pelo ID.
- `PUT /games/{id}`: Atualiza um jogo pelo ID.
- `DELETE /games/{id}`: Deleta um jogo pelo ID.

### Clientes

- `POST /customers`: Cria um novo cliente.
- `GET /customers`: Lista todos os clientes.
- `GET /customers/{id}`: Busca um cliente pelo ID.
- `PUT /customers/{id}`: Atualiza um cliente pelo ID.
- `DELETE /customers/{id}`: Deleta um cliente pelo ID.

### Aluguéis

- `POST /rentals`: Cria um novo aluguel.
- `GET /rentals`: Lista todos os aluguéis.
- `GET /rentals/{id}`: Busca um aluguel pelo ID.
- `PUT /rentals/{id}`: Atualiza um aluguel pelo ID.
- `DELETE /rentals/{id}`: Deleta um aluguel pelo ID.

## Estrutura do Projeto

```plaintext
src
├── main
│   ├── java
│   │   └── com
│   │       └── boardcamp
│   │           └── api
│   │               ├── controllers
│   │               ├── dtos
│   │               ├── models
│   │               ├── repositories
│   │               └── services
│   └── resources
│       └── application.properties
└── test
  ├── java
  │   └── com
  │       └── boardcamp
  │           └── api
  │               ├── CustomerIntegrationTests.java
  │               ├── GameIntegrationTests.java
  │               └── RentalIntegrationTests.java
  └── resources
    └── application-test.properties
```