package ru.imaginaerum.wd.common.init.items.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class NetherGrog extends Item {

    public NetherGrog(Properties properties) {
        super(properties);
    }

    @Override
    public SoundEvent getEatingSound() {
        return SoundEvents.GENERIC_DRINK;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        ItemStack result = super.finishUsingItem(stack, level, entity);
        entity.igniteForSeconds(3.0F);
        return result;
    }

    // ----------------------------------------------------------------
    // 1.21: второй параметр — Item.TooltipContext вместо @Nullable Level
    // ----------------------------------------------------------------
    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context,
                                List<Component> components, TooltipFlag flag) {
        if (Screen.hasShiftDown()) {
            components.add(Component.translatable("wd.press_shift2")
                    .withStyle(ChatFormatting.DARK_GRAY));
            components.add(Component.translatable("wd.nether_grog")
                    .withStyle(ChatFormatting.DARK_PURPLE));
        } else {
            components.add(Component.translatable("wd.press_shift")
                    .withStyle(ChatFormatting.DARK_GRAY));
        }
        super.appendHoverText(stack, context, components, flag);
    }
}