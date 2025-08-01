package me.levyuri.anticheatbase.utils.fastmath;

public final class FastMath {

    /**
     * Archimede's constant PI, ratio of circle circumference to diameter.
     */
    public static final double PI = 105414357.0 / 33554432.0 + 1.984187159361080883e-9;

    /**
     * Napier's constant e, base of the natural logarithm.
     */
    public static final double E = 2850325.0 / 1048576.0 + 8.254840070411028747e-8;

    /**
     * Index of exp(0) in the array of integer exponentials.
     */
    static final int EXP_INT_TABLE_MAX_INDEX = 750;
    /**
     * Length of the array of integer exponentials.
     */
    static final int EXP_INT_TABLE_LEN = EXP_INT_TABLE_MAX_INDEX * 2;
    /**
     * Logarithm table length.
     */
    static final int LN_MANT_LEN = 1024;
    /** Exponential fractions table length. */
    static final int EXP_FRAC_TABLE_LEN = 1025; // 0, 1/1024, ... 1024/1024

    /** StrictFastMath.log(Double.MAX_VALUE): {@value} */
    private static final double LOG_MAX_VALUE = StrictMath.log(Double.MAX_VALUE);

    /** log(2) (high bits). */
    private static final double LN_2_A = 0.693147063255310059;

    /** log(2) (low bits). */
    private static final double LN_2_B = 1.17304635250823482e-7;

    /** Coefficients for log, when input 0.99 < x < 1.01. */
    private static final double[][] LN_QUICK_COEF = {
            {1.0, 5.669184079525E-24},
            {-0.25, -0.25},
            {0.3333333134651184, 1.986821492305628E-8},
            {-0.25, -6.663542893624021E-14},
            {0.19999998807907104, 1.1921056801463227E-8},
            {-0.1666666567325592, -7.800414592973399E-9},
            {0.1428571343421936, 5.650007086920087E-9},
            {-0.12502530217170715, -7.44321345601866E-11},
            {0.11113807559013367, 9.219544613762692E-9},
    };

    /** Coefficients for log in the range of 1.0 < x < 1.0 + 2^-10. */
    private static final double[][] LN_HI_PREC_COEF = {
            {1.0, -6.032174644509064E-23},
            {-0.25, -0.25},
            {0.3333333134651184, 1.9868161777724352E-8},
            {-0.2499999701976776, -2.957007209750105E-8},
            {0.19999954104423523, 1.5830993332061267E-10},
            {-0.16624879837036133, -2.6033824355191673E-8}
    };

    /** Sine, Cosine, Tangent tables are for 0, 1/8, 2/8, ... 13/8 = PI/2 approx. */
    private static final int SINE_TABLE_LEN = 14;

    /** Sine table (high bits). */
    private static final double[] SINE_TABLE_A =
            {
                    +0.0d,
                    +0.1246747374534607d,
                    +0.24740394949913025d,
                    +0.366272509098053d,
                    +0.4794255495071411d,
                    +0.5850973129272461d,
                    +0.6816387176513672d,
                    +0.7675435543060303d,
                    +0.8414709568023682d,
                    +0.902267575263977d,
                    +0.9489846229553223d,
                    +0.9808930158615112d,
                    +0.9974949359893799d,
                    +0.9985313415527344d,
            };
    /** Sine table (low bits). */
    private static final double[] SINE_TABLE_B =
            {
                    +0.0d,
                    -4.068233003401932E-9d,
                    +9.755392680573412E-9d,
                    +1.9987994582857286E-8d,
                    -1.0902938113007961E-8d,
                    -3.9986783938944604E-8d,
                    +4.23719669792332E-8d,
                    -5.207000323380292E-8d,
                    +2.800552834259E-8d,
                    +1.883511811213715E-8d,
                    -3.5997360512765566E-9d,
                    +4.116164446561962E-8d,
                    +5.0614674548127384E-8d,
                    -1.0129027912496858E-9d,
            };
    /** Cosine table (high bits). */
    private static final double[] COSINE_TABLE_A =
            {
                    +1.0d,
                    +0.9921976327896118d,
                    +0.9689123630523682d,
                    +0.9305076599121094d,
                    +0.8775825500488281d,
                    +0.8109631538391113d,
                    +0.7316888570785522d,
                    +0.6409968137741089d,
                    +0.5403022766113281d,
                    +0.4311765432357788d,
                    +0.3153223395347595d,
                    +0.19454771280288696d,
                    +0.07073719799518585d,
                    -0.05417713522911072d,
            };
    /** Cosine table (low bits). */
    private static final double[] COSINE_TABLE_B =
            {
                    +0.0d,
                    +3.4439717236742845E-8d,
                    +5.865827662008209E-8d,
                    -3.7999795083850525E-8d,
                    +1.184154459111628E-8d,
                    -3.43338934259355E-8d,
                    +1.1795268640216787E-8d,
                    +4.438921624363781E-8d,
                    +2.925681159240093E-8d,
                    -2.6437112632041807E-8d,
                    +2.2860509143963117E-8d,
                    -4.813899778443457E-9d,
                    +3.6725170580355583E-9d,
                    +2.0217439756338078E-10d,
            };
    /** Tangent table, used by atan() (high bits). */
    private static final double[] TANGENT_TABLE_A =
            {
                    +0.0d,
                    +0.1256551444530487d,
                    +0.25534194707870483d,
                    +0.3936265707015991d,
                    +0.5463024377822876d,
                    +0.7214844226837158d,
                    +0.9315965175628662d,
                    +1.1974215507507324d,
                    +1.5574076175689697d,
                    +2.092571258544922d,
                    +3.0095696449279785d,
                    +5.041914939880371d,
                    +14.101419448852539d,
                    -18.430862426757812d,
            };
    /** Tangent table, used by atan() (low bits). */
    private static final double[] TANGENT_TABLE_B =
            {
                    +0.0d,
                    -7.877917738262007E-9d,
                    -2.5857668567479893E-8d,
                    +5.2240336371356666E-9d,
                    +5.206150291559893E-8d,
                    +1.8307188599677033E-8d,
                    -5.7618793749770706E-8d,
                    +7.848361555046424E-8d,
                    +1.0708593250394448E-7d,
                    +1.7827257129423813E-8d,
                    +2.893485277253286E-8d,
                    +3.1660099222737955E-7d,
                    +4.983191803254889E-7d,
                    -3.356118100840571E-7d,
            };
    /** Bits of 1/(2*pi), need for reducePayneHanek(). */
    private static final long[] RECIP_2PI = new long[]{
            (0x28be60dbL << 32) | 0x9391054aL,
            (0x7f09d5f4L << 32) | 0x7d4d3770L,
            (0x36d8a566L << 32) | 0x4f10e410L,
            (0x7f9458eaL << 32) | 0xf7aef158L,
            (0x6dc91b8eL << 32) | 0x909374b8L,
            (0x01924bbaL << 32) | 0x82746487L,
            (0x3f877ac7L << 32) | 0x2c4a69cfL,
            (0xba208d7dL << 32) | 0x4baed121L,
            (0x3a671c09L << 32) | 0xad17df90L,
            (0x4e64758eL << 32) | 0x60d4ce7dL,
            (0x272117e2L << 32) | 0xef7e4a0eL,
            (0xc7fe25ffL << 32) | 0xf7816603L,
            (0xfbcbc462L << 32) | 0xd6829b47L,
            (0xdb4d9fb3L << 32) | 0xc9f2c26dL,
            (0xd3d18fd9L << 32) | 0xa797fa8bL,
            (0x5d49eeb1L << 32) | 0xfaf97c5eL,
            (0xcf41ce7dL << 32) | 0xe294a4baL,
            0x9afed7ecL << 32};
    /** Bits of pi/4, need for reducePayneHanek(). */
    private static final long[] PI_O_4_BITS = new long[]{
            (0xc90fdaa2L << 32) | 0x2168c234L,
            (0xc4c6628bL << 32) | 0x80dc1cd1L};
    /** Eighths.
     * This is used by sinQ, because its faster to do a table lookup than
     * a multiply in this time-critical routine
     */
    private static final double[] EIGHTHS = {0, 0.125, 0.25, 0.375, 0.5, 0.625, 0.75, 0.875, 1.0, 1.125, 1.25, 1.375, 1.5, 1.625};
    /** Table of 2^((n+2)/3) */
    private static final double[] CBRTTWO = {0.6299605249474366,
            0.7937005259840998,
            1.0,
            1.2599210498948732,
            1.5874010519681994};
    /**
     * 0x40000000 - used to split a double into two parts, both with the low order bits cleared.
     * Equivalent to 2^30.
     */
    private static final long HEX_40000000 = 0x40000000L; // 1073741824L

    /*
     *  There are 52 bits in the mantissa of a double.
     *  For additional precision, the code splits double numbers into two parts,
     *  by clearing the low order 30 bits if possible, and then performs the arithmetic
     *  on each half separately.
     */
    /** Mask used to clear low order 30 bits */
    private static final long MASK_30BITS = -1L - (HEX_40000000 - 1); // 0xFFFFFFFFC0000000L;
    /** Mask used to clear the non-sign part of an int. */
    private static final int MASK_NON_SIGN_INT = 0x7fffffff;
    /** Mask used to clear the non-sign part of a long. */
    private static final long MASK_NON_SIGN_LONG = 0x7fffffffffffffffL;
    /** Mask used to extract exponent from double bits. */
    private static final long MASK_DOUBLE_EXPONENT = 0x7ff0000000000000L;
    /** Mask used to extract mantissa from double bits. */
    private static final long MASK_DOUBLE_MANTISSA = 0x000fffffffffffffL;
    /** Mask used to add implicit high order bit for normalized double. */
    private static final long IMPLICIT_HIGH_BIT = 0x0010000000000000L;
    /** 2^52 - double numbers this large must be integral (no fraction) or NaN or Infinite */
    private static final double TWO_POWER_52 = 4503599627370496.0;
    /** Constant: {@value}. */
    private static final double F_1_3 = 1d / 3d;
    /** Constant: {@value}. */
    private static final double F_1_5 = 1d / 5d;
    /** Constant: {@value}. */
    private static final double F_1_7 = 1d / 7d;
    /** Constant: {@value}. */
    private static final double F_1_9 = 1d / 9d;
    /** Constant: {@value}. */
    private static final double F_1_11 = 1d / 11d;
    /** Constant: {@value}. */
    private static final double F_1_13 = 1d / 13d;
    /** Constant: {@value}. */
    private static final double F_1_15 = 1d / 15d;
    /** Constant: {@value}. */
    private static final double F_1_17 = 1d / 17d;
    /** Constant: {@value}. */
    private static final double F_3_4 = 3d / 4d;
    /** Constant: {@value}. */
    private static final double F_15_16 = 15d / 16d;
    /** Constant: {@value}. */
    private static final double F_13_14 = 13d / 14d;
    /** Constant: {@value}. */
    private static final double F_11_12 = 11d / 12d;
    /** Constant: {@value}. */
    private static final double F_9_10 = 9d / 10d;
    /** Constant: {@value}. */
    private static final double F_7_8 = 7d / 8d;
    /** Constant: {@value}. */
    private static final double F_5_6 = 5d / 6d;
    /** Constant: {@value}. */
    private static final double F_1_2 = 1d / 2d;
    /** Constant: {@value}. */
    private static final double F_1_4 = 1d / 4d;

    /**
     * Private Constructor
     */
    private FastMath() {
    }

    /**
     * Get the high order bits from the mantissa.
     * Equivalent to adding and subtracting HEX_40000 but also works for very large numbers
     *
     * @param d the value to split
     * @return the high order part of the mantissa
     */
    private static double doubleHighPart(double d) {
        long xl = Double.doubleToRawLongBits(d); // can take raw bits because just gonna convert it back
        xl &= MASK_30BITS; // Drop low order bits
        return Double.longBitsToDouble(xl);
    }

    static {
        exp(5);
        sin(5);
        log(5);
        atan2(5, 5);
    }

    /**
     * Compute the square root of x number.
     *
     * @param x number on which evaluation is done
     * @return square root of x. If the argument is NaN or less than zero, the result is NaN.
     */
    public static double sqrt(final double x) {
        if (Double.isNaN(x) || x < 0.0D) {
            return Double.NaN;
        }

        double t;

        double squareRoot = x / 2;

        if (x != 0.0D) {

            do {

                t = squareRoot;

                squareRoot = (t + (x / t)) / 2;

            } while ((t - squareRoot) != 0D);
        }

        return squareRoot;
    }

    /** Compute the hyperbolic cosine of a number.
     * @param x number on which evaluation is done
     * @return hyperbolic cosine of x
     */
    public static double cosh(double x) {
        if (Double.isNaN(x)) {
            return x;
        }

        // cosh[z] = (exp(z) + exp(-z))/2

        // for numbers with magnitude 20 or so,
        // exp(-z) can be ignored in comparison with exp(z)

        if (x > 20) {
            if (x >= LOG_MAX_VALUE) {
                // Avoid overflow (MATH-905).
                final double t = exp(0.5 * x);
                return (0.5 * t) * t;
            } else {
                return 0.5 * exp(x);
            }
        } else if (x < -20) {
            if (x <= -LOG_MAX_VALUE) {
                // Avoid overflow (MATH-905).
                final double t = exp(-0.5 * x);
                return (0.5 * t) * t;
            } else {
                return 0.5 * exp(-x);
            }
        }

        final double[] hiPrec = new double[2];
        if (x < 0.0) {
            x = -x;
        }
        exp(x, 0.0, hiPrec);

        double ya = hiPrec[0] + hiPrec[1];
        double yb = -(ya - hiPrec[0] - hiPrec[1]);

        double temp = ya * HEX_40000000;
        double yaa = ya + temp - temp;
        double yab = ya - yaa;

        // recip = 1/y
        double recip = 1.0 / ya;
        temp = recip * HEX_40000000;
        double recipa = recip + temp - temp;
        double recipb = recip - recipa;

        // Correct for rounding in division
        recipb += (1.0 - yaa * recipa - yaa * recipb - yab * recipa - yab * recipb) * recip;
        // Account for yb
        recipb += -yb * recip * recip;

        // y = y + 1/y
        temp = ya + recipa;
        yb += -(temp - ya - recipa);
        ya = temp;
        temp = ya + recipb;
        yb += -(temp - ya - recipb);
        ya = temp;

        double result = ya + yb;
        result *= 0.5;
        return result;
    }

    /** Compute the hyperbolic sine of a number.
     * @param x number on which evaluation is done
     * @return hyperbolic sine of x
     */
    public static double sinh(double x) {
        boolean negate = false;
        if (Double.isNaN(x)) {
            return x;
        }

        // sinh[z] = (exp(z) - exp(-z) / 2

        // for values of z larger than about 20,
        // exp(-z) can be ignored in comparison with exp(z)

        if (x > 20) {
            if (x >= LOG_MAX_VALUE) {
                // Avoid overflow (MATH-905).
                final double t = exp(0.5 * x);
                return (0.5 * t) * t;
            } else {
                return 0.5 * exp(x);
            }
        } else if (x < -20) {
            if (x <= -LOG_MAX_VALUE) {
                // Avoid overflow (MATH-905).
                final double t = exp(-0.5 * x);
                return (-0.5 * t) * t;
            } else {
                return -0.5 * exp(-x);
            }
        }

        if (x == 0) {
            return x;
        }

        if (x < 0.0) {
            x = -x;
            negate = true;
        }

        double result;

        double[] hiPrec = new double[2];
        if (x > 0.25) {
            exp(x, 0.0, hiPrec);

            double ya = hiPrec[0] + hiPrec[1];
            double yb = -(ya - hiPrec[0] - hiPrec[1]);

            double temp = ya * HEX_40000000;
            double yaa = ya + temp - temp;
            double yab = ya - yaa;

            // recip = 1/y
            double recip = 1.0 / ya;
            temp = recip * HEX_40000000;
            double recipa = recip + temp - temp;
            double recipb = recip - recipa;

            // Correct for rounding in division
            recipb += (1.0 - yaa * recipa - yaa * recipb - yab * recipa - yab * recipb) * recip;
            // Account for yb
            recipb += -yb * recip * recip;

            recipa = -recipa;
            recipb = -recipb;

            // y = y + 1/y
            temp = ya + recipa;
            yb += -(temp - ya - recipa);
            ya = temp;
            temp = ya + recipb;
            yb += -(temp - ya - recipb);
            ya = temp;

            result = ya + yb;
        } else {
            expm1(x, hiPrec);

            double ya = hiPrec[0] + hiPrec[1];
            double yb = -(ya - hiPrec[0] - hiPrec[1]);

            /* Compute expm1(-x) = -expm1(x) / (expm1(x) + 1) */
            double denom = 1.0 + ya;
            double denomr = 1.0 / denom;
            double denomb = -(denom - 1.0 - ya) + yb;
            double ratio = ya * denomr;
            double temp = ratio * HEX_40000000;
            double ra = ratio + temp - temp;
            double rb = ratio - ra;

            temp = denom * HEX_40000000;
            double za = denom + temp - temp;
            double zb = denom - za;

            rb += (ya - za * ra - za * rb - zb * ra - zb * rb) * denomr;

            // Adjust for yb
            rb += yb * denomr;                        // numerator
            rb += -ya * denomb * denomr * denomr;   // denominator

            // y = y - 1/y
            temp = ya + ra;
            yb += -(temp - ya - ra);
            ya = temp;
            temp = ya + rb;
            yb += -(temp - ya - rb);
            ya = temp;

            result = ya + yb;
        }
        result *= 0.5;

        if (negate) {
            result = -result;
        }

        return result;
    }

    /** Compute the hyperbolic tangent of a number.
     * @param x number on which evaluation is done
     * @return hyperbolic tangent of x
     */
    public static double tanh(double x) {
        boolean negate = false;

        if (Double.isNaN(x)) {
            return x;
        }

        // tanh[z] = sinh[z] / cosh[z]
        // = (exp(z) - exp(-z)) / (exp(z) + exp(-z))
        // = (exp(2x) - 1) / (exp(2x) + 1)

        // for magnitude > 20, sinh[z] == cosh[z] in double precision

        if (x > 20.0) {
            return 1.0;
        }

        if (x < -20) {
            return -1.0;
        }

        if (x == 0) {
            return x;
        }

        if (x < 0.0) {
            x = -x;
            negate = true;
        }

        double result;
        double[] hiPrec = new double[2];
        if (x >= 0.5) {
            // tanh(x) = (exp(2x) - 1) / (exp(2x) + 1)
            exp(x * 2.0, 0.0, hiPrec);

            double ya = hiPrec[0] + hiPrec[1];
            double yb = -(ya - hiPrec[0] - hiPrec[1]);

            /* Numerator */
            double na = -1.0 + ya;
            double nb = -(na + 1.0 - ya);
            double temp = na + yb;
            nb += -(temp - na - yb);
            na = temp;

            /* Denominator */
            double da = 1.0 + ya;
            double db = -(da - 1.0 - ya);
            temp = da + yb;
            db += -(temp - da - yb);
            da = temp;

            temp = da * HEX_40000000;
            double daa = da + temp - temp;
            double dab = da - daa;

            // ratio = na/da
            double ratio = na / da;
            temp = ratio * HEX_40000000;
            double ratioa = ratio + temp - temp;
            double ratiob = ratio - ratioa;

            // Correct for rounding in division
            ratiob += (na - daa * ratioa - daa * ratiob - dab * ratioa - dab * ratiob) / da;

            // Account for nb
            ratiob += nb / da;
            // Account for db
            ratiob += -db * na / da / da;

            result = ratioa + ratiob;
        } else {
            // tanh(x) = expm1(2x) / (expm1(2x) + 2)
            expm1(x * 2.0, hiPrec);

            double ya = hiPrec[0] + hiPrec[1];
            double yb = -(ya - hiPrec[0] - hiPrec[1]);

            /* Numerator */

            /* Denominator */
            double da = 2.0 + ya;
            double db = -(da - 2.0 - ya);
            double temp = da + yb;
            db += -(temp - da - yb);
            da = temp;

            temp = da * HEX_40000000;
            double daa = da + temp - temp;
            double dab = da - daa;

            // ratio = na/da
            double ratio = ya / da;
            temp = ratio * HEX_40000000;
            double ratioa = ratio + temp - temp;
            double ratiob = ratio - ratioa;

            // Correct for rounding in division
            ratiob += (ya - daa * ratioa - daa * ratiob - dab * ratioa - dab * ratiob) / da;

            // Account for nb
            ratiob += yb / da;
            // Account for db
            ratiob += -db * ya / da / da;

            result = ratioa + ratiob;
        }

        if (negate) {
            result = -result;
        }

        return result;
    }

    /** Compute the inverse hyperbolic cosine of a number.
     * @param a number on which evaluation is done
     * @return inverse hyperbolic cosine of a
     */
    public static double acosh(final double a) {
        return FastMath.log(a + FastMath.sqrt(a * a - 1));
    }

    /** Compute the inverse hyperbolic sine of a number.
     * @param a number on which evaluation is done
     * @return inverse hyperbolic sine of a
     */
    public static double asinh(double a) {
        boolean negative = false;
        if (a < 0) {
            negative = true;
            a = -a;
        }

        double absAsinh;
        if (a > 0.167) {
            absAsinh = FastMath.log(FastMath.sqrt(a * a + 1) + a);
        } else {
            final double a2 = a * a;
            if (a > 0.097) {
                absAsinh = a * (1 - a2 * (F_1_3 - a2 * (F_1_5 - a2 * (F_1_7 - a2 * (F_1_9 - a2 * (F_1_11 - a2 * (F_1_13 - a2 * (F_1_15 - a2 * F_1_17 * F_15_16) * F_13_14) * F_11_12) * F_9_10) * F_7_8) * F_5_6) * F_3_4) * F_1_2);
            } else if (a > 0.036) {
                absAsinh = a * (1 - a2 * (F_1_3 - a2 * (F_1_5 - a2 * (F_1_7 - a2 * (F_1_9 - a2 * (F_1_11 - a2 * F_1_13 * F_11_12) * F_9_10) * F_7_8) * F_5_6) * F_3_4) * F_1_2);
            } else if (a > 0.0036) {
                absAsinh = a * (1 - a2 * (F_1_3 - a2 * (F_1_5 - a2 * (F_1_7 - a2 * F_1_9 * F_7_8) * F_5_6) * F_3_4) * F_1_2);
            } else {
                absAsinh = a * (1 - a2 * (F_1_3 - a2 * F_1_5 * F_3_4) * F_1_2);
            }
        }

        return negative ? -absAsinh : absAsinh;
    }

    /** Compute the inverse hyperbolic tangent of a number.
     * @param a number on which evaluation is done
     * @return inverse hyperbolic tangent of a
     */
    public static double atanh(double a) {
        boolean negative = false;
        if (a < 0) {
            negative = true;
            a = -a;
        }

        double absAtanh;
        if (a > 0.15) {
            absAtanh = 0.5 * FastMath.log((1 + a) / (1 - a));
        } else {
            final double a2 = a * a;
            if (a > 0.087) {
                absAtanh = a * (1 + a2 * (F_1_3 + a2 * (F_1_5 + a2 * (F_1_7 + a2 * (F_1_9 + a2 * (F_1_11 + a2 * (F_1_13 + a2 * (F_1_15 + a2 * F_1_17))))))));
            } else if (a > 0.031) {
                absAtanh = a * (1 + a2 * (F_1_3 + a2 * (F_1_5 + a2 * (F_1_7 + a2 * (F_1_9 + a2 * (F_1_11 + a2 * F_1_13))))));
            } else if (a > 0.003) {
                absAtanh = a * (1 + a2 * (F_1_3 + a2 * (F_1_5 + a2 * (F_1_7 + a2 * F_1_9))));
            } else {
                absAtanh = a * (1 + a2 * (F_1_3 + a2 * F_1_5));
            }
        }

        return negative ? -absAtanh : absAtanh;
    }

    /** Compute the signum of a number.
     * The signum is -1 for negative numbers, +1 for positive numbers and 0 otherwise
     * @param a number on which evaluation is done
     * @return -1.0, -0.0, +0.0, +1.0 or NaN depending on sign of a
     */
    public static double signum(final double a) {
        return (a < 0.0) ? -1.0 : ((a > 0.0) ? 1.0 : a); // return +0.0/-0.0/NaN depending on a
    }

    /** Compute the signum of a number.
     * The signum is -1 for negative numbers, +1 for positive numbers and 0 otherwise
     * @param a number on which evaluation is done
     * @return -1.0, -0.0, +0.0, +1.0 or NaN depending on sign of a
     */
    public static float signum(final float a) {
        return (a < 0.0f) ? -1.0f : ((a > 0.0f) ? 1.0f : a); // return +0.0/-0.0/NaN depending on a
    }

    /** Compute next number towards positive infinity.
     * @param a number to which neighbor should be computed
     * @return neighbor of a towards positive infinity
     */
    public static double nextUp(final double a) {
        return nextAfter(a, Double.POSITIVE_INFINITY);
    }

    /** Compute next number towards positive infinity.
     * @param a number to which neighbor should be computed
     * @return neighbor of a towards positive infinity
     */
    public static float nextUp(final float a) {
        return nextAfter(a, Float.POSITIVE_INFINITY);
    }

    /** Compute next number towards negative infinity.
     * @param a number to which neighbor should be computed
     * @return neighbor of a towards negative infinity
     * @since 3.4
     */
    public static double nextDown(final double a) {
        return nextAfter(a, Double.NEGATIVE_INFINITY);
    }

    /** Compute next number towards negative infinity.
     * @param a number to which neighbor should be computed
     * @return neighbor of a towards negative infinity
     * @since 3.4
     */
    public static float nextDown(final float a) {
        return nextAfter(a, Float.NEGATIVE_INFINITY);
    }

    /**
     * Exponential function.
     *
     * Computes exp(x), function result is nearly rounded.   It will be correctly
     * rounded to the theoretical value for 99.9% of input values, otherwise it will
     * have a 1 ULP error.
     *
     * Method:
     *    Lookup intVal = exp(int(x))
     *    Lookup fracVal = exp(int(x-int(x) / 1024.0) * 1024.0 );
     *    Compute z as the exponential of the remaining bits by a polynomial minus one
     *    exp(x) = intVal * fracVal * (1 + z)
     *
     * Accuracy:
     *    Calculation is done with 63 bits of precision, so result should be correctly
     *    rounded for 99.9% of input values, with less than 1 ULP error otherwise.
     *
     * @param x   a double
     * @return double e<sup>x</sup>
     */
    public static double exp(double x) {
        return exp(x, 0.0, null);
    }

    /**
     * Internal helper method for exponential function.
     * @param x original argument of the exponential function
     * @param extra extra bits of precision on input (To Be Confirmed)
     * @param hiPrec extra bits of precision on output (To Be Confirmed)
     * @return exp(x)
     */
    private static double exp(double x, double extra, double[] hiPrec) {
        double intPartA;
        double intPartB;
        int intVal = (int) x;

        /* Lookup exp(floor(x)).
         * intPartA will have the upper 22 bits, intPartB will have the lower
         * 52 bits.
         */
        if (x < 0.0) {

            // We don't check against intVal here as conversion of large negative double values
            // may be affected by a JIT bug. Subsequent comparisons can safely use intVal
            if (x < -746d) {
                if (hiPrec != null) {
                    hiPrec[0] = 0.0;
                    hiPrec[1] = 0.0;
                }
                return 0.0;
            }

            if (intVal < -709) {
                /* This will produce a subnormal output */
                final double result = exp(x + 40.19140625, extra, hiPrec) / 285040095144011776.0;
                if (hiPrec != null) {
                    hiPrec[0] /= 285040095144011776.0;
                    hiPrec[1] /= 285040095144011776.0;
                }
                return result;
            }

            if (intVal == -709) {
                /* exp(1.494140625) is nearly a machine number... */
                final double result = exp(x + 1.494140625, extra, hiPrec) / 4.455505956692756620;
                if (hiPrec != null) {
                    hiPrec[0] /= 4.455505956692756620;
                    hiPrec[1] /= 4.455505956692756620;
                }
                return result;
            }

            intVal--;

        } else {
            if (intVal > 709) {
                if (hiPrec != null) {
                    hiPrec[0] = Double.POSITIVE_INFINITY;
                    hiPrec[1] = 0.0;
                }
                return Double.POSITIVE_INFINITY;
            }

        }

        intPartA = ExpIntTable.EXP_INT_TABLE_A[EXP_INT_TABLE_MAX_INDEX + intVal];
        intPartB = ExpIntTable.EXP_INT_TABLE_B[EXP_INT_TABLE_MAX_INDEX + intVal];

        /* Get the fractional part of x, find the greatest multiple of 2^-10 less than
         * x and look up the exp function of it.
         * fracPartA will have the upper 22 bits, fracPartB the lower 52 bits.
         */
        final int intFrac = (int) ((x - intVal) * 1024.0);
        final double fracPartA = ExpFracTable.EXP_FRAC_TABLE_A[intFrac];
        final double fracPartB = ExpFracTable.EXP_FRAC_TABLE_B[intFrac];

        /* epsilon is the difference in x from the nearest multiple of 2^-10.  It
         * has a value in the range 0 <= epsilon < 2^-10.
         * Do the subtraction from x as the last step to avoid possible loss of precision.
         */
        final double epsilon = x - (intVal + intFrac / 1024.0);

        /* Compute z = exp(epsilon) - 1.0 via a minimax polynomial.  z has
       full double precision (52 bits).  Since z < 2^-10, we will have
       62 bits of precision when combined with the constant 1.  This will be
       used in the last addition below to get proper rounding. */

        /* Remez generated polynomial.  Converges on the interval [0, 2^-10], error
       is less than 0.5 ULP */
        double z = 0.04168701738764507;
        z = z * epsilon + 0.1666666505023083;
        z = z * epsilon + 0.5000000000042687;
        z = z * epsilon + 1.0;
        z = z * epsilon + -3.940510424527919E-20;

        /* Compute (intPartA+intPartB) * (fracPartA+fracPartB) by binomial
       expansion.
       tempA is exact since intPartA and intPartB only have 22 bits each.
       tempB will have 52 bits of precision.
         */
        double tempA = intPartA * fracPartA;
        double tempB = intPartA * fracPartB + intPartB * fracPartA + intPartB * fracPartB;

        /* Compute the result.  (1+z)(tempA+tempB).  Order of operations is
       important.  For accuracy add by increasing size.  tempA is exact and
       much larger than the others.  If there are extra bits specified from the
       pow() function, use them. */
        final double tempC = tempB + tempA;

        // If tempC is positive infinite, the evaluation below could result in NaN,
        // because z could be negative at the same time.
        if (tempC == Double.POSITIVE_INFINITY) {
            return Double.POSITIVE_INFINITY;
        }

        final double result;
        if (extra != 0.0) {
            result = tempC * extra * z + tempC * extra + tempC * z + tempB + tempA;
        } else {
            result = tempC * z + tempB + tempA;
        }

        if (hiPrec != null) {
            // If requesting high precision
            hiPrec[0] = tempA;
            hiPrec[1] = tempC * extra * z + tempC * extra + tempC * z + tempB;
        }

        return result;
    }

    /**
     * Compute exp(x) - 1
     *
     * @param x number to compute shifted exponential
     * @return exp(x) - 1
     */
    public static double expm1(double x) {
        return expm1(x, null);
    }

    /**
     * Internal helper method for expm1
     *
     * @param x         number to compute shifted exponential
     * @param hiPrecOut receive high precision result for -1.0 < x < 1.0
     * @return exp(x) - 1
     */
    private static double expm1(double x, double[] hiPrecOut) {
        if (Double.isNaN(x) || x == 0.0) { // NaN or zero
            return x;
        }

        if (x <= -1.0 || x >= 1.0) {
            // If not between +/- 1.0
            //return exp(x) - 1.0;
            double[] hiPrec = new double[2];
            exp(x, 0.0, hiPrec);
            if (x > 0.0) {
                return -1.0 + hiPrec[0] + hiPrec[1];
            } else {
                final double ra = -1.0 + hiPrec[0];
                double rb = -(ra + 1.0 - hiPrec[0]);
                rb += hiPrec[1];
                return ra + rb;
            }
        }

        double baseA;
        double baseB;
        double epsilon;
        boolean negative = false;

        if (x < 0.0) {
            x = -x;
            negative = true;
        }

        {
            int intFrac = (int) (x * 1024.0);
            double tempA = ExpFracTable.EXP_FRAC_TABLE_A[intFrac] - 1.0;
            double tempB = ExpFracTable.EXP_FRAC_TABLE_B[intFrac];

            double temp = tempA + tempB;
            tempB = -(temp - tempA - tempB);
            tempA = temp;

            temp = tempA * HEX_40000000;
            baseA = tempA + temp - temp;
            baseB = tempB + (tempA - baseA);

            epsilon = x - intFrac / 1024.0;
        }


        /* Compute expm1(epsilon) */
        double zb = 0.008336750013465571;
        zb = zb * epsilon + 0.041666663879186654;
        zb = zb * epsilon + 0.16666666666745392;
        zb = zb * epsilon + 0.49999999999999994;
        zb *= epsilon;
        zb *= epsilon;

        double za = epsilon;
        double temp = za + zb;
        zb = -(temp - za - zb);
        za = temp;

        temp = za * HEX_40000000;
        temp = za + temp - temp;
        zb += za - temp;
        za = temp;

        /* Combine the parts.   expm1(a+b) = expm1(a) + expm1(b) + expm1(a)*expm1(b) */
        double ya = za * baseA;
        //double yb = za*baseB + zb*baseA + zb*baseB;
        temp = ya + za * baseB;
        double yb = -(temp - ya - za * baseB);
        ya = temp;

        temp = ya + zb * baseA;
        yb += -(temp - ya - zb * baseA);
        ya = temp;

        temp = ya + zb * baseB;
        yb += -(temp - ya - zb * baseB);
        ya = temp;

        //ya = ya + za + baseA;
        //yb = yb + zb + baseB;
        temp = ya + baseA;
        yb += -(temp - baseA - ya);
        ya = temp;

        temp = ya + za;
        //yb += (ya > za) ? -(temp - ya - za) : -(temp - za - ya);
        yb += -(temp - ya - za);
        ya = temp;

        temp = ya + baseB;
        //yb += (ya > baseB) ? -(temp - ya - baseB) : -(temp - baseB - ya);
        yb += -(temp - ya - baseB);
        ya = temp;

        temp = ya + zb;
        //yb += (ya > zb) ? -(temp - ya - zb) : -(temp - zb - ya);
        yb += -(temp - ya - zb);
        ya = temp;

        if (negative) {
            /* Compute expm1(-x) = -expm1(x) / (expm1(x) + 1) */
            double denom = 1.0 + ya;
            double denomr = 1.0 / denom;
            double denomb = -(denom - 1.0 - ya) + yb;
            double ratio = ya * denomr;
            temp = ratio * HEX_40000000;
            final double ra = ratio + temp - temp;
            double rb = ratio - ra;

            temp = denom * HEX_40000000;
            za = denom + temp - temp;
            zb = denom - za;

            rb += (ya - za * ra - za * rb - zb * ra - zb * rb) * denomr;

            // f(x) = x/1+x
            // Compute f'(x)
            // Product rule:  d(uv) = du*v + u*dv
            // Chain rule:  d(f(g(x)) = f'(g(x))*f(g'(x))
            // d(1/x) = -1/(x*x)
            // d(1/1+x) = -1/( (1+x)^2) *  1 =  -1/((1+x)*(1+x))
            // d(x/1+x) = -x/((1+x)(1+x)) + 1/1+x = 1 / ((1+x)(1+x))

            // Adjust for yb
            rb += yb * denomr;                      // numerator
            rb += -ya * denomb * denomr * denomr;   // denominator

            // negate
            ya = -ra;
            yb = -rb;
        }

        if (hiPrecOut != null) {
            hiPrecOut[0] = ya;
            hiPrecOut[1] = yb;
        }

        return ya + yb;
    }

    /**
     * Natural logarithm.
     *
     * @param x a double
     * @return log(x)
     */
    public static double log(final double x) {
        return log(x, null);
    }

    /**
     * Computes log(1 + x).
     *
     * @param x Number.
     * @return {@code log(1 + x)}.
     */
    public static double log1p(final double x) {
        if (x == -1) {
            return Double.NEGATIVE_INFINITY;
        }

        if (x == Double.POSITIVE_INFINITY) {
            return Double.POSITIVE_INFINITY;
        }

        if (x > 1e-6 ||
                x < -1e-6) {
            final double xpa = 1 + x;
            final double xpb = -(xpa - 1 - x);

            final double[] hiPrec = new double[2];
            final double lores = log(xpa, hiPrec);
            if (Double.isInfinite(lores)) { // Don't allow this to be converted to NaN
                return lores;
            }

            // Do a taylor series expansion around xpa:
            //   f(x+y) = f(x) + f'(x) y + f''(x)/2 y^2
            final double fx1 = xpb / xpa;
            final double epsilon = 0.5 * fx1 + 1;
            return epsilon * fx1 + hiPrec[1] + hiPrec[0];
        } else {
            // Value is small |x| < 1e6, do a Taylor series centered on 1.
            final double y = (x * F_1_3 - F_1_2) * x + 1;
            return y * x;
        }
    }

    /**
     * Internal helper method for natural logarithm function.
     *
     * @param x      original argument of the natural logarithm function
     * @param hiPrec extra bits of precision on output (To Be Confirmed)
     * @return log(x)
     */
    private static double log(final double x, final double[] hiPrec) {
        if (x == 0) { // Handle special case of +0/-0
            return Double.NEGATIVE_INFINITY;
        }
        long bits = Double.doubleToRawLongBits(x);

        /* Handle special cases of negative input, and NaN */
        if (((bits & 0x8000000000000000L) != 0 || Double.isNaN(x)) && x != 0.0) {
            if (hiPrec != null) {
                hiPrec[0] = Double.NaN;
            }

            return Double.NaN;
        }

        /* Handle special cases of Positive infinity. */
        if (x == Double.POSITIVE_INFINITY) {
            if (hiPrec != null) {
                hiPrec[0] = Double.POSITIVE_INFINITY;
            }

            return Double.POSITIVE_INFINITY;
        }

        /* Extract the exponent */
        int exp = (int) (bits >> 52) - 1023;

        if ((bits & 0x7ff0000000000000L) == 0) {
            // Subnormal!
            if (x == 0) {
                // Zero
                if (hiPrec != null) {
                    hiPrec[0] = Double.NEGATIVE_INFINITY;
                }

                return Double.NEGATIVE_INFINITY;
            }

            /* Normalize the subnormal number. */
            bits <<= 1;
            while ((bits & 0x0010000000000000L) == 0) {
                --exp;
                bits <<= 1;
            }
        }


        if ((exp == -1 || exp == 0) && x < 1.01 && x > 0.99 && hiPrec == null) {
            /* The normal method doesn't work well in the range [0.99, 1.01], so call do a straight
           polynomial expansion in higer precision. */

            /* Compute x - 1.0 and split it */
            double xa = x - 1.0;
            double xb = xa - x + 1.0;
            double tmp = xa * HEX_40000000;
            double aa = xa + tmp - tmp;
            double ab = xa - aa;
            xa = aa;
            xb = ab;

            final double[] lnCoef_last = LN_QUICK_COEF[LN_QUICK_COEF.length - 1];
            double ya = lnCoef_last[0];
            double yb = lnCoef_last[1];

            for (int i = LN_QUICK_COEF.length - 2; i >= 0; i--) {
                /* Multiply a = y * x */
                aa = ya * xa;
                ab = ya * xb + yb * xa + yb * xb;
                /* split, so now y = a */
                tmp = aa * HEX_40000000;
                ya = aa + tmp - tmp;
                yb = aa - ya + ab;

                /* Add  a = y + lnQuickCoef */
                final double[] lnCoef_i = LN_QUICK_COEF[i];
                aa = ya + lnCoef_i[0];
                ab = yb + lnCoef_i[1];
                /* Split y = a */
                tmp = aa * HEX_40000000;
                ya = aa + tmp - tmp;
                yb = aa - ya + ab;
            }

            /* Multiply a = y * x */
            aa = ya * xa;
            ab = ya * xb + yb * xa + yb * xb;
            /* split, so now y = a */
            tmp = aa * HEX_40000000;
            ya = aa + tmp - tmp;
            yb = aa - ya + ab;

            return ya + yb;
        }

        // lnm is a log of a number in the range of 1.0 - 2.0, so 0 <= lnm < ln(2)
        final double[] lnm = lnMant.LN_MANT[(int) ((bits & 0x000ffc0000000000L) >> 42)];

        // y is the most significant 10 bits of the mantissa
        //double y = Double.longBitsToDouble(bits & 0xfffffc0000000000L);
        //double epsilon = (x - y) / y;
        final double epsilon = (bits & 0x3ffffffffffL) / (TWO_POWER_52 + (bits & 0x000ffc0000000000L));

        double lnza = 0.0;
        double lnzb = 0.0;

        if (hiPrec != null) {
            /* split epsilon -> x */
            double tmp = epsilon * HEX_40000000;
            double aa = epsilon + tmp - tmp;
            double ab = epsilon - aa;
            double xa = aa;
            double xb = ab;

            /* Need a more accurate epsilon, so adjust the division. */
            final double numer = bits & 0x3ffffffffffL;
            final double denom = TWO_POWER_52 + (bits & 0x000ffc0000000000L);
            aa = numer - xa * denom - xb * denom;
            xb += aa / denom;

            /* Remez polynomial evaluation */
            final double[] lnCoef_last = LN_HI_PREC_COEF[LN_HI_PREC_COEF.length - 1];
            double ya = lnCoef_last[0];
            double yb = lnCoef_last[1];

            for (int i = LN_HI_PREC_COEF.length - 2; i >= 0; i--) {
                /* Multiply a = y * x */
                aa = ya * xa;
                ab = ya * xb + yb * xa + yb * xb;
                /* split, so now y = a */
                tmp = aa * HEX_40000000;
                ya = aa + tmp - tmp;
                yb = aa - ya + ab;

                /* Add  a = y + lnHiPrecCoef */
                final double[] lnCoef_i = LN_HI_PREC_COEF[i];
                aa = ya + lnCoef_i[0];
                ab = yb + lnCoef_i[1];
                /* Split y = a */
                tmp = aa * HEX_40000000;
                ya = aa + tmp - tmp;
                yb = aa - ya + ab;
            }

            /* Multiply a = y * x */
            aa = ya * xa;
            ab = ya * xb + yb * xa + yb * xb;

            /* split, so now lnz = a */
            /*
      tmp = aa * 1073741824.0;
      lnza = aa + tmp - tmp;
      lnzb = aa - lnza + ab;
             */
            lnza = aa + ab;
            lnzb = -(lnza - aa - ab);
        } else {
            /* High precision not required.  Eval Remez polynomial
         using standard double precision */
            lnza = -0.16624882440418567;
            lnza = lnza * epsilon + 0.19999954120254515;
            lnza = lnza * epsilon + -0.2499999997677497;
            lnza = lnza * epsilon + 0.3333333333332802;
            lnza = lnza * epsilon + -0.5;
            lnza = lnza * epsilon + 1.0;
            lnza *= epsilon;
        }

        /* Relative sizes:
         * lnzb     [0, 2.33E-10]
         * lnm[1]   [0, 1.17E-7]
         * ln2B*exp [0, 1.12E-4]
         * lnza      [0, 9.7E-4]
         * lnm[0]   [0, 0.692]
         * ln2A*exp [0, 709]
         */

        /* Compute the following sum:
         * lnzb + lnm[1] + ln2B*exp + lnza + lnm[0] + ln2A*exp;
         */

        //return lnzb + lnm[1] + ln2B*exp + lnza + lnm[0] + ln2A*exp;
        double a = LN_2_A * exp;
        double b = 0.0;
        double c = a + lnm[0];
        double d = -(c - a - lnm[0]);
        a = c;
        b += d;

        c = a + lnza;
        d = -(c - a - lnza);
        a = c;
        b += d;

        c = a + LN_2_B * exp;
        d = -(c - a - LN_2_B * exp);
        a = c;
        b += d;

        c = a + lnm[1];
        d = -(c - a - lnm[1]);
        a = c;
        b += d;

        c = a + lnzb;
        d = -(c - a - lnzb);
        a = c;
        b += d;

        if (hiPrec != null) {
            hiPrec[0] = a;
            hiPrec[1] = b;
        }

        return a + b;
    }

    /** Compute the base 10 logarithm.
     * @param x a number
     * @return log10(x)
     */
    public static double log10(final double x) {
        final double[] hiPrec = new double[2];

        final double lores = log(x, hiPrec);
        if (Double.isInfinite(lores)) { // don't allow this to be converted to NaN
            return lores;
        }

        final double tmp = hiPrec[0] * HEX_40000000;
        final double lna = hiPrec[0] + tmp - tmp;
        final double lnb = hiPrec[0] - lna + hiPrec[1];

        final double rln10a = 0.4342944622039795;
        final double rln10b = 1.9699272335463627E-8;

        return rln10b * lnb + rln10b * lna + rln10a * lnb + rln10a * lna;
    }

    /**
     * Computes the <a href="http://mathworld.wolfram.com/Logarithm.html">
     * logarithm</a> in a given base.
     *
     * Returns {@code NaN} if either argument is negative.
     * If {@code base} is 0 and {@code x} is positive, 0 is returned.
     * If {@code base} is positive and {@code x} is 0,
     * {@code Double.NEGATIVE_INFINITY} is returned.
     * If both arguments are 0, the result is {@code NaN}.
     *
     * @param base Base of the logarithm, must be greater than 0.
     * @param x Argument, must be greater than 0.
     * @return the value of the logarithm, i.e. the number {@code y} such that
     * <code>base<sup>y</sup> = x</code>.
     * @since 1.2 (previously in {@code MathUtils}, moved as of version 3.0)
     */
    public static double log(double base, double x) {
        return log(x) / log(base);
    }

    /**
     *  Computes sin(x) - x, where |x| < 1/16.
     *  Use a Remez polynomial approximation.
     *  @param x a number smaller than 1/16
     *  @return sin(x) - x
     */
    private static double polySine(final double x) {
        double x2 = x * x;

        double p = 2.7553817452272217E-6;
        p = p * x2 + -1.9841269659586505E-4;
        p = p * x2 + 0.008333333333329196;
        p = p * x2 + -0.16666666666666666;
        //p *= x2;
        //p *= x;
        p = p * x2 * x;

        return p;
    }

    /**
     *  Computes cos(x) - 1, where |x| < 1/16.
     *  Use a Remez polynomial approximation.
     *  @param x a number smaller than 1/16
     *  @return cos(x) - 1
     */
    private static double polyCosine(double x) {
        double x2 = x * x;

        double p = 2.479773539153719E-5;
        p = p * x2 + -0.0013888888689039883;
        p = p * x2 + 0.041666666666621166;
        p = p * x2 + -0.49999999999999994;
        p *= x2;

        return p;
    }

    /**
     *  Compute sine over the first quadrant (0 < x < pi/2).
     *  Use combination of table lookup and rational polynomial expansion.
     *  @param xa number from which sine is requested
     *  @param xb extra bits for x (may be 0.0)
     *  @return sin(xa + xb)
     */
    private static double sinQ(double xa, double xb) {
        int idx = (int) ((xa * 8.0) + 0.5);
        final double epsilon = xa - EIGHTHS[idx]; //idx*0.125;

        // Table lookups
        final double sintA = SINE_TABLE_A[idx];
        final double sintB = SINE_TABLE_B[idx];
        final double costA = COSINE_TABLE_A[idx];
        final double costB = COSINE_TABLE_B[idx];

        // Polynomial eval of sin(epsilon), cos(epsilon)
        double sinEpsA = epsilon;
        double sinEpsB = polySine(epsilon);
        final double cosEpsA = 1.0;
        final double cosEpsB = polyCosine(epsilon);

        // Split epsilon   xa + xb = x
        final double temp = sinEpsA * HEX_40000000;
        double temp2 = (sinEpsA + temp) - temp;
        sinEpsB += sinEpsA - temp2;
        sinEpsA = temp2;

        /* Compute sin(x) by angle addition formula */
        double result;

        /* Compute the following sum:
         *
         * result = sintA + costA*sinEpsA + sintA*cosEpsB + costA*sinEpsB +
         *          sintB + costB*sinEpsA + sintB*cosEpsB + costB*sinEpsB;
         *
         * Ranges of elements
         *
         * xxxtA   0            PI/2
         * xxxtB   -1.5e-9      1.5e-9
         * sinEpsA -0.0625      0.0625
         * sinEpsB -6e-11       6e-11
         * cosEpsA  1.0
         * cosEpsB  0           -0.0625
         *
         */

        //result = sintA + costA*sinEpsA + sintA*cosEpsB + costA*sinEpsB +
        //          sintB + costB*sinEpsA + sintB*cosEpsB + costB*sinEpsB;

        //result = sintA + sintA*cosEpsB + sintB + sintB * cosEpsB;
        //result += costA*sinEpsA + costA*sinEpsB + costB*sinEpsA + costB * sinEpsB;
        double a = 0;
        double b = 0;

        double t = sintA;
        double c = a + t;
        double d = -(c - a - t);
        a = c;
        b += d;

        t = costA * sinEpsA;
        c = a + t;
        d = -(c - a - t);
        a = c;
        b += d;

        b = b + sintA * cosEpsB + costA * sinEpsB;
        /*
    t = sintA*cosEpsB;
    c = a + t;
    d = -(c - a - t);
    a = c;
    b = b + d;
    t = costA*sinEpsB;
    c = a + t;
    d = -(c - a - t);
    a = c;
    b = b + d;
         */

        b = b + sintB + costB * sinEpsA + sintB * cosEpsB + costB * sinEpsB;
        /*
    t = sintB;
    c = a + t;
    d = -(c - a - t);
    a = c;
    b = b + d;
    t = costB*sinEpsA;
    c = a + t;
    d = -(c - a - t);
    a = c;
    b = b + d;
    t = sintB*cosEpsB;
    c = a + t;
    d = -(c - a - t);
    a = c;
    b = b + d;
    t = costB*sinEpsB;
    c = a + t;
    d = -(c - a - t);
    a = c;
    b = b + d;
         */

        if (xb != 0.0) {
            t = ((costA + costB) * (cosEpsA + cosEpsB) -
                    (sintA + sintB) * (sinEpsA + sinEpsB)) * xb;  // approximate cosine*xb
            c = a + t;
            d = -(c - a - t);
            a = c;
            b += d;
        }

        result = a + b;

        return result;
    }

    /**
     * Compute cosine in the first quadrant by subtracting input from PI/2 and
     * then calling sinQ.  This is more accurate as the input approaches PI/2.
     *  @param xa number from which cosine is requested
     *  @param xb extra bits for x (may be 0.0)
     *  @return cos(xa + xb)
     */
    private static double cosQ(double xa, double xb) {
        final double pi2a = 1.5707963267948966;
        final double pi2b = 6.123233995736766E-17;

        final double a = pi2a - xa;
        double b = -(a - pi2a + xa);
        b += pi2b - xb;

        return sinQ(a, b);
    }

    /**
     *  Compute tangent (or cotangent) over the first quadrant.   0 < x < pi/2
     *  Use combination of table lookup and rational polynomial expansion.
     *  @param xa number from which sine is requested
     *  @param xb extra bits for x (may be 0.0)
     *  @param cotanFlag if true, compute the cotangent instead of the tangent
     *  @return tan(xa + xb) (or cotangent, depending on cotanFlag)
     */
    private static double tanQ(double xa, double xb, boolean cotanFlag) {

        int idx = (int) ((xa * 8.0) + 0.5);
        final double epsilon = xa - EIGHTHS[idx]; //idx*0.125;

        // Table lookups
        final double sintA = SINE_TABLE_A[idx];
        final double sintB = SINE_TABLE_B[idx];
        final double costA = COSINE_TABLE_A[idx];
        final double costB = COSINE_TABLE_B[idx];

        // Polynomial eval of sin(epsilon), cos(epsilon)
        double sinEpsA = epsilon;
        double sinEpsB = polySine(epsilon);
        final double cosEpsA = 1.0;
        final double cosEpsB = polyCosine(epsilon);

        // Split epsilon   xa + xb = x
        double temp = sinEpsA * HEX_40000000;
        double temp2 = (sinEpsA + temp) - temp;
        sinEpsB += sinEpsA - temp2;
        sinEpsA = temp2;

        /* Compute sin(x) by angle addition formula */

        /* Compute the following sum:
         *
         * result = sintA + costA*sinEpsA + sintA*cosEpsB + costA*sinEpsB +
         *          sintB + costB*sinEpsA + sintB*cosEpsB + costB*sinEpsB;
         *
         * Ranges of elements
         *
         * xxxtA   0            PI/2
         * xxxtB   -1.5e-9      1.5e-9
         * sinEpsA -0.0625      0.0625
         * sinEpsB -6e-11       6e-11
         * cosEpsA  1.0
         * cosEpsB  0           -0.0625
         *
         */

        //result = sintA + costA*sinEpsA + sintA*cosEpsB + costA*sinEpsB +
        //          sintB + costB*sinEpsA + sintB*cosEpsB + costB*sinEpsB;

        //result = sintA + sintA*cosEpsB + sintB + sintB * cosEpsB;
        //result += costA*sinEpsA + costA*sinEpsB + costB*sinEpsA + costB * sinEpsB;
        double a = 0;
        double b = 0;

        // Compute sine
        double t = sintA;
        double c = a + t;
        double d = -(c - a - t);
        a = c;
        b += d;

        t = costA * sinEpsA;
        c = a + t;
        d = -(c - a - t);
        a = c;
        b += d;

        b += sintA * cosEpsB + costA * sinEpsB;
        b += sintB + costB * sinEpsA + sintB * cosEpsB + costB * sinEpsB;

        double sina = a + b;
        double sinb = -(sina - a - b);

        // Compute cosine

        a = b = c = d = 0.0;

        t = costA * cosEpsA;
        c = a + t;
        d = -(c - a - t);
        a = c;
        b += d;

        t = -sintA * sinEpsA;
        c = a + t;
        d = -(c - a - t);
        a = c;
        b += d;

        b += costB * cosEpsA + costA * cosEpsB + costB * cosEpsB;
        b -= sintB * sinEpsA + sintA * sinEpsB + sintB * sinEpsB;

        double cosa = a + b;
        double cosb = -(cosa - a - b);

        if (cotanFlag) {
            double tmp;
            tmp = cosa;
            cosa = sina;
            sina = tmp;
            tmp = cosb;
            cosb = sinb;
            sinb = tmp;
        }


        /* estimate and correct, compute 1.0/(cosa+cosb) */
        /*
    double est = (sina+sinb)/(cosa+cosb);
    double err = (sina - cosa*est) + (sinb - cosb*est);
    est += err/(cosa+cosb);
    err = (sina - cosa*est) + (sinb - cosb*est);
         */

        // f(x) = 1/x,   f'(x) = -1/x^2

        double est = sina / cosa;

        /* Split the estimate to get more accurate read on division rounding */
        temp = est * HEX_40000000;
        double esta = (est + temp) - temp;
        double estb = est - esta;

        temp = cosa * HEX_40000000;
        double cosaa = (cosa + temp) - temp;
        double cosab = cosa - cosaa;

        //double err = (sina - est*cosa)/cosa;  // Correction for division rounding
        double err = (sina - esta * cosaa - esta * cosab - estb * cosaa - estb * cosab) / cosa;  // Correction for division rounding
        err += sinb / cosa;                     // Change in est due to sinb
        err += -sina * cosb / cosa / cosa;    // Change in est due to cosb

        if (xb != 0.0) {
            // tan' = 1 + tan^2      cot' = -(1 + cot^2)
            // Approximate impact of xb
            double xbadj = xb + est * est * xb;
            if (cotanFlag) {
                xbadj = -xbadj;
            }

            err += xbadj;
        }

        return est + err;
    }

    /**
     * Sine function.
     *
     * @param x Argument.
     * @return sin(x)
     */
    public static double sin(double x) {
        boolean negative = false;
        int quadrant = 0;
        double xa;
        double xb = 0.0;

        /* Take absolute value of the input */
        xa = x;
        if (x < 0) {
            negative = true;
            xa = -xa;
        }

        /* Check for zero and negative zero */
        if (xa == 0.0) {
            long bits = Double.doubleToRawLongBits(x);
            if (bits < 0) {
                return -0.0;
            }
            return 0.0;
        }

        if (xa != xa || xa == Double.POSITIVE_INFINITY) {
            return Double.NaN;
        }

        /* Perform any argument reduction */
        if (xa > 3294198.0) {
            // PI * (2**20)
            // Argument too big for CodyWaite reduction.  Must use
            // PayneHanek.
            double[] reduceResults = new double[3];
            reducePayneHanek(xa, reduceResults);
            quadrant = ((int) reduceResults[0]) & 3;
            xa = reduceResults[1];
            xb = reduceResults[2];
        } else if (xa > 1.5707963267948966) {
            final CodyWaite cw = new CodyWaite(xa);
            quadrant = cw.getK() & 3;
            xa = cw.getRemA();
            xb = cw.getRemB();
        }

        if (negative) {
            quadrant ^= 2;  // Flip bit 1
        }

        switch (quadrant) {
            case 0:
                return sinQ(xa, xb);
            case 1:
                return cosQ(xa, xb);
            case 2:
                return -sinQ(xa, xb);
            case 3:
                return -cosQ(xa, xb);
            default:
                return Double.NaN;
        }
    }

    /**
     * Cosine function.
     *
     * @param x Argument.
     * @return cos(x)
     */
    public static double cos(double x) {
        int quadrant = 0;

        /* Take absolute value of the input */
        double xa = x;
        if (x < 0) {
            xa = -xa;
        }

        if (xa != xa || xa == Double.POSITIVE_INFINITY) {
            return Double.NaN;
        }

        /* Perform any argument reduction */
        double xb = 0;
        if (xa > 3294198.0) {
            // PI * (2**20)
            // Argument too big for CodyWaite reduction.  Must use
            // PayneHanek.
            double[] reduceResults = new double[3];
            reducePayneHanek(xa, reduceResults);
            quadrant = ((int) reduceResults[0]) & 3;
            xa = reduceResults[1];
            xb = reduceResults[2];
        } else if (xa > 1.5707963267948966) {
            final CodyWaite cw = new CodyWaite(xa);
            quadrant = cw.getK() & 3;
            xa = cw.getRemA();
            xb = cw.getRemB();
        }

        //if (negative)
        //  quadrant = (quadrant + 2) % 4;

        switch (quadrant) {
            case 0:
                return cosQ(xa, xb);
            case 1:
                return -sinQ(xa, xb);
            case 2:
                return -cosQ(xa, xb);
            case 3:
                return sinQ(xa, xb);
            default:
                return Double.NaN;
        }
    }

    /**
     * Tangent function.
     *
     * @param x Argument.
     * @return tan(x)
     */
    public static double tan(double x) {
        boolean negative = false;
        int quadrant = 0;

        /* Take absolute value of the input */
        double xa = x;
        if (x < 0) {
            negative = true;
            xa = -xa;
        }

        /* Check for zero and negative zero */
        if (xa == 0.0) {
            long bits = Double.doubleToRawLongBits(x);
            if (bits < 0) {
                return -0.0;
            }
            return 0.0;
        }

        if (xa != xa || xa == Double.POSITIVE_INFINITY) {
            return Double.NaN;
        }

        /* Perform any argument reduction */
        double xb = 0;
        if (xa > 3294198.0) {
            // PI * (2**20)
            // Argument too big for CodyWaite reduction.  Must use
            // PayneHanek.
            double[] reduceResults = new double[3];
            reducePayneHanek(xa, reduceResults);
            quadrant = ((int) reduceResults[0]) & 3;
            xa = reduceResults[1];
            xb = reduceResults[2];
        } else if (xa > 1.5707963267948966) {
            final CodyWaite cw = new CodyWaite(xa);
            quadrant = cw.getK() & 3;
            xa = cw.getRemA();
            xb = cw.getRemB();
        }

        if (xa > 1.5) {
            // Accuracy suffers between 1.5 and PI/2
            final double pi2a = 1.5707963267948966;
            final double pi2b = 6.123233995736766E-17;

            final double a = pi2a - xa;
            double b = -(a - pi2a + xa);
            b += pi2b - xb;

            xa = a + b;
            xb = -(xa - a - b);
            quadrant ^= 1;
            negative ^= true;
        }

        double result;
        if ((quadrant & 1) == 0) {
            result = tanQ(xa, xb, false);
        } else {
            result = -tanQ(xa, xb, true);
        }

        if (negative) {
            result = -result;
        }

        return result;
    }

    /**
     * Reduce the input argument using the Payne and Hanek method.
     * This is good for all inputs 0.0 < x < inf
     * Output is remainder after dividing by PI/2
     * The result array should contain 3 numbers.
     * result[0] is the integer portion, so mod 4 this gives the quadrant.
     * result[1] is the upper bits of the remainder
     * result[2] is the lower bits of the remainder
     *
     * @param x      number to reduce
     * @param result placeholder where to put the result
     */
    private static void reducePayneHanek(double x, double[] result) {
        /* Convert input double to bits */
        long inbits = Double.doubleToRawLongBits(x);
        int exponent = (int) ((inbits >> 52) & 0x7ff) - 1023;

        /* Convert to fixed point representation */
        inbits &= 0x000fffffffffffffL;
        inbits |= 0x0010000000000000L;

        /* Normalize input to be between 0.5 and 1.0 */
        exponent++;
        inbits <<= 11;

        /* Based on the exponent, get a shifted copy of recip2pi */
        long shpi0;
        long shpiA;
        long shpiB;
        int idx = exponent >> 6;
        int shift = exponent - (idx << 6);

        if (shift != 0) {
            shpi0 = (idx == 0) ? 0 : (RECIP_2PI[idx - 1] << shift);
            shpi0 |= RECIP_2PI[idx] >>> (64 - shift);
            shpiA = (RECIP_2PI[idx] << shift) | (RECIP_2PI[idx + 1] >>> (64 - shift));
            shpiB = (RECIP_2PI[idx + 1] << shift) | (RECIP_2PI[idx + 2] >>> (64 - shift));
        } else {
            shpi0 = (idx == 0) ? 0 : RECIP_2PI[idx - 1];
            shpiA = RECIP_2PI[idx];
            shpiB = RECIP_2PI[idx + 1];
        }

        /* Multiply input by shpiA */
        long a = inbits >>> 32;
        long b = inbits & 0xffffffffL;

        long c = shpiA >>> 32;
        long d = shpiA & 0xffffffffL;

        long ac = a * c;
        long bd = b * d;
        long bc = b * c;
        long ad = a * d;

        long prodB = bd + (ad << 32);
        long prodA = ac + (ad >>> 32);

        boolean bita = (bd & 0x8000000000000000L) != 0;
        boolean bitb = (ad & 0x80000000L) != 0;
        boolean bitsum = (prodB & 0x8000000000000000L) != 0;

        /* Carry */
        if ((bita && bitb) ||
                ((bita || bitb) && !bitsum)) {
            prodA++;
        }

        bita = (prodB & 0x8000000000000000L) != 0;
        bitb = (bc & 0x80000000L) != 0;

        prodB += bc << 32;
        prodA += bc >>> 32;

        bitsum = (prodB & 0x8000000000000000L) != 0;

        /* Carry */
        if ((bita && bitb) ||
                ((bita || bitb) && !bitsum)) {
            prodA++;
        }

        /* Multiply input by shpiB */
        c = shpiB >>> 32;
        d = shpiB & 0xffffffffL;
        ac = a * c;
        bc = b * c;
        ad = a * d;

        /* Collect terms */
        ac += (bc + ad) >>> 32;

        bita = (prodB & 0x8000000000000000L) != 0;
        bitb = (ac & 0x8000000000000000L) != 0;
        prodB += ac;
        bitsum = (prodB & 0x8000000000000000L) != 0;
        /* Carry */
        if ((bita && bitb) ||
                ((bita || bitb) && !bitsum)) {
            prodA++;
        }

        /* Multiply by shpi0 */
        c = shpi0 >>> 32;
        d = shpi0 & 0xffffffffL;

        bd = b * d;
        bc = b * c;
        ad = a * d;

        prodA += bd + ((bc + ad) << 32);

        /*
         * prodA, prodB now contain the remainder as a fraction of PI.  We want this as a fraction of
         * PI/2, so use the following steps:
         * 1.) multiply by 4.
         * 2.) do a fixed point muliply by PI/4.
         * 3.) Convert to floating point.
         * 4.) Multiply by 2
         */

        /* This identifies the quadrant */
        int intPart = (int) (prodA >>> 62);

        /* Multiply by 4 */
        prodA <<= 2;
        prodA |= prodB >>> 62;
        prodB <<= 2;

        /* Multiply by PI/4 */
        a = prodA >>> 32;
        b = prodA & 0xffffffffL;

        c = PI_O_4_BITS[0] >>> 32;
        d = PI_O_4_BITS[0] & 0xffffffffL;

        ac = a * c;
        bd = b * d;
        bc = b * c;
        ad = a * d;

        long prod2B = bd + (ad << 32);
        long prod2A = ac + (ad >>> 32);

        bita = (bd & 0x8000000000000000L) != 0;
        bitb = (ad & 0x80000000L) != 0;
        bitsum = (prod2B & 0x8000000000000000L) != 0;

        /* Carry */
        if ((bita && bitb) ||
                ((bita || bitb) && !bitsum)) {
            prod2A++;
        }

        bita = (prod2B & 0x8000000000000000L) != 0;
        bitb = (bc & 0x80000000L) != 0;

        prod2B += bc << 32;
        prod2A += bc >>> 32;

        bitsum = (prod2B & 0x8000000000000000L) != 0;

        /* Carry */
        if ((bita && bitb) ||
                ((bita || bitb) && !bitsum)) {
            prod2A++;
        }

        /* Multiply input by pio4bits[1] */
        c = PI_O_4_BITS[1] >>> 32;
        d = PI_O_4_BITS[1] & 0xffffffffL;
        ac = a * c;
        bc = b * c;
        ad = a * d;

        /* Collect terms */
        ac += (bc + ad) >>> 32;

        bita = (prod2B & 0x8000000000000000L) != 0;
        bitb = (ac & 0x8000000000000000L) != 0;
        prod2B += ac;
        bitsum = (prod2B & 0x8000000000000000L) != 0;
        /* Carry */
        if ((bita && bitb) ||
                ((bita || bitb) && !bitsum)) {
            prod2A++;
        }

        /* Multiply inputB by pio4bits[0] */
        a = prodB >>> 32;
        b = prodB & 0xffffffffL;
        c = PI_O_4_BITS[0] >>> 32;
        d = PI_O_4_BITS[0] & 0xffffffffL;
        ac = a * c;
        bc = b * c;
        ad = a * d;

        /* Collect terms */
        ac += (bc + ad) >>> 32;

        bita = (prod2B & 0x8000000000000000L) != 0;
        bitb = (ac & 0x8000000000000000L) != 0;
        prod2B += ac;
        bitsum = (prod2B & 0x8000000000000000L) != 0;
        /* Carry */
        if ((bita && bitb) ||
                ((bita || bitb) && !bitsum)) {
            prod2A++;
        }

        /* Convert to double */
        double tmpA = (prod2A >>> 12) / TWO_POWER_52;  // High order 52 bits
        double tmpB = (((prod2A & 0xfffL) << 40) + (prod2B >>> 24)) / TWO_POWER_52 / TWO_POWER_52; // Low bits

        double sumA = tmpA + tmpB;
        double sumB = -(sumA - tmpA - tmpB);

        /* Multiply by PI/2 and return */
        result[0] = intPart;
        result[1] = sumA * 2.0;
        result[2] = sumB * 2.0;
    }

    /**
     * Arctangent function
     *
     * @param x a number
     * @return atan(x)
     */
    public static double atan(double x) {
        return atan(x, 0.0, false);
    }

    /**
     * Internal helper function to compute arctangent.
     *
     * @param xa        number from which arctangent is requested
     * @param xb        extra bits for x (may be 0.0)
     * @param leftPlane if true, result angle must be put in the left half plane
     * @return atan(xa + xb) (or angle shifted by {@code PI} if leftPlane is true)
     */
    private static double atan(double xa, double xb, boolean leftPlane) {
        if (xa == 0.0) { // Matches +/- 0.0; return correct sign
            return leftPlane ? copySign(FastMath.PI, xa) : xa;
        }

        final boolean negate;
        if (xa < 0) {
            // negative
            xa = -xa;
            xb = -xb;
            negate = true;
        } else {
            negate = false;
        }

        if (xa > 1.633123935319537E16) { // Very large input
            return (negate ^ leftPlane) ? (-FastMath.PI * F_1_2) : (FastMath.PI * F_1_2);
        }

        /* Estimate the closest tabulated arctan value, compute eps = xa-tangentTable */
        final int idx;
        if (xa < 1) {
            idx = (int) (((-1.7168146928204136 * xa * xa + 8.0) * xa) + 0.5);
        } else {
            final double oneOverXa = 1 / xa;
            idx = (int) (-((-1.7168146928204136 * oneOverXa * oneOverXa + 8.0) * oneOverXa) + 13.07);
        }

        final double ttA = TANGENT_TABLE_A[idx];
        final double ttB = TANGENT_TABLE_B[idx];

        double epsA = xa - ttA;
        double epsB = -(epsA - xa + ttA);
        epsB += xb - ttB;

        double temp = epsA + epsB;
        epsB = -(temp - epsA - epsB);
        epsA = temp;

        /* Compute eps = eps / (1.0 + xa*tangent) */
        temp = xa * HEX_40000000;
        double ya = xa + temp - temp;
        double yb = xb + xa - ya;
        xa = ya;
        xb += yb;

        //if (idx > 8 || idx == 0)
        if (idx == 0) {
            /* If the slope of the arctan is gentle enough (< 0.45), this approximation will suffice */
            //double denom = 1.0 / (1.0 + xa*tangentTableA[idx] + xb*tangentTableA[idx] + xa*tangentTableB[idx] + xb*tangentTableB[idx]);
            final double denom = 1d / (1d + (xa + xb) * (ttA + ttB));
            //double denom = 1.0 / (1.0 + xa*tangentTableA[idx]);
            ya = epsA * denom;
            yb = epsB * denom;
        } else {
            double temp2 = xa * ttA;
            double za = 1d + temp2;
            double zb = -(za - 1d - temp2);
            temp2 = xb * ttA + xa * ttB;
            temp = za + temp2;
            zb += -(temp - za - temp2);
            za = temp;

            zb += xb * ttB;
            ya = epsA / za;

            temp = ya * HEX_40000000;
            final double yaa = (ya + temp) - temp;
            final double yab = ya - yaa;

            temp = za * HEX_40000000;
            final double zaa = (za + temp) - temp;
            final double zab = za - zaa;

            /* Correct for rounding in division */
            yb = (epsA - yaa * zaa - yaa * zab - yab * zaa - yab * zab) / za;

            yb += -epsA * zb / za / za;
            yb += epsB / za;
        }


        epsA = ya;
        epsB = yb;

        /* Evaluate polynomial */
        final double epsA2 = epsA * epsA;

        /*
    yb = -0.09001346640161823;
    yb = yb * epsA2 + 0.11110718400605211;
    yb = yb * epsA2 + -0.1428571349122913;
    yb = yb * epsA2 + 0.19999999999273194;
    yb = yb * epsA2 + -0.33333333333333093;
    yb = yb * epsA2 * epsA;
         */

        yb = 0.07490822288864472;
        yb = yb * epsA2 - 0.09088450866185192;
        yb = yb * epsA2 + 0.11111095942313305;
        yb = yb * epsA2 - 0.1428571423679182;
        yb = yb * epsA2 + 0.19999999999923582;
        yb = yb * epsA2 - 0.33333333333333287;
        yb = yb * epsA2 * epsA;


        ya = epsA;

        temp = ya + yb;
        yb = -(temp - ya - yb);
        ya = temp;

        /* Add in effect of epsB.   atan'(x) = 1/(1+x^2) */
        yb += epsB / (1d + epsA * epsA);

        final double eighths = EIGHTHS[idx];

        //result = yb + eighths[idx] + ya;
        double za = eighths + ya;
        double zb = -(za - eighths - ya);
        temp = za + yb;
        zb += -(temp - za - yb);
        za = temp;

        double result = za + zb;

        if (leftPlane) {
            // Result is in the left plane
            final double resultb = -(result - za - zb);
            final double pia = 1.5707963267948966 * 2;
            final double pib = 6.123233995736766E-17 * 2;

            za = pia - result;
            zb = -(za - pia + result);
            zb += pib - resultb;

            result = za + zb;
        }


        if (negate ^ leftPlane) {
            result = -result;
        }

        return result;
    }

    /**
     * Two arguments arctangent function
     *
     * @param y ordinate
     * @param x abscissa
     * @return phase angle of point (x,y) between {@code -PI} and {@code PI}
     */
    public static double atan2(final double y, final double x) {
        if (Double.isNaN(x) || Double.isNaN(y)) {
            return Double.NaN;
        }

        if (y == 0) {
            final double result = x * y;
            final double invx = 1d / x;
            final double invy = 1d / y;

            if (invx == 0) { // X is infinite
                if (x > 0) {
                    return y; // return +/- 0.0
                } else {
                    return copySign(FastMath.PI, y);
                }
            }

            if (x < 0 || invx < 0) {
                if (y < 0 || invy < 0) {
                    return -FastMath.PI;
                } else {
                    return FastMath.PI;
                }
            } else {
                return result;
            }
        }

        // y cannot now be zero

        if (y == Double.POSITIVE_INFINITY) {
            if (x == Double.POSITIVE_INFINITY) {
                return FastMath.PI * F_1_4;
            }

            if (x == Double.NEGATIVE_INFINITY) {
                return FastMath.PI * F_3_4;
            }

            return FastMath.PI * F_1_2;
        }

        if (y == Double.NEGATIVE_INFINITY) {
            if (x == Double.POSITIVE_INFINITY) {
                return -FastMath.PI * F_1_4;
            }

            if (x == Double.NEGATIVE_INFINITY) {
                return -FastMath.PI * F_3_4;
            }

            return -FastMath.PI * F_1_2;
        }

        if (x == Double.POSITIVE_INFINITY) {
            if (y > 0 || 1 / y > 0) {
                return 0d;
            }

            if (y < 0 || 1 / y < 0) {
                return -0d;
            }
        }

        if (x == Double.NEGATIVE_INFINITY) {
            if (y > 0.0 || 1 / y > 0.0) {
                return FastMath.PI;
            }

            if (y < 0 || 1 / y < 0) {
                return -FastMath.PI;
            }
        }

        // Neither y nor x can be infinite or NAN here

        if (x == 0) {
            if (y > 0 || 1 / y > 0) {
                return FastMath.PI * F_1_2;
            }

            if (y < 0 || 1 / y < 0) {
                return -FastMath.PI * F_1_2;
            }
        }

        // Compute ratio r = y/x
        final double r = y / x;
        if (Double.isInfinite(r)) { // bypass calculations that can create NaN
            return atan(r, 0, x < 0);
        }

        double ra = doubleHighPart(r);
        double rb = r - ra;

        // Split x
        final double xa = doubleHighPart(x);
        final double xb = x - xa;

        rb += (y - ra * xa - ra * xb - rb * xa - rb * xb) / x;

        final double temp = ra + rb;
        rb = -(temp - ra - rb);
        ra = temp;

        if (ra == 0) { // Fix up the sign so atan works correctly
            ra = copySign(0d, y);
        }

        // Call atan

        return atan(ra, rb, x < 0);
    }

    /**
     * Compute the arc sine of a number.
     *
     * @param x number on which evaluation is done
     * @return arc sine of x
     */
    public static double asin(final double x) {
        if (Double.isNaN(x)) {
            return Double.NaN;
        }

        if (x > 1.0 || x < -1.0) {
            return Double.NaN;
        }

        if (x == 1.0) {
            return FastMath.PI / 2.0;
        }

        if (x == -1.0) {
            return -FastMath.PI / 2.0;
        }

        if (x == 0.0) { // Matches +/- 0.0; return correct sign
            return x;
        }

        /* Compute asin(x) = atan(x/sqrt(1-x*x)) */

        /* Split x */
        double temp = x * HEX_40000000;
        final double xa = x + temp - temp;
        final double xb = x - xa;

        /* Square it */
        double ya = xa * xa;
        double yb = xa * xb * 2.0 + xb * xb;

        /* Subtract from 1 */
        ya = -ya;
        yb = -yb;

        double za = 1.0 + ya;
        double zb = -(za - 1.0 - ya);

        temp = za + yb;
        zb += -(temp - za - yb);
        za = temp;

        /* Square root */
        double y;
        y = sqrt(za);
        temp = y * HEX_40000000;
        ya = y + temp - temp;
        yb = y - ya;

        /* Extend precision of sqrt */
        yb += (za - ya * ya - 2 * ya * yb - yb * yb) / (2.0 * y);

        /* Contribution of zb to sqrt */
        double dx = zb / (2.0 * y);

        // Compute ratio r = x/y
        double r = x / y;
        temp = r * HEX_40000000;
        double ra = r + temp - temp;
        double rb = r - ra;

        rb += (x - ra * ya - ra * yb - rb * ya - rb * yb) / y;  // Correct for rounding in division
        rb += -x * dx / y / y;  // Add in effect additional bits of sqrt.

        temp = ra + rb;
        rb = -(temp - ra - rb);
        ra = temp;

        return atan(ra, rb, false);
    }

    /**
     * Compute the arc cosine of a number.
     *
     * @param x number on which evaluation is done
     * @return arc cosine of x
     */
    public static double acos(final double x) {
        if (Double.isNaN(x)) {
            return Double.NaN;
        }

        if (x > 1.0 || x < -1.0) {
            return Double.NaN;
        }

        if (x == -1.0) {
            return PI;
        }

        if (x == 1.0) {
            return 0.0;
        }

        if (x == 0) {
            return PI / 2.0;
        }

        /* Compute acos(x) = atan(sqrt(1-x*x)/x) */

        /* Split x */
        double temp = x * HEX_40000000;
        final double xa = x + temp - temp;
        final double xb = x - xa;

        /* Square it */
        double ya = xa * xa;
        double yb = xa * xb * 2.0 + xb * xb;

        /* Subtract from 1 */
        ya = -ya;
        yb = -yb;

        double za = 1.0 + ya;
        double zb = -(za - 1.0 - ya);

        temp = za + yb;
        zb += -(temp - za - yb);
        za = temp;

        /* Square root */
        double y = sqrt(za);
        temp = y * HEX_40000000;
        ya = y + temp - temp;
        yb = y - ya;

        /* Extend precision of sqrt */
        yb += (za - ya * ya - 2 * ya * yb - yb * yb) / (2.0 * y);

        /* Contribution of zb to sqrt */
        yb += zb / (2.0 * y);
        y = ya + yb;
        yb = -(y - ya - yb);

        // Compute ratio r = y/x
        double r = y / x;

        // Did r overflow?
        if (Double.isInfinite(r)) { // x is effectively zero
            return PI / 2; // so return the appropriate value
        }

        double ra = doubleHighPart(r);
        double rb = r - ra;

        rb += (y - ra * xa - ra * xb - rb * xa - rb * xb) / x;  // Correct for rounding in division
        rb += yb / x;  // Add in effect additional bits of sqrt.

        temp = ra + rb;
        rb = -(temp - ra - rb);
        ra = temp;

        return atan(ra, rb, x < 0);
    }

    /** Compute the cubic root of a number.
     * @param x number on which evaluation is done
     * @return cubic root of x
     */
    public static double cbrt(double x) {
        /* Convert input double to bits */
        long inbits = Double.doubleToRawLongBits(x);
        int exponent = (int) ((inbits >> 52) & 0x7ff) - 1023;
        boolean subnormal = false;

        if (exponent == -1023) {
            if (x == 0) {
                return x;
            }

            /* Subnormal, so normalize */
            subnormal = true;
            x *= 1.8014398509481984E16;  // 2^54
            inbits = Double.doubleToRawLongBits(x);
            exponent = (int) ((inbits >> 52) & 0x7ff) - 1023;
        }

        if (exponent == 1024) {
            // Nan or infinity.  Don't care which.
            return x;
        }

        /* Divide the exponent by 3 */
        int exp3 = exponent / 3;

        /* p2 will be the nearest power of 2 to x with its exponent divided by 3 */
        double p2 = Double.longBitsToDouble((inbits & 0x8000000000000000L) |
                (long) (((exp3 + 1023) & 0x7ff)) << 52);

        /* This will be a number between 1 and 2 */
        final double mant = Double.longBitsToDouble((inbits & 0x000fffffffffffffL) | 0x3ff0000000000000L);

        /* Estimate the cube root of mant by polynomial */
        double est = -0.010714690733195933;
        est = est * mant + 0.0875862700108075;
        est = est * mant + -0.3058015757857271;
        est = est * mant + 0.7249995199969751;
        est = est * mant + 0.5039018405998233;

        est *= CBRTTWO[exponent % 3 + 2];

        // est should now be good to about 15 bits of precision.   Do 2 rounds of
        // Newton's method to get closer,  this should get us full double precision
        // Scale down x for the purpose of doing newtons method.  This avoids over/under flows.
        final double xs = x / (p2 * p2 * p2);
        est += (xs - est * est * est) / (3 * est * est);
        est += (xs - est * est * est) / (3 * est * est);

        // Do one round of Newton's method in extended precision to get the last bit right.
        double temp = est * HEX_40000000;
        double ya = est + temp - temp;
        double yb = est - ya;

        double za = ya * ya;
        double zb = ya * yb * 2.0 + yb * yb;
        temp = za * HEX_40000000;
        double temp2 = za + temp - temp;
        zb += za - temp2;
        za = temp2;

        zb = za * yb + ya * zb + zb * yb;
        za *= ya;

        double na = xs - za;
        double nb = -(na - xs + za);
        nb -= zb;

        est += (na + nb) / (3 * est * est);

        /* Scale by a power of two, so this is exact. */
        est *= p2;

        if (subnormal) {
            est *= 3.814697265625E-6;  // 2^-18
        }

        return est;
    }

    /**
     * Convert degrees to radians, with error of less than 0.5 ULP
     *
     * @param x angle in degrees
     * @return x converted into radians
     */
    public static double toRadians(final double x) {
        return x / 180.0 * PI;
    }

    /**
     * Convert radians to degrees, with error of less than 0.5 ULP
     *
     * @param x angle in radians
     * @return x converted into degrees
     */
    public static double toDegrees(final double x) {
        return x * 180.0 / PI;
    }

    /**
     * Absolute value.
     * @param x number from which absolute value is requested
     * @return abs(x)
     */
    public static int abs(final int x) {
        return (x < 0) ? -x : x;
    }

    /**
     * Absolute value.
     * @param x number from which absolute value is requested
     * @return abs(x)
     */
    public static long abs(final long x) {
        return (x < 0) ? -x : x;
    }

    /**
     * Absolute value.
     * @param x number from which absolute value is requested
     * @return abs(x)
     */
    public static float abs(final float x) {
        return (x <= 0.0F) ? 0.0F - x : x;
    }

    /**
     * Absolute value.
     *
     * @param x number from which absolute value is requested
     * @return abs(x)
     */
    public static double abs(final double x) {
        return (x <= 0.0D) ? 0.0D - x : x;
    }

    /**
     * Compute least significant bit (Unit in Last Position) for a number.
     *
     * @param x number from which ulp is requested
     * @return ulp(x)
     */
    public static double ulp(final double x) {
        if (Double.isInfinite(x)) {
            return Double.POSITIVE_INFINITY;
        }
        return abs(x - Double.longBitsToDouble(Double.doubleToRawLongBits(x) ^ 1));
    }

    /**
     * Multiply a double number by a power of 2.
     * @param d number to multiply
     * @param n power of 2
     * @return d &times; 2<sup>n</sup>
     */
    public static double scalb(final double d, final int n) {

        // first simple and fast handling when 2^n can be represented using normal numbers
        if ((n > -1023) && (n < 1024)) {
            return d * Double.longBitsToDouble(((long) (n + 1023)) << 52);
        }

        // handle special cases
        if (Double.isNaN(d) || Double.isInfinite(d) || (d == 0)) {
            return d;
        }
        if (n < -2098) {
            return (d > 0) ? 0.0 : -0.0;
        }
        if (n > 2097) {
            return (d > 0) ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
        }

        // decompose d
        final long bits = Double.doubleToRawLongBits(d);
        final long sign = bits & 0x8000000000000000L;
        int exponent = ((int) (bits >>> 52)) & 0x7ff;
        long mantissa = bits & 0x000fffffffffffffL;

        // compute scaled exponent
        int scaledExponent = exponent + n;

        if (n < 0) {
            // we are really in the case n <= -1023
            if (scaledExponent > 0) {
                // both the input and the result are normal numbers, we only adjust the exponent
                return Double.longBitsToDouble(sign | (((long) scaledExponent) << 52) | mantissa);
            } else if (scaledExponent > -53) {
                // the input is a normal number and the result is a subnormal number

                // recover the hidden mantissa bit
                mantissa |= 1L << 52;

                // scales down complete mantissa, hence losing least significant bits
                final long mostSignificantLostBit = mantissa & (1L << (-scaledExponent));
                mantissa >>>= 1 - scaledExponent;
                if (mostSignificantLostBit != 0) {
                    // we need to add 1 bit to round up the result
                    mantissa++;
                }
                return Double.longBitsToDouble(sign | mantissa);

            } else {
                // no need to compute the mantissa, the number scales down to 0
                return (sign == 0L) ? 0.0 : -0.0;
            }
        } else {
            // we are really in the case n >= 1024
            if (exponent == 0) {

                // the input number is subnormal, normalize it
                while ((mantissa >>> 52) != 1) {
                    mantissa <<= 1;
                    --scaledExponent;
                }
                ++scaledExponent;
                mantissa &= 0x000fffffffffffffL;

                if (scaledExponent < 2047) {
                    return Double.longBitsToDouble(sign | (((long) scaledExponent) << 52) | mantissa);
                } else {
                    return (sign == 0L) ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
                }

            } else if (scaledExponent < 2047) {
                return Double.longBitsToDouble(sign | (((long) scaledExponent) << 52) | mantissa);
            } else {
                return (sign == 0L) ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
            }
        }
    }

    /**
     * Multiply a float number by a power of 2.
     * @param f number to multiply
     * @param n power of 2
     * @return f &times; 2<sup>n</sup>
     */
    public static float scalb(final float f, final int n) {

        // first simple and fast handling when 2^n can be represented using normal numbers
        if ((n > -127) && (n < 128)) {
            return f * Float.intBitsToFloat((n + 127) << 23);
        }

        // handle special cases
        if (Float.isNaN(f) || Float.isInfinite(f) || (f == 0f)) {
            return f;
        }
        if (n < -277) {
            return (f > 0) ? 0.0f : -0.0f;
        }
        if (n > 276) {
            return (f > 0) ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY;
        }

        // decompose f
        final int bits = Float.floatToIntBits(f);
        final int sign = bits & 0x80000000;
        int exponent = (bits >>> 23) & 0xff;
        int mantissa = bits & 0x007fffff;

        // compute scaled exponent
        int scaledExponent = exponent + n;

        if (n < 0) {
            // we are really in the case n <= -127
            if (scaledExponent > 0) {
                // both the input and the result are normal numbers, we only adjust the exponent
                return Float.intBitsToFloat(sign | (scaledExponent << 23) | mantissa);
            } else if (scaledExponent > -24) {
                // the input is a normal number and the result is a subnormal number

                // recover the hidden mantissa bit
                mantissa |= 1 << 23;

                // scales down complete mantissa, hence losing least significant bits
                final int mostSignificantLostBit = mantissa & (1 << (-scaledExponent));
                mantissa >>>= 1 - scaledExponent;
                if (mostSignificantLostBit != 0) {
                    // we need to add 1 bit to round up the result
                    mantissa++;
                }
                return Float.intBitsToFloat(sign | mantissa);

            } else {
                // no need to compute the mantissa, the number scales down to 0
                return (sign == 0) ? 0.0f : -0.0f;
            }
        } else {
            // we are really in the case n >= 128
            if (exponent == 0) {

                // the input number is subnormal, normalize it
                while ((mantissa >>> 23) != 1) {
                    mantissa <<= 1;
                    --scaledExponent;
                }
                ++scaledExponent;
                mantissa &= 0x007fffff;

                if (scaledExponent < 255) {
                    return Float.intBitsToFloat(sign | (scaledExponent << 23) | mantissa);
                } else {
                    return (sign == 0) ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY;
                }

            } else if (scaledExponent < 255) {
                return Float.intBitsToFloat(sign | (scaledExponent << 23) | mantissa);
            } else {
                return (sign == 0) ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY;
            }
        }

    }

    /**
     * Compute least significant bit (Unit in Last Position) for a number.
     *
     * @param x number from which ulp is requested
     * @return ulp(x)
     */
    public static float ulp(final float x) {
        if (Float.isInfinite(x)) {
            return Float.POSITIVE_INFINITY;
        }
        return abs(x - Float.intBitsToFloat(Float.floatToIntBits(x) ^ 1));
    }

    /**
     * Get the next machine representable number after a number, moving
     * in the direction of another number.
     * <p>
     * The ordering is as follows (increasing):
     * <ul>
     * <li>-INFINITY</li>
     * <li>-MAX_VALUE</li>
     * <li>-MIN_VALUE</li>
     * <li>-0.0</li>
     * <li>+0.0</li>
     * <li>+MIN_VALUE</li>
     * <li>+MAX_VALUE</li>
     * <li>+INFINITY</li>
     * <li></li>
     * </ul>
     * <p>
     * If arguments compare equal, then the second argument is returned.
     * <p>
     * If {@code direction} is greater than {@code d},
     * the smallest machine representable number strictly greater than
     * {@code d} is returned; if less, then the largest representable number
     * strictly less than {@code d} is returned.</p>
     * <p>
     * If {@code d} is infinite and direction does not
     * bring it back to finite numbers, it is returned unchanged.</p>
     *
     * @param d base number
     * @param direction (the only important thing is whether
     * {@code direction} is greater or smaller than {@code d})
     * @return the next machine representable number in the specified direction
     */
    public static double nextAfter(final double d, final double direction) {

        // handling of some important special cases
        if (Double.isNaN(d) || Double.isNaN(direction)) {
            return Double.NaN;
        } else if (d == direction) {
            return direction;
        } else if (Double.isInfinite(d)) {
            return (d < 0) ? -Double.MAX_VALUE : Double.MAX_VALUE;
        } else if (d == 0) {
            return (direction < 0) ? -Double.MIN_VALUE : Double.MIN_VALUE;
        }
        // special cases MAX_VALUE to infinity and  MIN_VALUE to 0
        // are handled just as normal numbers
        // can use raw bits since already dealt with infinity and NaN
        final long bits = Double.doubleToRawLongBits(d);
        final long sign = bits & 0x8000000000000000L;
        if ((direction < d) ^ (sign == 0L)) {
            return Double.longBitsToDouble(sign | ((bits & 0x7fffffffffffffffL) + 1));
        } else {
            return Double.longBitsToDouble(sign | ((bits & 0x7fffffffffffffffL) - 1));
        }

    }

    /**
     * Get the next machine representable number after a number, moving
     * in the direction of another number.
     * <p>
     * The ordering is as follows (increasing):
     * <ul>
     * <li>-INFINITY</li>
     * <li>-MAX_VALUE</li>
     * <li>-MIN_VALUE</li>
     * <li>-0.0</li>
     * <li>+0.0</li>
     * <li>+MIN_VALUE</li>
     * <li>+MAX_VALUE</li>
     * <li>+INFINITY</li>
     * <li></li>
     * </ul>
     * <p>
     * If arguments compare equal, then the second argument is returned.
     * <p>
     * If {@code direction} is greater than {@code f},
     * the smallest machine representable number strictly greater than
     * {@code f} is returned; if less, then the largest representable number
     * strictly less than {@code f} is returned.</p>
     * <p>
     * If {@code f} is infinite and direction does not
     * bring it back to finite numbers, it is returned unchanged.</p>
     *
     * @param f base number
     * @param direction (the only important thing is whether
     * {@code direction} is greater or smaller than {@code f})
     * @return the next machine representable number in the specified direction
     */
    public static float nextAfter(final float f, final double direction) {

        // handling of some important special cases
        if (Double.isNaN(f) || Double.isNaN(direction)) {
            return Float.NaN;
        } else if (f == direction) {
            return (float) direction;
        } else if (Float.isInfinite(f)) {
            return (f < 0f) ? -Float.MAX_VALUE : Float.MAX_VALUE;
        } else if (f == 0f) {
            return (direction < 0) ? -Float.MIN_VALUE : Float.MIN_VALUE;
        }
        // special cases MAX_VALUE to infinity and  MIN_VALUE to 0
        // are handled just as normal numbers

        final int bits = Float.floatToIntBits(f);
        final int sign = bits & 0x80000000;
        if ((direction < f) ^ (sign == 0)) {
            return Float.intBitsToFloat(sign | ((bits & 0x7fffffff) + 1));
        } else {
            return Float.intBitsToFloat(sign | ((bits & 0x7fffffff) - 1));
        }

    }

    public static int floorInt(final double x) {

        int var2 = (int) x;

        return x < var2 ? var2 - 1 : var2;
    }

    /**
     * Get the largest whole number smaller than x.
     *
     * @param x number from which floor is requested
     * @return a double number f such that f is an integer f &lt;= x &lt; f + 1.0
     */
    public static double floor(final double x) {
        long y;

        if (Double.isNaN(x)) {
            return x;
        }

        if (x >= TWO_POWER_52 || x <= -TWO_POWER_52) {
            return x;
        }

        y = (long) x;
        if (x < 0 && y != x) {
            y--;
        }

        if (y == 0) {
            return x * y;
        }

        return y;
    }

    /**
     * Get the smallest whole number larger than x.
     *
     * @param x number from which ceil is requested
     * @return a double number c such that c is an integer c - 1.0 &lt; x &lt;= c
     */
    public static double ceil(final double x) {
        double y;

        if (Double.isNaN(x)) {
            return x;
        }

        y = floor(x);
        if (y == x) {
            return y;
        }

        y += 1.0;

        if (y == 0) {
            return x * y;
        }

        return y;
    }

    /**
     * Get the whole number that is the nearest to x, or the even one if x is exactly half way between two integers.
     *
     * @param x number from which nearest whole number is requested
     * @return a double number r such that r is an integer r - 0.5 &lt;= x &lt;= r + 0.5
     */
    public static double rint(final double x) {
        double y = floor(x);
        double d = x - y;

        if (d > 0.5) {
            if (y == -1.0) {
                return -0.0; // Preserve sign of operand
            }
            return y + 1.0;
        }
        if (d < 0.5) {
            return y;
        }

        /* half way, round to even */
        long z = (long) y;
        return (z & 1) == 0 ? y : y + 1.0;
    }

    /**
     * Get the closest long to x.
     *
     * @param x number from which closest long is requested
     * @return closest long to x
     */
    public static long round(final double x) {
        final long bits = Double.doubleToRawLongBits(x);
        final int biasedExp = ((int) (bits >> 52)) & 0x7ff;
        // Shift to get rid of bits past comma except first one: will need to
        // 1-shift to the right to end up with correct magnitude.
        final int shift = (52 - 1 + Double.MAX_EXPONENT) - biasedExp;
        if ((shift & -64) == 0) {
            // shift in [0,63], so unbiased exp in [-12,51].
            long extendedMantissa = 0x0010000000000000L | (bits & 0x000fffffffffffffL);
            if (bits < 0) {
                extendedMantissa = -extendedMantissa;
            }
            // If value is positive and first bit past comma is 0, rounding
            // to lower integer, else to upper one, which is what "+1" and
            // then ">>1" do.
            return ((extendedMantissa >> shift) + 1L) >> 1;
        } else {
            // +-Infinity, NaN, or a mathematical integer.
            return (long) x;
        }
    }

    /** Get the closest int to x.
     * @param x number from which closest int is requested
     * @return closest int to x
     */
    public static int round(final float x) {
        final int bits = Float.floatToRawIntBits(x);
        final int biasedExp = (bits >> 23) & 0xff;
        // Shift to get rid of bits past comma except first one: will need to
        // 1-shift to the right to end up with correct magnitude.
        final int shift = (23 - 1 + Float.MAX_EXPONENT) - biasedExp;
        if ((shift & -32) == 0) {
            // shift in [0,31], so unbiased exp in [-9,22].
            int extendedMantissa = 0x00800000 | (bits & 0x007fffff);
            if (bits < 0) {
                extendedMantissa = -extendedMantissa;
            }
            // If value is positive and first bit past comma is 0, rounding
            // to lower integer, else to upper one, which is what "+1" and
            // then ">>1" do.
            return ((extendedMantissa >> shift) + 1) >> 1;
        } else {
            // +-Infinity, NaN, or a mathematical integer.
            return (int) x;
        }
    }

    /**
     * Computes the remainder as prescribed by the IEEE 754 standard.
     * The remainder value is mathematically equal to {@code x - y*n}
     * where {@code n} is the mathematical integer closest to the exact mathematical value
     * of the quotient {@code x/y}.
     * If two mathematical integers are equally close to {@code x/y} then
     * {@code n} is the integer that is even.
     * <ul>
     * <li>If either operand is NaN, the result is NaN.</li>
     * <li>If the result is not NaN, the sign of the result equals the sign of the dividend.</li>
     * <li>If the dividend is an infinity, or the divisor is a zero, or both, the result is NaN.</li>
     * <li>If the dividend is finite and the divisor is an infinity, the result equals the dividend.</li>
     * <li>If the dividend is a zero and the divisor is finite, the result equals the dividend.</li>
     * </ul>
     *
     * @param dividend the number to be divided
     * @param divisor  the number by which to divide
     * @return the remainder, rounded
     */
    public static double IEEEremainder(final double dividend, final double divisor) {
        if (getExponent(dividend) == 1024 || getExponent(divisor) == 1024 || divisor == 0.0) {
            // we are in one of the special cases
            if (Double.isInfinite(divisor) && !Double.isInfinite(dividend)) {
                return dividend;
            } else {
                return Double.NaN;
            }
        } else {
            // we are in the general case
            final double n = FastMath.rint(dividend / divisor);
            final double remainder = Double.isInfinite(n) ? 0.0 : dividend - divisor * n;
            return (remainder == 0) ? FastMath.copySign(remainder, dividend) : remainder;
        }
    }

    /**
     * Finds q such that a = q b + r with 0 &lt;= r &lt; b if b &gt; 0 and b &lt; r &lt;= 0 if b &lt; 0.
     * <p>
     * This methods returns the same value as integer division when
     * a and b are same signs, but returns a different value when
     * they are opposite (i.e. q is negative).
     * </p>
     *
     * @param a dividend
     * @param b divisor
     * @return q such that a = q b + r with 0 &lt;= r &lt; b if b &gt; 0 and b &lt; r &lt;= 0 if b &lt; 0
     * @see #floorMod(int, int)
     * @since 3.4
     */
    public static int floorDiv(final int a, final int b) {

        if (b == 0) return b;

        final int m = a % b;
        if ((a ^ b) >= 0 || m == 0) {
            // a an b have same sign, or division is exact
            return a / b;
        } else {
            // a and b have opposite signs and division is not exact
            return (a / b) - 1;
        }

    }

    /**
     * Finds q such that a = q b + r with 0 &lt;= r &lt; b if b &gt; 0 and b &lt; r &lt;= 0 if b &lt; 0.
     * <p>
     * This methods returns the same value as integer division when
     * a and b are same signs, but returns a different value when
     * they are opposite (i.e. q is negative).
     * </p>
     *
     * @param a dividend
     * @param b divisor
     * @return q such that a = q b + r with 0 &lt;= r &lt; b if b &gt; 0 and b &lt; r &lt;= 0 if b &lt; 0
     * @see #floorMod(long, long)
     * @since 3.4
     */
    public static long floorDiv(final long a, final long b) {

        if (b == 0L) return b;

        final long m = a % b;
        if ((a ^ b) >= 0L || m == 0L) {
            // a an b have same sign, or division is exact
            return a / b;
        } else {
            // a and b have opposite signs and division is not exact
            return (a / b) - 1L;
        }

    }

    /**
     * Finds r such that a = q b + r with 0 &lt;= r &lt; b if b &gt; 0 and b &lt; r &lt;= 0 if b &lt; 0.
     * <p>
     * This methods returns the same value as integer modulo when
     * a and b are same signs, but returns a different value when
     * they are opposite (i.e. q is negative).
     * </p>
     *
     * @param a dividend
     * @param b divisor
     * @return r such that a = q b + r with 0 &lt;= r &lt; b if b &gt; 0 and b &lt; r &lt;= 0 if b &lt; 0
     * @see #floorDiv(int, int)
     * @since 3.4
     */
    public static int floorMod(final int a, final int b) {

        if (b == 0) return b;

        final int m = a % b;
        if ((a ^ b) >= 0 || m == 0) {
            // a an b have same sign, or division is exact
            return m;
        } else {
            // a and b have opposite signs and division is not exact
            return b + m;
        }

    }

    /**
     * Finds r such that a = q b + r with 0 &lt;= r &lt; b if b &gt; 0 and b &lt; r &lt;= 0 if b &lt; 0.
     * <p>
     * This methods returns the same value as integer modulo when
     * a and b are same signs, but returns a different value when
     * they are opposite (i.e. q is negative).
     * </p>
     *
     * @param a dividend
     * @param b divisor
     * @return r such that a = q b + r with 0 &lt;= r &lt; b if b &gt; 0 and b &lt; r &lt;= 0 if b &lt; 0
     * @see #floorDiv(long, long)
     * @since 3.4
     */
    public static long floorMod(final long a, final long b) {

        if (b == 0L) return b;

        final long m = a % b;
        if ((a ^ b) >= 0L || m == 0L) {
            // a an b have same sign, or division is exact
            return m;
        } else {
            // a and b have opposite signs and division is not exact
            return b + m;
        }

    }

    /**
     * Returns the first argument with the sign of the second argument.
     * A NaN {@code sign} argument is treated as positive.
     *
     * @param magnitude the value to return
     * @param sign      the sign for the returned value
     * @return the magnitude with the same sign as the {@code sign} argument
     */
    public static double copySign(double magnitude, double sign) {
        // The highest order bit is going to be zero if the
        // highest order bit of m and s is the same and one otherwise.
        // So (m^s) will be positive if both m and s have the same sign
        // and negative otherwise.
        final long m = Double.doubleToRawLongBits(magnitude); // don't care about NaN
        final long s = Double.doubleToRawLongBits(sign);
        if ((m ^ s) >= 0) {
            return magnitude;
        }
        return -magnitude; // flip sign
    }

    /**
     * Returns the first argument with the sign of the second argument.
     * A NaN {@code sign} argument is treated as positive.
     *
     * @param magnitude the value to return
     * @param sign      the sign for the returned value
     * @return the magnitude with the same sign as the {@code sign} argument
     */
    public static float copySign(float magnitude, float sign) {
        // The highest order bit is going to be zero if the
        // highest order bit of m and s is the same and one otherwise.
        // So (m^s) will be positive if both m and s have the same sign
        // and negative otherwise.
        final int m = Float.floatToRawIntBits(magnitude);
        final int s = Float.floatToRawIntBits(sign);
        if ((m ^ s) >= 0) {
            return magnitude;
        }
        return -magnitude; // flip sign
    }

    /**
     * Return the exponent of a double number, removing the bias.
     * <p>
     * For double numbers of the form 2<sup>x</sup>, the unbiased
     * exponent is exactly x.
     * </p>
     *
     * @param d number from which exponent is requested
     * @return exponent for d in IEEE754 representation, without bias
     */
    public static int getExponent(final double d) {
        // NaN and Infinite will return 1024 anywho so can use raw bits
        return (int) ((Double.doubleToRawLongBits(d) >>> 52) & 0x7ff) - 1023;
    }

    /**
     * Return the exponent of a float number, removing the bias.
     * <p>
     * For float numbers of the form 2<sup>x</sup>, the unbiased
     * exponent is exactly x.
     * </p>
     *
     * @param f number from which exponent is requested
     * @return exponent for d in IEEE754 representation, without bias
     */
    public static int getExponent(final float f) {
        // NaN and Infinite will return the same exponent anywho so can use raw bits
        return ((Float.floatToRawIntBits(f) >>> 23) & 0xff) - 127;
    }

    public static double hypot(final double x, final double y) {
        return sqrt((x * x) + (y * y));
    }

    /**
     * Enclose large data table in nested static class so it's only loaded on first access.
     */
    private static class ExpIntTable {
        /**
         * Exponential evaluated at integer values,
         * exp(x) =  expIntTableA[x + EXP_INT_TABLE_MAX_INDEX] + expIntTableB[x+EXP_INT_TABLE_MAX_INDEX].
         */
        private static final double[] EXP_INT_TABLE_A;
        /**
         * Exponential evaluated at integer values,
         * exp(x) =  expIntTableA[x + EXP_INT_TABLE_MAX_INDEX] + expIntTableB[x+EXP_INT_TABLE_MAX_INDEX]
         */
        private static final double[] EXP_INT_TABLE_B;

        static {
            EXP_INT_TABLE_A = new double[FastMath.EXP_INT_TABLE_LEN];
            EXP_INT_TABLE_B = new double[FastMath.EXP_INT_TABLE_LEN];

            final double[] tmp = new double[2];
            final double[] recip = new double[2];

            // Populate expIntTable
            for (int i = 0; i < FastMath.EXP_INT_TABLE_MAX_INDEX; i++) {
                NumbersUtils.expint(i, tmp);
                EXP_INT_TABLE_A[i + FastMath.EXP_INT_TABLE_MAX_INDEX] = tmp[0];
                EXP_INT_TABLE_B[i + FastMath.EXP_INT_TABLE_MAX_INDEX] = tmp[1];

                if (i != 0) {
                    // Negative integer powers
                    NumbersUtils.splitReciprocal(tmp, recip);
                    EXP_INT_TABLE_A[FastMath.EXP_INT_TABLE_MAX_INDEX - i] = recip[0];
                    EXP_INT_TABLE_B[FastMath.EXP_INT_TABLE_MAX_INDEX - i] = recip[1];
                }
            }
        }
    }

    /**
     * Enclose large data table in nested static class so it's only loaded on first access.
     */
    private static class ExpFracTable {
        /**
         * Exponential over the range of 0 - 1 in increments of 2^-10
         * exp(x/1024) =  expFracTableA[x] + expFracTableB[x].
         * 1024 = 2^10
         */
        private static final double[] EXP_FRAC_TABLE_A;
        /**
         * Exponential over the range of 0 - 1 in increments of 2^-10
         * exp(x/1024) =  expFracTableA[x] + expFracTableB[x].
         */
        private static final double[] EXP_FRAC_TABLE_B;

        static {
            EXP_FRAC_TABLE_A = new double[FastMath.EXP_FRAC_TABLE_LEN];
            EXP_FRAC_TABLE_B = new double[FastMath.EXP_FRAC_TABLE_LEN];

            final double[] tmp = new double[2];

            // Populate expFracTable
            final double factor = 1d / (EXP_FRAC_TABLE_LEN - 1);
            for (int i = 0; i < EXP_FRAC_TABLE_A.length; i++) {
                NumbersUtils.slowexp(i * factor, tmp);
                EXP_FRAC_TABLE_A[i] = tmp[0];
                EXP_FRAC_TABLE_B[i] = tmp[1];
            }
        }
    }

    /**
     * Enclose the Cody/Waite reduction (used in "sin", "cos" and "tan").
     */
    private static class CodyWaite {
        /** k */
        private final int finalK;
        /** remA */
        private final double finalRemA;
        /** remB */
        private final double finalRemB;

        /**
         * @param xa Argument.
         */
        CodyWaite(double xa) {
            // Estimate k.
            //k = (int)(xa / 1.5707963267948966);
            int k = (int) (xa * 0.6366197723675814);

            // Compute remainder.
            double remA;
            double remB;
            while (true) {
                double a = -k * 1.570796251296997;
                remA = xa + a;
                remB = -(remA - xa - a);

                a = -k * 7.549789948768648E-8;
                double b = remA;
                remA = a + b;
                remB += -(remA - b - a);

                a = -k * 6.123233995736766E-17;
                b = remA;
                remA = a + b;
                remB += -(remA - b - a);

                if (remA > 0) {
                    break;
                }

                // Remainder is negative, so decrement k and try again.
                // This should only happen if the input is very close
                // to an even multiple of pi/2.
                --k;
            }

            this.finalK = k;
            this.finalRemA = remA;
            this.finalRemB = remB;
        }

        /**
         * @return k
         */
        int getK() {
            return finalK;
        }

        /**
         * @return remA
         */
        double getRemA() {
            return finalRemA;
        }

        /**
         * @return remB
         */
        double getRemB() {
            return finalRemB;
        }
    }

    /**
     * Enclose large data table in nested static class so it's only loaded on first access.
     */
    private static class lnMant {
        /**
         * Extended precision logarithm table over the range 1 - 2 in increments of 2^-10.
         */
        private static final double[][] LN_MANT;

        static {
            LN_MANT = new double[FastMath.LN_MANT_LEN][];

            // Populate lnMant table
            for (int i = 0; i < LN_MANT.length; i++) {
                final double d = Double.longBitsToDouble((((long) i) << 42) | 0x3ff0000000000000L);
                LN_MANT[i] = NumbersUtils.slowLog(d);
            }
        }
    }
}
