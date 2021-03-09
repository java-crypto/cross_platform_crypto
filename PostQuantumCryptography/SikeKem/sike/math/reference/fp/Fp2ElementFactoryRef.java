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
package sike.math.reference.fp;

import sike.math.api.Fp2Element;
import sike.math.api.Fp2ElementFactory;
import sike.param.SikeParam;

import java.math.BigInteger;

/**
 * Factory for reference elements of quadratic extension field F(p^2).
 *
 * @author Roman Strobl, roman.strobl@wultra.com
 */
public class Fp2ElementFactoryRef implements Fp2ElementFactory {

    private final SikeParam sikeParam;

    /**
     * Fp2Element factory constructor for reference elements.
     * @param sikeParam SIKE parameters.
     */
    public Fp2ElementFactoryRef(SikeParam sikeParam) {
        this.sikeParam = sikeParam;
    }

    @Override
    public Fp2Element zero() {
        return new Fp2ElementRef(sikeParam, BigInteger.ZERO, BigInteger.ZERO);
    }

    @Override
    public Fp2Element one() {
        return new Fp2ElementRef(sikeParam, BigInteger.ONE, BigInteger.ZERO);
    }

    @Override
    public Fp2Element generate(BigInteger x0r) {
        return new Fp2ElementRef(sikeParam, x0r, BigInteger.ZERO);
    }

    @Override
    public Fp2Element generate(BigInteger x0r, BigInteger x0i) {
        return new Fp2ElementRef(sikeParam, x0r, x0i);
    }
}
