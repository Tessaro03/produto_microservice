package com.produto.validation.validarId;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.produto.infra.validation.ValidacaoException;
import com.produto.model.Produto;
import com.produto.repository.ProdutoRepository;

public class ValidandoSeProdutoPertenceALojaTest {
    
    @Mock
    private ProdutoRepository repository;

    @InjectMocks
    private ValidandoSeProdutoPertenceALoja validacao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Produto pertence a loja")
    void testValidarCase1() {
        Produto produto = new Produto(1l, 1l, "teste", null, 1d, 10, null, null);
        Optional<Produto> produtoOptional = Optional.of(produto);
        
        when(repository.findById(any())).thenReturn(produtoOptional);

        assertThrows(ValidacaoException.class, () -> validacao.validar(1l, 1l));
    }

    @Test
    @DisplayName("Produto não pertence a loja")
    void testValidarCase2() {
        Produto produto = new Produto(1l, 2l, "teste", null, 1d, 10, null, null);
        Optional<Produto> produtoOptional = Optional.of(produto);
        
        when(repository.findById(any())).thenReturn(produtoOptional);

        assertDoesNotThrow( () -> validacao.validar(1l,1l));
    }
     
}
