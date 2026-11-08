package com.gamedepot.admin.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "videojuegos")
public class Videojuego {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(length = 100)
    private String desarrollador;

    @NotNull(message = "La clasificación de edad es obligatoria")
    @Enumerated(EnumType.STRING)
    @Column(name = "clasificacion_edad", nullable = false, length = 30)
    private ClasificacionEdad clasificacionEdad = ClasificacionEdad.E;

    @Column(nullable = false)
    private Boolean activo = true;

    @ManyToMany
    @JoinTable(
        name = "videojuego_categoria",
        joinColumns = @JoinColumn(name = "videojuego_id"),
        inverseJoinColumns = @JoinColumn(name = "categoria_id")
    )
    private List<Categoria> categorias = new ArrayList<>();

    @OneToMany(mappedBy = "videojuego", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImagenVideojuego> imagenes = new ArrayList<>();

    @OneToMany(mappedBy = "videojuego", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VarianteVideojuego> variantes = new ArrayList<>();

    public Videojuego() {
    }

    public Videojuego(Long id, String nombre, String descripcion, String desarrollador,
                      ClasificacionEdad clasificacionEdad, Boolean activo,
                      List<Categoria> categorias, List<ImagenVideojuego> imagenes) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.desarrollador = desarrollador;
        this.clasificacionEdad = clasificacionEdad != null ? clasificacionEdad : ClasificacionEdad.E;
        this.activo = activo != null ? activo : true;
        if (categorias != null) {
            this.categorias = categorias;
        }
        if (imagenes != null) {
            this.imagenes = imagenes;
            for (ImagenVideojuego img : imagenes) {
                img.setVideojuego(this);
            }
        }
    }

    public void addCategoria(Categoria categoria) {
        if (categoria != null && !this.categorias.contains(categoria)) {
            this.categorias.add(categoria);
        }
    }

    public void removeCategoria(Categoria categoria) {
        if (categoria != null) {
            this.categorias.remove(categoria);
        }
    }

    public void addImagen(ImagenVideojuego imagen) {
        if (imagen != null) {
            this.imagenes.add(imagen);
            imagen.setVideojuego(this);
        }
    }

    public void removeImagen(ImagenVideojuego imagen) {
        if (imagen != null) {
            this.imagenes.remove(imagen);
            imagen.setVideojuego(null);
        }
    }

    public void addVariante(VarianteVideojuego variante) {
        if (variante != null) {
            this.variantes.add(variante);
            variante.setVideojuego(this);
        }
    }

    public String getImagenPrincipalUrl() {
        if (imagenes != null && !imagenes.isEmpty()) {
            return imagenes.get(0).getUrl();
        }
        return "https://images.unsplash.com/photo-1550745165-9bc0b252726f?w=600&auto=format&fit=crop&q=80";
    }

    public BigDecimal getPrecioMinimo() {
        if (variantes == null || variantes.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return variantes.stream()
                .filter(v -> Boolean.TRUE.equals(v.getActivo()) && v.getPrecio() != null)
                .map(VarianteVideojuego::getPrecio)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
    }

    public int getStockTotal() {
        if (variantes == null || variantes.isEmpty()) {
            return 0;
        }
        return variantes.stream()
                .mapToInt(v -> v.getStock() != null ? v.getStock() : 0)
                .sum();
    }

    public boolean isStockBajo() {
        if (variantes == null || variantes.isEmpty()) {
            return false;
        }
        return variantes.stream().anyMatch(VarianteVideojuego::isStockBajo);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDesarrollador() {
        return desarrollador;
    }

    public void setDesarrollador(String desarrollador) {
        this.desarrollador = desarrollador;
    }

    public ClasificacionEdad getClasificacionEdad() {
        return clasificacionEdad;
    }

    public void setClasificacionEdad(ClasificacionEdad clasificacionEdad) {
        this.clasificacionEdad = clasificacionEdad;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public List<Categoria> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
    }

    public List<ImagenVideojuego> getImagenes() {
        return imagenes;
    }

    public void setImagenes(List<ImagenVideojuego> imagenes) {
        this.imagenes = imagenes;
    }

    public List<VarianteVideojuego> getVariantes() {
        return variantes;
    }

    public void setVariantes(List<VarianteVideojuego> variantes) {
        this.variantes = variantes;
    }

    @Override
    public String toString() {
        return "Videojuego{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", clasificacionEdad=" + clasificacionEdad +
                ", activo=" + activo +
                '}';
    }
}
