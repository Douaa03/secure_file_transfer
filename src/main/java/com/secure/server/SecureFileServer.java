package com.secure.server;

import java.io.File;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class SecureFileServer {

    public static final int PORT = 5000;

    public static Map<String, String> USERS = new HashMap<>();

    static {
        USERS.put("admin", "1234");
        USERS.put("user", "pass");
    }

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("[SERVER] Attente de connexion...");

            new File("received_files").mkdirs();

            while (true) {
                Socket client = serverSocket.accept();
                System.out.println("[SERVER] Client connecté : " + client.getInetAddress());
                new ClientTransferHandler(client).start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
