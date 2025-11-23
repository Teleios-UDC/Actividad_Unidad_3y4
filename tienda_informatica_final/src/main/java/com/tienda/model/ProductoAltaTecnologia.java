package com.tienda.model;

import java.time.LocalDate;

public class ProductoAltaTecnologia {
    
    private int idProductoAltaTecnologia;
    private String paisOrigen;
    private LocalDate fechaFabricacion;
    private Producto producto;
    private CategoriaAltaTecnologia categoriaAltaTecnologia;

    public ProductoAltaTecnologia() {
    }

    public ProductoAltaTecnologia(String paisOrigen, LocalDate fechaFabricacion, Producto producto, CategoriaAltaTecnologia categoriaAltaTecnologia) {
        this.paisOrigen = paisOrigen;
        this.fechaFabricacion = fechaFabricacion;
        this.producto = producto;
        this.categoriaAltaTecnologia = categoriaAltaTecnologia;
    }


    public ProductoAltaTecnologia(int idProductoAltaTecnologia, String paisOrigen, LocalDate fechaFabricacion, Producto producto, CategoriaAltaTecnologia categoriaAltaTecnologia) {
        this.idProductoAltaTecnologia = idProductoAltaTecnologia;
        this.paisOrigen = paisOrigen;
        this.fechaFabricacion = fechaFabricacion;
        this.producto = producto;
        this.categoriaAltaTecnologia = categoriaAltaTecnologia;
    }

    // Getters y Setters
    public int getIdProductoAltaTecnologia() {
        return idProductoAltaTecnologia;
    }

    public void setIdProductoAltaTecnologia(int idProductoAltaTecnologia) {
        this.idProductoAltaTecnologia = idProductoAltaTecnologia;
    }

    public String getPaisOrigen() {
        return paisOrigen;
    }

    public void setPaisOrigen(String paisOrigen) {
        this.paisOrigen = paisOrigen;
    }

    public LocalDate getFechaFabricacion() {
        return fechaFabricacion;
    }

    public void setFechaFabricacion(LocalDate fechaFabricacion) {
        this.fechaFabricacion = fechaFabricacion;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public CategoriaAltaTecnologia getCategoriaAltaTecnologia() {
        return categoriaAltaTecnologia;
    }

    public void setCategoriaAltaTecnologia(CategoriaAltaTecnologia categoriaAltaTecnologia) {
        this.categoriaAltaTecnologia = categoriaAltaTecnologia;
    }
}