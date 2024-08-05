package com.produto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.produto.dtos.categoria.CategoriaInputDTO;
import com.produto.service.CategoriaService;


@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService service;

    @GetMapping
    public ResponseEntity verCategorias() {
        return ResponseEntity.ok().body(service.verCategorias());
    }
    
    @PostMapping
    public void criarCategoria(@RequestBody CategoriaInputDTO dto){
        service.criarCategoriaSeNaoExiste(dto.nome());
    }

    @DeleteMapping("{id}")
    public void deletarCategoria(@PathVariable Long id){
        service.deletarCategoria(id);
    }

}
