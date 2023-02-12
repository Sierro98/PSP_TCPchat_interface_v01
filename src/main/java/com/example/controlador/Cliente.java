package com.example.controlador;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class Cliente implements Initializable {
    public ListView listViewUsuarios;
    public ListView<String> listViewMensajes;
    public Button btnEnviar;
    public Button grupo2_btn;
    public Button grupo1_btn;
    public Button grupo3_btn;
    public Button grupo4_btn;
    public Button grupo5_btn;
    public TextField tf_espacioEscritura;
    public Button btnIniciar;
    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private boolean done;
    private String nombre;
    private String mensaje;
    private ButtonType entrar = new ButtonType("Entrar", ButtonBar.ButtonData.OK_DONE);
    private ButtonType salir = new ButtonType("Salir", ButtonBar.ButtonData.CANCEL_CLOSE);
    private List<String> mensajesRecibidos;

    public void action_btnGrupo(ActionEvent actionEvent) {
        //TODO: el handling de los botones esta mal hecho, hacer como en el proyecto de interfaces
        Alert alert =
                new Alert(Alert.AlertType.INFORMATION,
                        "¿Desea entrar o salir?", entrar, salir);
        alert.setTitle("ATENTO");
        Optional<ButtonType> result = alert.showAndWait();
        if (actionEvent.getSource().equals(grupo1_btn)) {
            if (result.equals(entrar)) {

            } else if (result.equals(salir)) {

            }
        } else if (actionEvent.getSource().equals(grupo2_btn)) {
            if (result.equals(entrar)) {

            } else if (result.equals(salir)) {

            }
        } else if (actionEvent.getSource().equals(grupo3_btn)) {
            if (result.equals(entrar)) {

            } else if (result.equals(salir)) {

            }
        } else if (actionEvent.getSource().equals(grupo4_btn)) {
            if (result.equals(entrar)) {

            } else if (result.equals(salir)) {

            }
        } else if (actionEvent.getSource().equals(grupo5_btn)) {
            if (result.equals(entrar)) {

            } else if (result.equals(salir)) {

            }
        }
    }

    public void enviarMensaje(ActionEvent actionEvent) {
        String mensaje2Send = tf_espacioEscritura.getText();
        out.println(mensaje2Send);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void iniciarCliente(ActionEvent actionEvent) {
        try {
            client = new Socket("localhost", 6000);
            out = new PrintWriter(client.getOutputStream(), true);
            mensajesRecibidos = new ArrayList<>();
            ClienteHandler clienteHandler = new ClienteHandler(client);

            clienteHandler.recibirMensaje(listViewMensajes);
        } catch (IOException e) {
            shudown();
        }
    }

    //MANEJADOR DE INPUTS
//    class InputHandler implements Runnable {
//        @Override
//        public void run() {
//            try {
//                BufferedReader inReader = new BufferedReader(new InputStreamReader(System.in));
//                while (!done) {
//                    mensaje = inReader.readLine();
//                    if (mensaje.equals("/quit")) {
//                        out.println(mensaje);
//                        inReader.close();
//                        shudown();
//                    } else {
//                        mensajesRecibidos.add(mensaje);
//                        out.println(mensaje);
//                    }
//                }
//            } catch (IOException e) {
//                shudown();
//            }
//        }
//    }


    public void shudown() {
        done = true;
        try {
            in.close();
            out.close();
            if (!client.isClosed()) {
                client.close();
            }
        } catch (IOException e) {
            //ignore
        }
    }

    public class IniciarCliente implements Runnable {
        @Override
        public void run() {
            try {
                client = new Socket("localhost", 6000);
                out = new PrintWriter(client.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(client.getInputStream()));

                String inMensaje;
                while ((inMensaje = in.readLine()) != null) {
                    System.out.println(inMensaje);
                }
            } catch (IOException e) {
                shudown();
            }
        }

        public void shudown() {
            done = true;
            try {
                in.close();
                out.close();
                if (!client.isClosed()) {
                    client.close();
                }
            } catch (IOException e) {
                //ignore
            }
        }
    }
}
