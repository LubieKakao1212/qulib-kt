package com.LubieKakao1212.qulib.math

object Constants {
    const val degToRad = (Math.PI / 180.0).toFloat()
    const val radToDeg = (180.0 / Math.PI).toFloat()
    const val piHalf = Math.PI.toFloat() / 2f
    const val pi = Math.PI.toFloat()

    const val epsilon = 0.001
}
fun loop(a: Double, min: Double, max: Double): Double {
    val b = a - min
    val c = b % (max - min)
    return c + min
}

fun loop(a: Float, min: Float, max: Float): Float {
    val b = a - min
    val c = b % (max - min)
    return c + min
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