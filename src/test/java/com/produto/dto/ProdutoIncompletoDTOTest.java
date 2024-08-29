package com.produto.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.produto.dtos.pedido.input.ProdutoIncompletoDTO;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;


public class ProdutoIncompletoDTOTest {

    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void quantidadeMenorQueMin(){
        ProdutoIncompletoDTO dto = new ProdutoIncompletoDTO(1l, 0, null);

        Set<ConstraintViolation<ProdutoIncompletoDTO>> violations = validator.validate(dto);
        
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());

        ConstraintViolation<ProdutoIncompletoDTO> violation = violations.iterator().next();
        assertEquals("deve ser maior que ou igual à 1", violation.getMessage());
    }
    
    @Test
    void quantidadeMaiorQueMin(){
        ProdutoIncompletoDTO dto = new ProdutoIncompletoDTO(1l, 2, null);

        Set<ConstraintViolation<ProdutoIncompletoDTO>> violations = validator.validate(dto);
        
        assertTrue(violations.isEmpty());
        assertEquals(0, violations.size());
    }

    @Test
    void quantidadeIgualMin(){
        ProdutoIncompletoDTO dto = new ProdutoIncompletoDTO(1l, 1, null);

        Set<ConstraintViolation<ProdutoIncompletoDTO>> violations = validator.validate(dto);
        
        assertTrue(violations.isEmpty());
        assertEquals(0, violations.size());
    }


}