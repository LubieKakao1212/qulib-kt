package com.lubiekakao1212.qulib.raycast.config

import com.lubiekakao1212.qulib.raycast.RaycastHit

class RaycastResultConfig<T>(private val action : (List<RaycastHit<T>>) -> List<RaycastHit<T>>) {

    constructor() : this({
        it
    })

    fun apply(raycastIn: List<RaycastHit<T>>): List<RaycastHit<T>> {
        return action(raycastIn)
    }

    fun filtering(filter : (RaycastHit<T>) -> Boolean) : RaycastResultConfig<T> {
        return RaycastResultConfig {
            action(it).filter(filter)
        }
    }

    fun taking(predicate : (RaycastHit<T>) -> Boolean): RaycastResultConfig<T> {
        return RaycastResultConfig {
            action(it).takeWhile(predicate)
        }
    }

    fun <R : Comparable<R>> sorting(order : (RaycastHit<T>) -> R?) : RaycastResultConfig<T> {
        return RaycastResultConfig {
            action(it).sortedBy(order)
        }
    }

    fun clipping(distance : Double) : RaycastResultConfig<T> {
        return RaycastResultConfig { it ->
            action(it).filter {it2 ->
                it2.intersection.isInside() || it2.intersection.distanceMin < distance
            }
        }
    }
}