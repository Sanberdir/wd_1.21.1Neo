package ru.imaginaerum.wd.common.init.items.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HealingDew extends Item {

    public HealingDew(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(
            ItemStack stack,
            TooltipContext context,
            List<Component> components,
            TooltipFlag flag
    ) {

        if (Screen.hasShiftDown()) {
            components.add(Component.translatable("wd.press_shift2")
                    .withStyle(ChatFormatting.DARK_GRAY));

            components.add(Component.translatable("wd.healing_dew")
                    .withStyle(ChatFormatting.DARK_PURPLE));
        } else {
            components.add(Component.translatable("wd.press_shift")
                    .withStyle(ChatFormatting.DARK_GRAY));
        }

        super.appendHoverText(stack, context, components, flag);
    }

    @Override
    public InteractionResult interactLivingEntity(
            ItemStack stack,
            Player player,
            LivingEntity target,
            InteractionHand hand
    ) {

        Level level = target.level();

        if (!(target instanceof ZombieVillager zombieVillager)) {
            return super.interactLivingEntity(stack, player, target, hand);
        }

        if (!level.isClientSide) {

            boolean success = level.random.nextFloat() < 0.7F;
            boolean isBaby = zombieVillager.isBaby();

            double x = zombieVillager.getX();
            double y = zombieVillager.getY();
            double z = zombieVillager.getZ();

            float yRot = zombieVillager.getYRot();
            float xRot = zombieVillager.getXRot();

            zombieVillager.discard();

            if (success) {

                Villager villager = new Villager(EntityType.VILLAGER, (ServerLevel) level);

                villager.moveTo(x, y, z, yRot, xRot);

                if (isBaby) {
                    villager.setBaby(true);
                }

                level.addFreshEntity(villager);

                ((ServerLevel) level).sendParticles(
                        ParticleTypes.HAPPY_VILLAGER,
                        x,
                        y + 1,
                        z,
                        10,
                        0.5,
                        0.5,
                        0.5,
                        0.1
                );

                level.playSound(
                        null,
                        zombieVillager.blockPosition(),
                        SoundEvents.BELL_BLOCK,
                        SoundSource.PLAYERS,
                        1F,
                        1F
                );

            } else {

                Zombie zombie = new Zombie(EntityType.ZOMBIE, (ServerLevel) level);

                zombie.moveTo(x, y, z, yRot, xRot);

                if (isBaby) {
                    zombie.setBaby(true);
                }

                level.addFreshEntity(zombie);

                ((ServerLevel) level).sendParticles(
                        ParticleTypes.SMOKE,
                        x,
                        y + 1,
                        z,
                        15,
                        0.5,
                        0.5,
                        0.5,
                        0.1
                );

                level.playSound(
                        null,
                        zombieVillager.blockPosition(),
                        SoundEvents.FIRE_EXTINGUISH,
                        SoundSource.PLAYERS,
                        1F,
                        1F
                );
            }

            if (!player.isCreative()) {
                stack.shrink(1);
            }
        }

        player.swing(hand);

        return InteractionResult.SUCCESS;
    }
}