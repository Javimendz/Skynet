package com.backend.mapper;

import com.backend.domain.RegistroPeso;
import com.backend.dto.EvolucionPesoDto;

import java.util.List;
import java.util.stream.Collectors;

public class EvolucionPesoMapper {

    private EvolucionPesoMapper() {}

    public static EvolucionPesoDto toDto(RegistroPeso registro) {
        return EvolucionPesoDto.builder()
                .fecha(registro.getFechaRegistro())
                .peso(registro.getPeso())
                .build();
    }

    public static List<EvolucionPesoDto> toDtoList(List<RegistroPeso> registros) {
        return registros.stream()
                .map(EvolucionPesoMapper::toDto)
                .collect(Collectors.toList());
    }
}