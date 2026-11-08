package com.gamedepot.admin.model;

public enum EstadoEnvio {
    PREPARANDO("Preparando en almacén"),
    EN_TRANSITO("En tránsito con paquetería"),
    ENTREGADO("Entregado en destino"),
    DEVUELTO("Devuelto a remitente");

    private final String etiqueta;

    EstadoEnvio(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }
}
