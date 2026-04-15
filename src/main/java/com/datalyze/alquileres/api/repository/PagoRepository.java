package com.datalyze.alquileres.api.repository;

import com.datalyze.alquileres.api.entity.PagoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagoRepository extends JpaRepository<PagoEntity,Integer> {
}
