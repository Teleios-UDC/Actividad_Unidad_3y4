package com.tienda.ports;

import com.tienda.model.Alquiler;
import java.util.List;

public interface AlquilerServicePort {
 
    int registrarNuevoAlquiler(Alquiler alquiler);
  
    Alquiler consultarAlquilerPorId(int id);

    List<Alquiler> obtenerTodosLosAlquileres();

    boolean actualizarDatosAlquiler(Alquiler alquiler);

    boolean eliminarAlquiler(int id);
}