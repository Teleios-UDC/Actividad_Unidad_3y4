package com.tienda.adapters;

import com.tienda.ports.AlquilerRepositoryPort;
import com.tienda.model.*; 
import com.tienda.util.DatabaseConfig;

import java.sql.*;
import java.time.LocalDateTime; 
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


//Adaptador de Salida para la gestión del Alquiler.

public class MySQLAlquilerRepositoryAdapter implements AlquilerRepositoryPort {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_DATE;
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    //Consultas SQL
    private static final String SQL_INSERT_TRANSACCION = 
        "INSERT INTO TRANSACCION (Precio, TipoPago) VALUES (?, ?)"; 
        
    private static final String SQL_INSERT_ALQUILER = 
        "INSERT INTO ALQUILER (CATEGORIA_ALQUILABLE_CATEGORIA_idCATEGORIA, PRODUCTO_idPRODUCTO, CLIENTE_idCLIENTE, InicioAlquier, " +
        "FinAlquiler, TRANSACCION_idTRANSACCION) " +
        "VALUES (?, ?, ?, ?, ?, ?)";
        
    private static final String SQL_SELECT_ALQUILER_BY_ID = 
        "SELECT a.idALQUILER, a.InicioAlquier, a.FinAlquiler, " + 
        "calq.CATEGORIA_idCATEGORIA AS CAlqId, " + 
        "c.idCATEGORIA AS CatId, c.Nombre AS CatNombre, " + 
        "p.idPRODUCTO AS ProdId, p.Nombre AS ProdNombre, " +  
        "cl.idCLIENTE AS CliId, cl.Nombre AS CliNombre, " +          
        "t.idTRANSACCION AS TransId, t.Precio AS MontoTotal, t.TipoPago AS Tipo " + 
        "FROM ALQUILER a " +
        "JOIN CATEGORIA_ALQUILABLE calq ON a.CATEGORIA_ALQUILABLE_CATEGORIA_idCATEGORIA = calq.CATEGORIA_idCATEGORIA " + 
        "JOIN CATEGORIA c ON calq.CATEGORIA_idCATEGORIA = c.idCATEGORIA " + 
        "JOIN PRODUCTO p ON a.PRODUCTO_idPRODUCTO = p.idPRODUCTO " +
        "JOIN CLIENTE cl ON a.CLIENTE_idCLIENTE = cl.idCLIENTE " +
        "JOIN TRANSACCION t ON a.TRANSACCION_idTRANSACCION = t.idTRANSACCION " +
        "WHERE a.idALQUILER = ?";
        
    private static final String SQL_SELECT_ALL_ALQUILERES = 
        SQL_SELECT_ALQUILER_BY_ID.substring(0, SQL_SELECT_ALQUILER_BY_ID.indexOf("WHERE"));

    private static final String SQL_UPDATE_TRANSACCION = 
        "UPDATE TRANSACCION SET Precio = ? WHERE idTRANSACCION = ?";

    private static final String SQL_UPDATE_ALQUILER = 
        "UPDATE ALQUILER SET InicioAlquier = ?, FinAlquiler = ? WHERE idALQUILER = ?";
        
    private static final String SQL_DELETE_ALQUILER = "DELETE FROM ALQUILER WHERE idALQUILER = ?";
    private static final String SQL_DELETE_TRANSACCION = "DELETE FROM TRANSACCION WHERE idTRANSACCION = ?";

    // MÉTODO SAVE (Transaccional)
    @Override
    public int save(Alquiler alquiler) {
        Connection conn = null;
        int idAlquilerGenerado = 0;
        int idTransaccionGenerado = 0;

        try {
            conn = DatabaseConfig.getConnection();
            conn.setAutoCommit(false); 

            //Insertar TRANSACCION y obtener ID
            Transaccion transaccion = alquiler.getTransaccion();
            try (PreparedStatement ps = conn.prepareStatement(SQL_INSERT_TRANSACCION, Statement.RETURN_GENERATED_KEYS)) {
                
                ps.setDouble(1, transaccion.getPrecio()); 
                ps.setString(2, transaccion.getTipoPago());
                
                ps.executeUpdate();
                
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {    
                        idTransaccionGenerado = rs.getInt(1);
                        transaccion.setIdTransaccion(idTransaccionGenerado); 
                    } else {
                        throw new SQLException("Fallo al obtener ID de Transacción. ¿idTRANSACCION tiene AUTO_INCREMENT?");
                    }
                }
            }

            //Insertar ALQUILER usando el ID de Transacción
            try (PreparedStatement ps = conn.prepareStatement(SQL_INSERT_ALQUILER, Statement.RETURN_GENERATED_KEYS)) {

                //Usamos el ID de la Categoría Base
                ps.setInt(1, alquiler.getCategoriaAlquilable().getCategoria().getIdCategoria()); // ID 1
                
                ps.setInt(2, alquiler.getProducto().getIdProducto());
                ps.setInt(3, alquiler.getCliente().getIdCliente());
                ps.setString(4, alquiler.getInicioAlquiler().format(DATE_FORMATTER)); 
                ps.setString(5, alquiler.getFinAlquiler().format(DATE_FORMATTER));
                ps.setInt(6, idTransaccionGenerado);
                ps.executeUpdate();
                
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        idAlquilerGenerado = rs.getInt(1);
                        alquiler.setIdAlquiler(idAlquilerGenerado);
                    } else {
                        throw new SQLException("Fallo al obtener ID de Alquiler.");
                    }
                }
            }
            
            conn.commit();
            System.out.println("LOG: Alquiler y Transacción guardados exitosamente. ID Alquiler: " + idAlquilerGenerado);

        } catch (SQLException e) {
            System.err.println("ERROR FATAL AL GUARDAR ALQUILER (ROLLBACK): " + e.getMessage());
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
        } finally {
            DatabaseConfig.closeConnection(conn); 
        }
        return idAlquilerGenerado;
    }


    //MÉTODOS FIND, UPDATE, DELETE Y MAPEO


    @Override
    public Alquiler findById(int id) {
        Alquiler alquiler = null;
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_ALQUILER_BY_ID)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    alquiler = mapResultSetToAlquiler(rs); 
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al consultar Alquiler por ID: " + e.getMessage());
            e.printStackTrace();
        }
        return alquiler;
    }

    @Override
    public List<Alquiler> findAll() {
        List<Alquiler> alquileres = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_ALL_ALQUILERES);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                alquileres.add(mapResultSetToAlquiler(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al consultar todos los Alquileres: " + e.getMessage());
            e.printStackTrace();
        }
        return alquileres;
    }
    
    @Override
    public boolean update(Alquiler alquiler) {
        boolean success = false;
        Connection conn = null;

        try {
            conn = DatabaseConfig.getConnection();
            conn.setAutoCommit(false); 

            // Actualizar ALQUILER
            try (PreparedStatement ps = conn.prepareStatement(SQL_UPDATE_ALQUILER)) {
                ps.setString(1, alquiler.getInicioAlquiler().format(DATE_FORMATTER));
                ps.setString(2, alquiler.getFinAlquiler().format(DATE_FORMATTER));
                ps.setInt(3, alquiler.getIdAlquiler());
                if (ps.executeUpdate() == 0) throw new SQLException("Fallo al actualizar ALQUILER.");
            }

            //Actualizar TRANSACCION (Precio)
            Transaccion t = alquiler.getTransaccion();
            if (t != null && t.getIdTransaccion() > 0) {
                try (PreparedStatement ps = conn.prepareStatement(SQL_UPDATE_TRANSACCION)) {
                    ps.setDouble(1, t.getPrecio()); 
                    ps.setInt(2, t.getIdTransaccion());
                    ps.executeUpdate();
                }
            }
            
            conn.commit();
            success = true;

        } catch (SQLException e) {
            System.err.println("ERROR AL ACTUALIZAR ALQUILER (ROLLBACK): " + e.getMessage());
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
        } finally {
            DatabaseConfig.closeConnection(conn);
        }
        return success;
    }
    
    @Override
    public boolean delete(int id) {
        boolean success = false;
        Connection conn = null;
        int idTransaccion = -1;

        try {
            conn = DatabaseConfig.getConnection();
            conn.setAutoCommit(false);

            //Obtener el ID de Transaccion asociado
            Alquiler a = findById(id); 
            if (a == null || a.getTransaccion() == null) {
                 conn.rollback();
                 return false;
            }
            idTransaccion = a.getTransaccion().getIdTransaccion();

            //Eliminar ALQUILER
            try (PreparedStatement ps = conn.prepareStatement(SQL_DELETE_ALQUILER)) {
                ps.setInt(1, id);
                if (ps.executeUpdate() == 0) throw new SQLException("Fallo al eliminar ALQUILER.");
            }

            //Eliminar TRANSACCION
            try (PreparedStatement ps = conn.prepareStatement(SQL_DELETE_TRANSACCION)) {
                ps.setInt(1, idTransaccion);
                ps.executeUpdate();
            }

            conn.commit();
            success = true;
        } catch (SQLException e) {
            System.err.println("ERROR AL ELIMINAR ALQUILER (ROLLBACK): " + e.getMessage());
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
        } finally {
            DatabaseConfig.closeConnection(conn);
        }
        return success;
    }

    private Alquiler mapResultSetToAlquiler(ResultSet rs) throws SQLException {
        
        
        Categoria categoriaBase = new Categoria(
            rs.getInt("CatId"), 
            rs.getString("CatNombre")
        );
        CategoriaAlquilable catAlq = new CategoriaAlquilable(
            rs.getInt("CAlqId"),
            categoriaBase.getIdCategoria()
        );
        catAlq.setCategoria(categoriaBase); 


        Producto producto = new Producto();
        producto.setIdProducto(rs.getInt("ProdId"));
        producto.setNombre(rs.getString("ProdNombre"));
        
        Cliente cliente = new Cliente();
        cliente.setIdCliente(rs.getInt("CliId"));
        cliente.setNombre(rs.getString("CliNombre"));
        
        Transaccion transaccion = new Transaccion(
            rs.getInt("TransId"), 
            LocalDateTime.now(), 
            rs.getDouble("MontoTotal"), 
            rs.getString("Tipo")
        );
        transaccion.setPrecio(rs.getDouble("MontoTotal")); 

        //Mapear Alquiler
        Alquiler alquiler = new Alquiler(
            rs.getInt("idALQUILER"),
            rs.getDate("InicioAlquier").toLocalDate(), 
            rs.getDate("FinAlquiler").toLocalDate(),
            catAlq.getIdCategoriaAlquilable(),
            producto.getIdProducto(),
            cliente.getIdCliente(),
            transaccion.getIdTransaccion()
        );
        
        //Asignar objetos de relación 
        alquiler.setCategoriaAlquilable(catAlq);
        alquiler.setProducto(producto);
        alquiler.setCliente(cliente);
        alquiler.setTransaccion(transaccion);
        return alquiler;
    }
}