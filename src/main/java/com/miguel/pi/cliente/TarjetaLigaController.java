/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.miguel.pi.cliente;

import com.miguel.pi.cliente.Datos.Liga;
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
public class TarjetaLigaController implements Initializable {


    @FXML
    private Label nombreLiga;
    @FXML
    private Label costeLiga;
    @FXML
    private Label hInicioLiga;
    @FXML
    private Label fInicioLiga;
    @FXML
    private Label inscritosLiga;
    @FXML
    private Label deporteLiga;
    @FXML
    private AnchorPane fondo;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }  
    
    public void rellenarTarjeta (Liga liga){
        nombreLiga.setText(liga.getNombre());
        costeLiga.setText(liga.getCoste()+"€");
        hInicioLiga.setText(liga.getHoraInicio());
        fInicioLiga.setText(liga.getFechaInicio());
        inscritosLiga.setText(liga.getEquiposInscritos()+" / "+liga.getMaxEquipos());
        deporteLiga.setText(liga.getDeporte());
        
        switch (liga.getEstado()) {
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
