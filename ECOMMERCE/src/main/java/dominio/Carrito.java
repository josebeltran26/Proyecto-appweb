package dominio;

import java.util.ArrayList;
import java.util.List;

public class Carrito {
    private Long id;
    private Usuario usuario;
    private List<ItemCarrito> items = new ArrayList<>();

    public Carrito() {
    }

    public Carrito(Long id, Usuario usuario, List<ItemCarrito> items) {
        this.id = id;
        this.usuario = usuario;
        if (items != null) {
            this.items = items;
        }
    }

    public void addItem(ItemCarrito item) {
        if (item != null) {
            this.items.add(item);
            item.setCarrito(this);
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
                ", items=" + items +
                '}';
    }
}

