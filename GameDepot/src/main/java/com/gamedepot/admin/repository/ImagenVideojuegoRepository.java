package com.gamedepot.admin.repository;

import com.gamedepot.admin.model.ImagenVideojuego;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImagenVideojuegoRepository extends JpaRepository<ImagenVideojuego, Long> {

    List<ImagenVideojuego> findByVideojuegoId(Long videojuegoId);
}
