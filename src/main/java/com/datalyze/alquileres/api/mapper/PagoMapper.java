package com.datalyze.alquileres.api.mapper;

import com.datalyze.alquileres.api.dto.PagoDTO;
import com.datalyze.alquileres.api.dto.PagoResumenDTO;
import com.datalyze.alquileres.api.dto.PropiedadResumenDTO;
import com.datalyze.alquileres.api.dto.request.PagoRequestDTO;
import com.datalyze.alquileres.api.entity.PagoEntity;
import com.datalyze.alquileres.api.entity.PropiedadEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PagoMapper {
    PagoDTO toDto(PagoEntity entity);
    List<PagoDTO> toDtoList(List<PagoEntity> entities);

    @Mapping(source = "contrato.idContrato", target = "contratoIdContrato")
    PagoResumenDTO toDtoResumen(PagoEntity entity);
    List<PagoResumenDTO> toDtoResumenList(List<PagoEntity> entities);

    PagoEntity toEntity(PagoRequestDTO request);
    void updateEntity(PagoRequestDTO request, @MappingTarget PagoEntity entity);
}
