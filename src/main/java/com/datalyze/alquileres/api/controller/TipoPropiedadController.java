package com.datalyze.alquileres.api.controller;

import com.datalyze.alquileres.api.dto.ClienteDTO;
import com.datalyze.alquileres.api.dto.TipoPropiedadDTO;
import com.datalyze.alquileres.api.dto.request.ClienteRequestDTO;
import com.datalyze.alquileres.api.dto.request.TipoPropiedadRequestDTO;
import com.datalyze.alquileres.api.service.ClienteService;
import com.datalyze.alquileres.api.service.TipoPropiedadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tipopropiedad")
public class TipoPropiedadController {
    private TipoPropiedadService tipoPropiedadService;

    @Autowired
    public TipoPropiedadController(TipoPropiedadService tipoPropiedadService) {
        this.tipoPropiedadService = tipoPropiedadService;
    }

    @GetMapping
    public ResponseEntity<List<TipoPropiedadDTO>> obtenerTipoPropiedades() {
        return ResponseEntity.ok(this.tipoPropiedadService.obtenerTodos());
    }

    @PostMapping
    public ResponseEntity<TipoPropiedadDTO> crear(@RequestBody TipoPropiedadRequestDTO request) {
        return new ResponseEntity<>(this.tipoPropiedadService.crear(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoPropiedadDTO> actualizar(@PathVariable Integer id, @RequestBody TipoPropiedadRequestDTO request) {
        return ResponseEntity.ok(this.tipoPropiedadService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        this.tipoPropiedadService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}
