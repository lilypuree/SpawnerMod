package com.lilypuree.msms.client;


import com.lilypuree.msms.MSMSMod;
import com.lilypuree.msms.entity.BabySpiderEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class BabySpiderRenderer<T extends BabySpiderEntity> extends MobRenderer<T, BabySpiderModel<T>> {
    private static final ResourceLocation SPIDER_LOCATION = new ResourceLocation(MSMSMod.MODID, "textures/entity/baby_spider.png");

    public BabySpiderRenderer(EntityRendererManager manager) {
        super(manager, new BabySpiderModel<>(), 0.5F);
        this.addLayer(new BabySpiderEyesLayer<>(this));
    }

    @Override
    protected float getFlipDegrees(T entity) {
        return 180.0F;
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return SPIDER_LOCATION;
    }
}
