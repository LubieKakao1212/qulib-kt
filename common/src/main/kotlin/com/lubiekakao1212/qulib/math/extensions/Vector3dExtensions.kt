package com.lubiekakao1212.qulib.math.extensions

import com.lubiekakao1212.qulib.math.Constants
import net.minecraft.nbt.NbtDouble
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtList
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import org.joml.Vector3d

object Vector3dExtensions {

    object Consts {
        val SOUTH = Vector3d(0.0, 0.0, 1.0)
        val NORTH = Vector3d(0.0, 0.0, -1.0)
        val EAST = Vector3d(1.0, 0.0, 0.0)
        val WEST = Vector3d(-1.0, 0.0, 0.0)
        val UP = Vector3d(0.0, 1.0, 0.0)
        val DOWN = Vector3d(0.0, -1.0, 0.0)

        val ONE = Vector3d(1.0, 1.0, 1.0)
    }

    val SOUTH
        get() = Vector3d(Consts.SOUTH)

    val NORTH
        get() = Vector3d(Consts.NORTH)

    val EAST
        get() = Vector3d(Consts.EAST)

    val WEST
        get() = Vector3d(Consts.WEST)

    val UP
        get() = Vector3d(Consts.UP)

    val DOWN
        get() = Vector3d(Consts.DOWN)

}

fun Vector3d.fromCorner(pos: BlockPos) : Vector3d {
    this.x = pos.x.toDouble()
    this.y = pos.y.toDouble()
    this.z = pos.z.toDouble()
    return this;
}

fun Vector3d.fromCenter(pos: BlockPos) : Vector3d {
    this.x = pos.x.toDouble() + 0.5
    this.y = pos.y.toDouble() + 0.5
    this.z = pos.z.toDouble() + 0.5
    return this
}

fun Vector3d.from(pos: Vec3d) : Vector3d {
    this.x = pos.x
    this.y = pos.y
    this.z = pos.z
    return this
}

fun Vector3d.from(dir: Direction) : Vector3d {
    when(dir){
        Direction.SOUTH -> this.set(Vector3dExtensions.Consts.SOUTH)
        Direction.NORTH -> this.set(Vector3dExtensions.Consts.NORTH)
        Direction.EAST -> this.set(Vector3dExtensions.Consts.EAST)
        Direction.WEST -> this.set(Vector3dExtensions.Consts.WEST)
        Direction.UP -> this.set(Vector3dExtensions.Consts.UP)
        Direction.DOWN -> this.set(Vector3dExtensions.Consts.DOWN)
    }
    return this
}

fun Vector3d.equalsApprox(two: Vector3d, epsilon: Double = Constants.epsilon): Boolean {
    return this.distanceSquared(two) < epsilon * epsilon
}

/**
 * a.axisComparison(b) = b.axisComparison(a)
 * @return squared distance from [this] point to [two] axis multiplied by square magnitude of [two]
 */
fun Vector3d.axisComparison(two: Vector3d) : Double {
    val d = this.dot(two)
    val l = this.lengthSquared() * two.lengthSquared()
    return l - d * d
}

fun Vector3d.serializeNBT() : NbtList {
    val tag = NbtList()
    tag.add(NbtDouble.of(this.x))
    tag.add(NbtDouble.of(this.y))
    tag.add(NbtDouble.of(this.z))
    return tag
}

fun Vector3d.deserializeNBT(tag : NbtList) : Vector3d {
    if(tag.heldType != NbtElement.NUMBER_TYPE && tag.count() != 3){
        this.set(
            tag.getDouble(0),
            tag.getDouble(1),
            tag.getDouble(2)
        )
    }
    return this
}

/**
 * May miscalculate for non normalized vectors
 */
fun Vector3d.anyPerpendicular() : Vector3d {
    return if (this.axisComparison(Vector3dExtensions.Consts.SOUTH) < Constants.epsilon * Constants.epsilon) {
        Vector3d(0.0, -z, y)
    } else Vector3d(-y, x, 0.0)
}

operator fun Vector3d.plus(other : Vector3d) : Vector3d {
    return this.add(other, Vector3d())
}

operator fun Vector3d.minus(other : Vector3d) : Vector3d {
    return this.sub(other, Vector3d())
}