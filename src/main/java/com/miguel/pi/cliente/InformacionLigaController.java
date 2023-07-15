/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.miguel.pi.cliente;

import com.miguel.pi.cliente.Datos.Liga;
import com.miguel.pi.cliente.Utilidades.Geocoding;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
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
public class InformacionLigaController implements Initializable {

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
    private Label nombreLiga;
    @FXML
    private Label costeLiga;
    @FXML
    private Label maxEquiposLiga;
    @FXML
    private Label minEquiposLiga;
    @FXML
    private Label frecuenciaJornada;
    @FXML
    private Label hInicioLiga;
    @FXML
    private Label fInicioLiga;
    @FXML
    private Label hLimiteLiga;
    @FXML
    private Label fLimiteLiga;
    @FXML
    private Label deporteLiga;
    @FXML
    private Label horaFinPartidoLiga;
    @FXML
    private Label horaInicioPartidoLiga;
    @FXML
    private Label ubicacionLiga;
    @FXML
    private Label nombreLigaCabezera;

    private Liga ligaActual;

    private Socket serverSocket;
    private PrintWriter out;
    private BufferedReader in;
    @FXML
    private Label duracionPartidos;

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

    public void cargarDatos() {
        if (ligaActual != null) {
            nombreLigaCabezera.setText(ligaActual.getNombre());
            nombreLiga.setText(ligaActual.getNombre());

            String[] latiudLongitud = ligaActual.getUbicacion().split(",");

            String latitud = latiudLongitud[0].replaceAll(" ", "");
            String longitud = latiudLongitud[1].replaceAll(" ", "");

            ubicacionLiga.setText(Geocoding.obtenerNombre(latitud, longitud));
            costeLiga.setText(ligaActual.getCoste() + " €");
            maxEquiposLiga.setText(ligaActual.getMaxEquipos() + "");
            minEquiposLiga.setText(ligaActual.getMinEquipos() + "");
            hInicioLiga.setText(ligaActual.getHoraInicio() + "");
            fInicioLiga.setText(ligaActual.getFechaInicio());
            hLimiteLiga.setText(ligaActual.getHoraLimite());
            fLimiteLiga.setText(ligaActual.getFechaLimite());
            deporteLiga.setText(ligaActual.getDeporte());
            frecuenciaJornada.setText(ligaActual.getFrecuenciaJornada()+"");
            duracionPartidos.setText(ligaActual.getDuracionPartidos()+"");
            horaInicioPartidoLiga.setText(ligaActual.gethInicioPartidos());
            horaFinPartidoLiga.setText(ligaActual.gethFinPartidos());
        }
    }

    public Liga getLigaActual() {
        return ligaActual;
    }

    public void setLigaActual(Liga ligaActual) {
        this.ligaActual = ligaActual;
    }
}
