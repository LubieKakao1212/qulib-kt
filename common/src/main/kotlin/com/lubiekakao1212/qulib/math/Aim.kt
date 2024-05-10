package com.lubiekakao1212.qulib.math

import net.minecraft.util.math.Direction
import org.joml.Math
import org.joml.Quaterniond
import org.joml.Vector3d
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.sqrt

class Aim(pitchIn : Double, yawIn : Double) {

    var pitch = pitchIn
        set(value) {
            field = Math.clamp(-PI / 2, PI / 2, value)
        }

    var yaw = yawIn
        set(value) {
            field = value.loopAngle()
        }

    fun stepPerAxis(target : Aim, maxDeltaPerAxis: Double, dst : Aim = this) : Aim {
        return stepPerAxis(target, maxDeltaPerAxis, maxDeltaPerAxis, dst)
    }

    fun stepPerAxis(target : Aim, maxDeltaP: Double, maxDeltaY: Double, dst : Aim = this) : Aim {
        dst.pitch = pitch.stepTo(target.pitch, maxDeltaP)
        dst.yaw = yaw.stepToAngle(target.yaw, maxDeltaY)
        return dst
    }

    fun set(target : Aim) : Aim {
        this.pitch = target.pitch
        this.yaw = target.yaw
        return this
    }

    fun stepRaw(target : Aim, maxDelta : Double, dst : Aim = this) : Aim {
        val dp = pitch.angleDistanceTo(target.pitch)
        val dy = yaw - target.yaw

        val l = sqrt(dp * dp + dy * dy)

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
        dst.pitch = lerpAngle(pitch, b.pitch, t)
        dst.yaw = lerp(yaw, b.yaw, t)
        return dst
    }

    fun equals(other : Aim, epsilon : Double = Constants.epsilon) : Boolean {
        return equals(other, epsilon, epsilon)
    }

    fun equals(other : Aim, pitchEpsilon : Double = Constants.epsilon, yawEpsilon : Double = Constants.epsilon) : Boolean {
        val dp = this.pitch.angleDistanceTo(other.pitch)
        val dy = abs(this.yaw - other.yaw)

        return dp < pitchEpsilon && dy < yawEpsilon
    }

    fun toQuaternion() : Quaterniond {
        return Quaterniond().aimRad(pitch, yaw)
    }

    fun toQuaternion(left: Vector3d, up: Vector3d) : Quaterniond {
        return Quaterniond().aimRad(pitch, yaw, left, up)
    }

    fun toQuaternion(leftOrientation: Direction, upOrientation: Direction) : Quaterniond {
        return Quaterniond().aimRad(pitch, yaw, leftOrientation, upOrientation)
    }

}