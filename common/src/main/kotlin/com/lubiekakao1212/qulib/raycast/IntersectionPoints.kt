package com.lubiekakao1212.qulib.raycast

import org.joml.Vector3d

data class IntersectionPoints(val distanceMin : Double, val distanceMax : Double, val pointNear : Vector3d, val pointFar : Vector3d) {
    fun isValid(): Boolean {
        return distanceMin < distanceMax
    }

    fun isInside(): Boolean {
        return isValid() && distanceMin < 0.0 && distanceMax > 0.0
    }

    fun isInsideOrIntersects() : Boolean {
        return isValid() && distanceMax > 0.0
    }

    fun isOutsideAndIntersects() : Boolean {
        return isValid() && distanceMin > 0.0
    }
}
