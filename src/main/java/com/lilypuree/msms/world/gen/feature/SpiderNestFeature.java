package com.lilypuree.msms.world.gen.feature;

import com.lilypuree.msms.entity.ai.SpiderNestFinder;
import com.mojang.serialization.Codec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.BlockStateFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.Random;

public class SpiderNestFeature extends Feature<BlockStateFeatureConfig> {

    public SpiderNestFeature(Codec<BlockStateFeatureConfig> config) {
        super(config);
    }

    @Override
    public boolean place(ISeedReader reader, ChunkGenerator chunk, Random rand, BlockPos pos, BlockStateFeatureConfig config) {
        if (SpiderNestFinder.nestCondition.test(reader, pos)) {
            reader.setBlock(pos, config.state, 2);
            return true;
        }
        return false;
    }
}
