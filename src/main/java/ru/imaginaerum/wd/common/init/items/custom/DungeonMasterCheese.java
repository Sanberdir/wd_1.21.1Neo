package ru.imaginaerum.wd.common.init.items.custom;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.CustomModelData;
import net.minecraft.world.level.Level;

import ru.imaginaerum.wd.common.init.sounds.CustomSoundEvents;

public class DungeonMasterCheese extends Item {

    public static final int MAX_BITES = 2;
    public static final String CHEESE_STATE = "cheese_state";

    public DungeonMasterCheese(Properties properties) {
        super(properties.food(
                new FoodProperties.Builder()
                        .nutrition(20)
                        .saturationModifier(1.0f)
                        .alwaysEdible()
                        .build()
        ));
    }

    @Override
    public SoundEvent getDrinkingSound() {
        return CustomSoundEvents.NYAMNYAM.get();
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {

        CustomData customData = stack.getOrDefault(
                DataComponents.CUSTOM_DATA,
                CustomData.EMPTY
        );

        CompoundTag tag = customData.copyTag();

        int bites = tag.getInt(CHEESE_STATE);

        level.playSound(
                null,
                entity.blockPosition(),
                CustomSoundEvents.NYAMNYAM_END.get(),
                SoundSource.PLAYERS,
                1.0F,
                1.0F
        );

        if (entity instanceof Player player) {

            player.getFoodData().eat(20, 1.0F);

            player.addEffect(
                    new MobEffectInstance(
                            MobEffects.REGENERATION,
                            160,
                            0
                    )
            );
        }

        // ещё остались укусы
        if (bites < MAX_BITES) {

            ItemStack newStack = new ItemStack(this);

            CompoundTag newTag = new CompoundTag();
            int nextBite = bites + 1;

            // сохраняем состояние сыра
            newTag.putInt(CHEESE_STATE, nextBite);

            newStack.set(
                    DataComponents.CUSTOM_DATA,
                    CustomData.of(newTag)
            );

            // МОДЕЛЬ В 1.21 ЧЕРЕЗ COMPONENT
            newStack.set(
                    DataComponents.CUSTOM_MODEL_DATA,
                    new CustomModelData(nextBite)
            );

            stack.shrink(1);

            if (entity instanceof Player player) {

                if (!player.getInventory().add(newStack)) {
                    player.drop(newStack, false);
                }
            }

            return stack;
        }

        // последний кусок
        stack.shrink(1);

        return stack;
    }
}