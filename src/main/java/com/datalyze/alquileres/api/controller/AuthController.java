package com.datalyze.alquileres.api.controller;
import com.datalyze.alquileres.api.entity.UbicacionEntity;
import com.datalyze.alquileres.api.entity.UsuarioEntity;
import com.datalyze.alquileres.api.repository.UsuarioRepository;
import com.datalyze.alquileres.api.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager, UsuarioRepository usuarioRepository, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.usuarioRepository = usuarioRepository;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    @Transactional
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        // 1. Validar credenciales reales
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        // 2. Extraer datos operativos del usuario
        UsuarioEntity usuario = usuarioRepository.findByUsername(username).orElseThrow();
        List<Integer> sedesPermitidas = usuario.getUbicacionesAsignadas().stream()
                .map(UbicacionEntity::getIdUbicacion)
                .collect(Collectors.toList());

        // 3. Emitir JWT
        String token = jwtUtil.generateToken(usuario.getUsername(), usuario.getRol().name(), sedesPermitidas);
        return ResponseEntity.ok(Map.of("token", token));
    }
}
