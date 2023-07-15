/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.miguel.pi.cliente.Utilidades;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Miguel
 */
public class LectorINI {

    private String IP;
    private String PORT;

    public LectorINI() {
        try (InputStream is = new FileInputStream("servidor.ini")) {
            Properties pop = new Properties();
            pop.load(is);
            IP = pop.getProperty("IP");
            PORT = pop.getProperty("PORT");

        } catch (IOException ex) {
            Logger.getLogger(LectorINI.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public String getIP() {
        return IP;
    }

    public String getPORT() {
        return PORT;
    }

    
}
