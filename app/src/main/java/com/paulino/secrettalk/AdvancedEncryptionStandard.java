package com.paulino.secrettalk;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Guilherme Paulino on 10/19/2017.
 */

public class AdvancedEncryptionStandard {

    private static final String ALGORITHM = "AES";

    /**
     * Encrypts the given plain text
     *
     * @param str The plain text to encrypt
     * @param key Keyword for cryptography
     */
    public static byte[] encrypt(String str, String key)
    {
        byte[] data = new byte[str.length()];

        for(int i = 0; i < str.length(); i++) {
            data[i] = (byte) str.charAt(i);
        }

        if (!key.isEmpty()) {
            byte[] randomizer = new byte[key.length()];
            for(int i = 0; i < key.length(); i++) {
                randomizer[i] = (byte) key.charAt(i);
            }

            SecretKeySpec secretKey = new SecretKeySpec(randomizer, ALGORITHM);
            Cipher cipher = null;
            try {
                cipher = Cipher.getInstance(ALGORITHM);
                cipher.init(Cipher.ENCRYPT_MODE, secretKey);
                return cipher.doFinal(data);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

        return data;
    }

    /**
     * Decrypts the given byte array
     *
     * @param data The data to decrypt
     */
    public static String decrypt(byte[] data, String key, int len)
    {
        if (!key.isEmpty()) {
            try {
                SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
                Cipher cipher = Cipher.getInstance(ALGORITHM);
                cipher.init(Cipher.DECRYPT_MODE, secretKey);

                return new String(cipher.doFinal(data), 0, len);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        return new String(data, 0, len);
    }



}
