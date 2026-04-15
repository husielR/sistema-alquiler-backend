package com.datalyze.alquileres.api.entity;

import com.datalyze.alquileres.api.enumeration.PagoEstado;
import com.datalyze.alquileres.api.enumeration.PagoTipoPago;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

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
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "estado")
    private PagoEstado estado;
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "tipo_pago")
    private PagoTipoPago tipoPago;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_contrato", referencedColumnName = "id_contrato",insertable = false, updatable = false)
    private ContratoEntity contrato;

}
