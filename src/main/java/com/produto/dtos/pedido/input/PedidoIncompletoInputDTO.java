package com.produto.dtos.pedido.input;

import java.util.List;

/**
 * PedidoInputDTO
 */
public record PedidoIncompletoInputDTO(

    Long idPedido,

    List<ProdutoIncompletoDTO> produtosPedido

) {
}