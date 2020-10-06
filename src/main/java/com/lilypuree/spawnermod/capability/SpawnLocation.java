package com.lilypuree.spawnermod.capability;

import net.minecraft.util.math.BlockPos;

public class SpawnLocation implements ISpawnLocation {

    private BlockPos pos;

    @Override
    public BlockPos getSpawnPos() {
        return pos;
    }

    @Override
    public void setSpawnPos(BlockPos pos) {
        this.pos = pos;
    }
}
