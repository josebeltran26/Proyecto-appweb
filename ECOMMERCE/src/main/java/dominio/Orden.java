package dominio;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Orden {
    private Long id;
    private Usuario usuario;
    private String folio;
    private LocalDateTime fecha;
    private EstadoOrden estado;
    private Double total;
    private String direccionEnvio;
    private List<ItemOrden> items = new ArrayList<>();

    public Orden() {
    }

    public Orden(Long id, Usuario usuario, String folio, LocalDateTime fecha, EstadoOrden estado, Double total, String direccionEnvio, List<ItemOrden> items) {
        this.id = id;
        this.usuario = usuario;
        this.folio = folio;
        this.fecha = fecha;
        this.estado = estado;
        this.total = total;
        this.direccionEnvio = direccionEnvio;
        if (items != null) {
            this.items = items;
        }
    }

    public void addItem(ItemOrden item) {
        if (item != null) {
            this.items.add(item);
            item.setOrden(this);
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

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getDireccionEnvio() {
        return direccionEnvio;
    }

    public void setDireccionEnvio(String direccionEnvio) {
        this.direccionEnvio = direccionEnvio;
    }

    public List<ItemOrden> getItems() {
        return items;
    }

    public void setItems(List<ItemOrden> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "Orden{" +
                "id=" + id +
                ", usuario=" + (usuario != null ? usuario.getId() : null) +
                ", folio='" + folio + '\'' +
                ", fecha=" + fecha +
                ", estado=" + estado +
                ", total=" + total +
                ", direccionEnvio='" + direccionEnvio + '\'' +
                ", items=" + items +
                '}';
    }
}

