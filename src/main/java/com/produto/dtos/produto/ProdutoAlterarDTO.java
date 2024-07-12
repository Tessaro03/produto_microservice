package com.produto.dtos.produto;

import java.util.List;


public record ProdutoAlterarDTO(

    String nomeProduto,
    
    String descricao,
        
    List<String> categorias,
    
    Double valor,

    Integer quantidade


) {
    
}
