package com.lilypuree.spawnermod.events;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventHandler {

    @SubscribeEvent
    public static void onLivingSpawn(LivingSpawnEvent.CheckSpawn event) {

        if (event.getSpawnReason() == SpawnReason.NATURAL){
            LivingEntity entity = event.getEntityLiving();
            if (entity.getEntityWorld().getDimensionKey().getRegistryName().equals(DimensionType.OVERWORLD_ID)){
                if (isEntityHandled(entity)){
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event){
       PlayerEntity player = event.getPlayer();
       World world = player.getEntityWorld();
       if (!world.isRemote()){
          BlockPos pos = event.getPos();
       }
    }



    public static boolean isEntityHandled(Entity entity){
        return entity instanceof SkeletonEntity;
    }
}
