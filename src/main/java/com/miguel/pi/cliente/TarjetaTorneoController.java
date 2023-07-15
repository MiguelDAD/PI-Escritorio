/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.miguel.pi.cliente;

import com.miguel.pi.cliente.Datos.Torneo;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Miguel
 */
public class TarjetaTorneoController implements Initializable {

    @FXML
    private Label nombreTorneo;
    @FXML
    private Label costeTorneo;
    @FXML
    private Label hInicioTorneo;
    @FXML
    private Label fInicioTorneo;
    @FXML
    private Label inscritosTorneo;
    @FXML
    private Label deporteTorneo;
    @FXML
    private AnchorPane fondo;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void rellenarTarjeta(Torneo torneo) {
        nombreTorneo.setText(torneo.getNombre());
        costeTorneo.setText(torneo.getCoste() + "€");
        hInicioTorneo.setText(torneo.getHoraInicio());
        fInicioTorneo.setText(torneo.getFechaInicio());
        inscritosTorneo.setText(torneo.getEquiposInscritos() + " / " + torneo.getMaxEquipos());
        deporteTorneo.setText(torneo.getDeporte());

        switch (torneo.getEstado()) {
            case "Jugandose":
                fondo.setStyle("-fx-background-color:  #B0F2C2;");
                break;
            case "Esperando Jugadores":
                fondo.setStyle("-fx-background-color:  #EDEDAF;");
                break;
            case "Cancelado":
                fondo.setStyle("-fx-background-color:  #FFB6AF;");
                break;
            case "Finalizado":
                fondo.setStyle("-fx-background-color:  #B0F6F2;");
                break;

            default:
                fondo.setStyle("-fx-background-color:  #FFB6AF;");

        }

    }

}
