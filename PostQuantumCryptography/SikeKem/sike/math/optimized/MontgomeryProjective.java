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
package sike.math.optimized;

import sike.math.api.Fp2Element;
import sike.math.api.Fp2Point;
import sike.math.api.Montgomery;
import sike.math.optimized.fp.FpElementOpti;
import sike.model.MontgomeryCurve;
import sike.model.optimized.MontgomeryConstants;
import sike.param.SikeParam;

/**
 * Optimized elliptic curve mathematics on Montgomery curves with projective coordinates.
 *
 * @author Roman Strobl, roman.strobl@wultra.com
 */
public class MontgomeryProjective implements Montgomery {

    @Override
    public Fp2Point xDbl(MontgomeryCurve curve, Fp2Point p) {
        MontgomeryConstants constants = curve.getOptimizedConstants();
        Fp2Element a24plus = constants.getA24plus();
        Fp2Element c24 = constants.getC24();
        Fp2Element t0, t1, p2x, p2z;
        t0 = p.getX().subtract(p.getZ());
        t1 = p.getX().add(p.getZ());
        t0 = t0.square();
        t1 = t1.square();
        p2z = c24.multiply(t0);
        p2x = p2z.multiply(t1);
        t1 = t1.subtract(t0);
        t0 = a24plus.multiply(t1);
        p2z = p2z.add(t0);
        p2z = p2z.multiply(t1);
        return new Fp2PointProjective(p2x, p2z);
    }

    @Override
    public Fp2Point xTpl(MontgomeryCurve curve, Fp2Point p) {
        MontgomeryConstants constants = curve.getOptimizedConstants();
        Fp2Element a24plus = constants.getA24plus();
        Fp2Element a24minus = constants.getA24minus();
        Fp2Element t0, t1, t2, t3, t4, t5, t6, p3x, p3z;
        t0 = p.getX().subtract(p.getZ());
        t2 = t0.square();
        t1 = p.getX().add(p.getZ());
        t3 = t1.square();
        t4 = t1.add(t0);
        t0 = t1.subtract(t0);
        t1 = t4.square();
        t1 = t1.subtract(t3);
        t1 = t1.subtract(t2);
        // Multiplicands are swapped for faster computation as it is done in official C implementation.
        t5 = a24plus.multiply(t3);
        t3 = t5.multiply(t3);
        t6 = t2.multiply(a24minus);
        t2 = t2.multiply(t6);
        t3 = t2.subtract(t3);
        t2 = t5.subtract(t6);
        t1 = t2.multiply(t1);
        t2 = t3.add(t1);
        t2 = t2.square();
        p3x = t2.multiply(t4);
        t1 = t3.subtract(t1);
        t1 = t1.square();
        p3z = t1.multiply(t0);
        return new Fp2PointProjective(p3x, p3z);
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
        Fp2Element a = curve.getA();
        MontgomeryConstants constants = curve.getOptimizedConstants();
        Fp2Element c = constants.getC();
        Fp2Element t0, t1, j;
        j = a.square();
        t1 = c.square();
        t0 = t1.add(t1);
        t0 = j.subtract(t0);
        t0 = t0.subtract(t1);
        j = t0.subtract(t1);
        t1 = t1.square();
        j = j.multiply(t1);
        t0 = t0.add(t0);
        t0 = t0.add(t0);
        t1 = t0.square();
        t0 = t0.multiply(t1);
        t0 = t0.add(t0);
        t0 = t0.add(t0);
        j = j.inverse();
        j = t0.multiply(j);
        return j;
    }

    @Override
    public Fp2Element getA(SikeParam sikeParam, Fp2Element px, Fp2Element qx, Fp2Element rx) {
        Fp2Element t0, t1, ap;
        t1 = px.add(qx);
        t0 = px.multiply(qx);
        ap = rx.multiply(t1);
        ap = ap.add(t0);
        t0 = t0.multiply(rx);
        ap = ap.subtract(sikeParam.getFp2ElementFactory().one());
        t0 = t0.add(t0);
        t1 = t1.add(rx);
        t0 = t0.add(t0);
        ap = ap.square();
        t0 = t0.inverse();
        ap = ap.multiply(t0);
        ap = ap.subtract(t1);
        return ap;
    }

    /**
     * Combined coordinate doubling and differential addition.
     * @param p Point P.
     * @param q Point Q.
     * @param r Point P - Q.
     * @return Points P2 and P + Q.
     */
    private Fp2Point[] xDblAdd(Fp2Point p, Fp2Point q, Fp2Point r, Fp2Element a24plus) {
        Fp2Element t0, t1, t2, p2x, p2z, pqx, pqz;
        t0 = p.getX().add(p.getZ());
        t1 = p.getX().subtract(p.getZ());
        p2x = t0.square();
        t2 = q.getX().subtract(q.getZ());
        pqx = q.getX().add(q.getZ());
        t0 = t0.multiply(t2);
        p2z = t1.square();
        t1 = t1.multiply(pqx);
        t2 = p2x.subtract(p2z);
        p2x = p2x.multiply(p2z);
        pqx = a24plus.multiply(t2);
        pqz = t0.subtract(t1);
        p2z = pqx.add(p2z);
        pqx = t0.add(t1);
        p2z = p2z.multiply(t2);
        pqz = pqz.square();
        pqx = pqx.square();
        pqz = r.getX().multiply(pqz);
        pqx = r.getZ().multiply(pqx);
        Fp2PointProjective p2 = new Fp2PointProjective(p2x, p2z);
        Fp2PointProjective pq = new Fp2PointProjective(pqx, pqz);
        return new Fp2PointProjective[]{p2, pq};
    }

    /**
     * Three point Montgomery ladder.
     * @param curve Current curve.
     * @param m Scalar value.
     * @param px The x coordinate of point P.
     * @param qx The x coordinate of point Q.
     * @param rx The x coordinate of point P - Q.
     * @param bits Number of bits in field elements.
     * @return Calculated new point.
     */
    public Fp2Point ladder3Pt(MontgomeryCurve curve, byte[] m, Fp2Element px, Fp2Element qx, Fp2Element rx, int bits) {
        SikeParam sikeParam = curve.getSikeParam();
        Fp2Element a = curve.getA();
        Fp2Point r0 = new Fp2PointProjective(qx.copy(), sikeParam.getFp2ElementFactory().one());
        Fp2Point r1 = new Fp2PointProjective(px.copy(), sikeParam.getFp2ElementFactory().one());
        Fp2Point r2 = new Fp2PointProjective(rx.copy(), sikeParam.getFp2ElementFactory().one());

        // Compute A + 2C / 4C
        Fp2Element c = curve.getOptimizedConstants().getC();
        Fp2Element c2 = c.add(c);
        Fp2Element aPlus2c = a.add(c2);
        Fp2Element c4 = c2.add(c2);
        Fp2Element c4Inv = c4.inverse();
        Fp2Element aPlus2cOver4c = aPlus2c.multiply(c4Inv);

        byte prevBit = 0;
        for (int i = 0; i < bits; i++) {
            byte bit = (byte) (m[i >>> 3] >>> (i & 7) & 1);
            byte swap = (byte) (prevBit ^ bit);
            prevBit = bit;
            condSwap(sikeParam, r1, r2, swap);
            Fp2Point[] points = xDblAdd(r0, r2, r1, aPlus2cOver4c);
            r0 = points[0].copy();
            r2 = points[1].copy();
        }
        condSwap(sikeParam, r1, r2, prevBit);
        return r1;
    }

    /**
     * Swap two points conditionally.
     * @param sikeParam SIKE parameters.
     * @param p Point p.
     * @param q Point q.
     * @param mask Swap condition, if zero swap is not performed.
     */
    private void condSwap(SikeParam sikeParam, Fp2Point p, Fp2Point q, long mask) {
        FpElementOpti.conditionalSwap(sikeParam, (FpElementOpti) p.getX().getX0(), (FpElementOpti) q.getX().getX0(), mask);
        FpElementOpti.conditionalSwap(sikeParam, (FpElementOpti) p.getX().getX1(), (FpElementOpti) q.getX().getX1(), mask);
        FpElementOpti.conditionalSwap(sikeParam, (FpElementOpti) p.getZ().getX0(), (FpElementOpti) q.getZ().getX0(), mask);
        FpElementOpti.conditionalSwap(sikeParam, (FpElementOpti) p.getZ().getX1(), (FpElementOpti) q.getZ().getX1(), mask);
    }

}
