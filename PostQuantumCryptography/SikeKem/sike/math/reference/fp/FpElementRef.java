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
package sike.math.reference.fp;

import sike.math.api.FpElement;
import sike.param.SikeParam;
import sike.util.ByteEncoding;
import sike.util.OctetEncoding;

import java.math.BigInteger;
import java.util.Objects;

/**
 * Element of an F(p) field with a single coordinate x.
 *
 * @author Roman Strobl, roman.strobl@wultra.com
 */
public class FpElementRef implements FpElement {

    private final BigInteger x;

    private final SikeParam sikeParam;

    /**
     * The F(p^) field element constructor for given BigInteger value.
     * @param sikeParam Field prime.
     * @param x BigInteger value.
     */
    public FpElementRef(SikeParam sikeParam, BigInteger x) {
        this.sikeParam = sikeParam;
        this.x = x.mod(sikeParam.getPrime());
    }

    /**
     * Get the element value.
     * @return the element value.
     */
    public BigInteger getX() {
        return x;
    }

    /**
     * Get the field prime.
     * @return Field prime.
     */
    public BigInteger getPrime() {
        return sikeParam.getPrime();
    }

    /**
     * Add two elements.
     * @param o Other element.
     * @return Calculation result.
     */
    public FpElement add(FpElement o) {
        return new FpElementRef(sikeParam, x.add(o.getX()).mod(getPrime()));
    }

    /**
     * Subtract two elements.
     * @param o Other element.
     * @return Calculation result.
     */
    public FpElement subtract(FpElement o) {
        return new FpElementRef(sikeParam, x.subtract(o.getX()).mod(getPrime()));
    }

    /**
     * Multiply two elements.
     * @param o Other element.
     * @return Calculation result.
     */
    public FpElement multiply(FpElement o) {
        return new FpElementRef(sikeParam, x.multiply(o.getX()).mod(getPrime()));
    }

    /**
     * Square the elements.
     * @return Calculation result.
     */
    public FpElement square() {
        return multiply(this);
    }

    /**
     * Invert the element.
     * @return Calculation result.
     */
    public FpElement inverse() {
        return new FpElementRef(sikeParam, x.modInverse(getPrime()));
    }

    /**
     * Negate the element.
     * @return Calculation result.
     */
    public FpElement negate() {
        return new FpElementRef(sikeParam, getPrime().subtract(x));
    }

    /**
     * Get whether the element is the zero element.
     * @return Whether the element is the zero element.
     */
    public boolean isZero() {
        return BigInteger.ZERO.equals(x);
    }

    /**
     * Copy the element.
     * @return Element copy.
     */
    public FpElement copy() {
        return new FpElementRef(sikeParam, x);
    }

    /**
     * Encode the element in bytes.
     * @return Encoded element in bytes.
     */
    public byte[] getEncoded() {
        int primeSize = (sikeParam.getPrime().bitLength() + 7) / 8;
        return ByteEncoding.toByteArray(x, primeSize);
    }

    /**
     * Convert element to octet string.
     * @return Octet string.
     */
    public String toOctetString() {
        int primeSize = (sikeParam.getPrime().bitLength() + 7) / 8;
        return OctetEncoding.toOctetString(x, primeSize);
    }

    @Override
    public String toString() {
        return x.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FpElementRef fpElement = (FpElementRef) o;
        return getPrime().equals(fpElement.getPrime())
                && x.equals(fpElement.x);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPrime(), x);
    }
}
