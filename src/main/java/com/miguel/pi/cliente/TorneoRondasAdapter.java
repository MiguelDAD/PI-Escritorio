/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.miguel.pi.cliente;

import com.miguel.pi.cliente.App;
import com.miguel.pi.cliente.InsertarResultadoRondaController;
import com.miguel.pi.cliente.TarjetaRondasController;
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
public class TorneoRondasAdapter extends ListCell<TorneoEnfrentamientos> {

    public TorneoRondasAdapter() {
        super();

        setOnMouseClicked(event -> {
            if (!isEmpty()) {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("InsertarResultadoRonda.fxml"));
                Parent root = null;
                try {
                    root = loader.load();
                } catch (IOException ex) {
                    Logger.getLogger(TorneoRondasAdapter.class.getName()).log(Level.SEVERE, null, ex);
                }

                InsertarResultadoRondaController controller = loader.getController();
                controller.setEnfrentamiento(getItem());
                controller.insertarEquipos();
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
    public void updateItem(TorneoEnfrentamientos enfrentamiento, boolean empty) {
        super.updateItem(enfrentamiento, empty);
        try {
            if (empty || enfrentamiento == null) {
                Platform.runLater(() -> {
                    // Realiza aquí las operaciones relacionadas con la interfaz gráfica
                    this.setGraphic(null);
                });
            }
            if (enfrentamiento != null && !empty) {

                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(App.class.getResource("TarjetaRondas.fxml"));

                AnchorPane pane = loader.load();

                TarjetaRondasController rondas = loader.getController();

                rondas.rellenarTarjeta(enfrentamiento);
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
