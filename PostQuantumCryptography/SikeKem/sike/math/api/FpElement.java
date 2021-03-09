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
 * Element of an F(p) field with a single coordinate x.
 *
 * @author Roman Strobl, roman.strobl@wultra.com
 */
public interface FpElement {

    /**
     * Get the element value.
     * @return the element value.
     */
    BigInteger getX();

    /**
     * Add two elements.
     * @param o Other element.
     * @return Calculation result.
     */
    FpElement add(FpElement o);

    /**
     * Subtract two elements.
     * @param o Other element.
     * @return Calculation result.
     */
    FpElement subtract(FpElement o);

    /**
     * Multiply two elements.
     * @param o Other element.
     * @return Calculation result.
     */
    FpElement multiply(FpElement o);

    /**
     * Square the elements.
     * @return Calculation result.
     */
    FpElement square();
    /**
     * Invert the element.
     * @return Calculation result.
     */
    FpElement inverse();

    /**
     * Negate the element.
     * @return Calculation result.
     */
    FpElement negate();

    /**
     * Get whether the element is the zero element.
     * @return Whether the element is the zero element.
     */
    boolean isZero();

    /**
     * Copy the element.
     * @return Element copy.
     */
    FpElement copy();
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
