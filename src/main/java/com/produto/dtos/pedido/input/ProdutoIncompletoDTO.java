package com.produto.dtos.pedido.input;

import jakarta.validation.constraints.NotNull;

public record ProdutoIncompletoDTO(

    @NotNull
    Long id,

    @NotNull
    Integer quantidade,

    String observacao

) {

}
