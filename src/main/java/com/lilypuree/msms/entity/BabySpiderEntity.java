package com.lilypuree.msms.entity;

import com.lilypuree.msms.entity.ai.MakeNestGoal;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.SpiderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.function.Predicate;

public class BabySpiderEntity extends SpiderEntity {

    public BabySpiderEntity(EntityType<? extends SpiderEntity> type, World world) {
        super(type, world);
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MonsterEntity.createMonsterAttributes().add(Attributes.MAX_HEALTH, 2.0D).add(Attributes.MOVEMENT_SPEED, (double) 0.4F);
    }

    @Override
    public boolean causeFallDamage(float p_225503_1_, float p_225503_2_) {
        return false;
    }

    @Override
    protected void registerGoals() {
//        super.registerGoals();
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(3, new LeapAtTargetGoal(this, 0.4F));

        this.goalSelector.addGoal(3, new AvoidGoal<>(this, BabySpiderEntity.class, 30.0f, 1.0, 1.0));
        this.goalSelector.addGoal(3, new AvoidGoal<>(this, PlayerEntity.class, 30.0f, 1.0, 1.0));

        this.goalSelector.addGoal(4, new MakeNestGoal(this, 1.0));

        this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 0.8D));
        this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(6, new LookRandomlyGoal(this));

    }

    @Override
    protected PathNavigator createNavigation(World world) {
        return new GroundPathNavigator(this, world);
    }

    @Override
    public float getScale() {
        return 0.8f;
    }

    protected float getStandingEyeHeight(Pose pose, EntitySize size) {
        return 0.4F;
    }

    @Override
    public float getPathfindingMalus(PathNodeType p_184643_1_) {
        return super.getPathfindingMalus(p_184643_1_);
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.SPIDER_STEP, 0.15F, 1.3F);
    }

    @Override
    protected float getVoicePitch() {
        return 1.4f;
    }

    @Override
    protected float getSoundVolume() {
        return 0.8f;
    }

    public static class AvoidGoal<T extends LivingEntity> extends Goal {
        protected final CreatureEntity mob;
        private final double walkSpeedModifier;
        private final double sprintSpeedModifier;
        protected T toAvoid;
        protected final float maxDist;
        protected Path path;
        protected final PathNavigator pathNav;
        protected final Class<T> avoidClass;
        protected final Predicate<LivingEntity> avoidPredicate;
        protected final Predicate<LivingEntity> predicateOnAvoidEntity;
        private final EntityPredicate avoidEntityTargeting;

        public AvoidGoal(CreatureEntity entity, Class<T> avoidClass, float maxDist, double walkSpeed, double sprintSpeed) {
            this(entity, avoidClass, (entity1) -> {
                return true;
            }, maxDist, walkSpeed, sprintSpeed, EntityPredicates.NO_CREATIVE_OR_SPECTATOR::test);
        }

        public AvoidGoal(CreatureEntity entity, Class<T> avoidClass, Predicate<LivingEntity> avoidPredicate, float maxDist, double walkSpeed, double sprintSPeed, Predicate<LivingEntity> predicateOnAvoidEntity) {
            this.mob = entity;
            this.avoidClass = avoidClass;
            this.avoidPredicate = avoidPredicate;
            this.maxDist = maxDist;
            this.walkSpeedModifier = walkSpeed;
            this.sprintSpeedModifier = sprintSPeed;
            this.predicateOnAvoidEntity = predicateOnAvoidEntity;
            this.pathNav = entity.getNavigation();
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
            this.avoidEntityTargeting = (new EntityPredicate()).range((double) maxDist).selector(predicateOnAvoidEntity.and(avoidPredicate));
        }

        public AvoidGoal(CreatureEntity entity, Class<T> avoidClass, float maxDist, double walkSpeed, double sprintSpeed, Predicate<LivingEntity> predicateOnAvoidEntity) {
            this(entity, avoidClass, (p_203782_0_) -> {
                return true;
            }, maxDist, walkSpeed, sprintSpeed, predicateOnAvoidEntity);
        }

        public boolean canUse() {
            this.toAvoid = this.mob.level.getNearestLoadedEntity(this.avoidClass, this.avoidEntityTargeting, this.mob, this.mob.getX(), this.mob.getY(), this.mob.getZ(), this.mob.getBoundingBox().inflate((double) this.maxDist, 3.0D, (double) this.maxDist));
            if (this.toAvoid == null) {
                return false;
            } else {
                Vector3d vector3d = RandomPositionGenerator.getLandPosAvoid(this.mob, 16, 7, this.toAvoid.position());
                if (vector3d == null) {
                    return false;
                } else if (this.toAvoid.distanceToSqr(vector3d.x, vector3d.y, vector3d.z) < this.toAvoid.distanceToSqr(this.mob)) {
                    return false;
                } else {
                    this.path = this.pathNav.createPath(vector3d.x, vector3d.y, vector3d.z, 1);
                    return this.path != null;
                }
            }
        }

        public boolean canContinueToUse() {
            return !this.pathNav.isDone();
        }

        public void start() {
            this.pathNav.moveTo(this.path, this.walkSpeedModifier);
        }

        public void stop() {
            this.toAvoid = null;
        }

        public void tick() {
            if (this.mob.distanceToSqr(this.toAvoid) < 49.0D) {
                this.mob.getNavigation().setSpeedModifier(this.sprintSpeedModifier);
            } else {
                this.mob.getNavigation().setSpeedModifier(this.walkSpeedModifier);
            }

        }
    }


}
