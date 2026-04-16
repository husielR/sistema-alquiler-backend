package com.datalyze.alquileres.api.repository;

import com.datalyze.alquileres.api.entity.PagoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PagoRepository extends JpaRepository<PagoEntity,Integer> {
    List<PagoEntity> findAllByIdContrato(Integer idContrato);
}
