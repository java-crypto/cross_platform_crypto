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
package sike.model;

import sike.param.SikeParam;
import sike.util.SideChannelUtil;

import java.security.InvalidParameterException;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.Objects;

/**
 * SIKE encrypted message.
 *
 * @author Roman Strobl, roman.strobl@wultra.com
 */
public class EncryptedMessage {

    private final PublicKey c0;
    private final byte[] c1;

    /**
     * SIKE encrypted message constructor from public key and encrypted data.
     * @param c0 Alice's public key.
     * @param c1 Encrypted data.
     */
    public EncryptedMessage(PublicKey c0, byte[] c1) {
        this.c0 = c0;
        this.c1 = c1;
    }

    /**
     * SIKE encrypted message constructor from message encoded into byte array.
     * @param sikeParam SIKE parameters.
     * @param bytes Encrypted message encoded into byte array.
     */
    public EncryptedMessage(SikeParam sikeParam, byte[] bytes) {
        if (sikeParam == null) {
            throw new InvalidParameterException("Invalid parameter sikeParam");
        }
        int primeSize = (sikeParam.getPrime().bitLength() + 7) / 8;
        int pubKeySize = primeSize * 6;
        int messageSize = sikeParam.getMessageBytes();
        int expectedSize = pubKeySize + messageSize;
        if (bytes == null || bytes.length != expectedSize) {
            throw new InvalidParameterException("Invalid parameter bytes");
        }
        byte[] pubKeyBytes = new byte[pubKeySize];
        System.arraycopy(bytes, 0, pubKeyBytes, 0, pubKeySize);
        this.c0 = new SidhPublicKey(sikeParam, pubKeyBytes);
        this.c1 = new byte[messageSize];
        System.arraycopy(bytes, pubKeySize, this.c1, 0, messageSize);
    }

    /**
     * Get encrypted message encoded into byte array.
     * @return Encrypted message encoded into byte array.
     */
    public byte[] getEncoded() {
        if (c0 == null || c1 == null) {
            return null;
        }
        byte[] pubKey = c0.getEncoded();
        byte[] encoded = new byte[pubKey.length + c1.length];
        System.arraycopy(pubKey, 0, encoded, 0, pubKey.length);
        System.arraycopy(c1, 0, encoded, pubKey.length, c1.length);
        return encoded;
    }

    /**
     * Get Alice's public key.
     * @return Public key.
     */
    public PublicKey getC0() {
        return c0;
    }

    /**
     * Get encrypted data.
     * @return Encrypted data.
     */
    public byte[] getC1() {
        return c1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EncryptedMessage that = (EncryptedMessage) o;
        // Use constant time comparison to avoid timing attacks
        return SideChannelUtil.constantTimeAreEqual(getEncoded(), that.getEncoded());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(c0);
        result = 31 * result + Arrays.hashCode(c1);
        return result;
    }
}
