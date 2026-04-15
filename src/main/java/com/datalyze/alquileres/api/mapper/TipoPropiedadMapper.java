package com.datalyze.alquileres.api.mapper;

import com.datalyze.alquileres.api.dto.TipoPropiedadDTO;
import com.datalyze.alquileres.api.dto.request.TipoPropiedadRequestDTO;
import com.datalyze.alquileres.api.entity.TipoPropiedadEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;
@Mapper(componentModel = "spring")
public interface TipoPropiedadMapper {
    TipoPropiedadDTO toDto(TipoPropiedadEntity entity);
    List<TipoPropiedadDTO> toDtoList(List<TipoPropiedadEntity> entities);

    TipoPropiedadEntity toEntity(TipoPropiedadRequestDTO request);

    void updateEntity(TipoPropiedadRequestDTO request, @MappingTarget TipoPropiedadEntity entity);
}
