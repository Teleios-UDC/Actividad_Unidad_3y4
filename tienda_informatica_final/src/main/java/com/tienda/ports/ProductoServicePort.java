package com.tienda.ports;

import com.tienda.model.Producto;
import java.util.List;

//Puerto de servicio para la gestión de productos
public interface ProductoServicePort {

    // Registra un nuevo producto
    int registrarNuevoProducto(Producto producto);


    // Consulta un producto por su ID
    Producto consultarProductoPorId(int id);

    // Obtiene todos los productos
    List<Producto> obtenerTodosLosProductos();

    // Actualiza un producto existente
    boolean actualizarProducto(Producto producto);

    // Elimina un producto por su ID
    boolean eliminarProducto(int id);
}