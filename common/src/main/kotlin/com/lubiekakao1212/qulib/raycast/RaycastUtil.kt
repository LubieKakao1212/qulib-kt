package com.lubiekakao1212.qulib.raycast

import com.lubiekakao1212.qulib.QuLibLogger
import com.lubiekakao1212.qulib.math.nonNegative
import com.lubiekakao1212.qulib.math.nonZeroSign
import com.lubiekakao1212.qulib.raycast.config.RaycastResultConfig
import net.minecraft.block.BlockState
import net.minecraft.entity.Entity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.world.World
import org.joml.Vector3d
import org.joml.Vector3i
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min


const val raycastEpsilon = 0.001

fun <T> List<RaycastHit<T>>.configure(cfg : RaycastResultConfig<T>) : List<RaycastHit<T>> {
    return cfg.apply(this)
}

fun raycastEntitiesAll(level: World, origin: Vector3d, direction: Vector3d, range: Double): List<RaycastHit<Entity>> {
    val end = direction.mulAdd(range, origin, Vector3d())
    val raycastBounds = Box(origin.x, origin.y, origin.z, end.x, end.y, end.z).expand(0.5)
    return raycastEntitiesAll(level.getNonSpectatingEntities(Entity::class.java, raycastBounds), origin, direction)
}

fun raycastEntitiesAll(entities : List<Entity>, origin: Vector3d, direction: Vector3d) : List<RaycastHit<Entity>> {
    return entities.map { entity: Entity ->
        RaycastHit(
            intersectionPoints(entity.boundingBox, origin, direction),
            entity
        )
    }.filter { hit: RaycastHit<Entity> ->
        hit.intersection.isInsideOrIntersects()
    }
}


fun raycastBlocksFirst(level: World, origin: Vector3d, direction: Vector3d, range: Double) : RaycastHit<BlockStatePos>? {
    return raycastBlocksUntil(level, origin, direction, range) {
        false
    }.firstOrNull()
}

fun raycastBlocksAll(level: World, origin: Vector3d, direction: Vector3d, range: Double) : List<RaycastHit<BlockStatePos>> {
    return raycastBlocksUntil(level, origin, direction, range) {
        true
    }
}

fun raycastBlocksUntil(level: World, origin: Vector3d, direction: Vector3d, range: Double, enterFirst : Boolean = true, continuationPredicate : (RaycastHit<BlockStatePos>) -> Boolean): List<RaycastHit<BlockStatePos>> {
    val result: MutableList<RaycastHit<BlockStatePos>> = mutableListOf()

    raycastGridUntil(origin, direction, range, enterFirst) {
        addIfValid(
            result, level, it.target, it.intersection
        )
        continuationPredicate(result.last())
    }

    return result
}

fun raycastGridUntil(origin: Vector3d, direction: Vector3d, range: Double, enterSelf : Boolean, enterDelegate : (RaycastHit<Vector3i>) -> Boolean) {
    removeZero(direction, raycastEpsilon)

    val dirX = nonZeroSign(direction.x).toInt()
    val dirY = nonZeroSign(direction.y).toInt()
    val dirZ = nonZeroSign(direction.z).toInt()

    // 1 -> 1
    // -1 -> 0
    val dirXn = nonNegative(dirX).toDouble()
    val dirYn = nonNegative(dirY).toDouble()
    val dirZn = nonNegative(dirZ).toDouble()

    // 1 -> -1 -> 0
    // -1 -> 1 -> 1
    val dirXp = nonNegative(-dirX).toDouble()
    val dirYp = nonNegative(-dirY).toDouble()
    val dirZp = nonNegative(-dirZ).toDouble()

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

    if (enterSelf) {
        if(!enterDelegate(
                RaycastHit(
            IntersectionPoints(
            tMin, tMax,
            direction.mulAdd(tMin, origin, Vector3d()),
            direction.mulAdd(tMax, origin, Vector3d())
        ), currentBlockPos)
            ))
            return
    }

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
            Box(BlockPos(currentBlockPos.x, currentBlockPos.y, currentBlockPos.z)),
            origin,
            direction
        )
        tMax = intersections.distanceMax
        if(!enterDelegate(RaycastHit(intersections, currentBlockPos)))
        {
            break
        }
        i++
        if (i > 1000) {
            QuLibLogger.LOGGER.info("Raycast reached block limit. Probably a loop")
            break
        }
    }
}


fun Box.intersect(origin: Vector3d, direction: Vector3d): IntersectionPoints {
    val dir = removeZero(direction, raycastEpsilon)
    return intersectionPoints(this, origin, dir)
}

private fun intersectionPoints(bounds: Box, origin: Vector3d, direction: Vector3d): IntersectionPoints {
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
    return solve(plane, origin, direction)//if (direction == 0.0) Double.NEGATIVE_INFINITY else solve(plane, origin, direction)
}

private fun solveMax(plane: Double, origin: Double, direction: Double): Double {
    return solve(plane, origin, direction)//if (direction == 0.0) Double.POSITIVE_INFINITY else solve(plane, origin, direction)
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
    var i = 0
    if (abs(vector.x) < epsilon) {
        vector.x = 0.0
        i++
    }
    if (abs(vector.y) < epsilon) {
        vector.y = 0.0
        i++
    }
    if (abs(vector.z) < epsilon) {
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
    level: World,
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