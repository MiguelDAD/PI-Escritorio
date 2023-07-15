/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.miguel.pi.cliente.Datos;

/**
 *
 * @author Miguel
 */
public class Puntuacion implements Comparable<Puntuacion> {
    
    private String equipo;
    private int partidosjugados;
    private int puntos;
    private int puntuacionafavor;
    private int puntuacionencontra;
    private int diferencia;

    public Puntuacion(String equipo, int partidosjugados, int puntos, int puntuacionafavor, int puntuacionencontra, int diferencia) {
        this.equipo = equipo;
        this.partidosjugados = partidosjugados;
        this.puntos = puntos;
        this.puntuacionafavor = puntuacionafavor;
        this.puntuacionencontra = puntuacionencontra;
        this.diferencia = diferencia;
    }

    public Puntuacion() {
    }

    public String getEquipo() {
        return equipo;
    }

    public void setEquipo(String equipo) {
        this.equipo = equipo;
    }

    public int getPartidosjugados() {
        return partidosjugados;
    }

    public void setPartidosjugados(int partidosjugados) {
        this.partidosjugados = partidosjugados;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public int getPuntuacionafavor() {
        return puntuacionafavor;
    }

    public void setPuntuacionafavor(int puntuacionafavor) {
        this.puntuacionafavor = puntuacionafavor;
    }

    public int getPuntuacionencontra() {
        return puntuacionencontra;
    }

    public void setPuntuacionencontra(int puntuacionencontra) {
        this.puntuacionencontra = puntuacionencontra;
    }

    public int getDiferencia() {
        return diferencia;
    }

    public void setDiferencia(int diferencia) {
        this.diferencia = diferencia;
    }
    
    @Override
    public int compareTo(Puntuacion o) {
        if(o.getPuntos() - puntos == 0)
            return o.getDiferencia()-diferencia;
        
        return o.getPuntos() - puntos;
    }
    
}
