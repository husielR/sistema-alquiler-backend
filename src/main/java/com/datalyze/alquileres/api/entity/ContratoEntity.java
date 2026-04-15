package com.datalyze.alquileres.api.entity;

import com.datalyze.alquileres.api.enumeration.ContratoEstado;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "Contrato")
@Getter
@Setter
@NoArgsConstructor
public class ContratoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_contrato", nullable = false)
    private Integer idContrato;
    @Column(name = "id_cliente")
    private Integer idCliente;
    @Column(name = "id_propiedad")
    private Integer idPropiedad;
    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;
    @Column(name = "fecha_fin")
    private LocalDate fechaFin;
    @Column(name = "monto_garantia")
    private Double montoGarantia;
    @Column(name = "monto_mensual")
    private Double montoMensual;
    @Column(name = "dia_pago")
    private Integer diaPago;
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "estado")
    private ContratoEstado estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente",referencedColumnName = "id_cliente",insertable = false, updatable = false)
    private ClienteEntity cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_propiedad",referencedColumnName = "id_propiedad",insertable = false, updatable = false)
    private PropiedadEntity propiedad;
}
