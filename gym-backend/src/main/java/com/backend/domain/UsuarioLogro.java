package com.backend.domain;

import java.time.LocalDate;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "usuario_logros")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioLogro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long usuarioId;
    private Long logroId;
    private LocalDate fechaDesbloqueo;
}