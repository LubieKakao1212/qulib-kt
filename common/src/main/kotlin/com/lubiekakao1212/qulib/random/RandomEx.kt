package com.lubiekakao1212.qulib.random

import org.joml.Vector2d
import org.joml.Vector3d
import java.util.*
import kotlin.math.*

class RandomEx : Random {

    constructor(seed : Long) : super(seed)

    constructor() : super()

    fun randomScaleLinear(deviation: Double): Double {
        var value = sampleDeviation(deviation)
        if (value < 0) {
            value = 1 / (-value + 1)
        } else {
            value += 1.0
        }
        return value
    }

    fun randomScaleExponential(deviation: Double): Double {
        return exp(sampleDeviation(deviation))
    }

    /**
     * @return A Uniformly distributed value in range [-d, d)
     */
    fun sampleDeviation(d: Double): Double {
        if (d == 0.0) {
            return 0.0
        }
        return nextDouble(-d, d)
    }

    fun nextOnCircle(radius : Double = 1.0) : Vector2d {
        val theta = nextDouble()
        return Vector2d(cos(theta), sin(theta)).mul(radius)
    }

    fun nextInCircle(radius : Double = 1.0) : Vector2d {
        val r = sqrt(nextDouble()) * radius
        return nextOnCircle(r)
    }

    fun nextOnSphere(radius : Double = 1.0) : Vector3d {
        var phi = nextDouble() * PI * 2
        var costheta = nextDouble() * 2 - 1
        var theta = acos(costheta)
        var sintheta = sin(theta)

        return Vector3d(
            sintheta * cos(phi),
            sintheta * sin(phi),
            costheta,
        ).mul(radius)
    }

    fun nextInSphere(radius : Double = 1.0) : Vector3d {
        val r = cbrt(nextDouble()) * radius
        return nextOnSphere(r)
    }
}