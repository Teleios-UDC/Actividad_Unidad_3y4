package com.tienda.model;


public class CategoriaAltaTecnologia {
    
    private int idCategoriaAltaTecnologia;
    private Categoria categoria;

    public CategoriaAltaTecnologia() {
    }

    public CategoriaAltaTecnologia(int idCategoriaAltaTecnologia, Categoria categoria) {
        this.idCategoriaAltaTecnologia = idCategoriaAltaTecnologia;
        this.categoria = categoria;
    }

    // Getters y Setters
    public int getIdCategoriaAltaTecnologia() {
        return idCategoriaAltaTecnologia;
    }

    public void setIdCategoriaAltaTecnologia(int idCategoriaAltaTecnologia) {
        this.idCategoriaAltaTecnologia = idCategoriaAltaTecnologia;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
}