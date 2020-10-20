package com.lilypuree.msms.block;

import com.lilypuree.msms.capability.ChunkSpawnPointProvider;
import com.lilypuree.msms.util.ChunkUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BoneDepositBlock extends FallingBlock {
    public static final IntegerProperty POTENCY = ModBlockProperties.POTENCY;
    private Block parentBlock;
    private int sensitivity;

    public BoneDepositBlock(Block parentBlock, int sensitivity, Properties properties) {
        super(properties);
        this.parentBlock = parentBlock;
        this.sensitivity = sensitivity;
        this.registerDefaultState(this.getStateDefinition().any().setValue(POTENCY, 3));
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
        builder.add(POTENCY);
        super.createBlockStateDefinition(builder);
    }

    public void decreasePotency(World world, BlockState state, BlockPos pos) {
        int potency = state.getValue(POTENCY);
        if (potency == 0) {
            world.setBlockAndUpdate(pos, parentBlock.defaultBlockState());
            world.getChunkAt(pos).getCapability(ChunkSpawnPointProvider.CHUNK_SPAWN_POINTS).ifPresent(spawns -> spawns.removeSpawnPoint(pos));
        } else {
            world.setBlockAndUpdate(pos, state.setValue(POTENCY, potency - 1));
        }
    }


    public int getSensitivity() {
        return sensitivity;
    }
}