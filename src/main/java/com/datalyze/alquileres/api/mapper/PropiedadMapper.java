package com.datalyze.alquileres.api.mapper;

import com.datalyze.alquileres.api.dto.PropiedadDTO;
import com.datalyze.alquileres.api.dto.PropiedadResumenDTO;
import com.datalyze.alquileres.api.dto.request.PropiedadRequestDTO;
import com.datalyze.alquileres.api.entity.PropiedadEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PropiedadMapper {

    PropiedadDTO toDto(PropiedadEntity entity);
    List<PropiedadDTO> toDtoList(List<PropiedadEntity> entities);

    @Mapping(source = "tipoPropiedad.nombre", target = "tipoPropiedadNombre")
    @Mapping(source = "ubicacion.nombre", target = "ubicacionNombre") // NUEVA LÍNEA
    PropiedadResumenDTO toDtoResumen(PropiedadEntity entity);

    List<PropiedadResumenDTO> toDtoResumenList(List<PropiedadEntity> entities);

    PropiedadEntity toEntity(PropiedadRequestDTO request);

    void updateEntity(PropiedadRequestDTO request, @MappingTarget PropiedadEntity entity);
}
