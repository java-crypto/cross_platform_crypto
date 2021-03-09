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

import org.bouncycastle.util.Arrays;

/**
 * Utilities for preventing side channel attacks.
 *
 * @author Roman Strobl, roman.strobl@wultra.com
 */
public class SideChannelUtil {

    private SideChannelUtil() {
        
    }

    /**
     * Compare two byte arrays in constant time.
     * @param bytes1 First byte array.
     * @param bytes2 Second byte array.
     * @return Whether byte arrays are equal.
     */
    public static boolean constantTimeAreEqual(byte[] bytes1, byte[] bytes2) {
        return Arrays.constantTimeAreEqual(bytes1, bytes2);
    }
}
