package helperClasses;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class MessageEncoder {
    // Password must be at least 8 characters
    private static final String password = "password password";

    private Cipher desCipher;
    private SecretKey secretKey;

    public void createKey() {
        byte[] key= password.getBytes();

        DESKeySpec desKeySpec = null;
        SecretKeyFactory keyFactory = null;

        try {
            desKeySpec = new DESKeySpec(key);

            keyFactory = SecretKeyFactory.getInstance("DES");

            secretKey = keyFactory.generateSecret(desKeySpec);

            // Create Cipher
            desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");

        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException ex1) {
            ex1.printStackTrace();
        } catch (InvalidKeySpecException ex2) {
            ex2.printStackTrace();
        } catch (NoSuchPaddingException ex3) {
            ex3.printStackTrace();
        }
    }

    public byte[] encodeMessage(String stringToEncode) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        createKey();
        // Clear message
        byte[] clearSTR1 = stringToEncode.getBytes();

        // Encode message
        desCipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encodedSTR = desCipher.doFinal(clearSTR1);
        // String stringAfterEncoding = new String(encodedSTR);
        // System.out.println("stringAfterEncoding: " + stringAfterEncoding);

        return encodedSTR;
    }

    public String decodeMessage(byte[] encodedString) throws InvalidKeyException {
        createKey();
        String stringAfterDecoding = null;
        try {
            desCipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] clearSTR2 = desCipher.doFinal(encodedString);
            stringAfterDecoding = new String(clearSTR2);
//            System.out.println("stringAfterDecoding: " + stringAfterDecoding);

        } catch (IllegalBlockSizeException ex) {
            System.out.println("IllegalBlockSizeException: " + ex.getMessage());
        } catch (BadPaddingException e) {
            System.out.println("BadPaddingException: " + e.getMessage());
            for (StackTraceElement each : e.getStackTrace())
                System.out.println(each);
        }
        return stringAfterDecoding;
    }
}
