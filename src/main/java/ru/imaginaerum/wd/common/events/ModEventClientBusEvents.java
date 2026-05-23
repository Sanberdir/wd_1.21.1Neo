package ru.imaginaerum.wd.common.events;

import net.minecraft.client.Minecraft;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;

import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

import ru.imaginaerum.wd.WD;
import ru.imaginaerum.wd.common.init.patricles.ModParticles;
import ru.imaginaerum.wd.common.init.patricles.custom.StombleRoseParticles;

@EventBusSubscriber(modid = WD.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventClientBusEvents {

    @SubscribeEvent
    public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(
                ModParticles.STOMBLE_ROSE.get(),
                StombleRoseParticles.Provider::new
        );
    }
}