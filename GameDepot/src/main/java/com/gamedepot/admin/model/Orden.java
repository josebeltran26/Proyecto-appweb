package com.gamedepot.admin.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ordenes")
public class Orden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false, unique = true, length = 50)
    private String folio;

    @Column(nullable = false)
    private LocalDateTime fecha = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoOrden estado = EstadoOrden.PENDIENTE;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "direccion_id")
    private Direccion direccionEntrega;

    @OneToOne(mappedBy = "orden", cascade = CascadeType.ALL, orphanRemoval = true)
    private Envio envio;

    @OneToMany(mappedBy = "orden", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemOrden> items = new ArrayList<>();

    @OneToOne(mappedBy = "orden", cascade = CascadeType.ALL, orphanRemoval = true)
    private Pago pago;

    public Orden() {
    }

    public Orden(Long id, Usuario usuario, String folio, LocalDateTime fecha, EstadoOrden estado,
                 Direccion direccionEntrega, List<ItemOrden> items) {
        this.id = id;
        this.usuario = usuario;
        this.folio = folio;
        this.fecha = fecha != null ? fecha : LocalDateTime.now();
        this.estado = estado != null ? estado : EstadoOrden.PENDIENTE;
        this.direccionEntrega = direccionEntrega;
        if (items != null) {
            this.items = items;
            for (ItemOrden item : items) {
                item.setOrden(this);
            }
        }
        this.total = calcularTotal();
    }

    public BigDecimal calcularTotal() {
        if (items == null || items.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return items.stream()
                .map(ItemOrden::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public boolean requiereEnvio() {
        if (items == null) return false;
        return items.stream().anyMatch(i -> i.getVariante() != null && i.getVariante().isFisico());
    }

    public void addItem(ItemOrden item) {
        if (item != null) {
            this.items.add(item);
            item.setOrden(this);
            this.total = calcularTotal();
        }
    }

    public void removeItem(ItemOrden item) {
        if (item != null) {
            this.items.remove(item);
            item.setOrden(null);
            this.total = calcularTotal();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public EstadoOrden getEstado() {
        return estado;
    }

    public void setEstado(EstadoOrden estado) {
        this.estado = estado;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Direccion getDireccionEntrega() {
        return direccionEntrega;
    }

    public void setDireccionEntrega(Direccion direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }

    public Envio getEnvio() {
        return envio;
    }

    public void setEnvio(Envio envio) {
        this.envio = envio;
        if (envio != null) {
            envio.setOrden(this);
        }
    }

    public List<ItemOrden> getItems() {
        return items;
    }

    public void setItems(List<ItemOrden> items) {
        this.items = items;
        this.total = calcularTotal();
    }

    public Pago getPago() {
        return pago;
    }

    public void setPago(Pago pago) {
        this.pago = pago;
        if (pago != null) {
            pago.setOrden(this);
        }
    }

    @Override
    public String toString() {
        return "Orden{" +
                "id=" + id +
                ", folio='" + folio + '\'' +
                ", estado=" + estado +
                ", total=" + total +
                '}';
    }
}
