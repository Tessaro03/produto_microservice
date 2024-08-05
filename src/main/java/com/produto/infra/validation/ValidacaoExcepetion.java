
package com.produto.infra.validation;


public class ValidacaoExcepetion extends RuntimeException{

    public ValidacaoExcepetion(String mensagem){
      super(new ValidacaoDTO(mensagem).toString());
    }

    
}