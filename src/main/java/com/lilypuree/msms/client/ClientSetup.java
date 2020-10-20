package com.lilypuree.msms.client;

import com.lilypuree.msms.MSMSMod;
import com.lilypuree.msms.setup.BlockList;
import com.lilypuree.msms.setup.EntityList;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.entity.WolfRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = MSMSMod.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    public static void init(final FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(EntityList.BABY_SPIDER, BabySpiderRenderer::new);

//        RenderTypeLookup.setRenderLayer(BlockList.BONE_DEPOSIT_DIRT, RenderType.cutout());
//        RenderTypeLookup.setRenderLayer(BlockList.BONE_DEPOSIT_SAND, RenderType.cutout());
//        RenderTypeLookup.setRenderLayer(BlockList.BONE_DEPOSIT_STONE, RenderType.cutout());
        RenderTypeLookup.setRenderLayer(BlockList.SPIDER_NEST, RenderType.cutout());
    }

}
