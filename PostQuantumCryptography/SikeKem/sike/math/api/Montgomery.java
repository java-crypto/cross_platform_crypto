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

import sike.model.MontgomeryCurve;
import sike.param.SikeParam;

/**
 * Elliptic curve mathematics on Montgomery curves. A common interface for all implementation types
 *
 * @author Roman Strobl, roman.strobl@wultra.com
 */
public interface Montgomery {

    /**
     * Double a point.
     * @param curve Current curve.
     * @param p Point on the curve.
     * @return Calculated new point.
     */
    Fp2Point xDbl(MontgomeryCurve curve, Fp2Point p);

    /**
     * Triple a point.
     * @param curve Current curve.
     * @param p Point on the curve.
     * @return Calculated new point.
     */
    Fp2Point xTpl(MontgomeryCurve curve, Fp2Point p);

    /**
     * Repeated doubling of a point.
     * @param curve Current curve.
     * @param p Point on the curve.
     * @param e Number of iterations.
     * @return Calculated new point.
     */
    Fp2Point xDble(MontgomeryCurve curve, Fp2Point p, int e);

    /**
     * Repeated trippling of a point.
     * @param curve Current curve.
     * @param p Point on the curve.
     * @param e Number of iterations.
     * @return Calculated new point.
     */
    Fp2Point xTple(MontgomeryCurve curve, Fp2Point p, int e);

    /**
     * Calculate a j-invariant of a curve.
     * @param curve Current curve.
     * @return Calculated j-invariant.
     */
    Fp2Element jInv(MontgomeryCurve curve);

    /**
     * Recover the Montgomery curve coefficient a.
     * @param sikeParam SIKE parameters.
     * @param px The x coordinate of point P.
     * @param qx The x coordinate of point Q.
     * @param rx The x coordinate of point R.
     * @return Recovered coefficient a.
     */
    Fp2Element getA(SikeParam sikeParam, Fp2Element px, Fp2Element qx, Fp2Element rx);

}
