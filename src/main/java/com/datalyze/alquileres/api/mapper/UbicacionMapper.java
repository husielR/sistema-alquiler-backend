package com.datalyze.alquileres.api.mapper;

import com.datalyze.alquileres.api.dto.UbicacionDTO;
import com.datalyze.alquileres.api.entity.UbicacionEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UbicacionMapper {
    UbicacionDTO toDto(UbicacionEntity entity);
    List<UbicacionDTO> toDtoList(List<UbicacionEntity> entities);
}
