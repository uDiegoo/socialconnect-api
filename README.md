# SocialConnect API

> API RESTful de gestão para instituições sociais (ONGs, bancos de alimentos,
> CRAS, abrigos). Conecta doadores, voluntários e beneficiários.

**Disciplina:** Tópicos Especiais em Sistemas para Internet III
**Stack:** Java 21 · Spring Boot 4.1.1 · JPA · H2 (dev) · PostgreSQL (prod)

---

## Como Rodar

### Pré-requisitos

- JDK 21 LTS ([Adoptium](https://adoptium.net/))
- Maven 3.9+ (ou use o wrapper: `./mvnw`)
- IDE: IntelliJ IDEA (recomendado) ou VS Code

### Passos

```bash
# 1. Clone o repositório
git clone <url-do-repo>
cd socialconnect-api

# 2. Compile o projeto
mvn clean compile

# 3. Rode a aplicação
mvn spring-boot:run
```

Swagger UI: http://localhost:8080/swagger-ui.html

## Módulo de Produtos (Avaliação A1)

Controle de estoque de doações físicas, com alerta de estoque baixo.

| Método | Endpoint | Resposta |
|--------|----------|----------|
| GET | `/api/v1/produtos?page=0&size=10&sort=nome,asc&nome=arroz&categoria=ALIMENTO` | 200 (página) |
| GET | `/api/v1/produtos/{idProduto}` | 200 / 404 |
| POST | `/api/v1/produtos` | 201 + `Location` / 400 / 409 / 422 |
| PUT | `/api/v1/produtos/{idProduto}` | 200 / 400 / 404 / 409 / 422 |
| DELETE | `/api/v1/produtos/{idProduto}` | 204 / 404 |

Regras de negócio:
- **Estoque nunca negativo** → `422 Unprocessable Entity` (Problem Details).
- **Nome único** (sem diferenciar maiúsculas) → `409 Conflict`.
- `estoqueBaixo` na resposta é `true` quando `estoqueAtual < estoqueMinimo`.

Erros seguem o formato Problem Details (RFC 7807, `application/problem+json`).

Estrutura: `produtos/{controller,service,repository,dto,model}`, `exception/` (handler global),
`validation/` (`@EstoqueNaoNegativo`), migration `db/migration/V2__create_produtos_table.sql`.

## Testes

```bash
mvn test        # unitários (Mockito) + integração (Testcontainers/PostgreSQL 17, exige Docker)
```
