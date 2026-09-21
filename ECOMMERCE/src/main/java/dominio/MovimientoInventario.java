package dominio;

import java.time.LocalDateTime;

public class MovimientoInventario {
    private Long id;
    private Videojuego videojuego;
    private Integer cantidadAjustada;
    private TipoMovimientoInventario motivo;
    private String descripcion;
    private LocalDateTime fecha;

    public MovimientoInventario() {
    }

    public MovimientoInventario(Long id, Videojuego videojuego, Integer cantidadAjustada, TipoMovimientoInventario motivo, String descripcion, LocalDateTime fecha) {
        this.id = id;
        this.videojuego = videojuego;
        this.cantidadAjustada = cantidadAjustada;
        this.motivo = motivo;
        this.descripcion = descripcion;
        this.fecha = fecha;
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

    public Integer getCantidadAjustada() {
        return cantidadAjustada;
    }

    public void setCantidadAjustada(Integer cantidadAjustada) {
        this.cantidadAjustada = cantidadAjustada;
    }

    public TipoMovimientoInventario getMotivo() {
        return motivo;
    }

    public void setMotivo(TipoMovimientoInventario motivo) {
        this.motivo = motivo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "MovimientoInventario{" +
                "id=" + id +
                ", videojuegoId=" + (videojuego != null ? videojuego.getId() : null) +
                ", cantidadAjustada=" + cantidadAjustada +
                ", motivo=" + motivo +
                ", descripcion='" + descripcion + '\'' +
                ", fecha=" + fecha +
                '}';
    }
}

