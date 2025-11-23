package com.tienda.model;

import java.time.LocalDate;

public class ServicioTecnico {
    
    private int idServicioTecnico;
    private String detallesServicio;
    private LocalDate fecha;
    private Cliente cliente;
    private Producto producto;
    private CategoriaServicioTecnico categoriaServicioTecnico;
    private Transaccion transaccion;

    public ServicioTecnico() {
    }


    public ServicioTecnico(String detallesServicio, LocalDate fecha, Cliente cliente, Producto producto, CategoriaServicioTecnico categoriaServicioTecnico, Transaccion transaccion) {
        this.detallesServicio = detallesServicio;
        this.fecha = fecha;
        this.cliente = cliente;
        this.producto = producto;
        this.categoriaServicioTecnico = categoriaServicioTecnico;
        this.transaccion = transaccion;
    }


    public ServicioTecnico(int idServicioTecnico, String detallesServicio, LocalDate fecha, Cliente cliente, Producto producto, CategoriaServicioTecnico categoriaServicioTecnico, Transaccion transaccion) {
        this.idServicioTecnico = idServicioTecnico;
        this.detallesServicio = detallesServicio;
        this.fecha = fecha;
        this.cliente = cliente;
        this.producto = producto;
        this.categoriaServicioTecnico = categoriaServicioTecnico;
        this.transaccion = transaccion;
    }

    // Getters y Setters
    public int getIdServicioTecnico() {
        return idServicioTecnico;
    }

    public void setIdServicioTecnico(int idServicioTecnico) {
        this.idServicioTecnico = idServicioTecnico;
    }

    public String getDetallesServicio() {
        return detallesServicio;
    }

    public void setDetallesServicio(String detallesServicio) {
        this.detallesServicio = detallesServicio;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
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

    public CategoriaServicioTecnico getCategoriaServicioTecnico() {
        return categoriaServicioTecnico;
    }

    public void setCategoriaServicioTecnico(CategoriaServicioTecnico categoriaServicioTecnico) {
        this.categoriaServicioTecnico = categoriaServicioTecnico;
    }

    public Transaccion getTransaccion() {
        return transaccion;
    }

    public void setTransaccion(Transaccion transaccion) {
        this.transaccion = transaccion;
    }
}