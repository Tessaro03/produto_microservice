package com.produto.validation.validarId;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.produto.infra.validation.ValidacaoException;
import com.produto.repository.ProdutoRepository;

public class ValidarSeProdutoExisteTest {

    @Mock
    private ProdutoRepository repository;

    @InjectMocks
    private ValidarSeProdutoExiste validacao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Produto não existe")
    void testValidarCase1() {
        when(repository.existsById(1l)).thenReturn(false);
        assertThrows(ValidacaoException.class, () -> validacao.validar(1l));
    }

    @Test
    @DisplayName("Produto existe")
    void testValidarCase2() {
        when(repository.existsById(1l)).thenReturn(true);
        assertDoesNotThrow(() -> validacao.validar(1l));
    }
}
