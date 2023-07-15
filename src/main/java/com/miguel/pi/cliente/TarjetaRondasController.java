/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.miguel.pi.cliente;

import com.miguel.pi.cliente.Datos.TorneoEnfrentamientos;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author Miguel
 */
public class TarjetaRondasController implements Initializable {

    @FXML
    private Label eLocal;
    @FXML
    private Label eVisitante;
    @FXML
    private Label ptosLocal;
    @FXML
    private Label ptosVisitantes;

    /**
     * Initializes the controller class.
     */
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void rellenarTarjeta(TorneoEnfrentamientos te) {
        eLocal.setText(te.getEquipoLocal());
        eVisitante.setText(te.getEquipoVisitante());
        ptosLocal.setText(te.getPtosLocal()+"");
        ptosVisitantes.setText(te.getPtosVisitante()+"");
    }

}
