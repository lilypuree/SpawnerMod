package com.lilypuree.msms.entity.ai;

import jdk.nashorn.internal.ir.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.swing.border.AbstractBorder;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class SpiderNestFinder {


    public static BiPredicate<IWorld, BlockPos> nestCondition = (world, pos) -> {
        BlockPos up = pos.above();
        BlockPos down = pos.below();
        boolean upFull = isBlockSolidToSide(world, pos, Direction.UP);
        boolean downFull = isBlockSolidToSide(world, pos, Direction.DOWN);
        if (upFull) {
            int solidSides = 0;
            for (Direction direction : Direction.Plane.HORIZONTAL) {
                if (isBlockSolidToSide(world, pos, direction)) {
                    solidSides++;
                }
                if (upFull && solidSides > 0) {
                    return true;
                }
            }
        }
        return false;
    };

    public static Optional<BlockPos> findPossibleNestPosition(MobEntity entity, int maxDistance, int maxVertical) {
        BlockPos pos = entity.blockPosition();
        return findPossibleNestPosition(entity.getCommandSenderWorld(), pos.getX(), pos.getY(), pos.getZ(), maxDistance, maxVertical, nestCondition);
    }

    public static Optional<BlockPos> findPossibleNestPosition(World world, int x, int y, int z, int maxDistance, int maxVertical, BiPredicate<IWorld, BlockPos> nestPosValidator) {

        for (BlockPos pos : BlockPos.randomBetweenClosed(world.getRandom(), 20, x - maxDistance, y, z - maxDistance, x + maxDistance, y + maxVertical, z + maxDistance)) {
            if (world.getBlockState(pos).isAir(world, pos)) {
                if (nestPosValidator.test(world, pos)) {
                    return Optional.of(pos);
                }

            }
        }
        return Optional.empty();
    }

    public static boolean isBlockSolidToSide(IWorld world, BlockPos center, Direction direction) {
        BlockPos newPos = center.relative(direction);
        return world.getBlockState(newPos).isFaceSturdy(world, newPos, direction.getOpposite());
    }
}
