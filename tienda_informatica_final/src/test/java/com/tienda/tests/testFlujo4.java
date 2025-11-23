package com.tienda.tests;

import com.tienda.adapters.MySQLAlquilerRepositoryAdapter;
import com.tienda.model.*;
import com.tienda.ports.AlquilerRepositoryPort;
import com.tienda.ports.AlquilerServicePort;
import com.tienda.services.AlquilerService;

import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class testFlujo4 {

    public static void main(String[] args) {
        AlquilerRepositoryPort repository = new MySQLAlquilerRepositoryAdapter();
        AlquilerServicePort servicio = new AlquilerService(repository);

        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            mostrarMenu();
            opcion = solicitarEntero(scanner);

            try {
                switch (opcion) {
                    case 1:
                        registrarNuevoAlquiler(scanner, servicio);
                        break;
                    case 2:
                        consultarAlquilerPorId(scanner, servicio);
                        break;
                    case 3:
                        listarTodosLosAlquileres(servicio);
                        break;
                    case 4:
                        actualizarAlquiler(scanner, servicio);
                        break;
                    case 5:
                        eliminarAlquiler(scanner, servicio);
                        break;
                    case 0:
                        System.out.println("Saliendo del Flujo 4: Gestión de Alquileres.");
                        break;
                    default:
                        System.out.println("Opción no válida. Intente de nuevo.");
                }
            } catch (IllegalArgumentException e) {
                System.err.println("Error de Lógica de Negocio: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Error Inesperado: " + e.getMessage());
                e.printStackTrace();
            }

        } while (opcion != 0);

        scanner.close();
    }

    private static void mostrarMenu() {
        System.out.println("\n--- MENÚ GESTIÓN DE ALQUILERES ---");
        System.out.println("1. Registrar Nuevo Alquiler");
        System.out.println("2. Consultar Alquiler por ID");
        System.out.println("3. Listar Todos los Alquileres");
        System.out.println("4. Actualizar Fechas/Monto");
        System.out.println("5. Eliminar Alquiler");
        System.out.println("0. Salir");
        System.out.print("Seleccione una opción: ");
    }


    private static int solicitarEntero(Scanner scanner) {
        try {
            int valor = scanner.nextInt();
            if (scanner.hasNextLine()) {
                 scanner.nextLine(); 
            }
            return valor;
        } catch (InputMismatchException e) {
            scanner.nextLine();
            return -1;
        }
    }



    // 1. REGISTRAR NUEVO ALQUILER

    private static void registrarNuevoAlquiler(Scanner scanner, AlquilerServicePort servicio) {
        System.out.println("\n--- REGISTRO DE ALQUILER ---");
        

        //SOLICITAR IDS Y DATOS DE LAS LLAVES FORÁNEAS

        // CLIENTE (FK)
        System.out.print("Ingrese el ID del Cliente que alquila (DEBE EXISTIR en BD): ");
        int idCliente = solicitarEntero(scanner);
        
        // PRODUCTO (FK)
        System.out.print("Ingrese el ID del Producto a alquilar (DEBE EXISTIR en BD): ");
        int idProducto = solicitarEntero(scanner);

        // CATEGORÍA BASE
        System.out.print("Ingrese el ID de la Categoría base del producto (1.CPU - 2.IMPRESORA - 3.MONITOR - 4.DISCO DURO - 5.OTRO) (DEBE EXISTIR): ");
        int idCategoria = solicitarEntero(scanner);
        
        //CREAR OBJETOS EN MEMORIA A PARTIR DE LOS DATOS
 
        
        // Categoria Base, Producto y Cliente (solo con IDs)
        Categoria categoriaBase = new Categoria(idCategoria, "Categoría " + idCategoria);

        System.out.print("Ingrese el Nombre del Producto: ");
        String nombreProducto = scanner.nextLine();
        System.out.print("Ingrese el Modelo del Producto: ");
        String modeloProducto = scanner.nextLine();
        
        Producto producto = new Producto(
            idProducto, 
            nombreProducto, 
            modeloProducto, 
            "Descripción temporal (Creada en consola)", 
            categoriaBase 
        );
        
        Cliente cliente = new Cliente();
        cliente.setIdCliente(idCliente);
        
        CategoriaAlquilable catAlq = new CategoriaAlquilable(
            idCategoria,
            categoriaBase.getIdCategoria()
        );
        catAlq.setCategoria(categoriaBase); 



        //CREAR TRANSACCIÓN Y ALQUILER ---

        System.out.println("\n--- DATOS DE TRANSACCIÓN Y TIEMPO ---");
        System.out.print("Ingrese el Precio Total del Alquiler: ");
        double precio = scanner.nextDouble();
        scanner.nextLine(); 

        System.out.print("Ingrese el Tipo de Pago (Efectivo/Tarjeta): ");
        String tipoPago = scanner.nextLine();
        
        Transaccion transaccion = new Transaccion();
        transaccion.setPrecio(precio); 
        transaccion.setTipoPago(tipoPago);
        
        System.out.print("Ingrese fecha de INICIO (YYYY-MM-DD): ");
        LocalDate inicio = LocalDate.parse(scanner.nextLine());
        System.out.print("Ingrese fecha de FIN (YYYY-MM-DD): ");
        LocalDate fin = LocalDate.parse(scanner.nextLine());

        Alquiler nuevoAlquiler = new Alquiler();
        nuevoAlquiler.setInicioAlquiler(inicio); 
        nuevoAlquiler.setFinAlquiler(fin);

        nuevoAlquiler.setCliente(cliente);
        nuevoAlquiler.setProducto(producto);
        nuevoAlquiler.setTransaccion(transaccion);
        nuevoAlquiler.setCategoriaAlquilable(catAlq);
        
        int idGenerado = servicio.registrarNuevoAlquiler(nuevoAlquiler);

        if (idGenerado > 0) {
            System.out.println("Alquiler registrado exitosamente con ID: " + idGenerado);
        } else {
            System.out.println("Fallo en el registro del Alquiler.");
        }
    }


    //CONSULTAR ALQUILER

    private static void consultarAlquilerPorId(Scanner scanner, AlquilerServicePort servicio) {
        System.out.print("Ingrese el ID del alquiler a consultar: ");
        int id = solicitarEntero(scanner);

        Alquiler a = servicio.consultarAlquilerPorId(id);

        if (a != null) {
            System.out.println("\n--- DETALLES DEL ALQUILER ID: " + a.getIdAlquiler() + " ---");
            
            System.out.println("Fechas: " + a.getInicioAlquiler() + " a " + a.getFinAlquiler());
            
            String cliente = (a.getCliente() != null) ? a.getCliente().getNombre() + " " + a.getCliente().getApellido() : "N/A";
            String producto = (a.getProducto() != null) ? a.getProducto().getNombre() : "N/A"; 
            
            String categoria = (a.getCategoriaAlquilable() != null && a.getCategoriaAlquilable().getCategoria() != null) 
                             ? a.getCategoriaAlquilable().getCategoria().getNombre() 
                             : "N/A";

            System.out.println("\nCliente: " + cliente + " (ID: " + a.getCliente().getIdCliente() + ")");
            System.out.println("Producto: " + producto + " (ID: " + a.getProducto().getIdProducto() + ")");
            System.out.println("Categoría Base Alquilada: " + categoria);

            Transaccion t = a.getTransaccion();
            System.out.println("\n[TRANSACCIÓN ID: " + t.getIdTransaccion() + "]");
            System.out.printf("  Monto Total: $%.2f%n", t.getPrecio());
            System.out.println("  Tipo/Fecha: " + t.getTipoPago() + " (Fecha no almacenada en BD)"); 

        } else {
            System.out.println("Alquiler con ID " + id + " no encontrado.");
        }
    }
    

    //LISTAR ALQUILERES (READ ALL)

    private static void listarTodosLosAlquileres(AlquilerServicePort servicio) {
        System.out.println("\n--- LISTADO COMPLETO DE ALQUILERES ---");

        List<Alquiler> alquileres = servicio.obtenerTodosLosAlquileres();

        if (alquileres == null || alquileres.isEmpty()) {
            System.out.println("No hay alquileres registrados.");
            return;
        }

        System.out.printf("%-5s | %-15s | %-10s | %-10s | %-10s | %s%n", 
                          "ID", "CLIENTE (Nombre)", "PRODUCTO ID", "INICIO", "FIN", "PRECIO");
        System.out.println("----------------------------------------------------------------------");

        for (Alquiler a : alquileres) {
            
            String clienteNombre = (a.getCliente() != null) ? a.getCliente().getNombre() : "N/A";
            double precio = (a.getTransaccion() != null) ? a.getTransaccion().getPrecio() : 0.0;
            
            System.out.printf("%-5d | %-15s | %-10d | %-10s | %-10s | $%.2f%n",
                               a.getIdAlquiler(), clienteNombre, a.getProducto().getIdProducto(),
                               a.getInicioAlquiler(), a.getFinAlquiler(), precio);
        }
    }


    //ACTUALIZAR ALQUILER (UPDATE)

    private static void actualizarAlquiler(Scanner scanner, AlquilerServicePort servicio) {
        System.out.print("Ingrese el ID del alquiler a actualizar: ");
        int id = solicitarEntero(scanner);

        Alquiler alquilerExistente = servicio.consultarAlquilerPorId(id);

        if (alquilerExistente == null) {
            System.out.println("Alquiler con ID " + id + " no encontrado para actualizar.");
            return;
        }

        System.out.println("\n--- ACTUALIZANDO ALQUILER ID: " + id + " ---");
        System.out.print("Ingrese nueva fecha de INICIO (YYYY-MM-DD, o presione Enter para mantener " + alquilerExistente.getInicioAlquiler() + "): ");
        String nuevaFechaInicioStr = scanner.nextLine();
        
        if (!nuevaFechaInicioStr.isEmpty()) {
            alquilerExistente.setInicioAlquiler(LocalDate.parse(nuevaFechaInicioStr));
        }

        System.out.print("Ingrese nueva fecha de FIN (YYYY-MM-DD, o presione Enter para mantener " + alquilerExistente.getFinAlquiler() + "): ");
        String nuevaFechaFinStr = scanner.nextLine();
        
        if (!nuevaFechaFinStr.isEmpty()) {
            alquilerExistente.setFinAlquiler(LocalDate.parse(nuevaFechaFinStr));
        }

        System.out.printf("Ingrese nuevo Precio Total (%.2f, o presione Enter para mantener): ", alquilerExistente.getTransaccion().getPrecio());
        String nuevoPrecioStr = scanner.nextLine();

        if (!nuevoPrecioStr.isEmpty()) {
            try {
                double nuevoPrecio = Double.parseDouble(nuevoPrecioStr);
                alquilerExistente.getTransaccion().setPrecio(nuevoPrecio);
            } catch (NumberFormatException e) {
                System.err.println("Precio inválido. Se mantendrá el precio anterior.");
            }
        }

        if (servicio.actualizarDatosAlquiler(alquilerExistente)) {
            System.out.println("Alquiler ID " + id + " actualizado exitosamente.");
        } else {
            System.out.println("Fallo al actualizar el Alquiler ID " + id + ".");
        }
    }


    // 5. ELIMINAR ALQUILER

    private static void eliminarAlquiler(Scanner scanner, AlquilerServicePort servicio) {
        System.out.print("Ingrese el ID del alquiler a eliminar: ");
        int id = solicitarEntero(scanner);

        if (servicio.eliminarAlquiler(id)) {
            System.out.println("Alquiler y Transacción asociados al ID " + id + " eliminados/anulados exitosamente.");
        } else {
            System.out.println("Fallo al eliminar/anular el Alquiler ID " + id + ". Verifique si existe.");
        }
    }
}