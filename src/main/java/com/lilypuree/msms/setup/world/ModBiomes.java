package com.lilypuree.msms.setup.world;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeGenerationSettings;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ModBiomes {

    public static void init() {
    }

    public static void addMSMSFeaturesToBiome(Biome biome) {
        if (biome.getBiomeCategory() != Biome.Category.NETHER && biome.getBiomeCategory() != Biome.Category.THEEND && biome.getBiomeCategory() != Biome.Category.NONE) {
            addFeaturesToBiome(biome, GenerationStage.Decoration.UNDERGROUND_ORES, ConfiguredFeatureList.EGG_SACKS);
            addFeaturesToBiome(biome, GenerationStage.Decoration.TOP_LAYER_MODIFICATION, ConfiguredFeatureList.SPIDER_NESTS);
            if (biome == WorldGenRegistries.BIOME.get(Biomes.DARK_FOREST) || biome == WorldGenRegistries.BIOME.get(Biomes.DARK_FOREST_HILLS)) {
//                addFeaturesToBiome(biome, GenerationStage.Decoration.VEGETAL_DECORATION, ConfiguredFeatureList.DARK_OAK_SPIDER_005);
            }
        }
    }

    public static void addFeaturesToBiome(Biome biome, GenerationStage.Decoration feature, ConfiguredFeature<?, ?> configuredFeature) {
        ConvertImmutableFeatures(biome);
        List<List<Supplier<ConfiguredFeature<?, ?>>>> biomeFeatures = biome.getGenerationSettings().features();
        while (biomeFeatures.size() <= feature.ordinal()) {
            biomeFeatures.add(Lists.newArrayList());
        }
        biomeFeatures.get(feature.ordinal()).add(() -> configuredFeature);
    }

    private static void ConvertImmutableFeatures(Biome biome) {
        if (biome.getGenerationSettings().features instanceof ImmutableList) {
            biome.getGenerationSettings().features = biome.getGenerationSettings().features.stream().map(Lists::newArrayList).collect(Collectors.toList());
        }
    }
}
