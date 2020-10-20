package com.lilypuree.msms.entity.ai;

import com.lilypuree.msms.block.SpiderNestBlock;
import com.lilypuree.msms.setup.BlockList;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.Random;

public class MakeNestGoal extends Goal {

    private CreatureEntity entity;
    private BlockPos targetPos;
    private final double speedModifier;
    private final World level;

    public MakeNestGoal(CreatureEntity entity, double speedModifier) {
        this.entity = entity;
        this.level = entity.level;
        this.speedModifier = speedModifier;
    }


    @Override
    public boolean canUse() {
        if (entity.tickCount > 2000) {
            return this.setWantedPos();
        } else return false;
    }

    protected boolean setWantedPos() {
        BlockPos target = this.getNestPos();
        if (target == null) {
            return false;
        } else {
            this.targetPos = target;
            return true;
        }
    }

    protected BlockPos getNestPos() {
        Random random = this.entity.getRandom();
        BlockPos blockpos = this.entity.blockPosition();

        for (int i = 0; i < 10; ++i) {
            BlockPos nextPos = blockpos.offset(random.nextInt(20) - 10, random.nextInt(6) - 3, random.nextInt(20) - 10);
            if (SpiderNestFinder.nestCondition.test(level, nextPos) && this.entity.getWalkTargetValue(nextPos) < 0.0F) {
                return nextPos;
            }
        }

        return null;
    }

    @Override
    public void start() {
        Vector3d vector3d = Vector3d.atBottomCenterOf(targetPos);
        this.entity.getNavigation().moveTo(vector3d.x, vector3d.y, vector3d.z, this.speedModifier);
    }

    @Override
    public void stop() {
        if (this.targetPos != null && entity.distanceToSqr(Vector3d.atBottomCenterOf(targetPos)) < 3) {
            if (level.getBlockState(targetPos).getMaterial().isReplaceable()) {
                level.setBlockAndUpdate(targetPos, BlockList.SPIDER_NEST.defaultBlockState().setValue(SpiderNestBlock.INHABITED, true));
                entity.remove();
            }
        }
    }

    @Override
    public boolean canContinueToUse() {
        return this.entity.getNavigation().isDone();
    }


}
