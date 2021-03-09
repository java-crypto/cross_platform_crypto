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
package sike.math.optimized.fp;

import sike.math.api.FpElement;
import sike.param.SikeParam;
import sike.util.ByteEncoding;
import sike.util.OctetEncoding;
import sike.util.SideChannelUtil;

import java.math.BigInteger;
import java.util.Arrays;

/**
 * Representation of an optimized element of the base field F(p).
 *
 * @author Roman Strobl, roman.strobl@wultra.com
 */
public class FpElementOpti implements FpElement {

    private final SikeParam sikeParam;
    private final long[] value;

    /**
     * FpElement constructor.
     * @param sikeParam SIKE parameters.
     */
    public FpElementOpti(SikeParam sikeParam) {
        long[] value = new long[sikeParam.getFpWords()];
        this.sikeParam = sikeParam;
        this.value = value;
    }

    /**
     * FpElement constructor with provided long array value.
     * @param sikeParam SIKE parameters.
     * @param value Element value.
     */
    public FpElementOpti(SikeParam sikeParam, long[] value) {
        this.sikeParam = sikeParam;
        this.value = value;
    }

    /**
     * FpElement constructor with provided BigInteger value.
     * @param sikeParam SIKE parameters.
     * @param x BigInteger value.
     */
    public FpElementOpti(SikeParam sikeParam, BigInteger x) {
        this.sikeParam = sikeParam;
        // Convert element to Montgomery domain
        long[] value = new long[sikeParam.getFpWords()];
        int primeSize = (sikeParam.getPrime().bitLength() + 7) / 8;
        byte[] encoded = ByteEncoding.toByteArray(x, primeSize);
        for (int i = 0; i < primeSize; i++) {
            int j = i / 8;
            int k = i % 8;
            value[j] |= ((encoded[i] & 0xFFL) << (8 * k));
        }
        FpElementOpti a = new FpElementOpti(sikeParam, value);
        FpElementOpti b = (FpElementOpti) a.multiply(sikeParam.getPR2());
        FpElementOpti reduced = b.reduceMontgomery();
        this.value = new long[sikeParam.getFpWords()];
        System.arraycopy(reduced.getValue(), 0, this.value, 0, sikeParam.getFpWords());
    }

    /**
     * Get element value as long array.
     * @return Element value as long array.
     */
    public long[] getValue() {
        return value;
    }

    /**
     * Get element long array size.
     * @return Elemennt long array size.
     */
    public int size() {
        return value.length;
    }

    @Override
    public BigInteger getX() {
        return ByteEncoding.fromByteArray(getEncoded());
    }

    @Override
    public FpElement add(FpElement o) {
        // Compute z = x + y (mod 2*p)
        FpElementOpti z = new FpElementOpti(sikeParam);
        long carry = 0L;

        // z = x + y % p
        for (int i = 0; i < sikeParam.getFpWords(); i++) {
            long[] result = UnsignedLong.add(value[i], ((FpElementOpti)o).getValue()[i], carry);
            z.getValue()[i] = result[0];
            carry = result[1];
        }

        // z = z - p * 2
        carry = 0L;
        for (int i = 0; i < sikeParam.getFpWords(); i++) {
            long[] result = UnsignedLong.sub(z.getValue()[i], sikeParam.getPx2().getValue()[i], carry);
            z.getValue()[i] = result[0];
            carry = result[1];
        }

        // if z < 0, add p * 2 back
        long mask = -carry;
        carry = 0L;
        for (int i = 0; i < sikeParam.getFpWords(); i++) {
            long[] result = UnsignedLong.add(z.getValue()[i], sikeParam.getPx2().getValue()[i] & mask, carry);
            z.getValue()[i] = result[0];
            carry = result[1];
        }
        return z;
    }

    /**
     * Add two elements without reduction.
     * @param o Other element.
     * @return Calculation result.
     */
    public FpElement addNoReduction(FpElement o) {
        // Compute z = x + y, without reducing mod p.
        FpElementOpti z = new FpElementOpti(sikeParam, new long[sikeParam.getFpWords() * 2]);
        long carry = 0L;
        for (int i = 0; i < 2 * sikeParam.getFpWords(); i++) {
            long[] addResult = UnsignedLong.add(value[i], ((FpElementOpti) o).getValue()[i], carry);
            z.getValue()[i] = addResult[0];
            carry = addResult[1];
        }
        return z;
    }

    @Override
    public FpElement subtract(FpElement o) {
        // Compute z = x - y (mod 2*p)
        FpElementOpti z = new FpElementOpti(sikeParam);
        long borrow = 0L;

        // z = z - p * 2
        for (int i = 0; i < sikeParam.getFpWords(); i++) {
            long[] result = UnsignedLong.sub(value[i], ((FpElementOpti) o).getValue()[i], borrow);
            z.getValue()[i] = result[0];
            borrow = result[1];
        }

        // if z < 0, add p * 2 back
        long mask = -borrow;
        borrow = 0L;
        for (int i = 0; i < sikeParam.getFpWords(); i++) {
            long[] result = UnsignedLong.add(z.getValue()[i], sikeParam.getPx2().getValue()[i] & mask, borrow);
            z.getValue()[i] = result[0];
            borrow = result[1];
        }
        return z;
    }

    /**
     * Subtract two elements without reduction.
     * @param o Other element.
     * @return Calculation result.
     */
    public FpElementOpti subtractNoReduction(FpElement o) {
        // Compute z = x - y, without reducing mod p
        FpElementOpti z = new FpElementOpti(sikeParam, new long[sikeParam.getFpWords() * 2]);
        long borrow = 0L;

        for (int i = 0; i < sikeParam.getFpWords() * 2; i++) {
            long[] result = UnsignedLong.sub(value[i], ((FpElementOpti) o).getValue()[i], borrow);
            z.getValue()[i] = result[0];
            borrow = result[1];
        }
        long mask = -borrow;
        borrow = 0L;
        for (int i = sikeParam.getFpWords(); i < sikeParam.getFpWords() * 2; i++) {
            long[] result = UnsignedLong.add(z.getValue()[i], sikeParam.getP().getValue()[i - sikeParam.getFpWords()] & mask, borrow);
            z.getValue()[i] = result[0];
            borrow = result[1];
        }
        return z;
    }


    @Override
    public FpElement multiply(FpElement o) {
        // Compute z = x * y
        FpElementOpti z = new FpElementOpti(sikeParam, new long[sikeParam.getFpWords() * 2]);
        long carry;
        long t = 0L;
        long u = 0L;
        long v = 0L;

        for (int i = 0; i < sikeParam.getFpWords(); i++) {
            for (int j = 0; j <= i; j++) {
                long[] mulResult = UnsignedLong.mul(value[j], ((FpElementOpti) o).getValue()[i - j]);
                long[] addResult1 = UnsignedLong.add(mulResult[1], v, 0L);
                v = addResult1[0];
                carry = addResult1[1];
                long[] addResult2 = UnsignedLong.add(mulResult[0], u, carry);
                u = addResult2[0];
                carry = addResult2[1];
                t = t + carry;
            }
            z.getValue()[i] = v;
            v = u;
            u = t;
            t = 0L;
        }

        for (int i = sikeParam.getFpWords(); i < (2 * sikeParam.getFpWords()) - 1; i++) {
            for (int j = i - sikeParam.getFpWords() + 1; j < sikeParam.getFpWords(); j++) {
                long[] mulResult = UnsignedLong.mul(value[j], ((FpElementOpti) o).getValue()[i - j]);
                long[] addResult1 = UnsignedLong.add(mulResult[1], v, 0L);
                v = addResult1[0];
                carry = addResult1[1];
                long[] addResult2 = UnsignedLong.add(mulResult[0], u, carry);
                u = addResult2[0];
                carry = addResult2[1];
                t = t + carry;
            }
            z.getValue()[i] = v;
            v = u;
            u = t;
            t = 0L;
        }
        z.getValue()[2 * sikeParam.getFpWords() - 1] = v;
        return z;
    }

    /**
     * Montgomery multiplication. Input values must be already in Montgomery domain.
     * @param o Other value.
     * @return Multiplication result.
     */
    public FpElement multiplyMontgomery(FpElement o) {
        FpElementOpti ab = (FpElementOpti) multiply(o);
        return ab.reduceMontgomery();
    }

    @Override
    public FpElement square() {
        return multiply(this);
    }

    @Override
    public FpElement inverse() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public FpElement negate() {
        return sikeParam.getP().subtract(this);
    }

    @Override
    public boolean isZero() {
        FpElement zero = new FpElementOpti(sikeParam, new long[value.length]);
        return equals(zero);
    }

    /**
     * Reduce a field element in [0, 2*p) to one in [0,p).
     */
    public void reduce() {
        long borrow = 0L;
        for (int i = 0; i < sikeParam.getFpWords(); i++) {
            long[] result = UnsignedLong.sub(value[i], sikeParam.getP().getValue()[i], borrow);
            value[i] = result[0];
            borrow = result[1];
        }

        // Sets all bits if borrow = 1
        long mask = -borrow;
        borrow = 0L;
        for (int i = 0; i < sikeParam.getFpWords(); i++) {
            long[] result = UnsignedLong.add(value[i], sikeParam.getP().getValue()[i] & mask, borrow);
            value[i] = result[0];
            borrow = result[1];
        }
    }

    /**
     * Perform Montgomery reduction.
     * @return Reduced value.
     */
    public FpElementOpti reduceMontgomery() {
        FpElementOpti z = new FpElementOpti(sikeParam, new long[sikeParam.getFpWords()]);
        long carry;
        long t = 0L;
        long u = 0L;
        long v = 0L;
        int count = sikeParam.getZeroWords(); // number of 0 digits in the least significant part of p + 1

        for (int i = 0; i < sikeParam.getFpWords(); i++) {
            for (int j = 0; j < i; j++) {
                if (j < i - count + 1) {
                    long[] mulResult = UnsignedLong.mul(z.getValue()[j], sikeParam.getP1().getValue()[i - j]);
                    long[] addResult1 = UnsignedLong.add(mulResult[1], v, 0L);
                    v = addResult1[0];
                    carry = addResult1[1];
                    long[] addResult2 = UnsignedLong.add(mulResult[0], u, carry);
                    u = addResult2[0];
                    carry = addResult2[1];
                    t = t + carry;
                }
            }
            long[] addResult1 = UnsignedLong.add(v, value[i], 0L);
            v = addResult1[0];
            carry = addResult1[1];
            long[] addResult2 = UnsignedLong.add(u, 0L, carry);
            u = addResult2[0];
            carry = addResult2[1];
            t = t + carry;
            z.getValue()[i] = v;
            v = u;
            u = t;
            t = 0L;
        }

        for (int i = sikeParam.getFpWords(); i < (2 * sikeParam.getFpWords()) - 1; i++) {
            if (count > 0) {
                count--;
            }
            for (int j = i - sikeParam.getFpWords() + 1; j < sikeParam.getFpWords(); j++) {
                if (j < (sikeParam.getFpWords() - count)) {
                    long[] mulResult = UnsignedLong.mul(z.getValue()[j], sikeParam.getP1().getValue()[i - j]);
                    long[] addResult1 = UnsignedLong.add(mulResult[1], v, 0L);
                    v = addResult1[0];
                    carry = addResult1[1];
                    long[] addResult2 = UnsignedLong.add(mulResult[0], u, carry);
                    u = addResult2[0];
                    carry = addResult2[1];
                    t = t + carry;
                }
            }
            long[] addResult1 = UnsignedLong.add(v, value[i], 0L);
            v = addResult1[0];
            carry = addResult1[1];
            long[] addResult2 = UnsignedLong.add(u, 0L, carry);
            u = addResult2[0];
            carry = addResult2[1];
            t = t + carry;
            z.getValue()[i - sikeParam.getFpWords()] = v;
            v = u;
            u = t;
            t = 0L;
        }
        long[] addResult = UnsignedLong.add(v, value[2 * sikeParam.getFpWords() - 1], 0L);
        z.getValue()[sikeParam.getFpWords() - 1] = addResult[0];
        return z;
    }

    /**
     * Swap field elements conditionally, in constant time.
     * @param sikeParam SIKE parameters.
     * @param x First element.
     * @param y Second element.
     * @param mask Swap condition, if zero swap is not performed.
     */
    public static void conditionalSwap(SikeParam sikeParam, FpElementOpti x, FpElementOpti y, long mask) {
        long maskNeg = -mask;
        for (int i = 0; i < sikeParam.getFpWords(); i++) {
            long tmp = maskNeg & (x.getValue()[i] ^ y.getValue()[i]);
            x.getValue()[i] = tmp ^ x.getValue()[i];
            y.getValue()[i] = tmp ^ y.getValue()[i];
        }
    }

    /**
     * Create copy of the element.
     * @return Element copy.
     */
    public FpElementOpti copy() {
        return new FpElementOpti(sikeParam, value.clone());
    }

    @Override
    public byte[] getEncoded() {
        int primeSize = (sikeParam.getPrime().bitLength() + 7) / 8;
        byte[] bytes = new byte[primeSize];
        // Convert element from Montgomery domain
        long[] val = new long[sikeParam.getFpWords() * 2];
        System.arraycopy(value, 0, val, 0, sikeParam.getFpWords());
        FpElementOpti el = new FpElementOpti(sikeParam, val);
        FpElementOpti a = el.reduceMontgomery();
        reduce();
        for (int i = 0; i < primeSize; i++) {
            int j = i / 8;
            int k = i % 8;
            bytes[i] = (byte) (a.getValue()[j] >>> (8 * k));
        }
        return bytes;
    }

    @Override
    public String toOctetString() {
        int primeSize = (sikeParam.getPrime().bitLength() + 7) / 8;
        BigInteger x = ByteEncoding.fromByteArray(getEncoded());
        return OctetEncoding.toOctetString(x, primeSize);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof FpElementOpti)) {
            return false;
        }
        FpElementOpti that = (FpElementOpti) o;
        // Use constant time comparison to avoid timing attacks
        return SideChannelUtil.constantTimeAreEqual(getEncoded(), that.getEncoded());
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(value);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < value.length; i++) {
            sb.append(Long.toUnsignedString(value[i]));
            if (i < value.length - 1) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }
}
