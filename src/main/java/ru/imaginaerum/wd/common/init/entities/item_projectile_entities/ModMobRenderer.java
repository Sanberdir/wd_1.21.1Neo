package ru.imaginaerum.wd.common.init.entities.item_projectile_entities;


import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import ru.imaginaerum.wd.WD;
import ru.imaginaerum.wd.common.init.entities.ModEntities;

@EventBusSubscriber(modid = WD.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModMobRenderer {
    @SubscribeEvent
    public static void registerStarBallRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.STAR_BALL.get(), ThrownItemRenderer::new);
    }


}