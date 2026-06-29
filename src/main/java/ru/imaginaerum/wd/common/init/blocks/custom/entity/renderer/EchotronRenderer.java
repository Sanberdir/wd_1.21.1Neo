package ru.imaginaerum.wd.common.init.blocks.custom.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.Items;
import org.joml.Matrix4f;
import ru.imaginaerum.wd.common.init.blocks.custom.EchotronBlock;
import ru.imaginaerum.wd.common.init.blocks.custom.entity.EchotronBlockEntity;
import ru.imaginaerum.wd.common.init.blocks.custom.entity.model.EchotronModel;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class EchotronRenderer extends GeoBlockRenderer<EchotronBlockEntity> {

    public EchotronRenderer(BlockEntityRendererProvider.Context context) {
        super(new EchotronModel());
    }

    @Override
    public void actuallyRender(PoseStack poseStack,
                               EchotronBlockEntity animatable,
                               BakedGeoModel model,
                               RenderType renderType,
                               MultiBufferSource bufferSource,
                               VertexConsumer buffer,
                               boolean isReRender,
                               float partialTick,
                               int packedLight,
                               int packedOverlay,
                               int colour) {

        super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer,
                isReRender, partialTick, packedLight, packedOverlay, colour);

        // === Осколок (Echo Shard) ===
        poseStack.pushPose();
        poseStack.translate(0, 1.6, -0.01);
        poseStack.scale(0.75f, 0.75f, 0.75f);

        if (animatable.getLevel() != null) {
            float time = animatable.getLevel().getGameTime() + partialTick;
            int stage = animatable.getBlockState().getValue(EchotronBlock.STAGE);
            float spinZ = (time * stage * 3) % 360;

            poseStack.mulPose(Axis.ZN.rotationDegrees(spinZ));
            poseStack.mulPose(Axis.YP.rotationDegrees(90));
            poseStack.mulPose(Axis.ZP.rotationDegrees(45));
            poseStack.translate(0, -0.1, 0);

            Minecraft.getInstance().getItemRenderer().renderStatic(
                    Items.ECHO_SHARD.getDefaultInstance(),
                    ItemDisplayContext.GROUND,
                    packedLight,
                    packedOverlay,
                    poseStack,
                    bufferSource,
                    animatable.getLevel(),
                    0
            );
        }
        poseStack.popPose();

        // === Лучи (молнии) ===
        if (animatable.getLevel() != null) {
            float time = animatable.getLevel().getGameTime() + partialTick;
            int stage = animatable.getBlockState().getValue(EchotronBlock.STAGE);

            float raysF = Math.max(1f, stage * 3f / 2f);
            int maxRays = (int) Math.ceil(raysF);
            float partialRay = raysF - (maxRays - 1);

            float sphereBase = 0.45f;
            float sphereJitter = 0.25f;
            float pulsationSpeed = 15.0f;

            VertexConsumer vc = bufferSource.getBuffer(RenderType.lightning());

            poseStack.pushPose();
            poseStack.translate(0, 1.6f, 0);
            Matrix4f matrix = poseStack.last().pose();

            long baseSeed = animatable.getBlockPos().asLong();

            for (int i = 0; i < maxRays; i++) {
                java.util.Random rnd = new java.util.Random(baseSeed ^ (i * 0x9E3779B97F4A7C15L));

                double u = rnd.nextDouble();
                double v = rnd.nextDouble();
                double theta = 2.0 * Math.PI * v + (time * 0.02);
                double z = 2.0 * u - 1.0;
                double r = Math.sqrt(Math.max(0.0, 1.0 - z * z));

                float dirX = (float) (r * Math.cos(theta));
                float dirZ = (float) (r * Math.sin(theta));
                float dirY = (float) z;

                float length = sphereBase + sphereJitter * rnd.nextFloat() + 0.08f * (float) Math.sin(time / pulsationSpeed + i);
                float lengthInner = length * (0.45f + 0.2f * rnd.nextFloat());
                float alphaMultiplier = (i == maxRays - 1) ? partialRay : 1f;

                float x = dirX * length;
                float y = dirY * length;
                float z2 = dirZ * length;
                float xInner = dirX * lengthInner;
                float yInner = dirY * lengthInner;
                float zInner = dirZ * lengthInner;

                int a1 = Math.min(255, (int) (180 * alphaMultiplier));
                int a2 = Math.min(255, (int) (90 * alphaMultiplier));
                int a3 = Math.min(255, (int) (35 * alphaMultiplier));

                vc.addVertex(matrix, 0f, 0f, 0f).setColor(0, 255, 255, a1);
                vc.addVertex(matrix, xInner, yInner, zInner).setColor(0, 200, 255, a2);
                vc.addVertex(matrix, x, y, z2).setColor(0, 200, 255, a3);

                if (i % 3 == 0) {
                    float branchLen = length * (0.6f + 0.2f * rnd.nextFloat());
                    double branchTheta = theta + (rnd.nextDouble() - 0.5) * 0.6;
                    double branchZ = Math.max(-1.0, Math.min(1.0, z + (rnd.nextDouble() - 0.5) * 0.2));
                    double branchR = Math.sqrt(Math.max(0.0, 1.0 - branchZ * branchZ));
                    float bdx = (float) (branchR * Math.cos(branchTheta));
                    float bdz = (float) (branchR * Math.sin(branchTheta));
                    float bdy = (float) branchZ;

                    float bx = bdx * branchLen;
                    float by = bdy * branchLen;
                    float bz = bdz * branchLen;

                    int ab1 = Math.min(255, (int) (120 * alphaMultiplier));
                    int ab2 = Math.min(255, (int) (50 * alphaMultiplier));
                    int ab3 = Math.min(255, (int) (15 * alphaMultiplier));

                    vc.addVertex(matrix, 0f, 0f, 0f).setColor(0, 255, 255, ab1);
                    vc.addVertex(matrix, bx * 0.55f, by * 0.55f, bz * 0.55f).setColor(0, 200, 255, ab2);
                    vc.addVertex(matrix, bx, by, bz).setColor(0, 200, 255, ab3);
                }
            }

            poseStack.popPose();
        }
    }
}