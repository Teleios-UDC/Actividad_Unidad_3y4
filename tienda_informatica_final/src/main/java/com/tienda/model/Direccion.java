package com.tienda.model;

public class Direccion {
    private int idDIRECCION;
    private String ciudad;
    private String barrio;
    private String calle;


    public Direccion() {}


    public Direccion(int idDIRECCION, String ciudad, String barrio, String calle) {
        this.idDIRECCION = idDIRECCION;
        this.ciudad = ciudad;
        this.barrio = barrio;
        this.calle = calle;
    }
    

    public Direccion(String ciudad, String barrio, String calle) {
        this.ciudad = ciudad;
        this.barrio = barrio;
        this.calle = calle;
    }

    // Getters y Setters
    public int getIdDIRECCION() {
        return idDIRECCION;
    }

    public void setIdDIRECCION(int idDIRECCION) {
        this.idDIRECCION = idDIRECCION;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getBarrio() {
        return barrio;
    }

    public void setBarrio(String barrio) {
        this.barrio = barrio;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }
}