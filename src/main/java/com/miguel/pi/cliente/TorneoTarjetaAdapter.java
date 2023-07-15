/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.miguel.pi.cliente;

import com.miguel.pi.cliente.App;
import com.miguel.pi.cliente.Datos.Torneo;
import com.miguel.pi.cliente.InformacionTorneoController;
import com.miguel.pi.cliente.TarjetaTorneoController;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author Miguel
 */
public class TorneoTarjetaAdapter extends ListCell<Torneo> {

    public TorneoTarjetaAdapter() {
        super();

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
        });

    }

    @Override
    public void updateItem(Torneo torneo, boolean empty) {
        super.updateItem(torneo, empty);
        try {
            if (empty || torneo == null) {
                Platform.runLater(() -> {
                    // Realiza aquí las operaciones relacionadas con la interfaz gráfica
                    this.setGraphic(null);
                });
            }
            if (torneo != null && !empty) {

                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(App.class.getResource("TarjetaTorneo.fxml"));

                AnchorPane pane = loader.load();

                TarjetaTorneoController torneoController = loader.getController();

                torneoController.rellenarTarjeta(torneo);
                Platform.runLater(() -> {
                    // Realiza aquí las operaciones relacionadas con la interfaz gráfica
                    this.setGraphic(pane);
                });

            }
        } catch (Exception ex) {
            System.out.println("No se pudo actualizar la tarjeta");;
        }
    }

}
