package com.produto.dtos.pedido.input;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;


public record PedidoIncompletoInputDTO(

    @NotNull
    Long idCliente,

    @NotEmpty
    List<ProdutoIncompletoDTO> produtos

) {
}