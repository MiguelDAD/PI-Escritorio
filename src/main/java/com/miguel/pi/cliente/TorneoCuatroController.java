/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.miguel.pi.cliente;

import com.miguel.pi.cliente.Datos.TorneoEnfrentamientos;
import com.miguel.pi.cliente.Datos.Torneo;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyListCell;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Label;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.miguel.pi.cliente.Datos.Equipo;
import com.miguel.pi.cliente.LoginController;
import com.miguel.pi.cliente.Utilidades.Mensajes;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyListView;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

/**
 * FXML Controller class
 *
 * @author Miguel
 */
public class TorneoCuatroController implements Initializable {

    @FXML
    private Label nombreUusario;
    @FXML
    private MFXButton btnTorneo;
    @FXML
    private MFXButton btnLiga;
    @FXML
    private MFXButton datosTorneo;
    @FXML
    private MFXButton equipoTorneo;
    @FXML
    private MFXButton rondasTorneo;
    private MFXLegacyListView<TorneoEnfrentamientos> enfrentamiento1;
    @FXML
    private Label nombreTorneoCabezera;
    @FXML
    private Label version;
    @FXML
    private MFXLegacyListView<TorneoEnfrentamientos> enfrentamiento11;
    @FXML
    private MFXLegacyListView<TorneoEnfrentamientos> enfrentamiento21;
    @FXML
    private MFXLegacyListView<TorneoEnfrentamientos> enfrentamiento12;

    private Torneo torneoActual;

    private Socket serverSocket;
    private PrintWriter out;
    private BufferedReader in;
    @FXML
    private MFXButton finalizarBoton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        version.setText(App.version);

        serverSocket = App.serverSocket;
        out = App.out;
        in = App.in;

        nombreUusario.setText(App.nombre);

    }

    @FXML
    private void cambiarPanel(ActionEvent event) throws IOException {
        Parent root = null;

        //SEGUN LA ID DEL BOTON LANZA UNA U OTRA VENTANA
        if (event.getSource() == btnTorneo) {
            root = FXMLLoader.load(getClass().getResource("Torneo.fxml"));
        } else if (event.getSource() == btnLiga) {
            root = FXMLLoader.load(getClass().getResource("Liga.fxml"));
        }

        App.cambiarVentana(root);
    }

    @FXML
    private void cambiarPestania(ActionEvent event) throws IOException {

        Parent root = null;
        FXMLLoader fxmlLoader = new FXMLLoader();

        //SEGUN LA ID DEL BOTON LANZA UNA U OTRA VENTANA
        if (event.getSource() == datosTorneo) {
            fxmlLoader.setLocation(getClass().getResource("InformacionTorneo.fxml"));

            root = fxmlLoader.load();

            InformacionTorneoController infoTorneo = fxmlLoader.getController();

            infoTorneo.setTorneoActual(torneoActual);
            infoTorneo.cargarDatos();

        } else if (event.getSource() == equipoTorneo) {
            fxmlLoader.setLocation(getClass().getResource("EquiposTorneo.fxml"));

            root = fxmlLoader.load();

            EquiposTorneoController equipo = fxmlLoader.getController();

            equipo.setTorneoActual(torneoActual);
            equipo.insertarEquipos();
        } else if (event.getSource() == rondasTorneo) {

            if (torneoActual.getMaxEquipos() == 4) {
                fxmlLoader.setLocation(getClass().getResource("TorneoCuatro.fxml"));
                root = fxmlLoader.load();
                TorneoCuatroController torneoCuatro = fxmlLoader.getController();
                torneoCuatro.setTorneoActual(torneoActual);
                torneoCuatro.insertarEquipos();
            } else if (torneoActual.getMaxEquipos() == 8) {
                fxmlLoader.setLocation(getClass().getResource("TorneoOcho.fxml"));
                root = fxmlLoader.load();
                TorneoOchoController torneoOcho = fxmlLoader.getController();
                torneoOcho.setTorneoActual(torneoActual);
                torneoOcho.insertarEquipos();
            }

        }

        App.cambiarVentana(root);

    }

    //SEV-TOURNAMENTS-ROUNDS;3;1;122;?;xa;?!4;1;Pa;?;Prueba;?!:5;2;?;?;?;?!
    public void insertarEquipos() {

        cargarBoton();

        Queue<MFXLegacyListView<TorneoEnfrentamientos>> rondas1 = new LinkedList<>();
        rondas1.add(enfrentamiento11);
        rondas1.add(enfrentamiento12);

        Queue<MFXLegacyListView<TorneoEnfrentamientos>> rondas2 = new LinkedList<>();
        rondas2.add(enfrentamiento21);

        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {

                out.println(Mensajes.TOURNAMENTS_ROUNDS + ";" + torneoActual.getId());
                try {
                    String fromServer = in.readLine();
                    String[] mensajeRespuesta = fromServer.split(";");
                    System.out.println("DEL SERVER: " + fromServer);
                    if (mensajeRespuesta[0].equals(Mensajes.TOURNAMENTS_ROUNDS_OK)) {

                        if (mensajeRespuesta.length == 1) {
                            return false;
                        }
                        String[] rondas = mensajeRespuesta[1].split(":");

                        for (int i = 0; i < rondas.length; i++) {
                            String[] equiposRonda = rondas[i].split("!");
                            for (int j = 0; j < equiposRonda.length; j++) {
                                String[] partes = equiposRonda[j].split("/");
                                String id = partes[0];
                                String rondaEnfrentamiento = partes[1];
                                String eLocal = partes[2];
                                String ptosLocal = partes[3];
                                String eVisitante = partes[4];
                                String ptosVisitante = partes[5];
                                String idTorneo = partes[6];

                                //EL NUMERO DE LA RONDA
                                if (rondaEnfrentamiento.equalsIgnoreCase("1")) {
                                    MFXLegacyListView<TorneoEnfrentamientos> r1 = rondas1.poll();
                                    if (r1 != null) {
                                        r1.setCellFactory(param -> new TorneoRondasAdapter());
                                        TorneoEnfrentamientos te = new TorneoEnfrentamientos(Integer.parseInt(id), eLocal, ptosLocal,
                                                eVisitante, ptosVisitante, Integer.parseInt(rondaEnfrentamiento));
                                        te.setIdTorneo(torneoActual.getId());
                                        r1.getItems().add(te);

                                    }
                                } else if (rondaEnfrentamiento.equalsIgnoreCase("2")) {
                                    MFXLegacyListView<TorneoEnfrentamientos> r2 = rondas2.poll();
                                    if (r2 != null) {
                                        r2.setCellFactory(param -> new TorneoRondasAdapter());
                                        TorneoEnfrentamientos te = new TorneoEnfrentamientos(Integer.parseInt(id), eLocal, ptosLocal,
                                                eVisitante, ptosVisitante, Integer.parseInt(rondaEnfrentamiento));
                                        te.setIdTorneo(torneoActual.getId());
                                        r2.getItems().add(te);
                                    }
                                }
                            }

                        }
                        return true;
                        //SEV-TOURNAMENTS-ROUNDS;3;1;122;?;xa;?!4;1;Pa;?;Prueba;?!:5;2;?;?;?;?!

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
            App.cargando.completado();
        });

        Thread thread = new Thread(task);
        thread.start();
        App.cargando.cargando(App.mainStage.getX(), App.mainStage.getY());

    }

    public Torneo getTorneoActual() {
        return torneoActual;
    }

    public void setTorneoActual(Torneo torneoActual) {
        this.torneoActual = torneoActual;
        nombreTorneoCabezera.setText(torneoActual.getNombre());
    }

    private void cargarBoton() {
        switch (torneoActual.getEstado()) {
            case "Jugandose":
                finalizarBoton.setText("Finalizar");
                finalizarBoton.setDisable(false);
                break;
            case "Esperando Jugadores":
                finalizarBoton.setText("Cancelar");
                finalizarBoton.setDisable(false);
                break;
            case "Cancelado":
                finalizarBoton.setText("Cancelado");
                finalizarBoton.setDisable(true);
                break;
            case "Finalizado":
                finalizarBoton.setText("Finalizado");
                finalizarBoton.setDisable(true);
                break;

            default:
                finalizarBoton.setDisable(false);
                finalizarBoton.setText("Cancelar");
        }

    }

    @FXML
    private void finalizarTorneo(ActionEvent event) {

        String enviar = "";

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Terminar torneo");
        alert.setHeaderText("Seguro que deseas terminar el torneo?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            switch (torneoActual.getEstado()) {
                case "Jugandose":
                    finalizarBoton.setText("Finalizar");
                    finalizarBoton.setDisable(false);
                    enviar = Mensajes.TOURNAMENTS_END + ";" + torneoActual.getId();
                    break;
                case "Esperando Jugadores":
                    finalizarBoton.setText("Cancelar");
                    finalizarBoton.setDisable(false);
                    enviar = Mensajes.TOURNAMENTS_CANCEL + ";" + torneoActual.getId();
                    break;
            }

            enviarDato(enviar);
        }

    }

    private void enviarDato(String enviar) {

        if (enviar.equalsIgnoreCase("")) {
            return;
        }

        out.println(enviar);
        try {
            String fromServer = in.readLine();
            System.out.println("DEL SERVER: " + fromServer);

            if (fromServer.equalsIgnoreCase(Mensajes.TOURNAMENTS_END_OK)) {
                torneoActual.setEstado("Finalizado");
                finalizarBoton.setText("Finalizado");
                finalizarBoton.setDisable(true);
            }
            if (fromServer.equalsIgnoreCase(Mensajes.TOURNAMENTS_CANCEL_OK)) {
                torneoActual.setEstado("Cancelado");
                finalizarBoton.setText("Cancelado");
                finalizarBoton.setDisable(true);
            }

        } catch (IOException ex) {
            Logger.getLogger(TorneoController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
