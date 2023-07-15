/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.miguel.pi.cliente;

import com.miguel.pi.cliente.Datos.AdminUsuario;
import com.miguel.pi.cliente.Datos.Peticion;
import com.miguel.pi.cliente.Utilidades.Mensajes;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Miguel
 */
public class PeticionesDeporteController implements Initializable {

    @FXML
    private TableView<Peticion> tablaPeticiones;
    @FXML
    private TableColumn<Peticion, String> deporte;
    @FXML
    private TableColumn<Peticion, Integer> ctdad;
    @FXML
    private TableColumn<Peticion, Integer> ctdadE;
    @FXML
    private TableColumn<Peticion, String> iExtra;
    @FXML
    private TableColumn<Peticion, String> solicitante;
    @FXML
    private MFXButton aceptarBoton;
    @FXML
    private MFXButton rechazarBoton;

    private Socket serverSocket;
    private PrintWriter out;
    private BufferedReader in;
    @FXML
    private AnchorPane cargando;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        serverSocket = App.serverSocket;
        out = App.out;
        in = App.in;
        cargando.setVisible(false);

        deporte.setCellValueFactory(new PropertyValueFactory("nombreDto"));
        ctdad.setCellValueFactory(new PropertyValueFactory("ctdadTotal"));
        ctdadE.setCellValueFactory(new PropertyValueFactory("ctdadEquipo"));
        iExtra.setCellValueFactory(new PropertyValueFactory("informacionExtra"));
        solicitante.setCellValueFactory(new PropertyValueFactory("solicitante"));
        rellenarTabla();
    }

    private void cargando() {
        cargando.setVisible(true);

    }

    private void completado() {
        cargando.setVisible(false);
    }

    private void rellenarTabla() {
        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                tablaPeticiones.getItems().clear();

                out.println(Mensajes.PETITION_SPORT);
                try {
                    String fromServer = in.readLine();
                    String[] mensajeRespuesta = fromServer.split(";");

                    List<Peticion> peticion = new ArrayList<>();

                    if (mensajeRespuesta[0].equals(Mensajes.PETITION_SPORT_OK)) {
                        for (int i = 1; i < mensajeRespuesta.length; i++) {
                            String[] partesPeticion = mensajeRespuesta[i].split(":");
                            Peticion u = new Peticion(Integer.parseInt(partesPeticion[0]), partesPeticion[1], Integer.parseInt(partesPeticion[2]), Integer.parseInt(partesPeticion[3]), partesPeticion[4], partesPeticion[5]);

                            peticion.add(u);
                        }
                        ObservableList<Peticion> todoPeticiones = FXCollections.observableList(peticion);

                        tablaPeticiones.setItems(todoPeticiones);

                        return true;

                    } else if (mensajeRespuesta[0].equals(Mensajes.PETITION_SPORT_ERROR)) {
                        return false;
                    } else if (mensajeRespuesta[0].equals(Mensajes.ERROR)) {
                        return false;
                    }

                } catch (IOException ex) {
                    Logger.getLogger(TorneoController.class.getName()).log(Level.SEVERE, null, ex);
                }
                return false;
            }
        };

        task.setOnSucceeded(event -> {
            completado();
        });

        Thread thread = new Thread(task);
        thread.start();
        cargando();

    }

    @FXML
    private void activarBloqueo(MouseEvent event) {
        if (tablaPeticiones.getSelectionModel().getSelectedItem() == null) {
            return;
        }
        aceptarBoton.setDisable(false);
        rechazarBoton.setDisable(false);
    }

    private void desactivarBotones() {
        aceptarBoton.setDisable(true);
        rechazarBoton.setDisable(true);

    }

    @FXML
    private void aceptarPeticion(ActionEvent event) {
        Peticion eSeleccionado = tablaPeticiones.getSelectionModel().getSelectedItem();

        //Evita errores
        if (eSeleccionado == null) {
            desactivarBotones();
            return;
        }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("AniadirDeporte.fxml"));

            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("Añadir deporte");
            Image icon = new Image(getClass().getResourceAsStream("/com/miguel/pi/cliente/img/logo.png"));

            AniadirDeporteController aniadir = fxmlLoader.getController();
            aniadir.prerellenar(eSeleccionado.getNombreDto(), "" + eSeleccionado.getCtdadTotal(), "" + eSeleccionado.getCtdadEquipo());

            stage.getIcons().add(icon);

            stage.setScene(scene);
            stage.showAndWait();
            decicionPeticion(eSeleccionado, "SI");

        } catch (IOException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Fallo al crear la nueva ventana", e);
        }

        tablaPeticiones.getSelectionModel().clearSelection();
        desactivarBotones();
        rellenarTabla();

        
    }

    private void decicionPeticion(Peticion peticion, String decicion) {
        String mensaje = Mensajes.PETITION_ANSWER + ";" + peticion.getId() + ";" + decicion;

        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                out.println(mensaje);
                try {
                    String fromServer = in.readLine();

                    if (fromServer.equals(Mensajes.PETITION_ANSWER_OK)) {
                        return true;

                    } else {
                        return false;

                    }

                } catch (IOException ex) {
                    Logger.getLogger(TorneoController.class.getName()).log(Level.SEVERE, null, ex);
                }
                return false;

            }

        };

        task.setOnSucceeded(event -> {
            boolean respuesta = task.getValue();
            try {
                if (respuesta) {

                    tablaPeticiones.getItems().removeAll(peticion);
                    tablaPeticiones.getSelectionModel().clearSelection();
                    desactivarBotones();

                } else {
                    tablaPeticiones.getSelectionModel().clearSelection();
                    desactivarBotones();
                }
            } catch (Exception ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
            completado();

        });

        Thread thread = new Thread(task);
        thread.start();
        cargando();

    }

    @FXML
    private void recharPeticion(ActionEvent event) {
        Peticion eSeleccionado = tablaPeticiones.getSelectionModel().getSelectedItem();

        //Evita errores
        if (eSeleccionado == null) {
            desactivarBotones();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Rechazar peticion");
        alert.setHeaderText("Deseas rechazar la peticion?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            decicionPeticion(eSeleccionado, "NO");
        } else {
            tablaPeticiones.getSelectionModel().clearSelection();
            desactivarBotones();
        }
    }
}
