//package net.minecraft.world.spawner;
//
//import it.unimi.dsi.fastutil.objects.Object2IntMap;
//import it.unimi.dsi.fastutil.objects.Object2IntMaps;
//import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Objects;
//import java.util.Random;
//import java.util.function.Consumer;
//import java.util.stream.Stream;
//import javax.annotation.Nullable;
//import net.minecraft.block.BlockState;
//import net.minecraft.block.Blocks;
//import net.minecraft.entity.Entity;
//import net.minecraft.entity.EntityClassification;
//import net.minecraft.entity.EntitySpawnPlacementRegistry;
//import net.minecraft.entity.EntityType;
//import net.minecraft.entity.ILivingEntityData;
//import net.minecraft.entity.MobEntity;
//import net.minecraft.entity.SpawnReason;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.fluid.FluidState;
//import net.minecraft.nbt.CompoundNBT;
//import net.minecraft.pathfinding.PathType;
//import net.minecraft.tags.BlockTags;
//import net.minecraft.tags.FluidTags;
//import net.minecraft.util.Direction;
//import net.minecraft.util.WeightedRandom;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.math.ChunkPos;
//import net.minecraft.util.math.MathHelper;
//import net.minecraft.util.math.vector.Vector3d;
//import net.minecraft.util.registry.Registry;
//import net.minecraft.world.IBlockReader;
//import net.minecraft.world.IServerWorld;
//import net.minecraft.world.IWorldReader;
//import net.minecraft.world.World;
//import net.minecraft.world.biome.Biome;
//import net.minecraft.world.biome.DefaultBiomeMagnifier;
//import net.minecraft.world.biome.MobSpawnInfo;
//import net.minecraft.world.chunk.Chunk;
//import net.minecraft.world.chunk.IChunk;
//import net.minecraft.world.gen.ChunkGenerator;
//import net.minecraft.world.gen.Heightmap;
//import net.minecraft.world.gen.feature.structure.Structure;
//import net.minecraft.world.gen.feature.structure.StructureManager;
//import net.minecraft.world.server.ServerWorld;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//
//public final class WorldEntitySpawner {
//    private static final Logger LOGGER = LogManager.getLogger();
//    private static final int MAGIC_NUMBER = (int)Math.pow(17.0D, 2.0D);
//    private static final EntityClassification[] SPAWNING_CATEGORIES = Stream.of(EntityClassification.values()).filter((p_234965_0_) -> {
//        return p_234965_0_ != EntityClassification.MISC;
//    }).toArray((p_234963_0_) -> {
//        return new EntityClassification[p_234963_0_];
//    });
//
//    public static WorldEntitySpawner.EntityDensityManager createState(int naturalSpawnChunkCount, Iterable<Entity> entities, WorldEntitySpawner.IInitialDensityAdder chunkQuery) {
//        MobDensityTracker mobdensitytracker = new MobDensityTracker();
//        Object2IntOpenHashMap<EntityClassification> classifications = new Object2IntOpenHashMap<>();
//        Iterator iterator = entities.iterator();
//
//        while(true) {
//            Entity entity;
//            MobEntity mobentity;
//            do {
//                if (!iterator.hasNext()) {
//                    return new WorldEntitySpawner.EntityDensityManager(naturalSpawnChunkCount, classifications, mobdensitytracker);
//                }
//
//                entity = (Entity)iterator.next();
//                if (!(entity instanceof MobEntity)) {
//                    break;
//                }
//
//                mobentity = (MobEntity)entity;
//            } while(mobentity.isPersistenceRequired() || mobentity.requiresCustomPersistence());
//
//            final Entity entity_f = entity;
//            EntityClassification entityclassification = entity.getClassification(true);
//            if (entityclassification != EntityClassification.MISC) {
//                BlockPos blockpos = entity.blockPosition();
//                long pos = ChunkPos.asLong(blockpos.getX() >> 4, blockpos.getZ() >> 4);
//                chunkQuery.query(pos, (chunk) -> {
//                    MobSpawnInfo.SpawnCosts spawnCosts = getRoughBiome(blockpos, chunk).getMobSettings().getMobSpawnCost(entity_f.getType());
//                    if (spawnCosts != null) {
//                        mobdensitytracker.addCharge(entity_f.blockPosition(), spawnCosts.getCharge());
//                    }
//
//                    classifications.addTo(entityclassification, 1);
//                });
//            }
//        }
//    }
//
//    private static Biome getRoughBiome(BlockPos pos, IChunk chunk) {
//        return DefaultBiomeMagnifier.INSTANCE.getBiome(0L, pos.getX(), pos.getY(), pos.getZ(), chunk.getBiomes());
//    }
//
//    public static void spawnForChunk(ServerWorld world, Chunk chunk, WorldEntitySpawner.EntityDensityManager spawnState, boolean spawnFriendlies, boolean spawnEnemies, boolean tickCondition) {
//        world.getProfiler().push("spawner");
//
//        for(EntityClassification entityclassification : SPAWNING_CATEGORIES) {
//            if ((spawnFriendlies || !entityclassification.isFriendly()) && (spawnEnemies || entityclassification.isFriendly()) && (tickCondition || !entityclassification.isPersistent()) && spawnState.canSpawnForCategory(entityclassification)) {
//                spawnCategoryForChunk(entityclassification, world, chunk, (type, pos, chunk1) -> {
//                    return spawnState.canSpawn(type, pos, chunk1);
//                }, (entity, ichunk) -> {
//                    spawnState.afterSpawn(entity, ichunk);
//                });
//            }
//        }
//
//        world.getProfiler().pop();
//    }
//
//    public static void spawnCategoryForChunk(EntityClassification category, ServerWorld world, Chunk chunk, WorldEntitySpawner.IDensityCheck spawnCheck, WorldEntitySpawner.IOnSpawnDensityAdder spawnAfter) {
//        BlockPos blockpos = getRandomPosWithin(world, chunk);
//        if (blockpos.getY() >= 1) {
//            spawnCategoryForPosition(category, world, chunk, blockpos, spawnCheck, spawnAfter);
//        }
//    }
//
//    public static void spawnCategoryForPosition(EntityClassification category, ServerWorld world, IChunk chunk, BlockPos pos, WorldEntitySpawner.IDensityCheck spawnCheck, WorldEntitySpawner.IOnSpawnDensityAdder spawnAfter) {
//        StructureManager structuremanager = world.structureFeatureManager();
//        ChunkGenerator chunkgenerator = world.getChunkSource().getGenerator();
//        int spawnY = pos.getY();
//        BlockState blockstate = chunk.getBlockState(pos);
//        if (!blockstate.isRedstoneConductor(chunk, pos)) {
//            BlockPos.Mutable mutable = new BlockPos.Mutable();
//            int packCount = 0;
//
//            for(int k = 0; k < 3; ++k) {
//                int randX = pos.getX();
//                int randZ = pos.getZ();
//                int j1 = 6;
//                MobSpawnInfo.Spawners mobSpawnInfo = null;
//                ILivingEntityData ilivingentitydata = null;
//                int groupSize = MathHelper.ceil(world.random.nextFloat() * 4.0F);
//                int groupCount = 0;
//
//                for(int i = 0; i < groupSize; ++i) {
//                    randX += world.random.nextInt(6) - world.random.nextInt(6);
//                    randZ += world.random.nextInt(6) - world.random.nextInt(6);
//                    mutable.set(randX, spawnY, randZ);
//                    double spawnX = (double)randX + 0.5D;
//                    double spawnZ = (double)randZ + 0.5D;
//                    PlayerEntity playerentity = world.getNearestPlayer(spawnX, (double)spawnY, spawnZ, -1.0D, false);
//                    if (playerentity != null) {
//                        double d2 = playerentity.distanceToSqr(spawnX, (double)spawnY, spawnZ);
//                        if (isRightDistanceToPlayerAndSpawnPoint(world, chunk, mutable, d2)) {
//                            if (mobSpawnInfo == null) {
//                                mobSpawnInfo = getRandomSpawnMobAt(world, structuremanager, chunkgenerator, category, world.random, mutable);
//                                if (mobSpawnInfo == null) {
//                                    break;
//                                }
//
//                                groupSize = mobSpawnInfo.minCount + world.random.nextInt(1 + mobSpawnInfo.maxCount - mobSpawnInfo.minCount);
//                            }
//
//                            if (isValidSpawnPostitionForType(world, category, structuremanager, chunkgenerator, mobSpawnInfo, mutable, d2) && spawnCheck.test(mobSpawnInfo.type, mutable, chunk)) {
//                                MobEntity mobentity = getMobForSpawn(world, mobSpawnInfo.type);
//                                if (mobentity == null) {
//                                    return;
//                                }
//
//                                mobentity.moveTo(spawnX, (double)spawnY, spawnZ, world.random.nextFloat() * 360.0F, 0.0F);
//                                int canSpawn = net.minecraftforge.common.ForgeHooks.canEntitySpawn(mobentity, world, spawnX, spawnY, spawnZ, null, SpawnReason.NATURAL);
//                                if (canSpawn != -1 && (canSpawn == 1 || isValidPositionForMob(world, mobentity, d2))) {
//                                    if (!net.minecraftforge.event.ForgeEventFactory.doSpecialSpawn(mobentity, world, (float)spawnX, (float)spawnY, (float)spawnZ, null, SpawnReason.NATURAL))
//                                        ilivingentitydata = mobentity.finalizeSpawn(world, world.getCurrentDifficultyAt(mobentity.blockPosition()), SpawnReason.NATURAL, ilivingentitydata, (CompoundNBT)null);
//                                    ++packCount;
//                                    ++groupCount;
//                                    world.addFreshEntityWithPassengers(mobentity);
//                                    spawnAfter.run(mobentity, chunk);
//                                    if (packCount >= net.minecraftforge.event.ForgeEventFactory.getMaxSpawnPackSize(mobentity)) {
//                                        return;
//                                    }
//
//                                    if (mobentity.isMaxGroupSizeReached(groupCount)) {
//                                        break;
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//
//        }
//    }
//
//    private static boolean isRightDistanceToPlayerAndSpawnPoint(ServerWorld world, IChunk p_234978_1_, BlockPos.Mutable pos, double distanceSquared) {
//        if (distanceSquared <= 576.0D) {
//            return false;
//        } else if (world.getSharedSpawnPos().closerThan(new Vector3d((double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D), 24.0D)) {
//            return false;
//        } else {
//            ChunkPos chunkpos = new ChunkPos(pos);
//            return Objects.equals(chunkpos, p_234978_1_.getPos()) || world.getChunkSource().isEntityTickingChunk(chunkpos);
//        }
//    }
//
//    private static boolean isValidSpawnPostitionForType(ServerWorld world, EntityClassification classification, StructureManager structure, ChunkGenerator chunkGen, MobSpawnInfo.Spawners spawnInfo, BlockPos.Mutable pos, double distanceSq) {
//        EntityType<?> type = spawnInfo.type;
//        if (type.getCategory() == EntityClassification.MISC) {
//            return false;
//        } else if (!type.canSpawnFarFromPlayer() && distanceSq > (double)(type.getCategory().getDespawnDistance() * type.getCategory().getDespawnDistance())) {
//            return false;
//        } else if (type.canSummon() && canSpawnMobAt(world, structure, chunkGen, classification, spawnInfo, pos)) {
//            EntitySpawnPlacementRegistry.PlacementType placementType = EntitySpawnPlacementRegistry.getPlacementType(type);
//            if (!isSpawnPositionOk(placementType, world, pos, type)) {
//                return false;
//            } else if (!EntitySpawnPlacementRegistry.checkSpawnRules(type, world, SpawnReason.NATURAL, pos, world.random)) {
//                return false;
//            } else {
//                return world.noCollision(type.getAABB((double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D));
//            }
//        } else {
//            return false;
//        }
//    }
//
//    @Nullable
//    private static MobEntity getMobForSpawn(ServerWorld world, EntityType<?> type) {
//        try {
//            Entity entity = type.create(world);
//            if (!(entity instanceof MobEntity)) {
//                throw new IllegalStateException("Trying to spawn a non-mob: " + Registry.ENTITY_TYPE.getKey(type));
//            } else {
//                return (MobEntity)entity;
//            }
//        } catch (Exception exception) {
//            LOGGER.warn("Failed to create mob", (Throwable)exception);
//            return null;
//        }
//    }
//
//    private static boolean isValidPositionForMob(ServerWorld world, MobEntity entity, double distanceSquared) {
//        if (distanceSquared > (double)(entity.getType().getCategory().getDespawnDistance() * entity.getType().getCategory().getDespawnDistance()) && entity.removeWhenFarAway(distanceSquared)) {
//            return false;
//        } else {
//            return entity.checkSpawnRules(world, SpawnReason.NATURAL) && entity.checkSpawnObstruction(world);
//        }
//    }
//
//    @Nullable
//    private static MobSpawnInfo.Spawners getRandomSpawnMobAt(ServerWorld world, StructureManager p_234977_1_, ChunkGenerator p_234977_2_, EntityClassification p_234977_3_, Random p_234977_4_, BlockPos p_234977_5_) {
//        Biome biome = world.getBiome(p_234977_5_);
//        if (p_234977_3_ == EntityClassification.WATER_AMBIENT && biome.getBiomeCategory() == Biome.Category.RIVER && p_234977_4_.nextFloat() < 0.98F) {
//            return null;
//        } else {
//            List<MobSpawnInfo.Spawners> list = mobsAt(world, p_234977_1_, p_234977_2_, p_234977_3_, p_234977_5_, biome);
//            list = net.minecraftforge.event.ForgeEventFactory.getPotentialSpawns(world, p_234977_3_, p_234977_5_, list);
//            return list.isEmpty() ? null : WeightedRandom.getRandomItem(p_234977_4_, list);
//        }
//    }
//
//    private static boolean canSpawnMobAt(ServerWorld p_234976_0_, StructureManager p_234976_1_, ChunkGenerator p_234976_2_, EntityClassification p_234976_3_, MobSpawnInfo.Spawners p_234976_4_, BlockPos p_234976_5_) {
//        return mobsAt(p_234976_0_, p_234976_1_, p_234976_2_, p_234976_3_, p_234976_5_, (Biome)null).contains(p_234976_4_);
//    }
//
//    private static List<MobSpawnInfo.Spawners> mobsAt(ServerWorld world, StructureManager structureManager, ChunkGenerator chunkGen, EntityClassification category, BlockPos pos, @Nullable Biome biome) {
//        return category == EntityClassification.MONSTER && world.getBlockState(pos.below()).getBlock() == Blocks.NETHER_BRICKS && structureManager.getStructureAt(pos, false, Structure.NETHER_BRIDGE).isValid() ? Structure.NETHER_BRIDGE.getSpecialEnemies() : chunkGen.getMobsAt(biome != null ? biome : world.getBiome(pos), structureManager, category, pos);
//    }
//
//    private static BlockPos getRandomPosWithin(World p_222262_0_, Chunk p_222262_1_) {
//        ChunkPos chunkpos = p_222262_1_.getPos();
//        int i = chunkpos.getMinBlockX() + p_222262_0_.random.nextInt(16);
//        int j = chunkpos.getMinBlockZ() + p_222262_0_.random.nextInt(16);
//        int k = p_222262_1_.getHeight(Heightmap.Type.WORLD_SURFACE, i, j) + 1;
//        int l = p_222262_0_.random.nextInt(k + 1);
//        return new BlockPos(i, l, j);
//    }
//
//    public static boolean isValidEmptySpawnBlock(IBlockReader p_234968_0_, BlockPos p_234968_1_, BlockState p_234968_2_, FluidState p_234968_3_, EntityType<?> p_234968_4_) {
//        if (p_234968_2_.isCollisionShapeFullBlock(p_234968_0_, p_234968_1_)) {
//            return false;
//        } else if (p_234968_2_.isSignalSource()) {
//            return false;
//        } else if (!p_234968_3_.isEmpty()) {
//            return false;
//        } else if (p_234968_2_.is(BlockTags.PREVENT_MOB_SPAWNING_INSIDE)) {
//            return false;
//        } else {
//            return !p_234968_4_.isBlockDangerous(p_234968_2_);
//        }
//    }
//
//    public static boolean isSpawnPositionOk(EntitySpawnPlacementRegistry.PlacementType p_209382_0_, IWorldReader p_209382_1_, BlockPos p_209382_2_, @Nullable EntityType<?> p_209382_3_) {
//        if (p_209382_0_ == EntitySpawnPlacementRegistry.PlacementType.NO_RESTRICTIONS) {
//            return true;
//        } else if (p_209382_3_ != null && p_209382_1_.getWorldBorder().isWithinBounds(p_209382_2_)) {
//            return p_209382_0_.canSpawnAt(p_209382_1_, p_209382_2_, p_209382_3_);
//        }
//        return false;
//    }
//
//    public static boolean canSpawnAtBody(EntitySpawnPlacementRegistry.PlacementType placement, IWorldReader world, BlockPos pos, @Nullable EntityType<?> type) {
//        {
//            BlockState blockstate = world.getBlockState(pos);
//            FluidState fluidstate = world.getFluidState(pos);
//            BlockPos blockpos = pos.above();
//            BlockPos blockpos1 = pos.below();
//            switch(placement) {
//                case IN_WATER:
//                    return fluidstate.is(FluidTags.WATER) && world.getFluidState(blockpos1).is(FluidTags.WATER) && !world.getBlockState(blockpos).isRedstoneConductor(world, blockpos);
//                case IN_LAVA:
//                    return fluidstate.is(FluidTags.LAVA);
//                case ON_GROUND:
//                default:
//                    BlockState blockstate1 = world.getBlockState(blockpos1);
//                    if (!blockstate1.canCreatureSpawn(world, blockpos1, placement, type)) {
//                        return false;
//                    } else {
//                        return isValidEmptySpawnBlock(world, pos, blockstate, fluidstate, type) && isValidEmptySpawnBlock(world, blockpos, world.getBlockState(blockpos), world.getFluidState(blockpos), type);
//                    }
//            }
//        }
//    }
//
//    public static void spawnMobsForChunkGeneration(IServerWorld p_77191_0_, Biome p_77191_1_, int p_77191_2_, int p_77191_3_, Random p_77191_4_) {
//        MobSpawnInfo mobspawninfo = p_77191_1_.getMobSettings();
//        List<MobSpawnInfo.Spawners> list = mobspawninfo.getMobs(EntityClassification.CREATURE);
//        if (!list.isEmpty()) {
//            int i = p_77191_2_ << 4;
//            int j = p_77191_3_ << 4;
//
//            while(p_77191_4_.nextFloat() < mobspawninfo.getCreatureProbability()) {
//                MobSpawnInfo.Spawners mobspawninfo$spawners = WeightedRandom.getRandomItem(p_77191_4_, list);
//                int k = mobspawninfo$spawners.minCount + p_77191_4_.nextInt(1 + mobspawninfo$spawners.maxCount - mobspawninfo$spawners.minCount);
//                ILivingEntityData ilivingentitydata = null;
//                int l = i + p_77191_4_.nextInt(16);
//                int i1 = j + p_77191_4_.nextInt(16);
//                int j1 = l;
//                int k1 = i1;
//
//                for(int l1 = 0; l1 < k; ++l1) {
//                    boolean flag = false;
//
//                    for(int i2 = 0; !flag && i2 < 4; ++i2) {
//                        BlockPos blockpos = getTopNonCollidingPos(p_77191_0_, mobspawninfo$spawners.type, l, i1);
//                        if (mobspawninfo$spawners.type.canSummon() && isSpawnPositionOk(EntitySpawnPlacementRegistry.getPlacementType(mobspawninfo$spawners.type), p_77191_0_, blockpos, mobspawninfo$spawners.type)) {
//                            float f = mobspawninfo$spawners.type.getWidth();
//                            double d0 = MathHelper.clamp((double)l, (double)i + (double)f, (double)i + 16.0D - (double)f);
//                            double d1 = MathHelper.clamp((double)i1, (double)j + (double)f, (double)j + 16.0D - (double)f);
//                            if (!p_77191_0_.noCollision(mobspawninfo$spawners.type.getAABB(d0, (double)blockpos.getY(), d1)) || !EntitySpawnPlacementRegistry.checkSpawnRules(mobspawninfo$spawners.type, p_77191_0_, SpawnReason.CHUNK_GENERATION, new BlockPos(d0, (double)blockpos.getY(), d1), p_77191_0_.getRandom())) {
//                                continue;
//                            }
//
//                            Entity entity;
//                            try {
//                                entity = mobspawninfo$spawners.type.create(p_77191_0_.getLevel());
//                            } catch (Exception exception) {
//                                LOGGER.warn("Failed to create mob", (Throwable)exception);
//                                continue;
//                            }
//
//                            entity.moveTo(d0, (double)blockpos.getY(), d1, p_77191_4_.nextFloat() * 360.0F, 0.0F);
//                            if (entity instanceof MobEntity) {
//                                MobEntity mobentity = (MobEntity)entity;
//                                if (net.minecraftforge.common.ForgeHooks.canEntitySpawn(mobentity, p_77191_0_, d0, blockpos.getY(), d1, null, SpawnReason.CHUNK_GENERATION) == -1) continue;
//                                if (mobentity.checkSpawnRules(p_77191_0_, SpawnReason.CHUNK_GENERATION) && mobentity.checkSpawnObstruction(p_77191_0_)) {
//                                    ilivingentitydata = mobentity.finalizeSpawn(p_77191_0_, p_77191_0_.getCurrentDifficultyAt(mobentity.blockPosition()), SpawnReason.CHUNK_GENERATION, ilivingentitydata, (CompoundNBT)null);
//                                    p_77191_0_.addFreshEntityWithPassengers(mobentity);
//                                    flag = true;
//                                }
//                            }
//                        }
//
//                        l += p_77191_4_.nextInt(5) - p_77191_4_.nextInt(5);
//
//                        for(i1 += p_77191_4_.nextInt(5) - p_77191_4_.nextInt(5); l < i || l >= i + 16 || i1 < j || i1 >= j + 16; i1 = k1 + p_77191_4_.nextInt(5) - p_77191_4_.nextInt(5)) {
//                            l = j1 + p_77191_4_.nextInt(5) - p_77191_4_.nextInt(5);
//                        }
//                    }
//                }
//            }
//
//        }
//    }
//
//    private static BlockPos getTopNonCollidingPos(IWorldReader p_208498_0_, EntityType<?> p_208498_1_, int p_208498_2_, int p_208498_3_) {
//        int i = p_208498_0_.getHeight(EntitySpawnPlacementRegistry.getHeightmapType(p_208498_1_), p_208498_2_, p_208498_3_);
//        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable(p_208498_2_, i, p_208498_3_);
//        if (p_208498_0_.dimensionType().hasCeiling()) {
//            do {
//                blockpos$mutable.move(Direction.DOWN);
//            } while(!p_208498_0_.getBlockState(blockpos$mutable).isAir());
//
//            do {
//                blockpos$mutable.move(Direction.DOWN);
//            } while(p_208498_0_.getBlockState(blockpos$mutable).isAir() && blockpos$mutable.getY() > 0);
//        }
//
//        if (EntitySpawnPlacementRegistry.getPlacementType(p_208498_1_) == EntitySpawnPlacementRegistry.PlacementType.ON_GROUND) {
//            BlockPos blockpos = blockpos$mutable.below();
//            if (p_208498_0_.getBlockState(blockpos).isPathfindable(p_208498_0_, blockpos, PathType.LAND)) {
//                return blockpos;
//            }
//        }
//
//        return blockpos$mutable.immutable();
//    }
//
//    public static class EntityDensityManager {
//        private final int spawnableChunkCount;
//        private final Object2IntOpenHashMap<EntityClassification> mobCategoryCounts;
//        private final MobDensityTracker spawnPotential;
//        private final Object2IntMap<EntityClassification> unmodifiableMobCategoryCounts;
//        @Nullable
//        private BlockPos lastCheckedPos;
//        @Nullable
//        private EntityType<?> lastCheckedType;
//        private double lastCharge;
//
//        private EntityDensityManager(int p_i231621_1_, Object2IntOpenHashMap<EntityClassification> p_i231621_2_, MobDensityTracker p_i231621_3_) {
//            this.spawnableChunkCount = p_i231621_1_;
//            this.mobCategoryCounts = p_i231621_2_;
//            this.spawnPotential = p_i231621_3_;
//            this.unmodifiableMobCategoryCounts = Object2IntMaps.unmodifiable(p_i231621_2_);
//        }
//
//        private boolean canSpawn(EntityType<?> type, BlockPos pos, IChunk chunk) {
//            this.lastCheckedPos = pos;
//            this.lastCheckedType = type;
//            MobSpawnInfo.SpawnCosts spawnCosts = WorldEntitySpawner.getRoughBiome(pos, chunk).getMobSettings().getMobSpawnCost(type);
//            if (spawnCosts == null) {
//                this.lastCharge = 0.0D;
//                return true;
//            } else {
//                double d0 = spawnCosts.getCharge();
//                this.lastCharge = d0;
//                double d1 = this.spawnPotential.getPotentialEnergyChange(pos, d0);
//                return d1 <= spawnCosts.getEnergyBudget();
//            }
//        }
//
//        private void afterSpawn(MobEntity entity, IChunk iChunk) {
//            EntityType<?> type = entity.getType();
//            BlockPos blockpos = entity.blockPosition();
//            double d0;
//            if (blockpos.equals(this.lastCheckedPos) && type == this.lastCheckedType) {
//                d0 = this.lastCharge;
//            } else {
//                MobSpawnInfo.SpawnCosts spawnCosts = WorldEntitySpawner.getRoughBiome(blockpos, iChunk).getMobSettings().getMobSpawnCost(type);
//                if (spawnCosts != null) {
//                    d0 = spawnCosts.getCharge();
//                } else {
//                    d0 = 0.0D;
//                }
//            }
//
//            this.spawnPotential.addCharge(blockpos, d0);
//            this.mobCategoryCounts.addTo(type.getCategory(), 1);
//        }
//
//        @OnlyIn(Dist.CLIENT)
//        public int getSpawnableChunkCount() {
//            return this.spawnableChunkCount;
//        }
//
//        public Object2IntMap<EntityClassification> getMobCategoryCounts() {
//            return this.unmodifiableMobCategoryCounts;
//        }
//
//        private boolean canSpawnForCategory(EntityClassification category) {
//            int i = category.getMaxInstancesPerChunk() * this.spawnableChunkCount / WorldEntitySpawner.MAGIC_NUMBER;
//            return this.mobCategoryCounts.getInt(category) < i;
//        }
//    }
//
//    @FunctionalInterface
//    public interface IDensityCheck {
//        boolean test(EntityType<?> p_test_1_, BlockPos p_test_2_, IChunk p_test_3_);
//    }
//
//    @FunctionalInterface
//    public interface IInitialDensityAdder {
//        void query(long p_query_1_, Consumer<Chunk> p_query_3_);
//    }
//
//    @FunctionalInterface
//    public interface IOnSpawnDensityAdder {
//        void run(MobEntity p_run_1_, IChunk p_run_2_);
//    }
//}