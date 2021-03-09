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
import sike.math.api.Isogeny;
import sike.math.api.Montgomery;
import sike.math.optimized.fp.Fp2ElementOpti;
import sike.model.EvaluatedCurve;
import sike.model.MontgomeryCurve;
import sike.model.SidhPrivateKey;
import sike.model.SidhPublicKey;
import sike.model.optimized.MontgomeryConstants;
import sike.param.SikeParam;

import java.math.BigInteger;

/**
 * Optimized elliptic curve isogeny operations on Montgomery curves with projective coordinates.
 *
 * @author Roman Strobl, roman.strobl@wultra.com
 */
public class IsogenyProjective implements Isogeny {

    @Override
    public MontgomeryCurve curve2Iso(MontgomeryCurve curve, Fp2Point p2) {
        Fp2Element a24plus, c24;
        a24plus = p2.getX().square();
        c24 = p2.getZ().square();
        a24plus = c24.subtract(a24plus);
        MontgomeryCurve curve2 = new MontgomeryCurve(curve.getSikeParam());
        MontgomeryConstants constants = curve2.getOptimizedConstants();
        constants.setA24plus(a24plus);
        constants.setC24(c24);
        return curve2;
    }

    @Override
    public MontgomeryCurve curve3Iso(MontgomeryCurve curve, Fp2Point p3) {
        Fp2Element k1, k2, t0, t1, t2, t3, t4, a24plus, a24minus;
        k1 = p3.getX().subtract(p3.getZ());
        t0 = k1.square();
        k2 = p3.getX().add(p3.getZ());
        t1 = k2.square();
        // The optimized implementation deviates from the specification at this point, see:
        // https://github.com/microsoft/PQCrypto-SIDH/commit/7218dce28a25f17c3860c227df5d8a7b2adf1d1b#diff-08daf821aec6f05034194147b25fb0d9726a4d4ede8f4634c6a66fdbb728047fR170
        // The algorithm is defined as Alg. 15 in specification at https://sike.org/files/SIDH-spec.pdf
        // This implementation will use the original algorithm until the change is propagated into the specification.
        t2 = t0.add(t1);
        t3 = k1.add(k2);
        t3 = t3.square();
        t3 = t3.subtract(t2);
        t2 = t1.add(t3);
        t3 = t3.add(t0);
        t4 = t3.add(t0);
        t4 = t4.add(t4);
        t4 = t1.add(t4);
        a24minus = t2.multiply(t4);
        t4 = t1.add(t2);
        t4 = t4.add(t4);
        t4 = t0.add(t4);
        a24plus = t3.multiply(t4);
        MontgomeryCurve curve3 = new MontgomeryCurve(curve.getSikeParam());
        MontgomeryConstants constants = curve3.getOptimizedConstants();
        constants.setA24plus(a24plus);
        constants.setA24minus(a24minus);
        constants.setK1(k1);
        constants.setK2(k2);
        return curve3;
    }

    @Override
    public MontgomeryCurve curve4Iso(MontgomeryCurve curve, Fp2Point p4) {
        Fp2Element k1, k2, k3, a24plus, c24;
        k2 = p4.getX().subtract(p4.getZ());
        k3 = p4.getX().add(p4.getZ());
        k1 = p4.getZ().square();
        k1 = k1.add(k1);
        c24 = k1.square();
        k1 = k1.add(k1);
        a24plus = p4.getX().square();
        a24plus = a24plus.add(a24plus);
        a24plus = a24plus.square();
        MontgomeryCurve curve4 = new MontgomeryCurve(curve.getSikeParam());
        MontgomeryConstants constants = curve4.getOptimizedConstants();
        constants.setA24plus(a24plus);
        constants.setC24(c24);
        constants.setK1(k1);
        constants.setK2(k2);
        constants.setK3(k3);
        return curve4;
    }

    @Override
    public Fp2Point eval2Iso(Fp2Point q, Fp2Point p2) {
        Fp2Element t0, t1, t2, t3, qx, qz;
        t0 = p2.getX().add(p2.getZ());
        t1 = p2.getX().subtract(p2.getZ());
        t2 = q.getX().add(q.getZ());
        t3 = q.getX().subtract(q.getZ());
        t0 = t0.multiply(t3);
        t1 = t1.multiply(t2);
        t2 = t0.add(t1);
        t3 = t0.subtract(t1);
        qx = q.getX().multiply(t2);
        qz = q.getZ().multiply(t3);
        return new Fp2PointProjective(qx, qz);
    }

    @Override
    public Fp2Point eval3Iso(MontgomeryCurve curve, Fp2Point q, Fp2Point p3) {
        Fp2Element t0, t1, t2, qx, qz, k1, k2;
        k1 = curve.getOptimizedConstants().getK1();
        k2 = curve.getOptimizedConstants().getK2();
        t0 = q.getX().add(q.getZ());
        t1 = q.getX().subtract(q.getZ());
        t0 = k1.multiply(t0);
        t1 = k2.multiply(t1);
        t2 = t0.add(t1);
        t0 = t1.subtract(t0);
        t2 = t2.square();
        t0 = t0.square();
        qx = q.getX().multiply(t2);
        qz = q.getZ().multiply(t0);
        return new Fp2PointProjective(qx, qz);
    }

    @Override
    public Fp2Point eval4Iso(MontgomeryCurve curve, Fp2Point q, Fp2Point p4) {
        Fp2Element t0, t1, qx, qz, k1, k2, k3;
        k1 = curve.getOptimizedConstants().getK1();
        k2 = curve.getOptimizedConstants().getK2();
        k3 = curve.getOptimizedConstants().getK3();
        t0 = q.getX().add(q.getZ());
        t1 = q.getX().subtract(q.getZ());
        qx = t0.multiply(k2);
        qz = t1.multiply(k3);
        t0 = t0.multiply(t1);
        // Multiplicands are swapped for faster computation as it is done in official C implementation.
        t0 = k1.multiply(t0);
        t1 = qx.add(qz);
        qz = qx.subtract(qz);
        t1 = t1.square();
        qz = qz.square();
        qx = t0.add(t1);
        t0 = qz.subtract(t0);
        qx = qx.multiply(t1);
        qz = qz.multiply(t0);
        return new Fp2PointProjective(qx, qz);
    }

    @Override
    public EvaluatedCurve iso2e(MontgomeryCurve curve, Fp2Point s0, Fp2Point ... points) {
        SikeParam sikeParam = curve.getSikeParam();
        Montgomery montgomery = sikeParam.getMontgomery();
        MontgomeryCurve curveAp = curve;
        Fp2Point r = s0, phiP = null, phiQ = null, phiR = null;
        if (points.length == 3) {
            phiP = points[0];
            phiQ = points[1];
            phiR = points[2];
        }
        int m, pointCount = 0, ii = 0;
        Fp2Point[] treePoints = new Fp2Point[sikeParam.getTreePointsA()];
        int[] pointIndex = new int[sikeParam.getTreeRowsA()];
        int[] strategy = sikeParam.getStrategyA();

        int eAp = curve.getSikeParam().getEA();
        if (eAp % 2 == 1) {
            Fp2Point s = montgomery.xDble(curveAp, r, eAp - 1);
            curveAp = curve2Iso(curveAp, s);
            if (points.length == 3) {
                phiP = eval2Iso(phiP, s);
                phiQ = eval2Iso(phiQ, s);
                phiR = eval2Iso(phiR, s);
            }
            r = eval2Iso(r, s);
        }

        int index = 0;
        for (int row = 1; row < sikeParam.getTreeRowsA(); row++) {
            while (index < sikeParam.getTreeRowsA() - row) {
                treePoints[pointCount] = r.copy();
                pointIndex[pointCount++] = index;
                m = strategy[ii++];
                r = montgomery.xDble(curveAp, r, 2 * m);
                index += m;
            }

            curveAp = curve4Iso(curveAp, r);

            for (int i = 0; i < pointCount; i++) {
                treePoints[i] = eval4Iso(curveAp, treePoints[i], null);
            }
            if (points.length == 3) {
                phiP = eval4Iso(curveAp, phiP, null);
                phiQ = eval4Iso(curveAp, phiQ, null);
                phiR = eval4Iso(curveAp, phiR, null);
            }
            r = treePoints[pointCount - 1].copy();
            index = pointIndex[pointCount - 1];
            pointCount--;
        }

        curveAp = curve4Iso(curveAp, r);

        if (points.length == 3) {
            phiP = eval4Iso(curveAp, phiP, null);
            phiQ = eval4Iso(curveAp, phiQ, null);
            phiR = eval4Iso(curveAp, phiR, null);
            Fp2Element[] inverted = inv3Way(phiP.getZ(), phiQ.getZ(), phiR.getZ());
            phiP = new Fp2PointProjective(phiP.getX().multiply(inverted[0]), inverted[0]);
            phiQ = new Fp2PointProjective(phiQ.getX().multiply(inverted[1]), inverted[1]);
            phiR = new Fp2PointProjective(phiR.getX().multiply(inverted[2]), inverted[2]);
        }

        return new EvaluatedCurve(curveAp, phiP, phiQ, phiR);
    }

    @Override
    public EvaluatedCurve iso3e(MontgomeryCurve curve, Fp2Point s0, Fp2Point ... points) {
        SikeParam sikeParam = curve.getSikeParam();
        Montgomery montgomery = sikeParam.getMontgomery();
        MontgomeryCurve curveAp = curve;
        Fp2Point r = s0, phiP = null, phiQ = null, phiR = null;
        if (points.length == 3) {
            phiP = points[0];
            phiQ = points[1];
            phiR = points[2];
        }
        int m, pointCount = 0, ii = 0;
        Fp2Point[] treePoints = new Fp2Point[sikeParam.getTreePointsB()];
        int[] pointIndex = new int[sikeParam.getTreeRowsB()];
        int[] strategy = sikeParam.getStrategyB();

        int index = 0;
        for (int row = 1; row < sikeParam.getTreeRowsB(); row++) {
            while (index < sikeParam.getTreeRowsB() - row) {
                treePoints[pointCount] = r.copy();
                pointIndex[pointCount++] = index;
                m = strategy[ii++];
                r = montgomery.xTple(curveAp, r, m);
                index += m;
            }

            curveAp = curve3Iso(curveAp, r);

            for (int i = 0; i < pointCount; i++) {
                treePoints[i] = eval3Iso(curveAp, treePoints[i], r);
            }
            if (points.length == 3) {
                phiP = eval3Iso(curveAp, phiP, r);
                phiQ = eval3Iso(curveAp, phiQ, r);
                phiR = eval3Iso(curveAp, phiR, r);
            }
            r = treePoints[pointCount - 1].copy();
            index = pointIndex[pointCount - 1];
            pointCount--;
        }

        curveAp = curve3Iso(curveAp, r);

        if (points.length == 3) {
            phiP = eval3Iso(curveAp, phiP, r);
            phiQ = eval3Iso(curveAp, phiQ, r);
            phiR = eval3Iso(curveAp, phiR, r);
            Fp2Element[] inverted = inv3Way(phiP.getZ(), phiQ.getZ(), phiR.getZ());
            phiP = new Fp2PointProjective(phiP.getX().multiply(inverted[0]), inverted[0]);
            phiQ = new Fp2PointProjective(phiQ.getX().multiply(inverted[1]), inverted[1]);
            phiR = new Fp2PointProjective(phiR.getX().multiply(inverted[2]), inverted[2]);
        }

        return new EvaluatedCurve(curveAp, phiP, phiQ, phiR);
    }

    @Override
    public SidhPublicKey isoGen2(MontgomeryCurve curve, SidhPrivateKey privateKey) {
        SikeParam sikeParam = curve.getSikeParam();
        MontgomeryProjective montgomery = (MontgomeryProjective) sikeParam.getMontgomery();
        Fp2Point p1 = new Fp2PointProjective(sikeParam.getPB().getX(), sikeParam.getFp2ElementFactory().one());
        Fp2Point p2 = new Fp2PointProjective(sikeParam.getQB().getX(), sikeParam.getFp2ElementFactory().one());
        Fp2Point p3 = new Fp2PointProjective(sikeParam.getRB().getX(), sikeParam.getFp2ElementFactory().one());
        Fp2Point s = montgomery.ladder3Pt(curve, privateKey.getKey(), sikeParam.getPA().getX(), sikeParam.getQA().getX(),
                sikeParam.getRA().getX(), sikeParam.getBitsA());
        EvaluatedCurve evaluatedCurve = iso2e(curve, s, p1, p2, p3);
        return createPublicKey(sikeParam, evaluatedCurve);
    }

    @Override
    public SidhPublicKey isoGen3(MontgomeryCurve curve, SidhPrivateKey privateKey) {
        SikeParam sikeParam = curve.getSikeParam();
        MontgomeryProjective montgomery = (MontgomeryProjective) sikeParam.getMontgomery();
        Fp2Point p1 = new Fp2PointProjective(sikeParam.getPA().getX(), sikeParam.getFp2ElementFactory().one());
        Fp2Point p2 = new Fp2PointProjective(sikeParam.getQA().getX(), sikeParam.getFp2ElementFactory().one());
        Fp2Point p3 = new Fp2PointProjective(sikeParam.getRA().getX(), sikeParam.getFp2ElementFactory().one());
        Fp2Point s = montgomery.ladder3Pt(curve, privateKey.getKey(), sikeParam.getPB().getX(), sikeParam.getQB().getX(),
                sikeParam.getRB().getX(), sikeParam.getBitsB() - 1);
        EvaluatedCurve evaluatedCurve = iso3e(curve, s, p1, p2, p3);
        return createPublicKey(sikeParam, evaluatedCurve);
    }

    private SidhPublicKey createPublicKey(SikeParam sikeParam, EvaluatedCurve evaluatedCurve) {
        Fp2Point p = evaluatedCurve.getP();
        Fp2Point q = evaluatedCurve.getQ();
        Fp2Point r = evaluatedCurve.getR();
        Fp2Element px = new Fp2ElementOpti(sikeParam, p.getX().getX0(), p.getX().getX1());
        Fp2Element qx = new Fp2ElementOpti(sikeParam, q.getX().getX0(), q.getX().getX1());
        Fp2Element rx = new Fp2ElementOpti(sikeParam, r.getX().getX0(), r.getX().getX1());
        return new SidhPublicKey(sikeParam, px, qx, rx);
    }

    @Override
    public Fp2Element isoEx2(SikeParam sikeParam, byte[] sk2, Fp2Element p2, Fp2Element q2, Fp2Element r2) {
        MontgomeryProjective montgomery = (MontgomeryProjective) sikeParam.getMontgomery();
        Fp2Element a = montgomery.getA(sikeParam, p2, q2, r2);
        MontgomeryCurve curve = new MontgomeryCurve(sikeParam, a);
        Fp2Point s = montgomery.ladder3Pt(curve, sk2, p2, q2, r2, sikeParam.getBitsA());
        Fp2Element two = sikeParam.getFp2ElementFactory().generate(new BigInteger("2"));
        Fp2Element four = sikeParam.getFp2ElementFactory().generate(new BigInteger("4"));
        curve.getOptimizedConstants().setA24minus(curve.getA().add(two));
        curve.getOptimizedConstants().setC24(four);
        EvaluatedCurve iso2 = iso2e(curve, s);
        MontgomeryCurve curve2 = iso2.getCurve();
        Fp2Element a24plus = curve2.getOptimizedConstants().getA24plus();
        Fp2Element c24 = curve2.getOptimizedConstants().getC24();
        Fp2Element ap = a24plus.multiply(four);
        ap = ap.subtract(c24.multiply(two));
        curve2.setA(ap);
        curve2.getOptimizedConstants().setC(c24);
        return montgomery.jInv(curve2);
    }

    @Override
    public Fp2Element isoEx3(SikeParam sikeParam, byte[] sk3, Fp2Element p3, Fp2Element q3, Fp2Element r3) {
        MontgomeryProjective montgomery = (MontgomeryProjective) sikeParam.getMontgomery();
        Fp2Element a = montgomery.getA(sikeParam, p3, q3, r3);
        MontgomeryCurve curve = new MontgomeryCurve(sikeParam, a);
        Fp2Point s = montgomery.ladder3Pt(curve, sk3, p3, q3, r3, sikeParam.getBitsB() - 1);
        Fp2Element two = sikeParam.getFp2ElementFactory().generate(new BigInteger("2"));
        curve.getOptimizedConstants().setA24minus(curve.getA().add(two));
        curve.getOptimizedConstants().setA24minus(curve.getA().subtract(two));
        EvaluatedCurve iso3 = iso3e(curve, s);
        MontgomeryCurve curve3 = iso3.getCurve();
        Fp2Element a24plus = curve3.getOptimizedConstants().getA24plus();
        Fp2Element a24minus = curve3.getOptimizedConstants().getA24minus();
        Fp2Element ap = a24minus.add(a24plus);
        ap = ap.multiply(two);
        Fp2Element c = a24plus.subtract(a24minus);
        curve3.setA(ap);
        curve3.getOptimizedConstants().setC(c);
        return montgomery.jInv(curve3);
    }

    /**
     * Inverse three F(p^2) elements simultaneously.
     * @param z1 First element.
     * @param z2 Second element.
     * @param z3 Third element.
     * @return Inversed elements.
     */
    private Fp2Element[] inv3Way(Fp2Element z1, Fp2Element z2, Fp2Element z3) {
        Fp2Element t0, t1, t2, t3;
        t0 = z1.multiply(z2);
        t1 = z3.multiply(t0);
        t1 = t1.inverse();
        t2 = z3.multiply(t1);
        t3 = t2.multiply(z2);
        z2 = t2.multiply(z1);
        z3 = t0.multiply(t1);
        z1 = t3;
        return new Fp2Element[]{z1, z2, z3};
    }
}
