package com.lilypuree.msms.setup;

import com.lilypuree.msms.MSMSMod;
import com.lilypuree.msms.block.BoneDepositBlock;
import com.lilypuree.msms.block.EggSackBlock;
import com.lilypuree.msms.block.SpiderNestBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import net.minecraft.block.AbstractBlock;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = MSMSMod.MODID)
public class BlockRegistry {

    @SubscribeEvent
    public static void registerBlocks(final RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(
//                BlockList.BONE_DEPOSIT_DIRT = new BoneDepositBlock(Blocks.DIRT, 10, AbstractBlock.Properties.copy(Blocks.DIRT)).setRegistryName("bone_deposit_dirt"),
//                BlockList.BONE_DEPOSIT_SAND = new BoneDepositBlock(Blocks.SAND, 10, AbstractBlock.Properties.copy(Blocks.SAND)).setRegistryName("bone_deposit_sand"),
//                BlockList.BONE_DEPOSIT_STONE = new BoneDepositBlock(Blocks.STONE, 30, AbstractBlock.Properties.copy(Blocks.STONE)).setRegistryName("bone_deposit_stone"),
                BlockList.SPIDER_NEST = new SpiderNestBlock(AbstractBlock.Properties.of(Material.WEB).noCollission().strength(1.0f)).setRegistryName("spider_nest"),
                BlockList.EGG_SACKS = new EggSackBlock(AbstractBlock.Properties.of(Material.WEB).strength(1.0f)).setRegistryName("egg_sack")
        );
    }
}