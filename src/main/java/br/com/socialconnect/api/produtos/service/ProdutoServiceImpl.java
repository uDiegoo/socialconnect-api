package br.com.socialconnect.api.produtos.service;

import br.com.socialconnect.api.exception.ConflitoException;
import br.com.socialconnect.api.exception.RecursoNaoEncontradoException;
import br.com.socialconnect.api.exception.RegraNegocioException;
import br.com.socialconnect.api.produtos.dto.ProdutoRequestDTO;
import br.com.socialconnect.api.produtos.dto.ProdutoResponseDTO;
import br.com.socialconnect.api.produtos.model.Produto.CategoriaProduto;
import br.com.socialconnect.api.produtos.model.Produto;
import br.com.socialconnect.api.produtos.repository.ProdutoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ProdutoServiceImpl implements ProdutoService {

    private final ProdutoRepository repository;

    public ProdutoServiceImpl(ProdutoRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<ProdutoResponseDTO> listar(String nome, CategoriaProduto categoria, Pageable pageable) {
        Page<Produto> page;
        boolean temNome = nome != null && !nome.isBlank();

        if (temNome && categoria != null) {
            page = repository.findByNomeContainingIgnoreCaseAndCategoria(nome, categoria, pageable);
        } else if (temNome) {
            page = repository.findByNomeContainingIgnoreCase(nome, pageable);
        } else if (categoria != null) {
            page = repository.findByCategoria(categoria, pageable);
        } else {
            page = repository.findAll(pageable);
        }

        return page.map(this::toDTO);
    }

    @Override
    public ProdutoResponseDTO buscarPorId(Long idProduto) {
        Produto produto = repository.findById(idProduto)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado com o ID: " + idProduto));
        return toDTO(produto);
    }

    @Override
    public ProdutoResponseDTO criar(ProdutoRequestDTO dto) {
        if (dto.estoqueAtual() < 0) {
            throw new RegraNegocioException("O estoque não pode ser negativo: " + dto.estoqueAtual());
        }
        if (repository.existsByNomeIgnoreCase(dto.nome())) {
            throw new ConflitoException("Produto já cadastrado: " + dto.nome());
        }

        Produto produto = Produto.builder()
                .nome(dto.nome())
                .categoria(dto.categoria())
                .estoqueAtual(dto.estoqueAtual())
                .estoqueMinimo(dto.estoqueMinimo())
                .unidadeMedida(dto.unidadeMedida())
                .dataCadastro(LocalDate.now())
                .build();

        return toDTO(repository.save(produto));
    }

    @Override
    public ProdutoResponseDTO atualizar(Long idProduto, ProdutoRequestDTO dto) {
        Produto produto = repository.findById(idProduto)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado com o ID: " + idProduto));

        if (dto.estoqueAtual() < 0) {
            throw new RegraNegocioException("O estoque não pode ser negativo: " + dto.estoqueAtual());
        }
        if (repository.existsByNomeIgnoreCaseAndIdProdutoNot(dto.nome(), idProduto)) {
            throw new ConflitoException("Produto já cadastrado: " + dto.nome());
        }

        produto.setNome(dto.nome());
        produto.setCategoria(dto.categoria());
        produto.setEstoqueAtual(dto.estoqueAtual());
        produto.setEstoqueMinimo(dto.estoqueMinimo());
        produto.setUnidadeMedida(dto.unidadeMedida());

        return toDTO(repository.save(produto));
    }

    @Override
    public void deletar(Long idProduto) {
        Produto produto = repository.findById(idProduto)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado com o ID: " + idProduto));
        repository.delete(produto);
    }

    private ProdutoResponseDTO toDTO(Produto entity) {
        return new ProdutoResponseDTO(
                entity.getIdProduto(), entity.getNome(), entity.getCategoria(),
                entity.getEstoqueAtual(), entity.getEstoqueMinimo(), entity.getUnidadeMedida(),
                entity.getDataCadastro(), entity.getEstoqueAtual() < entity.getEstoqueMinimo()
        );
    }
}
