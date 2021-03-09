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

import sike.model.ImplementationType;
import sike.model.MontgomeryCurve;
import sike.model.Party;
import sike.model.SidhPrivateKey;
import sike.param.SikeParam;
import sike.util.ByteEncoding;

import java.math.BigInteger;
import java.security.*;

/**
 * SIDH and SIKE key generator.
 *
 * @author Roman Strobl, roman.strobl@wultra.com
 */
public class KeyGenerator {

    private final SikeParam sikeParam;
    private final RandomGenerator randomGenerator;

    /**
     * Key generator constructor.
     * @param sikeParam SIKE parameters.
     */
    public KeyGenerator(SikeParam sikeParam) {
        this.sikeParam = sikeParam;
        this.randomGenerator = new RandomGenerator();
    }

    /**
     * Constructor for key generator with alternative random generator.
     * @param sikeParam SIKE parameters.
     * @param randomGenerator Alternative random generator.
     */
    public KeyGenerator(SikeParam sikeParam, RandomGenerator randomGenerator) {
        this.sikeParam = sikeParam;
        this.randomGenerator = randomGenerator;
    }

    /**
     * Generate a key pair.
     * @param party Alice or Bob.
     * @return Generated key pair.
     * @throws GeneralSecurityException Thrown in case cryptography fails.
     */
    public KeyPair generateKeyPair(Party party) throws GeneralSecurityException {
        PrivateKey privateKey = generatePrivateKey(party);
        PublicKey publicKey = derivePublicKey(party, privateKey);
        return new KeyPair(publicKey, privateKey);
    }

    /**
     * Generate a private key.
     * @param party Alice or Bob.
     * @return Generated key pair.
     * @throws GeneralSecurityException Thrown in case cryptography fails.
     */
    public PrivateKey generatePrivateKey(Party party) throws GeneralSecurityException {
        byte[] s = randomGenerator.generateRandomBytes(sikeParam.getMessageBytes());
        BigInteger randomKey = generateRandomKey(sikeParam, party);
        return new SidhPrivateKey(sikeParam, party, randomKey, s);
    }

    /**
     * Derive public key from a private key.
     * @param party Alice or Bob.
     * @param privateKey Private key.
     * @return Derived public key.
     * @throws InvalidKeyException Thrown in case key derivation fails.
     */
    public PublicKey derivePublicKey(Party party, PrivateKey privateKey) throws InvalidKeyException {
        if (!(privateKey instanceof SidhPrivateKey)) {
            throw new InvalidKeyException("Invalid private key");
        }
        SidhPrivateKey priv = (SidhPrivateKey) privateKey;
        MontgomeryCurve curve;
        if (sikeParam.getImplementationType() == ImplementationType.REFERENCE) {
            curve = new MontgomeryCurve(sikeParam, sikeParam.getA(), sikeParam.getB());
        } else if (sikeParam.getImplementationType() == ImplementationType.OPTIMIZED) {
            curve = new MontgomeryCurve(sikeParam, sikeParam.getA());
        } else {
            throw new InvalidParameterException("Unsupported implementation type");
        }
        if (party == Party.ALICE) {
            return sikeParam.getIsogeny().isoGen2(curve, priv);
        } else if (party == Party.BOB) {
            return sikeParam.getIsogeny().isoGen3(curve, priv);
        }
        throw new InvalidParameterException("Invalid party");
    }

    /**
     * Generate a random key.
     * @param sikeParam SIKE parameters.
     * @return Random BigInteger usable as a private key.
     * @throws GeneralSecurityException Thrown in case cryptography fails.
     */
    private BigInteger generateRandomKey(SikeParam sikeParam, Party party) throws GeneralSecurityException {
        if (party == Party.ALICE) {
            // random value in [0, 2^eA - 1]
            int length = (sikeParam.getBitsA() + 7) / 8;
            byte[] randomBytes = randomGenerator.generateRandomBytes(length);
            randomBytes[randomBytes.length - 1] &= sikeParam.getMaskA();
            return ByteEncoding.fromByteArray(randomBytes);
        }
        if (party == Party.BOB) {
            // random value in [0, 2^Floor(Log(2,3^eB)) - 1]
            int length = (sikeParam.getBitsB() - 1 + 7) / 8;
            byte[] randomBytes = randomGenerator.generateRandomBytes(length);
            randomBytes[randomBytes.length - 1] &= sikeParam.getMaskB();
            return ByteEncoding.fromByteArray(randomBytes);
        }
        throw new InvalidParameterException("Invalid party");
    }

}
