/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.miguel.pi.cliente;

import com.miguel.pi.cliente.Datos.Liga;
import com.miguel.pi.cliente.Datos.Puntuacion;
import com.miguel.pi.cliente.Utilidades.Mensajes;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedList;
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
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Miguel
 */
public class LigaClasificacionController implements Initializable {

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
    private TableView<Puntuacion> tablaLiga;
    @FXML
    private TableColumn<Puntuacion, String> equipoColumna;
    @FXML
    private TableColumn<Puntuacion, Integer> partidosJugadosColumna;
    @FXML
    private TableColumn<Puntuacion, Integer> puntosColumna;
    @FXML
    private TableColumn<Puntuacion, Integer> puntosFavorColumna;
    @FXML
    private TableColumn<Puntuacion, Integer> puntosContraColumna;
    @FXML
    private TableColumn<Puntuacion, Integer> diferenciaColumna;
    @FXML
    private MFXButton finalizarBoton;
    @FXML
    private Label nombreLigaCabezera;

    private Liga ligaActual;

    private Socket serverSocket;
    private PrintWriter out;
    private BufferedReader in;

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

        equipoColumna.setCellValueFactory(new PropertyValueFactory("equipo"));
        partidosJugadosColumna.setCellValueFactory(new PropertyValueFactory("partidosjugados"));
        puntosColumna.setCellValueFactory(new PropertyValueFactory("puntos"));
        puntosFavorColumna.setCellValueFactory(new PropertyValueFactory("puntuacionafavor"));
        puntosContraColumna.setCellValueFactory(new PropertyValueFactory("puntuacionencontra"));
        diferenciaColumna.setCellValueFactory(new PropertyValueFactory("diferencia"));

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

    private void cargarBoton() {
        switch (ligaActual.getEstado()) {
            case "Jugandose":
                finalizarBoton.setText("Finalizar");
                finalizarBoton.setDisable(false);
                break;
            case "Esperando Jugadores":
                finalizarBoton.setText("Cancelar");
                finalizarBoton.setDisable(false);
                break;
            case "Cancelado":
                finalizarBoton.setText("Cancelado");
                finalizarBoton.setDisable(true);
                break;
            case "Finalizado":
                finalizarBoton.setText("Finalizado");
                finalizarBoton.setDisable(true);
                break;

            default:
                finalizarBoton.setDisable(false);
                finalizarBoton.setText("Cancelar");
        }
    }

    @FXML
    private void finalizarLiga(ActionEvent event) {
        String enviar = "";

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Terminar liga");
        alert.setHeaderText("Seguro que deseas terminar la liga?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            switch (ligaActual.getEstado()) {
                case "Jugandose":
                    finalizarBoton.setText("Finalizar");
                    finalizarBoton.setDisable(false);
                    enviar = Mensajes.LIGUES_END + ";" + ligaActual.getId();
                    break;
                case "Esperando Jugadores":
                    finalizarBoton.setText("Cancelar");
                    finalizarBoton.setDisable(false);
                    enviar = Mensajes.LIGUES_CANCEL + ";" + ligaActual.getId();
                    break;
            }

            enviarDato(enviar);
        } else {
        }

    }

    private void enviarDato(String enviar) {

        if (enviar.equalsIgnoreCase("")) {
            return;
        }

        out.println(enviar);
        try {
            String fromServer = in.readLine();
            System.out.println("DEL SERVER: " + fromServer);

            if (fromServer.equalsIgnoreCase(Mensajes.LIGUES_END_OK)) {
                ligaActual.setEstado("Finalizado");
                finalizarBoton.setText("Finalizado");
                finalizarBoton.setDisable(true);
            }
            if (fromServer.equalsIgnoreCase(Mensajes.LIGUES_CANCEL_OK)) {
                ligaActual.setEstado("Cancelado");
                finalizarBoton.setText("Cancelado");
                finalizarBoton.setDisable(true);
            }

        } catch (IOException ex) {
            Logger.getLogger(TorneoController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void cargarClasificacion() {

        cargarBoton();

        //nombreEquipo:partidosJugados:puntos:puntosAFavor:puntosEnContra:Diferencia;
        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {

                try {
                    out.println(Mensajes.LIGUES_SCORE + ";" + ligaActual.getId());
                    String fromServer = in.readLine();
                    String[] datos = fromServer.split(";");

                    if (datos[0].equals(Mensajes.LIGUES_SCORE_OK)) {
                        List<Puntuacion> clasificacion = new LinkedList<>();

                        for (int i = 1; i < datos.length; i++) {
                            String[] clasificacionPartes = datos[i].split(":");
                            Puntuacion p = new Puntuacion(clasificacionPartes[0], Integer.parseInt(clasificacionPartes[1]), Integer.parseInt(clasificacionPartes[2]),
                                    Integer.parseInt(clasificacionPartes[3]), Integer.parseInt(clasificacionPartes[4]), Integer.parseInt(clasificacionPartes[5]));
                            clasificacion.add(p);
                        }

                        Collections.sort(clasificacion);

                        ObservableList<Puntuacion> puntuaciones = FXCollections.observableList(clasificacion);

                        tablaLiga.setItems(puntuaciones);

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
            App.cargando.completado();
        });

        Thread thread = new Thread(task);
        thread.start();
        App.cargando.cargando(App.mainStage.getX(), App.mainStage.getY());

    }

    public Liga getLigaActual() {
        return ligaActual;
    }

    public void setLigaActual(Liga ligaActual) {
        this.ligaActual = ligaActual;
        nombreLigaCabezera.setText(ligaActual.getNombre());
    }
}
