package com.LubieKakao1212.qulib.physics.raycast.config

import com.LubieKakao1212.qulib.physics.raycast.RaycastHit

interface IRaycastConfig {

    fun apply(raycastIn : List<RaycastHit<*>>) : List<RaycastHit<*>>

}