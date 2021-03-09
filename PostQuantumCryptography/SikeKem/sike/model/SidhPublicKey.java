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

import org.bouncycastle.util.Arrays;
import sike.math.api.Fp2Element;
import sike.param.SikeParam;
import sike.util.ByteEncoding;
import sike.util.OctetEncoding;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.util.Objects;

/**
 * SIDH or SIKE public key.
 *
 * @author Roman Strobl, roman.strobl@wultra.com
 */
public class SidhPublicKey implements PublicKey {

    private final SikeParam sikeParam;

    private final Fp2Element px;
    private final Fp2Element qx;
    private final Fp2Element rx;

    /**
     * Public key constructor from F(p^2) Elements.
     * @param sikeParam SIKE parameters.
     * @param px The x coordinate of public point P.
     * @param qx The x coordinate of public point Q.
     * @param rx The x coordinate of public point R.
     */
    public SidhPublicKey(SikeParam sikeParam, Fp2Element px, Fp2Element qx, Fp2Element rx) {
        this.sikeParam = sikeParam;
        this.px = px;
        this.qx = qx;
        this.rx = rx;
    }

    /**
     * Public key constructor from byte array representation.
     * @param sikeParam SIKE parameters.
     * @param bytes The x coordinates of public points P, Q and R.
     */
    public SidhPublicKey(SikeParam sikeParam, byte[] bytes) {
        this.sikeParam = sikeParam;
        BigInteger prime = sikeParam.getPrime();
        int primeSize = (prime.bitLength() + 7) / 8;
        if (bytes == null || bytes.length != 6 * primeSize) {
            throw new IllegalStateException("Invalid public key");
        }
        BigInteger[] keyParts = new BigInteger[6];
        for (int i = 0; i < 6; i++) {
            byte[] keyBytes = new byte[primeSize];
            System.arraycopy(bytes, i * primeSize, keyBytes, 0, keyBytes.length);
            keyParts[i] = ByteEncoding.fromByteArray(keyBytes);
        }
        this.px = sikeParam.getFp2ElementFactory().generate(keyParts[0], keyParts[1]);
        this.qx = sikeParam.getFp2ElementFactory().generate(keyParts[2], keyParts[3]);
        this.rx = sikeParam.getFp2ElementFactory().generate(keyParts[4], keyParts[5]);
    }

    /**
     * Construct public key from octets.
     * @param sikeParam SIKE parameters.
     * @param octets Octet value of the private key.
     */
    public SidhPublicKey(SikeParam sikeParam, String octets) {
        this.sikeParam = sikeParam;
        BigInteger prime = sikeParam.getPrime();
        int primeSize = (prime.bitLength() + 7) / 8;
        if (octets == null || octets.length() != 12 * primeSize) {
            throw new IllegalStateException("Invalid public key");
        }
        byte[] octetBytes = octets.getBytes(StandardCharsets.UTF_8);
        BigInteger[] keyParts = new BigInteger[6];
        for (int i = 0; i < 6; i++) {
            byte[] keyBytes = new byte[primeSize * 2];
            System.arraycopy(octetBytes, i * primeSize * 2, keyBytes, 0, keyBytes.length);
            keyParts[i] = OctetEncoding.fromOctetString(new String(keyBytes));
        }
        this.px = sikeParam.getFp2ElementFactory().generate(keyParts[0], keyParts[1]);
        this.qx = sikeParam.getFp2ElementFactory().generate(keyParts[2], keyParts[3]);
        this.rx = sikeParam.getFp2ElementFactory().generate(keyParts[4], keyParts[5]);
    }

    /**
     * Get the x coordinate of public point P.
     * @return The x coordinate of public point P.
     */
    public Fp2Element getPx() {
        return px;
    }

    /**
     * The x coordinate of public point Q.
     * @return The x coordinate of public point Q.
     */
    public Fp2Element getQx() {
        return qx;
    }

    /**
     * The x coordinate of public point R.
     * @return The x coordinate of public point R.
     */
    public Fp2Element getRx() {
        return rx;
    }

    @Override
    public String getAlgorithm() {
        return sikeParam.getName();
    }

    @Override
    public String getFormat() {
        // ASN.1 encoding is not supported
        return null;
    }

    /**
     * Get the public key encoded as bytes.
     * @return Public key encoded as bytes.
     */
    @Override
    public byte[] getEncoded() {
        byte[] pxEncoded = px.getEncoded();
        byte[] qxEncoded = qx.getEncoded();
        byte[] rxEncoded = rx.getEncoded();
        byte[] encoded = new byte[pxEncoded.length + qxEncoded.length + rxEncoded.length];
        System.arraycopy(pxEncoded, 0, encoded, 0, pxEncoded.length);
        System.arraycopy(qxEncoded, 0, encoded, pxEncoded.length, qxEncoded.length);
        System.arraycopy(rxEncoded, 0, encoded, pxEncoded.length + qxEncoded.length, rxEncoded.length);
        return encoded;
    }

    /**
     * Convert public key to octet string.
     * @return Octet string.
     */
    public String toOctetString() {
        return px.toOctetString() + qx.toOctetString() + rx.toOctetString();
    }

    @Override
    public String toString() {
        return "(" + px.toString() + ", " + qx.toString() + ", " + rx.toString() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SidhPublicKey that = (SidhPublicKey) o;
        // Use constant time comparison to avoid timing attacks
        return sikeParam.equals(that.sikeParam)
                && Arrays.constantTimeAreEqual(getEncoded(), that.getEncoded());
    }

    @Override
    public int hashCode() {
        return Objects.hash(sikeParam, px, qx, rx);
    }
}
