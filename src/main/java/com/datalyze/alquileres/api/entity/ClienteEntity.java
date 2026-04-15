package com.datalyze.alquileres.api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "Cliente")
@Getter
@Setter
@NoArgsConstructor
public class ClienteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente", nullable = false)
    private Integer idCliente;
    @Column(name = "dni_ce", nullable = false)
    private String dniCe;
    @Column(name = "nombres", nullable = false)
    private String nombres;
    @Column(name = "apellidos", nullable = false)
    private String apellidos;
    @Column(name = "telefono", nullable = false)
    private String telefono;
    @Column(name = "email")
    private String email;
    @Column(name = "contacto_emergencia")
    private String contactoEmergencia;

    @OneToMany(mappedBy = "cliente")
    private List<ContratoEntity> contrato;
}
