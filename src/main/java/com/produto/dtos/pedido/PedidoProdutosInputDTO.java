package com.produto.dtos.pedido;

import jakarta.validation.constraints.NotNull;

public record PedidoProdutosInputDTO(

    @NotNull
    Long id,

    @NotNull
    Integer quantidade

) {

}
