package com.tienda.model;

import java.time.LocalDate;

public class ProductoProveedor {
    
    private int idProductoProveedor;
    private LocalDate fechaAdquisicion;
    private Producto producto;
    private Proveedor proveedor;

    public ProductoProveedor() {
    }
    

    public ProductoProveedor(LocalDate fechaAdquisicion, Producto producto, Proveedor proveedor) {
        this.fechaAdquisicion = fechaAdquisicion;
        this.producto = producto;
        this.proveedor = proveedor;
    }


    public ProductoProveedor(int idProductoProveedor, LocalDate fechaAdquisicion, Producto producto, Proveedor proveedor) {
        this.idProductoProveedor = idProductoProveedor;
        this.fechaAdquisicion = fechaAdquisicion;
        this.producto = producto;
        this.proveedor = proveedor;
    }
    
    // Getters y Setters
    public int getIdProductoProveedor() {
        return idProductoProveedor;
    }

    public void setIdProductoProveedor(int idProductoProveedor) {
        this.idProductoProveedor = idProductoProveedor;
    }

    public LocalDate getFechaAdquisicion() {
        return fechaAdquisicion;
    }

    public void setFechaAdquisicion(LocalDate fechaAdquisicion) {
        this.fechaAdquisicion = fechaAdquisicion;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }
}