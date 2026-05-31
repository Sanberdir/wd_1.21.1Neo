package ru.imaginaerum.wd.common.init.entities.item_projectile_entities.arrows;

import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

import ru.imaginaerum.wd.WD;

public class FlameArrowRenderer extends ArrowRenderer<FlameArrow> {

    public static final ResourceLocation FLAME_ARROW =
            ResourceLocation.fromNamespaceAndPath(
                    WD.MOD_ID,
                    "textures/entity/projectiles/flame_arrow.png"
            );

    public FlameArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(FlameArrow arrow) {
        return FLAME_ARROW;
    }
}