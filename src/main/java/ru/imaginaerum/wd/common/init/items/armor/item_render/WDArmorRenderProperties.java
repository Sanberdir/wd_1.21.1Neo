package ru.imaginaerum.wd.common.init.items.armor.item_render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexMultiConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import ru.imaginaerum.wd.WD;
import ru.imaginaerum.wd.client.WDRenderTypes;
import ru.imaginaerum.wd.common.init.items.armor.ModArmorMaterials;
import ru.imaginaerum.wd.common.init.items.armor.model_layered.MagicArmorModel;
import ru.imaginaerum.wd.common.init.items.armor.model_layered.WDModelLayers;
import ru.imaginaerum.wd.common.init.items.custom.MagicHat;
import ru.imaginaerum.wd.common.init.items.custom.MagicHatJam;

public class WDArmorRenderProperties implements IClientItemExtensions {

    private static final ResourceLocation MAGIC_ARMOR_GLOW = ResourceLocation.fromNamespaceAndPath(
            WD.MOD_ID, "textures/armor/magic_glow/magic_armor_glow.png");
    private static boolean init;
    public static MagicArmorModel MAGIC_ARMOR_MODEL;

    public static void initializeModels() {
        init = true;
        MAGIC_ARMOR_MODEL = new MagicArmorModel(
                Minecraft.getInstance().getEntityModels().bakeLayer(WDModelLayers.MAGIC_ARMOR));
    }

    @Override
    public HumanoidModel<?> getHumanoidArmorModel(LivingEntity entityLiving, ItemStack itemStack,
                                                  EquipmentSlot armorSlot, HumanoidModel<?> _default) {
        if (!init) initializeModels();

        if (itemStack.getItem() instanceof MagicHat) return MAGIC_ARMOR_MODEL;
        if (itemStack.getItem() instanceof MagicHatJam) return MAGIC_ARMOR_MODEL;

        return _default;
    }

    private static boolean isMaterial(ArmorItem armorItem, net.minecraft.core.Holder<?> material) {
        return armorItem.getMaterial().unwrapKey()
                .equals(material.unwrapKey());
    }

    public static void renderCustomArmor(PoseStack poseStack, MultiBufferSource multiBufferSource,
                                          int light, ItemStack itemStack, ArmorItem armorItem,
                                          Model armorModel, boolean legs, ResourceLocation texture) {
        if (isMaterial(armorItem, ModArmorMaterials.MAGIC)) {
            VertexConsumer vc = itemStack.hasFoil()
                    ? VertexMultiConsumer.create(
                    multiBufferSource.getBuffer(RenderType.entityGlintDirect()),
                    multiBufferSource.getBuffer(RenderType.armorCutoutNoCull(texture)))
                    : multiBufferSource.getBuffer(RenderType.armorCutoutNoCull(texture));
            armorModel.renderToBuffer(poseStack, vc, light, OverlayTexture.NO_OVERLAY);
        }
// WDArmorRenderProperties.java
        if (isMaterial(armorItem, ModArmorMaterials.MAGIC_JAM)) {
            // как в 1.20.1 — entityTranslucent вместо armorCutoutNoCull
            VertexConsumer vc = itemStack.hasFoil()
                    ? VertexMultiConsumer.create(
                    multiBufferSource.getBuffer(RenderType.entityGlintDirect()),
                    multiBufferSource.getBuffer(RenderType.entityTranslucent(texture)))
                    : multiBufferSource.getBuffer(RenderType.entityTranslucent(texture));
            armorModel.renderToBuffer(poseStack, vc, light, OverlayTexture.NO_OVERLAY);

            VertexConsumer glowVc = multiBufferSource.getBuffer(
                    WDRenderTypes.getEyesAlphaEnabled(MAGIC_ARMOR_GLOW));
            armorModel.renderToBuffer(poseStack, glowVc, 15728880, OverlayTexture.NO_OVERLAY);
        }
    }
}