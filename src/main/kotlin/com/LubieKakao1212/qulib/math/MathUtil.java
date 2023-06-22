package com.LubieKakao1212.qulib.math;

public class MathUtil {

    public static final float radToDeg = (float)(180./Math.PI);
    public static final float piHalf = (float) Math.PI / 2.f;

    public static double loop(double a, double min, double max) {
        return MathUtilKt.loop(a, min, max);
    }

}
