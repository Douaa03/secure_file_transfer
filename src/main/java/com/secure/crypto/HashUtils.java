package com.secure.crypto;

import java.io.FileInputStream;
import java.security.MessageDigest;

public class HashUtils {

    public static String sha256(String filePath) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        FileInputStream fis = new FileInputStream(filePath);

        byte[] buffer = new byte[4096];
        int n;
        while ((n = fis.read(buffer)) != -1) {
            digest.update(buffer, 0, n);
        }
        fis.close();

        byte[] hash = digest.digest();

        StringBuilder sb = new StringBuilder();
        for (byte b : hash) sb.append(String.format("%02x", b));

        return sb.toString();
    }
}
