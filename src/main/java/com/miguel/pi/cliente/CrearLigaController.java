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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Miguel
 */
public class CrearLigaController implements Initializable {

    @FXML
    private MFXButton crearLigaBtn;
    @FXML
    private MFXTextField nombreLiga;
    @FXML
    private MFXDatePicker fInicioLiga;
    @FXML
    private MFXTextField ubicacionLiga;
    @FXML
    private MFXLegacyComboBox<String> hInicioLiga;
    @FXML
    private MFXLegacyComboBox<String> mInicioLiga;
    @FXML
    private MFXTextField costeLiga;
    @FXML
    private MFXDatePicker fLimiteLiga;
    @FXML
    private MFXComboBox<String> deporteLiga;
    @FXML
    private MFXLegacyComboBox<String> hLimiteLiga;
    @FXML
    private MFXLegacyComboBox<String> mLimiteLiga;
    @FXML
    private MFXComboBox<Integer> maxEquiposLiga;
    @FXML
    private MFXComboBox<Integer> minEquipoLiga;
    @FXML
    private MFXLegacyComboBox<String> hInicioPartidos;
    @FXML
    private MFXLegacyComboBox<String> mInicioPartidos;
    @FXML
    private MFXLegacyComboBox<String> hFinPartidos;
    @FXML
    private MFXLegacyComboBox<String> mFinPartidos;
    @FXML
    private MFXTextField frecuenciaJornada;
    @FXML
    private MFXTextField duracionPartido;

    private Socket serverSocket;
    private PrintWriter out;
    private BufferedReader in;

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

    @FXML
    private void crearLigaAccion(ActionEvent event) {
        if (comprobarCampos()) {
            //nombre!ubicacion!coste!maxEquipos!minEquipos!horaInicio!fechaInicio!horaLimite!fechaLimite
            //!deporte!frecuenciaJornada!duracionPartido!horaInicioPartidos!horaFinPartidos 
            String nombre = nombreLiga.getText();
            String ubicacionEscrita = ubicacionLiga.getText();
            String ubicacion = Geocoding.LatitudLongitud(ubicacionEscrita);
            String coste = costeLiga.getText();
            String maxE = maxEquiposLiga.getSelectedItem() + "";
            String minE = minEquipoLiga.getSelectedItem() + "";
            String hInicio = hInicioLiga.getValue() + ":" + mInicioLiga.getValue();
            String fInicio = fInicioLiga.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String hLimite = hLimiteLiga.getValue() + ":" + mLimiteLiga.getValue();
            String fLimite = fLimiteLiga.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String deporte = deporteLiga.getSelectedItem();
            String frecuenciaJ = frecuenciaJornada.getText();
            String duracionP = duracionPartido.getText();
            String horaInicioP = hInicioPartidos.getValue() + ":" + mInicioPartidos.getValue();
            String horaFinP = hFinPartidos.getValue() + ":" + mFinPartidos.getValue();

            String liga = nombre + "!" + ubicacion + "!" + coste + "!" + maxE + "!" + minE + "!" + hInicio + "!" + fInicio + "!"
                    + hLimite + "!" + fLimite + "!" + deporte + "!" + frecuenciaJ + "!" + duracionP + "!" + horaInicioP + "!" + horaFinP;

            crearLiga(liga);
        }
    }

    private boolean comprobarCampos() {

        //COMPROBAR QUE LOS CAMPOS ESTEN RELLENOS
        if (nombreLiga.getText().isEmpty() || ubicacionLiga.getText().isEmpty() || costeLiga.getText().isEmpty()
                || maxEquiposLiga.getSelectedIndex() < 0 || minEquipoLiga.getSelectedIndex() < 0 || hInicioLiga.getValue().isEmpty() || mInicioLiga.getValue().isEmpty()
                || hLimiteLiga.getValue().isEmpty() || mLimiteLiga.getValue().isEmpty() || deporteLiga.getSelectedIndex() < 0 || frecuenciaJornada.getText().isEmpty()
                || duracionPartido.getText().isEmpty() || hInicioPartidos.getValue().isEmpty() || mInicioPartidos.getValue().isEmpty()
                || hFinPartidos.getValue().isEmpty() || mFinPartidos.getValue().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Error");
            alert.setContentText("Faltan campos por rellenar");
            alert.showAndWait();
            return false;
        }

        try {
            fInicioLiga.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            fLimiteLiga.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Error");
            alert.setContentText("Fecha incorrecta");
            alert.showAndWait();
            return false;
        }
        //COMPROBAR LAS FECHAS BIEN
        if (!Validador.isDouble(costeLiga.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Error");
            alert.setContentText("El coste no es un numero decimal");
            alert.showAndWait();
            return false;
        }
        
        if(Validador.superaPrecioMaximo(costeLiga.getText())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Error");
            alert.setContentText("El coste maximos es 999€");
            alert.showAndWait();
            return false;
        }
        if(Validador.precioNegativo(costeLiga.getText())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Error");
            alert.setContentText("El precio no puede ser negativo");
            alert.showAndWait();
            return false;
        }
        
        
        //COMPROBAR LAS FECUENCIAS BIEN
        if (!Validador.isDouble(duracionPartido.getText())||!Validador.isDouble(frecuenciaJornada.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Error");
            alert.setContentText("Tienes que establecer enteros para las frecuencias");
            alert.showAndWait();
            return false;
        }
        
        if (Validador.fechaAntesQueHoy(fLimiteLiga.getValue())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Error");
            alert.setContentText("La fecha limite tiene que ser superior a hoy");
            alert.showAndWait();
            return false;
        }

        if (!Validador.fechaAntesQue(fLimiteLiga.getValue(), fInicioLiga.getValue())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Error");
            alert.setContentText("La fecha de inicio del liga tiene que ser superior a la fecha limite de inscripccion");
            alert.showAndWait();
            return false;
        }

        return true;
    }

    private void crearLiga(String liga) {

        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {

                out.println(Mensajes.LIGUES_CREATE + ";" + liga);
                try {
                    String fromServer = in.readLine();

                    if (fromServer.equals(Mensajes.LIGUES_OK_CREATE)) {
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

            if (respuesta) {

                //Obtengo la escena del controlador
                Stage stage = (Stage) crearLigaBtn.getScene().getWindow();
                //Lo cierro
                stage.close();

            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setTitle("Error");
                alert.setContentText("Error al insertar la liga");
                alert.showAndWait();
                //Obtengo la escena del controlador
                Stage stage = (Stage) crearLigaBtn.getScene().getWindow();
                //Lo cierro
                stage.close();
            }

        });

        Thread thread = new Thread(task);
        thread.start();

    }

    @FXML

    private void cambiarMinEquiposDisponibles(ActionEvent event) {
        int minimo = 2;
        int maximo = maxEquiposLiga.getSelectedItem() - 2;

        minEquipoLiga.setValue(null);

        List<Integer> numerosMinimos = new ArrayList<>();

        for (int i = minimo; i <= maximo; i++) {
            numerosMinimos.add(i);
        }

        ObservableList<Integer> minEquipos = FXCollections.observableList(numerosMinimos);

        minEquipoLiga.setItems(minEquipos);
        minEquipoLiga.getSelectionModel().selectIndex(0);
    }

    private void insertarMaxEquipos() {

        List<Integer> maxEquipos = new LinkedList<>();

        for (int i = 4; i <= 24; i++) {
            maxEquipos.add(i);
        }

        ObservableList<Integer> maxEquip = FXCollections.observableList(maxEquipos);

        maxEquiposLiga.setItems(maxEquip);
        maxEquiposLiga.getSelectionModel().selectIndex(0);
    }

    private void insertarHoras() {
        //Horas
        for (int i = 0; i < 24; i++) {
            hInicioLiga.getItems().add(String.format("%2s", i + "").replace(' ', '0'));
            hLimiteLiga.getItems().add(String.format("%2s", i + "").replace(' ', '0'));
            hInicioPartidos.getItems().add(String.format("%2s", i + "").replace(' ', '0'));
            hFinPartidos.getItems().add(String.format("%2s", i + "").replace(' ', '0'));
        }

        //Minutos
        for (int i = 0; i < 60; i++) {
            mInicioLiga.getItems().add(String.format("%2s", i + "").replace(' ', '0'));
            mLimiteLiga.getItems().add(String.format("%2s", i + "").replace(' ', '0'));
            mInicioPartidos.getItems().add(String.format("%2s", i + "").replace(' ', '0'));
            mFinPartidos.getItems().add(String.format("%2s", i + "").replace(' ', '0'));
        }

        hInicioLiga.getSelectionModel().selectFirst();
        hLimiteLiga.getSelectionModel().selectFirst();
        hInicioPartidos.getSelectionModel().selectFirst();
        hFinPartidos.getSelectionModel().selectFirst();
        mInicioLiga.getSelectionModel().selectFirst();
        mLimiteLiga.getSelectionModel().selectFirst();
        mInicioPartidos.getSelectionModel().selectFirst();
        mFinPartidos.getSelectionModel().selectFirst();
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

                        deporteLiga.setItems(todosDeportes);
                        deporteLiga.getSelectionModel().selectIndex(0);
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

}
