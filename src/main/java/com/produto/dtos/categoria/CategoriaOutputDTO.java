package com.produto.dtos.categoria;

import com.produto.model.Categoria;

public record CategoriaOutputDTO(

    Long id,
    String nome

) {
    
    public CategoriaOutputDTO(Categoria categoria){
        this(categoria.getId(), categoria.getNome());
    }

}
