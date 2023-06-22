package com.LubieKakao1212.qulib.nbt;

import com.LubieKakao1212.qulib.math.extensions.QuaterniondExtensionsKt;
import com.LubieKakao1212.qulib.math.extensions.Vector3dExtensionsKt;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaterniond;
import org.joml.Vector3d;

/**
 * @deprecated For backwards compatibility
 */
public class JomlNBT {

    public static Quaterniond readQuaternion(@NotNull ListTag nbt) {
        return QuaterniondExtensionsKt.deserializeNBT(new Quaterniond(), nbt);//new Quaterniond(nbt.getDouble(0), nbt.getDouble(1), nbt.getDouble(2), nbt.getDouble(3));
    }

    public static Quaterniond readQuaternion(@NotNull CompoundTag nbt, @NotNull String key) {
        return readQuaternion(nbt.getList(key, Tag.TAG_DOUBLE));
    }

    public static ListTag writeQuaternion(@NotNull Quaterniond value) {
        return QuaterniondExtensionsKt.serializeNBT(value);
    }

    public static Vector3d readVector3(@NotNull ListTag nbt) {
        return Vector3dExtensionsKt.deserializeNBT(new Vector3d(), nbt);
    }

    public static Vector3d readVector3(@NotNull CompoundTag nbt, @NotNull String key) {
        return readVector3(nbt.getList(key, Tag.TAG_DOUBLE));
    }

    public static ListTag writeVector3(@NotNull Vector3d value) {
        return Vector3dExtensionsKt.serializeNBT(value);
    }

}
