package com.LubieKakao1212.qulib.physics.raycast

import com.LubieKakao1212.qulib.QuLib
import com.LubieKakao1212.qulib.math.MathUtil
import com.LubieKakao1212.qulib.physics.raycast.config.RaycastResultConfig
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.AABB
import org.joml.Vector3d
import org.joml.Vector3i
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min


const val raycastEpsilon = 0.001

fun List<RaycastHit<*>>.configure(cfg : RaycastResultConfig) : List<RaycastHit<*>> {
    return cfg.apply(this)
}

fun raycastEntitiesAll(level: Level, origin: Vector3d, direction: Vector3d, range: Double): List<RaycastHit<Entity>>? {
    val end = direction.mulAdd(range, origin, Vector3d())
    val raycastBounds = AABB(origin.x, origin.y, origin.z, end.x, end.y, end.z).inflate(0.5)
    return level.getEntities(null, raycastBounds).map { entity: Entity ->
        RaycastHit(
            intersectionPoints(entity.boundingBox, origin, direction),
            entity
        )
    }.filter { hit: RaycastHit<Entity> ->
            hit.intersection.isValid()
        }
}

fun raycastBlocksFirst(level: Level, origin: Vector3d, direction: Vector3d, range: Double) : RaycastHit<BlockStatePos>? {
    return raycastBlocksUntil(level, origin, direction, range) {
        false
    }.firstOrNull()
}

fun raycastBlocksAll(level: Level, origin: Vector3d, direction: Vector3d, range: Double) : List<RaycastHit<BlockStatePos>> {
    return raycastBlocksUntil(level, origin, direction, range) {
        true
    }
}

fun raycastBlocksUntil(level: Level, origin: Vector3d, direction: Vector3d, range: Double, continuationPredicate : (RaycastHit<BlockStatePos>) -> Boolean): List<RaycastHit<BlockStatePos>> {
    removeZero(direction, raycastEpsilon)
    val result: MutableList<RaycastHit<BlockStatePos>> = mutableListOf()

    val dirX = MathUtil.nonZeroSign(direction.x).toInt()
    val dirY = MathUtil.nonZeroSign(direction.y).toInt()
    val dirZ = MathUtil.nonZeroSign(direction.z).toInt()

    // 1 -> 1
    // -1 -> 0
    val dirXn = MathUtil.nonNegative(dirX).toDouble()
    val dirYn = MathUtil.nonNegative(dirY).toDouble()
    val dirZn = MathUtil.nonNegative(dirZ).toDouble()

    // 1 -> -1 -> 0
    // -1 -> 1 -> 1
    val dirXp = MathUtil.nonNegative(-dirX).toDouble()
    val dirYp = MathUtil.nonNegative(-dirY).toDouble()
    val dirZp = MathUtil.nonNegative(-dirZ).toDouble()

    val originBlockPos = origin.floor(Vector3d())
    val currentBlockPos = Vector3i(originBlockPos.x.toInt(), originBlockPos.y.toInt(), originBlockPos.z.toInt())

    val nextX: Double = currentBlockPos.x + dirXn
    val nextY: Double = currentBlockPos.y + dirYn
    val nextZ: Double = currentBlockPos.z + dirZn

    var tMaxX = solveMax(nextX, origin.x, direction.x)
    var tMaxY = solveMax(nextY, origin.y, direction.y)
    var tMaxZ = solveMax(nextZ, origin.z, direction.z)

    val tMinX = solveMin(currentBlockPos.x + dirXp, origin.x, direction.x)
    val tMinY = solveMin(currentBlockPos.y + dirYp, origin.y, direction.y)
    val tMinZ = solveMin(currentBlockPos.z + dirZp, origin.z, direction.z)

    val tDeltaX = abs(solveMax(dirX.toDouble(), 0.0, direction.x))
    val tDeltaY = abs(solveMax(dirY.toDouble(), 0.0, direction.y))
    val tDeltaZ = abs(solveMax(dirZ.toDouble(), 0.0, direction.z))

    val tMin = tMin(tMaxX, tMinX, tMaxY, tMinY, tMaxZ, tMinZ)
    var tMax = tMax(tMaxX, tMinX, tMaxY, tMinY, tMaxZ, tMinZ)

    //We do not add the block we are in
    addIfValid(
        result, level!!, currentBlockPos, IntersectionPoints(
            tMin, tMax,
            direction.mulAdd(tMin, origin, Vector3d()),
            direction.mulAdd(tMax, origin, Vector3d())
        )
    )
    var i = 0
    while (tMax < range) {
        if (tMaxX < tMaxY && tMaxX < tMaxZ) {
            currentBlockPos.x += dirX
            tMaxX += tDeltaX
        } else if (tMaxY < tMaxZ) {
            currentBlockPos.y += dirY
            tMaxY += tDeltaY
        } else {
            currentBlockPos.z += dirZ
            tMaxZ += tDeltaZ
        }


        //TODO optimise?
        val intersections = intersectionPoints(
            AABB(BlockPos(currentBlockPos.x, currentBlockPos.y, currentBlockPos.z)),
            origin,
            direction
        )
        tMax = intersections.distanceMax
        if(addIfValid(result, level, currentBlockPos, intersections) && !continuationPredicate(result.last()))
        {
            break
        }
        i++
        if (i > 1000) {
            QuLib.LOGGER.info("Raycast reached block limit. Probably a loop")
            break
        }
    }
    return result
}

fun AABB.intersect(origin: Vector3d, direction: Vector3d): IntersectionPoints {
    val dir = removeZero(direction, raycastEpsilon)
    return intersectionPoints(this, origin, dir)
}

private fun intersectionPoints(bounds: AABB, origin: Vector3d, direction: Vector3d): IntersectionPoints {
    val tX1 = solveMin(bounds.minX, origin.x, direction.x)
    val tX2 = solveMax(bounds.maxX, origin.x, direction.x)
    val tY1 = solveMin(bounds.minY, origin.y, direction.y)
    val tY2 = solveMax(bounds.maxY, origin.y, direction.y)
    val tZ1 = solveMin(bounds.minZ, origin.z, direction.z)
    val tZ2 = solveMax(bounds.maxZ, origin.z, direction.z)
    val minT = tMin(tX1, tX2, tY1, tY2, tZ1, tZ2)
    val maxT = tMax(tX1, tX2, tY1, tY2, tZ1, tZ2)
    return IntersectionPoints(
        minT, maxT,
        direction.mulAdd(minT, origin, Vector3d()),
        direction.mulAdd(maxT, origin, Vector3d())
    )
}

private fun solve(plane: Double, origin: Double, direction: Double): Double {
    return (plane - origin) / direction
}

private fun solveMin(plane: Double, origin: Double, direction: Double): Double {
    return if (direction == 0.0) Double.NEGATIVE_INFINITY else solve(plane, origin, direction)
}

private fun solveMax(plane: Double, origin: Double, direction: Double): Double {
    return if (direction == 0.0) Double.POSITIVE_INFINITY else solve(plane, origin, direction)
}

private fun tMin(tX1: Double, tX2: Double, tY1: Double, tY2: Double, tZ1: Double, tZ2: Double): Double {
    return t(tX1, tX2, tY1, tY2, tZ1, tZ2, ::min, ::max)
}

private fun tMax(tX1: Double, tX2: Double, tY1: Double, tY2: Double, tZ1: Double, tZ2: Double): Double {
    return t(tX1, tX2, tY1, tY2, tZ1, tZ2, ::max, ::min)
}

private fun t(
    tX1: Double, tX2: Double,
    tY1: Double, tY2: Double,
    tZ1: Double, tZ2: Double,
    inner: (Double, Double) -> Double,
    outer: (Double, Double) -> Double
): Double {
    return outer(
        outer(
            inner(tX1, tX2),
            inner(tY1, tY2)
        ),
        inner(tZ1, tZ2)
    )
}


private fun removeZero(vector: Vector3d, epsilon: Double): Vector3d {
    var i = 0;
    if (Math.abs(vector.x) < epsilon) {
        vector.x = 0.0
        i++
    }
    if (Math.abs(vector.y) < epsilon) {
        vector.y = 0.0
        i++
    }
    if (Math.abs(vector.z) < epsilon) {
        vector.z = 0.0
        i++
    }
    if(i == 3) {
        return Vector3d(0.0)
    }
    return vector.normalize(Vector3d())
}

private fun addIfValid(
    addTo: MutableList<RaycastHit<BlockStatePos>>,
    level: Level,
    pos: Vector3i,
    intersections: IntersectionPoints
) : Boolean {
    val blockPos = BlockPos(pos.x, pos.y, pos.z)
    val blockState: BlockState = level.getBlockState(blockPos)
    if (!blockState.isAir) {
        addTo.add(
            RaycastHit(
                intersections,
                BlockStatePos(blockState, blockPos, level)
            )
        )
        return true
    }
    return false
}