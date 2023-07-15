/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.miguel.pi.cliente.Utilidades;

import java.time.LocalDate;

/**
 *
 * @author Miguel
 */
public class Validador {

    public static boolean isDouble(String numero) {
        try {
            Double.parseDouble(numero);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public static boolean isInteger(String numero){
        try {
            Integer.parseInt(numero);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public static boolean numeroNegativo(String numero){
        try {
            if(Integer.parseInt(numero)<0)
                return true;
        } catch (Exception e) {
            return false;
        }
        return false;
    }
    
    public static boolean fechaAntesQueHoy(LocalDate fechaLimite){
        try{        
            return fechaLimite.isBefore(LocalDate.now());
        }catch(Exception e){
            return false;
        }
    }
    
    public static boolean fechaAntesQue(LocalDate fechaLimite, LocalDate fechaInicio){
        
        try{        
            return fechaLimite.isBefore(fechaInicio);
        }catch(Exception e){
            return false;
        }
        
    }
    
    public static boolean superaPrecioMaximo(String numero){
        try {
            if(Double.parseDouble(numero)>999)
                return true;
        } catch (Exception e) {
            return false;
        }
        return false;
    }
    
    public static boolean precioNegativo(String numero){
        try {
            if(Double.parseDouble(numero)<0)
                return true;
        } catch (Exception e) {
            return false;
        }
        return false;
    }

}
