package ru.imaginaerum.wd.common.init.blocks.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.FallingBlock;

public class WDFallingBlock extends FallingBlock {
    public static final MapCodec<WDFallingBlock> CODEC =
            simpleCodec(WDFallingBlock::new);

    public WDFallingBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends FallingBlock> codec() {
        return CODEC;
    }
}
