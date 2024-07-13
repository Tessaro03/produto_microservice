package com.produto.dtos.produto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProdutoInputDTO(

    @NotBlank
    String nomeProduto,
    
    String descricao,
    
    List<String> categorias,
    
    @NotNull
    Double valor,

    @NotNull
    Integer quantidade
) {
    
}
