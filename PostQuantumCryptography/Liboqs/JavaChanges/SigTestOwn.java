package org.openquantumsafe;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SigTestOwn {
    // ### important: you need to change Signature.java as well !!! for code in line 57
    private static String logString = "";
    public static void run(String sigName) throws IOException {
        logString = "";
        String filename = "SIG_" + sigName;
        System.out.println("testing " + sigName);
        String printString = "PQC signature using";
        printLog(printString);
        printString = "OpenQuantumSafe.org liboqs-java library";
        printLog(printString);
        printString = "SIG algorithm: " + sigName;
        printLog(printString);

        printString = "\n*** generate a key pair for signer ***";
        printLog(printString);
        // create signer and verifier
        Signature signer = new Signature(sigName);
        Signature verifier = new Signature(sigName);
        // generate signer key pair
        byte[] signer_public_key = signer.generate_keypair();
        byte[] signerPrivateKeyEncoded = signer.export_secret_key();
        byte[] signerPublicKeyEncoded = signer.export_public_key();
        printString = "generated private key length: " + signerPrivateKeyEncoded.length;
        printLog(printString);
        printString = "generated public key length:  " + signerPublicKeyEncoded.length;
        printLog(printString);
        // save the keys to file
        Files.write(Paths.get(filename + "_signer_privatekey.dat"), signerPrivateKeyEncoded);
        Files.write(Paths.get(filename + "_signer_publickey.dat"), signerPublicKeyEncoded);
        printString = "private and public key for signer saved to file";
        printLog(printString);

        printString = "\n* * * sign the plaintext with the private key * * *";
        printLog(printString);
        byte[] message = "The quick brown fox jumps over the lazy dog".getBytes();
        byte[] signature = signer.sign(message);
        printString = "signature length: " + signature.length + " data: in a separate file";
        printLog(printString);
        // save the signature to file
        Files.write(Paths.get(filename + "_signature.dat"), signature);

        printString = "\n* * * verify the signature against the plaintext with the public key * * *";
        printLog(printString);
        // verify the signature
        boolean is_valid = verifier.verify(message, signature, signer_public_key);
        printString = "\nsignature is verified: " + is_valid;
        printLog(printString);

        printString = "\ninformation from Liboqs about used algorithm:\n" + signer.get_details();
        printLog(printString);
        // save logString to file
        Files.write(Paths.get(filename + ".txt"), logString.getBytes(StandardCharsets.UTF_8));
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuffer result = new StringBuffer();
        for (byte b : bytes) result.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        return result.toString();
    }

    private static void printLog(String string) {
        logString = logString + string + "\n";
    }
}
