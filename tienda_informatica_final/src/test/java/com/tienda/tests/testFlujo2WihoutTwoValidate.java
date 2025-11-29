package com.tienda.tests;

import com.tienda.adapters.MySQLClienteRepositoryAdapter;
import com.tienda.services.ClienteService;
import com.tienda.ports.ClienteRepositoryPort;
import com.tienda.ports.ClienteServicePort;

import com.tienda.model.Cliente;
import com.tienda.model.Direccion;
import com.tienda.model.Telefono;

import java.util.List;
import java.util.Scanner;


//Adaptador de Entrada: Simulacion de gestion de clientes Flujo 2 (CRUD)
public class testFlujo2WihoutTwoValidate {
    private static int solicitarOpcion(Scanner scanner) {
        System.out.print(">> ");
        if (scanner.hasNextInt()) {
            int opcion = scanner.nextInt();
            scanner.nextLine();
            return opcion;
        } else {
            System.out.println("Entrada inválida. Debe ser un número entero.");
            scanner.nextLine();
            return 0; 
        }
    }
    

    private static long solicitarNumeroTelefono(Scanner scanner) {
        System.out.print("Número de Teléfono (solo dígitos, ej: 123456789): ");
        if (scanner.hasNextLong()) {
            long telefono = scanner.nextLong();
            scanner.nextLine();
            return telefono;
        } else {
            System.out.println("Entrada inválida. El teléfono debe ser un número entero.");
            scanner.nextLine();
            return 0;
        }
    }

    private static void mostrarMenu() {
        System.out.println("\n===== FLÚJO 2: GESTIÓN DE CLIENTE (CRUD) =====");
        System.out.println("1. Registrar Nuevo Cliente");
        System.out.println("2. Actualizar Cliente y Datos Relacionados");
        System.out.println("3. Consultar Cliente por ID");
        System.out.println("4. Listar Todos los Clientes");
        System.out.println("5. Eliminar Cliente por ID");
        System.out.println("6. Salir");
        System.out.print("Seleccione una opción: ");
    }

    public static void main(String[] args) {
        ClienteRepositoryPort adaptadorBD = new MySQLClienteRepositoryAdapter();
        ClienteServicePort servicioCliente = new ClienteService(adaptadorBD);

        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            mostrarMenu();
            opcion = solicitarOpcion(scanner); 

            try {
                switch (opcion) {
                    case 1:
                        registrarCliente(scanner, servicioCliente);
                        break;
                    case 2:
                        actualizarCliente(scanner, servicioCliente);
                        break;
                    case 3:
                        consultarClientePorId(scanner, servicioCliente);
                        break;
                    case 4:
                        listarTodosLosClientes(servicioCliente);
                        break;
                    case 5:
                        eliminarCliente(scanner, servicioCliente);
                        break;
                    case 6:
                        System.out.println("Saliendo del Flujo 2. ¡Adiós!");
                        break;
                    default:
                        System.out.println("Opción no válida. Intente de nuevo.");
                }
            } catch (IllegalArgumentException e) {
                System.err.println("ERROR DE VALIDACIÓN: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("ERROR INESPERADO EN LA OPERACIÓN: " + e.getMessage());
                if (scanner.hasNextLine()) scanner.nextLine(); 
            }
        } while (opcion != 6);
        
        scanner.close();
    }

    // 1. REGISTRAR CLIENTE (CREATE)
    private static void registrarCliente(Scanner scanner, ClienteServicePort servicio) {
        System.out.println("\n--- REGISTRAR NUEVO CLIENTE ---");
        String nombre = solicitarCampoRequerido(scanner, "Nombre del Cliente");
        String apellido = solicitarCampoRequerido(scanner, "Apellido del Cliente");

        //DIRECCIÓN
        System.out.println("\n--- Datos de Dirección ---");
        String ciudad = solicitarCampoRequerido(scanner, "Ciudad");
        String barrio = solicitarCampoRequerido(scanner, "Barrio");
        String calle = solicitarCampoRequerido(scanner, "Calle/Carrera");

        Direccion nuevaDireccion = new Direccion(0, ciudad, barrio, calle);

        // TELÉFONO
        System.out.println("\n--- Datos de Teléfono ---");
        Long numeroTelefono = solicitarNumeroTelefono(scanner);
        
        System.out.print("Tipo de Teléfono (Ej: Fijo, Móvil): ");
        String tipoTelefono = solicitarCampoRequerido(scanner, "Tipo de Teléfono");

        Telefono nuevoTelefono = new Telefono(0, numeroTelefono, tipoTelefono);

        // ENSAMBLAR Y GUARDAR
        Cliente nuevoCliente = new Cliente(0, nombre, apellido, nuevaDireccion, nuevoTelefono);

        int idGenerado = servicio.registrarNuevoCliente(nuevoCliente);

        if (idGenerado > 0) {
            System.out.println("✅ Cliente, Dirección y Teléfono registrados exitosamente. ID Cliente: " + idGenerado);
            System.out.println("   ID Dirección: " + nuevoCliente.getDireccion().getIdDIRECCION());
            System.out.println("   ID Teléfono: " + nuevoCliente.getTelefono().getIdTelefono());
        } else {
            System.out.println("Error al registrar el cliente.");
        }
    }

    private static String solicitarCampoRequerido(Scanner scanner, String mensaje) {
        String entrada;
        do {
            System.out.print(mensaje + ": ");
            entrada = scanner.nextLine().trim();
            if (entrada.isEmpty()) {
                System.out.println("Este campo es obligatorio. Por favor, ingrese un valor.");
            }
        } while (entrada.isEmpty());
        return entrada;
    }


    //CONSULTAR CLIENTE
    private static void consultarClientePorId(Scanner scanner, ClienteServicePort servicio) {
        System.out.print("Ingrese el ID del cliente a consultar: ");
        int id = solicitarOpcion(scanner);

        Cliente cliente = servicio.consultarClientePorId(id);

        if (cliente != null) {
            System.out.println("\n--- DETALLES DEL CLIENTE ID: " + cliente.getIdCliente() + " ---");
            System.out.println("Nombre Completo: " + cliente.getNombre() + " " + cliente.getApellido());
            
            // Dirección
            Direccion d = cliente.getDireccion();
            System.out.println("\n[DIRECCIÓN ID: " + d.getIdDIRECCION() + "]");
            System.out.println("  Ciudad: " + d.getCiudad());
            System.out.println("  Barrio: " + d.getBarrio());
            System.out.println("  Calle: " + d.getCalle());
            
            // Teléfono
            Telefono t = cliente.getTelefono();
            System.out.println("\n[TELÉFONO ID: " + t.getIdTelefono() + "]");
            System.out.println("  Número: " + t.getTelefono());
            System.out.println("  Tipo: " + t.getTipoTelefono());

        } else {
            System.out.println("Cliente con ID " + id + " no encontrado.");
        }
    }
    

    // 4. LISTAR CLIENTES
    private static void listarTodosLosClientes(ClienteServicePort servicio) {
        System.out.println("\n--- LISTADO COMPLETO DE CLIENTES ---");

        List<Cliente> clientes = servicio.obtenerTodosLosClientes();

        if (clientes.isEmpty()) {
            System.out.println("No hay clientes registrados.");
            return;
        }

        System.out.printf("%-5s | %-20s | %-15s | %-30s%n", "ID", "NOMBRE COMPLETO", "TELÉFONO", "DIRECCIÓN (Ciudad/Barrio)");
        System.out.println("----------------------------------------------------------------------------------");

        for (Cliente c : clientes) {
            String nombreCompleto = c.getNombre() + " " + c.getApellido();
            String telefono = (c.getTelefono() != null) ? String.valueOf(c.getTelefono().getTelefono()) : "N/A";
            String direccionResumen = (c.getDireccion() != null) ? c.getDireccion().getCiudad() + "/" + c.getDireccion().getBarrio() : "N/A";
            
            System.out.printf("%-5d | %-20s | %-15s | %-30s%n",
                               c.getIdCliente(), nombreCompleto, telefono, direccionResumen);
        }
    }

    // 2. ACTUALIZAR CLIENTE
    private static void actualizarCliente(Scanner scanner, ClienteServicePort servicio) {
        System.out.println("\n--- ACTUALIZAR CLIENTE Y DATOS RELACIONADOS ---");
        System.out.print("Ingrese el ID del cliente a actualizar: ");
        int id = solicitarOpcion(scanner);

        Cliente clienteExistente = servicio.consultarClientePorId(id);

        if (clienteExistente == null) {
            System.out.println("Cliente con ID " + id + " no encontrado.");
            return;
        }
        
        // Cliente (Nombre/Apellido)
        System.out.println("\n[CLIENTE] Actual: " + clienteExistente.getNombre() + " " + clienteExistente.getApellido());
        System.out.print("Nuevo Nombre (deje vacío para mantener): ");
        String nuevoNombre = scanner.nextLine();
        if (!nuevoNombre.isEmpty()) clienteExistente.setNombre(nuevoNombre);
        
        System.out.print("Nuevo Apellido (deje vacío para mantener): ");
        String nuevoApellido = scanner.nextLine();
        if (!nuevoApellido.isEmpty()) clienteExistente.setApellido(nuevoApellido);

        // Dirección
        Direccion d = clienteExistente.getDireccion();
        System.out.println("\n[DIRECCIÓN] Actual: " + d.getCiudad() + ", " + d.getCalle());
        System.out.print("Nueva Ciudad (deje vacío para mantener): ");
        String nuevaCiudad = scanner.nextLine();
        if (!nuevaCiudad.isEmpty()) d.setCiudad(nuevaCiudad);

        System.out.print("Nuevo Barrio (deje vacío para mantener): ");
        String nuevoBarrio = scanner.nextLine();
        if (!nuevoBarrio.isEmpty()) d.setBarrio(nuevoBarrio);
        
        System.out.print("Nueva Calle (deje vacío para mantener): ");
        String nuevaCalle = scanner.nextLine();
        if (!nuevaCalle.isEmpty()) d.setCalle(nuevaCalle);

        // Teléfono
        Telefono t = clienteExistente.getTelefono();
        System.out.println("\n[TELÉFONO] Actual: " + t.getTelefono() + " (" + t.getTipoTelefono() + ")");
        System.out.print("Nuevo Tipo de Teléfono (deje vacío para mantener): ");
        String nuevoTipo = scanner.nextLine();
        if (!nuevoTipo.isEmpty()) t.setTipoTelefono(nuevoTipo);
        
        System.out.print("Desea cambiar el Número? (S/N): ");
        String cambiarTel = scanner.nextLine().trim().toUpperCase();
        if (cambiarTel.equals("S")) {
            long nuevoNumero = solicitarNumeroTelefono(scanner);
            t.setTelefono(nuevoNumero);
        }
        // Guardar Actualizaciones
        boolean actualizado = servicio.actualizarDatosCliente(clienteExistente);

        if (actualizado) {
            System.out.println("Cliente con ID " + id + " actualizado exitosamente (incluyendo Dir y Tel).");
        } else {
            System.out.println("Error al actualizar el cliente. Revise los logs.");
        }
    }

    // 5. ELIMINAR CLIENTE
    private static void eliminarCliente(Scanner scanner, ClienteServicePort servicio) {
        System.out.print("Ingrese el ID del cliente a eliminar: ");
        int id = solicitarOpcion(scanner);

        boolean eliminado = servicio.eliminarCliente(id);

        if (eliminado) {
            System.out.println("Cliente con ID " + id + " eliminado exitosamente.");
        } else {
            System.out.println("Error al eliminar el cliente (puede que no exista o haya dependencias sin cascada).");
        }
    }
}