package com.tienda.model;

import java.time.LocalDate;

public class Alquiler {
    
    private int idAlquiler;
    private LocalDate inicioAlquiler;
    private LocalDate finAlquiler;
    private int idCategoriaAlquilable;
    private int idProducto;
    private int idCliente;
    private int idTransaccion;

    private CategoriaAlquilable categoriaAlquilable;
    private Producto producto;
    private Cliente cliente;
    private Transaccion transaccion;

    public Alquiler() {
    }


    public Alquiler(LocalDate inicioAlquiler, LocalDate finAlquiler, int idCategoriaAlquilable, int idProducto, int idCliente, int idTransaccion) {
        this.inicioAlquiler = inicioAlquiler;
        this.finAlquiler = finAlquiler;
        this.idCategoriaAlquilable = idCategoriaAlquilable;
        this.idProducto = idProducto;
        this.idCliente = idCliente;
        this.idTransaccion = idTransaccion;
    }


    public Alquiler(int idAlquiler, LocalDate inicioAlquiler, LocalDate finAlquiler, int idCategoriaAlquilable, int idProducto, int idCliente, int idTransaccion) {
        this.idAlquiler = idAlquiler;
        this.inicioAlquiler = inicioAlquiler;
        this.finAlquiler = finAlquiler;
        this.idCategoriaAlquilable = idCategoriaAlquilable;
        this.idProducto = idProducto;
        this.idCliente = idCliente;
        this.idTransaccion = idTransaccion;
    }

    // Getters y Setters
    public int getIdAlquiler() {
        return idAlquiler;
    }

    public void setIdAlquiler(int idAlquiler) {
        this.idAlquiler = idAlquiler;
    }

    public LocalDate getInicioAlquiler() {
        return inicioAlquiler;
    }

    public void setInicioAlquiler(LocalDate inicioAlquiler) {
        this.inicioAlquiler = inicioAlquiler;
    }

    public LocalDate getFinAlquiler() {
        return finAlquiler;
    }

    public void setFinAlquiler(LocalDate finAlquiler) {
        this.finAlquiler = finAlquiler;
    }

    public CategoriaAlquilable getCategoriaAlquilable() {
        return categoriaAlquilable;
    }

    public void setCategoriaAlquilable(CategoriaAlquilable categoriaAlquilable) {
        this.categoriaAlquilable = categoriaAlquilable;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Transaccion getTransaccion() {
        return transaccion;
    }

    public void setTransaccion(Transaccion transaccion) {
        this.transaccion = transaccion;
    }
}