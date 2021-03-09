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
package sike.crypto;

import sike.math.api.Fp2Element;
import sike.model.Party;
import sike.model.SidhPrivateKey;
import sike.model.SidhPublicKey;
import sike.param.SikeParam;

import java.security.*;

/**
 * SIDH key exchange.
 *
 * @author Roman Strobl, roman.strobl@wultra.com
 */
public class Sidh {

    private final SikeParam sikeParam;

    /**
     * SIDH key exchange constructor.
     * @param sikeParam SIKE parameters.
     */
    public Sidh(SikeParam sikeParam) {
        this.sikeParam = sikeParam;
    }

    /**
     * Generate a shared secret isogeny j-invariant.
     * @param party Alice or Bob.
     * @param privateKey Private key.
     * @param publicKey Public key.
     * @return Shared secret isogeny j-invariant.
     * @throws GeneralSecurityException Thrown in case cryptography fails.
     */
    public Fp2Element generateSharedSecret(Party party, PrivateKey privateKey, PublicKey publicKey) throws GeneralSecurityException {
        if (!(privateKey instanceof SidhPrivateKey)) {
            throw new InvalidKeyException("Invalid private key");
        }
        if (!(publicKey instanceof SidhPublicKey)) {
            throw new InvalidKeyException("Invalid public key");
        }
        SidhPrivateKey priv = (SidhPrivateKey) privateKey;
        SidhPublicKey pub = (SidhPublicKey) publicKey;
        if (party == Party.ALICE) {
            return sikeParam.getIsogeny().isoEx2(sikeParam, priv.getKey(), pub.getPx(), pub.getQx(), pub.getRx());
        }
        if (party == Party.BOB) {
            return sikeParam.getIsogeny().isoEx3(sikeParam, priv.getKey(), pub.getPx(), pub.getQx(), pub.getRx());
        }
        throw new InvalidParameterException("Invalid party");
    }
}
