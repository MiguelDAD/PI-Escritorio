/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.miguel.pi.cliente;

import com.miguel.pi.cliente.Datos.AdminEquipo;
import com.miguel.pi.cliente.Datos.AdminUsuario;
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
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Miguel
 */
public class AdminUsuarioController implements Initializable, Filtrado {

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
    private MFXButton btnDeportes;
    @FXML
    private TableView<AdminUsuario> tablaUsuario;
    @FXML
    private TableColumn<AdminUsuario, String> nombreColumna;
    @FXML
    private TableColumn<AdminUsuario, String> emailColumna;
    @FXML
    private TableColumn<AdminUsuario, String> rolesColumna;
    @FXML
    private Button filtrarDatos;
    @FXML
    private MFXButton darAdminBtn;

    private Socket serverSocket;
    private PrintWriter out;
    private BufferedReader in;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        version.setText(App.version);
        nombreUusario.setText(App.nombre);

        serverSocket = App.serverSocket;
        out = App.out;
        in = App.in;

        nombreColumna.setCellValueFactory(new PropertyValueFactory("nombre"));
        emailColumna.setCellValueFactory(new PropertyValueFactory("email"));
        rolesColumna.setCellValueFactory(new PropertyValueFactory("roles"));
        rellenarTabla();
    }

    private void rellenarTabla() {
        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                tablaUsuario.getItems().clear();

                out.println(Mensajes.SHOW_USER_ADMIN);
                try {
                    String fromServer = in.readLine();
                    String[] mensajeRespuesta = fromServer.split(";");

                    List<AdminUsuario> usuarios = new ArrayList<>();

                    if (mensajeRespuesta[0].equals(Mensajes.SHOW_USER_ADMIN_OK)) {
                        for (int i = 1; i < mensajeRespuesta.length; i++) {
                            String[] partesUsuario = mensajeRespuesta[i].split(":");
                            AdminUsuario u = new AdminUsuario(Integer.parseInt(partesUsuario[0]), partesUsuario[1], partesUsuario[2], partesUsuario[3]);

                            usuarios.add(u);
                        }
                        ObservableList<AdminUsuario> todoUsuarios = FXCollections.observableList(usuarios);

                        tablaUsuario.setItems(todoUsuarios);

                        return true;

                    } else if (mensajeRespuesta[0].equals(Mensajes.SHOW_USER_ADMIN_ERROR)) {
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
    private void filtradoDatos(ActionEvent event) {
        //CREAR LA VENTANA
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("FiltrarUsuarios.fxml"));

            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("Filtrar usuarios");
            Image icon = new Image(getClass().getResourceAsStream("/com/miguel/pi/cliente/img/logo.png"));

            FiltrarUsuariosController filtrado = fxmlLoader.getController();
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

    @FXML
    private void activarBloqueo(MouseEvent event) {
        if (tablaUsuario.getSelectionModel().getSelectedItem() == null) {
            return;
        }
        darAdminBtn.setDisable(false);

    }

    private void desactivarBotones() {
        darAdminBtn.setDisable(true);

    }

    @FXML
    private void hacerAdministrador(ActionEvent event) {
        AdminUsuario eSeleccionado = tablaUsuario.getSelectionModel().getSelectedItem();

        //Evita errores
        if (eSeleccionado == null) {
            desactivarBotones();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Asignar administrador");
        alert.setHeaderText("Deseas asignar administrador?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            darAdministrador(eSeleccionado);
        } else {
            tablaUsuario.getSelectionModel().clearSelection();
            desactivarBotones();
        }
    }

    private void darAdministrador(AdminUsuario uSeleccionado) {
        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                out.println(Mensajes.ADD_USERADMIN_ADMIN + ";" + uSeleccionado.getId());
                try {
                    String fromServer = in.readLine();

                    if (fromServer.equals(Mensajes.ADD_USERADMIN_ADMIN_OK)) {
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
            App.cargando.completado();

            try {
                if (respuesta) {
                    rellenarTabla();
                    tablaUsuario.getSelectionModel().clearSelection();
                    desactivarBotones();

                } else {
                    tablaUsuario.getSelectionModel().clearSelection();
                    desactivarBotones();
                }
            } catch (Exception ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }

        });
        Thread thread = new Thread(task);
        thread.start();
        App.cargando.cargando(App.mainStage.getX(), App.mainStage.getY());
    }

    @Override
    public void filtrarDatos(String dato1, String dato2) {
        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                tablaUsuario.getItems().clear();

                out.println(Mensajes.SHOW_USER_ADMIN);
                try {
                    String fromServer = in.readLine();
                    String[] mensajeRespuesta = fromServer.split(";");

                    List<AdminUsuario> usuarios = new ArrayList<>();

                    if (mensajeRespuesta[0].equals(Mensajes.SHOW_USER_ADMIN_OK)) {
                        for (int i = 1; i < mensajeRespuesta.length; i++) {
                            String[] partesUsuario = mensajeRespuesta[i].split(":");
                            AdminUsuario u = new AdminUsuario(Integer.parseInt(partesUsuario[0]), partesUsuario[1], partesUsuario[2], partesUsuario[3]);

                            if (!dato1.isEmpty()) {
                                if (u.getNombre().contains(dato1)) {
                                    usuarios.add(u);
                                }
                            } else {
                                usuarios.add(u);
                            }

                        }
                        ObservableList<AdminUsuario> todoUsuarios = FXCollections.observableList(usuarios);

                        tablaUsuario.setItems(todoUsuarios);

                        return true;

                    } else if (mensajeRespuesta[0].equals(Mensajes.SHOW_USER_ADMIN_ERROR)) {
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
