package ru.imaginaerum.wd.common.init.items.armor.model_layered;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import ru.imaginaerum.wd.WD;

public class WDModelLayers {


    public static final ModelLayerLocation MAGIC_ARMOR = createLocation("magic_hat", "main");

    public static void register(EntityRenderersEvent.RegisterLayerDefinitions event) {

        event.registerLayerDefinition(MAGIC_ARMOR, () -> MagicArmorModel.createArmorLayer(new CubeDeformation(0.5F)));
    }

    private static ModelLayerLocation createLocation(String model, String layer) {
        return new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(WD.MOD_ID, model), layer);
    }


}
