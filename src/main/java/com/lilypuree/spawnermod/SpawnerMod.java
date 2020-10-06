package com.lilypuree.spawnermod;

import com.lilypuree.spawnermod.setup.ModSetup;
import com.lilypuree.spawnermod.setup.BlockList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(SpawnerMod.MODID)
public class SpawnerMod
{
    // Directly reference a log4j logger.

    public static final String MODID = "spawnermod";

    private static final Logger LOGGER = LogManager.getLogger();

    public SpawnerMod() {

        FMLJavaModLoadingContext.get().getModEventBus().addListener(ModSetup::init);

        BlockList.register();

    }

}
