package com.gamedepot.admin.repository;

import com.gamedepot.admin.model.EstadoOrden;
import com.gamedepot.admin.model.Orden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrdenRepository extends JpaRepository<Orden, Long> {

    @Query("SELECT o FROM Orden o JOIN FETCH o.usuario ORDER BY o.fecha DESC")
    List<Orden> findAllWithUsuario();

    List<Orden> findByEstadoOrderByFechaDesc(EstadoOrden estado);

    long countByEstado(EstadoOrden estado);

    List<Orden> findTop5ByOrderByFechaDesc();

    @Query("SELECT COALESCE(SUM(o.total), 0) FROM Orden o WHERE o.estado NOT IN (com.gamedepot.admin.model.EstadoOrden.PENDIENTE, com.gamedepot.admin.model.EstadoOrden.CANCELADO)")
    BigDecimal sumTotalProcessed();

    @Query("SELECT o FROM Orden o " +
           "LEFT JOIN FETCH o.items i " +
           "LEFT JOIN FETCH i.variante v " +
           "LEFT JOIN FETCH v.videojuego " +
           "LEFT JOIN FETCH i.keyAsignada " +
           "LEFT JOIN FETCH o.pago " +
           "LEFT JOIN FETCH o.envio e " +
           "LEFT JOIN FETCH o.direccionEntrega " +
           "WHERE o.id = :id")
    Optional<Orden> findByIdWithDetails(Long id);
}
