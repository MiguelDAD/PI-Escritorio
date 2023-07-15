/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.miguel.pi.cliente;

import com.miguel.pi.cliente.Datos.AdminDeporte;
import com.miguel.pi.cliente.Utilidades.Mensajes;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;

import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Miguel
 */
public class AdminDeporteController implements Initializable {

    @FXML
    private Label nombreUusario;
    @FXML
    private Label version;
    @FXML
    private MFXButton btnUsuario;
    @FXML
    private MFXButton btnEquipos;
    @FXML
    private MFXButton btnTorneos;
    @FXML
    private MFXButton btnLigas;
    @FXML
    private MFXButton btnPartidos;
    @FXML
    private TableColumn<AdminDeporte, String> nombreColumna;
    @FXML
    private MFXButton btnDeportes;
    @FXML
    private TableView<AdminDeporte> tablaDeporte;
    @FXML
    private TableColumn<AdminDeporte, String> ctdaJugadoresColumna;
    @FXML
    private TableColumn<AdminDeporte, String> ctdadEquiposColumna;

    private Socket serverSocket;
    private PrintWriter out;
    private BufferedReader in;
    @FXML
    private MFXButton aniadirBoton;
    @FXML
    private MFXButton peticionesBoton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        version.setText(App.version);
        nombreUusario.setText(App.nombre);

        serverSocket = App.serverSocket;
        out = App.out;
        in = App.in;

        nombreColumna.setCellValueFactory(new PropertyValueFactory("deporte"));
        ctdaJugadoresColumna.setCellValueFactory(new PropertyValueFactory("ctdadjugadoresmostrar"));
        ctdadEquiposColumna.setCellValueFactory(new PropertyValueFactory("ctdadequipomostrar"));
        rellenarTabla();
    }

    private void rellenarTabla() {
        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                tablaDeporte.getItems().clear();

                out.println(Mensajes.GET_SPORT_ADMIN);
                try {
                    String fromServer = in.readLine();
                    String[] mensajeRespuesta = fromServer.split(";");

                    List<AdminDeporte> deportes = new ArrayList<>();

                    if (mensajeRespuesta[0].equals(Mensajes.GET_SPORT_ADMIN_OK)) {
                        for (int i = 1; i < mensajeRespuesta.length; i++) {
                            String[] partesDeporte = mensajeRespuesta[i].split(":");
                            int id = Integer.parseInt(partesDeporte[0]);
                            String nombre = partesDeporte[1];
                            int ctdadJugadores = Integer.parseInt(partesDeporte[2]);
                            int cantidadEquipo = Integer.parseInt(partesDeporte[3]);
                            AdminDeporte d = new AdminDeporte(id, nombre, ctdadJugadores, cantidadEquipo);

                            deportes.add(d);
                        }
                        ObservableList<AdminDeporte> todosDeportes = FXCollections.observableList(deportes);

                        tablaDeporte.setItems(todosDeportes);

                        return true;

                    } else if (mensajeRespuesta[0].equals(Mensajes.GET_SPORT_ADMIN_ERROR)) {
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

        task.setOnSucceeded(event -> {
            App.cargando.completado();
        });

        Thread thread = new Thread(task);
        thread.start();
        App.cargando.cargando(App.mainStage.getX(),App.mainStage.getY());

    }

    @FXML
    private void cambiarPanel(ActionEvent event) throws IOException {
        Parent root = null;

        //SEGUN LA ID DEL BOTON LANZA UNA U OTRA VENTANA
        if (event.getSource() == btnUsuario) {
            root = FXMLLoader.load(getClass().getResource("AdminUsuario.fxml"));
        } else if (event.getSource() == btnEquipos) {
            root = FXMLLoader.load(getClass().getResource("AdminEquipo.fxml"));
        } else if (event.getSource() == btnTorneos) {
            root = FXMLLoader.load(getClass().getResource("AdminTorneo.fxml"));
        } else if (event.getSource() == btnLigas) {
            root = FXMLLoader.load(getClass().getResource("AdminLiga.fxml"));
        } else if (event.getSource() == btnPartidos) {
            root = FXMLLoader.load(getClass().getResource("AdminPartido.fxml"));
        } else if (event.getSource() == btnDeportes) {
            root = FXMLLoader.load(getClass().getResource("AdminDeporte.fxml"));
        }

        App.cambiarVentana(root);
    }

    @FXML
    private void activarBloqueo(MouseEvent event) {
    }

    @FXML
    private void modificarPerfil(MouseEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("ModificarPerfil.fxml"));

            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("Modificar perfil");
            Image icon = new Image(getClass().getResourceAsStream("/com/miguel/pi/cliente/img/logo.png"));

            stage.getIcons().add(icon);

            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Fallo al crear la nueva ventana", e);
        }
    }

    @FXML
    private void aniadirDeporte(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("AniadirDeporte.fxml"));

            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("Añadir deporte");
            Image icon = new Image(getClass().getResourceAsStream("/com/miguel/pi/cliente/img/logo.png"));

            stage.getIcons().add(icon);

            stage.setScene(scene);
            stage.showAndWait();
            rellenarTabla();

        } catch (IOException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Fallo al crear la nueva ventana", e);
        }
    }

    @FXML
    private void verPeticiones(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("PeticionesDeporte.fxml"));

            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("Peticiones deporte");
            Image icon = new Image(getClass().getResourceAsStream("/com/miguel/pi/cliente/img/logo.png"));

            stage.getIcons().add(icon);
            
            stage.setScene(scene);
            stage.showAndWait();
            rellenarTabla();

        } catch (IOException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Fallo al crear la nueva ventana", e);
        }
    }

}
