package com.produto.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.produto.dtos.avaliacao.NotaDTO;
import com.produto.dtos.pedido.input.PedidoIncompletoInputDTO;
import com.produto.dtos.pedido.input.ProdutoIncompletoDTO;
import com.produto.dtos.pedido.output.PedidoCompletoOutputDTO;
import com.produto.dtos.pedido.output.ProdutoCompletoDTO;
import com.produto.dtos.produto.ProdutoAlterarDTO;
import com.produto.dtos.produto.ProdutoInputDTO;
import com.produto.dtos.produto.ProdutoOutputDTO;
import com.produto.model.Produto;
import com.produto.repository.ProdutoRepository;

@Service
public class ProdutoService {
    
    @Autowired
    private ProdutoRepository repository;

    @Autowired
    private  CategoriaService categoriaService;

    @Autowired 
	private RabbitTemplate rabbitTemplate;
	

    public void criarProduto(ProdutoInputDTO dto){
        var categorias = categoriaService.retornaListaDeCategoria(dto.categorias());
        repository.save(new Produto(dto, categorias));
    }

    public List<ProdutoOutputDTO> listaProdutos(){
        var produtos = repository.findAll();
        return produtos.stream().map(ProdutoOutputDTO::new).collect(Collectors.toList());
    }

    public void alterarProduto(ProdutoAlterarDTO dto, Long id) {
        var produto = repository.getReferenceById(id);
        if (dto.nomeProduto() != null) 
            produto.setNomeProduto(dto.nomeProduto());
        if (dto.descricao() != null)
            produto.setDescricao(dto.descricao());
        if (dto.categorias() != null){
            var categorias = categoriaService.retornaListaDeCategoria(dto.categorias());
            produto.setCategorias(categorias);
        }
        if (dto.valor() != null) 
            produto.setValor(dto.valor());
        if (dto.quantidade() != null) 
            produto.setQuantidade(dto.quantidade());
        repository.save(produto);
    }

    public void deletarProduto(Long id) {
        repository.deleteById(id);
    }

    public void aumentaEstoque(Produto produto, Integer quantidade) {
        produto.setQuantidade(produto.getQuantidade() + quantidade);
        repository.save(produto);
    }

    public void diminuirEstoque(Produto produto, Integer quantidade) {
        produto.setQuantidade(produto.getQuantidade() - quantidade);
        repository.save(produto);
    }

    
    /* Separa Produtos recebidos do Pedido buscando no banco de dados e diminuindo estoque */
    public void separarProdutos(PedidoIncompletoInputDTO pedido) {
        Map<Produto, ProdutoIncompletoDTO> produtos = new HashMap<>();
        for (ProdutoIncompletoDTO produtoPedido : pedido.produtos()) {
            var produto = repository.findById(produtoPedido.id());
            if (produto.isPresent()) {
                produtos.put(produto.get(), produtoPedido);
                diminuirEstoque(produto.get(), produtoPedido.quantidade());
            }
        }
        retornaListaDeProduto(pedido.idPedido(),produtos);
    }
    
    
    /* Retorna Lista de Produto para Pedido com os Produtos (+ Quantidade e Observação), id do Pedido */
    public void retornaListaDeProduto(Long idPedido,Map<Produto, ProdutoIncompletoDTO> produtos){
        var produtosLista = produtos.entrySet()
        .stream().map( p -> new ProdutoCompletoDTO(p.getKey(), p.getValue())).collect(Collectors.toList());
        rabbitTemplate.convertAndSend("produto.separado",new PedidoCompletoOutputDTO(idPedido, produtosLista));    
        
    }

    // Recebera lista de pedido para repor estoque do produto
    public void reporProdutos(PedidoIncompletoInputDTO pedido) {
        for (ProdutoIncompletoDTO produtoPedido : pedido.produtos()) {
            var produto = repository.findById(produtoPedido.id());
            if (produto.isPresent()) {
                aumentaEstoque(produto.get(), produtoPedido.quantidade());
            }
        }
     }

    public void notaProduto(NotaDTO nota) {
        var produto = repository.findById(nota.idProduto());
        if (produto.isPresent()) {
            produto.get().setNota(nota.nota());
            repository.save(produto.get());
        }
    }
 
}

