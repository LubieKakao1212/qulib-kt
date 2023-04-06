package com.LubieKakao1212.qulib.physics.raycast

import org.joml.Vector3d

data class IntersectionPoints(val distanceMin : Double, val distanceMax : Double, val pointNear : Vector3d, val pointFar : Vector3d) {
    fun isValid(): Boolean {
        return isInside() || distanceMin < distanceMax
    }

    fun isInside(): Boolean {
        return (distanceMin < 0.0) xor (distanceMax < 0.0)
    }
}
