package Libsodium_SealedCryptobox_Encryption_String;

import ove.crypto.digest.Blake2b;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Base64;

public class LibsodiumSealedCryptoboxEncryptionString {
    // uses https://github.com/alphazero/Blake2b/blob/master/src/main/java/ove/crypto/digest/Blake2b.java
    // or
    public static void main(String[] args) throws GeneralSecurityException {
        System.out.println("Libsodium sealed crypto box hybrid string encryption");

        String plaintext = "The quick brown fox jumps over the lazy dog";
        System.out.println("plaintext: " + plaintext);

        // encryption
        System.out.println("\n* * * encryption * * *");
        System.out.println("all data are in Base64 encoding");
        // for encryption you need the public key from the one you are sending the data to (party B)
        String publicKeyBBase64 =  "jVSuHjVH47PMbMaAxL5ziBS9/Z0HQK6TJLw9X3Jw6yg=";
        // convert the base64 encoded keys
        byte[] publicKeyB = base64Decoding(publicKeyBBase64);
        String ciphertextBase64 = sealedCryptoboxEncryptionToBase64(publicKeyB, plaintext);
        System.out.println("publicKeyB:  " + publicKeyBBase64);
        System.out.println("ciphertext:  " + ciphertextBase64);
        System.out.println("output is (Base64) ciphertext");

        System.out.println("\n* * * decryption * * *");
        // received ciphertext
        String ciphertextReceivedBase64 = ciphertextBase64;
        // for decryption you need your private key (privateKeyB)
        String privateKeyBBase64 = "yNmXR5tfBXA/uZjanND+IYgGXlrFnrdUiUXesI4fOlM=";
        // convert the base64 encoded keys
        byte[] privateKeyB = base64Decoding(privateKeyBBase64);
        String decryptedtext = sealedCryptoboxDecryptionFromBase64(privateKeyB, ciphertextReceivedBase64);
        System.out.println("ciphertext:  " + ciphertextReceivedBase64);
        System.out.println("input is (Base64) ciphertext");
        System.out.println("privateKeyB: " + privateKeyBBase64);
        System.out.println("decrypt.text:" + decryptedtext);
    }

    private static String sealedCryptoboxEncryptionToBase64(byte[] publicKey, String plaintext) throws GeneralSecurityException {
        final int crypto_box_PUBLICKEYBYTES = 32;
        byte[] data = plaintext.getBytes(StandardCharsets.UTF_8);
        // create ephemeral keypair for sender
        TweetNaclFast.Box.KeyPair ephkeypair = TweetNaclFast.Box.keyPair();
        // create nonce
        byte[] nonce = crypto_box_seal_nonce(ephkeypair.getPublicKey(), publicKey);
        TweetNaclFast.Box box = new TweetNaclFast.Box(publicKey, ephkeypair.getSecretKey());
        byte[] ciphertext = box.box(data, nonce);
        if (ciphertext == null) throw new GeneralSecurityException("could not create box");
        byte[] sealedbox = new byte[ciphertext.length + crypto_box_PUBLICKEYBYTES];
        byte[] ephpubkey = ephkeypair.getPublicKey();
        for (int i = 0; i < crypto_box_PUBLICKEYBYTES; i ++)
            sealedbox[i] = ephpubkey[i];
        for(int i = 0; i < ciphertext.length; i ++)
            sealedbox[i+crypto_box_PUBLICKEYBYTES]=ciphertext[i];
        return base64Encoding(sealedbox);
    }

    private static String sealedCryptoboxDecryptionFromBase64(byte[] privateKey, String ciphertextBase64) throws GeneralSecurityException {
        final int crypto_box_PUBLICKEYBYTES = 32;
        final int crypto_box_MACBYTES = 16;
        final int crypto_box_SEALBYTES = (crypto_box_PUBLICKEYBYTES + crypto_box_MACBYTES);
        byte[] ciphertext = base64Decoding(ciphertextBase64);
        if ( ciphertext.length < crypto_box_SEALBYTES) throw new IllegalArgumentException("Ciphertext too short");
        byte[] pksender = Arrays.copyOfRange(ciphertext, 0, crypto_box_PUBLICKEYBYTES);
        byte[] ciphertextwithmac = Arrays.copyOfRange(ciphertext, crypto_box_PUBLICKEYBYTES , ciphertext.length);
        TweetNaclFast.Box.KeyPair keyPair = TweetNaclFast.Box.keyPair_fromSecretKey(privateKey);
        byte[] nonce = crypto_box_seal_nonce(pksender, keyPair.getPublicKey());
        TweetNaclFast.Box box = new TweetNaclFast.Box(pksender, privateKey);
        byte[] cleartext = box.open(ciphertextwithmac, nonce);
        if (cleartext == null) throw new GeneralSecurityException("could not open box");
        return new String(cleartext, StandardCharsets.UTF_8);
    }

    private static byte[] crypto_box_seal_nonce(byte[] senderpk, byte[] mypk){
        final int crypto_box_NONCEBYTES = 24;
        final Blake2b blake2b = Blake2b.Digest.newInstance( crypto_box_NONCEBYTES );
        blake2b.update(senderpk);
        blake2b.update(mypk);
        byte[] nonce = blake2b.digest();
        if (nonce == null || nonce.length!=crypto_box_NONCEBYTES) throw new IllegalArgumentException("Blake2b hashing failed");
        return nonce;
    }

    private static String base64Encoding(byte[] input) {return Base64.getEncoder().encodeToString(input);
    }
    private static byte[] base64Decoding(String input) {return Base64.getDecoder().decode(input);
    }
}

