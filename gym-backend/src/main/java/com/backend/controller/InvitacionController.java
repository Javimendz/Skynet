package com.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.dto.InvitacionRequestDto;
import com.backend.dto.InvitacionResponseDto;
import com.backend.security.dto.ApiResponseDto;
import com.backend.service.IInvitacionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Invitaciones", description = "Sistema de invitación de amigos a clases")
public class InvitacionController {

    private final IInvitacionService invitacionService;

    @PostMapping("/invitar")
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    @Operation(summary = "Invitar a un amigo", description = "Envía una invitación por email a un amigo, opcionalmente para una clase concreta")
    public ResponseEntity<ApiResponseDto<InvitacionResponseDto>> invitar(
            @Valid @RequestBody InvitacionRequestDto dto) {

        InvitacionResponseDto invitacion = invitacionService.crearInvitacion(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponseDto.<InvitacionResponseDto>builder()
                        .mensaje("Invitación enviada correctamente")
                        .success(true)
                        .datos(invitacion)
                        .build());
    }
}