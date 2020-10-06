package com.lilypuree.spawnermod.setup;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ItemRegistry {

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
                new BlockItem(BlockList.BONE_DEPOSIT_DIRT, new Item.Properties().group(ItemGroup.MISC)),
                new BlockItem(BlockList.BONE_DEPOSIT_SAND, new Item.Properties().group(ItemGroup.MISC)),
                new BlockItem(BlockList.BONE_DEPOSIT_STONE, new Item.Properties().group(ItemGroup.MISC))
        );
    }
}
