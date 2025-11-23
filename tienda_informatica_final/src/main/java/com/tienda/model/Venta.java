package com.tienda.model;

public class Venta {
    
    private int idVenta;
    private Cliente cliente;
    private Producto producto;
    private Transaccion transaccion;

    public Venta() {
    }


    public Venta(Cliente cliente, Producto producto, Transaccion transaccion) {
        this.cliente = cliente;
        this.producto = producto;
        this.transaccion = transaccion;
    }


    public Venta(int idVenta, Cliente cliente, Producto producto, Transaccion transaccion) {
        this.idVenta = idVenta;
        this.cliente = cliente;
        this.producto = producto;
        this.transaccion = transaccion;
    }

    // Getters y Setters
    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Transaccion getTransaccion() {
        return transaccion;
    }

    public void setTransaccion(Transaccion transaccion) {
        this.transaccion = transaccion;
    }
}