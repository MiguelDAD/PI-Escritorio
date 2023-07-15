/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.miguel.pi.cliente;

import com.miguel.pi.cliente.Utilidades.Geocoding;
import com.miguel.pi.cliente.Datos.Torneo;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.io.IOException;
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
public class InformacionTorneoController implements Initializable {

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
    private Label nombreTorneo;
    @FXML
    private Label ubicacionTorneo;
    @FXML
    private Label costeTorneo;
    @FXML
    private Label maxEquiposTorneo;
    @FXML
    private Label minEquiposTorneo;
    @FXML
    private Label hInicioTorneo;
    @FXML
    private Label fInicioTorneo;
    @FXML
    private Label hLimiteTorneo;
    @FXML
    private Label fLimiteTorneo;
    @FXML
    private Label deporteTorneo;

    private Torneo torneoActual;
    @FXML
    private Label nombreTorneoCabezera;
    @FXML
    private Label version;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        version.setText(App.version);

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
    }

    public void cargarDatos() {
        if (torneoActual != null) {
            nombreTorneoCabezera.setText(torneoActual.getNombre());
            nombreTorneo.setText(torneoActual.getNombre());

            String[] latiudLongitud = torneoActual.getUbicacion().split(",");

            String latitud = latiudLongitud[0].replaceAll(" ", "");
            String longitud = latiudLongitud[1].replaceAll(" ", "");

            ubicacionTorneo.setText(Geocoding.obtenerNombre(latitud, longitud));
            costeTorneo.setText(torneoActual.getCoste() + " €");
            maxEquiposTorneo.setText(torneoActual.getMaxEquipos() + "");
            minEquiposTorneo.setText(torneoActual.getMinEquipos() + "");
            hInicioTorneo.setText(torneoActual.getHoraInicio() + "");
            fInicioTorneo.setText(torneoActual.getFechaInicio());
            hLimiteTorneo.setText(torneoActual.getHoraLimite());
            fLimiteTorneo.setText(torneoActual.getFechaLimite());
            deporteTorneo.setText(torneoActual.getDeporte());
        }
    }

}
