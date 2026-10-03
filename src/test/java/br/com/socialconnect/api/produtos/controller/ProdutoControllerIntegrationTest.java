package br.com.socialconnect.api.produtos.controller;

import br.com.socialconnect.api.produtos.dto.ProdutoRequestDTO;
import br.com.socialconnect.api.produtos.dto.ProdutoResponseDTO;
import br.com.socialconnect.api.produtos.model.Produto.CategoriaProduto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class ProdutoControllerIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:17");

    @DynamicPropertySource
    static void propriedades(DynamicPropertyRegistry registry) {
        registry.add("spring.jpa.database-platform", () -> "org.hibernate.dialect.PostgreSQLDialect");
    }

    @LocalServerPort
    private int porta;

    private RestTestClient cliente;

    @BeforeEach
    void configurarCliente() {
        cliente = RestTestClient.bindToServer().baseUrl("http://localhost:" + porta).build();
    }

    @Test
    @DisplayName("Deve criar produto quando dados válidos")
    void deveCriarProdutoQuandoDadosValidos() {
        // ARRANGE
        ProdutoRequestDTO dto = new ProdutoRequestDTO("Arroz 5kg (integração)", CategoriaProduto.ALIMENTO, 3, 10, "unidade");

        // ACT
        ProdutoResponseDTO criado = cliente.post().uri("/api/v1/produtos")
                .contentType(MediaType.APPLICATION_JSON)
                .body(dto)
                .exchange()
                // ASSERT
                .expectStatus().isEqualTo(HttpStatus.CREATED)
                .expectHeader().exists("Location")
                .expectBody(ProdutoResponseDTO.class)
                .returnResult().getResponseBody();

        Assertions.assertNotNull(criado);
        Assertions.assertNotNull(criado.idProduto());
        Assertions.assertTrue(criado.estoqueBaixo());

        cliente.get().uri("/api/v1/produtos/{id}", criado.idProduto())
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @DisplayName("Deve retornar 409 quando nome duplicado")
    void deveRetornar409QuandoNomeDuplicado() {
        // ARRANGE
        ProdutoRequestDTO dto = new ProdutoRequestDTO("Sabonete (duplicado)", CategoriaProduto.HIGIENE, 50, 20, "unidade");
        cliente.post().uri("/api/v1/produtos").contentType(MediaType.APPLICATION_JSON).body(dto)
                .exchange().expectStatus().isEqualTo(HttpStatus.CREATED);

        // ACT
        cliente.post().uri("/api/v1/produtos").contentType(MediaType.APPLICATION_JSON).body(dto)
                .exchange()
                // ASSERT
                .expectStatus().isEqualTo(HttpStatus.CONFLICT)
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON)
                .expectBody()
                .jsonPath("$.status").isEqualTo(409)
                .jsonPath("$.title").isNotEmpty();
    }

    @Test
    @DisplayName("Deve retornar 422 quando estoque negativo")
    void deveRetornar422QuandoEstoqueNegativo() {
        // ARRANGE
        ProdutoRequestDTO dto = new ProdutoRequestDTO("Casaco (estoque negativo)", CategoriaProduto.ROUPA, -5, 10, "unidade");

        // ACT
        cliente.post().uri("/api/v1/produtos").contentType(MediaType.APPLICATION_JSON).body(dto)
                .exchange()
                // ASSERT
                .expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_CONTENT)
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON)
                .expectBody()
                .jsonPath("$.status").isEqualTo(422)
                .jsonPath("$.errors[0].field").isEqualTo("estoqueAtual");
    }
}
