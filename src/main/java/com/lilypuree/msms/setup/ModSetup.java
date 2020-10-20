package com.lilypuree.msms.setup;

import com.lilypuree.msms.MSMSMod;
import com.lilypuree.msms.capability.*;
import com.lilypuree.msms.entity.BabySpiderEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;


@Mod.EventBusSubscriber(modid = MSMSMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModSetup {

    public static void init(FMLCommonSetupEvent event) {
        CapabilityManager.INSTANCE.register(ISpawnLocation.class, new SpawnLocationProvider.SpawnLocationStorage(), SpawnLocation::new);
        CapabilityManager.INSTANCE.register(IChunkSpawnPoints.class, new ChunkSpawnPointProvider.ChunkSpawnPointStorage(), ChunkSpawnPoints::new);
        CapabilityManager.INSTANCE.register(IPlayerAggroHandler.class, new PlayerAggroHandlerProvider.PlayerAggroTimerStorage(), PlayerAggroHandler::new);
        event.enqueueWork(() -> {
            GlobalEntityTypeAttributes.put(EntityList.BABY_SPIDER, BabySpiderEntity.createAttributes().build());
        });
    }

    @SubscribeEvent
    public static void onAttachEntityCapabilities(AttachCapabilitiesEvent<Entity> event) {
        ResourceLocation spawnLoc = new ResourceLocation(MSMSMod.MODID, "spawn_loc");
        ResourceLocation aggroTimer = new ResourceLocation(MSMSMod.MODID, "aggro_timer");
        if (event.getObject() instanceof SkeletonEntity) {
            event.addCapability(spawnLoc, new SpawnLocationProvider());
        }
        EntityType type = event.getObject().getType();
        if (type == EntityType.SPIDER) {
            event.addCapability(spawnLoc, new SpawnLocationProvider());
        } else if (event.getObject() instanceof PlayerEntity) {
            event.addCapability(aggroTimer, new PlayerAggroHandlerProvider());
        }
    }

    @SubscribeEvent
    public static void onAttachChunkCapabilities(AttachCapabilitiesEvent<Chunk> event) {
        ResourceLocation spawnLoc = new ResourceLocation(MSMSMod.MODID, "spawn_points");
        event.addCapability(spawnLoc, new ChunkSpawnPointProvider());
    }


}