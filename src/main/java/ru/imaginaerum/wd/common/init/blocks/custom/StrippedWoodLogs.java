package ru.imaginaerum.wd.common.init.blocks.custom;

import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ItemAbility;
import org.jetbrains.annotations.Nullable;
import ru.imaginaerum.wd.common.init.blocks.BlocksWD;

public class StrippedWoodLogs extends RotatedPillarBlock {
    public StrippedWoodLogs(Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable BlockState getToolModifiedState(BlockState state, UseOnContext context, ItemAbility itemAbility, boolean simulate) {
        if(context.getItemInHand().getItem() instanceof AxeItem) {
            if(state.is(BlocksWD.APPLE_LOG.get())) {
                return BlocksWD.STRIPPED_APPLE_LOG.get().defaultBlockState().setValue(AXIS, state.getValue(AXIS));
            }

            if(state.is(BlocksWD.APPLE_WOOD.get())) {
                return BlocksWD.STRIPPED_APPLE_WOOD.get().defaultBlockState().setValue(AXIS, state.getValue(AXIS));
            }
        }
        return super.getToolModifiedState(state, context, itemAbility, simulate);
    }
}
