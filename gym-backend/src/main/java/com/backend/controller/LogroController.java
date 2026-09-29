package com.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;
import com.backend.dto.LogroResponseDto;
import com.backend.security.dto.ApiResponseDto;
import com.backend.service.ILogroService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/perfil")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Logros", description = "Sistema de gamificación y progreso del usuario")
public class LogroController {

    private final ILogroService logroService;

    @GetMapping("/logros")
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    @Operation(summary = "Obtener logros del usuario", description = "Devuelve todos los logros, desbloqueados y en progreso, ordenados por cercanía a completarse")
    public ResponseEntity<ApiResponseDto<List<LogroResponseDto>>> obtenerLogros(Authentication authentication) {
        log.info("Consultando logros para: {}", authentication.getName());
        List<LogroResponseDto> logros = logroService.obtenerLogrosUsuario(authentication.getName());

        return ResponseEntity.ok(ApiResponseDto.<List<LogroResponseDto>>builder()
                .mensaje("Logros recuperados")
                .success(true)
                .datos(logros)
                .build());
    }

    @GetMapping("/logros/proximo")
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    @Operation(summary = "Logro más cercano a desbloquear", description = "Devuelve solo el logro con mayor % de progreso aún no desbloqueado, para el widget de Home")
    public ResponseEntity<ApiResponseDto<LogroResponseDto>> obtenerProximoLogro(Authentication authentication) {
        LogroResponseDto proximo = logroService.obtenerProximoLogro(authentication.getName());
        return ResponseEntity.ok(ApiResponseDto.<LogroResponseDto>builder()
                .mensaje("Próximo logro calculado")
                .success(true)
                .datos(proximo)
                .build());
    }
}