package dominio;

public class ItemCarrito {
    private Long id;
    private Carrito carrito;
    private Videojuego videojuego;
    private Integer cantidad;

    public ItemCarrito() {
    }

    public ItemCarrito(Long id, Carrito carrito, Videojuego videojuego, Integer cantidad) {
        this.id = id;
        this.carrito = carrito;
        this.videojuego = videojuego;
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

    @Override
    public String toString() {
        return "ItemCarrito{" +
                "id=" + id +
                ", carritoId=" + (carrito != null ? carrito.getId() : null) +
                ", videojuegoId=" + (videojuego != null ? videojuego.getId() : null) +
                ", cantidad=" + cantidad +
                '}';
    }
}

