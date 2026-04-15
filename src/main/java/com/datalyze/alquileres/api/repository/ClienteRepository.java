package com.datalyze.alquileres.api.repository;

import com.datalyze.alquileres.api.entity.ClienteEntity;
import com.datalyze.alquileres.api.enumeration.ContratoEstado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<ClienteEntity, Integer> {

    @Query("""
                SELECT c FROM ClienteEntity c
                WHERE EXISTS (
                    SELECT ct FROM ContratoEntity ct
                    WHERE ct.cliente = c
                    AND ct.estado = :estado
                )
            """)
    List<ClienteEntity> findClientesConContratoActivo(@Param("estado") ContratoEstado estado);
}
