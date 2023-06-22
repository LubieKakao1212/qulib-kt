package com.LubieKakao1212.qulib.math;

import org.joml.Quaterniond;
import org.joml.Vector3d;

import java.util.Random;

/**
 * @deprecated For backwards compatibility
 */
public class AimUtil {

    private static final Random random = new Random();

    public static Vector3d calculateForwardWithUniformSpread(Quaterniond aim, double maxSpread, Vector3d forwardIn) {
        return AimUtilKt.randomSpread(random, aim, maxSpread, forwardIn);
    }

    public static Quaterniond aimRad(double pitch, double yaw) {
        return AimUtilKt.aimRad(new Quaterniond(), pitch, yaw);
    }


}
