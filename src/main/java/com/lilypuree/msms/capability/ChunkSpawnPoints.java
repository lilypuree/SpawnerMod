package com.lilypuree.msms.capability;

import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChunkSpawnPoints implements IChunkSpawnPoints {

    Set<BlockPos> spawnPoints = null;

    private boolean virgin = true;

    @Override
    public Set<BlockPos> getSpawnPoints() {
        return spawnPoints;
    }

    @Override
    public void removeSpawnPoint(BlockPos pos) {
        if (spawnPoints != null) {
            spawnPoints.remove(pos);
        }
    }

    @Override
    public void addSpawnPoint(BlockPos pos) {
        if (spawnPoints == null) {
            initialize();
        }
        spawnPoints.add(pos);
    }


    @Override
    public void initialize() {
        this.spawnPoints = new HashSet<>();
    }

    @Override
    public void reset() {
        spawnPoints = null;
    }
}