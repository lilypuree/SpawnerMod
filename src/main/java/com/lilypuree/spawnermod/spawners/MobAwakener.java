package com.lilypuree.spawnermod.spawners;

import com.lilypuree.spawnermod.block.BoneDepositBlock;
import com.lilypuree.spawnermod.capability.ChunkSpawnPointProvider;
import com.lilypuree.spawnermod.capability.IChunkSpawnPoints;
import com.lilypuree.spawnermod.setup.BlockList;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.LazyOptional;

import java.util.Iterator;
import java.util.ListIterator;

public class MobAwakener {


    public static int alarmNearbyMobs(World world, BlockPos pos, int noiseLevel) {
        Chunk chunk = world.getChunkAt(pos);
        LazyOptional<IChunkSpawnPoints> spawnPoints = chunk.getCapability(ChunkSpawnPointProvider.CHUNK_SPAWN_POINTS);
        int i = getMaxSpawnsForChunk(chunk);
        spawnPoints.ifPresent(spawns -> {
            ListIterator<BlockPos> iterator = spawns.getSpawnPoints().listIterator(spawns.getSpawnPoints().size());
            while (i > 0 && iterator.hasPrevious()) {
                BlockPos spawnPos = iterator.previous();
                BlockState spawner = world.getBlockState(spawnPos);
                if (!isBlockSpawner(spawner)) {
                    spawns.removeSpawnPoint(spawnPos);
                    continue;
                } else if (canBlockSpawnMob(world, pos, spawnPos, spawner, noiseLevel)) {

                }


            }
        });

    }

    public static boolean isBlockSpawner(BlockState state) {
        return state.getBlock() instanceof BoneDepositBlock;
    }

    public static int getMaxSpawnsForChunk(Chunk chunk) {
        return 4;
    }

    public static boolean canBlockSpawnMob(World world, BlockPos alarm, BlockPos spawn, BlockState spawner, int noiseLevel) {

        return alarm.distanceSq(spawn) < 10.0f;
    }

    public static boolean handleBoneDepositSpawns(World world, BlockPos alarm, BlockPos spawn, BlockState spawner, int noiseLevel) {
        boolean isInDistance = alarm.distanceSq(spawn) < 10.0f;
        if (isInDistance) {
            BoneDepositBlock block = (BoneDepositBlock) spawner.getBlock();
            int sensitivity = block.getSensitivity();
            if (noiseLevel > sensitivity) {

            }
        }
        return false;
    }


}
