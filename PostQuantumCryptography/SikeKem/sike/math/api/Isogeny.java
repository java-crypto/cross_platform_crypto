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

import sike.model.EvaluatedCurve;
import sike.model.MontgomeryCurve;
import sike.model.SidhPrivateKey;
import sike.model.SidhPublicKey;
import sike.param.SikeParam;

/**
 * Elliptic curve isogeny operations on Montgomery curves.
 *
 * @author Roman Strobl, roman.strobl@wultra.com
 */
public interface Isogeny {

    /**
     * Compute a 2-isogenous curve.
     * @param curve Current curve.
     * @param p2    Generator point with order equal to 2 on current curve.
     * @return A 2-isogenous curve.
     */
    MontgomeryCurve curve2Iso(MontgomeryCurve curve, Fp2Point p2);

    /**
     * Compute a 3-isogenous curve.
     * @param curve Current curve.
     * @param p3 Generator point with order equal to 3 on current curve.
     * @return A 3-isogenous curve.
     */
    MontgomeryCurve curve3Iso(MontgomeryCurve curve, Fp2Point p3);

    /**
     * Compute a 4-isogenous curve.
     * @param curve Current curve.
     * @param p4 Generator point with order equal to 4 on current curve.
     * @return A 4-isogenous curve.
     */
    MontgomeryCurve curve4Iso(MontgomeryCurve curve, Fp2Point p4);

    /**
     * Evaluate a 2-isogeny at a point.
     * @param q Point to be evaluated.
     * @param p2 Generator point with order equal to 2 on current curve.
     * @return Evaluated point.
     */
    Fp2Point eval2Iso(Fp2Point q, Fp2Point p2);

    /**
     * Evaluate a 3-isogeny at a point.
     * @param curve Current curve.
     * @param q Point to be evaluated.
     * @param p3 Generator point with order equal to 3 on current curve.
     * @return Evaluated point.
     */
    Fp2Point eval3Iso(MontgomeryCurve curve, Fp2Point q, Fp2Point p3);

    /**
     * Evaluate a 4-isogeny at a point.
     * @param curve Current curve.
     * @param q Point to be evaluated.
     * @param p4 Generator point with order equal to 4 on current curve.
     * @return Evaluated point.
     */
    Fp2Point eval4Iso(MontgomeryCurve curve, Fp2Point q, Fp2Point p4);

    /**
     * Compute a 2^eA-isogeny and evaluate points p and q on this isogeny.
     * @param curve Current curve.
     * @param s Generator point with order equal to 2^eA on current curve.
     * @param points Points to be evaluated on computed isogeny (optional).
     * @return Curve corresponding to a 2^eA-isogeny.
     */
    EvaluatedCurve iso2e(MontgomeryCurve curve, Fp2Point s, Fp2Point... points);

    /**
     * Compute a 3^eB-isogeny and evaluate points p and q on this isogeny.
     * @param curve Current curve.
     * @param s Generator point with order equal to 3^eB on current curve.
     * @param points Points to be evaluated on computed isogeny (optional).
     * @return Curve corresponding to a 3^eB-isogeny.
     */
    EvaluatedCurve iso3e(MontgomeryCurve curve, Fp2Point s, Fp2Point... points);

    /**
     * Derive a public key from a private key for Alice.
     * @param curve Starting curve.
     * @param privateKey Private key.
     * @return Derived public key.
     */
    SidhPublicKey isoGen2(MontgomeryCurve curve, SidhPrivateKey privateKey);

    /**
     * Derive a public key from a private key for Bob.
     * @param curve Starting curve.
     * @param privateKey Private key.
     * @return Derived public key.
     */
    SidhPublicKey isoGen3(MontgomeryCurve curve, SidhPrivateKey privateKey);

    /**
     * Compute a shared secret isogeny j-invariant in the 2-torsion.
     * @param sikeParam SIKE parameters.
     * @param sk2 Private key.
     * @param p2 The x coordinate of public point P.
     * @param q2 The x coordinate of public point Q.
     * @param r2 The x coordinate of public point R.
     * @return Shared secret isogeny j-invariant.
     */
    Fp2Element isoEx2(SikeParam sikeParam, byte[] sk2, Fp2Element p2, Fp2Element q2, Fp2Element r2);

    /**
     * Compute a shared secret isogeny j-invariant in the 3-torsion.
     * @param sikeParam SIKE parameters.
     * @param sk3 Private key.
     * @param p3 The x coordinate of public point P.
     * @param q3 The x coordinate of public point Q.
     * @param r3 The x coordinate of public point R.
     * @return Shared secret isogeny j-invariant.
     */
    Fp2Element isoEx3(SikeParam sikeParam, byte[] sk3, Fp2Element p3, Fp2Element q3, Fp2Element r3);

}
