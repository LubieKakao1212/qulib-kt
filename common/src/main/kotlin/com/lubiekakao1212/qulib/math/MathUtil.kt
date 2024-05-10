package com.lubiekakao1212.qulib.math

import net.minecraft.structure.OceanMonumentGenerator.DoubleYRoom
import kotlin.math.*

object Constants {
    const val degToRad = (PI / 180.0).toFloat()
    const val radToDeg = (180.0 / Math.PI).toFloat()
    const val piHalf = PI.toFloat() / 2f
    const val pi = PI.toFloat()

    const val TAU = PI * 2
    const val tau = pi * 2

    const val epsilon = 0.001
}

fun loop(a: Double, min: Double, max: Double) : Double {
    val b = a - min
    val c = b.mod(max - min)
    return c + min
}

fun loop(a: Float, min: Float, max: Float) : Float {
    val b = a - min
    val c = b.mod(max - min)
    return c + min
}

/**
 *  @return value in range -pi, pi
 */
fun Double.loopAngle() : Double {
    return (this + PI).mod(Constants.TAU) - PI
}

private fun Double.loopAngle2() : Double{
    return this.mod(Constants.TAU) - PI
}


/**
 * Assumes both a and b are in range -pi, pi
 */
fun angleDistance(a : Double, b : Double): Double {
    val a1 = a.loopAngle2()
    val b1 = b.loopAngle2()

    val d1 = abs(b - a)
    val d2 = abs(b1 - a1)
    return min(d1, d2)
}

fun Double.angleDistanceTo(b : Double) : Double {
    return angleDistance(this, b)
}

fun lerpAngle(a : Double, b : Double, t : Double) : Double {
    val a1 = a.loopAngle()
    val b1 = b.loopAngle()

    val b2 = if(a1 > b1) b1 + Constants.TAU else b1 - Constants.TAU

    val b3 = if(
        abs(a1 - b1) <
        abs(a1 - b2)) b1 else b2

    return a1.lerpTo(b3, t).loopAngle()
}

fun stepAngle(a : Double, b : Double, maxDelta : Double) : Double {
    val a1 = a.loopAngle()
    val b1 = b.loopAngle()

    val b2 = if(a1 > b1) b1 + Constants.TAU else b1 - Constants.TAU

    val sd1 = b1 - a1
    val sd2 = b2 - a1

    val d1 = abs(sd1)
    val d2 = abs(sd2)

    val d = min(min(d1, d2), maxDelta)

    return a + d * (if(d1 < d2) sign(sd1) else sign(sd2))
}

fun Double.stepToAngle(b : Double, maxDelta : Double) : Double {
    return stepAngle(this, b, maxDelta)
}

fun Double.stepTo(b : Double, maxDelta : Double) : Double {
    val d1 = b - this
    val d = min(abs(d1), maxDelta)
    return this + sign(d1) * d
}

fun lerp(a: Double, b: Double, t: Double) : Double {
    return  a + (b - a) * t
}

fun lerpPrecise(a: Double, b: Double, t: Double) : Double {
    return (a * (1 - t)) + (b * t);
}

fun Double.lerpTo(b: Double, t: Double) : Double {
    return  this + (b - this) * t
}

fun Double.lerpPreciseTo(b: Double, t: Double) : Double {
    return (this * (1 - t)) + (b * t)
}

fun nonZeroSign(value: Double): Long {
    return negativeSign(value) or 1L
}

fun negativeSign(value: Double): Long {
    return value.toRawBits() shr 63
}

/**
 * @param value should be either -1 or 1
 */
fun nonNegative(value : Int) : Int {
    return (value + 1) shr 1
}