package com.lubiekakao1212.qulib.math.mc

import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Position
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.Vec3i
import org.joml.Vector3d
import org.joml.Vector3dc
import org.joml.Vector3ic

class Vector3m(x : Double, y : Double, z : Double) : Vector3d(x, y, z) {

    constructor(v : Double) : this(v, v, v)
    constructor(v : Vector3dc) : this(v.x(), v.y(), v.z())
    constructor(v : Vector3ic) : this(v.x().toDouble(), v.y().toDouble(), v.z().toDouble())
    constructor(v : Position) : this(v.x, v.y, v.z)
    constructor(v : Vec3i) : this(v.x.toDouble(), v.y.toDouble(), v.z.toDouble())

    constructor() : this(0.0)

    fun toVec3d() : Vec3d {
        return Vec3d(x, y, z)
    }

}