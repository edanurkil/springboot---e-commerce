package com.example.e_commerce.util;

import java.security.SecureRandom;
import java.util.Base64;

public class SecretKeyGenerator {

    public static void main(String[] args) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] keyBytes = new byte[32];
        secureRandom.nextBytes(keyBytes);

        String base64Key = Base64.getEncoder().encodeToString(keyBytes);

        System.out.println("Generated JWT Secret Key (Base64 encoded):");
        System.out.println(base64Key);
    }
}