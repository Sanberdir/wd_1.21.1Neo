package ru.imaginaerum.wd.common.init.blocks.custom.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.util.GeckoLibUtil;

public class GlowingJamBlockEntity extends BlockEntity implements GeoAnimatable {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public GlowingJamBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.GLOWING_JAM.get(), pos, state);
    }

    @Override
    public void registerControllers(software.bernie.geckolib.animation.AnimatableManager.ControllerRegistrar controllers) {
        // пока пусто
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    // ✅ НОВЫЙ ВАРИАНТ getTick
    @Override
    public double getTick(Object animatable) {
        return net.minecraft.client.Minecraft.getInstance().level != null
                ? net.minecraft.client.Minecraft.getInstance().level.getGameTime()
                : 0;
    }
}