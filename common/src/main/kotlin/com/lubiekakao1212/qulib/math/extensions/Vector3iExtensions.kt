package com.lubiekakao1212.qulib.math.extensions

import net.minecraft.util.math.BlockPos
import org.joml.Vector3i

fun Vector3i.from(pos : BlockPos) : Vector3i {
    this.x = pos.x
    this.y = pos.y
    this.z = pos.z
    return this
}

fun Vector3i.toBlockPos() : BlockPos {
    return BlockPos(x, y, z)
}

