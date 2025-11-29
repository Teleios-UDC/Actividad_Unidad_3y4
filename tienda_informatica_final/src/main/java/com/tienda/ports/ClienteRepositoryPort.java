package com.tienda.ports;

import com.tienda.model.Cliente;
import java.util.List;


//Puerto de Salida: Contrato de acceso a datos para la entidad Cliente.

public interface ClienteRepositoryPort {

    // Guarda el Cliente, su Dirección y su Teléfono en una única transacción.
    int save(Cliente cliente);

    // Actualiza el Cliente y sus entidades relacionadas (Dirección, Teléfono).
    boolean update(Cliente cliente);

    // Busca un Cliente por su ID (cargando Dirección y Teléfono).
    Cliente findById(int id);

    // Obtiene todos los Clientes.
    List<Cliente> findAll();

    // Elimina un Cliente por su ID.
    boolean delete(int id);
}