/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.miguel.pi.cliente;

import com.miguel.pi.cliente.Datos.Equipo;
import com.miguel.pi.cliente.Datos.Torneo;
import com.miguel.pi.cliente.Utilidades.Mensajes;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyListView;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;

import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author Miguel
 */
public class EquiposTorneoController implements Initializable {

    @FXML
    private Label nombreUusario;
    @FXML
    private MFXButton btnTorneo;
    @FXML
    private MFXButton btnLiga;
    @FXML
    private MFXButton datosTorneo;
    @FXML
    private MFXButton equipoTorneo;
    @FXML
    private MFXButton rondasTorneo;
    @FXML
    private Label nombreTorneoCabezera;
    @FXML
    private MFXLegacyListView<Equipo> equiposListView;

    private Torneo torneoActual;

    private Socket serverSocket;
    private PrintWriter out;
    private BufferedReader in;
    @FXML
    private Label version;

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
    private void cambiarPestania(ActionEvent event) throws IOException {

        Parent root = null;
        FXMLLoader fxmlLoader = new FXMLLoader();

        //SEGUN LA ID DEL BOTON LANZA UNA U OTRA VENTANA
        if (event.getSource() == datosTorneo) {
            fxmlLoader.setLocation(getClass().getResource("InformacionTorneo.fxml"));

            root = fxmlLoader.load();

            InformacionTorneoController infoTorneo = fxmlLoader.getController();

            infoTorneo.setTorneoActual(torneoActual);
            infoTorneo.cargarDatos();

        } else if (event.getSource() == equipoTorneo) {
            fxmlLoader.setLocation(getClass().getResource("EquiposTorneo.fxml"));

            root = fxmlLoader.load();

            EquiposTorneoController equipo = fxmlLoader.getController();

            equipo.setTorneoActual(torneoActual);
            equipo.insertarEquipos();
        } else if (event.getSource() == rondasTorneo) {

            if (torneoActual.getMaxEquipos() == 4) {
                fxmlLoader.setLocation(getClass().getResource("TorneoCuatro.fxml"));
                root = fxmlLoader.load();
                TorneoCuatroController torneoCuatro = fxmlLoader.getController();
                torneoCuatro.setTorneoActual(torneoActual);
                torneoCuatro.insertarEquipos();
            } else if (torneoActual.getMaxEquipos() == 8) {
                fxmlLoader.setLocation(getClass().getResource("TorneoOcho.fxml"));
                root = fxmlLoader.load();
                TorneoOchoController torneoOcho = fxmlLoader.getController();
                torneoOcho.setTorneoActual(torneoActual);
                torneoOcho.insertarEquipos();
            }

        }

        App.cambiarVentana(root);

    }

    public Torneo getTorneoActual() {
        return torneoActual;
    }

    public void setTorneoActual(Torneo torneoActual) {
        this.torneoActual = torneoActual;
        nombreTorneoCabezera.setText(torneoActual.getNombre());
    }

    public void insertarEquipos() {
        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {

                try {
                    out.println(Mensajes.TOURNAMENT_TEAMS + ";" + torneoActual.getId());
                    String fromServer = in.readLine();
                    String[] datos = fromServer.split(";");

                    if (datos[0].equals(Mensajes.TOURNAMENT_TEAMS_OK)) {
                        for (int i = 1; i < datos.length; i++) {
                            String[] partesEquipos = datos[i].split(":");
                            String nombreEqupo = partesEquipos[0];
                            String lider = partesEquipos[1];

                            System.out.println(nombreEqupo + " " + lider + " ");
                            Equipo e = new Equipo(nombreEqupo, lider);

                            if (partesEquipos.length > 2) {
                                String[] integrantes = partesEquipos[2].split("!");
                                for (String participante : integrantes) {
                                    e.insertarUsuario(participante);
                                }

                            }

                            equiposListView.getItems().add(e);
                        }

                        return true;

                    } else if (datos[0].equals(Mensajes.TOURNAMENT_TEAMS_ERROR)) {
                        equiposListView.getItems().clear();
                        return false;

                    } else {
                        equiposListView.getItems().clear();
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
                equiposListView.setCellFactory(param -> new EquipoTarjetaAdapter());
            } else {
                equiposListView.getItems().clear();
            }
            
            App.cargando.completado();

        });

        Thread thread = new Thread(task);
        thread.start();
        App.cargando.cargando(App.mainStage.getX(), App.mainStage.getY());

    }

}
