package com.LubieKakao1212.qulib.math

import com.LubieKakao1212.qulib.math.extensions.*
import net.minecraft.core.Direction
import org.joml.Quaterniond
import org.joml.Vector3d
import java.util.*

fun Random.randomSpread(aim: Quaterniond, maxSpread: Double, forward : Vector3d = Vector3dExtensions.NORTH) : Vector3d {
    return fixedSpread(aim, nextDouble() * maxSpread / 2.0, forward)
}

fun Random.fixedSpread(aim: Quaterniond, spread: Double, forward : Vector3d = Vector3dExtensions.NORTH) : Vector3d {
    return calculateForwardWithSpread(aim, spread, nextDouble() * Math.PI * 2.0, forward)
}

fun calculateForwardWithSpread(aim : Quaterniond, spread: Double, roll : Double, forward : Vector3d = Vector3dExtensions.NORTH) : Vector3d {
    return calculateForwardWithSpread(spread, roll, aim * forward)
}

fun calculateForwardWithSpread(spread: Double, roll : Double, forward : Vector3d) : Vector3d {
    val side = forward.anyPerpendicular()

    val rotSide = Quaterniond().fromAxisAngleDeg(side, spread)

    val rotRound = Quaterniond().fromAxisAngleRad(forward, roll)

    return rotRound.mul(rotSide).transform(forward)
}

fun Quaterniond.aimDeg(pitch: Double, yaw: Double): Quaterniond {
    return this.aimRad(pitch * Constants.degToRad, yaw * Constants.degToRad)
}

fun Quaterniond.aimRad(pitch: Double, yaw: Double): Quaterniond {
    return aimRad(pitch, yaw, Direction.WEST, Direction.UP)
}

fun Quaterniond.aimRad(pitch: Double, yaw: Double, leftOrientation: Direction, upOrientation: Direction): Quaterniond {
    return aimRad(pitch, yaw, Vector3d().from(leftOrientation), Vector3d().from(upOrientation))
}

fun Quaterniond.aimRad(pitch: Double, yaw: Double, left: Vector3d, up: Vector3d): Quaterniond {
    return this.fromAxisAngleRad(up, -yaw).mul(Quaterniond().fromAxisAngleRad(left, -pitch))
}