package com.example.fbelectronica.objetos;

import java.io.Serializable;

public class Productos implements Serializable {

    private String _ID;
    private String marca;
    private String descripcion;
    private double precio;
    private String foto;


    public Productos() {
    }

    public Productos(String marca, String desc, double precio, String foto){
        this.setMarca(marca);
        this.setDescripcion(desc);
        this.setPrecio(precio);
        this.setFoto(foto);
    }

    public String get_ID() {
        return _ID;
    }
    public void set_ID(String _ID) {
        this._ID = _ID;
    }
    public String getMarca() {
        return marca;
    }
    public void setMarca(String marca) {
        this.marca = marca;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public double getPrecio() {
        return precio;
    }
    public void setPrecio(double precio) {
        this.precio = precio;
    }
    public String getFoto() {
        return foto;
    }
    public void setFoto(String foto) {
        this.foto = foto;
    }

}
