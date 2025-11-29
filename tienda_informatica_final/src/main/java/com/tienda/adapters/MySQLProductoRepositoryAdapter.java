package com.tienda.adapters;

import com.tienda.ports.ProductoRepositoryPort;
import com.tienda.model.Producto;
import com.tienda.model.Categoria;
import com.tienda.model.ProductoAltaTecnologia;
import com.tienda.model.CategoriaAltaTecnologia;
import com.tienda.util.DatabaseConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


//Adaptador de Salida: Este archivo Implementa la lógica JDBC para Producto.

public class MySQLProductoRepositoryAdapter implements ProductoRepositoryPort {

    //Consultas SQL
    private static final String SQL_INSERT_PRODUCTO = 
        "INSERT INTO PRODUCTO (CATEGORIA_idCATEGORIA, Nombre, Modelo, Descripcion) VALUES (?, ?, ?, ?)";
    private static final String SQL_INSERT_ALTA_TEC = 
        "INSERT INTO PRODUCTO_ALTA_TECNOLOGIA (PRODUCTO_idPRODUCTO, CATEGORIA_ALTA_TECNOLOGIA_CATEGORIA_idCATEGORIA, PaisOrigen, " +
        "FechaFabricacion) VALUES (?, ?, ?, ?)";
    private static final String SQL_SELECT_PRODUCTO_BY_ID = 
        "SELECT p.idPRODUCTO, p.Nombre, p.Modelo, p.Descripcion, c.idCATEGORIA, c.Nombre as NombreCategoria, " +
        "pat.IdPRODUCTO_ALTA_TECNOLOGIA, pat.PaisOrigen, pat.FechaFabricacion " +
        "FROM PRODUCTO p " +
        "JOIN CATEGORIA c ON p.CATEGORIA_idCATEGORIA = c.idCATEGORIA " +
        "LEFT JOIN PRODUCTO_ALTA_TECNOLOGIA pat ON p.idPRODUCTO = pat.PRODUCTO_idPRODUCTO " +
        "WHERE p.idPRODUCTO = ?";
    private static final String SQL_UPDATE_PRODUCTO = 
        "UPDATE PRODUCTO SET Nombre = ?, Modelo = ?, Descripcion = ?, CATEGORIA_idCATEGORIA = ? WHERE idPRODUCTO = ?";
    private static final String SQL_DELETE_PRODUCTO = 
        "DELETE FROM PRODUCTO WHERE idPRODUCTO = ?";
    private static final String SQL_SELECT_ALL_PRODUCTS = 
        "SELECT p.idPRODUCTO, p.Nombre, p.Modelo, p.Descripcion, c.idCATEGORIA, c.Nombre as NombreCategoria, " +
        "pat.IdPRODUCTO_ALTA_TECNOLOGIA, pat.PaisOrigen, pat.FechaFabricacion " +
        "FROM PRODUCTO p " +
        "JOIN CATEGORIA c ON p.CATEGORIA_idCATEGORIA = c.idCATEGORIA " +
        "LEFT JOIN PRODUCTO_ALTA_TECNOLOGIA pat ON p.idPRODUCTO = pat.PRODUCTO_idPRODUCTO";



    //MÉTODO SAVE (Registro)
    @Override
    public int save(Producto producto) {
        Connection conn = null;
        int idGenerado = 0;

        try {
            conn = DatabaseConfig.getConnection();
            conn.setAutoCommit(false);

            //Insertar PRODUCTO
            try (PreparedStatement ps = conn.prepareStatement(SQL_INSERT_PRODUCTO, Statement.RETURN_GENERATED_KEYS)) {
                
                //Ya que tenemos la categoría como objeto, extraemos su ID
                int idCategoria = producto.getCategoria().getIdCategoria(); 

                ps.setInt(1, idCategoria);
                ps.setString(2, producto.getNombre());
                ps.setString(3, producto.getModelo());
                ps.setString(4, producto.getDescripcion());

                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        idGenerado = rs.getInt(1);
                    }
                }
            }

            // 2. Insertar PRODUCTO_ALTA_TECNOLOGIA (si aplica)
            if (idGenerado > 0 && producto.getProductoAltaTecnologia() != null) {
                ProductoAltaTecnologia altaTec = (ProductoAltaTecnologia) producto.getProductoAltaTecnologia();
                
                try (PreparedStatement psAltaTec = conn.prepareStatement(SQL_INSERT_ALTA_TEC)) {
                    
                    // PRODUCTO_idPRODUCTO (FK a Producto)
                    psAltaTec.setInt(1, idGenerado); 
                    
                    // CATEGORIA_ALTA_TECNOLOGIA_CATEGORIA_idCATEGORIA (FK a CategoriaBase)
                    psAltaTec.setInt(2, producto.getCategoria().getIdCategoria()); 
                    
                    psAltaTec.setString(3, altaTec.getPaisOrigen());
                    psAltaTec.setDate(4, Date.valueOf(altaTec.getFechaFabricacion()));

                    psAltaTec.executeUpdate();
                }
            }
            
            conn.commit();
            System.out.println("LOG: Producto y entidades asociadas guardados exitosamente. ID: " + idGenerado);

        } catch (SQLException e) {
            System.err.println("ERROR FATAL AL GUARDAR PRODUCTO (ROLLBACK): " + e.getMessage());
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
        }
        return idGenerado;
    }



    // MÉTODO FIND BY ID (Lectura Completa)
    @Override
    public Producto findById(int id) {
        Producto producto = null;
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_PRODUCTO_BY_ID)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    producto = mapResultSetToProducto(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al consultar Producto por ID: " + e.getMessage());
            e.printStackTrace();
        }
        return producto;
    }


    // MÉTODO FIND ALL (Lectura de Lista)
    @Override
    public List<Producto> findAll() {
        List<Producto> productos = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_ALL_PRODUCTS);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                productos.add(mapResultSetToProducto(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al consultar todos los Productos: " + e.getMessage());
            e.printStackTrace();
        }
        return productos;
    }


    // MÉTODO UPDATE
    @Override
    public boolean update(Producto producto) {
        boolean success = false;
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE_PRODUCTO)) {

            ps.setString(1, producto.getNombre());
            ps.setString(2, producto.getModelo());
            ps.setString(3, producto.getDescripcion());
            ps.setInt(4, producto.getCategoria().getIdCategoria());
            ps.setInt(5, producto.getIdProducto());

            success = ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar Producto: " + e.getMessage());
            e.printStackTrace();
        }
        return success;
    }


    // MÉTODO DELETE
    @Override
    public boolean delete(int id) {
        boolean success = false;
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_DELETE_PRODUCTO)) {

            ps.setInt(1, id);
            success = ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar Producto: " + e.getMessage());
            e.printStackTrace();
        }
        return success;
    }

    // MÉTODO DE MAPEO


    private Producto mapResultSetToProducto(ResultSet rs) throws SQLException {

        // 1. Mapear Categoria
        Categoria categoria = new Categoria(
            rs.getInt("idCATEGORIA"),
            rs.getString("NombreCategoria")
        );

        // 2. Mapear Producto principal
        Producto producto = new Producto(
            rs.getInt("idPRODUCTO"),
            rs.getString("Nombre"),
            rs.getString("Modelo"),
            rs.getString("Descripcion"),
            categoria
        );

        // 3. Mapear ProductoAltaTecnologia
        // Se verifica si el ID de Alta Tecnología es NULL
        if (rs.getObject("IdPRODUCTO_ALTA_TECNOLOGIA") != null) {
            
            // Creamos la sub-categoría Alta Tecnología
            CategoriaAltaTecnologia catAltaTec = new CategoriaAltaTecnologia(
                rs.getInt("idCATEGORIA"), //Se usa el mismo ID de la categoría base
                categoria
            );

            // Creamos el Producto Alta Tecnología
            ProductoAltaTecnologia altaTec = new ProductoAltaTecnologia(
                rs.getInt("IdPRODUCTO_ALTA_TECNOLOGIA"),
                rs.getString("PaisOrigen"),
                rs.getDate("FechaFabricacion").toLocalDate(),
                producto,
                catAltaTec
            );
            
            //Esto se guarda en altaTec
            producto.setProductoAltaTecnologia(altaTec); 
        }

        return producto;
    }
}