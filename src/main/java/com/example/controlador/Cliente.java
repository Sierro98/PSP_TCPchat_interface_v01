package com.example.controlador;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class Cliente implements Initializable, Runnable {
    public ListView listViewUsuarios;
    public ListView listViewMensajes;
    public Button btnEnviar;
    public Button grupo2_btn;
    public Button grupo1_btn;
    public Button grupo3_btn;
    public Button grupo4_btn;
    public Button grupo5_btn;
    public TextField tf_espacioEscritura;
    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private boolean done;
    private String nombre;
    private ButtonType entrar = new ButtonType("Entrar", ButtonBar.ButtonData.OK_DONE);
    private ButtonType salir = new ButtonType("Salir", ButtonBar.ButtonData.CANCEL_CLOSE);

    public void action_btnGrupo(ActionEvent actionEvent) {
        Alert alert =
                new Alert(Alert.AlertType.INFORMATION,
                        "¿Desea entrar o salir?", entrar, salir);
        alert.setTitle("ATENTO");
        Optional<ButtonType> result = alert.showAndWait();
        //TODO: hacer entrada o salida de los grupos
    }

    public void enviarMensaje(ActionEvent actionEvent) {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Cliente client = new Cliente();
        client.run();
    }

    @Override
    public void run() {
        try {
            client = new Socket("localhost", 6000);
            out = new PrintWriter(client.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            InputHandler inHandler = new InputHandler();
            Thread t = new Thread(inHandler);
            t.start();
            System.out.print("Seleccione el nombre: ");
            nombre = in.readLine();
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

    //MANEJADOR DE INPUTS
    class InputHandler implements Runnable {
        @Override
        public void run() {
            try {
                BufferedReader inReader = new BufferedReader(new InputStreamReader(System.in));
                while (!done) {
                    String mensaje = inReader.readLine();
                    if (mensaje.equals("/quit")) {
                        out.println(mensaje);
                        inReader.close();
                        shudown();
                    } else {
                        out.println(mensaje);
                    }
                }
            } catch (IOException e) {
                shudown();
            }
        }
    }
}
