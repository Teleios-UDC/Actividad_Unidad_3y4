package com.tienda.services;

import com.tienda.ports.ProductoRepositoryPort;
import com.tienda.ports.ProductoServicePort;
import com.tienda.model.Producto;
import java.util.List;


    //Capa de Servicios/Dominio : Implementa la lógica de negocio.

public class ProductoService implements ProductoServicePort {
    
    // Inyección de Dependencia
    private final ProductoRepositoryPort repositoryPort;

    // Constructor para inyección
    public ProductoService(ProductoRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    //Implementación de Operaciones de Negocio

    @Override
    public int registrarNuevoProducto(Producto producto) {
        //Aquí irían validaciones antes de guardar
        if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio.");
        }
        
        System.out.println("LOG: Lógica de negocio (Service) ejecutada. Delegando a Persistencia.");
        

        return repositoryPort.save(producto);
    }

    @Override
    public Producto consultarProductoPorId(int id) {
        //Lógica de Consulta
        return repositoryPort.findById(id);
    }

    @Override
    public List<Producto> obtenerTodosLosProductos() {
        return repositoryPort.findAll();
    }

    @Override
    public boolean actualizarProducto(Producto producto) {
        //Verifica si el ID existe antes de actualizar
        if (producto.getIdProducto() <= 0) {
            throw new IllegalArgumentException("Se requiere un ID válido para actualizar.");
        }
        return repositoryPort.update(producto);
    }

    @Override
    public boolean eliminarProducto(int id) {
        //Verifica si el producto tiene ventas pendientes antes de eliminarlo
        
        return repositoryPort.delete(id);
    }
}