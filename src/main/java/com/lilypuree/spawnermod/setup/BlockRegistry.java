package com.lilypuree.spawnermod.setup;

import com.lilypuree.spawnermod.block.BoneDepositBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class BlockRegistry {

    @SubscribeEvent
    public static void registerBlocks(final RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(
                BlockList.BONE_DEPOSIT_DIRT = new BoneDepositBlock(Block.Properties.from(Blocks.DIRT)),
                BlockList.BONE_DEPOSIT_SAND = new BoneDepositBlock(Block.Properties.from(Blocks.SAND)),
                BlockList.BONE_DEPOSIT_STONE = new BoneDepositBlock(Block.Properties.from(Blocks.STONE))
        );

    }
}
