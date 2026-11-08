package com.gamedepot.admin.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "item_orden")
public class ItemOrden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orden_id", nullable = false)
    private Orden orden;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variante_id", nullable = false)
    private VarianteVideojuego variante;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(name = "precio_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal descuento = BigDecimal.ZERO;

    @OneToOne(mappedBy = "itemOrden")
    private KeyLicencia keyAsignada;

    public ItemOrden() {
    }

    public ItemOrden(Long id, Orden orden, VarianteVideojuego variante, Integer cantidad, BigDecimal precioUnitario, BigDecimal descuento) {
        this.id = id;
        this.orden = orden;
        this.variante = variante;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.descuento = descuento != null ? descuento : BigDecimal.ZERO;
    }

    public BigDecimal getSubtotal() {
        BigDecimal unit = precioUnitario != null ? precioUnitario : BigDecimal.ZERO;
        int qty = cantidad != null ? cantidad : 0;
        BigDecimal desc = descuento != null ? descuento : BigDecimal.ZERO;
        return unit.multiply(BigDecimal.valueOf(qty)).subtract(desc);
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

    public Orden getOrden() {
        return orden;
    }

    public void setOrden(Orden orden) {
        this.orden = orden;
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

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public KeyLicencia getKeyAsignada() {
        return keyAsignada;
    }

    public void setKeyAsignada(KeyLicencia keyAsignada) {
        this.keyAsignada = keyAsignada;
    }

    @Override
    public String toString() {
        return "ItemOrden{" +
                "id=" + id +
                ", variante=" + (variante != null ? variante.getNombreCompleto() : null) +
                ", cantidad=" + cantidad +
                ", precioUnitario=" + precioUnitario +
                ", subtotal=" + getSubtotal() +
                '}';
    }
}
