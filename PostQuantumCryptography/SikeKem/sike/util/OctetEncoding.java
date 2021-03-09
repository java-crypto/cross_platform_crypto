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

import java.math.BigInteger;
import java.security.InvalidParameterException;

/**
 * Converter for octet encoding specified in SIKE specification.
 *
 * @author Roman Strobl, roman.strobl@wultra.com
 */
public class OctetEncoding {

    private OctetEncoding() {

    }

    /**
     * Convert a BigInteger into octet string.
     *
     * @param n      A non-negative number to convert.
     * @param length Length of generated octet string specified as number of octets.
     * @return Converted octet string.
     */
    public static String toOctetString(BigInteger n, int length) {
        if (n.signum() == -1) {
            throw new InvalidParameterException("Number is negative");
        }
        String hex = n.toString(16).toUpperCase();
        if (hex.length() % 2 == 1) {
            hex = "0" + hex;
        }
        char[] chars = hex.toCharArray();
        int expectedLength = length * 2;
        if (chars.length > expectedLength) {
            throw new InvalidParameterException("Number is too large, length: " + chars.length + ", expected: " + expectedLength);
        }
        StringBuilder sb = new StringBuilder();
        for (int i = hex.length() - 1; i >= 0; i -= 2) {
            sb.append(chars[i - 1]);
            sb.append(chars[i]);
        }
        for (int i = 0; i < expectedLength - chars.length; i++) {
            sb.append("0");
        }
        return sb.toString();
    }

    /**
     * Convert a byte array representing a number into an octet string.
     *
     * @param data   Byte array representing a number.
     * @param length Length of generated octet string specified as number of octets.
     * @return Converted octet string.
     */
    public static String toOctetString(byte[] data, int length) {
        return toOctetString(ByteEncoding.fromByteArray(data), length);
    }


    /**
     * Convert an octet string into BigInteger.
     *
     * @param str Octet string.
     * @return Converted BigInteger value.
     */
    public static BigInteger fromOctetString(String str) {
        if (str == null || str.length() % 2 == 1) {
            throw new InvalidParameterException("Invalid octet string");
        }
        char[] chars = str.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = chars.length - 1; i >= 0; i -= 2) {
            sb.append(chars[i - 1]);
            sb.append(chars[i]);
        }
        return new BigInteger(sb.toString(), 16);
    }

    /**
     * Convert an octet string to byte array.
     * @param str Octet string.
     * @param length Expected length of byte array.
     * @return Converted byte array value.
     */
    public static byte[] fromOctetString(String str, int length) {
        return ByteEncoding.toByteArray(fromOctetString(str), length);
    }
}