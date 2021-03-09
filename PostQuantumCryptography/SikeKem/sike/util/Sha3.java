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

import org.bouncycastle.crypto.digests.SHAKEDigest;

/**
 * SHA-3 hash function SHAKE256 with variable output length.
 *
 * @author Roman Strobl, roman.strobl@wultra.com
 */
public class Sha3 {

    private Sha3() {
        
    }

    /**
     * Hash data using SHAKE256.
     * @param data Data to hash.
     * @param outputLen Output length.
     * @return Hashed data.
     */
    public static byte[] shake256(byte[] data, int outputLen) {
        SHAKEDigest shake256 = new SHAKEDigest(256);
        shake256.update(data, 0, data.length);
        byte[] hashed = new byte[outputLen];
        // Squeeze output to required message length
        shake256.doFinal(hashed, 0, outputLen);
        return hashed;
    }
}
