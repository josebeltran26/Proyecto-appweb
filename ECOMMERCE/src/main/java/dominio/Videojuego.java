package dominio;

import java.util.ArrayList;
import java.util.List;

public class Videojuego {
    private Long id;
    private String nombre;
    private String descripcion;
    private Double precio;
    private Integer stock;
    private Integer stockMinimo;
    private Boolean activo;
    private String plataforma;
    private String desarrollador;
    private String clasificacionEdad;
    private List<Categoria> categorias = new ArrayList<>();
    private List<ImagenVideojuego> imagenes = new ArrayList<>();

    public Videojuego() {
    }

    public Videojuego(Long id, String nombre, String descripcion, Double precio, Integer stock, Integer stockMinimo, Boolean activo, String plataforma, String desarrollador, String clasificacionEdad, List<Categoria> categorias, List<ImagenVideojuego> imagenes) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.stockMinimo = stockMinimo;
        this.activo = activo;
        this.plataforma = plataforma;
        this.desarrollador = desarrollador;
        this.clasificacionEdad = clasificacionEdad;
        if (categorias != null) {
            this.categorias = categorias;
        }
        if (imagenes != null) {
            this.imagenes = imagenes;
        }
    }

    public void addCategoria(Categoria categoria) {
        if (categoria != null) {
            this.categorias.add(categoria);
        }
    }

    public void addImagen(ImagenVideojuego imagen) {
        if (imagen != null) {
            this.imagenes.add(imagen);
            imagen.setVideojuego(this);
        }
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

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(Integer stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getPlataforma() {
        return plataforma;
    }

    public void setPlataforma(String plataforma) {
        this.plataforma = plataforma;
    }

    public String getDesarrollador() {
        return desarrollador;
    }

    public void setDesarrollador(String desarrollador) {
        this.desarrollador = desarrollador;
    }

    public String getClasificacionEdad() {
        return clasificacionEdad;
    }

    public void setClasificacionEdad(String clasificacionEdad) {
        this.clasificacionEdad = clasificacionEdad;
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

    @Override
    public String toString() {
        return "Videojuego{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", precio=" + precio +
                ", stock=" + stock +
                ", stockMinimo=" + stockMinimo +
                ", activo=" + activo +
                ", plataforma='" + plataforma + '\'' +
                ", desarrollador='" + desarrollador + '\'' +
                ", clasificacionEdad='" + clasificacionEdad + '\'' +
                ", categorias=" + categorias +
                ", imagenes=" + imagenes +
                '}';
    }
}

