/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.miguel.pi.cliente;

import com.miguel.pi.cliente.Datos.Jornada;
import com.miguel.pi.cliente.Datos.Liga;
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
public class TarjetaJornadaController implements Initializable {


    @FXML
    private Label eLocal;
    @FXML
    private Label ptosLocal;
    @FXML
    private Label eVisitante;
    @FXML
    private Label ptosVisitante;
    @FXML
    private Label fecha;
    @FXML
    private Label hora;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    } 
    
    public void rellenarTarjeta(Jornada jornada) {
        eLocal.setText(jornada.getEquipoLocal());
        ptosLocal.setText(jornada.getPtosLocal());
        eVisitante.setText(jornada.getEquipoVisitante());
        ptosVisitante.setText(jornada.getPtosVisitante());
        fecha.setText(jornada.getFecha());
        hora.setText(jornada.getHora());

    }
    
}
