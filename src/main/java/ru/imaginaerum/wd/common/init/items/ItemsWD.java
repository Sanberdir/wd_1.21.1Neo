package ru.imaginaerum.wd.common.init.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import ru.imaginaerum.wd.WD;
import ru.imaginaerum.wd.common.init.blocks.BlocksWD;
import ru.imaginaerum.wd.common.init.items.armor.ModArmorMaterials;
import ru.imaginaerum.wd.common.init.items.custom.StarBallItem;
import ru.imaginaerum.wd.common.init.effects.EffectsWD;
import ru.imaginaerum.wd.common.init.items.custom.*;

import java.util.List;

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

    public static final DeferredItem<Item> MEADOW_GOLDEN_FLOWER = ITEMS.register("meadow_golden_flower",
            () -> new ItemNameBlockItem(BlocksWD.MEADOW_GOLDEN_FLOWER.get(), new Item.Properties()));
    public static final DeferredItem<Item> MEADOW_GOLDEN_FLOWER_INACTIVE = ITEMS.register("meadow_golden_flower_inactive",
            () -> new ItemNameBlockItem(BlocksWD.MEADOW_GOLDEN_FLOWER.get(), new Item.Properties()));

    public static final DeferredItem<Item> RAW_BEAR_MEAT = ITEMS.register("raw_bear_meat",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.6f)
                    .build())));
    public static final DeferredItem<Item> RAW_SLICING_GOATS_MEAT = ITEMS.register("raw_slicing_goats_meat",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationModifier(0.04f)
                    .build())));
    public static final DeferredItem<Item> RAW_GOATS_MEAT = ITEMS.register("raw_goats_meat",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.09f)
                    .build())));
    public static final DeferredItem<Item> GOAT_MEAT_KEBAB = ITEMS.register("goat_meat_kebab",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.2f)
                    .build())));
    public static final DeferredItem<Item> COOKED_GOAT_MEAT_KEBAB = ITEMS.register("cooked_goat_meat_kebab",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.7f)
                    .build())));
    public static final DeferredItem<Item> CAMEL_MEAT_KEBAB = ITEMS.register("camel_meat_kebab",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.2f)
                    .build())));
    public static final DeferredItem<Item> COOKED_CAMEL_MEAT_KEBAB = ITEMS.register("cooked_camel_meat_kebab",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.7f)
                    .build())));
    public static final DeferredItem<Item> RAW_HORSE = ITEMS.register("raw_horse",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.09f)
                    .build())));
    public static final DeferredItem<Item> COOKED_HORSE = ITEMS.register("cooked_horse",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationModifier(0.6f)
                    .build())));
    public static final DeferredItem<Item> RAW_CAMEL_MEAT = ITEMS.register("raw_camel_meat",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.09f)
                    .build())));
    public static final DeferredItem<Item> COOKED_CAMEL_MEAT = ITEMS.register("cooked_camel_meat",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationModifier(0.6f)
                    .build())));
    public static final DeferredItem<Item> RAW_SLICING_CAMEL_MEAT = ITEMS.register("raw_slicing_camel_meat",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.09f)
                    .build())));
    public static final DeferredItem<Item> COOKED_SLICING_CAMEL_MEAT = ITEMS.register("cooked_slicing_camel_meat",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationModifier(0.6f)
                    .build())));
    public static final DeferredItem<Item> COOKED_SLICING_GOATS_MEAT = ITEMS.register("cooked_slicing_goats_meat",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.6f)
                    .build())));
    public static final DeferredItem<Item> COOKED_BEAR_MEAT = ITEMS.register("cooked_bear_meat",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationModifier(0.6f)
                    .build())));
    public static final DeferredItem<Item> COOKED_GOATS_MEAT = ITEMS.register("cooked_goats_meat",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(8).saturationModifier(0.4f)
                    .build())));
    public static final DeferredItem<Item> GOATS_MEAT_PILAF = ITEMS.register("goats_meat_pilaf",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(8).saturationModifier(0.4f)
                    .build())));
    public static final DeferredItem<Item> WIZARD_PIE_SLICE = ITEMS.register("wizard_pie_slice",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.1f)
                    .effect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 1200, 1), 1)
                    .effect(new MobEffectInstance(MobEffects.REGENERATION, 100, 0), 1)
                    .build())));
    public static final DeferredItem<Item> ROTTEN_PIE_SLICE = ITEMS.register("rotten_pie_slice",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.1f)
                    .effect(new MobEffectInstance(MobEffects.HUNGER, 100, 0), 1)
                    .build())));
    public static final DeferredItem<Item> FROG_BODY = ITEMS.register("frog_body",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationModifier(0.5f)
                    .effect(new MobEffectInstance(MobEffects.POISON, 40, 0), 0.6F)
                    .effect(new MobEffectInstance(MobEffects.CONFUSION, 120, 0), 0.9F)
                    .build())));
    public static final DeferredItem<Item> COOKED_FROG = ITEMS.register("cooked_frog",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.6f)
                    .build())));
    public static final DeferredItem<Item> FROG_LEGS = ITEMS.register("frog_legs",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationModifier(0.1f)
                    .build())));
    public static final DeferredItem<Item> COOKED_FROG_LEGS = ITEMS.register("cooked_frog_legs",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.3f)
                    .build())));
    public static final DeferredItem<Item> GOULASH_WITH_GOAT_MEAT = ITEMS.register("goulash_with_goat_meat",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(10).saturationModifier(0.7f)
                    .build())));
    public static final DeferredItem<Item> SWEET_ROLL = ITEMS.register("sweet_roll",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(18).saturationModifier(0.8f)
                    .effect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 720, 2), 1F)
                    .build())));
    public static final DeferredItem<Item> SPAGETTI_IN_THE_NORTH = ITEMS.register("spaghetti_in_the_north",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(12).saturationModifier(0.8f)
                    .build())));
    public static final DeferredItem<Item> ROAST_GOAT_MEAT_WITH_FREEZE_BERRIES_SYRUP = ITEMS.register("roast_goat_meat_with_freeze_berries_syrup",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(12).saturationModifier(0.6f)
                    .build())));
    public static final DeferredItem<Item> BEAR_MEAT_SOUP = ITEMS.register("bear_meat_soup",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.8f)
                    .build())));
    public static final DeferredItem<Item> MEDICAL_POTATO = ITEMS.register("medical_potato",
            () -> new MedicalPotato(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.6f)
                    .build())));
    public static final DeferredItem<Item> BRIGHT_PEPPER = ITEMS.register("bright_pepper",
            () -> new MedicalPotato(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationModifier(0.2f)
                    .build()).fireResistant()));
    public static final DeferredItem<Item> KRUTNEVY_BREAD = ITEMS.register("krutnevy_bread",
            () -> new MinusEffectBeneficial(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.45f)
                    .build())));
    public static final DeferredItem<Item> TURTLE_SOUP = ITEMS.register("turtle_soup",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationModifier(0.4f)
                    .build())));
    public static final DeferredItem<Item> CLEANED_TURTLE_NECK = ITEMS.register("cleaned_turtle_neck",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.4f)
                    .build())));
    public static final DeferredItem<Item> PICKLED_TURTLE_NECK = ITEMS.register("pickled_turtle_neck",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f)
                    .effect(new MobEffectInstance(MobEffects.DIG_SPEED, 1200, 1), 0.7F)
                    .effect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 1200, 0), 0.8F)
                    .effect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 1200, 1), 1)
                    .build())));
    public static final DeferredItem<Item> TURTLE_NECK = ITEMS.register("turtle_neck",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationModifier(0.4f)
                    .build())));
    public static final DeferredItem<Item> HOT_COCOA_WITH_SPARKING_POLLEN = ITEMS.register("hot_cocoa_with_sparkling_pollen",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.2f)
                    .build())));
    public static final DeferredItem<Item> HUNTING_TWISTER = ITEMS.register("hunting_twister",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(14).saturationModifier(0.7f)
                    .build())));
    public static final DeferredItem<Item> IRIS = ITEMS.register("iris",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.2f).fast()
                    .build())));
    public static final DeferredItem<Item> MUSHROOM_ON_STICK = ITEMS.register("mushroom_on_stick",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.1f).alwaysEdible()
                    .effect(new MobEffectInstance(MobEffects.POISON, 120, 0), 0.6F)
                    .build())));
    public static final DeferredItem<Item> COOKED_MUSHROOM_ON_STICK = ITEMS.register("cooked_mushroom_on_stick",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.6f).alwaysEdible()
                    .build())));
    public static final DeferredItem<Item> COOKED_SHPIKACHKI = ITEMS.register("cooked_shpikachki",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationModifier(0.6f)
                    .build())));
    public static final DeferredItem<Item> SHPIKACHKI = ITEMS.register("shpikachki",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f)
                    .build())));
    public static final DeferredItem<Item> DUNGEON_MASTER_CHEESE = ITEMS.register("dungeon_master_cheese",
            () -> new DungeonMasterCheese(new Item.Properties().food(new FoodProperties.Builder().nutrition(20).saturationModifier(1f)
                    .effect(new MobEffectInstance(MobEffects.REGENERATION, 160, 0), 1F)
                    .build())));

    public static final DeferredItem<Item> WIZARD_PIE = ITEMS.register("wizard_pie",
            () -> new ItemNameBlockItem(BlocksWD.WIZARD_PIE.get(), new Item.Properties()));
    public static final DeferredItem<Item> ROTTEN_PIE = ITEMS.register("rotten_pie",
            () -> new ItemNameBlockItem(BlocksWD.ROTTEN_PIE.get(), new Item.Properties()));
    public static final DeferredItem<Item> SUGAR_SACK = ITEMS.register("sugar_sack",
            () -> new ItemNameBlockItem(BlocksWD.SUGAR_SACK.get(), new Item.Properties()));

    public static final DeferredItem<Item> DRAGOLIT_BLOCK = ITEMS.register("dragolit_block",
            () -> new ItemNameBlockItem(BlocksWD.DRAGOLIT_BLOCK.get(), new Item.Properties()));
    public static final DeferredItem<Item> DRAGOLIT_GRID = ITEMS.register("dragolit_grid",
            () -> new ItemNameBlockItem(BlocksWD.DRAGOLIT_GRID.get(), new Item.Properties()));
    public static final DeferredItem<Item> STRANGE_CHIP = ITEMS.register("strange_chip",
            () -> new ItemNameBlockItem(BlocksWD.STRANGE_CHIP.get(), new Item.Properties()));

    public static final DeferredItem<Item> A_BLOCK_OF_SPARKLING_POLLEN = ITEMS.register("a_block_of_sparkling_pollen",
            () -> new ItemNameBlockItem(BlocksWD.A_BLOCK_OF_SPARKLING_POLLEN.get(), new Item.Properties()));

    public static final DeferredItem<Item> POT = ITEMS.register("pot",
            () -> new ItemNameBlockItem(BlocksWD.POT.get(), new Item.Properties()));
    public static final DeferredItem<Item> POT_FROM_MEAT_GOAT = ITEMS.register("pot_from_meat_goat",
            () -> new ItemNameBlockItem(BlocksWD.POT_FROM_MEAT_GOAT.get(), new Item.Properties()));
    public static final DeferredItem<Item> POT_FROM_MEAT_CAMEL = ITEMS.register("pot_from_meat_camel",
            () -> new ItemNameBlockItem(BlocksWD.POT_FROM_MEAT_CAMEL.get(), new Item.Properties()));
    public static final DeferredItem<Item> MARINADED_POT_FROM_MEAT_CAMEL = ITEMS.register("marinaded_pot_from_meat_camel",
            () -> new ItemNameBlockItem(BlocksWD.MARINADED_POT_FROM_MEAT_CAMEL.get(), new Item.Properties()));
    public static final DeferredItem<Item> MARINADED_POT_FROM_MEAT_GOAT = ITEMS.register("marinaded_pot_from_meat_goat",
            () -> new ItemNameBlockItem(BlocksWD.MARINADED_POT_FROM_MEAT_GOAT.get(), new Item.Properties()));

    public static final DeferredItem<Item> IRON_WATERING_CAN = ITEMS.register("iron_watering_can",
            () -> new IronWateringCan(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> MAGIC_SOIL = ITEMS.register("magic_soil",
            () -> new ItemNameBlockItem(BlocksWD.MAGIC_SOIL.get(), new Item.Properties()));
    public static final DeferredItem<Item> MAGIC_SOIL_FARMLAND = ITEMS.register("magic_soil_farmland",
            () -> new ItemNameBlockItem(BlocksWD.MAGIC_SOIL_FARMLAND.get(), new Item.Properties()));
    public static final DeferredItem<Item> MAGIC_SOIL_GRASS = ITEMS.register("magic_soil_grass",
            () -> new ItemNameBlockItem(BlocksWD.MAGIC_SOIL_GRASS.get(), new Item.Properties()));
    public static final DeferredItem<Item> BRIGHT_PEPPER_SEEDS = ITEMS.register("bright_pepper_seeds",
            () -> new ItemNameBlockItem(BlocksWD.BRIGHT_PEPPER_SEEDS.get(), new Item.Properties()));

    public static final DeferredItem<Item> SILVERAN = ITEMS.register("silveran",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> HANDFUL_YADOGA = ITEMS.register("handful_yadoga",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> HANDFUL_NETHER = ITEMS.register("handful_nether",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CLEANSING_DECOCTION = ITEMS.register("cleansing_decoction",
            () -> new CleansingDecoction(new Item.Properties().durability(12)));
    public static final DeferredItem<Item> SOUL_STONE = ITEMS.register("soul_stone",
            () -> new SoulStone(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> ROBIN_STICK = ITEMS.register("robin_stick",
            () -> new StarBallItem(new Item.Properties().durability(70)));
    public static final DeferredItem<Item> STAR_BALL = ITEMS.register("star_ball",
            () -> new StarBallItem(new Item.Properties()));
    public static final DeferredItem<Item> DRAGOLIT_RAPIER = ITEMS.register("dragolit_rapier",
            () -> new Rapier(Tiers.NETHERITE, -2,-1.6f,new Item.Properties().durability(70)));

    public static final DeferredItem<Item> DRAGOLITE_CAGE = ITEMS.register("dragolite_cage",
            () -> new DragoliteCage(BlocksWD.DRAGOLITE_CAGE.get(), new Item.Properties()));

    public static final DeferredItem<Item> FLAME_ARROW = ITEMS.register("flame_arrow",
            () -> new FlameArrowItem(new Item.Properties()));


    public static final DeferredItem<Item> MUSIC_DISK_1 = ITEMS.register("music_disk_1",
            () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.RARE).jukeboxPlayable(JukeboxSongsWD.THE_LONG_WAY_HOME)));
    public static final DeferredItem<Item> MUSIC_DISK_2 = ITEMS.register("music_disk_2",
            () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.RARE).jukeboxPlayable(JukeboxSongsWD.SO_LITTLE_BUT_SO_BIG)));
    public static final DeferredItem<Item> MUSIC_DISK_3 = ITEMS.register("music_disk_3",
            () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.RARE).jukeboxPlayable(JukeboxSongsWD.WANDERING_MINSTREL)));

    public static final DeferredItem<Item> MAGIC_HAT = ITEMS.register("magic_hat",
            () -> new MagicHat(ModArmorMaterials.MAGIC, ArmorItem.Type.HELMET, new Item.Properties()));
    public static final DeferredItem<Item> MAGIC_HAT_JAM = ITEMS.register("magic_hat_jam",
            () -> new MagicHatJam(ModArmorMaterials.MAGIC_JAM, ArmorItem.Type.HELMET, new Item.Properties()));


    public static final DeferredItem<Item> MAG_ELYTRA = ITEMS.register("mag_elytra",
            () -> new ModElytra(ModArmorMaterials.ELYTRA, ArmorItem.Type.CHESTPLATE, new Item.Properties().durability(1200).fireResistant()));
    public static final DeferredItem<Item> DRAGOLITE_UPGRADE_SMITHING_TEMPLATE = ITEMS.register("dragolite_upgrade_smithing_template",
            () -> new WDSmithingTemplateItem((
                    Component.translatable("item.wd.dragolite_upgrade_apply_to").withStyle(ChatFormatting.BLUE)), // displayName
                    Component.translatable("item.wd.dragolite_upgrade_smithing_template.base_slot").withStyle(ChatFormatting.BLUE), // baseSlotDescription
                    Component.translatable("item.wd.dragolite_upgrade_smithing_template.add_slot").withStyle(ChatFormatting.GRAY), // addSlotDescription
                    Component.translatable("item.wd.dragolite_upgrade_smithing_template.base_tooltip"), // baseSlotTooltip
                    Component.translatable("item.wd.dragolite_upgrade_smithing_template.add_tooltip"), // addSlotTooltip
                    List.of(ResourceLocation.fromNamespaceAndPath("wd", "item/empty_slot_elytra")),
                    List.of(ResourceLocation.fromNamespaceAndPath("minecraft", "item/empty_slot_ingot"))
            ));
    public static final DeferredItem<Item> APPLE_PLANKS = ITEMS.register("apple_planks",
            () -> new ItemNameBlockItem(BlocksWD.APPLE_PLANKS.get(), new Item.Properties()));
    public static final DeferredItem<Item> APPLE_CABINET = ITEMS.register("apple_cabinet",
            () -> new ItemNameBlockItem(BlocksWD.APPLE_CABINET.get(), new Item.Properties()));
    public static final DeferredItem<Item> APPLE_STAIRS = ITEMS.register("apple_stairs",
            () -> new ItemNameBlockItem(BlocksWD.APPLE_STAIRS.get(), new Item.Properties()));
    public static final DeferredItem<Item> APPLE_SLAB = ITEMS.register("apple_slab",
            () -> new ItemNameBlockItem(BlocksWD.APPLE_SLAB.get(), new Item.Properties()));
    public static final DeferredItem<Item> APPLE_FENCE = ITEMS.register("apple_fence",
            () -> new ItemNameBlockItem(BlocksWD.APPLE_FENCE.get(), new Item.Properties()));
    public static final DeferredItem<Item> APPLE_FENCE_GATE = ITEMS.register("apple_fence_gate",
            () -> new ItemNameBlockItem(BlocksWD.APPLE_FENCE_GATE.get(), new Item.Properties()));
    public static final DeferredItem<Item> APPLE_BUTTON = ITEMS.register("apple_button",
            () -> new ItemNameBlockItem(BlocksWD.APPLE_BUTTON.get(), new Item.Properties()));
    public static final DeferredItem<Item> APPLE_PRESSURE_PLATE = ITEMS.register("apple_pressure_plate",
            () -> new ItemNameBlockItem(BlocksWD.APPLE_PRESSURE_PLATE.get(), new Item.Properties()));
    public static final DeferredItem<Item> APPLE_DOOR = ITEMS.register("apple_door",
            () -> new ItemNameBlockItem(BlocksWD.APPLE_DOOR.get(), new Item.Properties()));
    public static final DeferredItem<Item> APPLE_TRAPDOOR = ITEMS.register("apple_trapdoor",
            () -> new ItemNameBlockItem(BlocksWD.APPLE_TRAPDOOR.get(), new Item.Properties()));
}
