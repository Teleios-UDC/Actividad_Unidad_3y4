package com.tienda.model;

public class EmpresaFabricante {
    
    private int idEmpresaFabricante;
    private String nombre;
    private int numeroEmpleados;
    private Direccion direccion;

    public EmpresaFabricante() {
    }


    public EmpresaFabricante(String nombre, int numeroEmpleados) {
        this.nombre = nombre;
        this.numeroEmpleados = numeroEmpleados;
    }


    public EmpresaFabricante(int idEmpresaFabricante, String nombre, int numeroEmpleados, Direccion direccion) {
        this.idEmpresaFabricante = idEmpresaFabricante;
        this.nombre = nombre;
        this.numeroEmpleados = numeroEmpleados;
        this.direccion = direccion;
    }
    
    // Getters y Setters
    public int getIdEmpresaFabricante() {
        return idEmpresaFabricante;
    }

    public void setIdEmpresaFabricante(int idEmpresaFabricante) {
        this.idEmpresaFabricante = idEmpresaFabricante;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getNumeroEmpleados() {
        return numeroEmpleados;
    }

    public void setNumeroEmpleados(int numeroEmpleados) {
        this.numeroEmpleados = numeroEmpleados;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }
}