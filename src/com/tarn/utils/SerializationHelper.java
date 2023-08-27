package com.tarn.utils;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class SerializationHelper {

    public static void serialize(Object object, String location, SecretKey key){
        try {
            FileOutputStream fileOut = new FileOutputStream(location);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(new byte[16]));
            CipherOutputStream cipherOut = new CipherOutputStream(fileOut, cipher);
            ObjectOutputStream objectOut = new ObjectOutputStream(cipherOut);
            objectOut.writeObject(object);
            objectOut.close();
            cipherOut.close();
            fileOut.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

        public static Object deserialize(String location, SecretKey key){
            try{
                FileInputStream fileIn = new FileInputStream(location);
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
                cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(new byte[16]));
                CipherInputStream cipherIn = new CipherInputStream(fileIn, cipher);
                ObjectInputStream objectIn = new ObjectInputStream(cipherIn);

                Object object = objectIn.readObject();

                objectIn.close();
                cipherIn.close();
                fileIn.close();
                return object;
            } catch (NoSuchPaddingException | NoSuchAlgorithmException | IOException | InvalidKeyException |
                     ClassNotFoundException | InvalidAlgorithmParameterException e) {
                throw new RuntimeException(e);
            }

        }

}


