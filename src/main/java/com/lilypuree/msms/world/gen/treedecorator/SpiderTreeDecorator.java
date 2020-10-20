package com.lilypuree.msms.world.gen.treedecorator;

import com.lilypuree.msms.setup.BlockList;
import com.lilypuree.msms.setup.world.Decorators;
import com.mojang.serialization.Codec;
import net.minecraft.block.BeehiveBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.tileentity.BeehiveTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class SpiderTreeDecorator extends TreeDecorator {
    public static final Codec<SpiderTreeDecorator> CODEC = Codec.floatRange(0.0F, 1.0F).fieldOf("probability").xmap(SpiderTreeDecorator::new, (o) -> {
        return o.probability;
    }).codec();
    private final float probability;

    public SpiderTreeDecorator(float probability) {
        this.probability = probability;
    }

    @Override
    protected TreeDecoratorType<?> type() {
        return Decorators.TreeDecoratorTypes.SPIDER_EGGS;
    }

    @Override
    public void place(ISeedReader world, Random rand, List<BlockPos> poses, List<BlockPos> p_225576_4_, Set<BlockPos> p_225576_5_, MutableBoundingBox mBB) {
        if (!(rand.nextFloat() >= this.probability)) {
            Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(rand);
            int i = !p_225576_4_.isEmpty() ? Math.max(p_225576_4_.get(0).getY() - 1, poses.get(0).getY()) : Math.min(poses.get(0).getY() + 1 + rand.nextInt(3), poses.get(poses.size() - 1).getY());
            List<BlockPos> list = poses.stream().filter((pos) -> {
                return pos.getY() == i;
            }).collect(Collectors.toList());
            if (!list.isEmpty()) {
                BlockPos blockpos = list.get(rand.nextInt(list.size()));
                BlockPos blockpos1 = blockpos.relative(direction, 1 + rand.nextInt(1));
                if (Feature.isAir(world, blockpos1) && !Feature.isAir(world, blockpos1.above())) {
                    BlockState blockState = BlockList.SPIDER_NEST.defaultBlockState();
                    this.setBlock(world, blockpos1, blockState, p_225576_5_, mBB);
                }
            }

        }
    }
}
