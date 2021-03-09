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
package sike.model;

import sike.util.SideChannelUtil;

import java.util.Arrays;
import java.util.Objects;

/**
 * SIKE encapsulation result.
 *
 * @author Roman Strobl, roman.strobl@wultra.com
 */
public class EncapsulationResult {

    private final byte[] secret;
    private final EncryptedMessage encryptedMessage;

    /**
     * SIKE encapsulation result constructor.
     * @param secret Shared secret.
     * @param encryptedMessage Encrypted message to be sent to Bob.
     */
    public EncapsulationResult(byte[] secret, EncryptedMessage encryptedMessage) {
        this.secret = secret;
        this.encryptedMessage = encryptedMessage;
    }

    /**
     * Get the shared secret.
     * @return Shared secret.
     */
    public byte[] getSecret() {
        return secret;
    }

    /**
     * Get the encrypted message.
     * @return Encypted message.
     */
    public EncryptedMessage getEncryptedMessage() {
        return encryptedMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EncapsulationResult that = (EncapsulationResult) o;
        // Use constant time comparison to avoid timing attacks
        return SideChannelUtil.constantTimeAreEqual(secret, that.secret) &
                encryptedMessage.equals(that.encryptedMessage);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(encryptedMessage);
        result = 31 * result + Arrays.hashCode(secret);
        return result;
    }
}
