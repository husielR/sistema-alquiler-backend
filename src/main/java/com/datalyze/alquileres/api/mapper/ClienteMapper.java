package com.datalyze.alquileres.api.mapper;

import com.datalyze.alquileres.api.dto.ClienteDTO;
import com.datalyze.alquileres.api.dto.ClienteResumenDTO;
import com.datalyze.alquileres.api.dto.request.ClienteRequestDTO;
import com.datalyze.alquileres.api.entity.ClienteEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ContratoMapper.class})
public interface ClienteMapper {
    //Para el GET (Lectura)
    ClienteDTO toDto(ClienteEntity entity);
    List<ClienteDTO> toDtoList(List<ClienteEntity> entities);

    List<ClienteResumenDTO> toDtoResumenList(List<ClienteEntity> entities);


    // Para el POST (Crear) -> Convierte el JSON de entrada a Entidad
    ClienteEntity toEntity(ClienteRequestDTO request);

    // Para el PUT (Actualizar) -> Toma los datos del JSON y sobreescribe la Entidad existente
    void updateEntity(ClienteRequestDTO request, @MappingTarget ClienteEntity entity);
}
