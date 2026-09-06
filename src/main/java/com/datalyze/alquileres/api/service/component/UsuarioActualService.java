package com.datalyze.alquileres.api.service.component;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UsuarioActualService {

    private Authentication getAuthentication() {
        return SecurityContextHolder
                .getContext()
                .getAuthentication();
    }

    public boolean isEncargado(){
        return getAuthentication().getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ENCARGADO"));
    }

    public boolean isAdmin() {
        return getAuthentication().getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    public List<Integer> obtenerMisSedes() {
        Authentication auth = getAuthentication();
        @SuppressWarnings("unchecked")
        List<Integer> misSedes = (List<Integer>) auth.getDetails();
        if (misSedes == null || misSedes.isEmpty()) {
            return List.of(-1);
        }
        return misSedes;
    }

}
