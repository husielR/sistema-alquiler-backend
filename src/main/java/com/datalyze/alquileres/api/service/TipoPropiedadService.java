package com.datalyze.alquileres.api.service;

import com.datalyze.alquileres.api.dto.TipoPropiedadDTO;
import com.datalyze.alquileres.api.dto.request.TipoPropiedadRequestDTO;
import com.datalyze.alquileres.api.entity.ClienteEntity;
import com.datalyze.alquileres.api.entity.TipoPropiedadEntity;
import com.datalyze.alquileres.api.mapper.ContratoMapper;
import com.datalyze.alquileres.api.mapper.TipoPropiedadMapper;
import com.datalyze.alquileres.api.repository.ContratoRepository;
import com.datalyze.alquileres.api.repository.TipoPropiedadRepository;
import com.datalyze.alquileres.api.service.imp.CrudImp;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TipoPropiedadService implements CrudImp<TipoPropiedadDTO, TipoPropiedadRequestDTO>{
    private final TipoPropiedadMapper tipoPropiedadMapper;
    private final TipoPropiedadRepository tipoPropiedadRepository;

    @Override
    public List<TipoPropiedadDTO> obtenerTodos() {
        return this.tipoPropiedadMapper.toDtoList(this.tipoPropiedadRepository.findAll());
    }

    @Override
    public TipoPropiedadDTO obtenerPorId(Integer id) {
        TipoPropiedadEntity tipoPropiedad = this.tipoPropiedadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tipo de propiedad no encontrada con ID: " + id));
        
        return tipoPropiedadMapper.toDto(tipoPropiedad);
    }

    @Override
    public TipoPropiedadDTO crear(TipoPropiedadRequestDTO request) {
        TipoPropiedadEntity tipoPropiedadEntity = this.tipoPropiedadMapper.toEntity(request);
        tipoPropiedadEntity = this.tipoPropiedadRepository.save(tipoPropiedadEntity);
        return tipoPropiedadMapper.toDto(tipoPropiedadEntity);
    }

    @Override
    public TipoPropiedadDTO actualizar(Integer id, TipoPropiedadRequestDTO request) {
        TipoPropiedadEntity propiedadExistente = this.tipoPropiedadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tipo de propiedad no encontrado con ID:: " + id));
        this.tipoPropiedadMapper.updateEntity(request, propiedadExistente);
        propiedadExistente = this.tipoPropiedadRepository.save(propiedadExistente);
        return this.tipoPropiedadMapper.toDto(propiedadExistente);    }

    @Override
    public void eliminar(Integer id) {
        if (!this.tipoPropiedadRepository.existsById(id)) {
            throw new RuntimeException("Tipo de propiedad no encontrado con ID: " + id);
        }
        this.tipoPropiedadRepository.deleteById(id);
    }
}
