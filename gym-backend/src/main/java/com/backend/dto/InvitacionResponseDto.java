package com.backend.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvitacionResponseDto {
    private Long id;
    private String emailInvitado;
    private String estado; // "PENDIENTE", "ACEPTADA", "EXPIRADA"
    private String enlaceInvitacion; // URL única para que el invitado se registre/acepte
}