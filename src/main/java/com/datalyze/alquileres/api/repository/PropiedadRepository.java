package com.datalyze.alquileres.api.repository;

import com.datalyze.alquileres.api.entity.ClienteEntity;
import com.datalyze.alquileres.api.entity.PropiedadEntity;
import com.datalyze.alquileres.api.enumeration.PropiedadEstado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PropiedadRepository extends JpaRepository<PropiedadEntity,Integer> {
    List<PropiedadEntity> findByEstadoNot(PropiedadEstado estado);
}
