package com.backend.dto;

import lombok.*;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EvolucionPesoDto {
    private LocalDate fecha;
    private Double peso;
}