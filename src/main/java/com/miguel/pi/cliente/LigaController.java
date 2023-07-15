/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.miguel.pi.cliente;

import com.miguel.pi.cliente.Utilidades.Mensajes;
import com.miguel.pi.cliente.Datos.Liga;
import com.miguel.pi.cliente.Utilidades.Filtrado;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyListView;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Miguel
 */
public class LigaController implements Initializable, Filtrado {

    @FXML
    private Label nombreUusario;
    @FXML
    private MFXButton btnTorneo;
    @FXML
    private MFXButton btnLiga;
    @FXML
    private Label version;
    @FXML
    private MFXButton btnCrearLiga;
    @FXML
    private MFXLegacyListView<Liga> ligaListView;

    private Socket serverSocket;
    private PrintWriter out;
    private BufferedReader in;
    @FXML
    private Button filtrarDatos;

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

        recibirLigas();
    }

    private void recibirLigas() {

        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {

                ligaListView.getItems().clear();
                out.println(Mensajes.LIGUES_ORGANIZER);
                try {
                    String fromServer = in.readLine();
                    String[] mensajeRespuesta = fromServer.split(";");
                    System.out.println("DEL SERVER: " + fromServer);
                    if (mensajeRespuesta[0].equals(Mensajes.LIGUES_SEND_ORGANIZER)) {
                        for (int i = 1; i < mensajeRespuesta.length; i++) {
                            String[] partesTorneo = mensajeRespuesta[i].split("!");

                            //id!nombre!ubicacion!coste!maxEquipos!minEquipos!horaInicio!fechaInicio!horaLimite!fechaLimite
                            //!deporte!frecuenciaJornada!duracionPartido!horaInicioPartidos!horaFinPartidos!estado!equiposInscritos
                            Liga l = new Liga(Integer.parseInt(partesTorneo[0]), partesTorneo[1], partesTorneo[2], Double.parseDouble(partesTorneo[3]), Integer.parseInt(partesTorneo[4]),
                                    Integer.parseInt(partesTorneo[5]), partesTorneo[6], partesTorneo[7], partesTorneo[8], partesTorneo[9], partesTorneo[10],
                                    Integer.parseInt(partesTorneo[11]), Integer.parseInt(partesTorneo[12]), partesTorneo[13], partesTorneo[14],
                                    partesTorneo[15], Integer.parseInt(partesTorneo[16]));

                            ligaListView.getItems().add(l);
                        }

                        return true;

                    } else if (mensajeRespuesta[0].equals(Mensajes.LIGUES_ERROR_SEND_ORGANIZER)) {
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
            boolean respuesta = task.getValue();

            if (respuesta) {
                ligaListView.setCellFactory(param -> new LigaTarjetaAdapter());
            } else {
                ligaListView.getItems().clear();
            }
            App.cargando.completado();
        });

        Thread thread = new Thread(task);
        thread.start();
        App.cargando.cargando(App.mainStage.getX(), App.mainStage.getY());

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
    private void crearLiga(ActionEvent event) {
        //CREAR LA VENTANA
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("CrearLiga.fxml"));

            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("Crear Liga");
            Image icon = new Image(getClass().getResourceAsStream("/com/miguel/pi/cliente/img/logo.png"));

            stage.getIcons().add(icon);

            stage.setScene(scene);
            stage.showAndWait();
            recibirLigas();

        } catch (IOException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Fallo al crear la nueva ventana", e);
        }

    }

    @FXML
    private void modificarPerfil(MouseEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("ModificarPerfil.fxml"));

            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("Modificar perfil");
            Image icon = new Image(getClass().getResourceAsStream("/com/miguel/pi/cliente/img/logo.png"));

            stage.getIcons().add(icon);

            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Fallo al crear la nueva ventana", e);
        }
    }

    @FXML
    private void filtradoDatos(ActionEvent event) {
        //CREAR LA VENTANA
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("FiltrarDatos.fxml"));

            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("Filtrar Liga");
            Image icon = new Image(getClass().getResourceAsStream("/com/miguel/pi/cliente/img/logo.png"));

            FiltrarDatosController filtrado = fxmlLoader.getController();
            filtrado.setAplicacion(this);

            stage.getIcons().add(icon);

            stage.setScene(scene);
            stage.showAndWait();

        } catch (IOException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Fallo al crear la nueva ventana", e);
        }
    }

    @Override
    public void filtrarDatos(String deporte, String estado) {

        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {

                ligaListView.getItems().clear();
                ligaListView.refresh();

                out.println(Mensajes.LIGUES_ORGANIZER);
                try {
                    String fromServer = in.readLine();
                    String[] mensajeRespuesta = fromServer.split(";");
                    System.out.println("DEL SERVER: " + fromServer);
                    if (mensajeRespuesta[0].equals(Mensajes.LIGUES_SEND_ORGANIZER)) {
                        List<Liga> ligas = new LinkedList<>();
                        for (int i = 1; i < mensajeRespuesta.length; i++) {
                            String[] partesTorneo = mensajeRespuesta[i].split("!");

                            //id!nombre!ubicacion!coste!maxEquipos!minEquipos!horaInicio!fechaInicio!horaLimite!fechaLimite
                            //!deporte!frecuenciaJornada!duracionPartido!horaInicioPartidos!horaFinPartidos!estado!equiposInscritos
                            Liga l = new Liga(Integer.parseInt(partesTorneo[0]), partesTorneo[1], partesTorneo[2], Double.parseDouble(partesTorneo[3]), Integer.parseInt(partesTorneo[4]),
                                    Integer.parseInt(partesTorneo[5]), partesTorneo[6], partesTorneo[7], partesTorneo[8], partesTorneo[9], partesTorneo[10],
                                    Integer.parseInt(partesTorneo[11]), Integer.parseInt(partesTorneo[12]), partesTorneo[13], partesTorneo[14],
                                    partesTorneo[15], Integer.parseInt(partesTorneo[16]));

                            if (!deporte.isEmpty() && !estado.isEmpty()) {
                                if (l.getDeporte().equalsIgnoreCase(deporte) && l.getEstado().equalsIgnoreCase(estado)) {
                                    ligas.add(l);
                                }
                            } else if (!deporte.isEmpty()) {
                                if (l.getDeporte().equalsIgnoreCase(deporte)) {
                                    ligas.add(l);
                                }
                            } else if (!estado.isEmpty()) {
                                if (l.getEstado().equalsIgnoreCase(estado)) {
                                    ligas.add(l);
                                }
                            } else {
                                ligas.add(l);
                            }

                        }

                        ObservableList<Liga> ligaObser = FXCollections.observableList(ligas);
                        ligaListView.setItems(ligaObser);

                        return true;

                    } else if (mensajeRespuesta[0].equals(Mensajes.LIGUES_ERROR_SEND_ORGANIZER)) {
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

        task.setOnSucceeded(event
                -> {
            boolean respuesta = task.getValue();

            if (respuesta) {
                ligaListView.setCellFactory(param -> new LigaTarjetaAdapter());
            } else {
                ligaListView.getItems().clear();
            }
            App.cargando.completado();
        }
        );

        Thread thread = new Thread(task);
        thread.start();
        App.cargando.cargando(App.mainStage.getX(), App.mainStage.getY());

    }

}
