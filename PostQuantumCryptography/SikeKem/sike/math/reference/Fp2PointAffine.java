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
package sike.math.reference;

import sike.math.api.Fp2Element;
import sike.math.api.Fp2Point;
import sike.param.SikeParam;

import java.security.InvalidParameterException;
import java.util.Objects;

/**
 * Point with affine coordinates [x:y] in F(p^2).
 *
 * @author Roman Strobl, roman.strobl@wultra.com
 */
public class Fp2PointAffine implements Fp2Point {

    private final Fp2Element x;
    private final Fp2Element y;

    /**
     * Affine point constructor.
     * @param x The x element.
     * @param y The y element.
     */
    public Fp2PointAffine(Fp2Element x, Fp2Element y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Construct point at infinity.
     * @param sikeParam SIKE parameters.
     * @return Point at infinity.
     */
    public static Fp2Point infinity(SikeParam sikeParam) {
        Fp2Element zero = sikeParam.getFp2ElementFactory().zero();
        return new Fp2PointAffine(zero, zero);
    }

    @Override
    public Fp2Element getX() {
        return x;
    }

    @Override
    public Fp2Element getY() {
        return y;
    }

    /**
     * The z coordinate is not defined in affine coordinate system.
     */
    @Override
    public Fp2Element getZ() {
        throw new InvalidParameterException("Invalid point coordinate");
    }

    @Override
    public Fp2Point add(Fp2Point o) {
        return new Fp2PointAffine(x.add(o.getX()), y.add(o.getY()));
    }

    @Override
    public Fp2Point subtract(Fp2Point o) {
        return new Fp2PointAffine(x.subtract(o.getX()), y.subtract(o.getY()));
    }

    @Override
    public Fp2Point multiply(Fp2Point o) {
        return new Fp2PointAffine(x.multiply(o.getX()), y.multiply(o.getY()));
    }

    @Override
    public Fp2Point square() {
        return multiply(this);
    }

    @Override
    public Fp2Point inverse() {
        return new Fp2PointAffine(x.inverse(), y.inverse());
    }

    @Override
    public Fp2Point negate() {
        return new Fp2PointAffine(x, y.negate());
    }

    @Override
    public boolean isInfinite() {
        return y.isZero();
    }

    @Override
    public Fp2Point copy() {
        return new Fp2PointAffine(x.copy(), y.copy());
    }

    public String toString() {
        return "(" + x.toString() + ", " + y.toString() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fp2PointAffine that = (Fp2PointAffine) o;
        return x.equals(that.x) &&
                y.equals(that.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
