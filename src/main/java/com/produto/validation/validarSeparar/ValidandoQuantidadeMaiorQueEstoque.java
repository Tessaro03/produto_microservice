package com.produto.validation.validarSeparar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.produto.dtos.pedido.input.ProdutoIncompletoDTO;
import com.produto.infra.validation.ValidacaoExcepetion;
import com.produto.repository.ProdutoRepository;

import jakarta.transaction.Transactional;

@Service
public class ValidandoQuantidadeMaiorQueEstoque implements ValidadorSeparacao{

    @Autowired
    private ProdutoRepository repository;

    @Override
    @Transactional
    public void validar(ProdutoIncompletoDTO dto) {
        
        var produto = repository.getReferenceById(dto.id());
        if (produto.getQuantidade() < dto.quantidade()) {
            throw new ValidacaoExcepetion("Quantidade indisponivel " + produto.getNomeProduto() );
        }
    }
    
}
