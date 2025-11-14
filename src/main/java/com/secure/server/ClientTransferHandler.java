package com.secure.server;

import com.secure.crypto.AESUtils;
import com.secure.crypto.HashUtils;

import java.io.*;
import java.net.Socket;

public class ClientTransferHandler extends Thread {

    private Socket socket;

    public ClientTransferHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {

            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            /* --------- PHASE 1 : AUTH ---------- */

            String login = in.readUTF();
            String pass = in.readUTF();

            if (!SecureFileServer.USERS.containsKey(login)
                    || !SecureFileServer.USERS.get(login).equals(pass)) {

                out.writeUTF("AUTH_FAIL");
                socket.close();
                return;
            }

            out.writeUTF("AUTH_OK");

            /* --------- PHASE 2 : NEGOCIATION ---------- */

            String fileName = in.readUTF();
            long encryptedSize = in.readLong();
            String clientHash = in.readUTF();

            out.writeUTF("READY_FOR_TRANSFER");

            /* --------- PHASE 3 : TRANSFER ---------- */

            byte[] encryptedData = new byte[(int) encryptedSize];
            in.readFully(encryptedData);

            byte[] decrypted = AESUtils.decrypt(encryptedData);

            FileOutputStream fos = new FileOutputStream("received_files/" + fileName);
            fos.write(decrypted);
            fos.close();

            String serverHash = HashUtils.sha256("received_files/" + fileName);

            if (serverHash.equals(clientHash))
                out.writeUTF("TRANSFER_SUCCESS");
            else
                out.writeUTF("TRANSFER_FAIL");

            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
