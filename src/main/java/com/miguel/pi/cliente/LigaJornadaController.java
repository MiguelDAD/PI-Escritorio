/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.miguel.pi.cliente;

import com.miguel.pi.cliente.Datos.Jornada;
import com.miguel.pi.cliente.Datos.Liga;
import com.miguel.pi.cliente.Datos.Torneo;
import com.miguel.pi.cliente.Utilidades.Mensajes;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyListView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * FXML Controller class
 *
 * @author Miguel
 */
public class LigaJornadaController implements Initializable {

    @FXML
    private Label nombreUusario;
    @FXML
    private MFXButton btnTorneo;
    @FXML
    private MFXButton btnLiga;
    @FXML
    private Label version;
    @FXML
    private MFXButton datosLiga;
    @FXML
    private MFXButton equipoLiga;
    @FXML
    private MFXButton clasificacionLiga;
    @FXML
    private MFXButton jornadasLiga;
    @FXML
    private Label nombreLigaCabezera;
    @FXML
    private MFXComboBox<String> jorndadaLiga;

    private Liga ligaActual;

    private Socket serverSocket;
    private PrintWriter out;
    private BufferedReader in;
    @FXML
    private MFXLegacyListView<Jornada> jornadasListView;

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
        if (event.getSource() == datosLiga) {
            fxmlLoader.setLocation(getClass().getResource("InformacionLiga.fxml"));

            root = fxmlLoader.load();

            InformacionLigaController infoTorneo = fxmlLoader.getController();

            infoTorneo.setLigaActual(ligaActual);
            infoTorneo.cargarDatos();

        } else if (event.getSource() == equipoLiga) {
            fxmlLoader.setLocation(getClass().getResource("EquiposLiga.fxml"));

            root = fxmlLoader.load();

            EquiposLigaController equipo = fxmlLoader.getController();

            equipo.setLigaActual(ligaActual);
            equipo.insertarEquipos();
        } else if (event.getSource() == jornadasLiga) {

            fxmlLoader.setLocation(getClass().getResource("LigaJornada.fxml"));

            root = fxmlLoader.load();

            LigaJornadaController jornada = fxmlLoader.getController();

            jornada.setLigaActual(ligaActual);
            jornada.insertarJornada();

        } else if (event.getSource() == clasificacionLiga) {

            fxmlLoader.setLocation(getClass().getResource("LigaClasificacion.fxml"));

            root = fxmlLoader.load();

            LigaClasificacionController clasificacion = fxmlLoader.getController();

            clasificacion.setLigaActual(ligaActual);
            clasificacion.cargarClasificacion();

        }

        App.cambiarVentana(root);

    }

    @FXML
    private void jornadaSeleccionada(ActionEvent event) {
        cargarJornada();
    }

    public Liga getLigaActual() {
        return ligaActual;
    }

    public void setLigaActual(Liga ligaActual) {
        this.ligaActual = ligaActual;
        nombreLigaCabezera.setText(ligaActual.getNombre());
    }

    public void insertarJornada() {

        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {

                jorndadaLiga.setDisable(true);

                out.println(Mensajes.LIGUES_NUMBER_ROUNDS + ";" + ligaActual.getId());
                try {
                    String fromServer = in.readLine();
                    String[] mensajeRespuesta = fromServer.split(";");
                    System.out.println("DEL SERVER: " + fromServer);
                    if (mensajeRespuesta[0].equals(Mensajes.LIGUES_NUMBER_ROUNDS_OK)) {
                        List<String> jornadas = new ArrayList<>();
                        for (int i = 1; i <= Integer.parseInt(mensajeRespuesta[1]); i++) {

                            jornadas.add("Jornada " + i);
                        }

                        ObservableList<String> todoJornadas = FXCollections.observableList(jornadas);

                        jorndadaLiga.setItems(todoJornadas);
                        jorndadaLiga.getSelectionModel().selectIndex(0);

                        return true;
                    } else if (mensajeRespuesta[0].equals(Mensajes.LIGUES_NUMBER_ROUNDS_ERROR)) {
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

        task.setOnSucceeded(event -> {
            boolean respuesta = task.getValue();

            if (respuesta) {
                jorndadaLiga.setDisable(false);
            }

        });

        Thread thread = new Thread(task);
        thread.start();

    }

    public void cargarJornada() {

        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {

                jorndadaLiga.setDisable(true);

                String jornadaEscogida = jorndadaLiga.getSelectedItem();

                if (jornadaEscogida == null || jornadaEscogida.isEmpty()) {
                    return false;
                }

                String jorndaSelect = jornadaEscogida.substring(jornadaEscogida.indexOf(" ") + 1);

                int nJornada = Integer.parseInt(jorndaSelect);

                jornadasListView.getItems().clear();
                out.println(Mensajes.LIGUES_ROUND_DATE + ";" + ligaActual.getId() + ";" + nJornada);
                try {
                    String fromServer = in.readLine();
                    String[] mensajeRespuesta = fromServer.split(";");
                    System.out.println("DEL SERVER: " + fromServer);
                    if (mensajeRespuesta[0].equals(Mensajes.LIGUES_ROUND_DATE_OK)) {
                        String[] partesJornada = mensajeRespuesta[1].split("!");
                        for (int i = 0; i < partesJornada.length; i++) {
                            String[] jornada = partesJornada[i].split("/");
                            //id;eLocal;ptosLocal;eVisitante;ptosVisiante;fecha;hora!
                            Jornada j = new Jornada(Integer.parseInt(jornada[0]), jornada[1], jornada[2], jornada[3], jornada[4],
                                    jornada[5], jornada[6]);

                            jornadasListView.getItems().add(j);
                        }
                        jornadasListView.setCellFactory(param -> new LigaJornadaAdapter());

                        return true;

                    } else if (mensajeRespuesta[0].equals(Mensajes.LIGUES_ROUND_DATE_ERROR)) {
                        jornadasListView.getItems().clear();

                        return false;
                    } else if (mensajeRespuesta[0].equals(Mensajes.ERROR)) {
                        jornadasListView.getItems().clear();

                        return false;
                    }

                } catch (Exception ex) {
                    return false;
                }
                return false;
            }
        };

        task.setOnSucceeded(event -> {
            boolean respuesta = task.getValue();

            if (respuesta) {
                jorndadaLiga.setDisable(false);
                jornadasListView.refresh();
            }

        });

        Thread thread = new Thread(task);
        thread.start();

    }
}
