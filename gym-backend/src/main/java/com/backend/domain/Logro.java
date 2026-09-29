package com.backend.domain;


import com.backend.domain.enums.TipoCondicion;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "logros")
@Data
public class Logro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;          // "Guerrero de Acero"
    private String descripcion;     // "Completa 10 sesiones de fuerza"
    private String icono;

    @Enumerated(EnumType.STRING)
    private TipoCondicion tipoCondicion; // SESIONES_TOTALES, SESIONES_INTENSIDAD, RACHA_DIAS, RESERVAS_TOTALES...

    private String filtroIntensidad;     // opcional, ej. "ALTA" si el logro es específico de un tipo
    private int objetivo;                // 10
}