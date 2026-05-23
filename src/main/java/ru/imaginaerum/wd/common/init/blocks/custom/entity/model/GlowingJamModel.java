package ru.imaginaerum.wd.common.init.blocks.custom.entity.model;

import net.minecraft.resources.ResourceLocation;
import ru.imaginaerum.wd.WD;
import ru.imaginaerum.wd.common.init.blocks.custom.entity.GlowingJamBlockEntity;
import software.bernie.geckolib.model.GeoModel;

public class GlowingJamModel extends GeoModel<GlowingJamBlockEntity> {

    @Override
    public ResourceLocation getModelResource(GlowingJamBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(WD.MOD_ID, "geo/glowing_jam.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GlowingJamBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(WD.MOD_ID, "textures/block/glowing_jam.png");
    }

    @Override
    public ResourceLocation getAnimationResource(GlowingJamBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(WD.MOD_ID, "animations/glowing_jam.animation.json");
    }
}