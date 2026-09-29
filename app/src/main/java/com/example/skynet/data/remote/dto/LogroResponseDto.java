package com.example.skynet.data.remote.dto;

import com.google.gson.annotations.SerializedName;

public class LogroResponseDto {
    @SerializedName("nombre")
    private String nombre;
    @SerializedName("descripcion")
    private String descripcion;
    @SerializedName("icono")
    private String icono;
    @SerializedName("fechaDesbloqueo")
    private String fechaDesbloqueo;
    @SerializedName("porcentajeProgreso")
    private Double porcentajeProgreso;
    @SerializedName("desbloqueado")
    private boolean desbloqueado;

    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public String getIcono() { return icono; }
    public String getFechaDesbloqueo() { return fechaDesbloqueo; }
    public Double getPorcentajeProgreso() { return porcentajeProgreso; }
    public boolean isDesbloqueado() { return desbloqueado; }
}
