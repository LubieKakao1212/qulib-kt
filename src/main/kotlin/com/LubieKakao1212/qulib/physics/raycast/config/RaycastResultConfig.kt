package com.LubieKakao1212.qulib.physics.raycast.config

import com.LubieKakao1212.qulib.physics.raycast.RaycastHit

class RaycastResultConfig(private val action : (List<RaycastHit<*>>) -> List<RaycastHit<*>>) {

    fun apply(raycastIn: List<RaycastHit<*>>): List<RaycastHit<*>> {
        return action(raycastIn)
    }

    fun filtering(filter : (RaycastHit<*>) -> Boolean) : RaycastResultConfig {
        return RaycastResultConfig {
            action(it).filter(filter)
        }
    }

    fun taking(predicate : (RaycastHit<*>) -> Boolean): RaycastResultConfig {
        return RaycastResultConfig {
            action(it).takeWhile(predicate)
        }
    }

    fun <R : Comparable<R>> sorting(order : (RaycastHit<*>) -> R?) : RaycastResultConfig {
        return RaycastResultConfig {
            action(it).sortedBy(order)
        }
    }

}