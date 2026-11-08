package com.gamedepot.admin.repository;

import com.gamedepot.admin.model.MovimientoInventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovimientoInventarioRepository extends JpaRepository<MovimientoInventario, Long> {

    @Query("SELECT m FROM MovimientoInventario m JOIN FETCH m.variante v JOIN FETCH v.videojuego ORDER BY m.fecha DESC")
    List<MovimientoInventario> findAllWithVariante();

    List<MovimientoInventario> findByVarianteIdOrderByFechaDesc(Long varianteId);
}
