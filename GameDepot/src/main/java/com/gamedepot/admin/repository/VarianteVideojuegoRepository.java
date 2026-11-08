package com.gamedepot.admin.repository;

import com.gamedepot.admin.model.Plataforma;
import com.gamedepot.admin.model.VarianteVideojuego;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VarianteVideojuegoRepository extends JpaRepository<VarianteVideojuego, Long> {

    @Query("SELECT v FROM VarianteVideojuego v JOIN FETCH v.videojuego ORDER BY v.videojuego.nombre ASC, v.plataforma ASC")
    List<VarianteVideojuego> findAllWithVideojuego();

    List<VarianteVideojuego> findByVideojuegoId(Long videojuegoId);

    Optional<VarianteVideojuego> findBySku(String sku);

    boolean existsBySku(String sku);

    @Query("SELECT v FROM VarianteVideojuego v JOIN FETCH v.videojuego WHERE v.stock <= v.stockMinimo ORDER BY v.stock ASC")
    List<VarianteVideojuego> findLowStock();

    @Query("SELECT COUNT(v) FROM VarianteVideojuego v WHERE v.stock <= v.stockMinimo")
    long countLowStock();

    long countByActivoTrue();
}
