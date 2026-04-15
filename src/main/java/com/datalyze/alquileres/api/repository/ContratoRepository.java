package com.datalyze.alquileres.api.repository;

import com.datalyze.alquileres.api.entity.ContratoEntity;
import com.datalyze.alquileres.api.enumeration.ContratoEstado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContratoRepository extends JpaRepository<ContratoEntity,Integer> {
    boolean existsByPropiedad_IdPropiedadAndEstado(Integer idPropiedad, ContratoEstado estado);

}
