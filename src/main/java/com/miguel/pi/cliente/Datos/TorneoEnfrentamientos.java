/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.miguel.pi.cliente.Datos;

/**
 *
 * @author Miguel
 */
public class TorneoEnfrentamientos {
    private int id;
    private String equipoLocal;
    private String ptosLocal;
    private String equipoVisitante;
    private String ptosVisitante;
    private int numeroDeRonda;
    private int idTorneo;

    public TorneoEnfrentamientos(int id, String equipoLocal, String ptosLocal, String equipoVisitante, String ptosVisitante, int numeroDeRonda, int idTorneo) {
        this.id = id;
        this.equipoLocal = equipoLocal;
        this.ptosLocal = ptosLocal;
        this.equipoVisitante = equipoVisitante;
        this.ptosVisitante = ptosVisitante;
        this.numeroDeRonda = numeroDeRonda;
        this.idTorneo = idTorneo;
    }

    
    
    public TorneoEnfrentamientos(int id, String equipoLocal, String ptosLocal, String equipoVisitante, String ptosVisitante, int numeroDeRonda) {
        this.id = id;
        this.equipoLocal = equipoLocal;
        this.ptosLocal = ptosLocal;
        this.equipoVisitante = equipoVisitante;
        this.ptosVisitante = ptosVisitante;
        this.numeroDeRonda = numeroDeRonda;
    }

    public TorneoEnfrentamientos() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEquipoLocal() {
        return equipoLocal;
    }

    public void setEquipoLocal(String equipoLocal) {
        this.equipoLocal = equipoLocal;
    }

    public String getPtosLocal() {
        return ptosLocal;
    }

    public void setPtosLocal(String ptosLocal) {
        this.ptosLocal = ptosLocal;
    }

    public String getEquipoVisitante() {
        return equipoVisitante;
    }

    public void setEquipoVisitante(String equipoVisitante) {
        this.equipoVisitante = equipoVisitante;
    }

    public String getPtosVisitante() {
        return ptosVisitante;
    }

    public void setPtosVisitante(String ptosVisitante) {
        this.ptosVisitante = ptosVisitante;
    }

    public int getNumeroDeRonda() {
        return numeroDeRonda;
    }

    public void setNumeroDeRonda(int numeroDeRonda) {
        this.numeroDeRonda = numeroDeRonda;
    }

    public int getIdTorneo() {
        return idTorneo;
    }

    public void setIdTorneo(int idTorneo) {
        this.idTorneo = idTorneo;
    }
    
    
    
}
