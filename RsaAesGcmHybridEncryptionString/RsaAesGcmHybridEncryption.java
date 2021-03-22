package RSA_AES_GCM_Hybrid_String_Encryption;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RsaAesGcmHybridEncryption {
    public static void main(String[] args) throws GeneralSecurityException {
        System.out.println("RSA AES GCM hybrid encryption");
        System.out.println("\nuses AES GCM 256 random key for the plaintext encryption");
        System.out.println("and RSA OAEP SHA 1 2048 bit for the encryption of the random key\n");

        String plaintextString = "The quick brown fox jumps over the lazy dog";
        System.out.println("plaintext: " + plaintextString);

        System.out.println("\n* * * Encryption * * *");
        // # # # usually we would load the private and public key from a file or keystore # # #
        // # # # here we use hardcoded keys for demonstration - don't do this in real programs # # #
        // String filenamePrivateKeyPem = "privatekey2048.pem";
        // String filenamePublicKeyPem = "publickey2048.pem";
        PublicKey publicKeyLoad = getPublicKeyFromString(loadRsaPublicKeyPem());
        // use this in production
        //PublicKey publicKeyLoad = getPublicKeyFromString(loadRsaKeyPemFile(filenamePublicKeyPem));
        String completeCiphertextBase64 = rsaOaepSha1AesGcm256HybridStringEncryption(publicKeyLoad, plaintextString);
        System.out.println("complete ciphertext: " + completeCiphertextBase64);
        System.out.println("output is (Base64) encryptionKey : (Base64) nonce : (Base64) ciphertext : (Base64) gcmTag");

        // transport the encrypted data to recipient

        // receiving the encrypted data, split data
        System.out.println("\n* * * Decryption * * *");
        String completeCiphertextReceivedBase64 = completeCiphertextBase64;
        System.out.println("completeCiphertextReceived: " + completeCiphertextReceivedBase64);
        PrivateKey privateKeyLoad = getPrivateKeyFromString(loadRsaPrivateKeyPem());
        // use this in production
        //PrivateKey privateKeyLoad = getPrivateKeyFromString(loadRsaKeyPemFile(filenamePrivateKeyPem));
        String decryptedtext = rsaOaepSha1AesGcm256HybridStringDecryption(privateKeyLoad, completeCiphertextReceivedBase64);
        System.out.println("plaintext:  " + decryptedtext);
    }

    private static byte[] generateRandomAesKey() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] key = new byte[32];
        secureRandom.nextBytes(key);
        return key;
    }

    private static byte[] generateRandomNonce() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] nonce = new byte[12];
        secureRandom.nextBytes(nonce);
        return nonce;
    }

    public static byte[] concatenateByteArrays(byte[] a, byte[] b) {
        return ByteBuffer
                .allocate(a.length + b.length)
                .put(a).put(b)
                .array();
    }

    private static String rsaOaepSha1AesGcm256HybridStringEncryption (PublicKey publicKey, String plaintext)
            throws NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException,
            IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        // generate random key
        byte[] encryptionKey = generateRandomAesKey();
        // encrypt plaintext
         String ciphertextBase64 = aesGcmEncryptToBase64(encryptionKey, plaintext);
        // encrypt the aes encryption key with the rsa public key
        String encryptionKeyBase64 = base64Encoding(rsaEncryptionOaepSha1(publicKey, encryptionKey));
        // complete output
        return encryptionKeyBase64 + ":" + ciphertextBase64;
    }

    private static String rsaOaepSha1AesGcm256HybridStringDecryption (PrivateKey privateKey, String completeCiphertext)
            throws NoSuchPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException,
            IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        String[] parts = completeCiphertext.split(":", 0);
        byte[] encryptionKeyReceived = base64Decoding(parts[0]);
        byte[] nonce = base64Decoding(parts[1]);
        byte[] encryptedData = base64Decoding(parts[2]);
        byte[] gcmTag = base64Decoding(parts[3]);
        // decrypt the encryption key with the rsa private key
        byte[] decryptionKey = rsaDecryptionOaepSha1(privateKey, encryptionKeyReceived);
        return aesGcmDecrypt(decryptionKey, nonce, encryptedData, gcmTag);
    }

    private static String aesGcmEncryptToBase64(byte[] key, String data) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        byte[] nonce = generateRandomNonce();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(16 * 8, nonce);
        Cipher cipher = Cipher.getInstance("AES/GCM/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, gcmParameterSpec);
        byte[] ciphertextWithTag = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        byte[] ciphertext = new byte[(ciphertextWithTag.length-16)];
        byte[] gcmTag = new byte[16];
        System.arraycopy(ciphertextWithTag, 0, ciphertext, 0, (ciphertextWithTag.length - 16));
        System.arraycopy(ciphertextWithTag, (ciphertextWithTag.length-16), gcmTag, 0, 16);
        String nonceBase64 = base64Encoding(nonce);
        String ciphertextBase64 = base64Encoding(ciphertext);
        String gcmTagBase64 = base64Encoding(gcmTag);
        return nonceBase64 + ":" + ciphertextBase64 + ":" + gcmTagBase64;
    }

    private static String aesGcmDecrypt(byte[] key, byte[] nonce, byte[] ciphertextWithoutTag, byte[] gcmTag)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        byte[] encryptedData = concatenateByteArrays(ciphertextWithoutTag, gcmTag);
        Cipher cipher = Cipher.getInstance("AES/GCM/PKCS5Padding");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(16 * 8, nonce);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, gcmParameterSpec);
        return new String(cipher.doFinal(encryptedData));
    }

    public static byte[] rsaEncryptionOaepSha1 (PublicKey publicKey, byte[] plaintextByte) throws NoSuchAlgorithmException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException, InvalidAlgorithmParameterException {
        byte[] ciphertextByte = null;
        Cipher encryptCipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-1ANDMGF1PADDING");
        OAEPParameterSpec oaepParameterSpecJCE = new OAEPParameterSpec("SHA1", "MGF1", MGF1ParameterSpec.SHA1, PSource.PSpecified.DEFAULT);
        encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey, oaepParameterSpecJCE);
        ciphertextByte = encryptCipher.doFinal(plaintextByte);
        return ciphertextByte;
    }

    public static byte[] rsaDecryptionOaepSha1 (PrivateKey privateKey, byte[] ciphertextByte) throws NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        byte[] decryptedtextByte = null;
        Cipher decryptCipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-1ANDMGF1PADDING");
        OAEPParameterSpec oaepParameterSpecJCE = new OAEPParameterSpec("SHA1", "MGF1", MGF1ParameterSpec.SHA1, PSource.PSpecified.DEFAULT);
        decryptCipher.init(Cipher.DECRYPT_MODE, privateKey, oaepParameterSpecJCE);
        decryptedtextByte = decryptCipher.doFinal(ciphertextByte);
        return decryptedtextByte;
    }

    private static String base64Encoding(byte[] input) {
        return Base64.getEncoder().encodeToString(input);
    }
    private static byte[] base64Decoding(String input) {
        return Base64.getDecoder().decode(input);
    }

    private static String loadRsaPrivateKeyPem() {
        // this is a sample key - don't worry !
        return "-----BEGIN PRIVATE KEY-----\n" +
                "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDwSZYlRn86zPi9\n" +
                "e1RTZL7QzgE/36zjbeCMyOhf6o/WIKeVxFwVbG2FAY3YJZIxnBH+9j1XS6f+ewjG\n" +
                "FlJY4f2IrOpS1kPiO3fmOo5N4nc8JKvjwmKtUM0t63uFFPfs69+7mKJ4w3tk2mSN\n" +
                "4gb8J9P9BCXtH6Q78SdOYvdCMspA1X8eERsdLb/jjHs8+gepKqQ6+XwZbSq0vf2B\n" +
                "MtaAB7zTX/Dk+ZxDfwIobShPaB0mYmojE2YAQeRq1gYdwwO1dEGk6E5J2toWPpKY\n" +
                "/IcSYsGKyFqrsmbw0880r1BwRDer4RFrkzp4zvY+kX3eDanlyMqDLPN+ghXT1lv8\n" +
                "snZpbaBDAgMBAAECggEBAIVxmHzjBc11/73bPB2EGaSEg5UhdzZm0wncmZCLB453\n" +
                "XBqEjk8nhDsVfdzIIMSEVEowHijYz1c4pMq9osXR26eHwCp47AI73H5zjowadPVl\n" +
                "uEAot/xgn1IdMN/boURmSj44qiI/DcwYrTdOi2qGA+jD4PwrUl4nsxiJRZ/x7PjL\n" +
                "hMzRbvDxQ4/Q4ThYXwoEGiIBBK/iB3Z5eR7lFa8E5yAaxM2QP9PENBr/OqkGXLWV\n" +
                "qA/YTxs3gAvkUjMhlScOi7PMwRX9HsrAeLKbLuC1KJv1p2THUtZbOHqrAF/uwHaj\n" +
                "ygUblFaa/BTckTN7PKSVIhp7OihbD04bSRrh+nOilcECgYEA/8atV5DmNxFrxF1P\n" +
                "ODDjdJPNb9pzNrDF03TiFBZWS4Q+2JazyLGjZzhg5Vv9RJ7VcIjPAbMy2Cy5BUff\n" +
                "EFE+8ryKVWfdpPxpPYOwHCJSw4Bqqdj0Pmp/xw928ebrnUoCzdkUqYYpRWx0T7YV\n" +
                "RoA9RiBfQiVHhuJBSDPYJPoP34kCgYEA8H9wLE5L8raUn4NYYRuUVMa+1k4Q1N3X\n" +
                "Bixm5cccc/Ja4LVvrnWqmFOmfFgpVd8BcTGaPSsqfA4j/oEQp7tmjZqggVFqiM2m\n" +
                "J2YEv18cY/5kiDUVYR7VWSkpqVOkgiX3lK3UkIngnVMGGFnoIBlfBFF9uo02rZpC\n" +
                "5o5zebaDImsCgYAE9d5wv0+nq7/STBj4NwKCRUeLrsnjOqRriG3GA/TifAsX+jw8\n" +
                "XS2VF+PRLuqHhSkQiKazGr2Wsa9Y6d7qmxjEbmGkbGJBC+AioEYvFX9TaU8oQhvi\n" +
                "hgA6ZRNid58EKuZJBbe/3ek4/nR3A0oAVwZZMNGIH972P7cSZmb/uJXMOQKBgQCs\n" +
                "FaQAL+4sN/TUxrkAkylqF+QJmEZ26l2nrzHZjMWROYNJcsn8/XkaEhD4vGSnazCu\n" +
                "/B0vU6nMppmezF9Mhc112YSrw8QFK5GOc3NGNBoueqMYy1MG8Xcbm1aSMKVv8xba\n" +
                "rh+BZQbxy6x61CpCfaT9hAoA6HaNdeoU6y05lBz1DQKBgAbYiIk56QZHeoZKiZxy\n" +
                "4eicQS0sVKKRb24ZUd+04cNSTfeIuuXZrYJ48Jbr0fzjIM3EfHvLgh9rAZ+aHe/L\n" +
                "84Ig17KiExe+qyYHjut/SC0wODDtzM/jtrpqyYa5JoEpPIaUSgPuTH/WhO3cDsx6\n" +
                "3PIW4/CddNs8mCSBOqTnoaxh\n" +
                "-----END PRIVATE KEY-----";
    }

    private static String loadRsaPublicKeyPem() {
        // this is a sample key - don't worry !
        return "-----BEGIN PUBLIC KEY-----\n" +
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA8EmWJUZ/Osz4vXtUU2S+\n" +
                "0M4BP9+s423gjMjoX+qP1iCnlcRcFWxthQGN2CWSMZwR/vY9V0un/nsIxhZSWOH9\n" +
                "iKzqUtZD4jt35jqOTeJ3PCSr48JirVDNLet7hRT37Ovfu5iieMN7ZNpkjeIG/CfT\n" +
                "/QQl7R+kO/EnTmL3QjLKQNV/HhEbHS2/44x7PPoHqSqkOvl8GW0qtL39gTLWgAe8\n" +
                "01/w5PmcQ38CKG0oT2gdJmJqIxNmAEHkatYGHcMDtXRBpOhOSdraFj6SmPyHEmLB\n" +
                "ishaq7Jm8NPPNK9QcEQ3q+ERa5M6eM72PpF93g2p5cjKgyzzfoIV09Zb/LJ2aW2g\n" +
                "QwIDAQAB\n" +
                "-----END PUBLIC KEY-----";
    }

    public static PrivateKey getPrivateKeyFromString(String key) throws GeneralSecurityException {
        String privateKeyPEM = key;
        privateKeyPEM = privateKeyPEM.replace("-----BEGIN PRIVATE KEY-----", "");
        privateKeyPEM = privateKeyPEM.replace("-----END PRIVATE KEY-----", "");
        privateKeyPEM = privateKeyPEM.replaceAll("[\\r\\n]+", "");
        byte[] encoded = Base64.getDecoder().decode(privateKeyPEM);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
        PrivateKey privKey = (PrivateKey) kf.generatePrivate(keySpec);
        return privKey;
    }

    public static PublicKey getPublicKeyFromString(String key) throws GeneralSecurityException {
        String publicKeyPEM = key;
        publicKeyPEM = publicKeyPEM.replace("-----BEGIN PUBLIC KEY-----", "");
        publicKeyPEM = publicKeyPEM.replace("-----END PUBLIC KEY-----", "");
        publicKeyPEM = publicKeyPEM.replaceAll("[\\r\\n]+", "");
        byte[] encoded = Base64.getDecoder().decode(publicKeyPEM);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PublicKey pubKey = (PublicKey) kf.generatePublic(new X509EncodedKeySpec(encoded));
        return pubKey;
    }

    private static String loadRsaKeyPemFile(String filename) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filename)), StandardCharsets.UTF_8);
    }
}
