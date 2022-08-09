package com.misman.start.util;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

public class Cryptor {

    private static final String PASSWORD = "123Acr..!!*2022";
    private static final String SALT = "123TheSalt..!!*2022";
    private static final IvParameterSpec IV_PARAM = new IvParameterSpec("ACR1234567890_Mi".getBytes());


    private static SecretKey generateSecretKey() {
        SecretKey secret = null;
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(PASSWORD.toCharArray(), SALT.getBytes(), 65536, 256);
            secret = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return secret;
    }

    public static String encrypt(String value, Base64.Encoder encoder) {
        try {
            SecretKey secret = generateSecretKey();
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, secret, IV_PARAM);
            byte[] cipherText = cipher.doFinal(value.getBytes(StandardCharsets.UTF_8));
            return encoder.encodeToString(cipherText);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String decrypt(String encrypted, Base64.Decoder decoder) {
        SecretKey secret = generateSecretKey();
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secret, IV_PARAM);
            byte[] original = cipher.doFinal(decoder.decode(encrypted));
            return new String(original);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException |
                 InvalidAlgorithmParameterException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

}
