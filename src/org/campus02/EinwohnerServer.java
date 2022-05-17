package org.campus02;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class EinwohnerServer {

    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(1111)) {
            while (true) {
                // Server wartet nun auf Client
                System.out.println("Server wartet auf Client");
                Socket client = server.accept();

                EinwohnerHandler einwohnerHandler = new EinwohnerHandler(client);
                // einwohnerHandler.start(); // -> nur ein client

                Thread thread = new Thread(einwohnerHandler);
                thread.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
