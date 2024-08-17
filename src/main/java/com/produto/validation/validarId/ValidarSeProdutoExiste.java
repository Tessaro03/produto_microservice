package com.produto.validation.validarId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.produto.infra.validation.ValidacaoException;
import com.produto.repository.ProdutoRepository;

import jakarta.transaction.Transactional;

@Service
public class ValidarSeProdutoExiste implements ValidadorId{
    
    @Autowired 
    private ProdutoRepository repository;

    @Override
    @Transactional
    public void validar(Long id) {
        if (!repository.existsById(id)) {
            throw new ValidacaoException("id "+ id + " não encontrado");
        }
    }

    

}
