package com.miguel.pi.cliente;

import com.miguel.pi.cliente.Utilidades.Mensajes;
import com.miguel.pi.cliente.Utilidades.LectorINI;
import java.io.BufferedReader;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javafx.application.Application.launch;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.StageStyle;

/**
 * JavaFX App
 */
public class App extends Application {

    public static Stage mainStage;
    private static Scene nuevaEscena;

    public static Socket serverSocket;
    public static PrintWriter out;
    public static BufferedReader in;

    public static int idUsuario;
    public static String nombre;
    public static Cargando cargando;

    public final static String version = "PreAlpha-0.2";

    @Override
    public void start(Stage stage) throws Exception {
        cargando = new Cargando();

        realizarConexion();

        this.mainStage = stage;

        stage.setOnCloseRequest(event -> {
            try {
                out.println(Mensajes.EXIT);
                System.out.println(Mensajes.EXIT);
            } catch (Exception e) {
                System.out.println("NO SE PUDO CONTACTAR CON EL SERVIDOR");
            }
        });

        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));

        nuevaEscena = new Scene(root);
        mainStage.setTitle("Basilisk");
        mainStage.setScene(nuevaEscena);
        mainStage.setResizable(false);
        //src/main/resources/com/miguel/pi/cliente/img/logo.png
        Image icon = new Image(getClass().getResourceAsStream("/com/miguel/pi/cliente/img/logo.png"));
        mainStage.getIcons().add(icon);

        mainStage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public static void cambiarVentana(Parent root) {
        Scene scene = new Scene(root);
        mainStage.setScene(scene);
        mainStage.show();
    }

    public static int getIdUsuario() {
        return idUsuario;
    }

    public static void setIdUsuario(int idUsuario) {
        App.idUsuario = idUsuario;
    }

    private void realizarConexion() {
        LectorINI lini = new LectorINI();

        String host = lini.getIP();
        int port = Integer.parseInt(lini.getPORT());

        try {

            serverSocket = new Socket(host, port);
            out = new PrintWriter(serverSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
            idUsuario = 0;
            nombre = "";
        } catch (Exception e) {
            System.out.println("NO SE ENCUENTRA EL SERVIDOR");
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Error en la conexion");
            alert.setHeaderText("No se pudo conectar con el servidor: " + host + ":" + port + ", deseas reintentarlo?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                realizarConexion();
            } else {
                //SI EL USUARIO NO QUIERE REINTENTARLO SE CIERRA LA APLICACION
                System.exit(0);
            }
        }

    }

}
