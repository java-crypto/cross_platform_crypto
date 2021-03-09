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

import sike.math.api.Fp2Element;
import sike.model.optimized.MontgomeryConstants;
import sike.param.SikeParam;

import java.security.InvalidParameterException;
import java.util.Objects;

/**
 * Montgomery curve parameters.
 *
 * @author Roman Strobl, roman.strobl@wultra.com
 */
public class MontgomeryCurve {

    private final SikeParam sikeParam;
    private Fp2Element a;
    private Fp2Element b;
    private MontgomeryConstants optimizedConstants;

    /**
     * Montgomery curve constructor.
     * @param sikeParam SIKE parameters.
     */
    public MontgomeryCurve(SikeParam sikeParam) {
        this.sikeParam = sikeParam;
        if (sikeParam.getImplementationType() == ImplementationType.OPTIMIZED) {
            optimizedConstants = new MontgomeryConstants(sikeParam);
        }
    }

    /**
     * Montgomery curve constructor.
     * @param sikeParam SIKE parameters.
     * @param a Montgomery curve coefficient a.
     */
    public MontgomeryCurve(SikeParam sikeParam, Fp2Element a) {
        this.sikeParam = sikeParam;
        this.a = a;
        if (sikeParam.getImplementationType() == ImplementationType.OPTIMIZED) {
            optimizedConstants = new MontgomeryConstants(sikeParam, a);
        }
    }

    /**
     * Montgomery curve constructor.
     * @param sikeParam SIKE parameters.
     * @param a Montgomery curve coefficient a.
     * @param b Montgomery curve coefficient b.
     */
    public MontgomeryCurve(SikeParam sikeParam, Fp2Element a, Fp2Element b) {
        this(sikeParam, a);
        this.b = b;
    }

    /**
     * Get SIKE parameters.
     * @return SIKE parameters.
     */
    public SikeParam getSikeParam() {
        return sikeParam;
    }

    /**
     * Get Montgomery curve coefficient a.
     * @return Montgomery curve coefficient a.
     */
    public Fp2Element getA() {
        return a;
    }

    /**
     * Set Montgomery curve coefficient a.
     * @param a Montgomery curve coefficient a.
     */
    public void setA(Fp2Element a) {
        this.a = a;
    }

    /**
     * Get Montgomery curve coefficient b.
     * @return Montgomery curve coefficient b.
     */
    public Fp2Element getB() {
        return b;
    }

    /**
     * Set Montgomery curve coefficient b.
     * @param b Montgomery curve coefficient b.
     */
    public void setB(Fp2Element b) {
        this.b = b;
    }

    /**
     * Get optimized montgomery constants for this curve.
     * @return Optimized montgomery constants for this curve.
     */
    public MontgomeryConstants getOptimizedConstants() {
        if (sikeParam.getImplementationType() != ImplementationType.OPTIMIZED) {
            throw new InvalidParameterException("Invalid implementation type");
        }
        return optimizedConstants;
    }

    @Override
    public String toString() {
        if (sikeParam.getImplementationType() == ImplementationType.OPTIMIZED) {
            return "a = " + a + ", " + optimizedConstants;
        }
        return "a = " + a + ", b = " + b;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MontgomeryCurve that = (MontgomeryCurve) o;
        return sikeParam.equals(that.sikeParam) &&
                a.equals(that.a) &&
                Objects.equals(b, that.b) &&
                Objects.equals(optimizedConstants, that.optimizedConstants);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sikeParam, a, b, optimizedConstants);
    }
}
