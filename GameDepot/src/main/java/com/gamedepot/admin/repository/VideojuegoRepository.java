package com.gamedepot.admin.repository;

import com.gamedepot.admin.model.Categoria;
import com.gamedepot.admin.model.Videojuego;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideojuegoRepository extends JpaRepository<Videojuego, Long> {

    @Query("SELECT DISTINCT v FROM Videojuego v LEFT JOIN FETCH v.variantes ORDER BY v.nombre ASC")
    List<Videojuego> findAllWithVariantes();

    List<Videojuego> findAllByOrderByNombreAsc();

    long countByCategoriasContaining(Categoria categoria);

    List<Videojuego> findByCategoriasContaining(Categoria categoria);

    long countByActivoTrue();
}
