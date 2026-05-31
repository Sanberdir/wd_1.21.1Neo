package ru.imaginaerum.wd.common.init.items.custom;

import net.minecraft.core.Direction;
import net.minecraft.core.Position;

import net.minecraft.core.registries.Registries;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;

import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;

import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;

import net.minecraft.world.level.Level;

import ru.imaginaerum.wd.common.init.entities.item_projectile_entities.arrows.FlameArrow;

public class FlameArrowItem extends ArrowItem implements ProjectileItem {

    public FlameArrowItem(Properties properties) {
        super(properties);
    }

    @Override
    public AbstractArrow createArrow(
            Level level,
            ItemStack stack,
            LivingEntity shooter,
            ItemStack weapon
    ) {
        return new FlameArrow(level, shooter);
    }

    @Override
    public Projectile asProjectile(
            Level level,
            Position pos,
            ItemStack stack,
            Direction direction
    ) {

        FlameArrow arrow = new FlameArrow(
                level,
                pos.x(),
                pos.y(),
                pos.z()
        );

        arrow.pickup = AbstractArrow.Pickup.ALLOWED;

        return arrow;
    }

    @Override
    public boolean isInfinite(
            ItemStack ammo,
            ItemStack bow,
            LivingEntity livingEntity
    ) {

        int infinityLevel = EnchantmentHelper.getTagEnchantmentLevel(
                livingEntity.level()
                        .registryAccess()
                        .lookupOrThrow(net.minecraft.core.registries.Registries.ENCHANTMENT)
                        .getOrThrow(Enchantments.INFINITY),
                bow
        );

        return infinityLevel > 0;
    }

    private static net.minecraft.core.Holder<net.minecraft.world.item.enchantment.Enchantment>
    levelInfinity(Player player) {

        return player.level()
                .registryAccess()
                .lookupOrThrow(Registries.ENCHANTMENT)
                .getOrThrow(Enchantments.INFINITY);
    }
}