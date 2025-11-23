package com.tienda.model;

public class CategoriaServicioTecnico {
    
    private int idCategoriaServicioTecnico;
    private Categoria categoria;

    public CategoriaServicioTecnico() {
    }

    public CategoriaServicioTecnico(int idCategoriaServicioTecnico, Categoria categoria) {
        this.idCategoriaServicioTecnico = idCategoriaServicioTecnico;
        this.categoria = categoria;
    }

    // Getters y Setters
    public int getIdCategoriaServicioTecnico() {
        return idCategoriaServicioTecnico;
    }

    public void setIdCategoriaServicioTecnico(int idCategoriaServicioTecnico) {
        this.idCategoriaServicioTecnico = idCategoriaServicioTecnico;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
}