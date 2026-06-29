package ru.imaginaerum.wd.common.init.blocks.custom.entity.model;

import net.minecraft.resources.ResourceLocation;
import ru.imaginaerum.wd.WD;
import ru.imaginaerum.wd.common.init.blocks.custom.entity.EchotronBlockEntity;
import software.bernie.geckolib.model.GeoModel;

public class EchotronModel extends GeoModel<EchotronBlockEntity> {
    @Override
    public ResourceLocation getModelResource(EchotronBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(WD.MOD_ID, "geo/echotron.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(EchotronBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(WD.MOD_ID, "textures/block/echotron.png");
    }

    @Override
    public ResourceLocation getAnimationResource(EchotronBlockEntity animatable) {
        return null;
    }
}