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

/**
 * Point in F(p^2) with unspecified coordinate system.
 *
 * @author Roman Strobl, roman.strobl@wultra.com
 */
public interface Fp2Point {

    /**
     * Get the x coordinate.
     * @return The x coordinate.
     */
    Fp2Element getX();

    /**
     * Get the y coordinate.
     * @return The y coordinate.
     */
    Fp2Element getY();

    /**
     * Get the z coordinate.
     * @return The z coordinate.
     */
    Fp2Element getZ();

    /**
     * Add two points.
     * @param o Other point.
     * @return Result of point addition.
     */
    Fp2Point add(Fp2Point o);

    /**
     * Subtract two points.
     * @param o Other point.
     * @return Result of point subtraction.
     */
    Fp2Point subtract(Fp2Point o);

    /**
     * Multiply two points.
     * @param o Other point.
     * @return Result of point multiplication.
     */
    Fp2Point multiply(Fp2Point o);

    /**
     * Square a point.
     * @return Result of a square operation.
     */
    Fp2Point square();

    /**
     * Invert a point.
     * @return Result of an inversion operation.
     */
    Fp2Point inverse();

    /**
     * Negate a point.
     * @return Result of a negation operation.
     */
    Fp2Point negate();

    /**
     * Get whether this is a point at infinity.
     * @return Whether this is a point at infinity.
     */
    boolean isInfinite();

    /**
     * Copy the point.
     * @return Point copy.
     */
    Fp2Point copy();

}
