package com.LubieKakao1212.qulib.physics.raycast

import com.LubieKakao1212.qulib.QuLib

class RaycastResult(private val hits : List<RaycastHit<*>>, sorted : Boolean = false) {

    var isSorted = sorted
        private set

    fun filtered(filter : (RaycastHit<*>) -> Boolean) : RaycastResult {
        return RaycastResult(hits.filter(filter), isSorted)
    }

    fun takeUntil(predicate : (RaycastHit<*>) -> Boolean, warnUnsorted : Boolean = true) : RaycastResult {
        if(warnUnsorted && !isSorted) {
            QuLib.LOGGER.warn("takeUntil on unsorted Raycast may result in unwanted behaviour")
        }
        return RaycastResult(hits.takeWhile(predicate), isSorted)
    }

    fun <R : Comparable<R>> sort(order : (RaycastHit<*>) -> R?): RaycastResult {
        return RaycastResult(hits.sortedBy(order), isSorted)
    }

    fun byConfiguration() : RaycastResult {

    }

}