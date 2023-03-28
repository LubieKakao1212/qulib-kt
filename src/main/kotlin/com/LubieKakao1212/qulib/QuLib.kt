package com.LubieKakao1212.qulib

import net.minecraftforge.fml.common.Mod
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.joml.Vector3d

/**
 * Main mod class. Should be an `object` declaration annotated with `@Mod`.
 * The modid should be declared in this object and should match the modId entry
 * in mods.toml.
 *
 * An example for blocks is in the `blocks` package of this mod.
 */
@Mod(QuLib.ID)
object QuLib {
    const val ID = "qulib"

    // the logger for our mod
    val LOGGER: Logger = LogManager.getLogger(ID)

    init {
    }

    /*
    *//**
     * This is used for initializing client specific
     * things such as renderers and keymaps
     * Fired on the mod specific event bus.
     *//*
    private fun onClientSetup(event: FMLClientSetupEvent) {
        LOGGER.log(Level.INFO, "Initializing client...")
    }

    *//**
     * Fired on the global Forge bus.
     *//*
    private fun onServerSetup(event: FMLDedicatedServerSetupEvent) {
        LOGGER.log(Level.INFO, "Server starting...")
    }*/
}