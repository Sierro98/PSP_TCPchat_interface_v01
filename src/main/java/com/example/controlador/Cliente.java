package com.example.controlador;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class Cliente implements Initializable {
    public ListView<String> listViewUsuarios;
    public ListView<String> listViewMensajes;
    public Button btnEnviar;
    public Button grupo2_btn;
    public Button grupo1_btn;
    public Button grupo3_btn;
    public Button grupo4_btn;
    public Button grupo5_btn;
    public TextField tf_espacioEscritura;
    public Button btnIniciar;
    public Button btnSalir;
    public Button send2Grupo2;
    public Button send2Grupo3;
    public Button send2Grupo4;
    public Button send2Grupo5;
    public Button send2Grupo1;
    public Button btnRefreshUsers;
    ClienteHandler clienteHandler;
    private Socket client = null;
    private BufferedReader in;
    private PrintWriter out;
    private boolean done;
    private String nombre;
    private String mensaje;
    private ButtonType entrar = new ButtonType("Entrar", ButtonBar.ButtonData.OK_DONE);
    private ButtonType salir = new ButtonType("Salir", ButtonBar.ButtonData.CANCEL_CLOSE);

    public void action_btnGrupo(ActionEvent actionEvent) {
        Alert alert =
                new Alert(Alert.AlertType.INFORMATION,
                        "¿Desea entrar o salir?", entrar, salir);
        alert.setTitle("ATENTO");
        Optional<ButtonType> result = alert.showAndWait();
        if (actionEvent.getSource().equals(grupo1_btn)) {
            if (result.isPresent() && result.get() == entrar) {
                out.println("/join grupo1");
            } else if (result.isPresent() && result.get() == salir) {
                out.println("/leave grupo1");
            }
        } else if (actionEvent.getSource().equals(grupo2_btn)) {
            if (result.isPresent() && result.get() == entrar) {
                out.println("/join grupo2");
            } else if (result.isPresent() && result.get() == salir) {
                out.println("/leave grupo2");
            }
        } else if (actionEvent.getSource().equals(grupo3_btn)) {
            if (result.isPresent() && result.get() == entrar) {
                out.println("/join grupo3");
            } else if (result.isPresent() && result.get() == salir) {
                out.println("/leave grupo3");
            }
        } else if (actionEvent.getSource().equals(grupo4_btn)) {
            if (result.isPresent() && result.get() == entrar) {
                out.println("/join grupo4");
            } else if (result.isPresent() && result.get() == salir) {
                out.println("/leave grupo4");
            }
        } else if (actionEvent.getSource().equals(grupo5_btn)) {
            if (result.isPresent() && result.get() == entrar) {
                out.println("/join grupo5");
            } else if (result.isPresent() && result.get() == salir) {
                out.println("/leave grupo5");
            }
        }
    }

    public void enviarMensaje(ActionEvent actionEvent) {
        String mensaje2Send = tf_espacioEscritura.getText();
        out.println(mensaje2Send);
        tf_espacioEscritura.setText("");
    }

    public void send2(ActionEvent actionEvent) {
        if (actionEvent.getSource().equals(send2Grupo1)) {
            String mensaje2Send = tf_espacioEscritura.getText();
            out.println("/grupo1 " + mensaje2Send);
            tf_espacioEscritura.setText("");
        } else if (actionEvent.getSource().equals(send2Grupo2)) {
            String mensaje2Send = tf_espacioEscritura.getText();
            out.println("/grupo2 " + mensaje2Send);
            tf_espacioEscritura.setText("");
        } else if (actionEvent.getSource().equals(send2Grupo3)) {
            String mensaje2Send = tf_espacioEscritura.getText();
            out.println("/grupo3 " + mensaje2Send);
            tf_espacioEscritura.setText("");
        } else if (actionEvent.getSource().equals(send2Grupo4)) {
            String mensaje2Send = tf_espacioEscritura.getText();
            out.println("/grupo4 " + mensaje2Send);
            tf_espacioEscritura.setText("");
        } else if (actionEvent.getSource().equals(send2Grupo5)) {
            String mensaje2Send = tf_espacioEscritura.getText();
            out.println("/grupo5 " + mensaje2Send);
            tf_espacioEscritura.setText("");
        }
    }

    public void onEnter(KeyEvent keyEvent) {
        if (keyEvent.getCode().toString().equals("ENTER")) {
            String mensaje2Send = tf_espacioEscritura.getText();
            out.println(mensaje2Send);
            tf_espacioEscritura.setText("");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void iniciarCliente(ActionEvent actionEvent) {
        try {
            client = new Socket("localhost", 6000);
            out = new PrintWriter(client.getOutputStream(), true);
            clienteHandler = new ClienteHandler(client);
            clienteHandler.recibirMensaje(listViewMensajes, listViewUsuarios);
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

    public void cerrarApp(ActionEvent actionEvent) {
        Stage stage = (Stage) btnSalir.getScene().getWindow();
        if (client != null) {
            clienteHandler.setDone(true);
            out.println("/quit");
            listViewMensajes.getItems().clear();
            stage.close();
        } else {
            stage.close();
        }
    }

    public void refreshUsers(ActionEvent actionEvent) {
        listViewUsuarios.getItems().clear();
        out.println("/users");
    }
}
