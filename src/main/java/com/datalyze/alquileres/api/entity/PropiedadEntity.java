package com.datalyze.alquileres.api.entity;

import com.datalyze.alquileres.api.enumeration.PropiedadEstado;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "Propiedad")
@Getter
@Setter
@NoArgsConstructor
public class PropiedadEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_propiedad", nullable = false)
    private Integer idPropiedad;
    @Column(name = "id_ubicacion")
    private Integer idUbicacion;
    @Column(name = "id_tipo", nullable = false)
    private Integer idTipo;
    @Column(name = "identificador")
    private String identificador;
    @Column(name = "precio_base")
    private Double precioBase;
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "estado")
    private PropiedadEstado estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo", referencedColumnName = "id_tipo", insertable = false, updatable = false)
    private TipoPropiedadEntity tipoPropiedad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ubicacion", referencedColumnName = "id_ubicacion", insertable = false, updatable = false)
    private UbicacionEntity ubicacion;
}
