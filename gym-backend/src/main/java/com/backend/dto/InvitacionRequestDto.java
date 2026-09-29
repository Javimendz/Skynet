package com.backend.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvitacionRequestDto {

    @NotNull(message = "El ID del usuario que invita es obligatorio")
    private Long usuarioInvitaId;

    @NotBlank(message = "El email del invitado es obligatorio")
    @Email(message = "El email no tiene un formato válido")
    private String emailInvitado;

    // Opcional: si la invitación es para una clase concreta
    private Long horarioId;
}