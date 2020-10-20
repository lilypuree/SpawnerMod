package com.lilypuree.msms.setup.world;

import com.lilypuree.msms.MSMSMod;
import com.lilypuree.msms.world.gen.placement.SpiderNestPlacement;
import com.lilypuree.msms.world.gen.treedecorator.SpiderTreeDecorator;
import com.mojang.serialization.Codec;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.NoPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class Decorators {

    public static void init() {
        TreeDecoratorTypes.init();
    }

    public static final Placement<NoPlacementConfig> SPIDER_NESTS = createDecorator("spider_nest", new SpiderNestPlacement(NoPlacementConfig.CODEC));

    public static class TreeDecoratorTypes {
        public static void init() {

        }

        public static final TreeDecoratorType<SpiderTreeDecorator> SPIDER_EGGS = register("spider_eggs", SpiderTreeDecorator.CODEC);
        public static final SpiderTreeDecorator SPIDER_EGG_0002 = new SpiderTreeDecorator(0.002F);
        public static final SpiderTreeDecorator SPIDER_EGG_002 = new SpiderTreeDecorator(0.02F);
        public static final SpiderTreeDecorator SPIDER_EGG_09 = new SpiderTreeDecorator(0.9F);

        private static <P extends TreeDecorator> TreeDecoratorType<P> register(String id, Codec<P> config) {
            Constructor<TreeDecoratorType> constructor = ObfuscationReflectionHelper.findConstructor(TreeDecoratorType.class, Codec.class);
            try {
                return Registry.register(Registry.TREE_DECORATOR_TYPES, new ResourceLocation(MSMSMod.MODID, id), constructor.newInstance(config));
            } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public static <DC extends IPlacementConfig, D extends Placement<DC>> D createDecorator(String id, D decorator) {
        Registry.register(Registry.DECORATOR, new ResourceLocation(MSMSMod.MODID, id), decorator);
        return decorator;
    }
}
