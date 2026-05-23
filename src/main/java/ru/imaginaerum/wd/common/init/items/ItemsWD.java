package ru.imaginaerum.wd.common.init.items;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import ru.imaginaerum.wd.WD;
import ru.imaginaerum.wd.common.init.blocks.BlocksWD;
import ru.imaginaerum.wd.common.init.effects.EffectsWD;
import ru.imaginaerum.wd.common.init.items.custom.Jam;
import ru.imaginaerum.wd.common.init.items.custom.MurdererRose;

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
    public static final DeferredItem<Item> SUGAR_REFINED = ITEMS.register("sugar_refined",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.5f)
                    .build())));
}
