package com.produto.validation.validarSeparar;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.when;

import com.produto.dtos.pedido.input.ProdutoIncompletoDTO;
import com.produto.infra.validation.ValidacaoException;
import com.produto.model.Produto;
import com.produto.repository.ProdutoRepository;

public class ValidandoQuantidadeMaiorQueEstoqueTest {

    @Mock
    private ProdutoRepository repository;

    @InjectMocks
    private ValidandoQuantidadeMaiorQueEstoque validacao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    @DisplayName("Quantidade solicitada maior do que a disponível em estoque")
    void testValidarCase1() {
        ProdutoIncompletoDTO dto = new ProdutoIncompletoDTO(1l, 11, null);
        Produto produto = new Produto(1l, 1l, "teste", null, 1d, 10, null, null);
        
        when(repository.getReferenceById(any())).thenReturn(produto);
        
        assertThrows(ValidacaoException.class, () -> validacao.validar(dto));
    }

    @Test
    @DisplayName("Quantidade solicitada menor do que a disponível em estoque")
    void testValidarCase2() {
        ProdutoIncompletoDTO dto = new ProdutoIncompletoDTO(1l, 9, null);
        Produto produto = new Produto(1l, 1l, "teste", null, 1d, 10, null, null);
        
        when(repository.getReferenceById(any())).thenReturn(produto);
        
        assertDoesNotThrow( () -> validacao.validar(dto));
    }
    
    @Test
    @DisplayName("Quantidade solicitada igual do que a disponível em estoque")
    void testValidarCase3() {
        ProdutoIncompletoDTO dto = new ProdutoIncompletoDTO(1l, 10, null);
        Produto produto = new Produto(1l, 1l, "teste", null, 1d, 10, null, null);
        
        when(repository.getReferenceById(any())).thenReturn(produto);
        
        assertDoesNotThrow( () -> validacao.validar(dto));
    }
}
