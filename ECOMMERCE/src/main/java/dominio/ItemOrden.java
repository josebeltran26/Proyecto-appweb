package dominio;

public class ItemOrden {
    private Long id;
    private Orden orden;
    private Videojuego videojuego;
    private Integer cantidad;
    private Double precioUnitario;
    private Double descuento;

    public ItemOrden() {
    }

    public ItemOrden(Long id, Orden orden, Videojuego videojuego, Integer cantidad, Double precioUnitario, Double descuento) {
        this.id = id;
        this.orden = orden;
        this.videojuego = videojuego;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.descuento = descuento;
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

    public Videojuego getVideojuego() {
        return videojuego;
    }

    public void setVideojuego(Videojuego videojuego) {
        this.videojuego = videojuego;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(Double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public Double getDescuento() {
        return descuento;
    }

    public void setDescuento(Double descuento) {
        this.descuento = descuento;
    }

    @Override
    public String toString() {
        return "ItemOrden{" +
                "id=" + id +
                ", ordenId=" + (orden != null ? orden.getId() : null) +
                ", videojuegoId=" + (videojuego != null ? videojuego.getId() : null) +
                ", cantidad=" + cantidad +
                ", precioUnitario=" + precioUnitario +
                ", descuento=" + descuento +
                '}';
    }
}

