# Registro de Uso de IA Generativa

> **Política da disciplina:** O uso de IA generativa é permitido como
> assistente. O discente é **integralmente responsável** por testar, auditar e
> defender todo o código entregue, independentemente de como foi gerado.

## Instruções

Para cada aula ou entrega, registre abaixo:
- **Data**
- **Ferramenta** (ChatGPT, Copilot, Claude, etc.)
- **Prompt(s) utilizado(s)** (resumo ou cópia)
- **O que foi feito com a saída** (copiado integralmente, adaptado, usado como referência, descartado)

---


## Registro

| Data       | Aula                   | Ferramenta | Prompt (resumo)                                                                                                                                                                       | Uso da saída                                                                                                           |
| ---------- | ---------------------- | ---------- |---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------| ---------------------------------------------------------------------------------------------------------------------- |
| data       | aula   | ferramenta | prompt (resumo)                                                                      | uso da saída                                                           |
| ---------- | ------ | ---------- | ------------------------------------------------------------------------------------ | ---------------------------------------------------------------------- |
| 04/09/2026 | aula 4 | chatgpt    | "qual a diferença entre entity e dto no spring boot? ex, quando devo usar cada um?"  | usei como referência pra entender melhor a separação entre as classes. |
| 14/09/2026 | aula 3 | claude     | "como faço uma validação com @notblank e @min no spring boot? tem algum ex simples?" | usei o exemplo pra estudar e adaptei pro meu projeto.                  |
| 02/09/2026 | Aula - 3 | ChatGPT    | "sobre APIs REST com Java e Spring Boot. qual a diferença entre Entity, DTO, Service, Repository e Controller            |
| 16/09/2026 | aula - 3 | Claude     | "pra que serve o Bean Validation no Spring? quero saber tbm @NotBlank, @NotNull, @Size e @Min e mostre exemplos simples."                                                               | Usei como material de estudo e adaptei os exemplos para as validações necessárias no projeto.                          |
| 02/10/2026 | Aula - A1    | Claude     | "requisitos de um módulo de Produtos em uma API Spring Boot e me explique quais entidades, DTOs, validações, regras de negócio e endpoints precisam ser implementados." | enviei o arquivo para ter uma ideia resumida do que estava sendo pedido.. |
| 02/10/2026 | Aula - A1    | Claude     | "Retornando exceção adequada para ser tratada como HTTP 422 no spring (quando um estoque for < que uma quantidade )"                          | Usei como referência para entender a implementação da regra e adaptei a solução ao projeto.                            |
| 02/10/2026 | Aula - A1    | ChatGPT     | "exemplos de endpoint no Swagger/OpenAPI usando @Tag, @Operation, @ApiResponse e @Schema, DTOs."                         | Usei como referência para completar a documentação Swagger e entender as anotações utilizadas.                         |
| 02/10/2026 | Aula - A1    | ChatGPT    | "paginação no spring utilizando Pageable, permitindo filtros opcionais por nome e categoria."                                                   | Usei para entender a paginação e os filtros e adaptei o código à estrutura existente da API.                           |
| 02/10/2026 | Aula - A1    | ChatGPT     | "valide esta implementação de uma API REST em Spring Boot"  | Usei como revisão final. Corrigi os pontos identificados e testei os endpoints antes da entrega via postman.                       |

---

## Declaração de Uso de IA (A1)

### Ferramentas utilizadas:

* [x] Claude
* [x] ChatGPT
* [ ] Gemini
* [ ] Copilot / Codeium
* [ ] Nenhuma

### Como utilizei:
* Usei IA como assistente durante o desenvolvimento e revisão do módulo de Produtos, e durante as aulas para melhor entendimento tanto da parte teórica quanto da parte prática.

### O que eu entendo 100%:
* A separação de responsabilidades entre Entity, DTO, Repository, Service e Controller.
* As validações utilizadas no `ProdutoRequestDTO`, como `@NotBlank`, `@NotNull`, `@Size` e `@Min`.
* A regra que impede que o estoque fique negativo.
* A regra de estoque baixo, em que `estoqueAtual < estoqueMinimo`.
* A validação de nome único e o uso do HTTP `409 Conflict`.
* A utilização de `Pageable` para paginação e filtros.
* A utilização dos principais códigos HTTP dos endpoints do módulo.
* A documentação dos endpoints utilizando Swagger/OpenAPI.

### O que precisei estudar mais:
* Tratamento de erros
* Bean Validation.
* Migrations do Flyway.
* Endpoints utilizando Swagger/OpenAPI.
* Testes de integração utilizando Spring Boot e Testcontainers.


_Declaração: Ao submeter este repositório, confirmo que todo o código foi
revisado, testado e compreendido por mim._

---