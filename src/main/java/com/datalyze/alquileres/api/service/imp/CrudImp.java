package com.datalyze.alquileres.api.service.imp;

import java.util.List;

public interface CrudImp<t,d> {
    List<t> obtenerTodos();
    t obtenerPorId(Integer id);
    t crear(d request);
    t actualizar(Integer id, d request);
    void eliminar(Integer id);
}
