package net.minecraft.world.spawner;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.util.math.BlockPos;

public class MobDensityTracker {
    private final List<MobDensityTracker.DensityEntry> trackerEntries = Lists.newArrayList();

    public void addTracker(BlockPos pos, double spawnCost) {
        if (spawnCost != 0.0D) {
            this.trackerEntries.add(new MobDensityTracker.DensityEntry(pos, spawnCost));
        }

    }

    public double func_234999_b_(BlockPos otherPos, double spawnCost) {
        if (spawnCost == 0.0D) {
            return 0.0D;
        } else {
            double d0 = 0.0D;

            for(MobDensityTracker.DensityEntry tracker : this.trackerEntries) {
                d0 += tracker.func_235002_a_(otherPos);
            }

            return d0 * spawnCost;
        }
    }

    static class DensityEntry {
        private final BlockPos pos;
        private final double spawnCost;

        public DensityEntry(BlockPos pos, double spawnCost) {
            this.pos = pos;
            this.spawnCost = spawnCost;
        }

        public double func_235002_a_(BlockPos otherPos) {
            double distance = this.pos.distanceSq(otherPos);
            return distance == 0.0D ? Double.POSITIVE_INFINITY : this.spawnCost / Math.sqrt(distance);
        }
    }
}