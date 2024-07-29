package com.produto.dtos.categoria;

import jakarta.validation.constraints.NotBlank;

public record CategoriaInputDTO(

    @NotBlank
    String nome

) {
    
}
