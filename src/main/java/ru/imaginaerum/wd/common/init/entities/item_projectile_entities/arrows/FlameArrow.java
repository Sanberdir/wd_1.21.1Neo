package ru.imaginaerum.wd.common.init.entities.item_projectile_entities.arrows;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

import ru.imaginaerum.wd.common.init.entities.ModEntities;
import ru.imaginaerum.wd.common.init.items.ItemsWD;

public class FlameArrow extends AbstractArrow {

    public FlameArrow(EntityType<? extends FlameArrow> type, Level level) {
        super(type, level);
    }

    public FlameArrow(Level level, LivingEntity shooter) {
        super(
                ModEntities.FLAME_ARROW.get(),
                shooter,
                level,
                new ItemStack(ItemsWD.FLAME_ARROW.get()),
                null
        );
    }

    public FlameArrow(Level level, double x, double y, double z) {
        super(
                ModEntities.FLAME_ARROW.get(),
                x,
                y,
                z,
                level,
                new ItemStack(ItemsWD.FLAME_ARROW.get()),
                null
        );
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(ItemsWD.FLAME_ARROW.get());
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide && !this.inGround) {
            this.level().addParticle(
                    ParticleTypes.LAVA,
                    this.getX(),
                    this.getY(),
                    this.getZ(),
                    0.0D,
                    0.0D,
                    0.0D
            );
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);

        if (this.level().isClientSide) return;

        BlockPos pos = result.getBlockPos();
        BlockState state = this.level().getBlockState(pos);

        // Костёр
        if (state.is(Blocks.CAMPFIRE) && !state.getValue(CampfireBlock.LIT)) {
            this.level().setBlock(pos, state.setValue(CampfireBlock.LIT, true), 11);
            return;
        }
        if (state.is(Blocks.SOUL_CAMPFIRE) && !state.getValue(CampfireBlock.LIT)) {
            this.level().setBlock(pos, state.setValue(CampfireBlock.LIT, true), 11);
            return;
        }

        // Свечи
        if (state.getBlock() instanceof CandleBlock) {
            if (!state.getValue(CandleBlock.LIT)) {
                this.level().setBlock(pos, state.setValue(CandleBlock.LIT, true), 11);
            }
            return;
        }

        // Огонь — используем BaseFireBlock.getState() как в старом коде!
        BlockPos front = pos.relative(result.getDirection());

        if (this.level().isEmptyBlock(front)) {
            BlockState fire = BaseFireBlock.getState(this.level(), front); // ← вот исправление
            if (fire.canSurvive(this.level(), front)) {
                this.level().setBlock(front, fire, 11);
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);

        Entity entity = result.getEntity();

        if (entity instanceof LivingEntity livingEntity) {
            livingEntity.setRemainingFireTicks(200);
        }
    }
}