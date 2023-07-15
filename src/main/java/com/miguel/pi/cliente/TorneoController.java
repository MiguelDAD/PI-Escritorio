/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.miguel.pi.cliente;

import com.miguel.pi.cliente.Datos.Liga;
import com.miguel.pi.cliente.Datos.Torneo;
import com.miguel.pi.cliente.Utilidades.Filtrado;
import com.miguel.pi.cliente.Utilidades.Mensajes;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXListView;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyListView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
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
import javafx.scene.control.Button;

import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Miguel
 */
public class TorneoController implements Initializable, Filtrado {

    @FXML
    private Label nombreUusario;
    @FXML
    private MFXButton btnTorneo;
    @FXML
    private MFXButton btnLiga;
    @FXML
    private MFXLegacyListView<Torneo> torneoListView;
    @FXML
    private MFXButton btnCrearTorneo;

    private Socket serverSocket;
    private PrintWriter out;
    private BufferedReader in;

    @FXML
    private Label version;
    @FXML
    private Button filtrarDatos;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        version.setText(App.version);

        serverSocket = App.serverSocket;
        out = App.out;
        in = App.in;

        nombreUusario.setText(App.nombre);

        recibirTorneos();

    }

    private void recibirTorneos() {

        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {

                torneoListView.getItems().clear();
                out.println(Mensajes.TOURNAMENTS_ORGANIZER);
                try {
                    String fromServer = in.readLine();
                    String[] mensajeRespuesta = fromServer.split(";");
                    System.out.println("DEL SERVER: " + fromServer);
                    if (mensajeRespuesta[0].equals(Mensajes.TOURNAMENTS_SEND_ORGANIZER)) {
                        for (int i = 1; i < mensajeRespuesta.length; i++) {
                            String[] partesTorneo = mensajeRespuesta[i].split("!");
                            //Prueba:1554-56857:15.0:4:2:15:00:2023-04-04:15:00:2023-04-19:1:0;
                            //nombre:ubicacion:coste:maxEquipos:minEquipos:horaInicio:fechaInicio:horaLimite:fechaLimite:deporte:estado:equiposInscritos:id
                            Torneo t = new Torneo(partesTorneo[0], partesTorneo[1], Double.parseDouble(partesTorneo[2]), Integer.parseInt(partesTorneo[3]), Integer.parseInt(partesTorneo[4]),
                                    partesTorneo[5], partesTorneo[6], partesTorneo[7], partesTorneo[8], partesTorneo[9],
                                    partesTorneo[10], Integer.parseInt(partesTorneo[11]), Integer.parseInt(partesTorneo[12]));

                            torneoListView.getItems().add(t);
                        }
                        torneoListView.setCellFactory(param -> new TorneoTarjetaAdapter());

                        return true;

                    } else if (mensajeRespuesta[0].equals(Mensajes.TOURNAMENTS_ERROR_SEND_ORGANIZER)) {
                        torneoListView.getItems().clear();

                        return false;
                    } else if (mensajeRespuesta[0].equals(Mensajes.ERROR)) {
                        torneoListView.getItems().clear();

                        return false;
                    }

                } catch (Exception ex) {
                    return false;
                }
                return false;
            }
        };
        task.setOnSucceeded(event -> {
            App.cargando.completado();
        });

        Thread thread = new Thread(task);
        thread.start();
        App.cargando.cargando(App.mainStage.getX(), App.mainStage.getY());

    }

    @FXML
    private void cambiarPanel(ActionEvent event) throws IOException {
        Parent root = null;

        //SEGUN LA ID DEL BOTON LANZA UNA U OTRA VENTANA
        if (event.getSource() == btnTorneo) {
            root = FXMLLoader.load(getClass().getResource("Torneo.fxml"));
        } else if (event.getSource() == btnLiga) {
            root = FXMLLoader.load(getClass().getResource("Liga.fxml"));
        }

        App.cambiarVentana(root);

    }

    @FXML
    private void crearTorneo(ActionEvent event) {

        //CREAR LA VENTANA
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("CrearTorneo.fxml"));

            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("Crear Torneo");
            Image icon = new Image(getClass().getResourceAsStream("/com/miguel/pi/cliente/img/logo.png"));

            stage.getIcons().add(icon);

            stage.setScene(scene);
            stage.showAndWait();
            recibirTorneos();

        } catch (IOException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Fallo al crear la nueva ventana", e);
        }

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
    private void filtradoDatos(ActionEvent event) {
        //CREAR LA VENTANA
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("FiltrarDatos.fxml"));

            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("Crear Liga");
            Image icon = new Image(getClass().getResourceAsStream("/com/miguel/pi/cliente/img/logo.png"));

            FiltrarDatosController filtrado = fxmlLoader.getController();
            filtrado.setAplicacion(this);

            stage.getIcons().add(icon);

            stage.setScene(scene);
            stage.showAndWait();

        } catch (IOException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Fallo al crear la nueva ventana", e);
        }
    }

    @Override
    public void filtrarDatos(String deporte, String estado) {
        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {

                torneoListView.getItems().clear();
                torneoListView.refresh();

                out.println(Mensajes.TOURNAMENTS_ORGANIZER);
                try {
                    String fromServer = in.readLine();
                    String[] mensajeRespuesta = fromServer.split(";");
                    System.out.println("DEL SERVER: " + fromServer);
                    if (mensajeRespuesta[0].equals(Mensajes.TOURNAMENTS_SEND_ORGANIZER)) {
                        List<Torneo> torneos = new LinkedList<>();
                        for (int i = 1; i < mensajeRespuesta.length; i++) {
                            String[] partesTorneo = mensajeRespuesta[i].split("!");
                            //Prueba:1554-56857:15.0:4:2:15:00:2023-04-04:15:00:2023-04-19:1:0;
                            //nombre:ubicacion:coste:maxEquipos:minEquipos:horaInicio:fechaInicio:horaLimite:fechaLimite:deporte:estado:equiposInscritos:id
                            Torneo l = new Torneo(partesTorneo[0], partesTorneo[1], Double.parseDouble(partesTorneo[2]), Integer.parseInt(partesTorneo[3]), Integer.parseInt(partesTorneo[4]),
                                    partesTorneo[5], partesTorneo[6], partesTorneo[7], partesTorneo[8], partesTorneo[9],
                                    partesTorneo[10], Integer.parseInt(partesTorneo[11]), Integer.parseInt(partesTorneo[12]));

                            if (!deporte.isEmpty() && !estado.isEmpty()) {
                                if (l.getDeporte().equalsIgnoreCase(deporte) && l.getEstado().equalsIgnoreCase(estado)) {
                                    torneos.add(l);
                                }
                            } else if (!deporte.isEmpty()) {
                                if (l.getDeporte().equalsIgnoreCase(deporte)) {
                                    torneos.add(l);
                                }
                            } else if (!estado.isEmpty()) {
                                if (l.getEstado().equalsIgnoreCase(estado)) {
                                    torneos.add(l);
                                }
                            } else {
                                torneos.add(l);
                            }
                        }

                        ObservableList<Torneo> tObser = FXCollections.observableList(torneos);
                        torneoListView.setItems(tObser);

                        torneoListView.setCellFactory(param -> new TorneoTarjetaAdapter());

                        return true;

                    } else if (mensajeRespuesta[0].equals(Mensajes.TOURNAMENTS_ERROR_SEND_ORGANIZER)) {
                        torneoListView.getItems().clear();
                        torneoListView.refresh();

                        return false;
                    } else if (mensajeRespuesta[0].equals(Mensajes.ERROR)) {
                        torneoListView.getItems().clear();
                        torneoListView.refresh();

                        return false;
                    }

                } catch (Exception ex) {
                    return false;
                }
                return false;
            }
        };
        
        task.setOnSucceeded(event -> {
            App.cargando.completado();
        });
        
        Thread thread = new Thread(task);
        thread.start();
        App.cargando.cargando(App.mainStage.getX(), App.mainStage.getY());

    }

}
