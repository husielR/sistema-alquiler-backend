package com.datalyze.alquileres.api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "Pago")
@Getter
@Setter
@NoArgsConstructor
public class PagoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago", nullable = false)
    private Integer idPago;
    @Column(name = "id_contrato")
    private Integer idContrato;
    @Column(name = "periodo_mes")
    private Integer periodoMes;
    @Column(name = "periodo_anio")
    private Integer periodoAnio;
    @Column(name = "monto_pagado")
    private Double montoPagado;
    @Column(name = "fecha_pago")
    private LocalDate fechaPago;
    @Column(name = "fecha_vencimiento")
    private LocalDate fechaVencimiento;
    @Column(name = "estado")
    private String estado;
    @Column(name = "tipo_pago")
    private String tipoPago;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_contrato", referencedColumnName = "id_contrato",insertable = false, updatable = false)
    private ContratoEntity contrato;

}
