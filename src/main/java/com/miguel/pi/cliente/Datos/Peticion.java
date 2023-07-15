/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.miguel.pi.cliente.Datos;

/**
 *
 * @author Miguel
 */
public class Peticion {
    private int id;
    private String nombreDto;
    private int ctdadTotal;
    private int ctdadEquipo;
    private String informacionExtra;
    private String solicitante;

    public Peticion(int id, String nombreDto, int ctdadTotal, int ctdadEquipo, String informacionExtra, String solicitante) {
        this.id = id;
        this.nombreDto = nombreDto;
        this.ctdadTotal = ctdadTotal;
        this.ctdadEquipo = ctdadEquipo;
        this.informacionExtra = informacionExtra;
        this.solicitante = solicitante;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreDto() {
        return nombreDto;
    }

    public void setNombreDto(String nombreDto) {
        this.nombreDto = nombreDto;
    }

    public int getCtdadTotal() {
        return ctdadTotal;
    }

    public void setCtdadTotal(int ctdadTotal) {
        this.ctdadTotal = ctdadTotal;
    }

    public int getCtdadEquipo() {
        return ctdadEquipo;
    }

    public void setCtdadEquipo(int ctdadEquipo) {
        this.ctdadEquipo = ctdadEquipo;
    }

    public String getInformacionExtra() {
        return informacionExtra;
    }

    public void setInformacionExtra(String informacionExtra) {
        this.informacionExtra = informacionExtra;
    }

    public String getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(String solicitante) {
        this.solicitante = solicitante;
    }
    
    
}
