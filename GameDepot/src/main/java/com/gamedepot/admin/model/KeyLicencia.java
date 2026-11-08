package com.gamedepot.admin.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "keys_licencia")
public class KeyLicencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variante_id", nullable = false)
    private VarianteVideojuego variante;

    @NotBlank(message = "El código de licencia es obligatorio")
    @Column(nullable = false, unique = true, length = 100)
    private String codigo;

    @Column(nullable = false)
    private Boolean asignada = false;

    @Column(name = "fecha_asignacion")
    private LocalDateTime fechaAsignacion;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_orden_id")
    private ItemOrden itemOrden;

    public KeyLicencia() {
    }

    public KeyLicencia(Long id, VarianteVideojuego variante, String codigo, Boolean asignada, LocalDateTime fechaAsignacion) {
        this.id = id;
        this.variante = variante;
        this.codigo = codigo;
        this.asignada = asignada != null ? asignada : false;
        this.fechaAsignacion = fechaAsignacion;
    }

    public KeyLicencia(VarianteVideojuego variante, String codigo) {
        this.variante = variante;
        this.codigo = codigo;
        this.asignada = false;
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

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Boolean getAsignada() {
        return asignada;
    }

    public void setAsignada(Boolean asignada) {
        this.asignada = asignada;
    }

    public LocalDateTime getFechaAsignacion() {
        return fechaAsignacion;
    }

    public void setFechaAsignacion(LocalDateTime fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }

    public ItemOrden getItemOrden() {
        return itemOrden;
    }

    public void setItemOrden(ItemOrden itemOrden) {
        this.itemOrden = itemOrden;
    }

    @Override
    public String toString() {
        return "KeyLicencia{" +
                "id=" + id +
                ", codigo='" + codigo + '\'' +
                ", asignada=" + asignada +
                '}';
    }
}
