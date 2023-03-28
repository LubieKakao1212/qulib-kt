package com.LubieKakao1212.qulib.math

import com.LubieKakao1212.qulib.math.MathUtil.equals
import org.joml.Vector3d
import kotlin.math.abs

object MathUtil {

    const val degToRad = (Math.PI / 180.0).toFloat()
    const val radToDeg = (180.0 / Math.PI).toFloat()
    const val piHalf = Math.PI.toFloat() / 2f
    const val pi = Math.PI.toFloat()

    const val epsilon = 0.001

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


}