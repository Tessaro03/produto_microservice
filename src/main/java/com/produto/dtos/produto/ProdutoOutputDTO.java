package com.produto.dtos.produto;

import java.util.List;
import java.util.stream.Collectors;

import com.produto.dtos.categoria.CategoriaOutputDTO;
import com.produto.model.Produto;

public record ProdutoOutputDTO(
    Long id,
    String nomeProduto,
    Integer quantidade,
    Double valor,
    String descricao,
    List<CategoriaOutputDTO> categoria
) {
    
    public ProdutoOutputDTO(Produto produto){
        this(produto.getId(),produto.getNomeProduto(),produto.getQuantidade(), 
        produto.getValor(),produto.getDescricao(),
         produto.getCategorias().stream().map(CategoriaOutputDTO::new).collect(Collectors.toList()));
    }

}

