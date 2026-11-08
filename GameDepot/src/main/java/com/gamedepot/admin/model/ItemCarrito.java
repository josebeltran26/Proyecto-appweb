package com.gamedepot.admin.model;

import jakarta.persistence.*;

@Entity
@Table(name = "item_carrito")
public class ItemCarrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carrito_id", nullable = false)
    private Carrito carrito;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variante_id", nullable = false)
    private VarianteVideojuego variante;

    @Column(nullable = false)
    private Integer cantidad;

    public ItemCarrito() {
    }

    public ItemCarrito(Long id, Carrito carrito, VarianteVideojuego variante, Integer cantidad) {
        this.id = id;
        this.carrito = carrito;
        this.variante = variante;
        this.cantidad = cantidad;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Carrito getCarrito() {
        return carrito;
    }

    public void setCarrito(Carrito carrito) {
        this.carrito = carrito;
    }

    public VarianteVideojuego getVariante() {
        return variante;
    }

    public void setVariante(VarianteVideojuego variante) {
        this.variante = variante;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public String toString() {
        return "ItemCarrito{" +
                "id=" + id +
                ", variante=" + (variante != null ? variante.getNombreCompleto() : null) +
                ", cantidad=" + cantidad +
                '}';
    }
}
