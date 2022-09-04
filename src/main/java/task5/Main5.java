package task5;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
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
                password = line.substring(i + 9);
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

    public static String createFile (String path) throws IOException {
        String pathDir = path.substring(0, path.lastIndexOf("/"));
        String pathNewFile = pathDir + "/encode.txt";
        File newFile = new File(pathNewFile);
        Files.delete(Paths.get(path));
        return newFile.getAbsolutePath();
    }

    public static void decrypt(String pathNewFile) {
        try (BufferedReader br = new BufferedReader(new FileReader(pathNewFile))) {
            String line;
            byte[] decode = new byte[0];
            while ((line = br.readLine()) != null) {
                decode = Base64.getDecoder().decode(line);
            }
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
    public static void writeEncode(String pathNewFile, String encode) {
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(pathNewFile)))
        {
             bw.write(encode);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Incorrect number of arguments");
            return;
        }

        String path = args[0];
        String password = parsePassword(path);
        String encode = encrypt(password);
        String pathNewFile = createFile(path);
        writeEncode(pathNewFile, encode);
        decrypt(pathNewFile);
    }
}
