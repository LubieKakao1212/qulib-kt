package com.LubieKakao1212.qulib.util.joml;

import com.LubieKakao1212.qulib.math.extensions.Vector3dExtensions;
import com.LubieKakao1212.qulib.math.extensions.Vector3dExtensionsKt;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;

/**
 * @deprecated For backwards compatibility
 */
@Deprecated
public class Vector3dUtil {

    public static Vector3d south() {
        return Vector3dExtensions.INSTANCE.getSOUTH();
    }

    public static Vector3d of(@NotNull Vec3 pos) {
        return Vector3dExtensionsKt.from(new Vector3d(), pos);
    }

}
