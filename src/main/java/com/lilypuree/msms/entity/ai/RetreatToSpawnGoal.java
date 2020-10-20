//package com.lilypuree.msms.entity.ai;
//
//import com.lilypuree.msms.block.BoneDepositBlock;
//import com.lilypuree.msms.capability.ISpawnLocation;
//import com.lilypuree.msms.capability.SpawnLocationProvider;
//import com.lilypuree.msms.spawners.MobAwakener;
//import com.lilypuree.msms.util.ChunkUtils;
//import net.minecraft.block.BlockState;
//import net.minecraft.block.Blocks;
//import net.minecraft.entity.ai.RandomPositionGenerator;
//import net.minecraft.entity.ai.goal.Goal;
//import net.minecraft.entity.monster.MonsterEntity;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.math.vector.Vector3d;
//import net.minecraft.world.World;
//
//import java.util.EnumSet;
//import java.util.Optional;
//
//public class RetreatToSpawnGoal extends Goal {
//
//    private MonsterEntity entity;
//    private World world;
//    private BlockPos infiltrateTarget;
//    private BlockPos retreatBlock;
//    private boolean moveFinished;
//
//    public RetreatToSpawnGoal(MonsterEntity entity) {
//        this.entity = entity;
//        this.world = entity.getCommandSenderWorld();
//        retreatBlock = BlockPos.ZERO;
//        infiltrateTarget = BlockPos.ZERO;
//        moveFinished = false;
//        this.setFlags(EnumSet.of(Flag.MOVE));
//    }
//
//    @Override
//    public boolean canUse() {
//        World world = entity.getCommandSenderWorld();
//        if (!world.dimensionType().hasFixedTime() && world.getSkyDarken() < 5) {
//            long time = world.getDayTime() % 24000L;
//            return time > 22800;
//        }
//        return false;
//    }
//
//    @Override
//    public boolean canContinueToUse() {
//        if (entity.getNavigation().isDone()) {
//            BlockState spawner = world.getBlockState(retreatBlock);
//            int potency = spawner.getValue(BoneDepositBlock.POTENCY);
//            if (isRetreatableBoneDeposit(spawner)) {
//                world.setBlockAndUpdate(retreatBlock, spawner.setValue(BoneDepositBlock.POTENCY, potency + 1));
//                entity.remove();
//                moveFinished = true;
//            }
//            return false;
//        }
//        return !retreatBlock.equals(BlockPos.ZERO);
//    }
//
//
//    @Override
//    public void stop() {
//        if (moveFinished) {
//        }
//        retreatBlock = BlockPos.ZERO;
//        infiltrateTarget = BlockPos.ZERO;
//    }
//
//    @Override
//    public void start() {
//        world = entity.getCommandSenderWorld();
//        infiltrateTarget = entity.getCapability(SpawnLocationProvider.SPAWN_LOCATION_CAP).map(ISpawnLocation::getSpawnPos).orElse(BlockPos.ZERO);
//        if (!infiltrateTarget.equals(BlockPos.ZERO) && infiltrateTarget.distSqr(entity.blockPosition()) < 2000 && isRetreatableBoneDeposit(world.getBlockState(infiltrateTarget))) {
//
//            Optional<BlockPos> retreat = MobAwakener.findSpawnableGroundVertical(world, infiltrateTarget, 10, 3, 2000, entity.getType());
//
//            if (retreat.isPresent()) {
//                this.retreatBlock = retreat.get();
//                entity.getNavigation().moveTo(retreatBlock.getX() + 0.5d, retreatBlock.getY(), retreatBlock.getZ() + 0.5d, entity.getSpeed());
//                moveFinished = false;
//                return;
//            }
//        }
//        for (ChunkUtils.getNearbySpawners())
//        if (world.getChunkAt())
//            Optional<BlockPos> nearbyBlock = findNearbyInfiltrateBlock();
//        nearbyBlock.ifPresent();
//
//    }
//
//
//    private static boolean isRetreatableBoneDeposit(BlockState spawner) {
//        return spawner.getBlock() instanceof BoneDepositBlock && spawner.getValue(BoneDepositBlock.POTENCY) < 3;
//    }
//
//    private Optional<BlockPos> findNearbyInfiltrateBlock() {
//        Vector3d randomTarget = RandomPositionGenerator.getLandPos(entity, 15, 5);
//        if (randomTarget != null) {
//            int x = (int) randomTarget.x;
//            int y = (int) randomTarget.y;
//            int z = (int) randomTarget.z;
//            int width = 4;
//            int depth = 2;
//            for (BlockPos pos : BlockPos.randomBetweenClosed(entity.getRandom(), 50, x - width, y - depth, z - width, x + width, y + depth, z + width)) {
//                BlockState state = world.getBlockState(pos);
//                if (state.getBlock() == Blocks.DIRT || state.getBlock() == Blocks.SAND) {
//                    return Optional.of(pos);
//                }
//            }
//        }
//        return Optional.empty();
//    }
//
//}
