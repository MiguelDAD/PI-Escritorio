/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.miguel.pi.cliente.Datos;

/**
 *
 * @author Miguel
 */
public class Torneo {

            //nombre:ubicacion:coste:maxEquipos:minEquipos:horaInicio:fechaInicio:horaLimite:fechaLimite:deporte:estado:equiposInscritos:id
    private String nombre;
    private String ubicacion;
    private double coste;
    private int maxEquipos;
    private int minEquipos;
    private String horaInicio;
    private String fechaInicio;
    private String horaLimite;
    private String fechaLimite;
    private String deporte;
    private String estado;
    private int equiposInscritos;
    private int id;

    public Torneo() {
    }

    public Torneo(String nombre, String ubicacion, double coste, int maxEquipos, int minEquipos, String horaInicio, String fechaInicio, String horaLimite, String fechaLimite, String deporte, String estado, int equiposInscritos, int id) {
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.coste = coste;
        this.maxEquipos = maxEquipos;
        this.minEquipos = minEquipos;
        this.horaInicio = horaInicio;
        this.fechaInicio = fechaInicio;
        this.horaLimite = horaLimite;
        this.fechaLimite = fechaLimite;
        this.deporte = deporte;
        this.estado = estado;
        this.equiposInscritos = equiposInscritos;
        this.id = id;
    }
    
    

    public Torneo(String nombre, String ubicacion, double coste, int maxEquipos, int minEquipos, String horaInicio, String fechaInicio, String horaLimite, String fechaLimite, String deporte, int equiposInscritos) {
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.coste = coste;
        this.maxEquipos = maxEquipos;
        this.minEquipos = minEquipos;
        this.horaInicio = horaInicio;
        this.fechaInicio = fechaInicio;
        this.horaLimite = horaLimite;
        this.fechaLimite = fechaLimite;
        this.deporte = deporte;
        this.equiposInscritos = equiposInscritos;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public double getCoste() {
        return coste;
    }

    public void setCoste(double coste) {
        this.coste = coste;
    }

    public int getMaxEquipos() {
        return maxEquipos;
    }

    public void setMaxEquipos(int maxEquipos) {
        this.maxEquipos = maxEquipos;
    }

    public int getMinEquipos() {
        return minEquipos;
    }

    public void setMinEquipos(int minEquipos) {
        this.minEquipos = minEquipos;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getHoraLimite() {
        return horaLimite;
    }

    public void setHoraLimite(String horaLimite) {
        this.horaLimite = horaLimite;
    }

    public String getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(String fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    public int getEquiposInscritos() {
        return equiposInscritos;
    }

    public void setEquiposInscritos(int equiposInscritos) {
        this.equiposInscritos = equiposInscritos;
    }

    public String getDeporte() {
        return deporte;
    }

    public void setDeporte(String deporte) {
        this.deporte = deporte;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return nombre;
    }

}
