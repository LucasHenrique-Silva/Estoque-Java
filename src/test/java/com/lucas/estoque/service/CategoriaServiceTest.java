package com.lucas.estoque.service;

import com.lucas.estoque.dto.CategoriaRequest;
import com.lucas.estoque.dto.CategoriaResponse;
import com.lucas.estoque.entity.Categoria;
import com.lucas.estoque.repository.CategoriaRepository;

import com.lucas.estoque.shared.exception.BusinessException;
import com.lucas.estoque.shared.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tools.jackson.databind.ObjectMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CategoriaServiceTest {

    @Mock
    private CategoriaRepository repository;

    @InjectMocks
    private CategoriaService service;

    @Test
    void deveCriarCategoriaQuandoNaoExiste() throws Exception {
        String json = """
                {
                  "nome": "Celular",
                  "descricao": "Categoria de celulares"
                }
                """;
        CategoriaRequest request = new ObjectMapper().readValue(json, CategoriaRequest.class);

        when(repository.existsByNomeIgnoreCase(request.nome())).thenReturn(false);
        when(repository.save(any(Categoria.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CategoriaResponse criada = service.criar(request);

        assertEquals("Celular", criada.nome());
        assertEquals("Categoria de celulares", criada.descricao());
        verify(repository).save(any(Categoria.class));
    }

    @Test
    void naoDeveCriarCasoExista() throws Exception {
        String json = """
                {
                  "nome": "Celular",
                  "descricao": "Categoria de celulares"
                }
                """;
        CategoriaRequest request = new ObjectMapper().readValue(json, CategoriaRequest.class);

        when(repository.existsByNomeIgnoreCase(request.nome())).thenReturn(true);

        assertThrows(BusinessException.class, () -> service.criar(request));
        verify(repository).existsByNomeIgnoreCase(request.nome());
    }

    @Test
    void  naoDeveEncontraPorId(){
      when(repository.findById(1L)).thenReturn(Optional.empty());
      assertThrows(ResourceNotFoundException.class, ()->service.buscar(1L));

    }

    @Test
    void deveAtualizarComSucesso(){
        String json = """
                {  
                  "nome": "Celular",
                  "descricao": "Categoria de celulares"
                }
                """;
        CategoriaRequest request = new ObjectMapper().readValue(json, CategoriaRequest.class);
        when(repository.findById(1L)).thenReturn(Optional.of(new Categoria("Antigo", "Descricao antiga")));
        CategoriaResponse response = service.atualizar(1L, request);
        assertEquals(request.nome(), response.nome());
        assertEquals(request.descricao(), response.descricao());

        verify(repository).findById(1L);
    }

    @Test
    void naodeveAtualizarInexistente(){
        String json = """
                {  
                  "nome": "Celular",
                  "descricao": "Categoria de celulares"
                }
                """;
        CategoriaRequest request = new ObjectMapper().readValue(json, CategoriaRequest.class);
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, ()->service.atualizar(1L, request));

    }

}
