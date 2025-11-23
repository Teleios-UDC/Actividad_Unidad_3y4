package com.tienda.ports;

import com.tienda.model.Producto;
import java.util.List;

public interface ProductoRepositoryPort {

    int save(Producto producto);

    boolean update(Producto producto);

    Producto findById(int id);

    List<Producto> findAll();

    boolean delete(int id);
}