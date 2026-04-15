package com.datalyze.alquileres.api.service;

import com.datalyze.alquileres.api.dto.ContratoDTO;
import com.datalyze.alquileres.api.dto.PagoDTO;
import com.datalyze.alquileres.api.dto.PagoResumenDTO;
import com.datalyze.alquileres.api.dto.request.ContratoRequestDTO;
import com.datalyze.alquileres.api.dto.request.PagoRequestDTO;
import com.datalyze.alquileres.api.entity.ContratoEntity;
import com.datalyze.alquileres.api.entity.PagoEntity;
import com.datalyze.alquileres.api.entity.PropiedadEntity;
import com.datalyze.alquileres.api.mapper.PagoMapper;
import com.datalyze.alquileres.api.repository.ContratoRepository;
import com.datalyze.alquileres.api.repository.PagoRepository;
import com.datalyze.alquileres.api.service.imp.CrudImp;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PagoService implements CrudImp<PagoDTO, PagoRequestDTO> {
    private final PagoRepository pagoRepository;
    private final PagoMapper pagoMapper;

    @Override
    public List<PagoDTO> obtenerTodos() {
        List<PagoEntity> entidades = this.pagoRepository.findAll();
        return this.pagoMapper.toDtoList(entidades);
    }

    @Override
    public PagoDTO obtenerPorId(Integer id) {
        PagoEntity pagoEntity = this.pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado con ID: " + id));
        return this.pagoMapper.toDto(pagoEntity);

    }

    @Override
    public PagoDTO crear(PagoRequestDTO request) {
        PagoEntity pagoEntity = this.pagoMapper.toEntity(request);
        pagoEntity = this.pagoRepository.save(pagoEntity);
        return this.pagoMapper.toDto(pagoEntity);
    }

    @Override
    public PagoDTO actualizar(Integer id, PagoRequestDTO request) {
        return null;
    }

    @Override
    public void eliminar(Integer id) {

    }

    public List<PagoResumenDTO> obtenerTodosResumen() {
        List<PagoEntity> entidades = this.pagoRepository.findAll();
        return this.pagoMapper.toDtoResumenList(entidades);
    }
}
