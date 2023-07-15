/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.miguel.pi.cliente;

import com.miguel.pi.cliente.Utilidades.Geocoding;
import com.miguel.pi.cliente.Utilidades.Mensajes;
import com.miguel.pi.cliente.Utilidades.Validador;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyComboBox;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
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
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Miguel
 */
public class CrearTorneoController implements Initializable {

    @FXML
    private MFXTextField nombreTorneo;
    @FXML
    private MFXTextField ubicacionTorneo;
    @FXML
    private MFXTextField costeTorneo;
    @FXML
    private MFXLegacyComboBox<String> hInicioTorneo;
    @FXML
    private MFXLegacyComboBox<String> hLimiteTorneo;
    @FXML
    private MFXDatePicker fInicioTorneo;
    @FXML
    private MFXDatePicker fLimiteTorneo;
    @FXML
    private MFXComboBox<Integer> maxEquiposTorneo;
    @FXML
    private MFXComboBox<Integer> minEquipoTorneo;
    @FXML
    private MFXComboBox<String> deporteTorneo;

    private Socket serverSocket;
    private PrintWriter out;
    private BufferedReader in;
    @FXML
    private MFXButton crearTorneoBtn;
    @FXML
    private MFXLegacyComboBox<String> mLimiteTorneo;
    @FXML
    private MFXLegacyComboBox<String> mInicioTorneo;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        serverSocket = App.serverSocket;
        out = App.out;
        in = App.in;

        insertarDeportes();
        insertarMaxEquipos();
        insertarHoras();
    }

    private void insertarHoras() {
        //Horas
        for (int i = 0; i < 24; i++) {
            //AÑADE A LAS CADENAS QUE TENGAN UN SOLO CARACTER UN = 0 a la izquierda
            hInicioTorneo.getItems().add(String.format("%2s", i + "").replace(' ', '0'));
            hLimiteTorneo.getItems().add(String.format("%2s", i + "").replace(' ', '0'));
        }

        //Minutos
        for (int i = 0; i < 60; i++) {
            mInicioTorneo.getItems().add(String.format("%2s", i + "").replace(' ', '0'));
            mLimiteTorneo.getItems().add(String.format("%2s", i + "").replace(' ', '0'));
        }

        hInicioTorneo.getSelectionModel().selectFirst();
        hLimiteTorneo.getSelectionModel().selectFirst();
        mInicioTorneo.getSelectionModel().selectFirst();
        mLimiteTorneo.getSelectionModel().selectFirst();
    }

    private void insertarDeportes() {
        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {

                out.println(Mensajes.GET_SPORT);
                try {
                    String fromServer = in.readLine();
                    String[] mensajeRespuesta = fromServer.split(";");

                    List<String> deportes = new ArrayList<>();

                    if (mensajeRespuesta[0].equals(Mensajes.SEND_SPORT)) {
                        for (int i = 1; i < mensajeRespuesta.length; i++) {
                            String[] partesDeporte = mensajeRespuesta[i].split(":");

                            deportes.add(partesDeporte[0]);
                        }
                        ObservableList<String> todosDeportes = FXCollections.observableList(deportes);

                        deporteTorneo.setItems(todosDeportes);
                        deporteTorneo.getSelectionModel().selectIndex(0);
                        return true;

                    } else if (mensajeRespuesta[0].equals(Mensajes.ERROR_SEND_SPORT)) {
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

        Thread thread = new Thread(task);
        thread.start();

    }

    private void insertarMaxEquipos() {

        Integer[] maxEquipo = {4, 8};

        List<Integer> maxEquipos = Arrays.asList(maxEquipo);

        ObservableList<Integer> maxEquip = FXCollections.observableList(maxEquipos);

        maxEquiposTorneo.setItems(maxEquip);
        maxEquiposTorneo.getSelectionModel().selectIndex(0);
    }

    @FXML
    private void cambiarMinEquiposDisponibles(ActionEvent event) {

        int minimo = 3;
        int maximo = maxEquiposTorneo.getSelectedItem() - 1;

        minEquipoTorneo.setValue(null);

        List<Integer> numerosMinimos = new ArrayList<>();

        for (int i = minimo; i <= maximo; i++) {
            numerosMinimos.add(i);
        }

        ObservableList<Integer> minEquipos = FXCollections.observableList(numerosMinimos);

        minEquipoTorneo.setItems(minEquipos);
        minEquipoTorneo.getSelectionModel().selectIndex(0);
    }

    @FXML
    private void crearTorneoAccion(ActionEvent event) {

        if (comprobarCampos()) {

            //nombre!ubicacion!coste!maxEquipos!minEquipos!horaInicio!fechaInicio!horaLimite!fechaLimite!deporte
            String nombre = nombreTorneo.getText();
            String ubicacionEscrita = ubicacionTorneo.getText();
            String ubicacion = Geocoding.LatitudLongitud(ubicacionEscrita);
            String coste = costeTorneo.getText();
            String maxE = maxEquiposTorneo.getSelectedItem() + "";
            String minE = minEquipoTorneo.getSelectedItem() + "";
            String hInicio = hInicioTorneo.getValue() + ":" + mInicioTorneo.getValue();
            String fInicio = fInicioTorneo.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String hLimite = hLimiteTorneo.getValue() + ":" + mLimiteTorneo.getValue();
            String fLimite = fLimiteTorneo.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String deporte = deporteTorneo.getSelectedItem();

            String torneo = nombre + "!" + ubicacion + "!" + coste + "!" + maxE + "!" + minE + "!" + hInicio + "!" + fInicio + "!"
                    + hLimite + "!" + fLimite + "!" + deporte;

            enviarTorneo(torneo);
        }
    }

    private boolean comprobarCampos() {

        //COMPROBAR QUE LOS CAMPOS ESTEN RELLENOS
        if (nombreTorneo.getText().isEmpty() || ubicacionTorneo.getText().isEmpty() || costeTorneo.getText().isEmpty()
                || maxEquiposTorneo.getSelectedIndex() < 0 || minEquipoTorneo.getSelectedIndex() < 0 || hInicioTorneo.getValue().isEmpty() || mInicioTorneo.getValue().isEmpty()
                || hLimiteTorneo.getValue().isEmpty() || mLimiteTorneo.getValue().isEmpty() || deporteTorneo.getSelectedIndex() < 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Error");
            alert.setContentText("Faltan campos por rellenar");
            alert.showAndWait();
            return false;
        }

        try {
            fInicioTorneo.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            fLimiteTorneo.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Error");
            alert.setContentText("Fecha incorrecta");
            alert.showAndWait();
            return false;
        }
        //COMPROBAR LAS FECHAS BIEN
        if (!Validador.isDouble(costeTorneo.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Error");
            alert.setContentText("El coste no es un numero decimal");
            alert.showAndWait();
            return false;
        }
        
        if(Validador.superaPrecioMaximo(costeTorneo.getText())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Error");
            alert.setContentText("El coste maximos es 999€");
            alert.showAndWait();
            return false;
        }
        if(Validador.precioNegativo(costeTorneo.getText())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Error");
            alert.setContentText("El precio no puede ser negativo");
            alert.showAndWait();
            return false;
        }
        
        if (Validador.fechaAntesQueHoy(fLimiteTorneo.getValue())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Error");
            alert.setContentText("La fecha limite tiene que ser superior a hoy");
            alert.showAndWait();
            return false;
        }

        if (!Validador.fechaAntesQue(fLimiteTorneo.getValue(), fInicioTorneo.getValue())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Error");
            alert.setContentText("La fecha de inicio del torneo tiene que ser superior a la fecha limite de inscripccion");
            alert.showAndWait();
            return false;
        }

        return true;
    }

    private void enviarTorneo(String torneo) {
        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {

                out.println(Mensajes.TOURNAMENT_CREATE + ";" + torneo);
                try {
                    String fromServer = in.readLine();

                    if (fromServer.equals(Mensajes.TOURNAMENT_OK_CREATE)) {

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

                    //Obtengo la escena del controlador
                    Stage stage = (Stage) crearTorneoBtn.getScene().getWindow();
                    //Lo cierro
                    stage.close();

                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setTitle("Error");
                    alert.setContentText("Error al insertar el torneo");
                    alert.showAndWait();
                    //Obtengo la escena del controlador
                    Stage stage = (Stage) crearTorneoBtn.getScene().getWindow();
                    //Lo cierro
                    stage.close();
                }
            } catch (Exception ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        Thread thread = new Thread(task);
        thread.start();
    }

}
