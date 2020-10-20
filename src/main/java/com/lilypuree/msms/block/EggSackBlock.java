package com.lilypuree.msms.block;

import com.lilypuree.msms.MSMSMod;
import com.lilypuree.msms.entity.BabySpiderEntity;
import com.lilypuree.msms.setup.BlockList;
import com.lilypuree.msms.setup.EntityList;
import com.lilypuree.msms.util.ChunkUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class EggSackBlock extends Block implements ISpiderSpawners {

    public EggSackBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void onPlace(BlockState state, World world, BlockPos pos, BlockState newState, boolean p_220082_5_) {
        super.onPlace(state, world, pos, newState, p_220082_5_);
        if (!world.isClientSide()) {
            ChunkUtils.addSpawnPosToChunk(world.getChunkAt(pos), pos);
        }
    }

    @Override
    public void onRemove(BlockState state, World level, BlockPos pos, BlockState newState, boolean p_196243_5_) {
        super.onRemove(state, level, pos, newState, p_196243_5_);
        if (!level.isClientSide()) {
            ChunkUtils.removeSpawnPosFromChunk(level.getChunkAt(pos), pos);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
    }

    @Override
    public void destroy(IWorld world, BlockPos pos, BlockState state) {
        if (world.isClientSide()) {
            BlockPos.withinManhattanStream(pos, 1, 1, 1).forEach(pos1 -> {
                for (int i = 0; i < 5; ++i) {
                    double d0 = world.getRandom().nextGaussian() * 0.02D;
                    double d1 = world.getRandom().nextGaussian() * 0.02D;
                    double d2 = world.getRandom().nextGaussian() * 0.02D;
                    double d3 = 10.0D;
                    world.addParticle(ParticleTypes.POOF, pos1.getX() + 0.5 - d0 * 10.0D, pos1.getY() + 0.8 - d1 * 10.0D, pos1.getZ() + 0.5 - d2 * 10.0D, d0, d1, d2);
                }
            });
        }
    }

    @Override
    public void playerDestroy(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity te, ItemStack stack) {

        explode(world, pos);
    }

    public static void explode(World world, BlockPos pos) {

        BlockPos.withinManhattanStream(pos, 1, 1, 1).filter(otherPos -> world.getBlockState(otherPos).getMaterial().isReplaceable())
                .forEach(otherPos -> {
                    if (world.getRandom().nextFloat() < 0.5f) {
                        world.setBlockAndUpdate(otherPos, Blocks.COBWEB.defaultBlockState());
                    }
                });
        world.setBlockAndUpdate(pos, Blocks.COBWEB.defaultBlockState());
        for (Direction dir : Direction.values()) {
            BlockPos newPos = pos.relative(dir);
            if (world.getBlockState(newPos).getBlock() == BlockList.EGG_SACKS) {
                explode(world, newPos);
            }
        }

        spawnSpiders(world, pos);
    }

    public static void spawnSpiders(World world, BlockPos pos) {
        Random random = world.getRandom();
        int min = MSMSMod.serverConfigs.getMinSpidersFromEggSack();
        int max = MSMSMod.serverConfigs.getMaxSpidersFromEggSack() - min;
        for (int i = 0; i < min + random.nextInt(max); i++) {
            BabySpiderEntity baby = EntityList.BABY_SPIDER.create(world);
            baby.moveTo(pos, world.random.nextFloat() * 360.0F, 0.0F);
            double velocity = 0.2 + random.nextFloat() * 0.3;
            double phi = random.nextFloat() * Math.PI / 2;
            double theta = random.nextFloat() * 2 * Math.PI;
            baby.setDeltaMovement(velocity * Math.sin(phi) * Math.cos(theta), velocity * Math.cos(phi), velocity * Math.sin(phi) * Math.sin(theta));
            world.addFreshEntity(baby);
        }
    }

    @Override
    public void afterSpawn(World world, BlockState state, BlockPos pos) {
        if (world.getRandom().nextFloat() < 0.3f) {
            return;
        } else {
            world.setBlockAndUpdate(pos, Blocks.COBWEB.defaultBlockState());
        }
    }
}
