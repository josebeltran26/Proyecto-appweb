package com.gamedepot.admin.model;

public enum FormatoVideojuego {
    FISICO("Disco / Cartucho Físico"),
    DIGITAL("Clave Digital / Código");

    private final String etiqueta;

    FormatoVideojuego(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }
}
