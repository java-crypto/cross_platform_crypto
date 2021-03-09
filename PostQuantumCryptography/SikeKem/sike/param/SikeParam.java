/*
 * Copyright 2020 Wultra s.r.o.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General License for more details.
 *
 * You should have received a copy of the GNU Affero General License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package sike.param;

import sike.math.api.*;
import sike.math.optimized.fp.FpElementOpti;
import sike.model.ImplementationType;

import java.math.BigInteger;

/**
 * SIKE parameters.
 *
 * @author Roman Strobl, roman.strobl@wultra.com
 */
public interface SikeParam {

    /**
     * Get implementation type.
     * @return Implementation type.
     */
    ImplementationType getImplementationType();

    /**
     * Factory for Fp2Elements.
     * @return Factory for Fp2Elements.
     */
    Fp2ElementFactory getFp2ElementFactory();

    /**
     * Get Montgomery curve math algorithms.
     * @return Montgomery curve math algorithms.
     */
    Montgomery getMontgomery();

    /**
     * Get Isogeny curve algorithms.
     * @return Isogeny curve algorithms.
     */
    Isogeny getIsogeny();

    /**
     * Get SIKE variant name.
     * @return SIKE variant name.
     */
    String getName();

    /**
     * Get Montgomery coefficient a for starting curve.
     * @return Montgomery coefficient a for starting curve.
     */
    Fp2Element getA();

    /**
     * Get Montgomery coefficient b for starting curve.
     * @return Montgomery coefficient b for starting curve.
     */
    Fp2Element getB();

    /**
     * Get parameter eA.
     * @return Parameter eA.
     */
    int getEA();

    /**
     * Get parameter eB.
     * @return Parameter eB.
     */
    int getEB();

    /**
     * Get factor of A.
     * @return Factor of A.
     */
    BigInteger getOrdA();

    /**
     * Get factor of B.
     * @return Factor of b.
     */
    BigInteger getOrdB();

    /**
     * Get most significant bit of A.
     * @return Most significant bit of A.
     */
    int getBitsA();

    /**
     * Get most significant bit of B.
     * @return Most significant bit of B.
     */
    int getBitsB();

    /**
     * Get mask used for key generation of A.
     * @return Mask used for key generation of A.
     */
    byte getMaskA();

    /**
     * Get mask used for key generation of B.
     * @return Mask used for key generation of B.
     */
    byte getMaskB();

    /**
     * Get field prime.
     * @return Field prime.
     */
    BigInteger getPrime();

    /**
     * Get point PA.
     * @return point PA.
     */
    Fp2Point getPA();

    /**
     * Get point QA.
     * @return point QA.
     */
    Fp2Point getQA();

    /**
     * Get point RA.
     * @return point RA.
     */
    Fp2Point getRA();

    /**
     * Get point PB.
     * @return point PB.
     */
    Fp2Point getPB();

    /**
     * Get point QB.
     * @return point QB.
     */
    Fp2Point getQB();

    /**
     * Get point RB.
     * @return point RB.
     */
    Fp2Point getRB();

    /**
     * Get the number of bytes used for cryptography operations.
     * @return Number of bytes used for cryptography operations.
     */
    int getCryptoBytes();

    /**
     * Get the number of bytes used for message operations.
     * @return Number of bytes used for message operations.
     */
    int getMessageBytes();

    /**
     * Get number of rows for optimized tree computations in the 2-isogeny graph.
     * @return Number of rows for optimized tree computations in the 2-isogeny graph.
     */
    int getTreeRowsA();

    /**
     * Get number of rows for optimized tree computations in the 3-isogeny graph.
     * @return Number of rows for optimized tree computations in the 3-isogeny graph.
     */
    int getTreeRowsB();

    /**
     * Get maximum number of points for optimized tree computations in the 2-isogeny graph.
     * @return Maxim number of points for optimized tree computations in the 2-isogeny graph.
     */
    int getTreePointsA();

    /**
     * Get maximum number of points for optimized tree computations in the 3-isogeny graph.
     * @return Maxim number of points for optimized tree computations in the 3-isogeny graph.
     */
    int getTreePointsB();

    /**
     * Get optimization strategy for tree computations in the 2-isogeny graph.
     * @return Optimization strategy for tree computations in the 2-isogeny graph.
     */
    int[] getStrategyA();

    /**
     * Get optimization strategy for tree computations in the 3-isogeny graph.
     * @return Optimization strategy for tree computations in the 3-isogeny graph.
     */
    int[] getStrategyB();

    /**
     * Get size of long array for optimized elements.
     * @return Size of long array.
     */
    int getFpWords();

    /**
     * Get number of 0 digits in the least significant part of p + 1.
     * @return Number of 0 digits in the least significant part of p + 1.
     */
    int getZeroWords();

    /**
     * Get optimized field prime p.
     * @return Field prime p.
     */
    FpElementOpti getP();

    /**
     * Get optimized value p + 1.
     * @return Value p + 1.
     */
    FpElementOpti getP1();

    /**
     * Get optimized value p * 2.
     * @return Value p * 2.
     */
    FpElementOpti getPx2();

    /**
     * Get optimized value pR2.
     * @return Optimized value pR2.
     */
    FpElementOpti getPR2();

    /**
     * Get the power strategy for the p34 algorithm.
     * @return Power strategy.
     */
    int[] getPowStrategy();

    /**
     * Get the multiplication strategy for the p34 algorithm.
     * @return Multiplication strategy.
     */
    int[] getMulStrategy();

    /**
     * Get initial multiplication value for the p34 algorithm.
     * @return Initial multiplication value for the p34 algorithm
     */
    int getInitialMul();

}
