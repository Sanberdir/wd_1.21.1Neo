package ru.imaginaerum.wd.common.init.blocks.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import ru.imaginaerum.wd.common.init.blocks.custom.entity.ModHangingSignBlockEntity;


public class ModHangingSignBlock extends CeilingHangingSignBlock {
    public ModHangingSignBlock(Properties properties, WoodType type) {
        super(type, properties);
    }
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new ModHangingSignBlockEntity(pPos, pState);
    }

}