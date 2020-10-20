package com.lilypuree.msms.setup.world;

import com.google.common.collect.ImmutableList;
import com.lilypuree.msms.MSMSMod;
import com.lilypuree.msms.block.SpiderNestBlock;
import com.lilypuree.msms.setup.BlockList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placement.NoPlacementConfig;

public class ConfiguredFeatureList {

    public static void init() {
    }

    public static final ConfiguredFeature<?, ?> EGG_SACKS = registerConfiguredFeature("egg_sacks", Feature.ORE.configured(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, BlockList.EGG_SACKS.defaultBlockState(), MSMSMod.serverConfigs.getEggSackClusterSize())).range(128).squared().count(MSMSMod.serverConfigs.getEggSackCount()));
    public static final ConfiguredFeature<?, ?> SPIDER_NESTS = registerConfiguredFeature("spider_nest", FeatureList.SPIDER_NEST.configured(new BlockStateFeatureConfig(BlockList.SPIDER_NEST.defaultBlockState().setValue(SpiderNestBlock.INHABITED, true))).decorated(Decorators.SPIDER_NESTS.configured(NoPlacementConfig.INSTANCE)));

//    private static RuleTest isLeaves = new TagMatchRuleTest(BlockTags.LEAVES);
//    public static final ConfiguredFeature<?, ?> SPIDER_TREE = FeatureList.REPLACE_BLOCKSTATES.configured(new ReplaceBlockStatesConfig(isLeaves, BlockList.EGG_SACKS.defaultBlockState())).decorated(Placement.);


    public static final ConfiguredFeature<BaseTreeFeatureConfig, ?> DARK_OAK_SPIDER_005 = registerConfiguredFeature("dark_oak_spider_005", Feature.TREE.configured(Features.DARK_OAK.config.withDecorators(ImmutableList.of(Decorators.TreeDecoratorTypes.SPIDER_EGG_09))));
    public static final ConfiguredFeature<BaseTreeFeatureConfig, ?> FANCY_OAK_SPIDER_002 = registerConfiguredFeature("fancy_oak_spider_002", Feature.TREE.configured(Features.FANCY_OAK.config.withDecorators(ImmutableList.of(Decorators.TreeDecoratorTypes.SPIDER_EGG_002))));


    public static <FC extends IFeatureConfig, F extends Feature<FC>, CF extends ConfiguredFeature<FC, F>> CF registerConfiguredFeature(String id, CF configuredFeature) {
        Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(MSMSMod.MODID, id), configuredFeature);
        return configuredFeature;
    }
}
