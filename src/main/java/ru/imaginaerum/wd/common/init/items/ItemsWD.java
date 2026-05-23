package ru.imaginaerum.wd.common.init.items;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import ru.imaginaerum.wd.WD;
import ru.imaginaerum.wd.common.init.blocks.BlocksWD;
import ru.imaginaerum.wd.common.init.effects.EffectsWD;
import ru.imaginaerum.wd.common.init.items.custom.*;

public class ItemsWD {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(WD.MOD_ID);

    public static final DeferredItem<Item> JAR = ITEMS.register("jar",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> COASTAL_STEEP_FLOWER = ITEMS.register("coastal_steep_flower",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> COASTAL_STEEP_FIBERS = ITEMS.register("coastal_steep_fibers",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> POISON_BERRY = ITEMS.register("poison_berry",
            () -> new ItemNameBlockItem(BlocksWD.POISON_BERRY.get(), new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationModifier(0.5f).alwaysEdible().fast()
                            .effect(new MobEffectInstance(MobEffects.HARM, 10, 0), 0.7F)
                            .effect(new MobEffectInstance(MobEffects.BLINDNESS, 60, 0), 0.5F)
                            .effect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 120, 2), 0.3F)
                            .build())));

    public static final DeferredItem<Item> CHARMING_BERRIES = ITEMS.register("charming_berries",
            () -> new ItemNameBlockItem(BlocksWD.CHARMING_BERRIES_BLOCK.get(), new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationModifier(0.5f).alwaysEdible().fast()
                            .effect(new MobEffectInstance(MobEffects.POISON, 80, 0), 0.5F)
                            .effect(new MobEffectInstance(MobEffects.HEALTH_BOOST, 200, 0), 0.5F)
                            .build())));

    public static final DeferredItem<Item> FREEZE_BERRIES = ITEMS.register("freeze_berries",
            () -> new ItemNameBlockItem(BlocksWD.FREEZE_BERRIES.get(), new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationModifier(0.5f).alwaysEdible().fast()
                            .effect(new MobEffectInstance(EffectsWD.FREEZE, 240, 0), 1F)
                            .build())));
    public static final DeferredItem<Item> FIRE_STEM = ITEMS.register("fire_stem",
            () -> new ItemNameBlockItem(BlocksWD.FIRE_STEM.get(), new Item.Properties().fireResistant()));
    public static final DeferredItem<Item> COASTAL_STEEP = ITEMS.register("coastal_steep",
            () -> new ItemNameBlockItem(BlocksWD.COASTAL_STEEP.get(), new Item.Properties()));
    public static final DeferredItem<Item> ROSE_OF_GHOSTY_TEARS = ITEMS.register("rose_of_ghosty_tears",
            () -> new ItemNameBlockItem(BlocksWD.ROSE_OF_GHOSTY_TEARS.get(), new Item.Properties()));
    public static final DeferredItem<Item> ROSE_OF_THE_MURDERER = ITEMS.register("rose_of_the_murderer",
            () -> new MurdererRose(BlocksWD.ROSE_OF_THE_MURDERER.get(), new Item.Properties()));
    public static final DeferredItem<Item> WARPED_WART = ITEMS.register("warped_wart",
            () -> new ItemNameBlockItem(BlocksWD.WARPED_WART.get(), new Item.Properties()));

    public static final DeferredItem<Item> POISON_BERRY_JAM = ITEMS.register("poison_berry_jam",
            () -> new Jam(BlocksWD.POISON_BERRY_JAM.get(), new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(13).saturationModifier(0.3f)
                            .effect(new MobEffectInstance(MobEffects.POISON, 180, 0), 1F)
                            .effect(new MobEffectInstance(MobEffects.HEALTH_BOOST, 3300, 4), 0.95F)
                            .build())));
    public static final DeferredItem<Item> FREEZE_JAM = ITEMS.register("freeze_jam",
            () -> new Jam(BlocksWD.FREEZE_JAM.get(), new Item.Properties()
                            .food(new FoodProperties.Builder().nutrition(10).saturationModifier(0.3f)
                            .effect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 0), 0.9F)
                            .effect(new MobEffectInstance(EffectsWD.FREEZE, 2200, 0), 1F)
                            .build())));
    public static final DeferredItem<Item> CHARMING_JAM = ITEMS.register("charming_jam",
            () -> new Jam(BlocksWD.CHARMING_JAM.get(), new Item.Properties()
                            .food(new FoodProperties.Builder().nutrition(10).saturationModifier(0.3f)
                            .effect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 200, 1), 0.2F)
                            .effect(new MobEffectInstance(MobEffects.DIG_SPEED, 1200, 0), 0.4F)
                            .effect(new MobEffectInstance(MobEffects.BLINDNESS, 120, 0), 0.6F)
                            .effect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 840, 2), 0.7F)
                            .build())));
    public static final DeferredItem<Item> SWEET_JAM = ITEMS.register("sweet_jam",
            () -> new Jam(BlocksWD.SWEET_JAM.get(), new Item.Properties()
                            .food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f)
                            .build())));
    public static final DeferredItem<Item> APPLE_JAM = ITEMS.register("apple_jam",
            () -> new Jam(BlocksWD.APPLE_JAM.get(), new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f)
                            .build())));
    public static final DeferredItem<Item> JAM_TONIC = ITEMS.register("jam_tonic",
            () -> new Jam(BlocksWD.JAM_TONIC.get(), new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.7f)
                            .effect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 2000, 10), 1F)
                            .effect(new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 4800, 4), 1F)
                            .effect(new MobEffectInstance(MobEffects.DIG_SPEED, 5000, 8), 1F)
                            .build())));
    public static final DeferredItem<Item> JAM_INVISIBILITY = ITEMS.register("jam_invisibility",
            () -> new Jam(BlocksWD.JAM_INVISIBILITY.get(), new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(12).saturationModifier(0.55f)
                            .effect(new MobEffectInstance(MobEffects.INVISIBILITY, 4800, 0), 1F)
                            .build())));
    public static final DeferredItem<Item> LEVITAN_JAM = ITEMS.register("levitan_jam",
            () -> new Jam(BlocksWD.LEVITAN_JAM.get(), new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(15).saturationModifier(0.4f)
                            .effect(new MobEffectInstance(MobEffects.LEVITATION, 4, 100), 1F)
                            .effect(new MobEffectInstance(MobEffects.SLOW_FALLING, 200, 10), 1F)
                            .build())));
    public static final DeferredItem<Item> GLOWING_JAM = ITEMS.register("glowing_jam",
            () -> new Jam(BlocksWD.GLOWING_JAM.get(), new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(15).saturationModifier(0.4f)
                            .effect(new MobEffectInstance(MobEffects.GLOWING, 220, 0), 1F)
                            .build())));
    public static final DeferredItem<Item> SUGAR_REFINED = ITEMS.register("sugar_refined",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.5f)
                            .build())));

    public static final DeferredItem<Item> RAW_WAFFLES = ITEMS.register("raw_waffles",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.6f).alwaysEdible()
                    .build())));
    public static final DeferredItem<Item> WAFFLES = ITEMS.register("waffles",
            () -> new ItemNameBlockItem(BlocksWD.WAFFLES.get(), new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(6).saturationModifier(1).alwaysEdible()
                            .build())));
    public static final DeferredItem<Item> BERRIES_WAFFLES = ITEMS.register("berries_waffles",
            () -> new ItemNameBlockItem(BlocksWD.BERRIES_WAFFLES.get(), new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(6).saturationModifier(1).alwaysEdible().build())));
    public static final DeferredItem<Item> APPLE_WAFFLES = ITEMS.register("apple_waffles",
            () -> new ItemNameBlockItem(BlocksWD.APPLE_WAFFLES.get(), new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(6).saturationModifier(1).alwaysEdible().build())));
    public static final DeferredItem<Item> ICE_WAFFLES = ITEMS.register("ice_waffles",
            () -> new ItemNameBlockItem(BlocksWD.ICE_WAFFLES.get(), new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(6).saturationModifier(1).alwaysEdible().build())));
    public static final DeferredItem<Item> POISON_WAFFLES = ITEMS.register("poison_waffles",
            () -> new ItemNameBlockItem(BlocksWD.POISON_WAFFLES.get(), new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(6).saturationModifier(1).alwaysEdible().build())));
    public static final DeferredItem<Item> CHARMING_WAFFLES = ITEMS.register("charming_waffles",
            () -> new ItemNameBlockItem(BlocksWD.CHARMING_WAFFLES.get(), new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(6).saturationModifier(1).alwaysEdible().build())));
    public static final DeferredItem<Item> GLOW_BERRIES_WAFFLES = ITEMS.register("glow_berries_waffles",
            () -> new ItemNameBlockItem(BlocksWD.GLOW_BERRIES_WAFFLES.get(), new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(6).saturationModifier(1).alwaysEdible().build())));
    public static final DeferredItem<Item> SPATIAL_ORCHID = ITEMS.register("spatial_orchid",
            () -> new ItemNameBlockItem(BlocksWD.SPATIAL_ORCHID.get(), new Item.Properties()));
    public static final DeferredItem<Item> THE_PILLAGERS_CHEST = ITEMS.register("the_pillagers_chest",
            () -> new ItemNameBlockItem(BlocksWD.THE_PILLAGERS_CHEST.get(), new Item.Properties()));
    public static final DeferredItem<Item> GOLDEN_CHEST_KING_PILLAGER = ITEMS.register("golden_chest_king_pillager",
            () -> new ItemNameBlockItem(BlocksWD.GOLDEN_CHEST_KING_PILLAGER.get(), new Item.Properties()));
    public static final DeferredItem<Item> THE_PILLAGERS_KEY = ITEMS.register("the_pillagers_key",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> THE_KING_PILLAGERS_KEY = ITEMS.register("the_king_pillagers_key",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> A_DROP_OF_LOVE = ITEMS.register("a_drop_of_love",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SPARKLING_POLLEN = ITEMS.register("sparkling_pollen",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CRIMSON_BONE_MEAL = ITEMS.register("crimson_bone_meal",
            () -> new NetherrackBonemeal(new Item.Properties(), Blocks.CRIMSON_NYLIUM));
    public static final DeferredItem<Item> WARPED_BONE_MEAL = ITEMS.register("warped_bone_meal",
            () -> new NetherrackBonemeal(new Item.Properties(), Blocks.WARPED_NYLIUM));
    public static final DeferredItem<Item> GRASS_BONE_MEAL = ITEMS.register("grass_bone_meal",
            () -> new GrassBoneMeal(new Item.Properties()));
    public static final DeferredItem<Item> MYCELIUM_BONE_MEAL = ITEMS.register("mycelium_bone_meal",
            () -> new DirtBoneMeal(new Item.Properties(), Blocks.MYCELIUM));

    public static final DeferredItem<Item> DRAGOLIT_INGOT = ITEMS.register("dragolit_ingot",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CLEAR_DRAGOLIT_NUGGET = ITEMS.register("clear_dragolit_nugget",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> STRANGE_SCRAP = ITEMS.register("strange_scrap",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> HEALING_DEW = ITEMS.register("healing_dew",
            () -> new HealingDew(new Item.Properties()));
    public static final DeferredItem<Item> NETHER_GROG = ITEMS.register("nether_grog",
            () -> new NetherGrog(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).alwaysEdible().saturationModifier(1)
                    .effect(new MobEffectInstance(MobEffects.DIG_SPEED, 6000, 2), 1F)
                    .build()).rarity(Rarity.RARE)));
}
