package com.lilypuree.msms.world.gen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.ReplaceBlockConfig;
import net.minecraft.world.gen.feature.template.RuleTest;

public class ReplaceBlockStatesConfig implements IFeatureConfig {

    public static final Codec<ReplaceBlockStatesConfig> CODEC = RecordCodecBuilder.create((builder) -> {
        return builder.group(RuleTest.CODEC.fieldOf("target").forGetter((o) -> {
            return o.ruleTest;
        }), BlockState.CODEC.fieldOf("state").forGetter((o) -> {
            return o.state;
        })).apply(builder, ReplaceBlockStatesConfig::new);
    });
    public final RuleTest ruleTest;
    public final BlockState state;

    public ReplaceBlockStatesConfig(RuleTest ruleTest, BlockState state) {
        this.ruleTest = ruleTest;
        this.state = state;
    }
}
