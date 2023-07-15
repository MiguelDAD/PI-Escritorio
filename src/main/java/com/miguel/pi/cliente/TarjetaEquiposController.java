/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.miguel.pi.cliente;

import com.miguel.pi.cliente.Datos.Equipo;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyListView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author Miguel
 */
public class TarjetaEquiposController implements Initializable {

    @FXML
    private Label nombreEquipo;
    @FXML
    private Label liderEquipo;
    @FXML
    private MFXLegacyListView<String> integrantesEquipo;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void rellenarTarjeta(Equipo e) {
        nombreEquipo.setText(e.getNombreEquipo());
        liderEquipo.setText(e.getLider());
        ObservableList<String> integrantes = FXCollections.observableList(e.getIntegrantes());

        integrantesEquipo.setItems(integrantes);
    }

}
