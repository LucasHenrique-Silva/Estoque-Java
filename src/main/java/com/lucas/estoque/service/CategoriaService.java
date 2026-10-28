package com.lucas.estoque.service;


import com.lucas.estoque.DTO.CategoriaRequest;
import com.lucas.estoque.DTO.CategoriaResponse;
import com.lucas.estoque.entity.Categoria;
import com.lucas.estoque.repository.CategoriaRepository;
import com.lucas.estoque.shared.exception.BusinessException;
import com.lucas.estoque.shared.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoriaService {

    private final CategoriaRepository repository;

    public CategoriaService(CategoriaRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public CategoriaResponse criar(CategoriaRequest req) {
        if (repository.existsByNomeIgnoreCase(req.nome())) {
            throw new BusinessException("Ja existe uma categoria com esse nome");
        }
        Categoria salva = repository.save(new Categoria(req.nome(), req.descricao()));
        return toResponse(salva);

    }

    @Transactional(readOnly = true)
    public Page<CategoriaResponse> listar(Pageable pageable) {
        return repository.findAll(pageable).map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public CategoriaResponse buscar(Long id){
        return toResponse(buscarEntidade(id));
    }

    @Transactional
    public CategoriaResponse atualizar(Long id, CategoriaRequest req) {
        Categoria categoria = buscarEntidade(id);
        categoria.setNome(req.nome());
        categoria.setDescricao(req.descricao());
        return toResponse(categoria);
    }

    private Categoria buscarEntidade(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));
    }

    private CategoriaResponse toResponse(Categoria c) {
        return new CategoriaResponse(c.getId(), c.getNome(), c.getDescricao());
    }
}

