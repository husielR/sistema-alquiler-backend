package com.datalyze.alquileres.api.controller;

import com.datalyze.alquileres.api.dto.ClienteDTO;
import com.datalyze.alquileres.api.dto.PropiedadDTO;
import com.datalyze.alquileres.api.dto.PropiedadResumenDTO;
import com.datalyze.alquileres.api.dto.request.ClienteRequestDTO;
import com.datalyze.alquileres.api.dto.request.PropiedadRequestDTO;
import com.datalyze.alquileres.api.enumeration.PropiedadEstado;
import com.datalyze.alquileres.api.service.PropiedadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/propiedad")
public class PropiedadController {
    private PropiedadService propiedadService;

    @Autowired
    public PropiedadController(PropiedadService propiedadService) {
        this.propiedadService = propiedadService;
    }

    @GetMapping
    public ResponseEntity<List<PropiedadDTO>> getPropiedadEntity() {
        return ResponseEntity.ok(this.propiedadService.obtenerTodos());
    }

    @PostMapping
    public ResponseEntity<PropiedadDTO> crearPropiedad(@RequestBody PropiedadRequestDTO request) {
        return new ResponseEntity<>(this.propiedadService.crear(request), HttpStatus.CREATED);
    }

    @GetMapping("/estados")
    public ResponseEntity<List<String>> obtenerEstados() {
        List<String> estados = Arrays.stream(PropiedadEstado.values())
                .map(Enum::name)
                .toList();
        return ResponseEntity.ok(estados);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PropiedadDTO> actualizarPropiedad(@PathVariable Integer id, @RequestBody PropiedadRequestDTO request) {
        return ResponseEntity.ok(this.propiedadService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable Integer id) {
        this.propiedadService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<PropiedadResumenDTO>> getClienteActivoEntity() {
        return ResponseEntity.ok(this.propiedadService.getPropiedadAvailable());
    }

    @GetMapping("/disponibles-edicion/{idPropiedadActual}")
    public ResponseEntity<List<PropiedadResumenDTO>> obtenerPropiedadesParaEdicion(@PathVariable Integer idPropiedadActual) {
        return ResponseEntity.ok(this.propiedadService.getPropiedadesParaEdicion(idPropiedadActual));
    }
}
