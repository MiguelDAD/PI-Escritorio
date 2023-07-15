/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.miguel.pi.cliente;

import com.miguel.pi.cliente.Utilidades.Mensajes;
import com.miguel.pi.cliente.Utilidades.Validador;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Miguel
 */
public class AniadirDeporteController implements Initializable {

    @FXML
    private MFXButton crearDeporteBtn;
    @FXML
    private MFXTextField nombreDto;
    @FXML
    private MFXTextField ctdadJugadores;
    @FXML
    private MFXTextField ctdadJugadoresEquipo;

    private Socket serverSocket;
    private PrintWriter out;
    private BufferedReader in;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        serverSocket = App.serverSocket;
        out = App.out;
        in = App.in;
    }

    @FXML
    private void crearDeporteAccion(ActionEvent event) {
        if(comprobarCampos()){
            String mensaje = Mensajes.ADD_SPORT_ADMIN+";"+nombreDto.getText()+";"+ctdadJugadores.getText()+";"+ctdadJugadoresEquipo.getText();
            enviarDeporte(mensaje);
        }
        
    }
    
    public void prerellenar(String nombre, String ctdad, String ctdE){
        nombreDto.setText(nombre);
        ctdadJugadores.setText(ctdad);
        ctdadJugadoresEquipo.setText(ctdE);
    }
    
    private void enviarDeporte(String mensaje) {
        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {

                out.println(mensaje);
                try {
                    String fromServer = in.readLine();

                    if (fromServer.equals(Mensajes.ADD_SPORT_ADMIN_OK)) {

                        return true;

                    } else {
                        return false;

                    }

                } catch (IOException ex) {
                    Logger.getLogger(TorneoController.class.getName()).log(Level.SEVERE, null, ex);
                }
                return false;

            }

        };

        task.setOnSucceeded(event -> {
            boolean respuesta = task.getValue();
            try {
                if (respuesta) {

                    //Obtengo la escena del controlador
                    Stage stage = (Stage) crearDeporteBtn.getScene().getWindow();
                    //Lo cierro
                    stage.close();

                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setTitle("Error");
                    alert.setContentText("Error al insertar el deporte");
                    alert.showAndWait();
                    //Obtengo la escena del controlador
                    Stage stage = (Stage) crearDeporteBtn.getScene().getWindow();
                    //Lo cierro
                    stage.close();
                }
            } catch (Exception ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        Thread thread = new Thread(task);
        thread.start();
    }

    
    private boolean comprobarCampos(){
        if (nombreDto.getText().isEmpty() || ctdadJugadores.getText().isEmpty() || ctdadJugadoresEquipo.getText().isEmpty()
                ) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Error");
            alert.setContentText("Faltan campos por rellenar");
            alert.showAndWait();
            return false;
        } 
        
        if(!Validador.isInteger(ctdadJugadores.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Error");
            alert.setContentText("El numero de jugadores total no es un numero");
            alert.showAndWait();
            return false;
        }
        
        if(!Validador.isInteger(ctdadJugadoresEquipo.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Error");
            alert.setContentText("El numero de jugadores por equipos no es un numero");
            alert.showAndWait();
            return false;
        }
        
        if(Validador.numeroNegativo(ctdadJugadores.getText())||Validador.numeroNegativo(ctdadJugadoresEquipo.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Error");
            alert.setContentText("El numero de jugadores no puede ser negativo");
            alert.showAndWait();
            return false;
        }
        
        return true;
    }

}
