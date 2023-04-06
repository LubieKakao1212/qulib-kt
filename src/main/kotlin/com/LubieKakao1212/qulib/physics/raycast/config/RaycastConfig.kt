package com.LubieKakao1212.qulib.physics.raycast.config

import com.LubieKakao1212.qulib.physics.raycast.RaycastHit

class RaycastConfig(private val action : (List<RaycastHit<*>>) -> List<RaycastHit<*>>) : IRaycastConfig {

    override fun apply(raycastIn: List<RaycastHit<*>>): List<RaycastHit<*>> {
        return action(raycastIn)
    }

    fun addFilter(filter : (RaycastHit<*>) -> Boolean) : RaycastConfig {
        return RaycastConfig {
            action(it).filter(filter)
        }
    }

    fun addTake() {

    }

    fun <R : Comparable<R>> addSorting(order : (RaycastHit<*>) -> R?) : RaycastConfig {
        return RaycastConfig {
            action(it).sortedBy(order)
        }
    }

}