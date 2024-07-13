package com.produto.dtos.pedido.output;

import com.produto.dtos.pedido.input.ProdutoIncompletoDTO;
import com.produto.model.Produto;

public record ProdutoCompletoDTO(

        Long idProduto,
        Long idLoja,
        String nomeProduto,
        Double valor,
        Integer quantidade,
        String observacao
) { 

    public ProdutoCompletoDTO(Produto produto, ProdutoIncompletoDTO produtoPedido){
        this(produto.getId(), produto.getIdLoja(), produto.getNomeProduto(),
         produto.getValor(), produtoPedido.quantidade(), produtoPedido.observacao());
    }

}
