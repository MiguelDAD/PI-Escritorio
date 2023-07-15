/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.miguel.pi.cliente;

import com.miguel.pi.cliente.Datos.TorneoEnfrentamientos;
import com.miguel.pi.cliente.Datos.Equipo;
import com.miguel.pi.cliente.Utilidades.Mensajes;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
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
public class InsertarResultadoRondaController implements Initializable {

    @FXML
    private MFXTextField resultadoEquipo1;
    @FXML
    private MFXComboBox<Equipo> equipo1;
    @FXML
    private MFXComboBox<Equipo> equipo2;
    @FXML
    private MFXTextField resultadoEquipo2;
    @FXML
    private MFXButton guardarBton;

    private Socket serverSocket;
    private PrintWriter out;
    private BufferedReader in;

    private TorneoEnfrentamientos enfrentamiento;

    private ObservableList<Equipo> todosEquipos;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        serverSocket = App.serverSocket;
        out = App.out;
        in = App.in;
    }

    @FXML
    private void guardarResultado(ActionEvent event) {

        if (equipo1.getSelectionModel().getSelectedItem() == null) {
            datosIncorrecto();
            return;
        }
        if (equipo2.getSelectionModel().getSelectedItem() == null) {
            datosIncorrecto();
            return;
        }

        String eLocal = equipo1.getSelectedItem().getNombreEquipo();
        String eVisitante = equipo2.getSelectedItem().getNombreEquipo();

        int ptosLocal, ptosVisitantes;

        try {
            ptosLocal = Integer.parseInt(resultadoEquipo1.getText());
            ptosVisitantes = Integer.parseInt(resultadoEquipo2.getText());

        } catch (Exception e) {
            datosIncorrecto();
            return;
        }

        //IdRonda;eL;pL;eV;pV
        String mensaje = Mensajes.SAVE_ROUNDS + ";" + enfrentamiento.getId() + ";" + eLocal + ";" + ptosLocal + ";" + eVisitante + ";" + ptosVisitantes;
        enviarCambio(mensaje);

    }

    private void enviarCambio(String mensaje) {

        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {

                try {
                    out.println(mensaje);
                    String fromServer = in.readLine();
                    String[] datos = fromServer.split(";");

                    if (datos[0].equals(Mensajes.SAVE_ROUNDS_OK)) {

                        return true;

                    } else {

                        return false;

                    }

                } catch (IOException ex) {
                    Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                }
                return false;
            }
        };

        task.setOnSucceeded(event -> {
            boolean respuesta = task.getValue();

            if (respuesta) {

                String eLocal = equipo1.getSelectedItem().getNombreEquipo();
                String eVisitante = equipo2.getSelectedItem().getNombreEquipo();

                int ptosLocal = Integer.parseInt(resultadoEquipo1.getText());
                int ptosVisitantes = Integer.parseInt(resultadoEquipo2.getText());

                enfrentamiento.setEquipoLocal(eLocal);
                enfrentamiento.setEquipoVisitante(eVisitante);
                enfrentamiento.setPtosLocal("" + ptosLocal);
                enfrentamiento.setPtosVisitante("" + ptosVisitantes);

                Stage stage = (Stage) guardarBton.getScene().getWindow();
                stage.close();

            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setTitle("Error");
                alert.setContentText("Error al guardar la ronda del torneo");
                alert.showAndWait();
                //Obtengo la escena del controlador
                Stage stage = (Stage) guardarBton.getScene().getWindow();
                //Lo cierro
                stage.close();
            }

        });

        Thread thread = new Thread(task);
        thread.start();

    }

    private void datosIncorrecto() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setTitle("Error");
        alert.setContentText("Los datos son incorrectos");
        alert.showAndWait();
    }

    public void insertarEquipos() {

        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                try {
                    out.println(Mensajes.TOURNAMENT_TEAMS + ";" + enfrentamiento.getIdTorneo());
                    String fromServer = in.readLine();
                    String[] datos = fromServer.split(";");

                    List<Equipo> equipos = new LinkedList();

                    if (datos[0].equals(Mensajes.TOURNAMENT_TEAMS_OK)) {
                        for (int i = 1; i < datos.length; i++) {
                            String[] partesEquipos = datos[i].split(":");
                            String nombreEqupo = partesEquipos[0];
                            String lider = partesEquipos[1];
                            
                            System.out.println(nombreEqupo + " " + lider + " ");
                            Equipo e = new Equipo(nombreEqupo, lider);

                            if (partesEquipos.length > 2) {
                                String[] integrantes = partesEquipos[2].split("!");
                                for (String participante : integrantes) {
                                    e.insertarUsuario(participante);
                                }

                            }

                            equipos.add(e);
                        }
                        equipos.add(new Equipo("?",""));

                        todosEquipos = FXCollections.observableList(equipos);

                        equipo1.setItems(todosEquipos);
                        equipo2.setItems(todosEquipos);

                        resultadoEquipo1.setText(enfrentamiento.getPtosLocal());
                        resultadoEquipo2.setText(enfrentamiento.getPtosVisitante());

                        for (int i = 0; i < todosEquipos.size(); i++) {
                            try {
                                String equipo = todosEquipos.get(i).getNombreEquipo();
                                if (equipo.equalsIgnoreCase(enfrentamiento.getEquipoLocal())) {
                                    equipo1.getSelectionModel().selectIndex(i);
                                }
                                if (equipo.equalsIgnoreCase(enfrentamiento.getEquipoVisitante())) {
                                    equipo2.getSelectionModel().selectIndex(i);
                                }
                            } catch (Exception e) {
                                System.out.println("No hay equipo seleccionado");
                            }

                        }

                        return true;

                    } else if (datos[0].equals(Mensajes.TOURNAMENT_TEAMS_ERROR)) {
                        return false;

                    } else {
                        return false;
                    }

                } catch (IOException ex) {
                    Logger.getLogger(InsertarResultadoRondaController.class.getName()).log(Level.SEVERE, null, ex);
                }

                return false;
            }
        };

        Thread thread = new Thread(task);
        thread.start();

    }

    private void establecerValores() {
        resultadoEquipo1.setText(enfrentamiento.getPtosLocal());
        resultadoEquipo2.setText(enfrentamiento.getPtosVisitante());

        for (int i = 0; i < todosEquipos.size(); i++) {

            String equipo = todosEquipos.get(i).getNombreEquipo();
            if (equipo.equalsIgnoreCase(enfrentamiento.getEquipoLocal())) {
                equipo1.getSelectionModel().selectIndex(i);
            }
            if (equipo.equalsIgnoreCase(enfrentamiento.getEquipoVisitante())) {
                equipo2.getSelectionModel().selectIndex(i);
            }

        }

    }

    public TorneoEnfrentamientos getEnfrentamiento() {
        return enfrentamiento;
    }

    public void setEnfrentamiento(TorneoEnfrentamientos enfrentamiento) {
        this.enfrentamiento = enfrentamiento;
    }

}
