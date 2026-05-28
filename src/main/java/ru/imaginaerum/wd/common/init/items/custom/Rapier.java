package ru.imaginaerum.wd.common.init.items.custom;

import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;

public class Rapier extends SwordItem {

    public Rapier(Tier tier, int attackDamage, float attackSpeed, Properties properties) {
        // В 1.21.1 урон и скорость передаются через атрибуты в Properties
        super(tier, properties.attributes(SwordItem.createAttributes(tier, attackDamage, attackSpeed)));
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        boolean result = super.hurtEnemy(stack, target, attacker);
        if (result) {
            // Используем RandomSource вместо java.util.Random
            RandomSource random = attacker.getRandom();

            if (random.nextFloat() <= 0.4f) { // 40% шанс слепоты
                target.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 1000, 2));
            }
            if (random.nextFloat() <= 0.2f) { // 20% шанс слабости
                target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 1000, 2));
            }
            if (random.nextFloat() <= 0.4f) { // 40% шанс замедления
                target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 1000, 2));
            }

            // 5% шанс мгновенного убийства (не для боссов)
            if (!(target instanceof EnderDragon) && !(target instanceof WitherBoss) && random.nextFloat() <= 0.05f) {
                if (!target.level().isClientSide()) {
                    target.kill();
                }
            }
        }
        return result;
    }
}