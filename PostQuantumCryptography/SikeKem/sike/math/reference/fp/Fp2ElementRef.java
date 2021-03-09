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
package sike.math.reference.fp;

import sike.math.api.Fp2Element;
import sike.math.api.FpElement;
import sike.param.SikeParam;

import java.math.BigInteger;
import java.util.Objects;

/**
 * Element of a quadratic extension field F(p^2): x0 + x1*i.
 *
 * @author Roman Strobl, roman.strobl@wultra.com
 */
public class Fp2ElementRef implements Fp2Element {

    private final FpElement x0;
    private final FpElement x1;

    private final SikeParam sikeParam;

    /**
     * The F(p^2) field element constructor for given F(p) elements.
     * @param sikeParam SIKE parameters.
     * @param x0 The x0 real F(p) element.
     * @param x1 The x1 imaginary F(p) element.
     */
    public Fp2ElementRef(SikeParam sikeParam, FpElement x0, FpElement x1) {
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
    public Fp2ElementRef(SikeParam sikeParam, BigInteger x0b, BigInteger x1b) {
        this.sikeParam = sikeParam;
        this.x0 = new FpElementRef(sikeParam, x0b);
        this.x1 = new FpElementRef(sikeParam, x1b);
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
        return new Fp2ElementRef(sikeParam, r, i);
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
        return new Fp2ElementRef(sikeParam, r, i);
    }

    /**
     * Multiply two elements.
     * @param y Other element.
     * @return Calculation result.
     */
    public Fp2Element multiply(Fp2Element y) {
        // y = (x0 + i*x1) * (y0 + i*y1) = x0y0 - x1y1 + i*(x0y1 + x1y0)
        FpElement r1, r2, r, i1, i2, i;

        r1 = x0.multiply(y.getX0());
        r2 = x1.multiply(y.getX1());
        r = r1.subtract(r2);

        i1 = x0.multiply(y.getX1());
        i2 = x1.multiply(y.getX0());
        i = i1.add(i2);

        return new Fp2ElementRef(sikeParam, r, i);
    }

    /**
     * Multiply by the imaginary part of the element.
     * @return Calculation result.
     */
    public Fp2Element multiplyByI() {
        return new Fp2ElementRef(sikeParam, x1.negate(), x0.copy());
    }

    /**
     * Square the element.
     * @return Calculation result.
     */
    public Fp2Element square() {
        return multiply(this);
    }

    /**
     * Element exponentiation.
     * @param n Exponent
     * @return Calculation result.
     */
    public Fp2Element pow(BigInteger n) {
        if (n.compareTo(BigInteger.ZERO) < 0) {
            throw new ArithmeticException("Negative exponent");
        }
        if (n.compareTo(BigInteger.ZERO) == 0) {
            return sikeParam.getFp2ElementFactory().one();
        }
        if (n.compareTo(BigInteger.ONE) == 0) {
            return copy();
        }
        BigInteger e = n;
        Fp2Element base = copy();
        Fp2Element result = sikeParam.getFp2ElementFactory().one();
        while (e.compareTo(BigInteger.ZERO) > 0) {
            if (e.testBit(0)) {
                result = result.multiply(base);
            }
            e = e.shiftRight(1);
            base = base.square();
        }
        return result;
    }

    /**
     * Calculate the square root of the element.
     * @return Calculation result.
     */
    public Fp2Element sqrt() {
        // TODO - compare performance with reference C implementation, consider replacing algorithm
        if (isZero()) {
            return sikeParam.getFp2ElementFactory().zero();
        }
        if (!isQuadraticResidue()) {
            throw new ArithmeticException("The square root of a quadratic non-residue cannot be computed");
        }
        BigInteger prime = sikeParam.getPrime();
        if (prime.mod(new BigInteger("4")).compareTo(new BigInteger("3")) != 0) {
            throw new ArithmeticException("Field prime mod 4 is not 3");
        }
        Fp2Element a1, a2;
        Fp2Element neg1 = sikeParam.getFp2ElementFactory().one();
        BigInteger p = prime;
        p = p.shiftRight(2);
        a1 = copy();
        a1 = a1.pow(p);
        a2 = copy();
        a2 = a2.multiply(a1);
        a1 = a1.multiply(a2);
        if (a1.equals(neg1)) {
            return a2.multiplyByI();
        }
        p = prime;
        p = p.shiftRight(1);
        a1 = a1.add(sikeParam.getFp2ElementFactory().one());
        a1 = a1.pow(p);
        return a1.multiply(a2);
    }

    /**
     * Get whether the element is a quadratic residue modulo prime.
     * @return Whether the element is a quadratic residue.
     */
    public boolean isQuadraticResidue() {
        Fp2Element base = copy();
        BigInteger p = sikeParam.getPrime();
        p = p.multiply(p);
        p = p.subtract(BigInteger.ONE);
        p = p.shiftRight(1);
        base = base.pow(p);
        return base.equals(sikeParam.getFp2ElementFactory().one());
    }

    /**
     * Invert the element.
     * @return Calculation result.
     */
    public Fp2ElementRef inverse() {
        FpElement t0, t1, o0, o1;
        t0 = x0.square();
        t1 = x1.square();
        t0 = t0.add(t1);
        t0 = t0.inverse();
        o1 = x1.negate();
        o0 = x0.multiply(t0);
        o1 = o1.multiply(t0);
        return new Fp2ElementRef(sikeParam, o0, o1);
    }

    /**
     * Negate the element.
     * @return Calculation result.
     */
    public Fp2Element negate() {
        return new Fp2ElementRef(sikeParam, x0.negate(), x1.negate());
    }

    /**
     * Get whether the element is the zero element.
     * @return Whether the element is the zero element.
     */
    public boolean isZero() {
        return x0.isZero() && x1.isZero();
    }

    /**
     * Copy the element.
     * @return Element copy.
     */
    public Fp2Element copy() {
        return new Fp2ElementRef(sikeParam, new FpElementRef(sikeParam, x0.getX()), new FpElementRef(sikeParam, x1.getX()));
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
        return x1 + "i" + " + " + x0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fp2ElementRef that = (Fp2ElementRef) o;
        return sikeParam.getPrime().equals(that.sikeParam.getPrime())
                && x0.equals(that.x0)
                && x1.equals(that.x1);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sikeParam, x0, x1);
    }
}
