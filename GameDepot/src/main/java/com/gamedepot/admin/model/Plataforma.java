package com.gamedepot.admin.model;

public enum Plataforma {
    PC("PC / Steam / GOG"),
    PLAYSTATION_5("PlayStation 5"),
    PLAYSTATION_4("PlayStation 4"),
    XBOX_SERIES("Xbox Series X|S"),
    XBOX_ONE("Xbox One"),
    NINTENDO_SWITCH("Nintendo Switch");

    private final String etiqueta;

    Plataforma(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }
}
