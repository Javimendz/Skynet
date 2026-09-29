package com.example.skynet.data.remote.dto;

import com.google.gson.annotations.SerializedName;

public class EvolucionPesoDto {
    @SerializedName("fecha")
    private String fecha;
    @SerializedName("peso")
    private Double peso;

    public String getFecha() { return fecha; }
    public Double getPeso() { return peso; }
}
