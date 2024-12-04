package com.explore.inventorymanagementsystem.utils;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;

public class PasswordUtil {
    private static final int SALT_LENGTH = 16;
    private static final int ITERATIONS = 65536;
    private static final int KEY_LENGTH = 128;

    public static String hashPassword(String password) {
        // TODO (Efe): Implement password hashing
        // 1. Use secure hashing algorithm (e.g., BCrypt)
        // 2. Add salt if necessary
        // 3. Return hashed password
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public static boolean verifyPassword(String password, String hashedPassword) {
        // TODO (Efe): Implement password verification
        // 1. Hash input password
        // 2. Compare with stored hash
        // 3. Return true if matches
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private static byte[] hash(String password, byte[] salt) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return keyFactory.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("Error while hashing password", e);
        }
    }

    private static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return salt;
    }

    public static void main(String[] args) {
        System.out.println(hashPassword("john123"));
    }
}
