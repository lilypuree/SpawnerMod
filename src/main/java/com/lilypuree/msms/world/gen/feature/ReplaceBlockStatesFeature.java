package com.lilypuree.msms.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;

public class ReplaceBlockStatesFeature extends Feature<ReplaceBlockStatesConfig> {
    public ReplaceBlockStatesFeature(Codec<ReplaceBlockStatesConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean place(ISeedReader reader, ChunkGenerator gen, Random rand, BlockPos pos, ReplaceBlockStatesConfig config) {
        if (config.ruleTest.test(reader.getBlockState(pos), rand)) {
            reader.setBlock(pos, config.state, 2);
        }
        return true;
    }
}
