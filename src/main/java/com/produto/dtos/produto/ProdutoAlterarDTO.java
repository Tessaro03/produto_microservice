package com.produto.dtos.produto;

import java.util.List;

import jakarta.validation.constraints.Min;


public record ProdutoAlterarDTO(

    String nomeProduto,
    
    String descricao,
        
    List<String> categorias,
    
    @Min(0)
    Double valor,

    @Min(0)
    Integer quantidade


) {
    
}
