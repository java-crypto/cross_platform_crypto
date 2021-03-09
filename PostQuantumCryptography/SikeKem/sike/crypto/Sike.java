/*
 * Copyright 2020 Wultra s.r.o.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package sike.crypto;

import sike.math.api.Fp2Element;
import sike.model.*;
import sike.param.SikeParam;
import sike.util.ByteEncoding;
import sike.util.Sha3;

import java.math.BigInteger;
import java.security.*;

/**
 * SIKE key encapsulation.
 *
 * @author Roman Strobl, roman.strobl@wultra.com
 */
public class Sike {

    private final SikeParam sikeParam;
    private final RandomGenerator randomGenerator;
    private final KeyGenerator keyGenerator;
    private final Sidh sidh;

    /**
     * SIKE key encapsulation constructor.
     * @param sikeParam SIKE parameters.
     */
    public Sike(SikeParam sikeParam) {
        this.sikeParam = sikeParam;
        this.randomGenerator = new RandomGenerator();
        keyGenerator = new KeyGenerator(sikeParam);
        sidh = new Sidh(sikeParam);
    }

    /**
     * SIKE key encapsulation constructor with specified SecureRandom.
     * @param sikeParam SIKE parameters.
     * @param secureRandom SecureRandom to use.
     */
    public Sike(SikeParam sikeParam, SecureRandom secureRandom) {
        this.sikeParam = sikeParam;
        this.randomGenerator = new RandomGenerator(secureRandom);
        keyGenerator = new KeyGenerator(sikeParam, randomGenerator);
        sidh = new Sidh(sikeParam);
    }

    /**
     * SIKE encapsulation.
     * @param pk3 Bob's public key.
     * @return Encapsulation result with shared secret and encrypted message.
     * @throws GeneralSecurityException Thrown in case cryptography fails.
     */
    public EncapsulationResult encapsulate(PublicKey pk3) throws GeneralSecurityException {
        if (!(pk3 instanceof SidhPublicKey)) {
            throw new InvalidKeyException("Invalid public key");
        }
        byte[] m = randomGenerator.generateRandomBytes(sikeParam.getMessageBytes());
        byte[] r = generateR(m, pk3.getEncoded());
        EncryptedMessage encrypted = encrypt(pk3, m, r);
        SidhPublicKey c0Key = (SidhPublicKey) encrypted.getC0();
        byte[] k = generateK(m, c0Key.getEncoded(), encrypted.getC1());
        return new EncapsulationResult(k, encrypted);
    }

    /**
     * SIKE decapsulation.
     * @param sk3 Bob's private key.
     * @param pk3 Bob's public key.
     * @param encrypted Encrypted message received from Alice.
     * @return Shared secret.
     * @throws GeneralSecurityException Thrown in case cryptography fails.
     */
    public byte[] decapsulate(PrivateKey sk3, PublicKey pk3, EncryptedMessage encrypted) throws GeneralSecurityException {
        if (!(sk3 instanceof SidhPrivateKey)) {
            throw new InvalidKeyException("Invalid private key");
        }
        if (!(pk3 instanceof SidhPublicKey)) {
            throw new InvalidKeyException("Invalid public key");
        }
        if (encrypted == null) {
            throw new InvalidParameterException("Encrypted message is null");
        }
        if (encrypted.getC0() == null) {
            throw new InvalidParameterException("Invalid parameter c0");
        }
        if (encrypted.getC1() == null) {
            throw new InvalidParameterException("Invalid parameter c1");
        }
        SidhPrivateKey priv3 = (SidhPrivateKey) sk3;
        if (priv3.getS() == null) {
            throw new InvalidParameterException("Private key cannot be used for decapsulation");
        }
        byte[] m = decrypt(sk3, encrypted);
        byte[] r = generateR(m, pk3.getEncoded());
        BigInteger modulo = new BigInteger("2").pow(sikeParam.getEA());
        BigInteger key = ByteEncoding.fromByteArray(r).mod(modulo);
        PrivateKey rKey = new SidhPrivateKey(sikeParam, Party.ALICE, key);
        PublicKey c0Key = keyGenerator.derivePublicKey(Party.ALICE, rKey);
        byte[] k;
        // The public key equals method runs in constant time
        if (c0Key.equals(encrypted.getC0())) {
            k = generateK(m, c0Key.getEncoded(), encrypted.getC1());
        } else {
            k = generateK(priv3.getS(), c0Key.getEncoded(), encrypted.getC1());
        }
        return k;
    }

    /**
     * Encrypt a message.
     * @param pk3 Bob's public key.
     * @param m Message to encrypt, the message size must correspond to the SIKE parameter messageBytes.
     * @return Encrypted message.
     * @throws GeneralSecurityException Thrown in case cryptography fails.
     */
    public EncryptedMessage encrypt(PublicKey pk3, byte[] m) throws GeneralSecurityException {
        return encrypt(pk3, m, null);
    }

    /**
     * Encrypt a message.
     * @param pk3 Bob's public key.
     * @param m Message to encrypt, the message size must correspond to the SIKE parameter messageBytes.
     * @param r Optional byte representation of Alice's private key used in SIKE encapsulation.
     * @return Encrypted message.
     * @throws GeneralSecurityException Thrown in case cryptography fails.
     */
    private EncryptedMessage encrypt(PublicKey pk3, byte[] m, byte[] r) throws GeneralSecurityException {
        if (!(pk3 instanceof SidhPublicKey)) {
            throw new InvalidKeyException("Invalid public key");
        }
        if (m == null || m.length != sikeParam.getMessageBytes()) {
            throw new InvalidParameterException("Invalid message");
        }
        PrivateKey sk2;
        if (r == null) {
            // Generate ephemeral private key
            sk2 = keyGenerator.generatePrivateKey(Party.ALICE);
        } else {
            // Convert value r into private key
            BigInteger modulo = new BigInteger("2").pow(sikeParam.getEA());
            BigInteger key = ByteEncoding.fromByteArray(r).mod(modulo);
            sk2 = new SidhPrivateKey(sikeParam, Party.ALICE, key);
        }
        PublicKey c0 = keyGenerator.derivePublicKey(Party.ALICE, sk2);
        Fp2Element j = sidh.generateSharedSecret(Party.ALICE, sk2, pk3);
        byte[] h = Sha3.shake256(j.getEncoded(), sikeParam.getMessageBytes());
        byte[] c1 = new byte[sikeParam.getMessageBytes()];
        for (int i = 0; i < sikeParam.getMessageBytes(); i++) {
            c1[i] = (byte) (h[i] ^ m[i]);
        }
        return new EncryptedMessage(c0, c1);
    }

    /**
     * Decrypt a message.
     * @param sk3 Bob's private key.
     * @param encrypted Encrypted message received from Alice.
     * @return Decrypted message.
     * @throws GeneralSecurityException Thrown in case cryptography fails.
     */
    public byte[] decrypt(PrivateKey sk3, EncryptedMessage encrypted) throws GeneralSecurityException {
        if (!(sk3 instanceof SidhPrivateKey)) {
            throw new InvalidKeyException("Invalid private key");
        }
        if (encrypted == null) {
            throw new InvalidParameterException("Encrypted message is null");
        }
        PublicKey c0 = encrypted.getC0();
        if (!(c0 instanceof SidhPublicKey)) {
            throw new InvalidKeyException("Invalid public key");
        }
        byte[] c1 = encrypted.getC1();
        if (c1 == null) {
            throw new InvalidParameterException("Invalid parameter c1");
        }
        Fp2Element j = sidh.generateSharedSecret(Party.BOB, sk3, c0);
        byte[] h = Sha3.shake256(j.getEncoded(), sikeParam.getMessageBytes());
        byte[] m = new byte[sikeParam.getMessageBytes()];
        for (int i = 0; i < sikeParam.getMessageBytes(); i++) {
            m[i] = (byte) (h[i] ^ c1[i]);
        }
        return m;
    }

    /**
     * Generate the ephemeral private key r.
     * @param m Nonce.
     * @param pk3Enc Public key pk3 encoded in bytes.
     * @return Ephemeral private key r encoded in bytes.
     */
    private byte[] generateR(byte[] m, byte[] pk3Enc) {
        byte[] dataR = new byte[(m.length + pk3Enc.length)];
        System.arraycopy(m, 0, dataR, 0, m.length);
        System.arraycopy(pk3Enc, 0, dataR, m.length, pk3Enc.length);
        return Sha3.shake256(dataR, (sikeParam.getBitsA() + 7) / 8);
    }

    /**
     * Generate the shared secret K.
     * @param m Nonce.
     * @param c0 Public key bytes.
     * @param c1 Encrypted message bytes.
     * @return Shared secret bytes.
     */
    private byte[] generateK(byte[] m, byte[] c0, byte[] c1) {
        byte[] dataK = new byte[(m.length + c0.length + c1.length)];
        System.arraycopy(m, 0, dataK, 0, m.length);
        System.arraycopy(c0, 0, dataK, m.length, c0.length);
        System.arraycopy(c1, 0, dataK, m.length + c0.length, c1.length);
        return Sha3.shake256(dataK, sikeParam.getCryptoBytes());
    }

}
