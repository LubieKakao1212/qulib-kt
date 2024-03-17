package com.lubiekakao1212.qulib.raycast

import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

data class BlockStatePos(val state : BlockState, val pos : BlockPos, val level : World)
