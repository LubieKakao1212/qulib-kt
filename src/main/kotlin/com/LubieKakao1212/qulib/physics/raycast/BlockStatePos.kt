package com.LubieKakao1212.qulib.physics.raycast

import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

data class BlockStatePos(val state : BlockState, val pos : BlockPos, val level : Level)
