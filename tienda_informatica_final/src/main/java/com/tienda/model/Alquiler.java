package com.tienda.model;

import java.time.LocalDate;

public class Alquiler {
    
    private int idAlquiler;
    private LocalDate inicioAlquiler;
    private LocalDate finAlquiler;
    private CategoriaAlquilable categoriaAlquilable;
    private Producto producto;
    private Cliente cliente;
    private Transaccion transaccion;

    public Alquiler() {
    }


    public Alquiler(LocalDate inicioAlquiler, LocalDate finAlquiler, CategoriaAlquilable categoriaAlquilable, Producto producto, Cliente cliente, Transaccion transaccion) {
        this.inicioAlquiler = inicioAlquiler;
        this.finAlquiler = finAlquiler;
        this.categoriaAlquilable = categoriaAlquilable;
        this.producto = producto;
        this.cliente = cliente;
        this.transaccion = transaccion;
    }

 
    public Alquiler(int idAlquiler, LocalDate inicioAlquiler, LocalDate finAlquiler, CategoriaAlquilable categoriaAlquilable, Producto producto, Cliente cliente, Transaccion transaccion) {
        this.idAlquiler = idAlquiler;
        this.inicioAlquiler = inicioAlquiler;
        this.finAlquiler = finAlquiler;
        this.categoriaAlquilable = categoriaAlquilable;
        this.producto = producto;
        this.cliente = cliente;
        this.transaccion = transaccion;
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