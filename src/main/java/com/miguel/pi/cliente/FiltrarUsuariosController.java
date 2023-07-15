/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.miguel.pi.cliente;

import com.miguel.pi.cliente.Utilidades.Filtrado;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Miguel
 */
public class FiltrarUsuariosController implements Initializable {

    @FXML
    private MFXButton filtrarBtn;
    @FXML
    private MFXTextField nombreUsuario;
    private Filtrado aplicacion;

    public void setAplicacion(Filtrado aplicacion) {
        this.aplicacion = aplicacion;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void filtrarDatos(ActionEvent event) {
        Stage stage = (Stage) filtrarBtn.getScene().getWindow();
        stage.close();
        
        aplicacion.filtrarDatos(nombreUsuario.getText(), "");
    }

}
