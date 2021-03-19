package org.openquantumsafe;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class KemTestOwn {
    // ### important: you need to change KeyEncapsulation.java as well !!! for code in line 77
    private static String logString = "";
    public static void run(String kemName) throws IOException {
        logString = "";
        String filename = "KEM_" + kemName;
        System.out.println("testing " + kemName);
        String printString = "PQC key encapsulation mechanism (KEM) using";
        printLog(printString);
        printString = "OpenQuantumSafe.org liboqs-java library";
        printLog(printString);
        printString = "KEM algorithm: " + kemName;
        printLog(printString);

        printString = "\n*** generate a key pair for client=receiver and server=sender ***";
        printLog(printString);
        KeyEncapsulation client = new KeyEncapsulation(kemName);
        KeyEncapsulation server = new KeyEncapsulation(kemName);
        // generate key pair for server
        byte[] serverPublicKey = server.generate_keypair(); // dummy for saving
        byte[] serverPrivateKeyEncoded = server.export_secret_key();
        byte[] serverPublicKeyEncoded = server.export_public_key();
        printString = "generated private key length: " + serverPrivateKeyEncoded.length;
        printLog(printString);
        printString = "generated public key length:  " + serverPublicKeyEncoded.length;
        printLog(printString);
        // generate key pair for client
        byte[] clientPublicKey = client.generate_keypair(); // dummy for saving
        byte[] clientPrivateKeyEncoded = client.export_secret_key();
        byte[] clientPublicKeyEncoded = client.export_public_key();
        // save the keys to file
        Files.write(Paths.get(filename + "_server_privatekey.dat"), serverPrivateKeyEncoded);
        Files.write(Paths.get(filename + "_server_publickey.dat"), serverPublicKeyEncoded);
        Files.write(Paths.get(filename + "_client_privatekey.dat"), clientPrivateKeyEncoded);
        Files.write(Paths.get(filename + "_client_publickey.dat"), clientPublicKeyEncoded);
        printString = "private and public key for server and client saved to file";
        printLog(printString);

        // the client generates a public key and sends it to the sender
        // already done above
        // byte[] clientPublicKey = client.generate_keypair();

        // the sender generates his shared secret
        printString = "\n* * * generate the keyToEncrypt with the public key of the recipient * * *";
        printLog(printString);
        printString = "length of the received clientPublicKey: " + clientPublicKey.length;
        printLog(printString);

        // server: encapsulate secret with client's public key
        Pair<byte[], byte[]> server_pair = server.encap_secret(clientPublicKey);
        byte[] encryptedKey = server_pair.getLeft(); // send to recipient along with the ciphertext
        printString = "encryptedKey length: " + encryptedKey.length + " data: " + bytesToHex(encryptedKey);
        printLog(printString);
        // get the shared secret on server side
        byte[] sharedSecretServer = server_pair.getRight();
        printString = "sharedSecretServer length: " + sharedSecretServer.length + " data: "
                + bytesToHex(sharedSecretServer);
        printLog(printString);
        // save the encrypted key to file
        Files.write(Paths.get(filename + "_encryptedkey.dat"), encryptedKey);

        printString = "\n* * * decapsulate the keyToEncrypt with the private key of the recipient * * *";
        printLog(printString);
        // client: decapsulate
        byte[] sharedSecretClient = client.decap_secret(encryptedKey);
        printString = "sharedSecretClient length: " + sharedSecretClient.length + " data: "
                + bytesToHex(sharedSecretClient);
        printLog(printString);

        printString = "\ninformation from Liboqs about used algorithm:\n" + server.get_details();
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
