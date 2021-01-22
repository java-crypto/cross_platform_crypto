package Argon2id;

import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

public class Argon2id {
    public static void main(String[] args) {
        // uses Bouncy Castle
        System.out.println("Generate a 32 byte long encryption key with Argon2id");

        String password = "secret password";
        System.out.println("password: " + password);

        // ### security warning - never use a fixed salt in production, this is for compare reasons only
        byte[] salt = generateFixedSalt16Byte();
        // please use below generateSalt16Byte()
        //byte[] salt = generateSalt16Byte();
        System.out.println("salt (Base64): " + base64Encoding(salt));

        // ### the minimal parameter set is probably UNSECURE ###
        String encryptionKeyArgon2id = base64Encoding(generateArgon2idMinimal(password, salt));
        System.out.println("encryptionKeyArgon2id (Base64) minimal:     " + encryptionKeyArgon2id);

        encryptionKeyArgon2id = base64Encoding(generateArgon2idInteractive(password, salt));
        System.out.println("encryptionKeyArgon2id (Base64) interactive: " + encryptionKeyArgon2id);

        encryptionKeyArgon2id = base64Encoding(generateArgon2idModerate(password, salt));
        System.out.println("encryptionKeyArgon2id (Base64) moderate:    " + encryptionKeyArgon2id);

        encryptionKeyArgon2id = base64Encoding(generateArgon2idSensitive(password, salt));
        System.out.println("encryptionKeyArgon2id (Base64) sensitive:   " + encryptionKeyArgon2id);
    }

    // ### the minimal parameter set is probably UNSECURE ###
    public static byte[] generateArgon2idMinimal(String password, byte[] salt) {
        int opsLimit = 2;
        int memLimit = 8192;
        int outputLength = 32;
        int parallelism = 1;
        Argon2Parameters.Builder builder = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
                .withVersion(Argon2Parameters.ARGON2_VERSION_13) // 19
                .withIterations(opsLimit)
                .withMemoryAsKB(memLimit)
                .withParallelism(parallelism)
                .withSalt(salt);
        Argon2BytesGenerator gen = new Argon2BytesGenerator();
        gen.init(builder.build());
        byte[] result = new byte[outputLength];
        gen.generateBytes(password.getBytes(StandardCharsets.UTF_8), result, 0, result.length);
        return result;
    }

    public static byte[] generateArgon2idInteractive(String password, byte[] salt) {
        int opsLimit = 2;
        int memLimit = 66536;
        int outputLength = 32;
        int parallelism = 1;
        Argon2Parameters.Builder builder = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
                .withVersion(Argon2Parameters.ARGON2_VERSION_13) // 19
                .withIterations(opsLimit)
                .withMemoryAsKB(memLimit)
                .withParallelism(parallelism)
                .withSalt(salt);
        Argon2BytesGenerator gen = new Argon2BytesGenerator();
        gen.init(builder.build());
        byte[] result = new byte[outputLength];
        gen.generateBytes(password.getBytes(StandardCharsets.UTF_8), result, 0, result.length);
        return result;
    }

    public static byte[] generateArgon2idModerate(String password, byte[] salt) {
        int opsLimit = 3;
        int memLimit = 262144;
        int outputLength = 32;
        int parallelism = 1;
        Argon2Parameters.Builder builder = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
                .withVersion(Argon2Parameters.ARGON2_VERSION_13) // 19
                .withIterations(opsLimit)
                .withMemoryAsKB(memLimit)
                .withParallelism(parallelism)
                .withSalt(salt);
        Argon2BytesGenerator gen = new Argon2BytesGenerator();
        gen.init(builder.build());
        byte[] result = new byte[outputLength];
        gen.generateBytes(password.getBytes(StandardCharsets.UTF_8), result, 0, result.length);
        return result;
    }

    public static byte[] generateArgon2idSensitive(String password, byte[] salt) {
        int opsLimit = 4;
        int memLimit = 1048576;
        int outputLength = 32;
        int parallelism = 1;
        Argon2Parameters.Builder builder = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
                .withVersion(Argon2Parameters.ARGON2_VERSION_13) // 19
                .withIterations(opsLimit)
                .withMemoryAsKB(memLimit)
                .withParallelism(parallelism)
                .withSalt(salt);
        Argon2BytesGenerator gen = new Argon2BytesGenerator();
        gen.init(builder.build());
        byte[] result = new byte[outputLength];
        gen.generateBytes(password.getBytes(StandardCharsets.UTF_8), result, 0, result.length);
        return result;
    }

    private static byte[] generateSalt16Byte() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[16];
        secureRandom.nextBytes(salt);
        return salt;
    }

    private static byte[] generateFixedSalt16Byte() {
        // ### security warning - never use this in production ###
        byte[] salt = new byte[16]; // 16 x0's
        return salt;
    }

    private static String base64Encoding(byte[] input) {
        return Base64.getEncoder().encodeToString(input);
    }
}
