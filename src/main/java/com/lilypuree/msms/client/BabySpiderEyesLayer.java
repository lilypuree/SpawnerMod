package com.lilypuree.msms.client;

import com.lilypuree.msms.MSMSMod;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.AbstractEyesLayer;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class BabySpiderEyesLayer<T extends Entity, M extends BabySpiderModel<T>> extends AbstractEyesLayer<T,M> {
    private static final RenderType SPIDER_EYES = RenderType.eyes(new ResourceLocation(MSMSMod.MODID,"textures/entity/baby_spider_eyes.png"));

    public BabySpiderEyesLayer(IEntityRenderer<T, M> renderer) {
        super(renderer);
    }

    @Override
    public RenderType renderType() {
        return SPIDER_EYES;
    }
}
