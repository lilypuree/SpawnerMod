////
//// Source code recreated from a .class file by IntelliJ IDEA
//// (powered by Fernflower decompiler)
////
//
//package net.minecraft.world.spawner;
//
//import com.google.common.collect.Lists;
//import java.util.Iterator;
//import java.util.List;
//import net.minecraft.util.math.BlockPos;
//
//public class MobDensityTracker {
//    private final List<MobDensityTracker.DensityEntry> charges = Lists.newArrayList();
//
//    public MobDensityTracker() {
//    }
//
//    public void addCharge(BlockPos p_234998_1_, double p_234998_2_) {
//        if (p_234998_2_ != 0.0D) {
//            this.charges.add(new MobDensityTracker.DensityEntry(p_234998_1_, p_234998_2_));
//        }
//
//    }
//
//    public double getPotentialEnergyChange(BlockPos p_234999_1_, double p_234999_2_) {
//        if (p_234999_2_ == 0.0D) {
//            return 0.0D;
//        } else {
//            double lvt_4_1_ = 0.0D;
//
//            MobDensityTracker.DensityEntry lvt_7_1_;
//            for(Iterator var6 = this.charges.iterator(); var6.hasNext(); lvt_4_1_ += lvt_7_1_.getPotentialChange(p_234999_1_)) {
//                lvt_7_1_ = (MobDensityTracker.DensityEntry)var6.next();
//            }
//
//            return lvt_4_1_ * p_234999_2_;
//        }
//    }
//
//    static class DensityEntry {
//        private final BlockPos pos;
//        private final double charge;
//
//        public DensityEntry(BlockPos p_i231624_1_, double p_i231624_2_) {
//            this.pos = p_i231624_1_;
//            this.charge = p_i231624_2_;
//        }
//
//        public double getPotentialChange(BlockPos p_235002_1_) {
//            double lvt_2_1_ = this.pos.distSqr(p_235002_1_);
//            return lvt_2_1_ == 0.0D ? 1.0D / 0.0 : this.charge / Math.sqrt(lvt_2_1_);
//        }
//    }
//}
