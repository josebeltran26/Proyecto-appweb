package com.gamedepot.admin.model;

public enum ClasificacionEdad {
    E("Everyone (Para todos)"),
    E10("Everyone 10+ (Mayores de 10 años)"),
    T("Teen (Adolescentes 13+)"),
    M("Mature 17+ (Maduro 17+)"),
    AO("Adults Only 18+ (Solo Adultos 18+)"),
    RP("Rating Pending (Pendiente de Clasificación)");

    private final String descripcion;

    ClasificacionEdad(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
