package com.gamedepot.admin.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "movimiento_inventario")
public class MovimientoInventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La variante de videojuego es obligatoria")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variante_id", nullable = false)
    private VarianteVideojuego variante;

    @NotNull(message = "La cantidad ajustada es obligatoria")
    @Column(name = "cantidad_ajustada", nullable = false)
    private Integer cantidadAjustada;

    @NotNull(message = "El motivo es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipoMovimientoInventario motivo;

    @Column(length = 255)
    private String descripcion;

    @Column(nullable = false)
    private LocalDateTime fecha = LocalDateTime.now();

    public MovimientoInventario() {
    }

    public MovimientoInventario(Long id, VarianteVideojuego variante, Integer cantidadAjustada,
                                TipoMovimientoInventario motivo, String descripcion, LocalDateTime fecha) {
        this.id = id;
        this.variante = variante;
        this.cantidadAjustada = cantidadAjustada;
        this.motivo = motivo;
        this.descripcion = descripcion;
        this.fecha = fecha != null ? fecha : LocalDateTime.now();
    }

    public Videojuego getVideojuego() {
        return variante != null ? variante.getVideojuego() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public VarianteVideojuego getVariante() {
        return variante;
    }

    public void setVariante(VarianteVideojuego variante) {
        this.variante = variante;
    }

    public Integer getCantidadAjustada() {
        return cantidadAjustada;
    }

    public void setCantidadAjustada(Integer cantidadAjustada) {
        this.cantidadAjustada = cantidadAjustada;
    }

    public TipoMovimientoInventario getMotivo() {
        return motivo;
    }

    public void setMotivo(TipoMovimientoInventario motivo) {
        this.motivo = motivo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "MovimientoInventario{" +
                "id=" + id +
                ", variante=" + (variante != null ? variante.getNombreCompleto() : null) +
                ", cantidadAjustada=" + cantidadAjustada +
                ", motivo=" + motivo +
                ", fecha=" + fecha +
                '}';
    }
}
