package com.example.skynet.data.remote.dto;

import com.google.gson.annotations.SerializedName;

public class EjercicioHistorialDto {
    @SerializedName("fecha")
    private String fecha;
    
    @SerializedName("peso")
    private double peso;
    
    @SerializedName("repeticiones")
    private int repeticiones;

    @SerializedName("mejor1RM")
    private double mejor1RM;
    
    @SerializedName("volumenTotal")
    private double volumenTotal;

    @SerializedName("duracion")
    private String duracion;

    public EjercicioHistorialDto() {}

    public EjercicioHistorialDto(String fecha, double peso, int repeticiones, String duracion) {
        this.fecha = fecha;
        this.peso = peso;
        this.repeticiones = repeticiones;
        this.duracion = duracion;
    }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    
    public double getPeso() { return peso; }
    public void setPeso(double peso) { this.peso = peso; }
    
    public int getRepeticiones() { return repeticiones; }
    public void setRepeticiones(int repeticiones) { this.repeticiones = repeticiones; }

    public double getMejor1RM() { 
        if (mejor1RM <= 0 && peso > 0 && repeticiones > 0) {
            return peso * (1 + 0.0333 * repeticiones);
        }
        return mejor1RM; 
    }
    
    public void setMejor1RM(double mejor1RM) { this.mejor1RM = mejor1RM; }

    public double getVolumenTotal() { 
        if (volumenTotal <= 0) {
            return peso * repeticiones;
        }
        return volumenTotal; 
    }
    
    public void setVolumenTotal(double volumenTotal) { this.volumenTotal = volumenTotal; }

    public String getDuracion() { return duracion; }
    public void setDuracion(String duracion) { this.duracion = duracion; }
}
