package br.com.socialconnect.api.produtos.dto;

import br.com.socialconnect.api.produtos.model.Produto.CategoriaProduto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Produto do estoque da ONG")
public record ProdutoResponseDTO(
        @Schema(description = "Identificador do produto", example = "1")
        Long idProduto,

        @Schema(example = "Arroz 5kg")
        String nome,

        @Schema(example = "ALIMENTO")
        CategoriaProduto categoria,

        @Schema(description = "Quantidade atual em estoque", example = "3")
        Integer estoqueAtual,

        @Schema(description = "Quantidade mínima desejada em estoque", example = "10")
        Integer estoqueMinimo,

        @Schema(example = "unidade")
        String unidadeMedida,

        @Schema(description = "Data em que o produto foi cadastrado", example = "2026-10-02")
        LocalDate dataCadastro,

        @Schema(description = "true quando estoqueAtual < estoqueMinimo (produto precisa ser solicitado aos doadores)", example = "true")
        boolean estoqueBaixo
) {


}
