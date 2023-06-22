package com.LubieKakao1212.qulib.math;

/**
 * @deprecated For backwards compatibility
 */
@Deprecated
public class MathUtil {

    public static final float radToDeg = (float)(180. / Math.PI);
    public static final float degToRad = (float)(Math.PI / 180.);
    public static final float piHalf = (float) Math.PI / 2.f;
    public static final float pi = (float) Math.PI;

    public static double loop(double a, double min, double max) {
        return MathUtilKt.loop(a, min, max);
    }

}
