package com.tarn.utils.crypto;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class Keys {

    public static SecretKey generate() throws NoSuchAlgorithmException {
        SecureRandom securerandom = new SecureRandom();
        KeyGenerator keygenerator = KeyGenerator.getInstance("AES");
        keygenerator.init(256, securerandom);
        return keygenerator.generateKey();
    }


}
