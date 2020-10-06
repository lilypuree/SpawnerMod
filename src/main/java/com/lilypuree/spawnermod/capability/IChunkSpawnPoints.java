package com.lilypuree.spawnermod.capability;

import net.minecraft.util.math.BlockPos;

import java.util.List;
import java.util.Map;

public interface IChunkSpawnPoints {

    List<BlockPos> getSpawnPoints();

    void removeSpawnPoint(BlockPos pos);

    void addSpawnPoint(BlockPos pos);

    void reset();
}
