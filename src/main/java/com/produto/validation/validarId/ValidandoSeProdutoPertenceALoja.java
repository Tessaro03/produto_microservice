package com.produto.validation.validarId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.produto.infra.validation.ValidacaoException;
import com.produto.repository.ProdutoRepository;

@Service
public class ValidandoSeProdutoPertenceALoja implements ValidadorId {

    @Autowired
    private ProdutoRepository repository;

    @Override
    public void validar(Long idProduto, Long idLoja) {
        var produto = repository.findById(idProduto);
        if (produto.isPresent()) {
            if (produto.get().getIdLoja().equals(idLoja)) {
                throw new ValidacaoException("Produto não pertence a loja");
            }
        }
    }

    @Override
    public void validar(Long id) {
    }

    
    
}
