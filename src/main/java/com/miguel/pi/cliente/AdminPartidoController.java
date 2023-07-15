/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.miguel.pi.cliente;

import com.miguel.pi.cliente.Datos.AdminDeporte;
import com.miguel.pi.cliente.Datos.AdminEquipo;
import com.miguel.pi.cliente.Datos.AdminPartido;
import com.miguel.pi.cliente.Utilidades.Filtrado;
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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;

import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Miguel
 */
public class AdminPartidoController implements Initializable, Filtrado {

    @FXML
    private Label nombreUusario;
    @FXML
    private Label version;
    @FXML
    private MFXButton btnUsuario;
    @FXML
    private MFXButton btnEquipos;
    @FXML
    private MFXButton btnTorneos;
    @FXML
    private MFXButton btnLigas;
    @FXML
    private MFXButton btnPartidos;
    @FXML
    private TableView<AdminPartido> tablaPartido;
    @FXML
    private TableColumn<AdminPartido, String> deporteColumna;
    @FXML
    private TableColumn<AdminPartido, String> organizadorColumna;
    @FXML
    private TableColumn<AdminPartido, String> ubicacionColumna;
    @FXML
    private MFXButton eliminarBoton;
    @FXML
    private Button filtrarDatos;
    @FXML
    private MFXButton btnDeportes;
    @FXML
    private TableColumn<AdminPartido, String> fechaDeJuego;

    private Socket serverSocket;
    private PrintWriter out;
    private BufferedReader in;
    @FXML
    private TableColumn<AdminPartido, String> estadoColumna;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        version.setText(App.version);
        nombreUusario.setText(App.nombre);

        serverSocket = App.serverSocket;
        out = App.out;
        in = App.in;

        deporteColumna.setCellValueFactory(new PropertyValueFactory("deporte"));
        organizadorColumna.setCellValueFactory(new PropertyValueFactory("lider"));
        ubicacionColumna.setCellValueFactory(new PropertyValueFactory("direccion"));
        estadoColumna.setCellValueFactory(new PropertyValueFactory("estado"));
        fechaDeJuego.setCellValueFactory(new PropertyValueFactory("finicio"));

        rellenarTabla();
    }

    private void rellenarTabla() {
        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                tablaPartido.getItems().clear();

                out.println(Mensajes.SHOW_GAME_ADMIN);
                try {
                    String fromServer = in.readLine();
                    String[] mensajeRespuesta = fromServer.split(";");

                    List<AdminPartido> partidos = new ArrayList<>();

                    if (mensajeRespuesta[0].equals(Mensajes.SHOW_GAME_ADMIN_OK)) {
                        for (int i = 1; i < mensajeRespuesta.length; i++) {
                            String[] partesPartido = mensajeRespuesta[i].split("!");
                            int id = Integer.parseInt(partesPartido[0]);
                            String[] ubicacion = partesPartido[3].replaceAll(" ", "").split(",");

                            AdminPartido d = new AdminPartido(id, partesPartido[1], partesPartido[2], ubicacion[0], ubicacion[1], partesPartido[4], partesPartido[5]);

                            partidos.add(d);
                        }
                        ObservableList<AdminPartido> todoPartidos = FXCollections.observableList(partidos);

                        tablaPartido.setItems(todoPartidos);

                        return true;

                    } else if (mensajeRespuesta[0].equals(Mensajes.SHOW_GAME_ADMIN_ERROR)) {
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
        if (event.getSource() == btnUsuario) {
            root = FXMLLoader.load(getClass().getResource("AdminUsuario.fxml"));
        } else if (event.getSource() == btnEquipos) {
            root = FXMLLoader.load(getClass().getResource("AdminEquipo.fxml"));
        } else if (event.getSource() == btnTorneos) {
            root = FXMLLoader.load(getClass().getResource("AdminTorneo.fxml"));
        } else if (event.getSource() == btnLigas) {
            root = FXMLLoader.load(getClass().getResource("AdminLiga.fxml"));
        } else if (event.getSource() == btnPartidos) {
            root = FXMLLoader.load(getClass().getResource("AdminPartido.fxml"));
        } else if (event.getSource() == btnDeportes) {
            root = FXMLLoader.load(getClass().getResource("AdminDeporte.fxml"));
        }

        App.cambiarVentana(root);
    }

    @FXML
    private void activarBloqueo(MouseEvent event) {
        if (tablaPartido.getSelectionModel().getSelectedItem() == null) {
            return;
        }
        eliminarBoton.setDisable(false);
    }

    private void desactivarBotones() {
        eliminarBoton.setDisable(true);
    }

    @FXML
    private void eliminarPartido(ActionEvent event) {
        AdminPartido eSeleccionado = tablaPartido.getSelectionModel().getSelectedItem();

        //Evita errores
        if (eSeleccionado == null) {
            desactivarBotones();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Borrado equipo");
        alert.setHeaderText("Deseas borrar el partido?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            realizarBorrado(eSeleccionado);
        } else {
            tablaPartido.getSelectionModel().clearSelection();
            desactivarBotones();
        }

    }

    private void realizarBorrado(AdminPartido eSeleccionado) {
        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                out.println(Mensajes.DELETE_GAME_ADMIN + ";" + eSeleccionado.getId());
                try {
                    String fromServer = in.readLine();

                    if (fromServer.equals(Mensajes.DELETE_GAME_ADMIN_OK)) {
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

                    tablaPartido.getItems().removeAll(eSeleccionado);
                    tablaPartido.getSelectionModel().clearSelection();
                    desactivarBotones();

                } else {
                    tablaPartido.getSelectionModel().clearSelection();
                    desactivarBotones();
                }
            } catch (Exception ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
            App.cargando.completado();

        });

        Thread thread = new Thread(task);
        thread.start();
        App.cargando.cargando(App.mainStage.getX(), App.mainStage.getY());
    }

    @FXML
    private void filtradoDatos(ActionEvent event) {
        //CREAR LA VENTANA
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("FiltrarDatos.fxml"));

            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("Filtrar Partidos");
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

    @Override
    public void filtrarDatos(String deporte, String estado) {
        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                tablaPartido.getItems().clear();
                System.out.println("HOLA");
                out.println(Mensajes.SHOW_GAME_ADMIN);
                try {
                    String fromServer = in.readLine();
                    String[] mensajeRespuesta = fromServer.split(";");

                    List<AdminPartido> partidos = new ArrayList<>();

                    if (mensajeRespuesta[0].equals(Mensajes.SHOW_GAME_ADMIN_OK)) {
                        for (int i = 1; i < mensajeRespuesta.length; i++) {
                            String[] partesPartido = mensajeRespuesta[i].split("!");
                            int id = Integer.parseInt(partesPartido[0]);
                            String[] ubicacion = partesPartido[3].replaceAll(" ", "").split(",");

                            AdminPartido l = new AdminPartido(id, partesPartido[1], partesPartido[2], ubicacion[0], ubicacion[1], partesPartido[4], partesPartido[5]);
                            if (!deporte.isEmpty() && !estado.isEmpty()) {
                                if (l.getDeporte().equalsIgnoreCase(deporte) && l.getEstado().equalsIgnoreCase(estado)) {
                                    partidos.add(l);
                                }
                            } else if (!deporte.isEmpty()) {
                                if (l.getDeporte().equalsIgnoreCase(deporte)) {
                                    partidos.add(l);
                                }
                            } else if (!estado.isEmpty()) {
                                if (l.getEstado().equalsIgnoreCase(estado)) {
                                    partidos.add(l);
                                }
                            } else {
                                partidos.add(l);
                            }
                        }
                        ObservableList<AdminPartido> todoPartidos = FXCollections.observableList(partidos);

                        tablaPartido.setItems(todoPartidos);

                        return true;

                    } else if (mensajeRespuesta[0].equals(Mensajes.SHOW_GAME_ADMIN_ERROR)) {
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
            App.cargando.completado();
        });

        Thread thread = new Thread(task);
        thread.start();
        App.cargando.cargando(App.mainStage.getX(), App.mainStage.getY());
    }

}
