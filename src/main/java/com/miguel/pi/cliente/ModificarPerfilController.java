/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.miguel.pi.cliente;

import com.miguel.pi.cliente.Datos.Torneo;
import com.miguel.pi.cliente.Utilidades.Mensajes;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
public class ModificarPerfilController implements Initializable {

    @FXML
    private MFXButton modificarBtn;

    @FXML
    private MFXPasswordField contraseinaText;
    @FXML
    private MFXTextField emailText;

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

        rellenarCorreo();
    }

    @FXML
    private void modificarPerfil(ActionEvent event) {
        String correo = emailText.getText();
        String passwd = contraseinaText.getText();

        if (correo.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setTitle("EMAIL");
            alert.setContentText("El email no puede estar vacio");
            alert.showAndWait();
            return;
        }
        
         if (!comprobarCorreo(correo)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setTitle("EMAIL");
            alert.setContentText("El email es incorrecto");
            alert.showAndWait();
            return;
        }

        String mensaje = Mensajes.CHANGE_DATA + ";" + correo + ";" + passwd;
        actualizarPerfil(mensaje);
    }

     private boolean comprobarCorreo(String email) {
        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

        Matcher matcher = pattern.matcher(email);

        return matcher.find();
    }
    
    private void actualizarPerfil(String mensaje) {
        Task<Integer> task = new Task<Integer>() {
            @Override
            protected Integer call() throws Exception {

                out.println(mensaje);
                try {
                    String fromServer = in.readLine();

                    if (fromServer.equals(Mensajes.CHANGE_DATA_OK)) {

                        return 0;

                    } else if (fromServer.equals(Mensajes.CHANGE_DATA_ERROR)) {
                        return 1;

                    }else{
                        return 2;
                    }

                } catch (IOException ex) {
                    Logger.getLogger(TorneoController.class.getName()).log(Level.SEVERE, null, ex);
                }
                return 2;

            }

        };

        task.setOnSucceeded(event -> {
            int respuesta = task.getValue();
            try {
                if (respuesta==0) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText(null);
                    alert.setTitle("Correcto");
                    alert.setContentText("El perfil se ha modificado");
                    alert.showAndWait();

                    //Obtengo la escena del controlador
                    Stage stage = (Stage) modificarBtn.getScene().getWindow();
                    //Lo cierro
                    stage.close();

                }  else if(respuesta == 1){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setTitle("EMAIL DUPLICADO");
                    alert.setContentText("Ese email ya esta en uso");
                    alert.showAndWait();

                    //Obtengo la escena del controlador
                    Stage stage = (Stage) modificarBtn.getScene().getWindow();
                    //Lo cierro
                    stage.close();
                }else{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setTitle("Error");
                    alert.setContentText("Error al modificar el perfil");
                    alert.showAndWait();

                    //Obtengo la escena del controlador
                    Stage stage = (Stage) modificarBtn.getScene().getWindow();
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

    private void rellenarCorreo() {

        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {

                out.println(Mensajes.USER_EMAIL);
                try {
                    String fromServer = in.readLine();
                    String[] mensajeRespuesta = fromServer.split(";");
                    System.out.println("DEL SERVER: " + fromServer);
                    if (mensajeRespuesta[0].equals(Mensajes.USER_EMAIL_OK)) {
                        emailText.setText(mensajeRespuesta[1]);
                        return true;

                    } else if (mensajeRespuesta[0].equals(Mensajes.USER_EMAIL_ERROR)) {

                        return false;
                    } else if (mensajeRespuesta[0].equals(Mensajes.ERROR)) {

                        return false;
                    }

                } catch (Exception ex) {
                    return false;
                }
                return false;
            }
        };

        Thread thread = new Thread(task);
        thread.start();

    }

}
