package ru.imaginaerum.wd.common.events;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;

import net.neoforged.fml.common.EventBusSubscriber;

import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

import ru.imaginaerum.wd.WD;
import ru.imaginaerum.wd.common.init.blocks.custom.entity.ModBlockEntities;
import ru.imaginaerum.wd.common.init.blocks.custom.entity.renderer.GlowingJamBlockRenderer;
import ru.imaginaerum.wd.common.init.items.ItemsWD;
import ru.imaginaerum.wd.common.init.items.custom.SoulStone;
import ru.imaginaerum.wd.common.init.patricles.ModParticles;
import ru.imaginaerum.wd.common.init.patricles.custom.*;

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
        event.registerSpriteSet(
                ModParticles.ROBIN_STAR_PARTICLES_PROJECTILE.get(),
                RobinStarsParticlesProjectile.Provider::new
        );
        event.registerSpriteSet(
                ModParticles.ROBIN_STAR_PARTICLES.get(),
                RobinStarsParticles.Provider::new
        );
        event.registerSpriteSet(
                ModParticles.GOLDEN_FLOWER_PARTICLES.get(),
                GoldenFlowerParticle.Provider::new
        );
    }
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {

        event.enqueueWork(() -> {

            ItemProperties.register(
                    ItemsWD.SOUL_STONE.get(),
                    ResourceLocation.fromNamespaceAndPath(WD.MOD_ID, "charged"),
                    (stack, world, entity, seed) ->
                            SoulStone.isCharged(stack) ? 1.0F : 0.0F
            );

        });
    }
    @SubscribeEvent
    public static void registerBER(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.GLOWING_JAM.get(), GlowingJamBlockRenderer::new);
    }
}