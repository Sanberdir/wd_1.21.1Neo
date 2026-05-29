package ru.imaginaerum.wd.server.events;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import ru.imaginaerum.wd.common.init.entityes.ModEntities;
import ru.imaginaerum.wd.common.init.entityes.item_projectile_entities.StarBall;
import ru.imaginaerum.wd.common.init.items.ItemsWD;
import ru.imaginaerum.wd.common.init.sounds.CustomSoundEvents;

public class StrikeRobinStick {

    public static void execute(Entity entity, LevelAccessor world, double x, double y, double z) {

        if (!(entity instanceof LivingEntity living)) {
            return;
        }

        if (!(world instanceof Level level)) {
            return;
        }

        ItemStack main = living.getMainHandItem();
        ItemStack off = living.getOffhandItem();

        if (!isRobinStick(main) && !isRobinStick(off)) {
            return;
        }

        ItemStack stick = isRobinStick(main) ? main : off;

        if (stick.getDamageValue() >= 69) {
            return;
        }

        EquipmentSlot slot = (stick == living.getMainHandItem())
                ? EquipmentSlot.MAINHAND
                : EquipmentSlot.OFFHAND;

        stick.hurtAndBreak(1, living, slot);

        level.playSound(
                null,
                BlockPos.containing(x, y, z),
                CustomSoundEvents.ROBIN_STICK.get(),
                SoundSource.NEUTRAL,
                1.0F,
                1.0F
        );

        if (level.isClientSide()) {
            return;
        }

        StarBall projectile = new StarBall(ModEntities.STAR_BALL.get(), level);

        projectile.setOwner(living);
        projectile.setPos(living.getX(), living.getEyeY() - 0.1, living.getZ());

        projectile.shoot(
                living.getLookAngle().x,
                living.getLookAngle().y,
                living.getLookAngle().z,
                1.0F,
                0.0F
        );

        level.addFreshEntity(projectile);
    }

    private static boolean isRobinStick(ItemStack stack) {
        return stack.getItem() == ItemsWD.ROBIN_STICK.get();
    }
}