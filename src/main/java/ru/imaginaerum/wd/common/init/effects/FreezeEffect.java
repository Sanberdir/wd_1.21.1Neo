package ru.imaginaerum.wd.common.init.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Blocks;

public class FreezeEffect extends MobEffect {

    protected FreezeEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        var level = entity.level();
        var entityPos = entity.blockPosition();

        if (level.getBlockState(entityPos).is(Blocks.FIRE) ||
                level.getBlockState(entityPos).is(Blocks.LAVA)) {
            entity.setIsInPowderSnow(false);
            entity.clearFire();
            return true;
        }

        entity.setIsInPowderSnow(true);

        if (entity.isOnFire()) {
            entity.clearFire();
        }

        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}