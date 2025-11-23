package com.tienda.model;

public class Telefono {
    private int idTelefono; 
    private int telefono;
    private String tipoTelefono;

    public Telefono() {
    }
    

    public Telefono(int telefono, String tipoTelefono) {
        this.telefono = telefono;
        this.tipoTelefono = tipoTelefono;
    }


    public Telefono(int idTelefono, int telefono, String tipoTelefono) {
        this.idTelefono = idTelefono;
        this.telefono = telefono;
        this.tipoTelefono = tipoTelefono;
    }
    
    // Getters y Setters
    public int getIdTelefono() {
        return idTelefono;
    }

    public void setIdTelefono(int idTelefono) {
        this.idTelefono = idTelefono;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public String getTipoTelefono() {
        return tipoTelefono;
    }

    public void setTipoTelefono(String tipoTelefono) {
        this.tipoTelefono = tipoTelefono;
    }
}