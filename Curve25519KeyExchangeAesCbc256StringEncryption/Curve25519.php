<?php

//namespace Curve25519;
// https://github.com/lt/PHP-Curve25519
class Curve25519
{
    private $zero = [0,0,0,0, 0,0,0,0, 0,0];
    private $one  = [1,0,0,0, 0,0,0,0, 0,0];
    private $nine = [9,0,0,0, 0,0,0,0, 0,0];

    function add($a, $b) {
        return [
            $a[0] + $b[0], $a[1] + $b[1], $a[2] + $b[2], $a[3] + $b[3], $a[4] + $b[4],
            $a[5] + $b[5], $a[6] + $b[6], $a[7] + $b[7], $a[8] + $b[8], $a[9] + $b[9]
        ];
    }

    function sub($a, $b) {
        $r = [
            ($c = 0x7ffffda + $a[0] - $b[0]             ) & 0x3ffffff,
            ($c = 0x3fffffe + $a[1] - $b[1] + ($c >> 26)) & 0x1ffffff,
            ($c = 0x7fffffe + $a[2] - $b[2] + ($c >> 25)) & 0x3ffffff,
            ($c = 0x3fffffe + $a[3] - $b[3] + ($c >> 26)) & 0x1ffffff,
            ($c = 0x7fffffe + $a[4] - $b[4] + ($c >> 25)) & 0x3ffffff,
            ($c = 0x3fffffe + $a[5] - $b[5] + ($c >> 26)) & 0x1ffffff,
            ($c = 0x7fffffe + $a[6] - $b[6] + ($c >> 25)) & 0x3ffffff,
            ($c = 0x3fffffe + $a[7] - $b[7] + ($c >> 26)) & 0x1ffffff,
            ($c = 0x7fffffe + $a[8] - $b[8] + ($c >> 25)) & 0x3ffffff,
            ($c = 0x3fffffe + $a[9] - $b[9] + ($c >> 26)) & 0x1ffffff,
        ];

        $r[0] += 19 * ($c >> 25);

        return $r;
    }

    function mul($a, $b) {
        list($s0, $s1, $s2, $s3, $s4, $s5, $s6, $s7, $s8, $s9) = $a;
        list($r0, $r1, $r2, $r3, $r4, $r5, $r6, $r7, $r8, $r9) = $b;

        $r1_2 = $r1 * 2;
        $r3_2 = $r3 * 2;
        $r5_2 = $r5 * 2;

        $r2_19 = $r2 * 19;
        $r4_19 = $r4 * 19;
        $r5_19 = $r5 * 19;
        $r6_19 = $r6 * 19;
        $r7_19 = $r7 * 19;
        $r8_19 = $r8 * 19;
        $r9_19 = $r9 * 19;

        $r3_38 = $r3 * 38;
        $r5_38 = $r5 * 38;
        $r7_38 = $r7 * 38;
        $r9_38 = $r9 * 38;

        $r = [
            ($c = $m0 = ($r0 * $s0) + ($r1 * 38 * $s9) + ($r2_19 * $s8) + ($r3_38   * $s7) + ($r4_19 * $s6) + ($r5_38 * $s5) + ($r6_19 * $s4) + ($r7_38  * $s3) + ($r8_19 * $s2) + ($r9_38 * $s1)             ) & 0x3ffffff,
            ($c = $m1 = ($r0 * $s1) + ($r1      * $s0) + ($r2_19 * $s9) + ($r3 * 19 * $s8) + ($r4_19 * $s7) + ($r5_19 * $s6) + ($r6_19 * $s5) + ($r7_19  * $s4) + ($r8_19 * $s3) + ($r9_19 * $s2) + ($c >> 26)) & 0x1ffffff,
            ($c = $m2 = ($r0 * $s2) + ($r1_2    * $s1) + ($r2    * $s0) + ($r3_38   * $s9) + ($r4_19 * $s8) + ($r5_38 * $s7) + ($r6_19 * $s6) + ($r7_38  * $s5) + ($r8_19 * $s4) + ($r9_38 * $s3) + ($c >> 25)) & 0x3ffffff,
            ($c = $m3 = ($r0 * $s3) + ($r1      * $s2) + ($r2    * $s1) + ($r3      * $s0) + ($r4_19 * $s9) + ($r5_19 * $s8) + ($r6_19 * $s7) + ($r7_19  * $s6) + ($r8_19 * $s5) + ($r9_19 * $s4) + ($c >> 26)) & 0x1ffffff,
            ($c = $m4 = ($r0 * $s4) + ($r1_2    * $s3) + ($r2    * $s2) + ($r3_2    * $s1) + ($r4    * $s0) + ($r5_38 * $s9) + ($r6_19 * $s8) + ($r7_38  * $s7) + ($r8_19 * $s6) + ($r9_38 * $s5) + ($c >> 25)) & 0x3ffffff,
            ($c = $m5 = ($r0 * $s5) + ($r1      * $s4) + ($r2    * $s3) + ($r3      * $s2) + ($r4    * $s1) + ($r5    * $s0) + ($r6_19 * $s9) + ($r7_19  * $s8) + ($r8_19 * $s7) + ($r9_19 * $s6) + ($c >> 26)) & 0x1ffffff,
            ($c = $m6 = ($r0 * $s6) + ($r1_2    * $s5) + ($r2    * $s4) + ($r3_2    * $s3) + ($r4    * $s2) + ($r5_2  * $s1) + ($r6    * $s0) + ($r7_38  * $s9) + ($r8_19 * $s8) + ($r9_38 * $s7) + ($c >> 25)) & 0x3ffffff,
            ($c = $m7 = ($r0 * $s7) + ($r1      * $s6) + ($r2    * $s5) + ($r3      * $s4) + ($r4    * $s3) + ($r5    * $s2) + ($r6    * $s1) + ($r7     * $s0) + ($r8_19 * $s9) + ($r9_19 * $s8) + ($c >> 26)) & 0x1ffffff,
            ($c = $m8 = ($r0 * $s8) + ($r1_2    * $s7) + ($r2    * $s6) + ($r3_2    * $s5) + ($r4    * $s4) + ($r5_2  * $s3) + ($r6    * $s2) + ($r7 * 2 * $s1) + ($r8    * $s0) + ($r9_38 * $s9) + ($c >> 25)) & 0x3ffffff,
            ($c = $m9 = ($r0 * $s9) + ($r1      * $s8) + ($r2    * $s7) + ($r3      * $s6) + ($r4    * $s5) + ($r5    * $s4) + ($r6    * $s3) + ($r7     * $s2) + ($r8    * $s1) + ($r9    * $s0) + ($c >> 26)) & 0x1ffffff
        ];
        $r[0] += ($c = 19 * ($c >> 25)) & 0x3ffffff;
        $r[1] +=            ($c >> 26);

        return $r;
    }

    function sqr($a, $n = 1) {
        list($r0, $r1, $r2, $r3, $r4, $r5, $r6, $r7, $r8, $r9) = $a;

        do {
            $r0_2 = $r0 * 2;
            $r1_2 = $r1 * 2;
            $r2_2 = $r2 * 2;
            $r3_2 = $r3 * 2;
            $r4_2 = $r4 * 2;
            $r5_2 = $r5 * 2;
            $r7_2 = $r7 * 2;

            $r6_19 = $r6 * 19;
            $r7_38 = $r7 * 38;
            $r8_19 = $r8 * 19;
            $r9_38 = $r9 * 38;

            $s0 = ($r0   * $r0) + ($r5 * $r5 * 38) + ($r6_19 * $r4_2) + ($r7_38 * $r3_2) + ($r8_19 * $r2_2) + ($r9_38 * $r1_2);
            $s1 = ($r0_2 * $r1)                    + ($r6_19 * $r5_2) + ($r7_38 * $r4  ) + ($r8_19 * $r3_2) + ($r9_38 * $r2  );
            $s2 = ($r0_2 * $r2) + ($r1_2 * $r1)    + ($r6_19 * $r6  ) + ($r7_38 * $r5_2) + ($r8_19 * $r4_2) + ($r9_38 * $r3_2);
            $s3 = ($r0_2 * $r3) + ($r1_2 * $r2)                       + ($r7_38 * $r6  ) + ($r8_19 * $r5_2) + ($r9_38 * $r4  );
            $s4 = ($r0_2 * $r4) + ($r1_2 * $r3_2) + ($r2   * $r2)     + ($r7_38 * $r7  ) + ($r8_19 * $r6*2) + ($r9_38 * $r5_2);
            $s5 = ($r0_2 * $r5) + ($r1_2 * $r4  ) + ($r2_2 * $r3)                        + ($r8_19 * $r7_2) + ($r9_38 * $r6  );
            $s6 = ($r0_2 * $r6) + ($r1_2 * $r5_2) + ($r2_2 * $r4) + ($r3_2 * $r3)        + ($r8_19 * $r8  ) + ($r9_38 * $r7_2);
            $s7 = ($r0_2 * $r7) + ($r1_2 * $r6  ) + ($r2_2 * $r5) + ($r3_2 * $r4  )                         + ($r9_38 * $r8  );
            $s8 = ($r0_2 * $r8) + ($r1_2 * $r7_2) + ($r2_2 * $r6) + ($r3_2 * $r5_2) + ($r4 * $r4  )         + ($r9_38 * $r9  );
            $s9 = ($r0_2 * $r9) + ($r1_2 * $r8  ) + ($r2_2 * $r7) + ($r3_2 * $r6  ) + ($r4 * $r5_2);

            $r0 = ($c = $s0             ) & 0x3ffffff;
            $r1 = ($c = $s1 + ($c >> 26)) & 0x1ffffff;
            $r2 = ($c = $s2 + ($c >> 25)) & 0x3ffffff;
            $r3 = ($c = $s3 + ($c >> 26)) & 0x1ffffff;
            $r4 = ($c = $s4 + ($c >> 25)) & 0x3ffffff;
            $r5 = ($c = $s5 + ($c >> 26)) & 0x1ffffff;
            $r6 = ($c = $s6 + ($c >> 25)) & 0x3ffffff;
            $r7 = ($c = $s7 + ($c >> 26)) & 0x1ffffff;
            $r8 = ($c = $s8 + ($c >> 25)) & 0x3ffffff;
            $r9 = ($c = $s9 + ($c >> 26)) & 0x1ffffff;

            $r0 += ($c = 19 * ($c >> 25)) & 0x3ffffff;
            $r1 +=            ($c >> 26);
        } while (--$n);

        return [$r0, $r1, $r2, $r3, $r4, $r5, $r6, $r7, $r8, $r9];
    }

    function mul121665($in) {
        $r = [
            ($c = $in[0] * 121665             ) & 0x3ffffff,
            ($c = $in[1] * 121665 + ($c >> 26)) & 0x1ffffff,
            ($c = $in[2] * 121665 + ($c >> 25)) & 0x3ffffff,
            ($c = $in[3] * 121665 + ($c >> 26)) & 0x1ffffff,
            ($c = $in[4] * 121665 + ($c >> 25)) & 0x3ffffff,
            ($c = $in[5] * 121665 + ($c >> 26)) & 0x1ffffff,
            ($c = $in[6] * 121665 + ($c >> 25)) & 0x3ffffff,
            ($c = $in[7] * 121665 + ($c >> 26)) & 0x1ffffff,
            ($c = $in[8] * 121665 + ($c >> 25)) & 0x3ffffff,
            ($c = $in[9] * 121665 + ($c >> 26)) & 0x1ffffff,
        ];

        $r[0] += 19 * ($c >> 25);

        return $r;
    }

    function scalarmult($f, $c) {
        $t = $this->one;
        $u = $this->zero;
        $v = $this->one;
        $w = $c;

        $swapBit = 1;

        $i = 254;
        while ($i --> 2) {
            $x = $this->add($w, $v);
            $v = $this->sub($w, $v);
            $y = $this->add($t, $u);
            $u = $this->sub($t, $u);
            $t = $this->mul($y, $v);
            $u = $this->mul($x, $u);
            $z = $this->add($t, $u);
            $u = $this->sqr($this->sub($t, $u));
            $t = $this->sqr($z);
            $u = $this->mul($u, $c);
            $x = $this->sqr($x);
            $v = $this->sqr($v);
            $w = $this->mul($x, $v);
            $v = $this->sub($x, $v);

            $v = $this->mul($v, $this->add($this->mul121665($v), $x));

            $b = ($f[$i >> 3] >> ($i & 7)) & 1;
            // Constant time (i.e. branchless) swap.
            list($w, $t, $v, $u) = [[$w, $t, $v, $u], [$t, $w, $u, $v]][$b ^ $swapBit];
            $swapBit = $b;
        }

        $i = 3;
        while ($i--) {
            $x = $this->sqr($this->add($w, $v));
            $v = $this->sqr($this->sub($w, $v));
            $w = $this->mul($x, $v);
            $v = $this->sub($x, $v);
            $v = $this->mul($v, $this->add($this->mul121665($v), $x));
        }

        $a = $this->sqr($v);
        $b = $this->mul($this->sqr($a, 2), $v);
        $a = $this->mul($b, $a);
        $b = $this->mul($this->sqr($a), $b);
        $b = $this->mul($this->sqr($b, 5), $b);
        $c = $this->mul($this->sqr($b, 10), $b);
        $b = $this->mul($this->sqr($this->mul($this->sqr($c, 20), $c), 10), $b);
        $c = $this->mul($this->sqr($b, 50), $b);

        $r = $this->mul($w, $this->mul($this->sqr($this->mul($this->sqr($this->mul($this->sqr($c, 100), $c), 50), $b), 5), $a));

        $r = [
            ($c = $r[0] + 0x4000000             ) & 0x3ffffff,
            ($c = $r[1] + 0x1ffffff + ($c >> 26)) & 0x1ffffff,
            ($c = $r[2] + 0x3ffffff + ($c >> 25)) & 0x3ffffff,
            ($c = $r[3] + 0x1ffffff + ($c >> 26)) & 0x1ffffff,
            ($c = $r[4] + 0x3ffffff + ($c >> 25)) & 0x3ffffff,
            ($c = $r[5] + 0x1ffffff + ($c >> 26)) & 0x1ffffff,
            ($c = $r[6] + 0x3ffffff + ($c >> 25)) & 0x3ffffff,
            ($c = $r[7] + 0x1ffffff + ($c >> 26)) & 0x1ffffff,
            ($c = $r[8] + 0x3ffffff + ($c >> 25)) & 0x3ffffff,
            ($c = $r[9] + 0x1ffffff + ($c >> 26)) & 0x1ffffff
        ];

        return pack('V8',
             $r[0]        | ($r[1] << 26),
            ($r[1] >>  6) | ($r[2] << 19),
            ($r[2] >> 13) | ($r[3] << 13),
            ($r[3] >> 19) | ($r[4] <<  6),
             $r[5]        | ($r[6] << 25),
            ($r[6] >>  7) | ($r[7] << 19),
            ($r[7] >> 13) | ($r[8] << 12),
            ($r[8] >> 20) | ($r[9] <<  6)
        );
    }

    function clamp($secret)
    {
        $e = array_values(unpack('C32', $secret));

        $e[0]  &= 0xf8;
        $e[31] &= 0x7f;
        $e[31] |= 0x40;

        return $e;
    }

    function publicKey($secret)
    {
        if (!is_string($secret) || strlen($secret) !== 32) {
            throw new \InvalidArgumentException('Secret must be a 32 byte string');
        }

        return $this->scalarmult($this->clamp($secret), $this->nine);
    }

    function sharedKey($secret, $public)
    {
        if (!is_string($secret) || strlen($secret) !== 32) {
            throw new \InvalidArgumentException('Secret must be a 32 byte string');
        }

        if (!is_string($public) || strlen($public) !== 32) {
            throw new \InvalidArgumentException('Public must be a 32 byte string');
        }

        $w = unpack('V8', $public);

        $r = [
              $w[1]                         & 0x3ffffff, // 26
            (($w[1] >> 26) | ($w[2] <<  6)) & 0x1ffffff, // 25 - 51
            (($w[2] >> 19) | ($w[3] << 13)) & 0x3ffffff, // 26 - 77
            (($w[3] >> 13) | ($w[4] << 19)) & 0x1ffffff, // 25 - 102
             ($w[4] >>  6)                  & 0x3ffffff, // 26 - 128
              $w[5]                         & 0x1ffffff, // 25 - 153
            (($w[5] >> 25) | ($w[6] <<  7)) & 0x3ffffff, // 26 - 179
            (($w[6] >> 19) | ($w[7] << 13)) & 0x1ffffff, // 25 - 204
            (($w[7] >> 12) | ($w[8] << 20)) & 0x3ffffff, // 26 - 230
             ($w[8] >> 6)                   & 0x1ffffff, // 25 - 255
        ];

        return $this->scalarmult($this->clamp($secret), $r);
    }
}
