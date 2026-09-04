package com.datalyze.alquileres.api.repository;

import com.datalyze.alquileres.api.entity.UbicacionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UbicacionRepository extends JpaRepository<UbicacionEntity, Integer> {
}
