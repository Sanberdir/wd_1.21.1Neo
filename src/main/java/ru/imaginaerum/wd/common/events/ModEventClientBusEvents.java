package ru.imaginaerum.wd.common.events;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;

import net.neoforged.fml.common.EventBusSubscriber;

import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

import ru.imaginaerum.wd.WD;
import ru.imaginaerum.wd.common.init.blocks.custom.entity.ModBlockEntities;
import ru.imaginaerum.wd.common.init.blocks.custom.entity.renderer.GlowingJamBlockRenderer;
import ru.imaginaerum.wd.common.init.patricles.ModParticles;
import ru.imaginaerum.wd.common.init.patricles.custom.FliesParticles;
import ru.imaginaerum.wd.common.init.patricles.custom.StombleRoseParticles;

@EventBusSubscriber(modid = WD.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventClientBusEvents {

    @SubscribeEvent
    public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(
                ModParticles.STOMBLE_ROSE.get(),
                StombleRoseParticles.Provider::new
        );
        event.registerSpriteSet(
                ModParticles.FLIES.get(),
                FliesParticles.Provider::new
        );
    }
    @SubscribeEvent
    public static void registerBER(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.GLOWING_JAM.get(), GlowingJamBlockRenderer::new);
    }
}