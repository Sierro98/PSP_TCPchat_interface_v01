package com.example.controlador;

import javafx.application.Platform;
import javafx.scene.control.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

public class ClienteHandler {
    private Socket socket;
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;
    private boolean done = false;

    public void setDone(boolean done) {
        this.done = done;
    }

    public ClienteHandler(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.printWriter = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
            closeEverything();
        }
    }

    /**
     * Creamos un nuevo Thread para que el receptor de mensajes no sea bloquenate.
     *
     * @param listaMensajes
     */
    public void recibirMensaje(ListView<String> listaMensajes, ListView<String> listaUsuarios) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!done) {
                    try {
                        String msg = bufferedReader.readLine();
                        Platform.runLater(() -> {
                            if (!msg.startsWith("user")) listaMensajes.getItems().add(msg);
                            if (msg.startsWith("user")) listaUsuarios.getItems().add(msg.replaceAll("user:", ""));
                            if (listaMensajes.getItems().size() > 0)
                                listaMensajes.scrollTo(listaMensajes.getItems().size() - 1);
                        });

                    } catch (IOException e) {
                        e.printStackTrace();
                        closeEverything();
                        break;
                    }
                }
            }
        }).start();
    }


    private void closeEverything() {
        try {
            if (socket != null) {
                socket.close();
            }
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
