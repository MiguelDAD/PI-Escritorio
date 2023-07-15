/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.miguel.pi.cliente;

import com.miguel.pi.cliente.App;
import com.miguel.pi.cliente.Datos.Liga;
import com.miguel.pi.cliente.TarjetaLigaController;
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
public class LigaTarjetaAdapter extends ListCell<Liga> {

    public LigaTarjetaAdapter() {
        super();

        setOnMouseClicked(event -> {
            if (!isEmpty()) {
                try {
                    Parent root = null;

                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("InformacionLiga.fxml"));

                    root = fxmlLoader.load();

                    InformacionLigaController infoLiga = fxmlLoader.getController();

                    infoLiga.setLigaActual(getItem());
                    infoLiga.cargarDatos();

                    App.cambiarVentana(root);
                } catch (IOException ex) {
                    Logger.getLogger(TorneoTarjetaAdapter.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    @Override
    public void updateItem(Liga liga, boolean empty) {
        super.updateItem(liga, empty);
        try {
            if (empty || liga == null) {
                Platform.runLater(() -> {
                    // Realiza aquí las operaciones relacionadas con la interfaz gráfica
                    this.setGraphic(null);
                });
            }
            if (liga != null && !empty) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(App.class.getResource("TarjetaLiga.fxml"));

                AnchorPane pane = loader.load();

                TarjetaLigaController ligaController = loader.getController();

                ligaController.rellenarTarjeta(liga);
                Platform.runLater(() -> {
                    // Realiza aquí las operaciones relacionadas con la interfaz gráfica
                    this.setGraphic(pane);
                });

            }
        } catch (Exception ex) {
            System.out.println("No se pudo actualizar la tarjeta");
        }
    }
}
