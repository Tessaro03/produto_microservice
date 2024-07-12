package com.produto.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.produto.dtos.categoria.CategoriaOutputDTO;
import com.produto.model.Categoria;
import com.produto.repository.CategoriaRepository;
import com.produto.repository.ProdutoRepository;

@Service
public class CategoriaService {
    
    @Autowired
    private CategoriaRepository repository;

    @Autowired
    private ProdutoRepository produtoRepository;
 
    public void criarCategoriaSeNaoExiste(String categoriaNome){
        if (!repository.existsByCategoria(categoriaNome)){
            var categoria = new Categoria(categoriaNome);
            repository.save(categoria);
        }
    }

    public List<Categoria> retornaListaDeCategoria(List<String> categoriasString){
        List<Categoria> categorias = new ArrayList<>();
        categoriasString.forEach(c -> criarCategoriaSeNaoExiste(c));
        for (String categoriaString : categoriasString) {

            var categoria = repository.getReferenceByName(categoriaString);
            categorias.add(categoria);
        }
        return categorias;
    }

    public List<CategoriaOutputDTO> verCategorias() {
        var categorias = repository.findAll();
        return categorias.stream().map(CategoriaOutputDTO::new).collect(Collectors.toList());
    }

    public void deletarCategoria(Long id) {
        repository.deletarRelacaoCategoriaProdutos(id);
        repository.deleteById(id);
    }

}
