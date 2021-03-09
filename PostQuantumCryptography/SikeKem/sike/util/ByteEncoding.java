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
package sike.util;

import org.bouncycastle.util.BigIntegers;

import java.math.BigInteger;
import java.security.InvalidParameterException;

/**
 * Converter for byte encoding for compatibility with the GMP library.
 *
 * @author Roman Strobl, roman.strobl@wultra.com
 */
public class ByteEncoding {

    private ByteEncoding() {

    }

    /**
     * Convert unsigned number from byte array. The byte array is stored in big-endian ordering.
     *
     * @param data Byte array representing the number.
     * @return Converted number.
     */
    public static BigInteger fromByteArray(byte[] data) {
        return BigIntegers.fromUnsignedByteArray(reverse(data));
    }

    /**
     * Convert unsigned number into a byte array. The byte array is stored in big-endian ordering.
     * @param n Number to convert.
     * @param length Length of byte array.
     * @return Byte array representing converted number.
     */
    public static byte[] toByteArray(BigInteger n, int length) {
        byte[] encoded = reverse(BigIntegers.asUnsignedByteArray(n));
        if (encoded.length > length) {
            throw new InvalidParameterException("Number is too large");
        }
        if (encoded.length == length) {
            return encoded;
        }
        byte[] padded = new byte[length];
        System.arraycopy(encoded, 0, padded, 0, encoded.length);
        return padded;
    }

    /**
     * Reverse byte array.
     * @param data Source byte array.
     * @return Reversed byte array.
     */
    private static byte[] reverse(byte[] data) {
        byte[] out = new byte[data.length];
        for(int i = 0; i < data.length; i++) {
            out[i] = data[data.length - i - 1];
        }
        return out;
    }

}
