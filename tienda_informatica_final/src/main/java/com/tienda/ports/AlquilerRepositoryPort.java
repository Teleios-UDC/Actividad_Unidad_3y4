package com.tienda.ports;

import com.tienda.model.Alquiler;
import java.util.List;

public interface AlquilerRepositoryPort {

    int save(Alquiler alquiler);
    
    Alquiler findById(int id);

    List<Alquiler> findAll();

    boolean update(Alquiler alquiler);

    boolean delete(int id);
}