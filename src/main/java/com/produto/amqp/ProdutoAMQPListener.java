package com.produto.amqp;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.produto.dtos.avaliacao.NotaDTO;
import com.produto.dtos.pedido.input.PedidoIncompletoInputDTO;
import com.produto.service.ProdutoService;

import jakarta.validation.Valid;

@Service
public class ProdutoAMQPListener {

    @Autowired
    private ProdutoService service; 


    @RabbitListener(queues="pedido.solicitado")
    public void separarPedido(@Valid PedidoIncompletoInputDTO pedido){
        service.separarProdutos(pedido);
    }

    
    @RabbitListener(queues="pedido.cancelado-produto")
    public void pedidoCancelado(@Valid PedidoIncompletoInputDTO pedido){
        service.reporProdutos(pedido);
    }

    @RabbitListener(queues="avaliacao.produto")
    public void notaProduto(@Valid NotaDTO nota){
        service.notaProduto(nota);
    }

}