package com.tienda.tests;

import com.tienda.adapters.MySQLProductoRepositoryAdapter; 
import com.tienda.services.ProductoService;              
import com.tienda.ports.ProductoRepositoryPort;         
import com.tienda.ports.ProductoServicePort;           

import com.tienda.model.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;


// Este test simula un flujo completo de gestión de productos (CRUD)
// utilizando la arquitectura hexagonal con interacción por consola.

public class testFlujo1 {

    public static void main(String[] args) {

        ProductoRepositoryPort adaptadorBD = new MySQLProductoRepositoryAdapter();
        ProductoServicePort servicioProducto = new ProductoService(adaptadorBD);

        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            mostrarMenu();
            opcion = solicitarOpcion(scanner); 

            try {
                switch (opcion) {
                    case 1:
                        agregarProducto(scanner, servicioProducto);
                        break;
                    case 2:
                        actualizarProducto(scanner, servicioProducto);
                        break;
                    case 3:
                        consultarProductoPorId(scanner, servicioProducto);
                        break;
                    case 4:
                        listarTodosLosProductos(servicioProducto);
                        break;
                    case 5:
                        eliminarProducto(scanner, servicioProducto);
                        break;
                    case 6:
                        System.out.println("Saliendo del Flujo 1. ¡Adiós!");
                        break;
                    default:
                        System.out.println("Opción no válida. Intente de nuevo.");
                }
            } catch (Exception e) {
                System.err.println("¡ERROR EN LA OPERACIÓN!: " + e.getMessage());
                scanner.nextLine();
            }
        } while (opcion != 6);
        
        scanner.close();
    }

    private static void mostrarMenu() {
        System.out.println("\n===== FLÚJO 1: GESTIÓN DE PRODUCTO (CRUD) =====");
        System.out.println("1. Agregar Nuevo Producto");
        System.out.println("2. Actualizar Producto Existente");
        System.out.println("3. Consultar Producto por ID");
        System.out.println("4. Listar Todos los Productos");
        System.out.println("5. Eliminar Producto por ID");
        System.out.println("6. Salir");
        System.out.print("Seleccione una opción: ");
    }
    
    private static int solicitarOpcion(Scanner scanner) {
        if (scanner.hasNextInt()) {
            int opcion = scanner.nextInt();
            scanner.nextLine();
            return opcion;
        } else {
            System.out.println("Entrada inválida. Debe ser un número.");
            scanner.nextLine();
            return 0;
        }
    }


    //AGREGAR PRODUCTO (CREATE)

    private static void agregarProducto(Scanner scanner, ProductoServicePort servicio) {
        System.out.println("\n--- REGISTRAR NUEVO PRODUCTO ---");
        
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        
        System.out.print("Modelo: ");
        String modelo = scanner.nextLine();
        
        System.out.print("Descripción: ");
        String descripcion = scanner.nextLine();
        
        System.out.println("\nCategorías disponibles:");
        System.out.println("1: CPU");
        System.out.println("2: Impresora");
        System.out.println("3: Monitor");
        System.out.println("4: Disco Duro");
        System.out.println("5: Otros");
        System.out.print("Ingrese el ID de la Categoría (ej: 1, 2, 3, etc.): ");
        int idCategoria = solicitarOpcion(scanner);
        
        // Crear las entidades
        Categoria categoria = new Categoria(idCategoria, null);
        Producto nuevoProducto = new Producto(nombre, modelo, descripcion, categoria);
        
        System.out.print("¿Es Producto de Alta Tecnología? (S/N): ");
        String esAltaTec = scanner.nextLine().trim().toUpperCase();

        if (esAltaTec.equals("S")) {
            System.out.print("País de Origen: ");
            String paisOrigen = scanner.nextLine();

            //Se puso para la fecha la fecha actual
            ProductoAltaTecnologia pat = new ProductoAltaTecnologia(
                paisOrigen, 
                LocalDate.now(),
                nuevoProducto,
                new CategoriaAltaTecnologia(idCategoria, categoria)
            );
            nuevoProducto.setProductoAltaTecnologia(pat);
        }

        int idGenerado = servicio.registrarNuevoProducto(nuevoProducto);

        if (idGenerado > 0) {
            System.out.println("Producto registrado exitosamente. ID Generado: " + idGenerado);
        } else {
            System.out.println("Error al registrar el producto.");
        }
    }

    //CONSULTAR PRODUCTO
    private static void consultarProductoPorId(Scanner scanner, ProductoServicePort servicio) {
        System.out.print("Ingrese el ID del producto a consultar: ");
        int id = solicitarOpcion(scanner);

        Producto producto = servicio.consultarProductoPorId(id);

        if (producto != null) {
            System.out.println("\n--- DETALLES DEL PRODUCTO ID: " + producto.getIdProducto() + " ---");
            System.out.println("Nombre: " + producto.getNombre());
            System.out.println("Modelo: " + producto.getModelo());
            System.out.println("Descripción: " + producto.getDescripcion());
            System.out.println("Categoría ID: " + producto.getCategoria().getIdCategoria() + " (" + producto.getCategoria().getNombre() + ")");

            if (producto.getProductoAltaTecnologia() != null) {
                ProductoAltaTecnologia pat = producto.getProductoAltaTecnologia();
                System.out.println("  [ALTA TECNOLOGÍA]");
                System.out.println("  País Origen: " + pat.getPaisOrigen());
                System.out.println("  Fecha Fabricación: " + pat.getFechaFabricacion());
            }
        } else {
            System.out.println("Producto con ID " + id + " no encontrado.");
        }
    }


    //LISTAR PRODUCTOS (READ ALL)

    private static void listarTodosLosProductos(ProductoServicePort servicio) {
        System.out.println("\n--- LISTADO COMPLETO DE PRODUCTOS ---");

        List<Producto> productos = servicio.obtenerTodosLosProductos();

        if (productos.isEmpty()) {
            System.out.println("Inventario vacío.");
            return;
        }

        // Muestra la información
        for (Producto p : productos) {
            String categoriaNombre = (p.getCategoria() != null) ? p.getCategoria().getNombre() : "Sin Categoría";
            String altaTec = (p.getProductoAltaTecnologia() != null) ? " [ALTA TEC]" : "";
            
            System.out.printf("ID: %-4d | Nombre: %-30s | Modelo: %-15s | Categoría: %-15s %s%n",
                               p.getIdProducto(), p.getNombre(), p.getModelo(), categoriaNombre, altaTec);
        }
    }

    // 2. ACTUALIZAR PRODUCTO (UPDATE)
    private static void actualizarProducto(Scanner scanner, ProductoServicePort servicio) {
        System.out.println("\n--- ACTUALIZAR PRODUCTO ---");
        System.out.print("Ingrese el ID del producto a actualizar: ");
        int id = solicitarOpcion(scanner);
        
        Producto productoExistente = servicio.consultarProductoPorId(id);
        
        if (productoExistente == null) {
            System.out.println("Producto con ID " + id + " no encontrado.");
            return;
        }
        
        // Carga los datos actuales
        System.out.println("Datos actuales: " + productoExistente.getNombre() + " | " + productoExistente.getModelo());

        System.out.print("Nuevo Nombre (deje vacío para mantener '" + productoExistente.getNombre() + "'): ");
        String nuevoNombre = scanner.nextLine();
        
        System.out.print("Nuevo Modelo (deje vacío para mantener '" + productoExistente.getModelo() + "'): ");
        String nuevoModelo = scanner.nextLine();
        
        System.out.print("Nueva Descripción (deje vacío para mantener '" + productoExistente.getDescripcion() + "'): ");
        String nuevaDescripcion = scanner.nextLine();
        
        // Aplicar cambios al objeto existente
        if (!nuevoNombre.isEmpty()) {
            productoExistente.setNombre(nuevoNombre);
        }
        if (!nuevoModelo.isEmpty()) {
            productoExistente.setModelo(nuevoModelo);
        }
        if (!nuevaDescripcion.isEmpty()) {
            productoExistente.setDescripcion(nuevaDescripcion);
        }
         
        boolean actualizado = servicio.actualizarProducto(productoExistente);

        if (actualizado) {
            System.out.println("Producto con ID " + id + " actualizado exitosamente.");
        } else {
            System.out.println("Error al actualizar el producto.");
        }
    }

    // ELIMINAR PRODUCTO (DELETE)
    private static void eliminarProducto(Scanner scanner, ProductoServicePort servicio) {
        System.out.print("Ingrese el ID del producto a eliminar: ");
        int id = solicitarOpcion(scanner);
        
        boolean eliminado = servicio.eliminarProducto(id);

        if (eliminado) {
            System.out.println("Producto con ID " + id + " eliminado exitosamente.");
        } else {
            System.out.println("Error al eliminar el producto (puede que no exista o haya dependencias).");
        }
    }
}