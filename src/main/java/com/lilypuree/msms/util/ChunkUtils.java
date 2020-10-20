package com.lilypuree.msms.util;

import com.lilypuree.msms.block.BoneDepositBlock;
import com.lilypuree.msms.capability.ChunkSpawnPointProvider;
import com.lilypuree.msms.capability.IChunkSpawnPoints;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSection;

import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

public class ChunkUtils {

    public static void scanChunkForSpawners(Chunk chunk, IChunkSpawnPoints spawnPoints) {
        spawnPoints.initialize();
        for (ChunkSection section : chunk.getSections()) {
            if (section != null && !section.isEmpty()) {
                int minY = section.bottomBlockY();
                ChunkPos cp = chunk.getPos();
                BlockPos.betweenClosed(cp.getMinBlockX(), minY, cp.getMinBlockZ(), cp.getMaxBlockX(), minY + 15, cp.getMaxBlockZ()).forEach(pos -> {

                    BlockState state = section.getBlockState(pos.getX() & 15, pos.getY() & 15, pos.getZ() & 15);
                    if (state.getBlock() instanceof BoneDepositBlock) {
                        spawnPoints.addSpawnPoint(pos);
                    }
                });
            }
        }
    }

    public static void addSpawnPosToChunk(Chunk chunk, BlockPos pos) {
        chunk.getCapability(ChunkSpawnPointProvider.CHUNK_SPAWN_POINTS).ifPresent(spawnPoints -> {
            spawnPoints.addSpawnPoint(pos);
        });
    }

    public static void removeSpawnPosFromChunk(Chunk chunk, BlockPos pos) {
        chunk.getCapability(ChunkSpawnPointProvider.CHUNK_SPAWN_POINTS).ifPresent(spawnPoints -> {
            spawnPoints.removeSpawnPoint(pos);
        });
    }

    public static Collection<BlockPos> getNearbySpawners(World world, int x, int y, int z, int maxDistance, int maxVertical) {
        Collection<BlockPos> spawners = new HashSet<>();

        int minChunkX = (x - maxDistance) >> 4;
        int maxChunkX = (x + maxDistance) >> 4;
        int minChunkZ = (z - maxDistance) >> 4;
        int maxChunkZ = (z + maxDistance) >> 4;

        for (int i = minChunkX; i <= maxChunkX; i++) {
            for (int j = minChunkZ; j <= maxChunkZ; j++) {
                Chunk chunk = world.getChunk(i, j);
                chunk.getCapability(ChunkSpawnPointProvider.CHUNK_SPAWN_POINTS).ifPresent(spawnPoints -> {
                    spawners.addAll(spawnPoints.getSpawnPoints().stream().filter(pos -> {
                        boolean horizontal = (x - pos.getX()) * (x - pos.getX()) + (z - pos.getZ()) * (z - pos.getZ()) <= maxDistance * maxDistance;
                        boolean vertical = Math.abs(y - pos.getY()) <= maxVertical;
                        return horizontal && vertical;
                    }).collect(Collectors.toSet()));
                });
            }
        }
        return spawners;
    }
}
