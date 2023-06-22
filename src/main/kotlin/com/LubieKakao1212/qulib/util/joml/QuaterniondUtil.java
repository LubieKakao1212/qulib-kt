package com.LubieKakao1212.qulib.util.joml;

import com.LubieKakao1212.qulib.math.extensions.QuaterniondExtensionsKt;
import com.mojang.math.Quaternion;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaterniond;

/**
 * @deprecated For backwards compatibility
 */
public class QuaterniondUtil {

    public static double smallerAngle(Quaterniond a, Quaterniond b) {
        return QuaterniondExtensionsKt.smallAngle(a, b);
    }

    public static Quaterniond step(@NotNull Quaterniond from, @NotNull Quaterniond to, double maxAngle) {
        return QuaterniondExtensionsKt.step(from, to, maxAngle, new Quaterniond());
    }

    public static Quaternion toMojang(@NotNull Quaterniond quat) {
        return new Quaternion((float)quat.x, (float)quat.y, (float)quat.z, (float)quat.w);
    }
}
