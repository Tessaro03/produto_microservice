package com.produto.amqp;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.produto.dtos.pedido.input.PedidoIncompletoInputDTO;
import com.produto.service.ProdutoService;

@Service
public class ProdutoAMQPListener {

    @Autowired
    private ProdutoService service; 


    @RabbitListener(queues="pedido.solicitado")
    public void separarPedido(PedidoIncompletoInputDTO pedido){
        service.separarProdutos(pedido);
    }

    
    @RabbitListener(queues="pedido.cancelado-produto")
    public void pedidoCancelado(PedidoIncompletoInputDTO pedido){
        service.reporProdutos(pedido);
    }

}