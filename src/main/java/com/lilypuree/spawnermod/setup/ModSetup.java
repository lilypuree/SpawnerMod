package com.lilypuree.spawnermod.setup;

import com.lilypuree.spawnermod.SpawnerMod;
import com.lilypuree.spawnermod.capability.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;


@Mod.EventBusSubscriber(modid = SpawnerMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModSetup {

    public static void init(FMLCommonSetupEvent event){
        CapabilityManager.INSTANCE.register(ISpawnLocation.class, new SpawnLocationProvider.SpawnLocationStorage(), SpawnLocation::new);
        CapabilityManager.INSTANCE.register(IChunkSpawnPoints.class, new ChunkSpawnPointProvider.ChunkSpawnPointStorage(), ChunkSpawnPoints::new);
        CapabilityManager.INSTANCE.register(IPlayerAggroTimer.class, new PlayerAggroTimerProvider.PlayerAggroTimerStorage(), PlayerAggroTimer::new);
    }

    @SubscribeEvent
    public static void onAttachEntityCapabilities(AttachCapabilitiesEvent<Entity> event){
        ResourceLocation spawnLoc = new ResourceLocation(SpawnerMod.MODID, "spawn_loc");
        ResourceLocation aggroTimer = new ResourceLocation(SpawnerMod.MODID, "aggro_timer");
        if (event.getObject() instanceof SkeletonEntity){
            event.addCapability(spawnLoc, new SpawnLocationProvider());
        }else if (event.getObject() instanceof PlayerEntity){
            event.addCapability(aggroTimer, new PlayerAggroTimerProvider());
        }
    }

    @SubscribeEvent
    public static void onAttachChunkCapabilities(AttachCapabilitiesEvent<Chunk> event){
        ResourceLocation spawnLoc = new ResourceLocation(SpawnerMod.MODID, "spawn_points");
        event.addCapability(spawnLoc, new ChunkSpawnPointProvider());
    }
}
