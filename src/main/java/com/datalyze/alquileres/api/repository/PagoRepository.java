package com.datalyze.alquileres.api.repository;

import com.datalyze.alquileres.api.entity.PagoEntity;
import com.datalyze.alquileres.api.enumeration.PagoEstado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PagoRepository extends JpaRepository<PagoEntity,Integer> {
    List<PagoEntity> findAllByIdContrato(Integer idContrato);

    @Query("SELECT p FROM PagoEntity p " +
            "LEFT JOIN p.contrato c " +
            "LEFT JOIN c.propiedad pr " + // Agregamos el join a propiedad
            "LEFT JOIN c.cliente cl " +
            "WHERE (p.estado IN :estados) " +
            "AND (CAST(:idPropiedad AS integer) IS NULL OR c.idPropiedad = :idPropiedad) " +
            "AND (:isAdmin = true OR pr.idUbicacion IN :sedesIds) " + // <--- Filtro de aislamiento
            "AND (CAST(:fechaInicio AS date) IS NULL OR p.fechaVencimiento >= :fechaInicio) " +
            "AND (CAST(:fechaFin AS date) IS NULL OR p.fechaVencimiento <= :fechaFin) " +
            "AND (CAST(:termino AS string) IS NULL OR " +
            "     LOWER(cl.nombres) LIKE LOWER(CONCAT('%', CAST(:termino AS string), '%')) OR " +
            "     LOWER(cl.apellidos) LIKE LOWER(CONCAT('%', CAST(:termino AS string), '%')) OR " +
            "     LOWER(cl.dniCe) LIKE LOWER(CONCAT('%', CAST(:termino AS string), '%')))")
    Page<PagoEntity> buscarPagosAvanzados(
            @Param("termino") String termino,
            @Param("idPropiedad") Integer idPropiedad,
            @Param("estados") List<PagoEstado> estados,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin,
            @Param("isAdmin") boolean isAdmin,         // Nuevo
            @Param("sedesIds") List<Integer> sedesIds, // Nuevo
            Pageable pageable);
}
