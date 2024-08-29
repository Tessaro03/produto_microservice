package com.produto.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.produto.dtos.pedido.input.ProdutoIncompletoDTO;
import com.produto.dtos.pedido.output.PedidoCompletoOutputDTO;
import com.produto.dtos.produto.ProdutoAlterarDTO;
import com.produto.dtos.produto.ProdutoInputDTO;
import com.produto.infra.security.TokenService;
import com.produto.infra.security.UsuarioDTO;
import com.produto.model.Categoria;
import com.produto.model.Produto;
import com.produto.repository.ProdutoRepository;
import com.produto.validation.ProdutoValidation;

import jakarta.servlet.http.HttpServletRequest;

public class ProdutoServiceTest {

    @InjectMocks
    private ProdutoService produtoService;

    @Mock
    private CategoriaService categoriaService;

    @Mock
    private TokenService tokenService;

    @Mock
    private ProdutoRepository repository;

    @Mock
    private ProdutoValidation validador;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCriarProduto() {
        UsuarioDTO usuarioDTO = new UsuarioDTO(1l, "teste", "teste@gmail.com", "Loja");
        List<Categoria> categorias = List.of(new Categoria("Categoria1"), new Categoria("Categoria2"));
        Produto produto = new Produto(new ProdutoInputDTO("Produto1", "teste", null, 1d, 10), categorias, 1l);

        when(tokenService.extrairInformacoes(any())).thenReturn(usuarioDTO);
        when(categoriaService.retornaListaDeCategoria(any())).thenReturn(categorias);

        produtoService.criarProduto(mock(ProdutoInputDTO.class), mock(HttpServletRequest.class));
        when(repository.save(any(Produto.class))).thenReturn(produto);
        verify(repository, times(1)).save(any(Produto.class));
    }

    @Test
    void testAlterarProduto(){
        UsuarioDTO usuarioDTO = new UsuarioDTO(1l, "teste", "teste@gmail.com", "Loja");
        var produto = new Produto();

        when(tokenService.extrairInformacoes(any())).thenReturn(usuarioDTO);
        when(repository.getReferenceById(any())).thenReturn(produto);

        produtoService.alterarProduto(mock(ProdutoAlterarDTO.class),1l, mock(HttpServletRequest.class) );
        verify(repository, times(1)).save(any(Produto.class));
    }


    @Test
    void testAumentaEstoque(){
        Produto produto = new Produto();
        produto.setQuantidade(10);
        int quantidadeAdicional = 10;

        when(repository.save(any(Produto.class))).thenReturn(produto);
        produtoService.aumentaEstoque(produto, quantidadeAdicional);

        assertEquals(20, produto.getQuantidade());
        verify(repository, times(1)).save(any(Produto.class));
    }

    @Test
    void testDiminuirEstoque(){
        Produto produto = new Produto();
        produto.setQuantidade(10);
        int quantidadeReduzida = 10;

        when(repository.save(any(Produto.class))).thenReturn(produto);
        produtoService.diminuirEstoque(produto, quantidadeReduzida);

        assertEquals(0, produto.getQuantidade());
        verify(repository, times(1)).save(any(Produto.class));
    }

    @Test
    @DisplayName("Enviando separação de produtos para 3 lojas diferentes")
    void testRetornaListaDeProdutoCase1() {
        
        Produto produto1 = new Produto(1l, 2l, "produto1", null, 10d, 100, null, null);
        Produto produto2 = new Produto(2l, 2l, "produto1", null, 10d, 100, null, null);
        Produto produto3 = new Produto(3l, 1l, "produto1", null, 10d, 100, null, null);
        Produto produto4 = new Produto(4l, 4l, "produto1", null, 10d, 100, null, null);
        
        ProdutoIncompletoDTO dto1 = new ProdutoIncompletoDTO(1l, 1, null);
        ProdutoIncompletoDTO dto2 = new ProdutoIncompletoDTO(2l, 1, null);
        ProdutoIncompletoDTO dto3 = new ProdutoIncompletoDTO(3l, 1, null);
        ProdutoIncompletoDTO dto4 = new ProdutoIncompletoDTO(4l, 1, null);
        
        Map<Produto, ProdutoIncompletoDTO> produtos = new HashMap<>();
        produtos.put(produto1, dto1);
        produtos.put(produto2, dto2);
        produtos.put(produto3, dto3);
        produtos.put(produto4, dto4);
        
        produtoService.retornaListaDeProduto(1l, produtos);
        verify(rabbitTemplate, times(3)).convertAndSend(eq("produto.separado"), any(PedidoCompletoOutputDTO.class));
    }

    @Test
    @DisplayName("Enviando separação de produtos para 2 lojas diferentes")
    void testRetornaListaDeProdutoCase2() {
        
        Produto produto1 = new Produto(1l, 2l, "produto1", null, 10d, 100, null, null);
        Produto produto2 = new Produto(2l, 2l, "produto1", null, 10d, 100, null, null);
        Produto produto3 = new Produto(3l, 1l, "produto1", null, 10d, 100, null, null);
        Produto produto4 = new Produto(4l, 1l, "produto1", null, 10d, 100, null, null);
        
        ProdutoIncompletoDTO dto1 = new ProdutoIncompletoDTO(1l, 1, null);
        ProdutoIncompletoDTO dto2 = new ProdutoIncompletoDTO(2l, 1, null);
        ProdutoIncompletoDTO dto3 = new ProdutoIncompletoDTO(3l, 1, null);
        ProdutoIncompletoDTO dto4 = new ProdutoIncompletoDTO(4l, 1, null);
        
        Map<Produto, ProdutoIncompletoDTO> produtos = new HashMap<>();
        produtos.put(produto1, dto1);
        produtos.put(produto2, dto2);
        produtos.put(produto3, dto3);
        produtos.put(produto4, dto4);
        
        produtoService.retornaListaDeProduto(1l, produtos);
        verify(rabbitTemplate, times(2)).convertAndSend(eq("produto.separado"), any(PedidoCompletoOutputDTO.class));
    }

    @Test
    @DisplayName("Enviando separação de produtos para uma unica loja")
    void testRetornaListaDeProdutoCase3() {
        
        Produto produto1 = new Produto(1l, 1l, "produto1", null, 10d, 100, null, null);
        Produto produto2 = new Produto(2l, 1l, "produto1", null, 10d, 100, null, null);
        Produto produto3 = new Produto(3l, 1l, "produto1", null, 10d, 100, null, null);
        Produto produto4 = new Produto(4l, 1l, "produto1", null, 10d, 100, null, null);
        
        ProdutoIncompletoDTO dto1 = new ProdutoIncompletoDTO(1l, 1, null);
        ProdutoIncompletoDTO dto2 = new ProdutoIncompletoDTO(2l, 1, null);
        ProdutoIncompletoDTO dto3 = new ProdutoIncompletoDTO(3l, 1, null);
        ProdutoIncompletoDTO dto4 = new ProdutoIncompletoDTO(4l, 1, null);
        
        Map<Produto, ProdutoIncompletoDTO> produtos = new HashMap<>();
        produtos.put(produto1, dto1);
        produtos.put(produto2, dto2);
        produtos.put(produto3, dto3);
        produtos.put(produto4, dto4);
        
        produtoService.retornaListaDeProduto(1l, produtos);
        verify(rabbitTemplate, times(1)).convertAndSend(eq("produto.separado"), any(PedidoCompletoOutputDTO.class));
    }

}
