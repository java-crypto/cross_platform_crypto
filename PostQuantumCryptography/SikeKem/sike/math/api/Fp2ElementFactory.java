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
 * Factory for elements of quadratic extension field F(p^2).
 *
 * @author Roman Strobl, roman.strobl@wultra.com
 */
public interface Fp2ElementFactory {

    /**
     * Construct the zero element 0 + 0*i.
     * @return Zero element.
     */
    Fp2Element zero();

    /**
     * Construct the one element 1 + 0*i.
     * @return One element.
     */
    Fp2Element one();

    /**
     * Generate an element with value x0r + 0*i.
     * @param x0r Integer value for the real part of element.
     * @return Generated element.
     */
    Fp2Element generate(BigInteger x0r);

    /**
     * Generate an element with value x0r + x0i*i.
     * @param x0r Integer value for the real part of element.
     * @param x0i Integer value for the imaginary part of element.
     * @return Generated element.
     */
    Fp2Element generate(BigInteger x0r, BigInteger x0i);
}
