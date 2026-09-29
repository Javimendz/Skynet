package com.backend.domain;


import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "invitaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invitacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String emailInvitado;

    @Column(nullable = false, unique = true)
    private String token; // para el enlace único

    @Column(nullable = false)
    @Builder.Default
    private String estado = "PENDIENTE";

    @Column(name = "fecha_creacion", nullable = false)
    @Builder.Default
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_invita_id", nullable = false)
    @ToString.Exclude
    private Usuario usuarioInvita;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "horario_id", nullable = true)
    @ToString.Exclude
    private Horario horario; // opcional, si invita a una clase concreta
}