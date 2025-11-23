package com.tienda.model;

public class Transaccion {
    
    private int idTransaccion;
    private int precio;
    private String tipoPago;

    public Transaccion() {
    }

    public Transaccion(int idTransaccion, int precio, String tipoPago) {
        this.idTransaccion = idTransaccion;
        this.precio = precio;
        this.tipoPago = tipoPago;
    }

    // Getters y Setters
    public int getIdTransaccion() {
        return idTransaccion;
    }

    public void setIdTransaccion(int idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public String getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(String tipoPago) {
        this.tipoPago = tipoPago;
    }
}