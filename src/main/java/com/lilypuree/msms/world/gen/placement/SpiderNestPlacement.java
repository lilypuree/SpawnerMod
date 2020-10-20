package com.lilypuree.msms.world.gen.placement;

import com.lilypuree.msms.MSMSMod;
import com.mojang.serialization.Codec;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.WorldDecoratingHelper;
import net.minecraft.world.gen.placement.NoPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.SimplePlacement;

import java.util.Random;
import java.util.function.BiPredicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.lilypuree.msms.entity.ai.SpiderNestFinder.isBlockSolidToSide;

public class SpiderNestPlacement extends Placement<NoPlacementConfig> {

    public SpiderNestPlacement(Codec<NoPlacementConfig> config) {
        super(config);
    }


    @Override
    public Stream<BlockPos> getPositions(WorldDecoratingHelper world, Random rand, NoPlacementConfig config, BlockPos pos) {

        int x = pos.getX();
        int z = pos.getZ();
        int surface = world.getHeight(Heightmap.Type.MOTION_BLOCKING, x, z);
        int i = MSMSMod.serverConfigs.getSpiderNestsSpawnAttempts() / 2 + rand.nextInt(MSMSMod.serverConfigs.getSpiderNestsSpawnAttempts() / 2);
        return IntStream.range(0, i).mapToObj((num) -> {
            int j = rand.nextInt(16) + pos.getX();
            int k = rand.nextInt(16) + pos.getZ();
            int l = rand.nextInt(surface) + 10;
            return new BlockPos(j, l, k);
        });
    }
}
