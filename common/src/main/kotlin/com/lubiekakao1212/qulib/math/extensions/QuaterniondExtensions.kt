package com.lubiekakao1212.qulib.math.extensions

import net.minecraft.nbt.NbtDouble
import net.minecraft.nbt.NbtList
import net.minecraft.nbt.NbtElement
import org.joml.Quaterniond
import org.joml.Vector3d
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

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

fun Quaterniond.serializeNBT() : NbtList {
    val tag = NbtList();
    tag.add(NbtDouble.of(this.x))
    tag.add(NbtDouble.of(this.y))
    tag.add(NbtDouble.of(this.z))
    tag.add(NbtDouble.of(this.w))
    return tag
}

fun Quaterniond.deserializeNBT(tag : NbtList) : Quaterniond {
    if(tag.heldType != NbtElement.NUMBER_TYPE && tag.count() != 4) {
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