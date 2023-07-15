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
public class AdminEquipo {
    private String nombre;
    private String deporte;
    private String lider;
    private String latitud;
    private String longitud;
    private String privacidad;
    private String direccion;

    public AdminEquipo(String nombre, String deporte, String lider, String latitud, String longitud, String privacidad) {
        this.nombre = nombre;
        this.deporte = deporte;
        this.lider = lider;
        this.latitud = latitud;
        this.longitud = longitud;
        this.privacidad = privacidad;
        obtenerDireccion();
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

    public String getPrivacidad() {
        return privacidad;
    }

    public void setPrivacidad(String privacidad) {
        this.privacidad = privacidad;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    
    
    
}
