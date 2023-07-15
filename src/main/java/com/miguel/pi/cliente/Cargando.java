/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.miguel.pi.cliente;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Miguel
 */
public class Cargando {

    Stage cargando;

    public void cargando(double x, double y) {
        inicializarCargando();
        cargando.setX(x+8);
        cargando.setY(y+30);
        cargando.show();
    }
    
    public void cargando(double x, double y,double ancho, double largo){
        inicializarCargando();
        cargando.setX(x+8);
        cargando.setY(y+30);
        cargando.setWidth(ancho);
        cargando.setHeight(largo);
        cargando.show();
    }

    public void completado() {
        cargando.close();

    }

    private void inicializarCargando() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("Cargando.fxml"));

            Scene scene = new Scene(fxmlLoader.load());
            cargando = new Stage();
            cargando.initStyle(StageStyle.UNDECORATED);

            //scene.setFill(null);
            cargando.setOpacity(0.5);
            cargando.setResizable(false);
            

            Image icon = new Image(getClass().getResourceAsStream("/com/miguel/pi/cliente/img/logo.png"));

            cargando.getIcons().add(icon);
            cargando.initModality(Modality.APPLICATION_MODAL);

            cargando.setScene(scene);

        } catch (IOException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Fallo al crear la nueva ventana", e);
        }
    }

}
