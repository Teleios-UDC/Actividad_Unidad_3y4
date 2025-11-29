package com.tienda.adapters;

import com.tienda.ports.ProveedorRepositoryPort;
import com.tienda.model.Proveedor;
import com.tienda.model.Direccion;
import com.tienda.util.DatabaseConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLProveedorRepositoryAdapter implements ProveedorRepositoryPort {

    //Consultas SQL
    private static final String SQL_INSERT_DIRECCION = 
        "INSERT INTO DIRECCION (Ciudad, Barrio, Calle) VALUES (?, ?, ?)";

    private static final String SQL_INSERT_PROVEEDOR = 
        "INSERT INTO PROVEEDOR (NIF, DIRECCION_IdDIRECCION) VALUES (?, ?)"; 
        
    private static final String SQL_SELECT_PROVEEDOR_BY_ID = 
        "SELECT p.idPROVEEDOR, p.NIF, " +
        "d.IdDIRECCION, d.Ciudad, d.Barrio, d.Calle " +
        "FROM PROVEEDOR p " +
        "JOIN DIRECCION d ON p.DIRECCION_IdDIRECCION = d.IdDIRECCION " +
        "WHERE p.idPROVEEDOR = ?";
        
    private static final String SQL_SELECT_ALL_PROVEEDORES = 
        "SELECT p.idPROVEEDOR, p.NIF, " +
        "d.IdDIRECCION, d.Ciudad, d.Barrio, d.Calle " +
        "FROM PROVEEDOR p " +
        "JOIN DIRECCION d ON p.DIRECCION_IdDIRECCION = d.IdDIRECCION";

    // Solo NIF en el UPDATE
    private static final String SQL_UPDATE_PROVEEDOR = 
        "UPDATE PROVEEDOR SET NIF = ? WHERE idPROVEEDOR = ?";
    private static final String SQL_UPDATE_DIRECCION = 
        "UPDATE DIRECCION SET Ciudad = ?, Barrio = ?, Calle = ? WHERE IdDIRECCION = ?";
        
    private static final String SQL_DELETE_PROVEEDOR = 
        "DELETE FROM PROVEEDOR WHERE idPROVEEDOR = ?";


    // MÉTODO SAVE
    @Override
    public int save(Proveedor proveedor) {
        Connection conn = null;
        int idDireccionGenerado = 0;
        int idProveedorGenerado = 0;

        try {
            conn = DatabaseConfig.getConnection();
            conn.setAutoCommit(false); 

            //Insertar DIRECCION
            try (PreparedStatement ps = conn.prepareStatement(SQL_INSERT_DIRECCION, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, proveedor.getDireccion().getCiudad());
                ps.setString(2, proveedor.getDireccion().getBarrio());
                ps.setString(3, proveedor.getDireccion().getCalle());
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        idDireccionGenerado = rs.getInt(1);
                        proveedor.getDireccion().setIdDIRECCION(idDireccionGenerado);
                    }
                }
            }

            //Insertar PROVEEDOR
            try (PreparedStatement ps = conn.prepareStatement(SQL_INSERT_PROVEEDOR, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, proveedor.getNif());        
                ps.setInt(2, idDireccionGenerado);
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        idProveedorGenerado = rs.getInt(1);
                        proveedor.setIdProveedor(idProveedorGenerado);
                    }
                }
            }
            
            conn.commit();
            System.out.println("LOG: Dirección y Proveedor guardados exitosamente. ID Proveedor: " + idProveedorGenerado);

        } catch (SQLException e) {
            System.err.println("ERROR FATAL AL GUARDAR PROVEEDOR (ROLLBACK): " + e.getMessage());
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
        } finally {
            DatabaseConfig.closeConnection(conn); 
        }
        return idProveedorGenerado;
    }


    // MÉTODO UPDATE

    @Override
    public boolean update(Proveedor proveedor) {
        Connection conn = null;
        boolean success = false;
        try {
            conn = DatabaseConfig.getConnection();
            conn.setAutoCommit(false);

            // 1. Actualizar PROVEEDOR
            try (PreparedStatement ps = conn.prepareStatement(SQL_UPDATE_PROVEEDOR)) {
                ps.setString(1, proveedor.getNif());

                ps.setInt(2, proveedor.getIdProveedor());
                if (ps.executeUpdate() == 0) throw new SQLException("Fallo al actualizar PROVEEDOR.");
            }

            // 2. Actualizar DIRECCION
            if (proveedor.getDireccion() != null && proveedor.getDireccion().getIdDIRECCION() > 0) {
                try (PreparedStatement ps = conn.prepareStatement(SQL_UPDATE_DIRECCION)) {
                    ps.setString(1, proveedor.getDireccion().getCiudad());
                    ps.setString(2, proveedor.getDireccion().getBarrio());
                    ps.setString(3, proveedor.getDireccion().getCalle());
                    ps.setInt(4, proveedor.getDireccion().getIdDIRECCION());
                    if (ps.executeUpdate() == 0) throw new SQLException("Fallo al actualizar DIRECCION.");
                }
            }
            
            conn.commit();
            success = true;

        } catch (SQLException e) {
            System.err.println("ERROR AL ACTUALIZAR PROVEEDOR (ROLLBACK): " + e.getMessage());
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
        } finally {
            DatabaseConfig.closeConnection(conn);
        }
        return success;
    }
    


    // Mapeo (SIN Nombre)

    private Proveedor mapResultSetToProveedor(ResultSet rs) throws SQLException {
        
        // Mapear Direccion
        Direccion direccion = new Direccion(
            rs.getInt("IdDIRECCION"),
            rs.getString("Ciudad"),
            rs.getString("Barrio"),
            rs.getString("Calle")
        );
        
        // Mapear Proveedor
        return new Proveedor(
            rs.getInt("idPROVEEDOR"),
            rs.getString("NIF"),
            direccion
        );
    }

    @Override
    public List<Proveedor> findAll() {
        List<Proveedor> proveedores = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_ALL_PROVEEDORES);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                proveedores.add(mapResultSetToProveedor(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al consultar todos los Proveedores: " + e.getMessage());
            e.printStackTrace();
        }
        return proveedores;
    }

    @Override
    public Proveedor findById(int id) {
        Proveedor proveedor = null;
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_PROVEEDOR_BY_ID)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    proveedor = mapResultSetToProveedor(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al consultar Proveedor por ID: " + e.getMessage());
            e.printStackTrace();
        }
        return proveedor;
    }

    @Override
    public boolean delete(int id) {
        boolean success = false;
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_DELETE_PROVEEDOR)) {

            ps.setInt(1, id);
            success = ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar Proveedor: " + e.getMessage());
            e.printStackTrace();
        }
        return success;
    }
}