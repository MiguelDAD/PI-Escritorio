/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.miguel.pi.cliente;

import com.miguel.pi.cliente.Datos.Jornada;
import com.miguel.pi.cliente.Utilidades.Mensajes;
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

import javafx.scene.control.Label;
import javafx.stage.Stage;
/**
 * FXML Controller class
 *
 * @author Miguel
 */
public class InsertarResultadoJornadaController implements Initializable {


    @FXML
    private Label equipo1;
    @FXML
    private Label equipo2;
    @FXML
    private MFXTextField resultadoEquipo1;
    @FXML
    private MFXTextField resultadoEquipo2;
    @FXML
    private MFXButton guardarBton;
    
    private Jornada enfrentamiento;
    
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
    
    public void cargarResultado(Jornada j){
        
        enfrentamiento = j;
        equipo1.setText(j.getEquipoLocal());
        equipo2.setText(j.getEquipoVisitante());
        resultadoEquipo1.setText(j.getPtosLocal());
        resultadoEquipo2.setText(j.getPtosVisitante());
        
    }
    
    
    @FXML
    private void guardarResultado(ActionEvent event) {

        int ptosLocal, ptosVisitantes;

        try {
            ptosLocal = Integer.parseInt(resultadoEquipo1.getText());
            ptosVisitantes = Integer.parseInt(resultadoEquipo2.getText());

        } catch (Exception e) {
            datosIncorrecto();
            return;
        }

        //IdRonda;eL;pL;eV;pV
        String mensaje = Mensajes.LIGUES_JORNATE_SAVE + ";" + enfrentamiento.getId() + ";" + equipo1.getText() + ";" + ptosLocal + ";" + equipo2.getText() + ";" + ptosVisitantes;
        enviarCambio(mensaje);
        
    }
    
    private void enviarCambio(String mensaje) {

        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {

                try {
                    out.println(mensaje);
                    String fromServer = in.readLine();
                    String[] datos = fromServer.split(";");

                    if (datos[0].equals(Mensajes.LIGUES_JORNATE_SAVE_OK)) {

                        return true;

                    } else {

                        return false;

                    }

                } catch (IOException ex) {
                    Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                }
                return false;
            }
        };

        task.setOnSucceeded(event -> {
            boolean respuesta = task.getValue();

            if (respuesta) {

                int ptosLocal = Integer.parseInt(resultadoEquipo1.getText());
                int ptosVisitantes = Integer.parseInt(resultadoEquipo2.getText());
                
                enfrentamiento.setPtosLocal("" + ptosLocal);
                enfrentamiento.setPtosVisitante("" + ptosVisitantes);

                Stage stage = (Stage) guardarBton.getScene().getWindow();
                stage.close();

            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setTitle("Error");
                alert.setContentText("Error al guardar la jornada de la liga");
                alert.showAndWait();
                //Obtengo la escena del controlador
                Stage stage = (Stage) guardarBton.getScene().getWindow();
                //Lo cierro
                stage.close();
            }

        });

        Thread thread = new Thread(task);
        thread.start();

    }
    
    
    private void datosIncorrecto() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setTitle("Error");
        alert.setContentText("Los datos son incorrectos");
        alert.showAndWait();
    }
    

}
