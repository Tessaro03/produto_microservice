package com.produto.service;

import java.util.ArrayList;
import java.util.Comparator;
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
import com.produto.infra.security.TokenService;
import com.produto.model.Produto;
import com.produto.repository.ProdutoRepository;
import com.produto.validation.ProdutoValidation;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class ProdutoService {
    
    @Autowired
    private ProdutoRepository repository;

    @Autowired
    private  CategoriaService categoriaService;

    @Autowired 
	private RabbitTemplate rabbitTemplate;

    @Autowired
    private ProdutoValidation validador;

    @Autowired
    private TokenService tokenService;
	

    public void criarProduto(ProdutoInputDTO dto, HttpServletRequest request){
        var loja = tokenService.extrairInformacoes(request);

        var categorias = categoriaService.retornaListaDeCategoria(dto.categorias());
        repository.save(new Produto(dto, categorias, loja.id()));
    }

    public List<ProdutoOutputDTO> listaProdutos(){
        var produtos = repository.findAll();
        return produtos.stream().map(ProdutoOutputDTO::new).collect(Collectors.toList());
    }

    public void alterarProduto(ProdutoAlterarDTO dto, Long idProduto, HttpServletRequest request) {
        var loja = tokenService.extrairInformacoes(request);

        validador.validarAlterar(dto, idProduto, loja.id());
        var produto = repository.getReferenceById(idProduto);
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

    public void deletarProduto(Long idProduto, HttpServletRequest request) {
        var loja = tokenService.extrairInformacoes(request);
        validador.validarDeletar( idProduto, loja.id());

        repository.deleteById(idProduto);
    }

    public void aumentaEstoque(Produto produto, Integer quantidade) {
        produto.setQuantidade(produto.getQuantidade() + quantidade);
        repository.save(produto);
    }

    public void diminuirEstoque(Produto produto, Integer quantidade) {
        produto.setQuantidade(produto.getQuantidade() - quantidade);
        repository.save(produto);
    }
    
    /* Retorna Listas de Produtos para Pedido com os Produtos separados por loja (+ Quantidade e Observação), id do Cliente */
    public void retornaListaDeProduto(Long idCliente,Map<Produto, ProdutoIncompletoDTO> produtos){
        Long idAtual = null;
        List<ProdutoCompletoDTO> produtosSeparados = new ArrayList<>();
        List<ProdutoCompletoDTO> produtosLista = produtos.entrySet().stream().map( p -> new ProdutoCompletoDTO(p.getKey(), p.getValue())).collect(Collectors.toList());
        produtosLista.sort(Comparator.comparing(ProdutoCompletoDTO::idLoja));
        for (int i = 0; i < produtosLista.size(); i++) {
            var produto = produtosLista.get(i);

            if ( i == 0 || produto.idLoja().equals(idAtual)){
                produtosSeparados.add(produto);
            }  else {
                rabbitTemplate.convertAndSend("produto.separado", new PedidoCompletoOutputDTO(idCliente, produtosSeparados));
                produtosSeparados.clear();
                produtosSeparados.add(produto);
            }
            idAtual = produto.idLoja();

            if ( i == produtosLista.size() - 1) {
                rabbitTemplate.convertAndSend("produto.separado", new PedidoCompletoOutputDTO(idCliente, produtosSeparados));
            }
        }
    }
    
    /* Separa Produtos recebidos do Pedido buscando no banco de dados e diminuindo estoque */
    public void separarProdutos(PedidoIncompletoInputDTO pedido) {
        validador.validarSeparar(pedido);
        Map<Produto, ProdutoIncompletoDTO> produtos = new HashMap<>();
        for (ProdutoIncompletoDTO produtoPedido : pedido.produtos()) {
            var produto = repository.findById(produtoPedido.id());
            if (produto.isPresent()) {
                produtos.put(produto.get(), produtoPedido);
                diminuirEstoque(produto.get(), produtoPedido.quantidade());
            }
        }
        
        retornaListaDeProduto(pedido.idCliente(),produtos);
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

