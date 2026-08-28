package com.datalyze.alquileres.api.repository;

import com.datalyze.alquileres.api.entity.ContratoEntity;
import com.datalyze.alquileres.api.enumeration.ContratoEstado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContratoRepository extends JpaRepository<ContratoEntity,Integer> {
    boolean existsByPropiedad_IdPropiedadAndEstado(Integer idPropiedad, ContratoEstado estado);

    List<ContratoEntity> findByEstadoAndCliente_IdCliente(ContratoEstado estado, Integer idCliente);

    boolean existsByCliente_IdCliente(Integer clienteIdCliente);

    boolean existsByPropiedad_IdPropiedad(Integer propiedadIdPropiedad);

    @Query("SELECT c FROM ContratoEntity c " +
            "LEFT JOIN c.cliente cl " +
            "LEFT JOIN c.propiedad pr " + // Agregamos el join a propiedad
            "WHERE (CAST(:estado AS string) IS NULL OR c.estado = :estado) " +
            "AND (CAST(:idPropiedad AS integer) IS NULL OR c.idPropiedad = :idPropiedad) " +
            "AND (:isAdmin = true OR pr.idUbicacion IN :sedesIds) " + // <--- Filtro de aislamiento
            "AND (CAST(:termino AS string) IS NULL OR " +
            "     LOWER(cl.nombres) LIKE LOWER(CONCAT('%', CAST(:termino AS string), '%')) OR " +
            "     LOWER(cl.apellidos) LIKE LOWER(CONCAT('%', CAST(:termino AS string), '%')) OR " +
            "     LOWER(cl.dniCe) LIKE LOWER(CONCAT('%', CAST(:termino AS string), '%')))")
    Page<ContratoEntity> buscarConFiltrosAvanzados(
            @Param("termino") String termino,
            @Param("idPropiedad") Integer idPropiedad,
            @Param("estado") ContratoEstado estado,
            @Param("isAdmin") boolean isAdmin,         // Nuevo
            @Param("sedesIds") List<Integer> sedesIds, // Nuevo
            Pageable pageable);

}
