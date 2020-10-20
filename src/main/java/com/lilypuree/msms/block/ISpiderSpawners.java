package com.lilypuree.msms.block;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ISpiderSpawners {

    void afterSpawn(World world, BlockState state, BlockPos pos);

}
