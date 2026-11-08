package com.gamedepot.admin.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "direcciones")
public class Direccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @NotBlank(message = "La calle es obligatoria")
    @Column(nullable = false, length = 150)
    private String calle;

    @NotBlank(message = "El número exterior es obligatorio")
    @Column(name = "numero_exterior", nullable = false, length = 20)
    private String numeroExterior;

    @Column(name = "numero_interior", length = 20)
    private String numeroInterior;

    @Column(length = 100)
    private String colonia;

    @NotBlank(message = "La ciudad es obligatoria")
    @Column(nullable = false, length = 80)
    private String ciudad;

    @NotBlank(message = "El estado es obligatorio")
    @Column(nullable = false, length = 80)
    private String estado;

    @NotBlank(message = "El código postal es obligatorio")
    @Column(name = "codigo_postal", nullable = false, length = 10)
    private String codigoPostal;

    @Column(length = 255)
    private String referencias;

    @Column(name = "telefono_contacto", length = 20)
    private String telefonoContacto;

    public Direccion() {
    }

    public Direccion(Long id, Usuario usuario, String calle, String numeroExterior, String numeroInterior,
                     String colonia, String ciudad, String estado, String codigoPostal, String referencias, String telefonoContacto) {
        this.id = id;
        this.usuario = usuario;
        this.calle = calle;
        this.numeroExterior = numeroExterior;
        this.numeroInterior = numeroInterior;
        this.colonia = colonia;
        this.ciudad = ciudad;
        this.estado = estado;
        this.codigoPostal = codigoPostal;
        this.referencias = referencias;
        this.telefonoContacto = telefonoContacto;
    }

    public String getDireccionCompleta() {
        StringBuilder sb = new StringBuilder();
        sb.append(calle).append(" #").append(numeroExterior);
        if (numeroInterior != null && !numeroInterior.isBlank()) {
            sb.append(" Int. ").append(numeroInterior);
        }
        if (colonia != null && !colonia.isBlank()) {
            sb.append(", Col. ").append(colonia);
        }
        sb.append(", C.P. ").append(codigoPostal);
        sb.append(", ").append(ciudad).append(", ").append(estado);
        return sb.toString();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getNumeroExterior() {
        return numeroExterior;
    }

    public void setNumeroExterior(String numeroExterior) {
        this.numeroExterior = numeroExterior;
    }

    public String getNumeroInterior() {
        return numeroInterior;
    }

    public void setNumeroInterior(String numeroInterior) {
        this.numeroInterior = numeroInterior;
    }

    public String getColonia() {
        return colonia;
    }

    public void setColonia(String colonia) {
        this.colonia = colonia;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public String getReferencias() {
        return referencias;
    }

    public void setReferencias(String referencias) {
        this.referencias = referencias;
    }

    public String getTelefonoContacto() {
        return telefonoContacto;
    }

    public void setTelefonoContacto(String telefonoContacto) {
        this.telefonoContacto = telefonoContacto;
    }

    @Override
    public String toString() {
        return getDireccionCompleta();
    }
}
