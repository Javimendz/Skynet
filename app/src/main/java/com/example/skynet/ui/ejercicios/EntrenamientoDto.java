package com.example.skynet.ui.ejercicios;

public class EntrenamientoDto {
    private final Long id;
    private final String nombre;
    private final String descripcion;
    private final String imagenUrl;
    private final String origen;

    public EntrenamientoDto(Long id, String nombre, String descripcion, String imagenUrl, String origen) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagenUrl = imagenUrl;
        this.origen = origen != null ? origen : "General";
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public String getImagenUrl() { return imagenUrl; }
    public String getOrigen() { return origen; }

    @Deprecated
    public String getMusculo() { return origen; }
}
