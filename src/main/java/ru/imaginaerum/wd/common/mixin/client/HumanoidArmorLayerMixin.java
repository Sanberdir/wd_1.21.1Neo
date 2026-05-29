package ru.imaginaerum.wd.common.mixin.client;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.ClientHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.imaginaerum.wd.common.init.items.armor.item_render.WDArmorRenderProperties;
import ru.imaginaerum.wd.server.item.CustomArmorPostRender;

import javax.annotation.Nullable;
import java.util.Map;

@Mixin(HumanoidArmorLayer.class)
public abstract class HumanoidArmorLayerMixin extends RenderLayer {

    private static final Map<String, ResourceLocation> AC_ARMOR_LOCATION_CACHE = Maps.newHashMap();
    private ItemStack lastArmorItemStackRendered = ItemStack.EMPTY;

    @Shadow
    protected abstract void setPartVisibility(HumanoidModel humanoidModel, EquipmentSlot equipmentSlot);

    public HumanoidArmorLayerMixin(RenderLayerParent renderLayerParent) {
        super(renderLayerParent);
    }

    @Inject(
            method = "renderArmorPiece(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;ILnet/minecraft/client/model/HumanoidModel;FFFFFF)V",
            at = @At(value = "HEAD"),
            remap = true,
            cancellable = true
    )
    private void ac_renderArmorPiece(PoseStack poseStack, MultiBufferSource multiBufferSource,
                                     LivingEntity livingEntity, EquipmentSlot equipmentSlot,
                                     int light, HumanoidModel humanoidModel,
                                     float limbSwing, float limbSwingAmount, float partialTick,
                                     float ageInTicks, float netHeadYaw, float headPitch,
                                     CallbackInfo ci) {
        ItemStack itemstack = livingEntity.getItemBySlot(equipmentSlot);
        if (itemstack.getItem() instanceof CustomArmorPostRender) {
            ci.cancel();
            lastArmorItemStackRendered = itemstack;
            Item item = itemstack.getItem();
            if (item instanceof ArmorItem armorItem) {
                if (armorItem.getEquipmentSlot() == equipmentSlot) {
                    boolean legs = equipmentSlot == EquipmentSlot.LEGS;
                    HumanoidModel<?> model = this.getParentModel() instanceof HumanoidModel<?> hm ? hm : humanoidModel;
                    Model armorModel = ClientHooks.getArmorModel(livingEntity, itemstack, equipmentSlot, model);
                    setPartVisibility((HumanoidModel) armorModel, equipmentSlot);
                    ResourceLocation texture = getACArmorResource(livingEntity, itemstack, equipmentSlot, null);
                    WDArmorRenderProperties.renderCustomArmor(poseStack, multiBufferSource, light,
                            lastArmorItemStackRendered, armorItem, armorModel, legs, texture);
                }
            }
        }
    }

    private ResourceLocation getACArmorResource(LivingEntity entity, ItemStack stack,
                                                EquipmentSlot slot, @Nullable String type) {
        ArmorItem item = (ArmorItem) stack.getItem();
        // В 1.21.1 getMaterial() возвращает Holder<ArmorMaterial>
        // ResourceLocation берём через ключ холдера
        ResourceLocation materialId = item.getMaterial().unwrapKey()
                .map(key -> key.location())
                .orElse(ResourceLocation.withDefaultNamespace("unknown"));

        String domain = materialId.getNamespace();
        String texture = materialId.getPath();

        String s1 = String.format(java.util.Locale.ROOT,
                "%s:textures/models/armor/%s_layer_%d%s.png",
                domain, texture,
                (slot == EquipmentSlot.LEGS ? 2 : 1),
                type == null ? "" : String.format(java.util.Locale.ROOT, "_%s", type));

        return AC_ARMOR_LOCATION_CACHE.computeIfAbsent(s1, ResourceLocation::parse);
    }
}