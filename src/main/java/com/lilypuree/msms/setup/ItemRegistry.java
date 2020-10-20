package com.lilypuree.msms.setup;

import com.lilypuree.msms.MSMSMod;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = MSMSMod.MODID)
public class ItemRegistry {

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
//                new BlockItem(BlockList.BONE_DEPOSIT_DIRT, new Item.Properties().tab(ItemGroup.TAB_MISC)).setRegistryName("bone_deposit_dirt"),
//                new BlockItem(BlockList.BONE_DEPOSIT_SAND, new Item.Properties().tab(ItemGroup.TAB_MISC)).setRegistryName("bone_deposit_sand"),
//                new BlockItem(BlockList.BONE_DEPOSIT_STONE, new Item.Properties().tab(ItemGroup.TAB_MISC)).setRegistryName("bone_deposit_stone"),
                new BlockItem(BlockList.SPIDER_NEST, new Item.Properties().tab(ItemGroup.TAB_MISC)).setRegistryName("spider_nest"),
                new BlockItem(BlockList.EGG_SACKS, new Item.Properties().tab(ItemGroup.TAB_MISC)).setRegistryName("egg_sack"),
                new SpawnEggItem(EntityList.babyspiderinit.get(), 0x665d52, 0x3c0202, new Item.Properties().tab(ItemGroup.TAB_MISC)).setRegistryName("baby_spider_spawn_egg")
        );
    }
}