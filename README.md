⏱️ Bate Ponto API

API REST para registro de ponto eletrônico, com regras de negócio centralizadas no backend, seguindo conceitos da CLT, permitindo batidas de ponto flexíveis e cálculo de horas trabalhadas por dia.

Projeto desenvolvido com Spring Boot, JPA/Hibernate e H2 (in-memory) para ambiente de desenvolvimento.

🚀 Tecnologias utilizadas

Java 17

Spring Boot

Spring Web

Spring Data JPA

Hibernate

Banco H2 (em memória)

Maven

React (frontend consumidor da API)

📐 Arquitetura

O projeto segue uma arquitetura em camadas:

controller  →  service  →  repository  →  database


Responsabilidades:

Controller: apenas recebe e responde requisições HTTP

Service: regras de negócio (CLT, validações, cálculo de horas)

Repository: acesso ao banco de dados

DTOs: contrato de entrada e saída da API

Model: entidades JPA

▶️ Como rodar o projeto

Para desenvolvimento local sem Postgres/MySQL, use o profile `local`, que sobe H2 persistente em `./data/bate-ponto` e cria um usuario admin inicial:

```bash
./mvnw -o spring-boot:run -Dspring-boot.run.profiles=local
```

Credenciais locais:

```text
E-mail: admin@bateponto.com
Senha: admin123
```


A aplicação sobe em:

http://localhost:8080

🧪 Testes

Os testes podem ser executados com:

mvn test
