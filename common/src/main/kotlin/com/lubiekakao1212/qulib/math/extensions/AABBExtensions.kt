package com.lubiekakao1212.qulib.math.extensions

import com.lubiekakao1212.qulib.math.lerp
import net.minecraft.util.math.Box
import org.joml.Vector3d

object AABBExtensions {
    fun fromCorners(corner1 : Vector3d, corner2 : Vector3d) : Box {
        return Box(
            corner1.x, corner1.y, corner1.z,
            corner2.x, corner2.y, corner2.z)
    }

    fun fromCornerSize(corner : Vector3d, size : Vector3d) : Box {
        return Box(
            corner.x, corner.y, corner.z,
            corner.x + size.x, corner.y + size.y, corner.z + size.z)
    }

    fun fromCenterSize(center : Vector3d, size : Vector3d) : Box {
        val x1 = size.x / 2.0
        val y1 = size.y / 2.0
        val z1 = size.z / 2.0
        return Box(
            center.x - x1, center.y - y1, center.z - z1,
            center.x + x1, center.y + y1, center.z + z1)
    }
}

operator fun Box.contains(point: Vector3d) : Boolean {
    return this.contains(point.x, point.y, point.z)
}

fun Box.interpolate(x: Double, y: Double, z: Double, dst : Vector3d = Vector3d()) : Vector3d {
    return dst.set(
        lerp(minX, maxX, x),
        lerp(minY, maxY, y),
        lerp(minZ, maxZ, z),
    )
}

fun Box.sqrDistanceTo(point : Vector3d) : Double {
    var r = 0.0
    r += boxAxisPoint(point.x, minX, maxX)
    r += boxAxisPoint(point.y, minY, maxY)
    r += boxAxisPoint(point.z, minZ, maxZ)
    return r
}

private fun boxAxisPoint(p : Double, min : Double, max : Double) : Double {
    var r = 0.0
    if(p < min) {
        r = min - p
    }
    else if(p > max) {
        r = p - max
    }

    return r * r
}