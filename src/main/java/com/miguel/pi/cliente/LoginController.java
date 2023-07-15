/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.miguel.pi.cliente;

import com.miguel.pi.cliente.Utilidades.Mensajes;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author Miguel
 */
public class LoginController implements Initializable {

    @FXML
    private Label informacion;
    @FXML
    private MFXTextField usuario;
    @FXML
    private MFXTextField passwd;
    @FXML
    private MFXButton butonEntrar;
    @FXML
    private MFXButton registrarBoton;

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
    private void iniciarSesion(ActionEvent event) {

        String nombre = usuario.getText();
        String pass = passwd.getText();

        if (nombre.isEmpty() || pass.isEmpty()) {
            informacion.setText("NOMBRE DE USUARIO O CONTRASEÑA INCORRECTA");
            return;
        }

        String mensaje = Mensajes.LOGIN + ";" + nombre + ";" + pass + ";ORGANIZADOR";
        hacerLogin(mensaje);

    }

    private void hacerLogin(String mensaje) {

        Task<Integer> task = new Task<Integer>() {
            @Override
            protected Integer call() throws Exception {
                try {
                    out.println(mensaje);
                    String fromServer = in.readLine();
                    String[] datos = fromServer.split(";");
                    System.out.println(fromServer);
                    if (datos[0].equals(Mensajes.LOGIN_ACCEPT_ORGANIZER)) {
                        App.idUsuario = Integer.parseInt(datos[1]);
                        App.nombre = datos[2];
                        return 1;

                    } else if (datos[0].equals(Mensajes.LOGIN_ERROR_ORGANIZER) || datos[0].equals(Mensajes.ERROR)) {
                        return 0;
                    } else if (datos[0].equals(Mensajes.LOGIN_ACCEPT_ADMIN)) {
                        App.idUsuario = Integer.parseInt(datos[1]);
                        App.nombre = datos[2];
                        return 2;
                    }

                } catch (IOException ex) {
                    Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                }

                return 0;
            }
        };

        task.setOnSucceeded(event -> {
            int respuesta = task.getValue();
            try {
                App.cargando.completado();
                if (respuesta == 1) {
                    Parent root;
                    root = FXMLLoader.load(getClass().getResource("Torneo.fxml"));
                    App.cambiarVentana(root);

                } else if (respuesta == 0) {
                    informacion.setText("NOMBRE DE USUARIO O CONTRASEÑA INCORRECTA");
                } else if (respuesta == 2) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Administrador");
                    alert.setHeaderText("Deseas iniciar como administrador?");

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {
                        Parent root;
                        root = FXMLLoader.load(getClass().getResource("AdminUsuario.fxml"));
                        App.cambiarVentana(root);
                    } else {
                        Parent root;
                        root = FXMLLoader.load(getClass().getResource("Torneo.fxml"));
                        App.cambiarVentana(root);
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        Thread thread = new Thread(task);
        thread.start();
        App.cargando.cargando(App.mainStage.getX(), App.mainStage.getY());

    }

    @FXML
    private void cambiarRegistrar(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Register.fxml"));
            App.cambiarVentana(root);
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
