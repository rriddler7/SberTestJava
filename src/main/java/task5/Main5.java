package task5;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.*;

public class Main5 {
    private static final String PASS = "password";

    public static String parsePassword(String path) {
        String password = null;
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.contains(PASS)) {
                    continue;
                }
                int i = line.indexOf(PASS);
                password = line.substring(i + 9, line.length());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return password;
    }

    private static byte[] byteArrays(byte[] key, byte[] cipherText) {
        byte[] arr = new byte[key.length + cipherText.length];
        for (int i = 0; i < key.length; i++) {
            arr[i] = key[i];
        }
        for (int i = 0; i < cipherText.length; i++) {
            arr[i + key.length] = cipherText[i];
        }
        return arr;
    }

    public static String encrypt (String password) {
        String encode = null;
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            SecureRandom secureRandom = new SecureRandom();
            int keyBitSize = 256;
            keyGenerator.init(keyBitSize, secureRandom);
            SecretKey secretKey = keyGenerator.generateKey();
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(new byte[16]));

            byte[] cipherText = cipher.doFinal(password.getBytes(StandardCharsets.UTF_8));
            byte[] key = secretKey.getEncoded();
            byte[] arrays = byteArrays(key, cipherText);
            encode = Base64.getEncoder().encodeToString(arrays);
         }
        catch (Exception e) {
            e.printStackTrace();
        }
        return encode;
    }

    public static void decrypt(String path, String encode) {
        String password = null;
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            byte[] decode = new byte[0];
            line = br.readLine();
            System.out.println(line);
//            while ((line = br.readLine()) != null) {
//                decode = Base64.getDecoder().decode(encode);
//            }
            decode = Base64.getDecoder().decode(line);
            byte[] key1 = Arrays.copyOf(decode, 32);
            byte[] cipherText1 = Arrays.copyOfRange(decode, 32, decode.length);

            SecretKey keySpec = new SecretKeySpec(key1, "AES");

            Cipher cipher2 = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher2.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(new byte[16]));
            String text = new String(cipher2.doFinal(cipherText1), StandardCharsets.UTF_8);
            System.out.println(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void writeEncode(String path, String encode) {
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(path)))
        {
             bw.write(encode);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


//    public static void generateKey(String password) {
//        try {
//            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
//
//            SecureRandom secureRandom = new SecureRandom();
//            int keyBitSize = 256;
//            keyGenerator.init(keyBitSize, secureRandom);
//
//            SecretKey secretKey = keyGenerator.generateKey();
//            cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(new byte[16]));
//            byte[] cipherText = cipher.doFinal(password.getBytes(StandardCharsets.UTF_8));
//            byte[] key = secretKey.getEncoded();
//            byte[] sum = addArr(key, cipherText);
//            String encode = Base64.getEncoder().encodeToString(sum);
//            System.out.println(encode);
//
//            /*
//            write
//
//            read
//             */
//            byte[] decode = Base64.getDecoder().decode(encode);
//            byte[] key1 = Arrays.copyOf(decode, 32);
//            byte[] cipherText1 = Arrays.copyOfRange(decode, 32, decode.length);
//
//            SecretKey keySpec = new SecretKeySpec(key1, "AES");
//
//            Cipher cipher2 = Cipher.getInstance("AES/CBC/PKCS5Padding");
//            cipher2.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(new byte[16]));
//            String text = new String(cipher2.doFinal(cipherText1), StandardCharsets.UTF_8);
//            System.out.println(text);
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Incorrect number of args");
            return;
        }
        String path = args[0];
        String password = parsePassword(path);
        String encode = encrypt(password);
        writeEncode(path, encode);
        decrypt(path, encode);
    }
}
