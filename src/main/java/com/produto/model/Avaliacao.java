package com.produto.model;

public enum Avaliacao {
    RUIM(1),
    OK(2),
    BOM(3), 
    EXCELENTE(4),
    PERFEITO(5);

    private final int valor;

    Avaliacao(int valor) {
        this.valor = valor;
    }

    public int getValor() {
        return valor;
    }
}
