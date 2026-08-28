package com.datalyze.alquileres.api.entity;

import com.datalyze.alquileres.api.enumeration.RolUsuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Entity
@Table(name = "usuario")
@Getter
@Setter
@NoArgsConstructor
public class UsuarioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario", nullable = false)
    private Integer idUsuario;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol", nullable = false)
    private RolUsuario rol;

    // Relación Muchos a Muchos: Un usuario puede tener varias sedes, y una sede varios usuarios.
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "usuario_ubicacion",
            joinColumns = @JoinColumn(name = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_ubicacion")
    )
    private List<UbicacionEntity> ubicacionesAsignadas;
}
