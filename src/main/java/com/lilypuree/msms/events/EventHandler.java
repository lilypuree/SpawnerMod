package com.lilypuree.msms.events;

import com.lilypuree.msms.MSMSMod;
import com.lilypuree.msms.block.SpiderNestBlock;
import com.lilypuree.msms.capability.*;
import com.lilypuree.msms.entity.ai.SpiderNestFinder;
import com.lilypuree.msms.setup.BlockList;
import com.lilypuree.msms.setup.EntityList;
import com.lilypuree.msms.spawners.MobAwakener;
import com.lilypuree.msms.util.ChunkUtils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.monster.SpiderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Dimension;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunk;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventHandler {

    @SubscribeEvent
    public static void onLivingSpawn(LivingSpawnEvent.CheckSpawn event) {
        if (event.getSpawnReason() == SpawnReason.NATURAL) {
            LivingEntity entity = event.getEntityLiving();
            if (entity.getCommandSenderWorld().dimension().getRegistryName().equals(Dimension.OVERWORLD.getRegistryName())) {
                if (isEntityHandled(entity)) {
                    event.setResult(Event.Result.DENY);

                }
            }
        }
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        PlayerEntity player = event.getPlayer();
        World world = player.getCommandSenderWorld();
        int defaultNoise = 20;
        double distSqr = player.blockPosition().distSqr(event.getPos());
        int actualNoise = ((int) (defaultNoise * (distSqr > 25 ? (distSqr > 100 ? 0 : 1 - ((distSqr - 25) / 75)) : 1)));
        if (!world.isClientSide()) {
            player.getCapability(PlayerAggroHandlerProvider.AGGRO_HANDLER_CAP).ifPresent(handler -> {
                handler.addNoise(actualNoise);
            });
        }
    }

    @SubscribeEvent
    public static void onEntityDespawn(LivingSpawnEvent.AllowDespawn event) {
        Entity entity = event.getEntity();
        if (isEntityHandled(entity)) {
            Entity playerEntity = entity.getCommandSenderWorld().getNearestPlayer(entity, -1.0D);
            if (playerEntity != null) {
                MobEntity mob = ((MobEntity) entity);
                double distance = entity.distanceToSqr(playerEntity);
                int i = entity.getType().getCategory().getDespawnDistance();
                if (distance > i * i && mob.removeWhenFarAway(distance)) {
                    alternateDespawn(mob);
                }

                int k = entity.getType().getCategory().getNoDespawnDistance();

                if (mob.getNoActionTime() > 600 && mob.getRandom().nextInt(800) == 0 && distance > k * k && mob.removeWhenFarAway(distance)) {
                    alternateDespawn(mob);
                } else if (distance < k * k) {
                    mob.setNoActionTime(0);
                }
                event.setResult(Event.Result.DENY);
            }
        }
    }

    public static void alternateDespawn(MobEntity entity) {
        Optional<BlockPos> spawnPos = entity.getCapability(SpawnLocationProvider.SPAWN_LOCATION_CAP).map(ISpawnLocation::getSpawnPos);
        World world = entity.getCommandSenderWorld();
        if (entity instanceof SpiderEntity) {
            BlockState inhabitedNest = BlockList.SPIDER_NEST.defaultBlockState().setValue(SpiderNestBlock.INHABITED, true);

            if (spawnPos.isPresent()) {
                BlockState spawner = world.getBlockState(spawnPos.get());
                if (spawner.getBlock() == BlockList.SPIDER_NEST && !spawner.getValue(SpiderNestBlock.INHABITED)) {
                    world.setBlockAndUpdate(spawnPos.get(), inhabitedNest);
                    entity.remove();
                    return;
                }
            }

            BlockPos nestingPos = SpiderNestFinder.findPossibleNestPosition(entity, 3, 4).orElse(entity.blockPosition());
            if (world.getBlockState(nestingPos).getMaterial().isReplaceable()) {
                world.setBlockAndUpdate(nestingPos, inhabitedNest);
            }
        }
        entity.remove();
    }

    @SubscribeEvent
    public static void onChunkLoad(ChunkEvent.Load event) {
        IChunk iChunk = event.getChunk();
        if (iChunk instanceof Chunk && !event.getWorld().isClientSide()) {
            LazyOptional<IChunkSpawnPoints> spawnLocs = ((Chunk) iChunk).getCapability(ChunkSpawnPointProvider.CHUNK_SPAWN_POINTS);
            spawnLocs.ifPresent(spawnLocation -> {
                if (spawnLocation.getSpawnPoints().isEmpty()) {
                    ChunkUtils.scanChunkForSpawners((Chunk) iChunk, spawnLocation);
                }
            });
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {

        PlayerEntity player = event.player;
        if (!player.getCommandSenderWorld().isClientSide() && !player.isCreative()) {
            player.getCapability(PlayerAggroHandlerProvider.AGGRO_HANDLER_CAP).ifPresent(handler -> {
                if (player.isSprinting()) {
                    handler.addNoise(2);
                }
                MobAwakener.onPlayerTick(player);
                handler.tick();
            });
        }
    }


    //**********************************************| DEBUG |*******************************************************//

    private static final Logger LOGGER = LogManager.getLogger();

    @SubscribeEvent
    public static void onClick(PlayerInteractEvent.RightClickItem event) {
        if (!event.getWorld().isClientSide() && event.getItemStack().getItem() == Items.SHEARS) {
            LazyOptional<IChunkSpawnPoints> sp = event.getWorld().getChunkAt(event.getPos()).getCapability(ChunkSpawnPointProvider.CHUNK_SPAWN_POINTS);
            sp.ifPresent(spawnPoints -> {
                System.out.println(spawnPoints.getSpawnPoints());
                ;
            });
            event.getPlayer().getCapability(PlayerAggroHandlerProvider.AGGRO_HANDLER_CAP).ifPresent(cap -> {
                System.out.println(cap.getNoise());
            });
        }
    }

    public static boolean isEntityHandled(Entity entity) {
        if (MSMSMod.serverConfigs.isSpiderSpawnDisabled()) {
            if (entity.getType() == EntityType.SPIDER || entity.getType() == EntityList.BABY_SPIDER) {
                return true;
            }
        }
        return entity instanceof SkeletonEntity;
    }


}