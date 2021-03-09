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
package sike.math.optimized.fp;

import sike.math.api.Fp2Element;
import sike.math.api.FpElement;
import sike.param.SikeParam;
import sike.util.SideChannelUtil;

import java.math.BigInteger;
import java.util.Objects;

/**
 * Element of a quadratic extension field F(p^2): x0 + x1*i.
 *
 * @author Roman Strobl, roman.strobl@wultra.com
 */
public class Fp2ElementOpti implements Fp2Element {

    private final FpElement x0;
    private final FpElement x1;

    private final SikeParam sikeParam;

    /**
     * The F(p^2) field element constructor for given F(p) elements.
     * @param sikeParam SIKE parameters.
     * @param x0 The x0 real F(p) element.
     * @param x1 The x1 imaginary F(p) element.
     */
    public Fp2ElementOpti(SikeParam sikeParam, FpElement x0, FpElement x1) {
        this.sikeParam = sikeParam;
        this.x0 = x0.copy();
        this.x1 = x1.copy();
    }

    /**
     * The F(p^2) field element constructor for given BigInteger values.
     * @param sikeParam SIKE parameters.
     * @param x0b The x0 real F(p) element.
     * @param x1b The x1 imaginary F(p) element.
     */
    public Fp2ElementOpti(SikeParam sikeParam, BigInteger x0b, BigInteger x1b) {
        this.sikeParam = sikeParam;
        this.x0 = new FpElementOpti(sikeParam, x0b);
        this.x1 = new FpElementOpti(sikeParam, x1b);
    }

    /**
     * Get the real part of element.
     * @return Real part of element.
     */
    public FpElement getX0() {
        return x0;
    }

    /**
     * Get the imaginary part of element.
     * @return Imaginary part of element.
     */
    public FpElement getX1() {
        return x1;
    }

    /**
     * Add two elements.
     * @param y Other element.
     * @return Calculation result.
     */
    public Fp2Element add(Fp2Element y) {
        // y = (x0 + i*x1) + (y0 + i*y1) = x0 + y0 + i*(x1 + y1)
        FpElement r, i;

        r = x0.add(y.getX0());
        i = x1.add(y.getX1());
        return new Fp2ElementOpti(sikeParam, r, i);
    }

    /**
     * Subtract two elements.
     * @param y Other element.
     * @return Calculation result.
     */
    public Fp2Element subtract(Fp2Element y) {
        // y = (x0 + i*x1) - (y0 + i*y1) = x0 - y0 + i*(x1 - y1)
        FpElement r, i;

        r = x0.subtract(y.getX0());
        i = x1.subtract(y.getX1());
        return new Fp2ElementOpti(sikeParam, r, i);
    }

    /**
     * Multiply two elements.
     * @param y Other element.
     * @return Calculation result.
     */
    public Fp2Element multiply(Fp2Element y) {
        FpElement a = x0;
        FpElement b = x1;
        FpElement c = y.getX0();
        FpElement d = y.getX1();

        // (a + bi) * (c + di) = (a * c - b * d) + (a * d + b * c)i

        FpElement ac = a.multiply(c);
        FpElement bd = b.multiply(d);

        FpElement bMinusA = b.subtract(a);
        FpElement cMinusD = c.subtract(d);

        FpElementOpti adPlusBC = (FpElementOpti) bMinusA.multiply(cMinusD);
        adPlusBC = (FpElementOpti) adPlusBC.addNoReduction(ac);
        adPlusBC = (FpElementOpti) adPlusBC.addNoReduction(bd);

        // x1 = (a * d + b * c) * R mod p
        FpElementOpti x1o = adPlusBC.reduceMontgomery();

        FpElementOpti acMinusBd = ((FpElementOpti) ac).subtractNoReduction(bd);
        FpElementOpti x0o = acMinusBd.reduceMontgomery();

        // x0 = (a * c - b * d) * R mod p
        return new Fp2ElementOpti(sikeParam, x0o, x1o);
    }

    @Override
    public Fp2Element multiplyByI() {
        return new Fp2ElementOpti(sikeParam, x1.negate(), x0.copy());
    }

    /**
     * Square the element.
     * @return Calculation result.
     */
    public Fp2ElementOpti square() {
        FpElement a = x0;
        FpElement b = x1;

        // (a + bi) * (a + bi) = (a^2 - b^2) + (2ab)i.
        FpElement a2 = a.add(a);
        FpElement aPlusB = a.add(b);
        FpElement aMinusB = a.subtract(b);
        FpElement a2MinB2 = aPlusB.multiply(aMinusB);
        FpElement ab2 = a2.multiply(b);

        // (a^2 - b^2) * R mod p
        FpElementOpti x0o = ((FpElementOpti) a2MinB2).reduceMontgomery();

        // 2 * a * b * R mod p
        FpElementOpti x1o = ((FpElementOpti) ab2).reduceMontgomery();

        return new Fp2ElementOpti(sikeParam, x0o, x1o);
    }

    @Override
    public Fp2Element pow(BigInteger n) {
        throw new IllegalStateException("Not implemented yet");
    }

    /**
     * Calculate the square root of the element.
     * @return Calculation result.
     */
    public Fp2ElementOpti sqrt() {
        throw new IllegalStateException("Not implemented yet");
    }

    /**
     * Invert the element.
     * @return Calculation result.
     */
    public Fp2ElementOpti inverse() {
        FpElementOpti e1 = (FpElementOpti) x0.multiply(x0);
        FpElementOpti e2 = (FpElementOpti) x1.multiply(x1);
        e1 = (FpElementOpti) e1.addNoReduction(e2);
        // (a^2 + b^2) * R mod p
        FpElementOpti f1 = e1.reduceMontgomery();

        FpElementOpti f2 = (FpElementOpti) f1.multiplyMontgomery(f1);
        f2 = p34(f2);
        f2 = (FpElementOpti) f2.multiplyMontgomery(f2);
        f2 = (FpElementOpti) f2.multiplyMontgomery(f1);

        e1 = (FpElementOpti) x0.multiply(f2);
        FpElementOpti x0o = e1.reduceMontgomery();

        f1 = (FpElementOpti) new FpElementOpti(sikeParam).subtract(x1);
        e1 = (FpElementOpti) f1.multiply(f2);
        FpElementOpti x1o = e1.reduceMontgomery();

        return new Fp2ElementOpti(sikeParam, x0o, x1o);
    }

    @Override
    public Fp2Element negate() {
        return new Fp2ElementOpti(sikeParam, x1.negate(), x0.copy());
    }

    /**
     * Compute x ^ ((p - 3) / 4).
     * @param x Value x.
     * @return Computed value.
     */
    private FpElementOpti p34(FpElementOpti x) {
        FpElementOpti[] lookup = new FpElementOpti[16];
        int[] powStrategy = sikeParam.getPowStrategy();
        int[] mulStrategy = sikeParam.getMulStrategy();
        int initialMul = sikeParam.getInitialMul();
        FpElementOpti xSquare = (FpElementOpti) x.multiplyMontgomery(x);
        lookup[0] = x.copy();
        for (int i = 1; i < 16; i++) {
            lookup[i] = (FpElementOpti) lookup[i - 1].multiplyMontgomery(xSquare);
        }
        FpElementOpti dest = lookup[initialMul];
        for (int i = 0; i < powStrategy.length; i++) {
            dest = (FpElementOpti) dest.multiplyMontgomery(dest);
            for (int j = 1; j < powStrategy[i]; j++) {
                dest = (FpElementOpti) dest.multiplyMontgomery(dest);
            }
            dest = (FpElementOpti) dest.multiplyMontgomery(lookup[mulStrategy[i]]);
        }
        return dest;
    }

    @Override
    public boolean isZero() {
        return x0.isZero() && x1.isZero();
    }

    /**
     * Copy the element.
     * @return Element copy.
     */
    public Fp2ElementOpti copy() {
        return new Fp2ElementOpti(sikeParam, x0.copy(), x1.copy());
    }

    /**
     * Encode the element in bytes.
     * @return Encoded element in bytes.
     */
    public byte[] getEncoded() {
        byte[] x0Encoded = x0.getEncoded();
        byte[] x1Encoded = x1.getEncoded();
        byte[] encoded = new byte[x0Encoded.length + x1Encoded.length];
        System.arraycopy(x0Encoded, 0, encoded, 0, x0Encoded.length);
        System.arraycopy(x1Encoded, 0, encoded, x0Encoded.length, x1Encoded.length);
        return encoded;
    }

    /**
     * Convert element to octet string.
     * @return Octet string.
     */
    public String toOctetString() {
        return x0.toOctetString() + x1.toOctetString();
    }

    @Override
    public String toString() {
        return x1.getX() + "i" + " + " + x0.getX();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fp2ElementOpti that = (Fp2ElementOpti) o;
        // Use constant time comparison to avoid timing attacks
        return sikeParam.equals(that.sikeParam)
                && SideChannelUtil.constantTimeAreEqual(getEncoded(), that.getEncoded());
    }

    @Override
    public int hashCode() {
        return Objects.hash(sikeParam, x0, x1);
    }
}
