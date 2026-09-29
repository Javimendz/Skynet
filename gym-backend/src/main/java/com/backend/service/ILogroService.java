package com.backend.service;

import com.backend.dto.LogroResponseDto;
import java.util.List;

public interface ILogroService {
    List<LogroResponseDto> obtenerLogrosUsuario(String username);
    LogroResponseDto obtenerProximoLogro(String username);
}