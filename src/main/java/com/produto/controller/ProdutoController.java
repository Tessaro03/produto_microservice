package com.produto.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.produto.dtos.pedido.PedidoProdutosInputDTO;
import com.produto.dtos.produto.ProdutoAlterarDTO;
import com.produto.dtos.produto.ProdutoInputDTO;
import com.produto.service.ProdutoService;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired 
    private ProdutoService service;
 
    @GetMapping
    public ResponseEntity verProdutos() {    
        var produtos = service.listaProdutos();
        return ResponseEntity.ok().body(produtos);
    }
    
    @PostMapping
    public void criarProduto(@RequestBody ProdutoInputDTO dto){
        service.criarProduto(dto);
    }

    @PatchMapping("/{id}")
    public void  alterarProduto(@PathVariable Long id,@RequestBody ProdutoAlterarDTO dto){
        service.alterarProduto(dto, id);
    }

    @DeleteMapping("/{id}")
    public void deletarProduto(@PathVariable Long id){
        service.deletarProduto(id);
    }

    @PostMapping("/selecionar")
    public void postMethodName(@RequestBody List<PedidoProdutosInputDTO> pedidoProdutos) {
        service.separarProdutos(pedidoProdutos);
    }
    

}
