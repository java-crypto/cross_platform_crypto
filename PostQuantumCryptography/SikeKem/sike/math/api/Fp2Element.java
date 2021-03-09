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
package sike.math.api;

import java.math.BigInteger;

/**
 * Element of a quadratic extension field F(p^2): x0 + x1*i.
 *
 * @author Roman Strobl, roman.strobl@wultra.com
 */
public interface Fp2Element {

    /**
     * Get the real part of element.
     * @return Real part of element.
     */
    FpElement getX0();

    /**
     * Get the imaginary part of element.
     * @return Imaginary part of element.
     */
    FpElement getX1();

    /**
     * Add two elements.
     * @param y Other element.
     * @return Calculation result.
     */
    Fp2Element add(Fp2Element y);

    /**
     * Subtract two elements.
     * @param y Other element.
     * @return Calculation result.
     */
    Fp2Element subtract(Fp2Element y);

    /**
     * Multiply two elements.
     * @param y Other element.
     * @return Calculation result.
     */
    Fp2Element multiply(Fp2Element y);

    /**
     * Multiply by the imaginary part of the element.
     * @return Calculation result.
     */
    Fp2Element multiplyByI();

    /**
     * Square the element.
     * @return Calculation result.
     */
    Fp2Element square();

    /**
     * Element exponentiation.
     * @param n Exponent
     * @return Calculation result.
     */
    Fp2Element pow(BigInteger n);

    /**
     * Calculate the square root of the element.
     * @return Calculation result.
     */
    Fp2Element sqrt();

    /**
     * Invert the element.
     * @return Calculation result.
     */
    Fp2Element inverse();

    /**
     * Negate the element.
     * @return Calculation result.
     */
    Fp2Element negate();

    /**
     * Get whether the element is the zero element.
     * @return Whether the element is the zero element.
     */
    boolean isZero();

    /**
     * Copy the element.
     * @return Element copy.
     */
    Fp2Element copy();

    /**
     * Encode the element in bytes.
     * @return Encoded element in bytes.
     */
    byte[] getEncoded();

    /**
     * Convert element to octet string.
     * @return Octet string.
     */
    String toOctetString();

}
