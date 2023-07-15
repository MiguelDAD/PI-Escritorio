/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.miguel.pi.cliente.Datos;

import com.miguel.pi.cliente.Utilidades.Geocoding;

/**
 *
 * @author Miguel
 */
public class AdminLiga {
    private int id;
    private String nombre;
    private String deporte;
    private String lider;
    private String latitud;
    private String longitud;
    private String estado;
    private String direccion;

    public AdminLiga(int id, String nombre, String deporte, String lider, String latitud, String longitud, String privacidad) {
        this.id = id;
        this.nombre = nombre;
        this.deporte = deporte;
        this.lider = lider;
        this.latitud = latitud;
        this.longitud = longitud;
        this.estado = privacidad;
        obtenerDireccion();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    private void obtenerDireccion(){
        direccion = Geocoding.obtenerNombre(latitud, longitud);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDeporte() {
        return deporte;
    }

    public void setDeporte(String deporte) {
        this.deporte = deporte;
    }

    public String getLider() {
        return lider;
    }

    public void setLider(String lider) {
        this.lider = lider;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    
    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    
}
