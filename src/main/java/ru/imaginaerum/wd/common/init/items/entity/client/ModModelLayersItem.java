package ru.imaginaerum.wd.common.init.items.entity.client;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import ru.imaginaerum.wd.WD;

public class ModModelLayersItem {

    public static final ModelLayerLocation APPLE_BOAT_LAYER = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath(WD.MOD_ID, "boat/apple"), "main");
    public static final ModelLayerLocation APPLE_CHEST_BOAT_LAYER = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath(WD.MOD_ID, "chest_boat/apple"), "main");

}