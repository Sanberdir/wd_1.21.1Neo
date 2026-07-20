package ru.imaginaerum.wd.common.init.items.armor.elytra;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.ElytraModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import ru.imaginaerum.wd.WD;
import ru.imaginaerum.wd.common.init.items.ItemsWD;

@OnlyIn(Dist.CLIENT)
public class DragoliteElytraLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    // new ResourceLocation(modid, path) -> ResourceLocation.fromNamespaceAndPath(modid, path)
    private static final ResourceLocation TEXTURE_ELYTRA =
            ResourceLocation.fromNamespaceAndPath(WD.MOD_ID, "textures/entity/nezydra.png");
    private static final ResourceLocation TEXTURE_ELYTRA_GLOW =
            ResourceLocation.fromNamespaceAndPath(WD.MOD_ID, "textures/entity/nezydra_glow.png");

    private final ElytraModel<AbstractClientPlayer> elytraModel;

    public DragoliteElytraLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> parent,
                                EntityModelSet modelSet) {
        super(parent);
        this.elytraModel = new ElytraModel<>(modelSet.bakeLayer(ModelLayers.ELYTRA));
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight,
                       AbstractClientPlayer entity, float limbSwing, float limbSwingAmount,
                       float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {

        ItemStack chestItem = entity.getItemBySlot(EquipmentSlot.CHEST);
        if (chestItem.getItem() != ItemsWD.MAG_ELYTRA.get()) return;

        // Основная элитра
        poseStack.pushPose();
        poseStack.translate(0.0F, 0.0F, 0.125F);
        this.getParentModel().copyPropertiesTo(this.elytraModel);
        this.elytraModel.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        // всегда рисуем базовую текстуру
        VertexConsumer main = buffer.getBuffer(RenderType.armorCutoutNoCull(TEXTURE_ELYTRA));
        this.elytraModel.renderToBuffer(poseStack, main, packedLight,
                OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);

        // если предмет зачарован — дополнительно рисуем поверх слой переливания
        if (chestItem.hasFoil()) {
            VertexConsumer glint = buffer.getBuffer(RenderType.armorEntityGlint());
            this.elytraModel.renderToBuffer(poseStack, glint, packedLight,
                    OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        }

        poseStack.popPose();

        // Glow-слой (не зависит от foil, оставляем как есть)
        poseStack.pushPose();
        poseStack.translate(0.0F, 0.0F, 0.130F);
        this.getParentModel().copyPropertiesTo(this.elytraModel);
        this.elytraModel.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        VertexConsumer glow = buffer.getBuffer(RenderType.entityTranslucentEmissive(TEXTURE_ELYTRA_GLOW));
        this.elytraModel.renderToBuffer(poseStack, glow, 0xF000F0,
                OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        poseStack.popPose();
    }
}