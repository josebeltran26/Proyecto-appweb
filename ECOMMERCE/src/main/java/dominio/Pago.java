package dominio;

import java.time.LocalDateTime;

public class Pago {
    private Long id;
    private Orden orden;
    private Double monto;
    private MetodoPago metodoPago;
    private EstadoPago estadoPago;
    private LocalDateTime fecha;

    public Pago() {
    }

    public Pago(Long id, Orden orden, Double monto, MetodoPago metodoPago, EstadoPago estadoPago, LocalDateTime fecha) {
        this.id = id;
        this.orden = orden;
        this.monto = monto;
        this.metodoPago = metodoPago;
        this.estadoPago = estadoPago;
        this.fecha = fecha;
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

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public MetodoPago getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(MetodoPago metodoPago) {
        this.metodoPago = metodoPago;
    }

    public EstadoPago getEstadoPago() {
        return estadoPago;
    }

    public void setEstadoPago(EstadoPago estadoPago) {
        this.estadoPago = estadoPago;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "Pago{" +
                "id=" + id +
                ", ordenId=" + (orden != null ? orden.getId() : null) +
                ", monto=" + monto +
                ", metodoPago=" + metodoPago +
                ", estadoPago=" + estadoPago +
                ", fecha=" + fecha +
                '}';
    }
}

