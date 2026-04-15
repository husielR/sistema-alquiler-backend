package com.datalyze.alquileres.api.service;

import com.datalyze.alquileres.api.dto.ContratoDTO;
import com.datalyze.alquileres.api.dto.PropiedadDTO;
import com.datalyze.alquileres.api.dto.request.ContratoRequestDTO;
import com.datalyze.alquileres.api.entity.ClienteEntity;
import com.datalyze.alquileres.api.entity.ContratoEntity;
import com.datalyze.alquileres.api.entity.PropiedadEntity;
import com.datalyze.alquileres.api.enumeration.ContratoEstado;
import com.datalyze.alquileres.api.mapper.ContratoMapper;
import com.datalyze.alquileres.api.repository.ContratoRepository;
import com.datalyze.alquileres.api.service.imp.CrudImp;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ContratoService implements CrudImp<ContratoDTO, ContratoRequestDTO> {
    private final ContratoMapper contratoMapper;
    private final ContratoRepository contratoRepository;


    @Override
    public List<ContratoDTO> obtenerTodos() {
        return this.contratoMapper.toDtoList(this.contratoRepository.findAll());
    }

    @Override
    public ContratoDTO obtenerPorId(Integer id) {
        ContratoEntity contratoEntity = this.contratoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contrato no encontrado con ID: " + id));

        return this.contratoMapper.toDto(contratoEntity);
    }

    @Override
    public ContratoDTO crear(ContratoRequestDTO request) {
        ContratoEntity contratoEntity = this.contratoMapper.toEntity(request);
        contratoEntity = this.contratoRepository.save(contratoEntity);
        return this.contratoMapper.toDto(contratoEntity);
    }

    @Override
    public ContratoDTO actualizar(Integer id, ContratoRequestDTO request) {
        ContratoEntity contratoExistente = this.contratoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contrato no encontrado con ID: " + id));
        this.contratoMapper.updateEntity(request, contratoExistente);
        contratoExistente = this.contratoRepository.save(contratoExistente);
        return this.contratoMapper.toDto(contratoExistente);
    }

    @Override
    public void eliminar(Integer id) {
        if (!this.contratoRepository.existsById(id)) {
            throw new RuntimeException("Contrato no encontrado con ID: " + id);
        }
        this.contratoRepository.deleteById(id);
    }

    public List<ContratoDTO> obtenerContratoDisponibleCliente (Integer idCliente) {
        List<ContratoEntity> contratoEntity = this.contratoRepository.findByEstadoAndCliente_IdCliente(ContratoEstado.Activo,idCliente);

        return this.contratoMapper.toDtoList(contratoEntity);


    }
}
