package com.datalyze.alquileres.api.service;

import com.datalyze.alquileres.api.dto.ClienteDTO;
import com.datalyze.alquileres.api.dto.PropiedadDTO;
import com.datalyze.alquileres.api.dto.PropiedadResumenDTO;
import com.datalyze.alquileres.api.dto.request.PropiedadRequestDTO;
import com.datalyze.alquileres.api.entity.ClienteEntity;
import com.datalyze.alquileres.api.entity.PropiedadEntity;
import com.datalyze.alquileres.api.enumeration.ContratoEstado;
import com.datalyze.alquileres.api.enumeration.PropiedadEstado;
import com.datalyze.alquileres.api.mapper.PropiedadMapper;
import com.datalyze.alquileres.api.repository.ContratoRepository;
import com.datalyze.alquileres.api.repository.PropiedadRepository;
import com.datalyze.alquileres.api.service.imp.CrudImp;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PropiedadService implements CrudImp<PropiedadDTO, PropiedadRequestDTO> {
    private final PropiedadRepository propiedadRepository;
    private final PropiedadMapper propiedadMapper;
    private final ContratoRepository contratoRepository;


    @Override
    public List<PropiedadDTO> obtenerTodos() {
        List<PropiedadEntity> entidades = this.propiedadRepository.findAll();
        return this.propiedadMapper.toDtoList(entidades);
    }

    @Override
    public PropiedadDTO obtenerPorId(Integer id) {
        PropiedadEntity propiedad = this.propiedadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Propiedad no encontrado con ID: " + id));
        return this.propiedadMapper.toDto(propiedad);
    }

    @Override
    public PropiedadDTO crear(PropiedadRequestDTO request) {
        PropiedadEntity nuevaEntidad = this.propiedadMapper.toEntity(request);
        nuevaEntidad = this.propiedadRepository.save(nuevaEntidad);
        return this.propiedadMapper.toDto(nuevaEntidad);
    }

    @Override
    public PropiedadDTO actualizar(Integer idPropiedad, PropiedadRequestDTO request) {
        PropiedadEntity propiedadExistente = this.propiedadRepository.findById(idPropiedad)
                .orElseThrow(() -> new RuntimeException("Propiedad no encontrada con ID: " + idPropiedad));

        if (request.estado().equals("Disponible")) {
            boolean tieneContratoActivo = contratoRepository.existsByPropiedad_IdPropiedadAndEstado(idPropiedad, ContratoEstado.Activo);

            if (tieneContratoActivo) {
                throw new RuntimeException("No se puede marcar como Disponible. Existe un contrato ACTIVO para esta propiedad.");
            }

        }
        this.propiedadMapper.updateEntity(request, propiedadExistente);
        propiedadExistente = this.propiedadRepository.save(propiedadExistente);
        return this.propiedadMapper.toDto(propiedadExistente);
    }


    @Override
    public void eliminar(Integer id) {
        if (!this.propiedadRepository.existsById(id)) {
            throw new RuntimeException("Propiedad no encontrado con ID: " + id);
        }

        boolean tienePropiedad = this.contratoRepository.existsByPropiedad_IdPropiedad(id);

        if (tienePropiedad) {
            throw new RuntimeException("No se puede eliminar: Este propiedad tiene contratos en su historial. Por favor, desactívelo en su lugar.");
        } else {
            this.contratoRepository.deleteById(id);
        }
    }

    public List<PropiedadResumenDTO> getPropiedadAvailable() {
        return this.propiedadMapper.toDtoResumenList(this.propiedadRepository.findByEstadoNot(PropiedadEstado.Ocupado));
    }

    public List<PropiedadResumenDTO> getPropiedadesParaEdicion(Integer idPropiedadActual) {
        List<PropiedadEntity> entidades = this.propiedadRepository.findByEstadoNotOrIdPropiedad(PropiedadEstado.Ocupado, idPropiedadActual);
        return this.propiedadMapper.toDtoResumenList(entidades);
    }
}
