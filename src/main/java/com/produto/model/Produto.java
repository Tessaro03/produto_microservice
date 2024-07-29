package com.produto.model;

import java.util.List;

import com.produto.dtos.produto.ProdutoInputDTO;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name="Produto")
@Table(name="produtos")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Produto {
    
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    
    private Long idLoja;
    
    private String nomeProduto;
    
    private String descricao;
    
    private Double valor;

    private Integer quantidade;

    private Double nota;
    
    @ManyToMany
    private List<Categoria> categorias;
    
    public Produto(ProdutoInputDTO dto, List<Categoria> categorias){
    
        this.idLoja = 1l;
        this.nomeProduto = dto.nomeProduto();
        this.descricao = dto.descricao();
        this.valor = dto.valor();
        this.quantidade = dto.quantidade();
        this.categorias = categorias;
        this.nota = 0.0;
    }
}
