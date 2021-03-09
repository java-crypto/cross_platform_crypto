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
package sike.model.optimized;

import sike.math.api.Fp2Element;
import sike.param.SikeParam;

/**
 * Montgomery curve constants for optimization in projective coordinates.
 *
 * @author Roman Strobl, roman.strobl@wultra.com
 */
public class MontgomeryConstants {

    private Fp2Element c;
    private Fp2Element a24plus;
    private Fp2Element a24minus;
    private Fp2Element c24;
    private Fp2Element k1;
    private Fp2Element k2;
    private Fp2Element k3;

    /**
     * Default optimized montgomery constants constructor.
     * @param sikeParam SIKE parameters.
     */
    public MontgomeryConstants(SikeParam sikeParam) {
        c = sikeParam.getFp2ElementFactory().one();
    }

    /**
     * Optimized montgomery constants constructor with calculation of constants.
     * @param sikeParam SIKE parameters.
     * @param a Montgomery curve coefficient a.
     */
    public MontgomeryConstants(SikeParam sikeParam, Fp2Element a) {
        this(sikeParam);
        Fp2Element t1 = c.add(c);
        this.a24plus = a.add(t1);
        this.a24minus = a.subtract(t1);
        this.c24 = t1.add(t1);
    }

    /**
     * Get Montgomery curve constant c.
     * @return Montgomery constant c.
     */
    public Fp2Element getC() {
        return c;
    }

    /**
     * Set Montgomery curve constant c.
     * @param c Montgomery curve constant c.
     */
    public void setC(Fp2Element c) {
        this.c = c;
    }

    /**
     * Get Montgomery curve constant a24+.
     * @return Montgomery curve constant a24+.
     */
    public Fp2Element getA24plus() {
        return a24plus;
    }

    /**
     * Set Montgomery curve constant a24+.
     * @param a24plus Montgomery curve constant a24+.
     */
    public void setA24plus(Fp2Element a24plus) {
        this.a24plus = a24plus;
    }

    /**
     * Get Montgomery curve constant a24-.
     * @return Montgomery curve constant a24-.
     */
    public Fp2Element getA24minus() {
        return a24minus;
    }

    /**
     * Set Montgomery curve constant a24-.
     * @param a24minus Montgomery curve constant a24-.
     */
    public void setA24minus(Fp2Element a24minus) {
        this.a24minus = a24minus;
    }

    /**
     * Get Montgomery curve constant c24.
     * @return Montgomery curve constant c24.
     */
    public Fp2Element getC24() {
        return c24;
    }

    /**
     * Set Montgomery curve constant c24.
     * @param c24 Montgomery curve constant c24.
     */
    public void setC24(Fp2Element c24) {
        this.c24 = c24;
    }

    /**
     * Get Montgomery curve constant K1.
     * @return Montgomery curve constant K1.
     */
    public Fp2Element getK1() {
        return k1;
    }

    /**
     * Set Montgomery curve constant K2.
     * @param k1 Montgomery curve constant K2.
     */
    public void setK1(Fp2Element k1) {
        this.k1 = k1;
    }

    /**
     * Get Montgomery curve constant K2.
     * @return Montgomery curve constant K2.
     */
    public Fp2Element getK2() {
        return k2;
    }

    /**
     * Set Montgomery curve constant K2.
     * @param k2 Montgomery curve constant K2.
     */
    public void setK2(Fp2Element k2) {
        this.k2 = k2;
    }

    /**
     * Get Montgomery curve constant K3.
     * @return Montgomery curve constant K3.
     */
    public Fp2Element getK3() {
        return k3;
    }

    /**
     * Set Montgomery curve constant K3.
     * @param k3 Montgomery curve constant K3.
     */
    public void setK3(Fp2Element k3) {
        this.k3 = k3;
    }

    @Override
    public String toString() {
        return "MontgomeryConstants{" +
                "c=" + c +
                ", a24plus=" + a24plus +
                ", a24minus=" + a24minus +
                ", c24=" + c24 +
                ", k1=" + k1 +
                ", k2=" + k2 +
                ", k3=" + k3 +
                '}';
    }
}
