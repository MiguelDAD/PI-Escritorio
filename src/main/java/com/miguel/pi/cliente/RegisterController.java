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
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;

import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author Miguel
 */
public class RegisterController implements Initializable {

    @FXML
    private MFXTextField usuario;
    @FXML
    private MFXTextField passwd;
    @FXML
    private MFXButton butonEntrar;
    @FXML
    private Label informacion;
    @FXML
    private MFXTextField email;
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
    private void cambiarIniciarSesion(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
            App.cambiarVentana(root);
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void registrarAccion(ActionEvent event) {

        String correo = email.getText();
        String nombre = usuario.getText();
        String pass = passwd.getText();
        
        if (!comprobarCorreo(correo)){
            informacion.setText("CORREO INCORRECTO");
            return;
        }
        
        if (nombre.isEmpty() || pass.isEmpty() || correo.isEmpty()) {
            informacion.setText("NO PUEDE HABER CAMPOS VACIOS");
            return;
        }
        String mensaje = Mensajes.REGISTER + ";" + nombre + ";" + correo + ";" + pass + ";ORGANIZADOR";
        hacerRegister(mensaje);

    }
    
    private boolean comprobarCorreo(String email) {
        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

        Matcher matcher = pattern.matcher(email);

        return matcher.find();
    }

    private void hacerRegister(String mensaje) {
        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                try {
                    out.println(mensaje);
                    String fromServer = in.readLine();
                    String[] datos = fromServer.split(";");
                    if (datos[0].equals(Mensajes.REGISTER_ACCEPT_ORGANIZER)) {

                        App.idUsuario = Integer.parseInt(datos[1]);
                        App.nombre = datos[2];

                        return true;

                    } else if (datos[0].equals(Mensajes.REGISTER_ERROR_ORGANIZER) || datos[0].equals(Mensajes.ERROR)) {
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
            try {
                App.cargando.completado();
                if (respuesta) {
                    Parent root = FXMLLoader.load(getClass().getResource("Torneo.fxml"));
                    App.cambiarVentana(root);
                } else {
                    informacion.setText("NOMBRE DE USUARIO O EMAIL YA REGISTRADO");
                }
            } catch (IOException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        Thread thread = new Thread(task);
        thread.start();
        App.cargando.cargando(App.mainStage.getX(), App.mainStage.getY());

    }
}
