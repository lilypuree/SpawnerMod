package com.lilypuree.spawnermod.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BoneDepositBlock extends FallingBlock {
    public static final IntegerProperty POTENCY = BlockStateProperties.LEVEL_0_3;
    private Block parentBlock;
    private int sensitivity

    public BoneDepositBlock(Block parentBlock, int sensitivity, Properties properties) {
        super(properties);
        this.parentBlock = parentBlock;
        this.sensitivity = sensitivity;
        this.setDefaultState(this.getStateContainer().getBaseState().with(POTENCY, 3));
    }

    @Override
    public void onPlayerDestroy(IWorld worldIn, BlockPos pos, BlockState state) {
        super.onPlayerDestroy(worldIn, pos, state);
    }

    @Override
    public void onExplosionDestroy(World worldIn, BlockPos pos, Explosion explosionIn) {
        super.onExplosionDestroy(worldIn, pos, explosionIn);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(POTENCY);
        super.fillStateContainer(builder);
    }

    public int getSensitivity() {
        return sensitivity;
    }
}
