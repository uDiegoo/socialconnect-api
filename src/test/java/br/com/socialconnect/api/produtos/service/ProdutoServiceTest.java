package br.com.socialconnect.api.produtos.service;

import br.com.socialconnect.api.exception.ConflitoException;
import br.com.socialconnect.api.exception.RegraNegocioException;
import br.com.socialconnect.api.produtos.dto.ProdutoRequestDTO;
import br.com.socialconnect.api.produtos.dto.ProdutoResponseDTO;
import br.com.socialconnect.api.produtos.model.Produto.CategoriaProduto;
import br.com.socialconnect.api.produtos.model.Produto;
import br.com.socialconnect.api.produtos.repository.ProdutoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @Mock
    private ProdutoRepository repository;

    @InjectMocks
    private ProdutoServiceImpl service;

    @Test
    @DisplayName("Deve criar produto quando dados válidos")
    void deveCriarProdutoQuandoDadosValidos() {
        // ==========================================
        // ARRANGE: Preparar o cenário
        // ==========================================
        ProdutoRequestDTO dto = new ProdutoRequestDTO("Arroz 5kg", CategoriaProduto.ALIMENTO, 3, 10, "unidade");
        Produto produtoSalvo = Produto.builder()
                .idProduto(1L).nome("Arroz 5kg").categoria(CategoriaProduto.ALIMENTO)
                .estoqueAtual(3).estoqueMinimo(10).unidadeMedida("unidade")
                .dataCadastro(LocalDate.now())
                .build();
        Mockito.when(repository.existsByNomeIgnoreCase("Arroz 5kg")).thenReturn(false);
        Mockito.when(repository.save(Mockito.any(Produto.class))).thenReturn(produtoSalvo);

        // ==========================================
        // ACT: Executar a ação
        // ==========================================
        ProdutoResponseDTO resultado = service.criar(dto);

        // ==========================================
        // ASSERT: Verificar o resultado
        // ==========================================
        Assertions.assertEquals(1L, resultado.idProduto());
        Assertions.assertEquals("Arroz 5kg", resultado.nome());
        Assertions.assertTrue(resultado.estoqueBaixo(), "3 < 10 deve marcar estoque baixo");
        Mockito.verify(repository, Mockito.times(1)).save(Mockito.any(Produto.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando estoque negativo")
    void deveLancarExcecaoQuandoEstoqueNegativo() {
        // ARRANGE
        ProdutoRequestDTO dto = new ProdutoRequestDTO("Camiseta", CategoriaProduto.ROUPA, -1, 5, "unidade");

        // ACT
        RegraNegocioException excecao = Assertions.assertThrows(RegraNegocioException.class,
                () -> service.criar(dto));

        // ASSERT
        Assertions.assertTrue(excecao.getMessage().contains("negativo"));
        Mockito.verify(repository, Mockito.never()).save(Mockito.any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando nome duplicado")
    void deveLancarExcecaoQuandoNomeDuplicado() {
        // ARRANGE
        ProdutoRequestDTO dto = new ProdutoRequestDTO("Arroz 5kg", CategoriaProduto.ALIMENTO, 3, 10, "unidade");
        Mockito.when(repository.existsByNomeIgnoreCase("Arroz 5kg")).thenReturn(true);

        // ACT
        ConflitoException excecao = Assertions.assertThrows(ConflitoException.class,
                () -> service.criar(dto));

        // ASSERT
        Assertions.assertTrue(excecao.getMessage().contains("Arroz 5kg"));
        Mockito.verify(repository, Mockito.never()).save(Mockito.any());
    }
}
