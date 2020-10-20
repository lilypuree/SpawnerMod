package com.lilypuree.msms.setup;

import com.lilypuree.msms.MSMSMod;
import com.lilypuree.msms.entity.BabySpiderEntity;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.util.NonNullLazy;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = MSMSMod.MODID)
public class EntityList {
    static final NonNullLazy<EntityType<BabySpiderEntity>> babyspiderinit = NonNullLazy.of(() -> EntityType.Builder.<BabySpiderEntity>of(BabySpiderEntity::new, EntityClassification.MONSTER).sized(0.5f, 0.8f).clientTrackingRange(8).build("baby_spider"));

    public static EntityType<BabySpiderEntity> BABY_SPIDER;

    private static <T extends Entity> EntityType<T> register(String registryName, EntityType<T> type) {
        return Registry.register(Registry.ENTITY_TYPE, new ResourceLocation(MSMSMod.MODID, registryName), type);
    }

    @SubscribeEvent
    public static void registerEntities(final RegistryEvent.Register<EntityType<?>> event) {
        BABY_SPIDER = register("baby_spider", babyspiderinit.get());

    }
}
