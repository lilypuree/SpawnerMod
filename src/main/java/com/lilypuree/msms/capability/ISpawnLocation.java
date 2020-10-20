package com.lilypuree.msms.capability;

import net.minecraft.util.math.BlockPos;

public interface ISpawnLocation {

    BlockPos getSpawnPos();

    void setSpawnPos(BlockPos pos);
}