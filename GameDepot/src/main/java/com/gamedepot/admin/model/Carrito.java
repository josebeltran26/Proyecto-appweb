package com.gamedepot.admin.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carritos")
public class Carrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "usuario_id", unique = true, nullable = false)
    private Usuario usuario;

    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemCarrito> items = new ArrayList<>();

    public Carrito() {
    }

    public Carrito(Long id, Usuario usuario, List<ItemCarrito> items) {
        this.id = id;
        this.usuario = usuario;
        if (items != null) {
            this.items = items;
            for (ItemCarrito item : items) {
                item.setCarrito(this);
            }
        }
    }

    public void addItem(ItemCarrito item) {
        if (item != null) {
            this.items.add(item);
            item.setCarrito(this);
        }
    }

    public void removeItem(ItemCarrito item) {
        if (item != null) {
            this.items.remove(item);
            item.setCarrito(null);
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

    public List<ItemCarrito> getItems() {
        return items;
    }

    public void setItems(List<ItemCarrito> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "Carrito{" +
                "id=" + id +
                ", usuario=" + (usuario != null ? usuario.getId() : null) +
                '}';
    }
}
