package com.lucas.estoque.controller;

import com.lucas.estoque.DTO.CategoriaRequest;
import com.lucas.estoque.DTO.CategoriaResponse;
import com.lucas.estoque.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaService service;

    public CategoriaController(CategoriaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CategoriaResponse> criar(@Valid @RequestBody CategoriaRequest req) {
        CategoriaResponse criada = service.criar(req);
        URI location = URI.create("/categorias/" + criada.id());
        return ResponseEntity.created(location).body(criada);
    }

    @GetMapping
    public Page<CategoriaResponse> listar(Pageable pageable) {
        return service.listar(pageable);
    }

    @GetMapping("/{id}")
    public CategoriaResponse buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @PutMapping("/{id}")
    public CategoriaResponse atualizar(@PathVariable Long id, @Valid @RequestBody CategoriaRequest req) {
        return service.atualizar(id, req);
    }

}
