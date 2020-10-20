package com.lilypuree.msms.spawners;

import com.lilypuree.msms.MSMSMod;
import com.lilypuree.msms.block.BoneDepositBlock;
import com.lilypuree.msms.block.ISpiderSpawners;
import com.lilypuree.msms.capability.*;
import com.lilypuree.msms.util.ChunkUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.spawner.WorldEntitySpawner;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;
import java.util.*;

public class MobAwakener {

    public static void onPlayerTick(PlayerEntity player) {
        long curTick = player.getCommandSenderWorld().getGameTime();

        LazyOptional<IPlayerAggroHandler> handler = player.getCapability(PlayerAggroHandlerProvider.AGGRO_HANDLER_CAP);
        handler.ifPresent(aggroHandler -> {
            long lastTick = aggroHandler.getLastSpawnTick();
            if (curTick - lastTick > MSMSMod.serverConfigs.getAggroInterval()) {
                if (alarmNearbyMobs(player.getCommandSenderWorld(), player.blockPosition(), aggroHandler.getNoise())) {

                }
                aggroHandler.setLastSpawnTick(curTick);
            }
        });
    }


    public static boolean alarmNearbyMobs(World world, BlockPos pos, int noiseLevel) {
        Set<BlockPos> removed = new HashSet<>();
        Collection<BlockPos> spawnedBoneDeposits = new ArrayList<>();
        Collection<BlockPos> spawnedSpiders = new ArrayList<>();
        ChunkUtils.getNearbySpawners(world, pos.getX(), pos.getY(), pos.getZ(), 12, 12).forEach(spawnPos -> {
            int i = getMaxSpawns();
            BlockState spawner = world.getBlockState(spawnPos);
            Block block = spawner.getBlock();
            if (i > 0) {
//                if (block instanceof BoneDepositBlock && handleBoneDepositSpawns(world, pos, spawnPos, spawner, noiseLevel)) {
//                    i -= 1;
//                    spawnedBoneDeposits.add(spawnPos);
//                } else
                if (block instanceof ISpiderSpawners && handleSpiderSpawns(world, pos, spawnPos, spawner, noiseLevel)) {
                    i -= 1;
                    spawnedSpiders.add(spawnPos);
                }
            } else {
                removed.add(spawnPos);
            }

        });
        for (BlockPos removedPos : removed) {
            ChunkUtils.removeSpawnPosFromChunk(world.getChunkAt(removedPos), removedPos);
        }
//        for (BlockPos spawnPos : spawnedBoneDeposits) {
//            BlockState spawner = world.getBlockState(spawnPos);
//            ((BoneDepositBlock) spawner.getBlock()).decreasePotency(world, spawner, spawnPos);
//        }
        for (BlockPos spawnPos : spawnedSpiders) {
            BlockState spawner = world.getBlockState(spawnPos);
            ((ISpiderSpawners) spawner.getBlock()).afterSpawn(world, spawner, spawnPos);
        }
        return !spawnedSpiders.isEmpty() || !spawnedBoneDeposits.isEmpty();
    }

    public static int getMaxSpawns() {
        return 4;
    }

    public static boolean handleBoneDepositSpawns(World world, BlockPos alarm, BlockPos spawn, BlockState spawner, int noiseLevel) {
        boolean isInDistance = alarm.distSqr(spawn) < 100.0f;
        if (isInDistance) {
            BoneDepositBlock block = (BoneDepositBlock) spawner.getBlock();
            int sensitivity = block.getSensitivity();
            if (noiseLevel > sensitivity) {
                Optional<BlockPos> spawnPos = findSpawnableGroundVertical(world, spawn, MSMSMod.serverConfigs.getBoneDepositVerticalRange(), MSMSMod.serverConfigs.getBoneDepositRadius(), 100, EntityType.SKELETON);
                if (spawnPos.isPresent()) {
                    MobEntity mobEntity = spawnMobAt(world, spawnPos.get(), EntityType.SKELETON);
                    mobEntity.getCapability(SpawnLocationProvider.SPAWN_LOCATION_CAP).ifPresent(spawnLoc -> spawnLoc.setSpawnPos(spawn));
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean handleSpiderSpawns(World world, BlockPos alarm, BlockPos spawn, BlockState spawner, int noiseLevel) {
        double dist = alarm.distSqr(spawn);
        boolean isInDistance = dist < 100.0f;
        if (isInDistance && noiseLevel > 30) {

            Optional<BlockPos> spawnPos = findNearSpawnableGround(world, spawn, alarm, 4, Math.sqrt(dist) + 3, EntityType.SPIDER);
            if (spawnPos.isPresent()) {
                MobEntity mobEntity = spawnMobAt(world, spawnPos.get(), EntityType.SPIDER);
                mobEntity.getCapability(SpawnLocationProvider.SPAWN_LOCATION_CAP).ifPresent(spawnLoc -> spawnLoc.setSpawnPos(spawn));

                return true;
            }
        }
        return false;
    }

    private static MobEntity spawnMobAt(World world, BlockPos pos, EntityType<?> type) {
        MobEntity mobEntity = (MobEntity) type.create(world);
        if (mobEntity != null) {
            mobEntity.moveTo(pos, world.getRandom().nextFloat() * 360F, 0.0F);
            if (mobEntity.checkSpawnObstruction(world) && world.noCollision(mobEntity)) {
                mobEntity.finalizeSpawn((IServerWorld) world, world.getCurrentDifficultyAt(mobEntity.blockPosition()), SpawnReason.MOB_SUMMONED, null, null);
                ((IServerWorld) world).addFreshEntityWithPassengers(mobEntity);
                mobEntity.spawnAnim();
            }
        }
        return mobEntity;
    }


    private static Optional<BlockPos> findNearSpawnableGround(World world, BlockPos center, BlockPos target, int range, double maxDistance, EntityType<?> type) {
        EntitySpawnPlacementRegistry.PlacementType placementType = EntitySpawnPlacementRegistry.getPlacementType(type);
        BlockPos.Mutable nextPos = new BlockPos.Mutable();
        Random r = world.getRandom();
        int counter = range * 2 + 1;
        counter = Math.floorDiv(counter * counter * counter, 10);

        BlockPos start = center.offset(-range, -range, -range);
        while (counter > 0) {
            nextPos.set(start.getX() + r.nextInt(range * 2), start.getY() + r.nextInt(range * 2), start.getZ() + r.nextInt(range * 2));
            if (nextPos.distSqr(target) > maxDistance * maxDistance) continue;
            if (isSpawnPositionOk(placementType, world, nextPos, type)) {
                return Optional.of(nextPos);
            }
            counter--;
        }
        return Optional.empty();
    }

    public static Optional<BlockPos> findSpawnableGroundVertical(World world, BlockPos pos, int range, int radius, int count, EntityType<?> type) {
        EntitySpawnPlacementRegistry.PlacementType placementType = EntitySpawnPlacementRegistry.getPlacementType(type);
        BlockPos.Mutable nextPos = new BlockPos.Mutable();
        Random r = world.getRandom();
        for (BlockPos spawnPos : BlockPos.randomBetweenClosed(r, radius * radius * 3, pos.getX() - radius, pos.getY(), pos.getZ() - radius, pos.getX() + radius, pos.getY(), pos.getZ() + radius)) {

            nextPos.set(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
            for (int y = 0; y < range + 1; y++) {
                if ((count-- > 0) && isSpawnPositionOk(placementType, world, nextPos, type)) {
//                        && EntitySpawnPlacementRegistry.checkSpawnRules(type, (IServerWorld) world, SpawnReason.NATURAL, spawnPos, r)) {
                    return Optional.of(nextPos.immutable());
                } else {
                    nextPos.move(Direction.UP);
                }
            }
        }
        return Optional.empty();
    }


    public static boolean isSpawnPositionOk(EntitySpawnPlacementRegistry.PlacementType placementType, IWorldReader world, BlockPos pos, @Nullable EntityType<?> type) {
        if (placementType == EntitySpawnPlacementRegistry.PlacementType.NO_RESTRICTIONS) {
            return true;
        } else if (type != null && world.getWorldBorder().isWithinBounds(pos)) {
            return canSpawnAtBody(placementType, world, pos, type);
        }
        return false;
    }

    public static boolean canSpawnAtBody(EntitySpawnPlacementRegistry.PlacementType placementType, IWorldReader world, BlockPos pos, @Nullable EntityType<?> type) {
        {
            BlockState blockstate = world.getBlockState(pos);
            FluidState fluidstate = world.getFluidState(pos);
            BlockPos above = pos.above();
            BlockPos below = pos.below();
            switch (placementType) {
                case IN_WATER:
                    return fluidstate.is(FluidTags.WATER) && world.getFluidState(below).is(FluidTags.WATER) && !world.getBlockState(above).isRedstoneConductor(world, above);
                case IN_LAVA:
                    return fluidstate.is(FluidTags.LAVA);
                case ON_GROUND:
                default:
                    BlockState underBlock = world.getBlockState(below);

                    if (!underBlock.isFaceSturdy(world, below, Direction.UP) || world.getLightEngine().getLayerListener(LightType.SKY).getLightValue(pos) - world.getSkyDarken() > 7) {
                        return false;
                    } else {
                        return WorldEntitySpawner.isValidEmptySpawnBlock(world, pos, blockstate, fluidstate, type) && WorldEntitySpawner.isValidEmptySpawnBlock(world, above, world.getBlockState(above), world.getFluidState(above), type);
                    }
            }
        }
    }

}