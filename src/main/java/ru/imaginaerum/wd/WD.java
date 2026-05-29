package ru.imaginaerum.wd;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.brewing.BrewingRecipeRegistry;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import org.slf4j.Logger;
import ru.imaginaerum.wd.client.ClientProxy;
import ru.imaginaerum.wd.common.init.blocks.BlocksWD;
import ru.imaginaerum.wd.common.init.blocks.custom.entity.ModBlockEntities;
import ru.imaginaerum.wd.common.init.effects.EffectsWD;
import ru.imaginaerum.wd.common.init.entityes.ModEntities;
import ru.imaginaerum.wd.common.init.entityes.item_projectile_entities.arrows.DispenserRegistry;
import ru.imaginaerum.wd.common.init.entityes.item_projectile_entities.arrows.FlameArrowRenderer;
import ru.imaginaerum.wd.common.init.items.ItemsWD;
import ru.imaginaerum.wd.common.init.items.armor.ModArmorMaterials;
import ru.imaginaerum.wd.common.init.items.armor.model_layered.WDModelLayers;
import ru.imaginaerum.wd.common.init.patricles.ModParticles;
import ru.imaginaerum.wd.common.init.patricles.custom.StombleRoseParticles;
import ru.imaginaerum.wd.common.init.recipes.ProperBrewingRecipe;
import ru.imaginaerum.wd.common.init.tab.TabsWD;
import ru.imaginaerum.wd.common.sounds.CustomSoundEvents;
import ru.imaginaerum.wd.server.CommonProxy;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(WD.MOD_ID)
public class WD {
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "wd";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    public static CommonProxy PROXY;
    public WD(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        if (FMLEnvironment.dist.isClient()) {
            PROXY = new ClientProxy();
        } else {
            PROXY = new CommonProxy();
        }
        PROXY.commonInit(modEventBus);
        NeoForge.EVENT_BUS.register(this);
        BlocksWD.BLOCKS.register(modEventBus);
        EffectsWD.MOB_EFFECTS.register(modEventBus);
        ItemsWD.ITEMS.register(modEventBus);
        CustomSoundEvents.SOUND_EVENTS.register(modEventBus);
        TabsWD.TABS_WD.register(modEventBus);
        ModParticles.PARTICLE_TYPES.register(modEventBus);
        ModArmorMaterials.ARMOR_MATERIALS.register(modEventBus);
        ModEntities.ENTITY_TYPES.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        NeoForge.EVENT_BUS.addListener(this::onRegisterBrewingRecipes);
        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
    // [ИЗМЕНЕНО] PotionUtils.setPotion → DataComponents.POTION_CONTENTS
    public static ItemStack createPotion(Holder<Potion> potion) {
        ItemStack stack = new ItemStack(Items.POTION);
        stack.set(DataComponents.POTION_CONTENTS, new PotionContents(potion));
        return stack;
    }

    public static ItemStack createSplashPotion(Holder<Potion> potion) {
        ItemStack stack = new ItemStack(Items.SPLASH_POTION);
        stack.set(DataComponents.POTION_CONTENTS, new PotionContents(potion));
        return stack;
    }

    public static ItemStack createLingeringPotion(Holder<Potion> potion) {
        ItemStack stack = new ItemStack(Items.LINGERING_POTION);
        stack.set(DataComponents.POTION_CONTENTS, new PotionContents(potion));
        return stack;
    }
    private void onRegisterBrewingRecipes(RegisterBrewingRecipesEvent event) {
        event.getBuilder().addRecipe(new ProperBrewingRecipe(
                Ingredient.of(Items.GLASS_BOTTLE),
                Ingredient.of(ItemsWD.COASTAL_STEEP_FLOWER.get()),
                createPotion(Potions.WATER)
        ));
    }
    private void commonSetup(final FMLCommonSetupEvent event) {
        DispenserRegistry.registerBehaviors();
    }
    private static final Collection<AbstractMap.SimpleEntry<Runnable, Integer>> workQueue = new ConcurrentLinkedQueue<>();

    public static void queueServerWork(int tick, Runnable action) {
        workQueue.add(new AbstractMap.SimpleEntry(action, tick));
    }

    @SubscribeEvent
    public void tick(ServerTickEvent.Post event) {

        List<AbstractMap.SimpleEntry<Runnable, Integer>> actions = new ArrayList<>();

        workQueue.forEach(work -> {
            work.setValue(work.getValue() - 1);

            if (work.getValue() <= 0) {
                actions.add(work);
            }
        });

        actions.forEach(e -> e.getKey().run());
        workQueue.removeAll(actions);
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }
    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            EntityRenderers.register(ModEntities.FLAME_ARROW.get(), FlameArrowRenderer::new);
        }
        @SubscribeEvent
        public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
            WDModelLayers.register(event);
        }
    }
}
