package com.secure.client;

import com.secure.crypto.AESUtils;
import com.secure.crypto.HashUtils;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Scanner;

public class SecureFileClient {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("IP Serveur: ");
        String ip = sc.nextLine();

        System.out.print("Port: ");
        int port = Integer.parseInt(sc.nextLine());

        System.out.print("Login: ");
        String login = sc.nextLine();

        System.out.print("Password: ");
        String pass = sc.nextLine();

        System.out.print("Chemin du fichier: ");
        String path = sc.nextLine();

        try {
            Socket socket = new Socket(ip, port);

            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            /* -------- PHASE 1 : AUTH -------- */

            out.writeUTF(login);
            out.writeUTF(pass);

            String resp = in.readUTF();
            if (!resp.equals("AUTH_OK")) {
                System.out.println("[CLIENT] Mauvais identifiants.");
                return;
            }

            /* -------- PRE-TRAITEMENT -------- */

            File f = new File(path);
            byte[] fileBytes = Files.readAllBytes(f.toPath());
            String sha256 = HashUtils.sha256(path);

            byte[] encrypted = AESUtils.encrypt(fileBytes);

            /* -------- PHASE 2 : NEGOCIATION -------- */

            out.writeUTF(f.getName());
            out.writeLong(encrypted.length);
            out.writeUTF(sha256);

            String ready = in.readUTF();
            if (!ready.equals("READY_FOR_TRANSFER")) {
                System.out.println("Erreur serveur.");
                return;
            }

            /* -------- PHASE 3 : TRANSFER -------- */

            out.write(encrypted);
            out.flush();

            String res = in.readUTF();
            System.out.println("[CLIENT] Réponse serveur : " + res);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
