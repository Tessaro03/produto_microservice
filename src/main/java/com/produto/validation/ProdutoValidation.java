package com.produto.validation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.produto.dtos.pedido.input.PedidoIncompletoInputDTO;
import com.produto.dtos.pedido.input.ProdutoIncompletoDTO;
import com.produto.dtos.produto.ProdutoAlterarDTO;
import com.produto.validation.validarId.ValidadorId;
import com.produto.validation.validarSeparar.ValidadorSeparacao;

import jakarta.validation.Valid;

@Service
public class ProdutoValidation {

    @Autowired
    private List<ValidadorId> validadorId;

    @Autowired
    private List<ValidadorSeparacao> validadorSeparacao;

    public void validarSeparar(@Valid PedidoIncompletoInputDTO dto){
        for (ProdutoIncompletoDTO produto : dto.produtos()) {
            validadorId.forEach(v -> v.validar(produto.id()));
            validadorSeparacao.forEach(v -> v.validar(produto));
        }
    }

    public void validarAlterar(ProdutoAlterarDTO dto, Long idProduto, Long idLoja){
        validadorId.forEach(v -> v.validar(idProduto, idLoja));

    }

    public void validarDeletar(Long idProduto, Long idLoja){
        validadorId.forEach(v -> v.validar(idProduto, idLoja));

    }

}
