package com.datalyze.alquileres.api.controller;

import com.datalyze.alquileres.api.dto.ClienteDTO;
import com.datalyze.alquileres.api.dto.UbicacionDTO;
import com.datalyze.alquileres.api.service.UbicacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ubicacion")
public class UbicacionController {
    private UbicacionService  ubicacionService;


    @Autowired
    public UbicacionController(UbicacionService ubicacionService) {
        this.ubicacionService = ubicacionService;
    }

    @GetMapping
    public ResponseEntity<List<UbicacionDTO>> getClienteEntity() {
        return ResponseEntity.ok(this.ubicacionService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UbicacionDTO> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(this.ubicacionService.obtenerPorId(id));
    }
}
