package ru.imaginaerum.wd.common.init.entityes.item_projectile_entities.arrows;

import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.core.dispenser.ProjectileDispenseBehavior;

import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;

import ru.imaginaerum.wd.common.init.items.ItemsWD;

public class DispenserRegistry {

    public static void registerBehaviors() {

        DispenseItemBehavior flameArrowBehavior =
                new ProjectileDispenseBehavior(
                        ItemsWD.FLAME_ARROW.get()
                );

        DispenserBlock.registerBehavior(
                ItemsWD.FLAME_ARROW.get(),
                flameArrowBehavior
        );
    }
}