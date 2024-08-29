package com.produto.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.produto.model.Categoria;
import com.produto.repository.CategoriaRepository;

public class CategoriaServiceTest {

    @InjectMocks
    private CategoriaService service;

    @Mock 
    private CategoriaRepository repository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    @DisplayName("Não existe a categoria no banco de dados")
    void testCriarCategoriaSeNaoExisteCase1() {
        when(repository.existsByCategoria(any())).thenReturn(false);
        
        service.criarCategoriaSeNaoExiste(anyString());

        verify(repository, times(1)).save(any(Categoria.class));
    }

    @Test
    @DisplayName("Existe a categoria no banco de dados")
    void testCriarCategoriaSeNaoExisteCase2() {
        when(repository.existsByCategoria(any())).thenReturn(true);
        
        service.criarCategoriaSeNaoExiste(anyString());

        verify(repository, times(0)).save(any(Categoria.class));
    }
}
