package dominio;

public class ImagenVideojuego {
    private Long id;
    private String url;
    private Videojuego videojuego;

    public ImagenVideojuego() {
    }

    public ImagenVideojuego(Long id, String url, Videojuego videojuego) {
        this.id = id;
        this.url = url;
        this.videojuego = videojuego;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Videojuego getVideojuego() {
        return videojuego;
    }

    public void setVideojuego(Videojuego videojuego) {
        this.videojuego = videojuego;
    }

    @Override
    public String toString() {
        return "ImagenVideojuego{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", videojuegoId=" + (videojuego != null ? videojuego.getId() : null) +
                '}';
    }
}

