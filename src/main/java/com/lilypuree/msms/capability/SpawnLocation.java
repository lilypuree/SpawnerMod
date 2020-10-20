package com.lilypuree.msms.capability;

import net.minecraft.util.math.BlockPos;

public class SpawnLocation implements ISpawnLocation {

    private BlockPos pos;

    public SpawnLocation(){
        pos = BlockPos.ZERO;
    }

    @Override
    public BlockPos getSpawnPos() {
        return pos;
    }

    @Override
    public void setSpawnPos(BlockPos pos) {
        this.pos = pos;
    }
}