package ru.imaginaerum.wd.common.init.items.custom;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class MinusEffectBeneficial extends Item {

    public MinusEffectBeneficial(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {

        if (entity instanceof Player player) {

            for (MobEffectInstance effectInstance : player.getActiveEffects()) {

                // В 1.21 проверяем через value()
                if (!effectInstance.getEffect().value().isBeneficial()) {

                    // removeEffect принимает Holder<MobEffect>
                    player.removeEffect(effectInstance.getEffect());
                    break;
                }
            }
        }

        return super.finishUsingItem(stack, level, entity);
    }
}