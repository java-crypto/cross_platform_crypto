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

import sike.math.api.Fp2Point;

import java.util.Objects;

/**
 * Evaluated curve and optional points.
 *
 * @author Roman Strobl, roman.strobl@wultra.com
 */
public class EvaluatedCurve {

    private final MontgomeryCurve curve;
    private final Fp2Point p;
    private final Fp2Point q;
    private final Fp2Point r;

    /**
     * Curve constructor.
     * @param curve Evaluated curve.
     * @param p Optional point P.
     * @param q Optional point Q.
     */
    public EvaluatedCurve(MontgomeryCurve curve, Fp2Point p, Fp2Point q) {
        this.curve = curve;
        this.p = p;
        this.q = q;
        this.r = null;
    }

    /**
     * Curve constructor.
     * @param curve Evaluated curve.
     * @param p Optional point P.
     * @param q Optional point Q.
     * @param r Optional point R.
     */
    public EvaluatedCurve(MontgomeryCurve curve, Fp2Point p, Fp2Point q, Fp2Point r) {
        this.curve = curve;
        this.p = p;
        this.q = q;
        this.r = r;
    }

    /**
     * Get the evaluated curve.
     * @return Evaluated curve.
     */
    public MontgomeryCurve getCurve() {
        return curve;
    }

    /**
     * Get optional point P.
     * @return Optional point P.
     */
    public Fp2Point getP() {
        return p;
    }

    /**
     * Get optional point Q.
     * @return Optional point Q.
     */
    public Fp2Point getQ() {
        return q;
    }

    /**
     * Get optional point R.
     * @return Optional point R.
     */
    public Fp2Point getR() {
        return r;
    }

    @Override
    public String toString() {
        return curve.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EvaluatedCurve that = (EvaluatedCurve) o;
        // Use & to avoid timing attacks
        return curve.equals(that.curve)
                & Objects.equals(p, that.p)
                & Objects.equals(q, that.q)
                & Objects.equals(r, that.r);
    }

    @Override
    public int hashCode() {
        return Objects.hash(curve, p, q, r);
    }
}
