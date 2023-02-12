package com.example.controlador;

import javafx.application.Platform;
import javafx.scene.control.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClienteHandler {
    private Socket socket;
    private BufferedReader bufferedReader;

    public ClienteHandler(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            closeEverything();
        }
    }

    /**
     * Creamos un nuevo Thread para que el receptor de mensajes no sea bloquenate.
     * @param listaMensajes
     */
    public void recibirMensaje(ListView<String> listaMensajes) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (socket.isConnected()) {
                    try {
                        String msg = bufferedReader.readLine();
                        Platform.runLater(()-> {
                            listaMensajes.getItems().add(msg);
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
