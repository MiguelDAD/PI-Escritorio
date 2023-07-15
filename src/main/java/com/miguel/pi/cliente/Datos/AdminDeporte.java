/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.miguel.pi.cliente.Datos;

/**
 *
 * @author Miguel
 */
public class AdminDeporte {

    private int id;
    private String deporte;
    private int cantidadJugadores;
    private int cantidadPorEquipo;
    private String ctdadjugadoresmostrar;
    private String ctdadequipomostrar;

    public AdminDeporte(int id, String deporte, int cantidadJugadores, int cantidadPorEquipo) {
        this.id = id;
        this.deporte = deporte;
        this.cantidadJugadores = cantidadJugadores;
        this.cantidadPorEquipo = cantidadPorEquipo;
        crearContenido();
    }

    private void crearContenido() {
       ctdadjugadoresmostrar = cantidadJugadores + " jugadore/s";
        ctdadequipomostrar = cantidadPorEquipo + " jugadore/s";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDeporte() {
        return deporte;
    }

    public void setDeporte(String deporte) {
        this.deporte = deporte;
    }

    public int getCantidadJugadores() {
        return cantidadJugadores;
    }

    public void setCantidadJugadores(int cantidadJugadores) {
        this.cantidadJugadores = cantidadJugadores;
        crearContenido();
    }

    public int getCantidadPorEquipo() {
        return cantidadPorEquipo;
    }

    public void setCantidadPorEquipo(int cantidadPorEquipo) {
        this.cantidadPorEquipo = cantidadPorEquipo;
        crearContenido();
    }

    public String getCtdadjugadoresmostrar() {
        return ctdadjugadoresmostrar;
    }

    public void setCtdadjugadoresmostrar(String ctdadjugadoresmostrar) {
        this.ctdadjugadoresmostrar = ctdadjugadoresmostrar;
    }

    public String getCtdadequipomostrar() {
        return ctdadequipomostrar;
    }

    public void setCtdadequipomostrar(String ctdadequipomostrar) {
        this.ctdadequipomostrar = ctdadequipomostrar;
    }
    
    

}
