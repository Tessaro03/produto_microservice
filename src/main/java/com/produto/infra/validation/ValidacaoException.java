
package com.produto.infra.validation;


public class ValidacaoException extends RuntimeException{

    public ValidacaoException(String mensagem){
      super(new ValidacaoDTO(mensagem).toString());
    }

    
}