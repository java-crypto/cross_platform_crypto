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

/**
 * Mathematical functions for the 64-bit unsigned integer type.
 * All methods are constant time to prevent side channel attacks.
 *
 * @author Roman Strobl, roman.strobl@wultra.com
 */
public class UnsignedLong {

    private UnsignedLong() {

    }

    /**
     * Add two unsigned long values with carry.
     * @param x First unsigned long value.
     * @param y Second unsigned long value.
     * @param carry Carry set to 1 in case of overflow, otherwise 0.
     * @return Unsigned long addition result.
     */
    public static long[] add(long x, long y, long carry) {
        long sum = x + y + carry;
        long carryOut = (((x & y) | ((x | y) & ~sum)) >>> 63);
        return new long[]{sum, carryOut};
    }

    /**
     * Subtract two unsigned long values with borrow.
     * @param x First unsigned long value.
     * @param y Second unsigned long value.
     * @param borrow Borrow set to 1 in case of underflow, otherwise 0.
     * @return Unsigned long subtraction result.
     */
    public static long[] sub(long x, long y, long borrow) {
        long sub = x - y;
        long borrowOut = (borrow & (1 ^ ((sub | -sub) >>> 63))) | (x ^ ((x ^ y) | ((x - y) ^ y))) >>> 63;
        long diff = sub - borrow;
        return new long[]{diff, borrowOut};
    }

    /**
     * Multiply two unsigned long values.
     * @param x First unsigned long value.
     * @param y Second unsigned long value.
     * @return Result of multiplication of two unsigned longs, represented by their hi and lo values, each 64-bit.
     */
    public static long[] mul(long x, long y) {
        long al, bl, ah, bh, albl, albh, ahbl, ahbh;
        long res1, res2, res3;
        long carry, temp;
        long maskL = 0L, maskH;
        long lo, hi;

        maskL = (~maskL) >>> 32;
        maskH = ~maskL;

        al = x & maskL;
        ah = x >>> 32;
        bl = y & maskL;
        bh = y >>> 32;

        albl = al * bl;
        albh = al * bh;
        ahbl = ah * bl;
        ahbh = ah * bh;
        lo = albl & maskL;

        res1 = albl >>> 32;
        res2 = ahbl & maskL;
        res3 = albh & maskL;
        temp = res1 + res2 + res3;
        carry = temp >>> 32;
        lo ^= temp << 32;

        res1 = ahbl >>> 32;
        res2 = albh >>> 32;
        res3 = ahbh & maskL;
        temp = res1 + res2 + res3 + carry;
        hi = temp & maskL;
        carry = temp & maskH;
        hi ^= (ahbh & maskH) + carry;
        return new long[]{hi, lo};
    }

}
