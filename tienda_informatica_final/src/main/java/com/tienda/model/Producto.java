package com.tienda.model;

public class Producto {
    
    private int idProducto; 
    private String nombre;
    private String modelo;
    private String descripcion;
    private Categoria categoria;
    private ProductoAltaTecnologia productoAltaTecnologia;


    public Producto() {
    }
    

    public Producto(String nombre, String modelo, String descripcion, Categoria categoria) {
        this.nombre = nombre;
        this.modelo = modelo;
        this.descripcion = descripcion;
        this.categoria = categoria;
    }

    public Producto(int idProducto, String nombre, String modelo, String descripcion, Categoria categoria) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.modelo = modelo;
        this.descripcion = descripcion;
        this.categoria = categoria;
    }

    // Getters y Setters
    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }


    public ProductoAltaTecnologia getProductoAltaTecnologia() {
        return productoAltaTecnologia;
    }


    public void setProductoAltaTecnologia(ProductoAltaTecnologia altaTec) {
        this.productoAltaTecnologia = altaTec;
    }
}