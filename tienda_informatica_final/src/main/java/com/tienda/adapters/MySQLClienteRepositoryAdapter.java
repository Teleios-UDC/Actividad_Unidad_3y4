package com.tienda.adapters;

import com.tienda.ports.ClienteRepositoryPort;
import com.tienda.model.Cliente;
import com.tienda.model.Direccion;
import com.tienda.model.Telefono;

import com.tienda.util.DatabaseConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


//Adaptador de Salida: Implementa la lógica JDBC para el Flujo 2 (Cliente).

public class MySQLClienteRepositoryAdapter implements ClienteRepositoryPort {

    // --- QUERIES SQL (AJUSTADAS PARA 1:1) ---
    private static final String SQL_INSERT_DIRECCION = 
        "INSERT INTO DIRECCION (Ciudad, Barrio, Calle) VALUES (?, ?, ?)";
        
    private static final String SQL_INSERT_TELEFONO = 
        "INSERT INTO Telefono (Telefono, TipoTelefono) VALUES (?, ?)"; 
        
    // La query del Cliente requiere 4 parámetros: Nombre, Apellido, FK_DIRECCION, FK_TELEFONO
    private static final String SQL_INSERT_CLIENTE = 
        "INSERT INTO CLIENTE (Nombre, Apellido, DIRECCION_IdDIRECCION, Telefono_idTelefono) VALUES (?, ?, ?, ?)"; 
        
    private static final String SQL_SELECT_CLIENTE_BY_ID = 
        "SELECT c.idCLIENTE, c.Nombre, c.Apellido, " +
        "d.IdDIRECCION, d.Ciudad, d.Barrio, d.Calle, " +
        "t.idTelefono, t.Telefono, t.TipoTelefono " +
        "FROM CLIENTE c " +
        "JOIN DIRECCION d ON c.DIRECCION_IdDIRECCION = d.IdDIRECCION " +
        "JOIN Telefono t ON c.Telefono_idTelefono = t.idTelefono " + 
        "WHERE c.idCLIENTE = ?";

    private static final String SQL_SELECT_CLIENTES_BY_CITY =
        "SELECT c.idCLIENTE, c.Nombre, c.Apellido, " +
        "d.IdDIRECCION, d.Ciudad, d.Barrio, d.Calle, " +
        "t.idTelefono, t.Telefono, t.TipoTelefono " +
        "FROM CLIENTE c " +
        "JOIN DIRECCION d ON c.DIRECCION_IdDIRECCION = d.IdDIRECCION " +
        "JOIN Telefono t ON c.Telefono_idTelefono = t.idTelefono " + 
        "WHERE d.Ciudad = ?";
        
    private static final String SQL_SELECT_ALL_CLIENTES = 
        "SELECT c.idCLIENTE, c.Nombre, c.Apellido, " +
        "d.IdDIRECCION, d.Ciudad, d.Barrio, d.Calle, " +
        "t.idTelefono, t.Telefono, t.TipoTelefono " +
        "FROM CLIENTE c " +
        "JOIN DIRECCION d ON c.DIRECCION_IdDIRECCION = d.IdDIRECCION " +
        "JOIN Telefono t ON c.Telefono_idTelefono = t.idTelefono";

    private static final String SQL_UPDATE_CLIENTE = 
        "UPDATE CLIENTE SET Nombre = ?, Apellido = ? WHERE idCLIENTE = ?";
    private static final String SQL_UPDATE_DIRECCION = 
        "UPDATE DIRECCION SET Ciudad = ?, Barrio = ?, Calle = ? WHERE IdDIRECCION = ?";
    private static final String SQL_UPDATE_TELEFONO = 
        "UPDATE Telefono SET Telefono = ?, TipoTelefono = ? WHERE idTelefono = ?";
        
    private static final String SQL_DELETE_CLIENTE = 
        "DELETE FROM CLIENTE WHERE idCLIENTE = ?";

        public static void closeConnection(Connection conn) {
    if (conn != null) {
        try {
            conn.close(); // Esta es la línea de corrección
        } catch (SQLException e) {
            System.err.println("Error al cerrar la conexión: " + e.getMessage());
            e.printStackTrace();
        }
    }
}



    // MÉTODO SAVE
    @Override
    public int save(Cliente cliente) {
        Connection conn = null;
        int idDireccionGenerado = 0;
        int idTelefonoGenerado = 0;
        int idClienteGenerado = 0;

        try {
            conn = DatabaseConfig.getConnection();
            conn.setAutoCommit(false); // Iniciar Transacción

            // 1. Insertar DIRECCION
            try (PreparedStatement ps = conn.prepareStatement(SQL_INSERT_DIRECCION, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, cliente.getDireccion().getCiudad());
                ps.setString(2, cliente.getDireccion().getBarrio());
                ps.setString(3, cliente.getDireccion().getCalle());
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        idDireccionGenerado = rs.getInt(1);
                        cliente.getDireccion().setIdDIRECCION(idDireccionGenerado);
                    }
                }
            }
            
            // 2. Insertar TELEFONO
            try (PreparedStatement ps = conn.prepareStatement(SQL_INSERT_TELEFONO, Statement.RETURN_GENERATED_KEYS)) {
                // ⭐ CLAVE: Usamos setLong para el número de teléfono (BIGINT en BD)
                ps.setLong(1, cliente.getTelefono().getTelefono()); 
                ps.setString(2, cliente.getTelefono().getTipoTelefono());
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        idTelefonoGenerado = rs.getInt(1);
                        cliente.getTelefono().setIdTelefono(idTelefonoGenerado);
                    }
                }
            }

            // 3. Insertar CLIENTE
            try (PreparedStatement ps = conn.prepareStatement(SQL_INSERT_CLIENTE, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, cliente.getNombre());        
                ps.setString(2, cliente.getApellido());       
                ps.setInt(3, idDireccionGenerado);            
                ps.setInt(4, idTelefonoGenerado);             
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        idClienteGenerado = rs.getInt(1);
                        cliente.setIdCliente(idClienteGenerado);
                    }
                }
            }
            
            conn.commit();
            System.out.println("LOG: Cliente, Dirección y Teléfono guardados exitosamente. ID Cliente: " + idClienteGenerado);

        } catch (SQLException e) {
            System.err.println("ERROR FATAL AL GUARDAR CLIENTE (ROLLBACK): " + e.getMessage());
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
        } finally {
            DatabaseConfig.closeConnection(conn);
        }
        return idClienteGenerado;
    }

 
    // MÉTODO FIND BY ID
    @Override
    public Cliente findById(int id) {
        Cliente cliente = null;
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_CLIENTE_BY_ID)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    cliente = mapResultSetToCliente(rs); 
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al consultar Cliente por ID: " + e.getMessage());
            e.printStackTrace();
        }
        return cliente;
    }


    // MÉTODO FIND ALL
    @Override
    public List<Cliente> findAll() {
        List<Cliente> clientes = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_ALL_CLIENTES);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                clientes.add(mapResultSetToCliente(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al consultar todos los Clientes: " + e.getMessage());
            e.printStackTrace();
        }
        return clientes;
    }


    // MÉTODO UPDATE (Transacción: Actualiza Cliente, Dirección y Teléfono)
    @Override
    public boolean update(Cliente cliente) {
        Connection conn = null;
        boolean success = false;
        try {
            conn = DatabaseConfig.getConnection();
            conn.setAutoCommit(false);

            // 1. Actualizar CLIENTE
            try (PreparedStatement ps = conn.prepareStatement(SQL_UPDATE_CLIENTE)) {
                ps.setString(1, cliente.getNombre());
                ps.setString(2, cliente.getApellido());
                ps.setInt(3, cliente.getIdCliente());
                if (ps.executeUpdate() == 0) throw new SQLException("Fallo al actualizar CLIENTE.");
            }

            // 2. Actualizar DIRECCION
            if (cliente.getDireccion() != null && cliente.getDireccion().getIdDIRECCION() > 0) {
                try (PreparedStatement ps = conn.prepareStatement(SQL_UPDATE_DIRECCION)) {
                    ps.setString(1, cliente.getDireccion().getCiudad());
                    ps.setString(2, cliente.getDireccion().getBarrio());
                    ps.setString(3, cliente.getDireccion().getCalle());
                    ps.setInt(4, cliente.getDireccion().getIdDIRECCION());
                    if (ps.executeUpdate() == 0) throw new SQLException("Fallo al actualizar DIRECCION.");
                }
            }
            
            // 3. Actualizar TELEFONO
            if (cliente.getTelefono() != null && cliente.getTelefono().getIdTelefono() > 0) {
                 try (PreparedStatement ps = conn.prepareStatement(SQL_UPDATE_TELEFONO)) {
                    ps.setLong(1, cliente.getTelefono().getTelefono()); // setLong
                    ps.setString(2, cliente.getTelefono().getTipoTelefono());
                    ps.setInt(3, cliente.getTelefono().getIdTelefono());
                    if (ps.executeUpdate() == 0) throw new SQLException("Fallo al actualizar TELEFONO.");
                }
            }
            
            conn.commit();
            success = true;

        } catch (SQLException e) {
            System.err.println("ERROR AL ACTUALIZAR CLIENTE (ROLLBACK): " + e.getMessage());
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
        } finally {
            DatabaseConfig.closeConnection(conn);
        }
        return success;
    }
    
    // MÉTODO DELETE
    @Override
    public boolean delete(int id) {

        boolean success = false;
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_DELETE_CLIENTE)) {

            ps.setInt(1, id);
            success = ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar Cliente: " + e.getMessage());
            e.printStackTrace();
        }
        return success;
    }


    // Mapeo de Cliente, Dirección y Teléfono)

    private Cliente mapResultSetToCliente(ResultSet rs) throws SQLException {
        
        // Mapear Direccion
        Direccion direccion = new Direccion(
            rs.getInt("IdDIRECCION"),
            rs.getString("Ciudad"),
            rs.getString("Barrio"),
            rs.getString("Calle")
        );
        
        // Mapear Telefono
        Telefono telefono = new Telefono(
            rs.getInt("idTelefono"),
            rs.getLong("Telefono"),
            rs.getString("TipoTelefono")
        );

        // Mapear Cliente
        return new Cliente(
            rs.getInt("idCLIENTE"),
            rs.getString("Nombre"),
            rs.getString("Apellido"),
            direccion,
            telefono
        );
    }
}