package com.lubiekakao1212.qulib.math.extensions

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

    fun fromCenterSize(corner : Vector3d, size : Vector3d) : Box {
        val x1 = size.x / 2.0
        val y1 = size.y / 2.0
        val z1 = size.z / 2.0
        return Box(
            corner.x - x1, corner.y - y1, corner.z - z1,
            corner.x + x1, corner.y + y1, corner.z + z1)
    }
}

operator fun Box.contains(point: Vector3d) : Boolean {
    return this.contains(point.x, point.y, point.z)
}