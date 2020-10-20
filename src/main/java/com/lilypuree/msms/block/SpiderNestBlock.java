package com.lilypuree.msms.block;

import com.lilypuree.msms.util.ChunkUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.WebBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.monster.SpiderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class SpiderNestBlock extends WebBlock implements ISpiderSpawners {

    public static final BooleanProperty INHABITED = BlockStateProperties.OCCUPIED;

    public SpiderNestBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(INHABITED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(INHABITED);
    }

    @Override
    public void entityInside(BlockState state, World world, BlockPos pos, Entity entity) {
        if (!(entity instanceof SpiderEntity)) {
            super.entityInside(state, world, pos, entity);

        }
    }

    @Override
    public void onPlace(BlockState state, World world, BlockPos pos, BlockState oldState, boolean p_220082_5_) {
        super.onPlace(state, world, pos, oldState, p_220082_5_);
        if (!world.isClientSide() && state.getValue(INHABITED)) {
            ChunkUtils.addSpawnPosToChunk(world.getChunkAt(pos), pos);
        }
    }

    @Override
    public void onRemove(BlockState state, World level, BlockPos pos, BlockState newState, boolean p_196243_5_) {
        super.onRemove(state, level, pos, newState, p_196243_5_);
        if (!level.isClientSide() && state.getValue(INHABITED)) {
            ChunkUtils.removeSpawnPosFromChunk(level.getChunkAt(pos), pos);
        }
    }


    @Override
    public void playerDestroy(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity blockEntity, ItemStack stack) {
        super.playerDestroy(world, player, pos, state, blockEntity, stack);
        if (!world.isClientSide() && state.getValue(INHABITED)) {
            addCobwebsAroundPlayer(world, player);
            MobEntity spider = EntityType.SPIDER.create(world);
            spider.moveTo(pos, world.random.nextFloat() * 360.0F, 0.0F);
            spider.finalizeSpawn((IServerWorld) world, world.getCurrentDifficultyAt(pos), SpawnReason.MOB_SUMMONED, null, null);
            spider.setTarget(player);
            world.addFreshEntity(spider);
            player.addEffect(new EffectInstance(Effects.POISON, 20, 1));
            player.addEffect(new EffectInstance(Effects.WEAKNESS, 40, 1));
        }
    }

    private void addCobwebsAroundPlayer(World world, PlayerEntity player) {
        BlockPos playerPos = player.blockPosition();
        BlockPos.Mutable pos = playerPos.above().mutable();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0 || world.random.nextFloat() < 0.3f) continue;
                pos.set(playerPos.getX() + i, playerPos.getY(), playerPos.getZ() + j);
                if (world.getBlockState(pos).getMaterial().isReplaceable()) {
                    world.setBlockAndUpdate(pos, Blocks.COBWEB.defaultBlockState());
                }
            }
        }
    }

    @Override
    public void afterSpawn(World world, BlockState state, BlockPos pos) {
        world.setBlockAndUpdate(pos, state.setValue(INHABITED, false));
    }
}
