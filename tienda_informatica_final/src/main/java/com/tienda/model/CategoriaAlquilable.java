package com.tienda.model;


public class CategoriaAlquilable {
    
    private int idCategoriaAlquilable;
    private Categoria categoria;

    public CategoriaAlquilable() {
    }

    public CategoriaAlquilable(int idCategoriaAlquilable, Categoria categoria) {
        this.idCategoriaAlquilable = idCategoriaAlquilable;
        this.categoria = categoria;
    }

    // Getters y Setters
    public int getIdCategoriaAlquilable() {
        return idCategoriaAlquilable;
    }

    public void setIdCategoriaAlquilable(int idCategoriaAlquilable) {
        this.idCategoriaAlquilable = idCategoriaAlquilable;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
}