package com.produto.dtos.pedido.output;

import java.util.List;

public record PedidoCompletoOutputDTO(

    Long idCliente,

    List<ProdutoCompletoDTO> produtos

) {
    

   
}
