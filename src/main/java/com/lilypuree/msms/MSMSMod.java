package com.lilypuree.msms;

import com.lilypuree.msms.client.ClientSetup;
import com.lilypuree.msms.setup.BlockList;
import com.lilypuree.msms.setup.ModSetup;
import com.lilypuree.msms.setup.world.Decorators;
import com.lilypuree.msms.setup.world.FeatureList;
import com.lilypuree.msms.setup.world.ModBiomes;
import commoble.databuddy.config.ConfigHelper;
import net.minecraft.block.Block;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static net.minecraftforge.versions.forge.ForgeVersion.MOD_ID;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MSMSMod.MODID)
public class MSMSMod {
    // Directly reference a log4j logger.

    public static final String MODID = "msms";

    public static ServerConfigs serverConfigs;

    private static final Logger LOGGER = LogManager.getLogger();

    public MSMSMod() {

        serverConfigs = ConfigHelper.register(ModLoadingContext.get(), FMLJavaModLoadingContext.get(),
                ModConfig.Type.SERVER, ServerConfigs::new);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ModSetup::init);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSetup::init);

    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class WorldGenRegistries {
        @SubscribeEvent
        public static void registerBiomes(RegistryEvent.Register<Biome> event) {
            ModBiomes.init();
        }

        @SubscribeEvent
        public static void registerDecorators(RegistryEvent.Register<Placement<?>> event) {
            Decorators.init();
        }

        @SubscribeEvent
        public static void registerFeatures(RegistryEvent.Register<Feature<?>> event) {
            FeatureList.init();
        }
    }
}