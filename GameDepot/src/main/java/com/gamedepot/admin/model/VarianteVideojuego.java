package com.gamedepot.admin.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "variantes_videojuego")
public class VarianteVideojuego {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "videojuego_id", nullable = false)
    private Videojuego videojuego;

    @NotNull(message = "La plataforma es obligatoria")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Plataforma plataforma;

    @NotNull(message = "El formato es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private FormatoVideojuego formato;

    @NotNull(message = "El precio es obligatorio")
    @PositiveOrZero(message = "El precio debe ser mayor o igual a 0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    @Column(nullable = false)
    private Integer stock = 0;

    @NotNull(message = "El stock mínimo es obligatorio")
    @Min(value = 0, message = "El stock mínimo no puede ser negativo")
    @Column(name = "stock_minimo", nullable = false)
    private Integer stockMinimo = 5;

    @Column(unique = true, length = 60)
    private String sku;

    @Column(nullable = false)
    private Boolean activo = true;

    @OneToMany(mappedBy = "variante", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<KeyLicencia> keys = new ArrayList<>();

    public VarianteVideojuego() {
    }

    public VarianteVideojuego(Long id, Videojuego videojuego, Plataforma plataforma, FormatoVideojuego formato,
                              BigDecimal precio, Integer stock, Integer stockMinimo, String sku, Boolean activo) {
        this.id = id;
        this.videojuego = videojuego;
        this.plataforma = plataforma;
        this.formato = formato;
        this.precio = precio;
        this.stock = stock != null ? stock : 0;
        this.stockMinimo = stockMinimo != null ? stockMinimo : 5;
        this.sku = sku;
        this.activo = activo != null ? activo : true;
    }

    public boolean isDigital() {
        return formato == FormatoVideojuego.DIGITAL;
    }

    public boolean isFisico() {
        return formato == FormatoVideojuego.FISICO;
    }

    public boolean isStockBajo() {
        return stock != null && stockMinimo != null && stock <= stockMinimo;
    }

    public String getNombreCompleto() {
        String juegoNombre = videojuego != null ? videojuego.getNombre() : "Juego";
        String plat = plataforma != null ? plataforma.getEtiqueta() : "";
        String form = formato != null ? formato.name() : "";
        return juegoNombre + " [" + plat + " - " + form + "]";
    }

    public long getKeysDisponiblesCount() {
        if (keys == null) return 0;
        return keys.stream().filter(k -> !Boolean.TRUE.equals(k.getAsignada())).count();
    }

    public void addKey(KeyLicencia key) {
        if (key != null) {
            this.keys.add(key);
            key.setVariante(this);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Videojuego getVideojuego() {
        return videojuego;
    }

    public void setVideojuego(Videojuego videojuego) {
        this.videojuego = videojuego;
    }

    public Plataforma getPlataforma() {
        return plataforma;
    }

    public void setPlataforma(Plataforma plataforma) {
        this.plataforma = plataforma;
    }

    public FormatoVideojuego getFormato() {
        return formato;
    }

    public void setFormato(FormatoVideojuego formato) {
        this.formato = formato;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(Integer stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public List<KeyLicencia> getKeys() {
        return keys;
    }

    public void setKeys(List<KeyLicencia> keys) {
        this.keys = keys;
    }

    @Override
    public String toString() {
        return getNombreCompleto() + " SKU: " + sku;
    }
}
