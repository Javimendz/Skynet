package com.backend.dto;


import com.backend.domain.enums.TipoNotificacion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OcupacionClaseDto {
    private Long horarioId;
    private LocalDate fecha;
    private int plazasOcupadas;
    private int aforoTotal;
    private List<String> nombresVisibles;
    private String evento; // "NUEVA_RESERVA" o "CANCELACION"
}