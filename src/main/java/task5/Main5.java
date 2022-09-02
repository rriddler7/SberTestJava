package task5;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.*;

public class Main5 {
    private static final String PASS = "password";
//    private static final SecureRandom RAND = new SecureRandom();

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

//    public static Optional generateSalt (final int length) {
//
//        if (length < 1) {
//            System.err.println("Error in generateSalt: length must be > 0");
//            return Optional.empty();
//        }
//
//        byte[] salt = new byte[length];
//        RAND.nextBytes(salt);
//
//        return Optional.of(Base64.getEncoder().encodeToString(salt));
//    }

//    private static final int ITERATIONS = 65536;
//    private static final int KEY_LENGTH = 512;
//    private static final String ALGORITHM = "PBKDF2WithHmacSHA512";

    public static void generateKey(String password) {
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

            byte[] sum = addArr(key, cipherText);

            String encode = Base64.getEncoder().encodeToString(sum);
            System.out.println(encode);

            /*
            write

            read
             */
            byte[] decode = Base64.getDecoder().decode(encode);
            byte[] key1 = Arrays.copyOf(decode, 32);
            byte[] cipherText1 = Arrays.copyOfRange(decode, 32, decode.length);

            SecretKey keySpec = new SecretKeySpec(key1, "AES");

            Cipher cipher2 = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher2.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(new byte[16]));
            String text = new String(cipher2.doFinal(cipherText1), StandardCharsets.UTF_8);
            System.out.println(text);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static byte[] addArr(byte[] key, byte[] cipherText) {
        byte[] arr = new byte[key.length + cipherText.length];
        for (int i = 0; i < key.length; i++) {
            arr[i] = key[i];
        }
        for (int i = 0; i < cipherText.length; i++) {
            arr[i + key.length] = cipherText[i];
        }
        return arr;
    }


//    public static Optional hashPassword (String password, String salt) {
//
//        char[] chars = password.toCharArray();
//        byte[] bytes = salt.getBytes();
//
//        PBEKeySpec spec = new PBEKeySpec(chars, bytes, ITERATIONS, KEY_LENGTH);
//
//        Arrays.fill(chars, Character.MIN_VALUE);
//
//        try {
//            SecretKeyFactory fac = SecretKeyFactory.getInstance(ALGORITHM);
//            byte[] securePassword = fac.generateSecret(spec).getEncoded();
//            return Optional.of(Base64.getEncoder().encodeToString(securePassword));
//
//        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
//            System.err.println("Exception encountered in hashPassword()");
//            return Optional.empty();
//
//        } finally {
//            spec.clearPassword();
//        }
//    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Incorrect number of args");
            return;
        }
        String path = args[0];
        String password = parsePassword(path);
        generateKey(password);
    }
}
