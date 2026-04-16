package com.datalyze.alquileres.api.mapper;

import com.datalyze.alquileres.api.dto.ContratoDTO;
import com.datalyze.alquileres.api.dto.ContratoResumenDTO;
import com.datalyze.alquileres.api.dto.request.ContratoRequestDTO;
import com.datalyze.alquileres.api.entity.ContratoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring",uses = {PagoMapper.class})
public interface ContratoMapper {
    ContratoDTO toDto(ContratoEntity entity);
    List<ContratoDTO> toDtoList(List<ContratoEntity> entities);

    ContratoResumenDTO toDtoResumen(ContratoEntity entity);
    List<ContratoResumenDTO> toDtoResumenList(List<ContratoEntity> entities);

    ContratoEntity toEntity(ContratoRequestDTO request);
    void updateEntity(ContratoRequestDTO request, @MappingTarget ContratoEntity entity);
}
