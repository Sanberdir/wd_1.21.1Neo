package ru.imaginaerum.wd.common.init.blocks.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.NetherWartBlock;
import net.minecraft.world.level.block.state.BlockState;

import ru.imaginaerum.wd.common.init.items.ItemsWD;

public class WarpedWartBlock extends NetherWartBlock {

    public WarpedWartBlock(Properties properties) {
        super(properties);
    }

    public ItemStack getCloneItemStack(LevelReader levelReader, BlockPos blockPos, BlockState blockState) {
        return new ItemStack(ItemsWD.WARPED_WART.get());
    }
}