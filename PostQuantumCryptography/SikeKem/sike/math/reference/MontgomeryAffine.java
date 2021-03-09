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
import sike.math.api.Montgomery;
import sike.model.EvaluatedCurve;
import sike.model.MontgomeryCurve;
import sike.param.SikeParam;

import java.math.BigInteger;

/**
 * Reference elliptic curve mathematics on Montgomery curves with affine coordinates.
 *
 * @author Roman Strobl, roman.strobl@wultra.com
 */
public class MontgomeryAffine implements Montgomery {

    @Override
    public Fp2Point xDbl(MontgomeryCurve curve, Fp2Point p) {
        if (p.isInfinite()) {
            return p;
        }

        Fp2Element t0, t1, t2, x2p, y2p;
        Fp2Element b = curve.getB();
        SikeParam sikeParam = curve.getSikeParam();

        t0 = p.getX().square();
        t1 = t0.add(t0);
        t2 = sikeParam.getFp2ElementFactory().one();
        t0 = t0.add(t1);
        t1 = curve.getA().multiply(p.getX());
        t1 = t1.add(t1);
        t0 = t0.add(t1);
        t0 = t0.add(t2);
        t1 = b.multiply(p.getY());
        t1 = t1.add(t1);
        t1 = t1.inverse();
        t0 = t0.multiply(t1);
        t1 = t0.square();
        t2 = b.multiply(t1);
        t2 = t2.subtract(curve.getA());
        t2 = t2.subtract(p.getX());
        t2 = t2.subtract(p.getX());
        t1 = t0.multiply(t1);
        t1 = b.multiply(t1);
        t1 = t1.add(p.getY());
        y2p = p.getX().add(p.getX());
        y2p = y2p.add(p.getX());
        y2p = y2p.add(curve.getA());
        y2p = y2p.multiply(t0);
        y2p = y2p.subtract(t1);
        x2p = t2;
        return new Fp2PointAffine(x2p, y2p);
    }

    @Override
    public Fp2Point xTpl(MontgomeryCurve curve, Fp2Point p) {
        Fp2Point p2 = xDbl(curve, p);
        return xAdd(curve, p, p2);
    }

    @Override
    public Fp2Point xDble(MontgomeryCurve curve, Fp2Point p, int e) {
        Fp2Point pAp = p;
        for (int i = 0; i < e; i++) {
            pAp = xDbl(curve, pAp);
        }
        return pAp;
    }

    @Override
    public Fp2Point xTple(MontgomeryCurve curve, Fp2Point p, int e) {
        Fp2Point pAp = p;
        for (int i = 0; i < e; i++) {
            pAp = xTpl(curve, pAp);
        }
        return pAp;
    }

    @Override
    public Fp2Element jInv(MontgomeryCurve curve) {
        Fp2Element t0, t1, j;
        Fp2Element a = curve.getA();
        SikeParam sikeParam = curve.getSikeParam();
        t0 = a.square();
        j = sikeParam.getFp2ElementFactory().generate(new BigInteger("3"));
        j = t0.subtract(j);
        t1 = j.square();
        j = j.multiply(t1);
        j = j.add(j);
        j = j.add(j);
        j = j.add(j);
        j = j.add(j);
        j = j.add(j);
        j = j.add(j);
        j = j.add(j);
        j = j.add(j);
        t1 = sikeParam.getFp2ElementFactory().generate(new BigInteger("4"));
        t0 = t0.subtract(t1);
        t0 = t0.inverse();
        j = j.multiply(t0);
        return j;
    }

    @Override
    public Fp2Element getA(SikeParam sikeParam, Fp2Element px, Fp2Element qx, Fp2Element rx) {
        Fp2Element t0, t1, a;
        t1 = px.add(qx);
        t0 = px.multiply(qx);
        a = rx.multiply(t1);
        a = a.add(t0);
        t0 = t0.multiply(rx);
        a = a.subtract(sikeParam.getFp2ElementFactory().one());
        t0 = t0.add(t0);
        t1 = t1.add(rx);
        t0 = t0.add(t0);
        a = a.square();
        t0 = t0.inverse();
        a = a.multiply(t0);
        a = a.subtract(t1);
        return a;
    }

    /**
     * Double-and-add scalar multiplication.
     * @param curve Current curve.
     * @param m Scalar value.
     * @param p Point on the curve.
     * @param bits Number of bits in field elements.
     * @return Calculated new point.
     */
    public Fp2Point doubleAndAdd(MontgomeryCurve curve, BigInteger m, Fp2Point p, int bits) {
        SikeParam sikeParam = curve.getSikeParam();
        Fp2Point q = Fp2PointAffine.infinity(sikeParam);
        for (int i = bits - 1; i >= 0; i--) {
            q = xDbl(curve, q);
            if (m.testBit(i)) {
                q = xAdd(curve, q, p);
            }
        }
        return q;
    }

    /**
     * Adding of two points.
     * @param curve Current curve.
     * @param p First point on the curve.
     * @param q Second point on the curve.
     * @return Calculated new point.
     */
    public Fp2Point xAdd(MontgomeryCurve curve, Fp2Point p, Fp2Point q) {
        if (p.isInfinite()) {
            return q;
        }
        if (q.isInfinite()) {
            return p;
        }
        if (p.equals(q)) {
            return xDbl(curve, p);
        }
        if (p.equals(q.negate())) {
            SikeParam sikeParam = curve.getSikeParam();
            return Fp2PointAffine.infinity(sikeParam);
        }

        Fp2Element t0, t1, t2, xpq, ypq;
        Fp2Element b = curve.getB();

        t0 = q.getY().subtract(p.getY());
        t1 = q.getX().subtract(p.getX());
        t1 = t1.inverse();
        t0 = t0.multiply(t1);
        t1 = t0.square();
        t2 = p.getX().add(p.getX());
        t2 = t2.add(q.getX());
        t2 = t2.add(curve.getA());
        t2 = t2.multiply(t0);
        t0 = t0.multiply(t1);
        t0 = b.multiply(t0);
        t0 = t0.add(p.getY());
        t0 = t2.subtract(t0);
        t1 = b.multiply(t1);
        t1 = t1.subtract(curve.getA());
        t1 = t1.subtract(p.getX());
        xpq = t1.subtract(q.getX());
        ypq = t0;
        return new Fp2PointAffine(xpq, ypq);
    }

    /**
     * Recover the point R = P - Q.
     * @param curve Current curve.
     * @param p Point P.
     * @param q Point Q.
     * @return Calculated point R.
     */
    public Fp2Point getXr(MontgomeryCurve curve, Fp2Point p, Fp2Point q) {
        Fp2Point qNeg = new Fp2PointAffine(q.getX(), q.getY().negate());
        return xAdd(curve, p, qNeg);
    }

    /**
     * Recover the curve and points P and Q.
     * @param sikeParam SIKE parameters.
     * @param px The x coordinate of point P.
     * @param qx The x coordinate of point Q.
     * @param rx The x coordinate of point R.
     * @return A recovered curve and points P and Q.
     */
    public EvaluatedCurve getYpYqAB(SikeParam sikeParam, Fp2Element px, Fp2Element qx, Fp2Element rx) {
        Fp2Element b, t1, t2, py, qy;
        Fp2Element a = getA(sikeParam, px, qx, rx);
        b = sikeParam.getFp2ElementFactory().one();
        MontgomeryCurve curve = new MontgomeryCurve(sikeParam, a, b);
        t1 = px.square();
        t2 = px.multiply(t1);
        t1 = a.multiply(t1);
        t1 = t2.add(t1);
        t1 = t1.add(px);
        py = t1.sqrt();
        t1 = qx.square();
        t2 = qx.multiply(t1);
        t1 = a.multiply(t1);
        t1 = t2.add(t1);
        t1 = t1.add(qx);
        qy = t1.sqrt();
        Fp2Point p = new Fp2PointAffine(px, py);
        Fp2Point q1 = new Fp2PointAffine(qx, qy.negate());
        Fp2Point t = xAdd(curve, p, q1);
        if (!t.getX().equals(rx)) {
            qy = qy.negate();
        }
        Fp2Point q = new Fp2PointAffine(qx, qy);
        return new EvaluatedCurve(curve, p, q);
    }

}
