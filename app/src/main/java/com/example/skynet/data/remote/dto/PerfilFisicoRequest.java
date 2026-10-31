package com.example.skynet.data.remote.dto;

import com.google.gson.annotations.SerializedName;

public class PerfilFisicoRequest {
    @SerializedName("usuarioId")
    private Long usuarioId;
    
    @SerializedName("peso")
    private Double peso;
    
    @SerializedName("altura")
    private Double altura;
    
    @SerializedName("edad")
    private Integer edad;
    
    @SerializedName("nivelActividad")
    private String nivelActividad;
    
    @SerializedName("objetivo")
    private String objetivo;

    @SerializedName("genero")
    private String genero;

    @SerializedName("patronAlimentacion")
    private String patronAlimentacion;

    @SerializedName("alergias")
    private java.util.List<String> alergias;

    @SerializedName("alergiasLibre")
    private String alergiasLibre;

    @SerializedName("alimentosRechazados")
    private String alimentosRechazados;

    @SerializedName("numComidas")
    private Integer numComidas;

    // Getters y Setters
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    public Double getPeso() { return peso; }
    public void setPeso(Double peso) { this.peso = peso; }
    public Double getAltura() { return altura; }
    public void setAltura(Double altura) { this.altura = altura; }
    public Integer getEdad() { return edad; }
    public void setEdad(Integer edad) { this.edad = edad; }
    public String getNivelActividad() { return nivelActividad; }
    public void setNivelActividad(String nivelActividad) { this.nivelActividad = nivelActividad; }
    public String getObjetivo() { return objetivo; }
    public void setObjetivo(String objetivo) { this.objetivo = objetivo; }
    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }
    public String getPatronAlimentacion() { return patronAlimentacion; }
    public void setPatronAlimentacion(String patronAlimentacion) { this.patronAlimentacion = patronAlimentacion; }
    public java.util.List<String> getAlergias() { return alergias; }
    public void setAlergias(java.util.List<String> alergias) { this.alergias = alergias; }
    public String getAlergiasLibre() { return alergiasLibre; }
    public void setAlergiasLibre(String alergiasLibre) { this.alergiasLibre = alergiasLibre; }
    public String getAlimentosRechazados() { return alimentosRechazados; }
    public void setAlimentosRechazados(String alimentosRechazados) { this.alimentosRechazados = alimentosRechazados; }
    public Integer getNumComidas() { return numComidas; }
    public void setNumComidas(Integer numComidas) { this.numComidas = numComidas; }
}
