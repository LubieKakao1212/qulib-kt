package com.LubieKakao1212.qulib.math.extensions

import com.LubieKakao1212.qulib.math.MathUtil
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.nbt.DoubleTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.Tag
import net.minecraft.world.phys.Vec3
import org.joml.Quaterniond
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

    fun Vector3d.from(pos: BlockPos) : Vector3d {
        this.x = pos.x.toDouble()
        this.y = pos.y.toDouble()
        this.z = pos.z.toDouble()
        return this;
    }

    fun Vector3d.from(pos: Vec3) : Vector3d {
        this.x = pos.x
        this.y = pos.y
        this.z = pos.z
        return this;
    }

    fun Vector3d.from(dir: Direction) : Vector3d {
        when(dir){
           Direction.SOUTH -> this.set(Consts.SOUTH)
           Direction.NORTH -> this.set(Consts.NORTH)
           Direction.EAST -> this.set(Consts.EAST)
           Direction.WEST -> this.set(Consts.WEST)
           Direction.UP -> this.set(Consts.UP)
           Direction.DOWN -> this.set(Consts.DOWN)
        }
        return this;
    }

    fun Vector3d.equalsApprox(two: Vector3d, epsilon: Double = MathUtil.epsilon): Boolean {
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

    fun Vector3d.serializeNBT() : ListTag {
        val tag = ListTag();
        tag.add(DoubleTag.valueOf(this.x))
        tag.add(DoubleTag.valueOf(this.y))
        tag.add(DoubleTag.valueOf(this.z))
        return tag
    }

    fun Vector3d.deserializeNBT(tag : ListTag) : Vector3d {
        if(tag.elementType != Tag.TAG_ANY_NUMERIC && tag.count() != 3){
            this.set(
                tag.getDouble(0),
                tag.getDouble(1),
                tag.getDouble(2)
            )
        }
        return this;
    }
}