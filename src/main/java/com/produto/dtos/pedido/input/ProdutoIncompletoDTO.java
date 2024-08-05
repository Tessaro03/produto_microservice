package com.produto.dtos.pedido.input;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ProdutoIncompletoDTO(

    @NotNull
    Long id,

    @NotNull
    @Min(1)
    Integer quantidade,

    String observacao

) {

}
