/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.miguel.pi.cliente.Datos;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Miguel
 */
public class Equipo {
    private String nombreEquipo;
    private String lider;
    private List<String> integrantes;

    public Equipo(String nombreEquipo, String lider) {
        this.nombreEquipo = nombreEquipo;
        this.lider = lider;
        integrantes = new ArrayList<>();
    }

    public String getNombreEquipo() {
        return nombreEquipo;
    }

    public void setNombreEquipo(String nombreEquipo) {
        this.nombreEquipo = nombreEquipo;
    }

    public String getLider() {
        return lider;
    }

    public void setLider(String lider) {
        this.lider = lider;
    }

    public List<String> getIntegrantes() {
        return integrantes;
    }

    public void setIntegrantes(List<String> integrantes) {
        this.integrantes = integrantes;
    }
    
    public void insertarUsuario (String usuario){
        integrantes.add(usuario);
    }

    @Override
    public String toString() {
        return nombreEquipo;
    }
    
    
}
