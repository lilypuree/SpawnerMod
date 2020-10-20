package com.lilypuree.msms.client;


import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class BabySpiderModel<T extends Entity> extends SegmentedModel<T> {
    private final ModelRenderer body;
    private final ModelRenderer head;
    private final ModelRenderer leg1;
    private final ModelRenderer leg3;
    private final ModelRenderer leg5;
    private final ModelRenderer leg7;
    private final ModelRenderer leg0;
    private final ModelRenderer leg2;
    private final ModelRenderer leg4;
    private final ModelRenderer leg6;

    public BabySpiderModel() {
        this.texWidth = 32;
        this.texHeight = 32;

        body = new ModelRenderer(this);
        body.setPos(0.0F, 18.0F, 0.0F);
        body.texOffs(0, 0).addBox(-3.0F, -3.0F, 0.0F, 6.0F, 6.0F, 6.0F, 0.0F, false);

        head = new ModelRenderer(this);
        head.setPos(0.0F, 19.0F, 0.0F);
        head.texOffs(0, 12).addBox(-2.5F, -2.5F, -5.0F, 5.0F, 5.0F, 5.0F, 0.0F, false);

        leg1 = new ModelRenderer(this);
        leg1.setPos(-3.0F, 19.0F, 2.0F);
        leg1.texOffs(13, 13).addBox(-7.0F, 0.0F, -1.0F, 7.0F, 0.0F, 2.0F, 0.0F, false);

        leg3 = new ModelRenderer(this);
        leg3.setPos(-3.0F, 19.0F, 1.0F);
        leg3.texOffs(13, 13).addBox(-7.0F, 0.0F, -1.0F, 7.0F, 0.0F, 2.0F, 0.0F, false);

        leg5 = new ModelRenderer(this);
        leg5.setPos(-2.5F, 19.5F, 0.0F);
        leg5.texOffs(13, 13).addBox(-6.5F, 0.0F, -1.0F, 7.0F, 0.0F, 2.0F, 0.0F, false);

        leg7 = new ModelRenderer(this);
        leg7.setPos(-2.5F, 19.5F, -1.0F);
        leg7.texOffs(13, 13).addBox(-6.5F, 0.0F, -1.0F, 7.0F, 0.0F, 2.0F, 0.0F, false);

        leg0 = new ModelRenderer(this);
        leg0.setPos(3.0F, 19.0F, 2.0F);
        leg0.texOffs(13, 13).addBox(0.0F, 0.0F, -1.0F, 7.0F, 0.0F, 2.0F, 0.0F, true);

        leg2 = new ModelRenderer(this);
        leg2.setPos(3.0F, 19.0F, 1.0F);
        leg2.texOffs(13, 13).addBox(0.0F, 0.0F, -1.0F, 7.0F, 0.0F, 2.0F, 0.0F, true);

        leg4 = new ModelRenderer(this);
        leg4.setPos(2.5F, 19.5F, 0.0F);
        leg4.texOffs(13, 13).addBox(-0.5F, 0.0F, -1.0F, 7.0F, 0.0F, 2.0F, 0.0F, true);

        leg6 = new ModelRenderer(this);
        leg6.setPos(2.5F, 19.5F, -1.0F);
        leg6.texOffs(13, 13).addBox(-0.5F, 0.0F, -1.0F, 7.0F, 0.0F, 2.0F, 0.0F, true);
    }

    @Override
    public Iterable<ModelRenderer> parts() {
        return ImmutableList.of(this.head, this.body, this.leg0, this.leg1, this.leg2, this.leg3, this.leg4, this.leg5, this.leg6, this.leg7);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch) {
        this.head.yRot = headYaw * ((float) Math.PI / 180F);
        this.head.xRot = headPitch * ((float) Math.PI / 180F);
        float f = -((float) Math.PI / 4F);
        this.leg0.zRot = -f;
        this.leg1.zRot = f;
        this.leg2.zRot = -f;
        this.leg3.zRot = f;
        this.leg4.zRot = -f;
        this.leg5.zRot = f;
        this.leg6.zRot = -f;
        this.leg7.zRot = f;
        float f1 = -0.0F;
        float f2 = -((float) Math.PI / 8F);
        this.leg0.yRot = f;
        this.leg1.yRot = -f;
        this.leg2.yRot = f2;
        this.leg3.yRot = -f2;
        this.leg4.yRot = -f2;
        this.leg5.yRot = f2;
        this.leg6.yRot = -f;
        this.leg7.yRot = f;
        float f3 = -(MathHelper.cos(limbSwing * 0.6662F * 2.0F + 0.0F) * 0.4F) * limbSwingAmount;
        float f4 = -(MathHelper.cos(limbSwing * 0.6662F * 2.0F + (float) Math.PI) * 0.4F) * limbSwingAmount;
        float f5 = -(MathHelper.cos(limbSwing * 0.6662F * 2.0F + ((float) Math.PI / 2F)) * 0.4F) * limbSwingAmount;
        float f6 = -(MathHelper.cos(limbSwing * 0.6662F * 2.0F + ((float) Math.PI * 1.5F)) * 0.4F) * limbSwingAmount;
        float f7 = Math.abs(MathHelper.sin(limbSwing * 0.6662F + 0.0F) * 0.4F) * limbSwingAmount;
        float f8 = Math.abs(MathHelper.sin(limbSwing * 0.6662F + (float) Math.PI) * 0.4F) * limbSwingAmount;
        float f9 = Math.abs(MathHelper.sin(limbSwing * 0.6662F + ((float) Math.PI / 2F)) * 0.4F) * limbSwingAmount;
        float f10 = Math.abs(MathHelper.sin(limbSwing * 0.6662F + ((float) Math.PI * 1.5F)) * 0.4F) * limbSwingAmount;
        this.leg0.yRot += f3;
        this.leg1.yRot += -f3;
        this.leg2.yRot += f4;
        this.leg3.yRot += -f4;
        this.leg4.yRot += f5;
        this.leg5.yRot += -f5;
        this.leg6.yRot += f6;
        this.leg7.yRot += -f6;
        this.leg0.zRot += f7;
        this.leg1.zRot += -f7;
        this.leg2.zRot += f8;
        this.leg3.zRot += -f8;
        this.leg4.zRot += f9;
        this.leg5.zRot += -f9;
        this.leg6.zRot += f10;
        this.leg7.zRot += -f10;
    }

}