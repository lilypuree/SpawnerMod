package com.lilypuree.msms.capability;

import net.minecraft.util.math.BlockPos;

import java.util.List;
import java.util.Set;

public interface IChunkSpawnPoints {

    Set<BlockPos> getSpawnPoints();

    void removeSpawnPoint(BlockPos pos);

    void addSpawnPoint(BlockPos pos);

    void initialize();

    void reset();
}