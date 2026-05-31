package ru.imaginaerum.wd.common.events;

import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.client.renderer.blockentity.SignRenderer;
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
import ru.imaginaerum.wd.common.init.blocks.custom.entity.DragoliteCageBlockEntity;
import ru.imaginaerum.wd.common.init.blocks.custom.entity.ModBlockEntities;
import ru.imaginaerum.wd.common.init.blocks.custom.entity.renderer.DragoliteCageEntityRenderer;
import ru.imaginaerum.wd.common.init.blocks.custom.entity.renderer.GlowingJamBlockRenderer;
import ru.imaginaerum.wd.common.init.items.ItemsWD;
import ru.imaginaerum.wd.common.init.items.custom.SoulStone;
import ru.imaginaerum.wd.common.init.items.entity.client.ModModelLayersItem;
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
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayersItem.APPLE_BOAT_LAYER, BoatModel::createBodyModel);
        event.registerLayerDefinition(ModModelLayersItem.APPLE_CHEST_BOAT_LAYER, ChestBoatModel::createBodyModel);
    }
    @SubscribeEvent
    public static void registerBER(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.GLOWING_JAM.get(), GlowingJamBlockRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.DRAGOLITE_CAGE_ENTITY.get(), DragoliteCageEntityRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.APPLE_SIGN.get(), SignRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.APPLE_HANGING_SIGN.get(), HangingSignRenderer::new);
    }
}