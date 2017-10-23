/*
 * Copyright 2017 Guilherme Paulino
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.paulino.secrettalk;

import android.util.Base64;

import com.paulino.secrettalk.logger.Log;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


/**
 * This static class implement the Advanced Encryption Standard algorithm for 128, 192 or 256-bits
 * keyword size.
 * It also encodes the string messages to Base 64 Bytes format, before encrypting.
 */
public class AdvancedEncryptionStandard {

    private static final String TAG = "AES";
    private static final String ALGORITHM = "AES";

    /**
     * Encrypts the given plain text
     *
     * @param str The plain text to encrypt
     * @param key Keyword for cryptography
     * @return Encoded and encrypted data
     */
    public static byte[] encrypt(String str, String key)
    {
        Log.d(TAG,"encrypt str.length(): "+str.length());

        // Encodes the string to bytes in Base64 format
        byte[] data = Base64.encode(str.getBytes(), 0, str.length(), Base64.DEFAULT);

        Log.d(TAG,"encrypt data.length: "+data.length);

        if (!key.isEmpty()) {
            byte[] randomizer = new byte[key.length()];
            for(int i = 0; i < key.length(); i++) {
                randomizer[i] = (byte) key.charAt(i);
            }

            Log.d(TAG,"encrypt randomizer.length(): "+randomizer.length);

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
     * @param encrypted The data to decrypt
     * @param key Keyword used in cryptography
     * @param flag indicates if it will decrypt a sent or a received message
     * @return Decrypted and decoded message to String format
     */
    public static String decrypt(byte[] encrypted, String key, int flag)
    {
        int len;
        if(flag>0){
            len = flag;             // Length of the sent message
        } else {
            len = encrypted.length; // Length of the received message
        }

        Log.d(TAG,"decrypt encrypted.length: "+len);

        if (!key.isEmpty()) {
            try {
                SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
                Cipher cipher = null;
                cipher = Cipher.getInstance(ALGORITHM);
                cipher.init(Cipher.DECRYPT_MODE, secretKey);

                // Decrypts message
                byte[] cipher_msg = cipher.doFinal(encrypted, 0, len);

                // Decodes the decrypted bytes and converts to string
                byte[] decrypted = Base64.decode(cipher_msg, 0, cipher_msg.length, Base64.DEFAULT);

                String decrypted_str = new String(decrypted, 0, decrypted.length);
                Log.d(TAG,"decrypted data: "+decrypted_str);

                return decrypted_str;
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

        // Decodes the non-encrypted bytes and converts to string
        byte[] decoded = Base64.decode(encrypted, 0, len, Base64.DEFAULT);

        Log.d(TAG,"decrypt decoded.length: "+decoded.length);

        return new String(decoded, 0, decoded.length);
    }
}
