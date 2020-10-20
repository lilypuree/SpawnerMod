package com.lilypuree.msms.setup.world;

import com.lilypuree.msms.MSMSMod;
import com.lilypuree.msms.world.gen.feature.ReplaceBlockStatesConfig;
import com.lilypuree.msms.world.gen.feature.ReplaceBlockStatesFeature;
import com.lilypuree.msms.world.gen.feature.SpiderNestFeature;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.*;

public class FeatureList {

    public static void init() {
    }

//    public static final Feature<ReplaceBlockStatesConfig> REPLACE_BLOCKSTATES = register("spider_tree", new ReplaceBlockStatesFeature(ReplaceBlockStatesConfig.CODEC));
    public static final Feature<BlockStateFeatureConfig> SPIDER_NEST = register("spider_nest", new SpiderNestFeature(BlockStateFeatureConfig.CODEC));

    public static  <C extends IFeatureConfig, F extends Feature<C>> F register(String id, F feature) {
        Registry.register(Registry.FEATURE, new ResourceLocation(MSMSMod.MODID, id), feature);
        return feature;
    }
}
