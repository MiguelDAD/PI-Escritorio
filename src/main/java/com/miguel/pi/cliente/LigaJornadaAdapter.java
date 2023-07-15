/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.miguel.pi.cliente;

import com.miguel.pi.cliente.Datos.Jornada;
import com.miguel.pi.cliente.Datos.TorneoEnfrentamientos;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author Miguel
 */
public class LigaJornadaAdapter extends ListCell<Jornada> {

    public LigaJornadaAdapter() {
        super();

        setOnMouseClicked(event -> {
            if (!isEmpty()) {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("InsertarResultadoJornada.fxml"));
                Parent root = null;
                try {
                    root = loader.load();
                } catch (IOException ex) {
                    Logger.getLogger(TorneoRondasAdapter.class.getName()).log(Level.SEVERE, null, ex);
                }

                InsertarResultadoJornadaController controller = loader.getController();
                controller.cargarResultado(getItem());
                Scene scene = new Scene(root);
                Stage dialog = new Stage();
                dialog.initOwner(new Stage());
                dialog.setScene(scene);
                dialog.showAndWait();

                getListView().refresh();

            }
        });
        
    }

    @Override
    public void updateItem(Jornada jornada, boolean empty) {
        super.updateItem(jornada, empty);
        try {
            if (empty || jornada == null) {
                Platform.runLater(() -> {
                    // Realiza aquí las operaciones relacionadas con la interfaz gráfica
                    this.setGraphic(null);
                });
            }
            if (jornada != null && !empty) {

                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(App.class.getResource("TarjetaJornada.fxml"));

                AnchorPane pane = loader.load();

                TarjetaJornadaController rondas = loader.getController();

                rondas.rellenarTarjeta(jornada);
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
