package com.tienda.model;

public class Proveedor {
    
    private int idProveedor;
    private String nif;
    private Direccion direccion;

    public Proveedor() {
    }

 
    public Proveedor(String nif, Direccion direccion) {
        this.nif = nif;
        this.direccion = direccion;
    }

 
    public Proveedor(int idProveedor, String nif, Direccion direccion) {
        this.idProveedor = idProveedor;
        this.nif = nif;
        this.direccion = direccion;
    }

    // Getters y Setters
    public int getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(int idProveedor) {
        this.idProveedor = idProveedor;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }
}