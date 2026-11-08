package com.gamedepot.admin.service;

import com.gamedepot.admin.model.Categoria;
import com.gamedepot.admin.repository.CategoriaRepository;
import com.gamedepot.admin.repository.VideojuegoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final VideojuegoRepository videojuegoRepository;

    public CategoriaService(CategoriaRepository categoriaRepository, VideojuegoRepository videojuegoRepository) {
        this.categoriaRepository = categoriaRepository;
        this.videojuegoRepository = videojuegoRepository;
    }

    @Transactional(readOnly = true)
    public List<Categoria> listarTodas() {
        return categoriaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Categoria buscarPorId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada con id: " + id));
    }

    public Categoria guardar(Categoria categoria) {
        if (categoria.getId() == null) {
            if (categoriaRepository.existsByNombreIgnoreCase(categoria.getNombre())) {
                throw new IllegalArgumentException("Ya existe una categoría con el nombre: " + categoria.getNombre());
            }
        } else {
            if (categoriaRepository.existsByNombreIgnoreCaseAndIdNot(categoria.getNombre(), categoria.getId())) {
                throw new IllegalArgumentException("Ya existe otra categoría con el nombre: " + categoria.getNombre());
            }
        }
        return categoriaRepository.save(categoria);
    }

    public void eliminar(Long id) {
        Categoria categoria = buscarPorId(id);
        long juegosAsociados = videojuegoRepository.countByCategoriasContaining(categoria);
        if (juegosAsociados > 0) {
            throw new IllegalStateException("No se puede eliminar la categoría '" + categoria.getNombre() +
                    "' porque tiene " + juegosAsociados + " videojuego(s) asociado(s). Desvincula los videojuegos primero.");
        }
        categoriaRepository.delete(categoria);
    }
}
