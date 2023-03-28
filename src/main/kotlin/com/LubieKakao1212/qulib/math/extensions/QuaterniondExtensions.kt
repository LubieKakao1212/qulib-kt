package com.LubieKakao1212.qulib.math.extensions

import com.LubieKakao1212.qulib.math.extensions.Vector3dExtensions.from
import net.minecraft.nbt.DoubleTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.Tag
import net.minecraftforge.common.util.INBTSerializable
import org.joml.Quaterniond
import org.joml.Vector3d
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

object QuaterniondExtensions {

    infix fun Quaterniond.mul(b: Quaterniond): Quaterniond {
        return this.mul(b, Quaterniond())
    }

    infix fun Quaterniond.angle(b: Quaterniond) : Double {
        return this.difference(b, Quaterniond()).angle()
    }

    infix fun Quaterniond.smallAngle(b: Quaterniond) : Double {
        return PI - abs((this angle b) - PI)
    }

    fun Quaterniond.step(to: Quaterniond, maxAngle: Double, dest: Quaterniond = Quaterniond()) : Quaterniond {
        val angle: Double = this smallAngle to

        var step = maxAngle / angle
        step = min(max(step, 0.0), 1.0)

        return this.slerp(to, step, dest).normalize()
    }

    fun Quaterniond.serializeNBT() : ListTag {
        val tag = ListTag();
        tag.add(DoubleTag.valueOf(this.x))
        tag.add(DoubleTag.valueOf(this.y))
        tag.add(DoubleTag.valueOf(this.z))
        tag.add(DoubleTag.valueOf(this.w))
        return tag
    }

    fun Quaterniond.deserializeNBT(tag : ListTag) : Quaterniond {
        if(tag.elementType != Tag.TAG_ANY_NUMERIC && tag.count() != 4){
            this.set(
                tag.getDouble(0),
                tag.getDouble(1),
                tag.getDouble(2),
                tag.getDouble(3)
            )
        }
        return this;
    }

    operator fun Quaterniond.timesAssign(b: Quaterniond) {
        this.mul(b)
    }

    operator fun Quaterniond.get(i: Int): Double {
        return when (i) {
            0 -> this.x
            1 -> this.y
            2 -> this.z
            3 -> this.w
            else -> throw IndexOutOfBoundsException("$i")
        }
    }

    operator fun Quaterniond.times(v: Vector3d): Vector3d {
        return this.transform(v)
    }

    operator fun Quaterniond.not(): Quaterniond {
        return this.invert(Quaterniond())
    }
}