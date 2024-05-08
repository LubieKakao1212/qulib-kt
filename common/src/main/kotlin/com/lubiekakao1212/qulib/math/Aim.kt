package com.lubiekakao1212.qulib.math

import org.joml.Quaterniond
import kotlin.math.sqrt

class Aim(var pitch : Double, var yaw : Double) {

    fun stepRaw(target : Aim, maxDelta : Double, dst : Aim = this) : Aim {
        val p = target.pitch
        val y = target.yaw

        val l = sqrt(p*p + y*y);

        if(l <= maxDelta) {
            dst.pitch = target.pitch
            dst.yaw = target.yaw
            return dst
        }
        else
        {
            val t = maxDelta / l
            return lerpRaw(target, t, dst)
        }
    }

    fun lerpRaw(b : Aim, t : Double, dst : Aim = this) : Aim {
        dst.pitch = lerp(pitch, b.pitch, t)
        dst.pitch = lerp(yaw, b.yaw, t)
        return dst
    }

    fun toQuaternion() : Quaterniond {
        return Quaterniond().aimRad(pitch, yaw)
    }

}