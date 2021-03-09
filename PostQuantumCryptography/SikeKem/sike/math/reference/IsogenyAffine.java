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
import sike.math.api.Isogeny;
import sike.math.api.Montgomery;
import sike.math.reference.fp.Fp2ElementRef;
import sike.model.EvaluatedCurve;
import sike.model.MontgomeryCurve;
import sike.model.SidhPrivateKey;
import sike.model.SidhPublicKey;
import sike.param.SikeParam;
import sike.util.ByteEncoding;

import java.math.BigInteger;

/**
 * Reference elliptic curve isogeny operations on Montgomery curves with projective coordinates.
 *
 * @author Roman Strobl, roman.strobl@wultra.com
 */
public class IsogenyAffine implements Isogeny {

    @Override
    public MontgomeryCurve curve2Iso(MontgomeryCurve curve, Fp2Point p2) {
        Fp2Element t1, aAp, bAp;
        Fp2Element b = curve.getB();
        SikeParam sikeParam = curve.getSikeParam();
        t1 = p2.getX().square();
        t1 = t1.add(t1);
        t1 = sikeParam.getFp2ElementFactory().one().subtract(t1);
        aAp = t1.add(t1);
        bAp = p2.getX().multiply(b);
        return new MontgomeryCurve(curve.getSikeParam(), aAp, bAp);
    }

    @Override
    public MontgomeryCurve curve3Iso(MontgomeryCurve curve, Fp2Point p3) {
        Fp2Element t1, t2, aAp, bAp;
        Fp2Element b = curve.getB();
        SikeParam sikeParam = curve.getSikeParam();
        t1 = p3.getX().square();
        bAp = b.multiply(t1);
        t1 = t1.add(t1);
        t2 = t1.add(t1);
        t1 = t1.add(t2);
        t2 = sikeParam.getFp2ElementFactory().generate(new BigInteger("6"));
        t1 = t1.subtract(t2);
        t2 = curve.getA().multiply(p3.getX());
        t1 = t2.subtract(t1);
        aAp = t1.multiply(p3.getX());
        return new MontgomeryCurve(curve.getSikeParam(), aAp, bAp);
    }

    @Override
    public MontgomeryCurve curve4Iso(MontgomeryCurve curve, Fp2Point p4) {
        Fp2Element t1, t2, aAp, bAp;
        Fp2Element b = curve.getB();
        SikeParam sikeParam = curve.getSikeParam();
        t1 = p4.getX().square();
        aAp = t1.square();
        aAp = aAp.add(aAp);
        aAp = aAp.add(aAp);
        t2 = sikeParam.getFp2ElementFactory().generate(new BigInteger("2"));
        aAp = aAp.subtract(t2);
        t1 = p4.getX().multiply(t1);
        t1 = t1.add(p4.getX());
        t1 = t1.multiply(b);
        t2 = t2.inverse();
        t2 = t2.negate();
        bAp = t2.multiply(t1);
        return new MontgomeryCurve(curve.getSikeParam(), aAp, bAp);
    }

    @Override
    public Fp2Point eval2Iso(Fp2Point q, Fp2Point p2) {
        Fp2Element t1, t2, t3, qxAp, qyAp;
        t1 = q.getX().multiply(p2.getX());
        t2 = q.getX().multiply(t1);
        t3 = t1.multiply(p2.getX());
        t3 = t3.add(t3);
        t3 = t2.subtract(t3);
        t3 = t3.add(p2.getX());
        t3 = q.getY().multiply(t3);
        t2 = t2.subtract(q.getX());
        t1 = q.getX().subtract(p2.getX());
        t1 = t1.inverse();
        qxAp = t2.multiply(t1);
        t1 = t1.square();
        qyAp = t3.multiply(t1);
        return new Fp2PointAffine(qxAp, qyAp);
    }

    @Override
    public Fp2Point eval3Iso(MontgomeryCurve curve, Fp2Point q, Fp2Point p3) {
        Fp2Element t1, t2, t3, t4, qxAp, qyAp;
        SikeParam sikeParam = curve.getSikeParam();
        t1 = q.getX().square();
        t1 = t1.multiply(p3.getX());
        t2 = p3.getX().square();
        t2 = q.getX().multiply(t2);
        t3 = t2.add(t2);
        t2 = t2.add(t3);
        t1 = t1.subtract(t2);
        t1 = t1.add(q.getX());
        t1 = t1.add(p3.getX());
        t2 = q.getX().subtract(p3.getX());
        t2 = t2.inverse();
        t3 = t2.square();
        t2 = t2.multiply(t3);
        t4 = q.getX().multiply(p3.getX());
        t4 = t4.subtract(sikeParam.getFp2ElementFactory().one());
        t1 = t4.multiply(t1);
        t1 = t1.multiply(t2);
        t2 = t4.square();
        t2 = t2.multiply(t3);
        qxAp = q.getX().multiply(t2);
        qyAp = q.getY().multiply(t1);
        return new Fp2PointAffine(qxAp, qyAp);
    }

    @Override
    public Fp2Point eval4Iso(MontgomeryCurve curve, Fp2Point q, Fp2Point p4) {
        Fp2Element t1, t2, t3, t4, t5, qxAp, qyAp;
        SikeParam sikeParam = curve.getSikeParam();
        t1 = q.getX().square();
        t2 = t1.square();
        t3 = p4.getX().square();
        t4 = t2.multiply(t3);
        t2 = t2.add(t4);
        t4 = t1.multiply(t3);
        t4 = t4.add(t4);
        t5 = t4.add(t4);
        t5 = t5.add(t5);
        t4 = t4.add(t5);
        t2 = t2.add(t4);
        t4 = t3.square();
        t5 = t1.multiply(t4);
        t5 = t5.add(t5);
        t2 = t2.add(t5);
        t1 = t1.multiply(q.getX());
        t4 = p4.getX().multiply(t3);
        t5 = t1.multiply(t4);
        t5 = t5.add(t5);
        t5 = t5.add(t5);
        t2 = t2.subtract(t5);
        t1 = t1.multiply(p4.getX());
        t1 = t1.add(t1);
        t1 = t1.add(t1);
        t1 = t2.subtract(t1);
        t2 = q.getX().multiply(t4);
        t2 = t2.add(t2);
        t2 = t2.add(t2);
        t1 = t1.subtract(t2);
        t1 = t1.add(t3);
        t1 = t1.add(sikeParam.getFp2ElementFactory().one());
        t2 = q.getX().multiply(p4.getX());
        t4 = t2.subtract(sikeParam.getFp2ElementFactory().one());
        t2 = t2.add(t2);
        t5 = t2.add(t2);
        t1 = t1.subtract(t5);
        t1 = t4.multiply(t1);
        t1 = t3.multiply(t1);
        t1 = q.getY().multiply(t1);
        t1 = t1.add(t1);
        qyAp = t1.negate();
        t2 = t2.subtract(t3);
        t1 = t2.subtract(sikeParam.getFp2ElementFactory().one());
        t2 = q.getX().subtract(p4.getX());
        t1 = t2.multiply(t1);
        t5 = t1.square();
        t5 = t5.multiply(t2);
        t5 = t5.inverse();
        qyAp = qyAp.multiply(t5);
        t1 = t1.multiply(t2);
        t1 = t1.inverse();
        t4 = t4.square();
        t1 = t1.multiply(t4);
        t1 = q.getX().multiply(t1);
        t2 = q.getX().multiply(t3);
        t2 = t2.add(q.getX());
        t3 = p4.getX().add(p4.getX());
        t2 = t2.subtract(t3);
        t2 = t2.negate();
        qxAp = t1.multiply(t2);
        return new Fp2PointAffine(qxAp, qyAp);
    }

    @Override
    public EvaluatedCurve iso2e(MontgomeryCurve curve, Fp2Point s, Fp2Point ... points) {
        Montgomery montgomery = curve.getSikeParam().getMontgomery();
        MontgomeryCurve curveAp = curve;
        Fp2Point sAp = s, phiP = null, phiQ = null;
        if (points.length == 2) {
            phiP = points[0];
            phiQ = points[1];
        }
        Fp2Point r;
        int eAp = curve.getSikeParam().getEA();
        if (eAp % 2 == 1) {
            r = montgomery.xDble(curveAp, sAp, eAp - 1);
            curveAp = curve2Iso(curveAp, r);
            sAp = eval2Iso(sAp, r);
            if (points.length == 2) {
                phiP = eval2Iso(phiP, r);
                phiQ = eval2Iso(phiQ, r);
            }
            eAp--;
        }
        for (int e = eAp - 2; e >= 0; e -= 2) {
            r = montgomery.xDble(curveAp, sAp, e);
            curveAp = curve4Iso(curveAp, r);
            // Fix division by zero in reference implementation
            if (e > 0) {
                sAp = eval4Iso(curveAp, sAp, r);
            }
            if (points.length == 2) {
                phiP = eval4Iso(curveAp, phiP, r);
                phiQ = eval4Iso(curveAp, phiQ, r);
            }
        }
        return new EvaluatedCurve(curveAp, phiP, phiQ);
    }

    @Override
    public EvaluatedCurve iso3e(MontgomeryCurve curve, Fp2Point s, Fp2Point ... points) {
        Montgomery montgomery = curve.getSikeParam().getMontgomery();
        MontgomeryCurve curveAp = curve;
        Fp2Point sAp = s, phiP = null, phiQ = null;
        if (points.length == 2) {
            phiP = points[0];
            phiQ = points[1];
        }
        Fp2Point r;
        for (int e = curve.getSikeParam().getEB() - 1; e >= 0; e--) {
            r = montgomery.xTple(curveAp, sAp, e);
            curveAp = curve3Iso(curveAp, r);
            // Fix division by zero in reference implementation
            if (e > 0) {
                sAp = eval3Iso(curveAp, sAp, r);
            }
            if (points.length == 2) {
                phiP = eval3Iso(curveAp, phiP, r);
                phiQ = eval3Iso(curveAp, phiQ, r);
            }
        }
        return new EvaluatedCurve(curveAp, phiP, phiQ);
    }

    @Override
    public SidhPublicKey isoGen2(MontgomeryCurve curve, SidhPrivateKey privateKey) {
        SikeParam sikeParam = curve.getSikeParam();
        MontgomeryAffine montgomery = (MontgomeryAffine) sikeParam.getMontgomery();
        Isogeny isogeny = sikeParam.getIsogeny();
        Fp2Point s = montgomery.doubleAndAdd(curve, privateKey.getFpElement().getX(), sikeParam.getQA(), sikeParam.getBitsA());
        s = montgomery.xAdd(curve, sikeParam.getPA(), s);
        EvaluatedCurve evaluatedCurve = isogeny.iso2e(curve, s, sikeParam.getPB(), sikeParam.getQB());
        return createPublicKey(sikeParam, evaluatedCurve);
    }

    @Override
    public SidhPublicKey isoGen3(MontgomeryCurve curve, SidhPrivateKey privateKey) {
        SikeParam sikeParam = curve.getSikeParam();
        MontgomeryAffine montgomery = (MontgomeryAffine) sikeParam.getMontgomery();
        Isogeny isogeny = sikeParam.getIsogeny();
        Fp2Point s = montgomery.doubleAndAdd(curve, privateKey.getFpElement().getX(), sikeParam.getQB(), sikeParam.getBitsB() - 1);
        s = montgomery.xAdd(curve, sikeParam.getPB(), s);
        EvaluatedCurve evaluatedCurve = isogeny.iso3e(curve, s, sikeParam.getPA(), sikeParam.getQA());
        return createPublicKey(sikeParam, evaluatedCurve);
    }

    /**
     * Create a public key from evaluated curve.
     * @param sikeParam SIKE parameters.
     * @param evaluatedCurve Evaluated curve.
     * @return Public key.
     */
    private SidhPublicKey createPublicKey(SikeParam sikeParam, EvaluatedCurve evaluatedCurve) {
        MontgomeryAffine montgomery = (MontgomeryAffine) sikeParam.getMontgomery();
        Fp2Point p = evaluatedCurve.getP();
        Fp2Point q = evaluatedCurve.getQ();
        Fp2Point r = montgomery.getXr(evaluatedCurve.getCurve(), p, q);

        Fp2Element px = new Fp2ElementRef(sikeParam, p.getX().getX0(), p.getX().getX1());
        Fp2Element qx = new Fp2ElementRef(sikeParam, q.getX().getX0(), q.getX().getX1());
        Fp2Element rx = new Fp2ElementRef(sikeParam, r.getX().getX0(), r.getX().getX1());
        return new SidhPublicKey(sikeParam, px, qx, rx);
    }

    @Override
    public Fp2Element isoEx2(SikeParam sikeParam, byte[] sk2, Fp2Element p2, Fp2Element q2, Fp2Element r2) {
        MontgomeryAffine montgomery = (MontgomeryAffine) sikeParam.getMontgomery();
        EvaluatedCurve iso = montgomery.getYpYqAB(sikeParam, p2, q2, r2);
        MontgomeryCurve curve = iso.getCurve();
        BigInteger m = ByteEncoding.fromByteArray(sk2);
        Fp2Point s = montgomery.doubleAndAdd(curve, m, iso.getQ(), sikeParam.getBitsA());
        s = montgomery.xAdd(curve, iso.getP(), s);
        EvaluatedCurve iso2 = iso2e(curve, s);
        return montgomery.jInv(iso2.getCurve());
    }

    @Override
    public Fp2Element isoEx3(SikeParam sikeParam, byte[] sk3, Fp2Element p3, Fp2Element q3, Fp2Element r3) {
        MontgomeryAffine montgomery = (MontgomeryAffine) sikeParam.getMontgomery();
        EvaluatedCurve iso = montgomery.getYpYqAB(sikeParam, p3, q3, r3);
        MontgomeryCurve curve = iso.getCurve();
        BigInteger m = ByteEncoding.fromByteArray(sk3);
        Fp2Point s = montgomery.doubleAndAdd(curve, m, iso.getQ(), sikeParam.getBitsB() - 1);
        s = montgomery.xAdd(curve, iso.getP(), s);
        EvaluatedCurve iso3 = iso3e(curve, s);
        return montgomery.jInv(iso3.getCurve());
    }

}
