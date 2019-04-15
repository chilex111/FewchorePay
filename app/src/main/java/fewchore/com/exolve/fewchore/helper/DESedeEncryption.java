package fewchore.com.exolve.fewchore.helper;


import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.Arrays;

public class DESedeEncryption {


    public String encrypt(String toBeEncrypted, String SecretKey) {

        try{

            MessageDigest md = MessageDigest.getInstance("md5");
            byte[] digestOfPassword = md.digest(SecretKey.getBytes("UTF-16LE"));

            byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);


            for (int j = 0, k = 16; j < 8;) {
                keyBytes[k++] = keyBytes[j++];
            }

            SecretKey secretKey = new SecretKeySpec(keyBytes, 0, 24, "DESede");

            IvParameterSpec iv = new IvParameterSpec(new byte[8]);
            Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");

            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);

            byte[] plainTextBytes = toBeEncrypted.getBytes("UTF-16LE");
            byte[] cipherText = cipher.doFinal(plainTextBytes);

            return new String(Base64.encodeBase64(cipherText));





          /*  MessageDigest md = MessageDigest.getInstance("md5");
            byte[] digestOfPassword = md.digest(SecretKey.getBytes("UTF-16LE"));
            byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);

            for (int j = 0, k = 16; j < 8;)
            {
                keyBytes[k++] = keyBytes[j++];
            }

            SecretKey secretKey = new SecretKeySpec(keyBytes,"DESede");
            IvParameterSpec iv = new IvParameterSpec(new byte[8]);
            Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");

            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
            byte[] plainTextBytes = ToBeEncrypted.getBytes("UTF-16LE");
            byte[] cipherText = cipher.doFinal(plainTextBytes);
            byte [] base64Bytes = Base64.encodeBase64(cipherText);

            return new String(base64Bytes);*/
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return toBeEncrypted;
    }


    public String decrypt(String encryptedText, String secretKey) {

        try{
            byte[] message = Base64.decodeBase64(encryptedText.getBytes("UTF-16LE"));

            MessageDigest md = MessageDigest.getInstance("md5");
            byte[] digestOfPassword = md.digest(secretKey.getBytes("UTF-16LE"));
            byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);

            for (int j = 0, k = 16; j < 8;)
            {
                keyBytes[k++] = keyBytes[j++];
            }

            SecretKey key = new SecretKeySpec(keyBytes, "DESede");
            IvParameterSpec iv = new IvParameterSpec(new byte[8]);
            Cipher decipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");

            decipher.init(Cipher.DECRYPT_MODE, key, iv);

            byte[] plainText = decipher.doFinal(message);

            return new String(plainText, "UTF-16LE");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return encryptedText;
    }
}
