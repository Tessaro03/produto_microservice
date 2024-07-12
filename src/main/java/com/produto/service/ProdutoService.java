package com.produto.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.produto.dtos.ProdutoInputDTO;
import com.produto.dtos.produto.ProdutoAlterarDTO;
import com.produto.dtos.produto.ProdutoOutputDTO;
import com.produto.model.Produto;
import com.produto.repository.ProdutoRepository;

@Service
public class ProdutoService {
    
    @Autowired
    private ProdutoRepository repository;

    @Autowired
    private  CategoriaService categoriaService;

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

    public void aumentaEstoque(Long id, Integer quantidade) {
        var produto = repository.getReferenceById(id);
        produto.setQuantidade(produto.getQuantidade() + quantidade);
        repository.save(produto);
    }

    public void diminuirEstoque(Long id, Integer quantidade) {
        var produto = repository.getReferenceById(id);
        produto.setQuantidade(produto.getQuantidade() - quantidade);
        repository.save(produto);
    }
}
