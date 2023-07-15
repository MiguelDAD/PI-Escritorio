/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.miguel.pi.cliente;

import com.miguel.pi.cliente.App;
import com.miguel.pi.cliente.Datos.Equipo;
import com.miguel.pi.cliente.TarjetaEquiposController;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author Miguel
 */
public class EquipoTarjetaAdapter extends ListCell<Equipo> {

    public EquipoTarjetaAdapter() {
        super();

        /*
        setOnMouseClicked(event -> {
            if (!isEmpty()) {
                try {
                    Parent root = null;

                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("InformacionTorneo.fxml"));

                    root = fxmlLoader.load();

                    InformacionTorneoController infoTorneo = fxmlLoader.getController();
                    
                    infoTorneo.setTorneoActual(getItem());
                    infoTorneo.cargarDatos();
                    
                    App.cambiarVentana(root);
                } catch (IOException ex) {
                    Logger.getLogger(TorneoTarjetaAdapter.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });*/
    }

    @Override
    public void updateItem(Equipo equipo, boolean empty) {
        super.updateItem(equipo, empty);
        if (empty || equipo == null) {
            Platform.runLater(() -> {
                // Realiza aquí las operaciones relacionadas con la interfaz gráfica
                this.setGraphic(null);
            });
        }

        if (equipo != null && !empty) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(App.class.getResource("TarjetaEquipos.fxml"));

                AnchorPane pane = loader.load();

                TarjetaEquiposController equipoController = loader.getController();

                equipoController.rellenarTarjeta(equipo);
                Platform.runLater(() -> {
                    // Realiza aquí las operaciones relacionadas con la interfaz gráfica
                    this.setGraphic(pane);
                });

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
