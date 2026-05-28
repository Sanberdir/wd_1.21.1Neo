package ru.imaginaerum.wd.common.init.items.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import ru.imaginaerum.wd.server.events.StrikeRobinStick;

import javax.annotation.Nullable;
import java.util.List;

public class StarBallItem extends Item {

    public StarBallItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack,
                                @Nullable TooltipContext context,
                                List<Component> components,
                                TooltipFlag flag) {

        if (Screen.hasShiftDown()) {
            components.add(Component.translatable("wd.press_shift2")
                    .withStyle(ChatFormatting.DARK_GRAY));

            components.add(Component.translatable("wd.robin_stick")
                    .withStyle(ChatFormatting.DARK_PURPLE));
        } else {
            components.add(Component.translatable("wd.press_shift")
                    .withStyle(ChatFormatting.DARK_GRAY));
        }

        super.appendHoverText(stack, context, components, flag);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {

        Block block = context.getLevel().getBlockState(context.getClickedPos()).getBlock();

        // сюда можно вернуть запреты под блоки при необходимости

        return super.useOn(context);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level,
                                                  Player player,
                                                  InteractionHand hand) {

        ItemStack stack = player.getItemInHand(hand);

        if (stack.getDamageValue() > 68) {
            return InteractionResultHolder.fail(stack);
        }

        // ВАЖНО: логика только на сервере
        if (!level.isClientSide()) {
            StrikeRobinStick.execute(
                    player,
                    level,
                    player.getX(),
                    player.getY(),
                    player.getZ()
            );
        }

        player.getCooldowns().addCooldown(this, 40);

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}