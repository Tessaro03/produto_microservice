package com.produto.dtos.pedido.output;

import java.util.List;

public record PedidoCompletoOutputDTO(

    Long idPedido,

    List<ProdutoCompletoDTO> produto

) {
    

   
}
