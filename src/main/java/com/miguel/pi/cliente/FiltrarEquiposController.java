/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.miguel.pi.cliente;

import com.miguel.pi.cliente.Utilidades.Filtrado;
import com.miguel.pi.cliente.Utilidades.Mensajes;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Miguel
 */
public class FiltrarEquiposController implements Initializable {

    @FXML
    private MFXButton filtrarBtn;
    @FXML
    private MFXComboBox<String> deporteCombo;
    @FXML
    private MFXTextField nombreEquipo;
    private Socket serverSocket;
    private PrintWriter out;
    private BufferedReader in;

    private Filtrado aplicacion;

    public void setAplicacion(Filtrado aplicacion) {
        this.aplicacion = aplicacion;
    }
    
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        serverSocket = App.serverSocket;
        out = App.out;
        in = App.in;

        cargarCombos();
    }

    @FXML
    private void filtrarDatos(ActionEvent event) {        
        Stage stage = (Stage) filtrarBtn.getScene().getWindow();
        stage.close();
        
        aplicacion.filtrarDatos(deporteCombo.getText(), nombreEquipo.getText());
    }

    private void cargarCombos() {
        insertarDeportes();
    }

    private void insertarDeportes() {
        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {

                out.println(Mensajes.GET_SPORT);
                try {
                    String fromServer = in.readLine();
                    String[] mensajeRespuesta = fromServer.split(";");

                    List<String> deportes = new ArrayList<>();

                    if (mensajeRespuesta[0].equals(Mensajes.SEND_SPORT)) {
                        for (int i = 1; i < mensajeRespuesta.length; i++) {
                            String[] partesDeporte = mensajeRespuesta[i].split(":");

                            deportes.add(partesDeporte[0]);
                        }
                        ObservableList<String> todosDeportes = FXCollections.observableList(deportes);

                        deporteCombo.setItems(todosDeportes);
                        return true;

                    } else if (mensajeRespuesta[0].equals(Mensajes.ERROR_SEND_SPORT)) {
                        return false;
                    } else if (mensajeRespuesta[0].equals(Mensajes.ERROR)) {
                        return false;
                    }

                } catch (IOException ex) {
                    Logger.getLogger(TorneoController.class.getName()).log(Level.SEVERE, null, ex);
                }
                return false;
            }
        };

        Thread thread = new Thread(task);
        thread.start();

    }

    
}
