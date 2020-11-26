package Curve25519_key_exchange;

import org.whispersystems.curve25519.Curve25519;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class Curve25519KeyExchangeAesCbc256StringEncryption {
    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, NoSuchPaddingException {
        System.out.println("Curve25519 key exchange and AES CBC 256 string encryption");
        // you need the library https://github.com/signalapp/curve25519-java
        // https://mvnrepository.com/artifact/org.whispersystems/curve25519-java
        // // or com.wavesplatform:curve25519-java as package on repl.it

        String plaintext = "The quick brown fox jumps over the lazy dog";

        // for encryption you need your private key (aPrivateKey) and the public key from other party (bPublicKey)
        String aPrivateKeyBase64 = "yJIzp7IueQOu8l202fwI21/aNXUxXBcg3jJoLFJATlU=";
        String bPublicKeyBase64 =  "jVSuHjVH47PMbMaAxL5ziBS9/Z0HQK6TJLw9X3Jw6yg=";

        // convert the base64 encoded keys
        byte[] aPrivateKey = base64Decoding(aPrivateKeyBase64);
        byte[] bPublicKey = base64Decoding(bPublicKeyBase64);

        // generate shared key
        byte[] aSharedKey = curve25519GenerateKeyAgreement(aPrivateKey, bPublicKey);
        // encrypt
        String ciphertextBase64 = aesCbcEncryptToBase64(aSharedKey, plaintext);
        System.out.println("\n* * * encryption * * *");
        System.out.println("all data are in Base64 encoding");
        System.out.println("aPrivateKey: " + aPrivateKeyBase64);
        System.out.println("bPublicKey:  " + bPublicKeyBase64);
        System.out.println("aSharedKey:  " + base64Encoding(aSharedKey));
        System.out.println("ciphertext:  " + ciphertextBase64);
        System.out.println("output is    (Base64) iv : (Base64) ciphertext");

        // for decryption you need your private key (bPrivateKey) and the public key from other party (aPublicKey)
        // received ciphertext
        String ciphertextReceivedBase64 = ciphertextBase64;
        // received public key
        String aPublicKeyBase64 =  "b+Z6ajj7wI6pKAK5N28Hzp0Lyhv2PvHofwGY3WSm7W0=";
        // own private key
        String bPrivateKeyBase64 = "yNmXR5tfBXA/uZjanND+IYgGXlrFnrdUiUXesI4fOlM=";
        // convert the base64 encoded keys
        byte[] aPublicKey = base64Decoding(aPublicKeyBase64);
        byte[] bPrivateKey = base64Decoding(bPrivateKeyBase64);
        // generate shared key
        byte[] bSharedKey = curve25519GenerateKeyAgreement(bPrivateKey, aPublicKey);
        // decrypt
        String decryptedtext = aesCbcDecryptFromBase64(bSharedKey, ciphertextReceivedBase64);
        System.out.println("\n* * * decryption * * *");
        System.out.println("ciphertext:  " + ciphertextReceivedBase64);
        System.out.println("input is     (Base64) iv : (Base64) ciphertext");
        System.out.println("bPrivateKey: " + bPrivateKeyBase64);
        System.out.println("aPublicKey:  " + aPublicKeyBase64);
        System.out.println("bSharedKey:  " + base64Encoding(bSharedKey));
        System.out.println("decrypt.text:" + decryptedtext);
    }

    private static String aesCbcEncryptToBase64(byte[] key, String data) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        byte[] iv = generateRandomInitvector();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        String ciphertextBase64 = Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes(StandardCharsets.UTF_8)));
        String ivBase64 = Base64.getEncoder().encodeToString(iv);
        return ivBase64 + ":" + ciphertextBase64;
    }

    private static String aesCbcDecryptFromBase64(byte[] key, String data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        String[] parts = data.split(":", 0);
        byte[] iv = base64Decoding(parts[0]);
        byte[] encryptedData = base64Decoding(parts[1]);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
        return new String(cipher.doFinal(encryptedData));
    }

    private static byte[] generateRandomInitvector() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] iv = new byte[16];
        secureRandom.nextBytes(iv);
        return iv;
    }

    private static byte[] curve25519GenerateKeyAgreement(byte[] privateKey, byte[] publicKey) {
        Curve25519 cipher = Curve25519.getInstance(Curve25519.BEST);
        return cipher.calculateAgreement(publicKey, privateKey);
    }

    private static String base64Encoding(byte[] input) {
        return Base64.getEncoder().encodeToString(input);
    }
    private static byte[] base64Decoding(String input) {
        return Base64.getDecoder().decode(input);
    }
}
