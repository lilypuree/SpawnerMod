//package net.minecraft.entity.ai;
//
//import java.util.Random;
//import java.util.function.Predicate;
//import java.util.function.ToDoubleFunction;
//import javax.annotation.Nullable;
//import net.minecraft.entity.CreatureEntity;
//import net.minecraft.pathfinding.PathNavigator;
//import net.minecraft.pathfinding.PathNodeType;
//import net.minecraft.pathfinding.WalkNodeProcessor;
//import net.minecraft.tags.FluidTags;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.math.MathHelper;
//import net.minecraft.util.math.vector.Vector3d;
//
//public class RandomPositionGenerator {
//    @Nullable
//    public static Vector3d getPos(CreatureEntity entity, int maxDistance, int yVariance) {
//        return generateRandomPos(entity, maxDistance, yVariance, 0, (Vector3d)null, true, (double)((float)Math.PI / 2F), entity::getWalkTargetValue, false, 0, 0, true);
//    }
//
//    @Nullable
//    public static Vector3d getAirPos(CreatureEntity p_226338_0_, int p_226338_1_, int p_226338_2_, int p_226338_3_, @Nullable Vector3d p_226338_4_, double p_226338_5_) {
//        return generateRandomPos(p_226338_0_, p_226338_1_, p_226338_2_, p_226338_3_, p_226338_4_, true, p_226338_5_, p_226338_0_::getWalkTargetValue, true, 0, 0, false);
//    }
//
//    @Nullable
//    public static Vector3d getLandPos(CreatureEntity p_191377_0_, int p_191377_1_, int p_191377_2_) {
//        return getLandPos(p_191377_0_, p_191377_1_, p_191377_2_, p_191377_0_::getWalkTargetValue);
//    }
//
//    @Nullable
//    public static Vector3d getLandPos(CreatureEntity p_221024_0_, int p_221024_1_, int p_221024_2_, ToDoubleFunction<BlockPos> p_221024_3_) {
//        return generateRandomPos(p_221024_0_, p_221024_1_, p_221024_2_, 0, (Vector3d)null, false, 0.0D, p_221024_3_, true, 0, 0, true);
//    }
//
//    @Nullable
//    public static Vector3d getAboveLandPos(CreatureEntity p_226340_0_, int p_226340_1_, int p_226340_2_, Vector3d p_226340_3_, float p_226340_4_, int p_226340_5_, int p_226340_6_) {
//        return generateRandomPos(p_226340_0_, p_226340_1_, p_226340_2_, 0, p_226340_3_, false, (double)p_226340_4_, p_226340_0_::getWalkTargetValue, true, p_226340_5_, p_226340_6_, true);
//    }
//
//    @Nullable
//    public static Vector3d getLandPosTowards(CreatureEntity p_234133_0_, int p_234133_1_, int p_234133_2_, Vector3d p_234133_3_) {
//        Vector3d vector3d = p_234133_3_.subtract(p_234133_0_.getX(), p_234133_0_.getY(), p_234133_0_.getZ());
//        return generateRandomPos(p_234133_0_, p_234133_1_, p_234133_2_, 0, vector3d, false, (double)((float)Math.PI / 2F), p_234133_0_::getWalkTargetValue, true, 0, 0, true);
//    }
//
//    @Nullable
//    public static Vector3d getPosTowards(CreatureEntity p_75464_0_, int p_75464_1_, int p_75464_2_, Vector3d p_75464_3_) {
//        Vector3d vector3d = p_75464_3_.subtract(p_75464_0_.getX(), p_75464_0_.getY(), p_75464_0_.getZ());
//        return generateRandomPos(p_75464_0_, p_75464_1_, p_75464_2_, 0, vector3d, true, (double)((float)Math.PI / 2F), p_75464_0_::getWalkTargetValue, false, 0, 0, true);
//    }
//
//    @Nullable
//    public static Vector3d getPosTowards(CreatureEntity p_203155_0_, int p_203155_1_, int p_203155_2_, Vector3d p_203155_3_, double p_203155_4_) {
//        Vector3d vector3d = p_203155_3_.subtract(p_203155_0_.getX(), p_203155_0_.getY(), p_203155_0_.getZ());
//        return generateRandomPos(p_203155_0_, p_203155_1_, p_203155_2_, 0, vector3d, true, p_203155_4_, p_203155_0_::getWalkTargetValue, false, 0, 0, true);
//    }
//
//    @Nullable
//    public static Vector3d getAirPosTowards(CreatureEntity p_226344_0_, int p_226344_1_, int p_226344_2_, int p_226344_3_, Vector3d p_226344_4_, double p_226344_5_) {
//        Vector3d vector3d = p_226344_4_.subtract(p_226344_0_.getX(), p_226344_0_.getY(), p_226344_0_.getZ());
//        return generateRandomPos(p_226344_0_, p_226344_1_, p_226344_2_, p_226344_3_, vector3d, false, p_226344_5_, p_226344_0_::getWalkTargetValue, true, 0, 0, false);
//    }
//
//    @Nullable
//    public static Vector3d getPosAvoid(CreatureEntity p_75461_0_, int p_75461_1_, int p_75461_2_, Vector3d p_75461_3_) {
//        Vector3d vector3d = p_75461_0_.position().subtract(p_75461_3_);
//        return generateRandomPos(p_75461_0_, p_75461_1_, p_75461_2_, 0, vector3d, true, (double)((float)Math.PI / 2F), p_75461_0_::getWalkTargetValue, false, 0, 0, true);
//    }
//
//    @Nullable
//    public static Vector3d getLandPosAvoid(CreatureEntity entity, int maxDistance, int yVariance, Vector3d avoid) {
//        Vector3d vector3d = entity.position().subtract(avoid);
//        return generateRandomPos(entity, maxDistance, yVariance, 0, vector3d, false, (double)((float)Math.PI / 2F), entity::getWalkTargetValue, true, 0, 0, true);
//    }
//
//    @Nullable
//    private static Vector3d generateRandomPos(CreatureEntity entity, int maxDistance, int yVariance, int baseY, @Nullable Vector3d direction, boolean canPathfindWater, double yawVariance, ToDoubleFunction<BlockPos> distanceGetter, boolean onGround, int aboveSolidAmountVariance, int aboveSoidAmount, boolean checkNavigatable) {
//        PathNavigator pathnavigator = entity.getNavigation();
//        Random random = entity.getRandom();
//        boolean restricted;
//        if (entity.hasRestriction()) {
//            restricted = entity.getRestrictCenter().closerThan(entity.position(), (double)(entity.getRestrictRadius() + (float)maxDistance) + 1.0D);
//        } else {
//            restricted = false;
//        }
//
//        boolean found = false;
//        double d0 = Double.NEGATIVE_INFINITY;
//        BlockPos blockpos = entity.blockPosition();
//
//        for(int i = 0; i < 10; ++i) {
//            BlockPos randomPos = getRandomDelta(random, maxDistance, yVariance, baseY, direction, yawVariance);
//            if (randomPos != null) {
//                int j = randomPos.getX();
//                int k = randomPos.getY();
//                int l = randomPos.getZ();
//                if (entity.hasRestriction() && maxDistance > 1) {
//                    BlockPos restrictCenter = entity.getRestrictCenter();
//                    if (entity.getX() > (double)restrictCenter.getX()) {
//                        j -= random.nextInt(maxDistance / 2);
//                    } else {
//                        j += random.nextInt(maxDistance / 2);
//                    }
//
//                    if (entity.getZ() > (double)restrictCenter.getZ()) {
//                        l -= random.nextInt(maxDistance / 2);
//                    } else {
//                        l += random.nextInt(maxDistance / 2);
//                    }
//                }
//
//                BlockPos newPos = new BlockPos((double)j + entity.getX(), (double)k + entity.getY(), (double)l + entity.getZ());
//                if (newPos.getY() >= 0 && newPos.getY() <= entity.level.getMaxBuildHeight() && (!restricted || entity.isWithinRestriction(newPos)) && (!checkNavigatable || pathnavigator.isStableDestination(newPos))) {
//                    if (onGround) {
//                        newPos = moveUpToAboveSolid(newPos, random.nextInt(aboveSolidAmountVariance + 1) + aboveSoidAmount, entity.level.getMaxBuildHeight(), (pos) -> {
//                            return entity.level.getBlockState(pos).getMaterial().isSolid();
//                        });
//                    }
//
//                    if (canPathfindWater || !entity.level.getFluidState(newPos).is(FluidTags.WATER)) {
//                        PathNodeType pathnodetype = WalkNodeProcessor.getBlockPathTypeStatic(entity.level, newPos.mutable());
//                        if (entity.getPathfindingMalus(pathnodetype) == 0.0F) {
//                            double d1 = distanceGetter.applyAsDouble(newPos);
//                            if (d1 > d0) {
//                                d0 = d1;
//                                blockpos = newPos;
//                                found = true;
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//        return found ? Vector3d.atBottomCenterOf(blockpos) : null;
//    }
//
//    @Nullable
//    private static BlockPos getRandomDelta(Random rand, int maxDistance, int yVariance, int baseY, @Nullable Vector3d direction, double yawVariance) {
//        if (direction != null && !(yawVariance >= Math.PI)) {
//            double baseYaw = MathHelper.atan2(direction.z, direction.x) - (double)((float)Math.PI / 2F);
//            double theta = baseYaw + (double)(2.0F * rand.nextFloat() - 1.0F) * yawVariance;
//            double radius = Math.sqrt(rand.nextDouble()) * (double)MathHelper.SQRT_OF_TWO * (double)maxDistance;
//            double x = -radius * Math.sin(theta);
//            double z = radius * Math.cos(theta);
//            if (!(Math.abs(x) > (double)maxDistance) && !(Math.abs(z) > (double)maxDistance)) {
//                int y = rand.nextInt(2 * yVariance + 1) - yVariance + baseY;
//                return new BlockPos(x, (double)y, z);
//            } else {
//                return null;
//            }
//        } else {
//            int i = rand.nextInt(2 * maxDistance + 1) - maxDistance;
//            int j = rand.nextInt(2 * yVariance + 1) - yVariance + baseY;
//            int k = rand.nextInt(2 * maxDistance + 1) - maxDistance;
//            return new BlockPos(i, j, k);
//        }
//    }
//
//    static BlockPos moveUpToAboveSolid(BlockPos baseBlock, int aboveSolidAmount, int maxY, Predicate<BlockPos> predicate) {
//        if (aboveSolidAmount < 0) {
//            throw new IllegalArgumentException("aboveSolidAmount was " + aboveSolidAmount + ", expected >= 0");
//        } else if (!predicate.test(baseBlock)) {
//            return baseBlock;
//        } else {
//            BlockPos blockpos;
//            for(blockpos = baseBlock.above(); blockpos.getY() < maxY && predicate.test(blockpos); blockpos = blockpos.above()) {
//            }
//
//            BlockPos blockpos1;
//            BlockPos blockpos2;
//            for(blockpos1 = blockpos; blockpos1.getY() < maxY && blockpos1.getY() - blockpos.getY() < aboveSolidAmount; blockpos1 = blockpos2) {
//                blockpos2 = blockpos1.above();
//                if (predicate.test(blockpos2)) {
//                    break;
//                }
//            }
//
//            return blockpos1;
//        }
//    }
//}