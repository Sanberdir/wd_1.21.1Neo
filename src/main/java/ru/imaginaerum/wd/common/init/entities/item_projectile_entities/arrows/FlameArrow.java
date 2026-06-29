package ru.imaginaerum.wd.common.init.entities.item_projectile_entities.arrows;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.vehicle.MinecartTNT;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

import ru.imaginaerum.wd.common.init.blocks.custom.CandleWizardPie;
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
        BlockPos front = pos.relative(result.getDirection());
        BlockState state = this.level().getBlockState(pos);
        if (this.level().isEmptyBlock(front)) {
            if (this.level() instanceof net.minecraft.server.level.ServerLevel serverLevel) {
                // Пробуем зажечь портал по обеим осям
                var portalX = new net.minecraft.world.level.portal.PortalShape(
                        serverLevel, front, net.minecraft.core.Direction.Axis.X);
                if (portalX.isValid()) {
                    portalX.createPortalBlocks();
                    return;
                }
                var portalZ = new net.minecraft.world.level.portal.PortalShape(
                        serverLevel, front, net.minecraft.core.Direction.Axis.Z);
                if (portalZ.isValid()) {
                    portalZ.createPortalBlocks();
                    return;
                }
            }

            // Обычный огонь если портал не создался
            BlockState fireState = BaseFireBlock.getState(this.level(), front);
            if (fireState.canSurvive(this.level(), front)) {
                this.level().setBlock(front, fireState, 11);
            }
        }
        // Костёр
        if (state.is(Blocks.CAMPFIRE) && !state.getValue(CampfireBlock.LIT)) {
            this.level().setBlock(pos, state.setValue(CampfireBlock.LIT, true), 11);
            return;
        }
        if (state.is(Blocks.SOUL_CAMPFIRE) && !state.getValue(CampfireBlock.LIT)) {
            this.level().setBlock(pos, state.setValue(CampfireBlock.LIT, true), 11);
            return;
        }
        if (state.is(Blocks.TNT)) {
            this.discard();

            // Удаляем блок TNT
            this.level().removeBlock(pos, false);

            // Создаем зажженный TNT
            PrimedTnt primedTnt = new PrimedTnt(
                    this.level(),
                    pos.getX() + 0.5D,
                    pos.getY(),
                    pos.getZ() + 0.5D,
                    this.getOwner() instanceof LivingEntity living ? living : null
            );

            // Мгновенный взрыв
            primedTnt.setFuse(0);

            this.level().addFreshEntity(primedTnt);

            return;
        }
        if (state.getBlock() instanceof CandleCakeBlock) {
            if (!state.getValue(BlockStateProperties.LIT)) {
                this.level().setBlock(pos,
                        state.setValue(BlockStateProperties.LIT, true),
                        11
                );
            }
            return;
        }
        if (state.getBlock() instanceof CandleWizardPie) {
            if (!state.getValue(BlockStateProperties.LIT)) {
                this.level().setBlock(pos,
                        state.setValue(BlockStateProperties.LIT, true),
                        11
                );
            }
            return;
        }
        // Свечи
        if (state.getBlock() instanceof CandleBlock) {
            if (!state.getValue(CandleBlock.LIT)) {
                this.level().setBlock(pos, state.setValue(CandleBlock.LIT, true), 11);
            }
            return;
        }


        if (this.level().isEmptyBlock(front)) {
            BlockState fire = BaseFireBlock.getState(this.level(), front); // ← вот исправление
            if (fire.canSurvive(this.level(), front)) {
                this.level().setBlock(front, fire, 11);
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity target = result.getEntity();

        // 🔥 Ставим флаг ДО нанесения урона
        if (target instanceof LivingEntity livingTarget) {
            livingTarget.getPersistentData().putBoolean("FlameArrowKill", true);
        }

        super.onHitEntity(result); // урон наносится здесь

        // TNT вагонетка
        if (target instanceof MinecartTNT tntCart) {
            if (!this.level().isClientSide) {
                double x = tntCart.getX();
                double y = tntCart.getY();
                double z = tntCart.getZ();
                this.discard();
                tntCart.discard();
                this.level().explode(
                        null,  // null — урон получают все включая стрелявшего
                        x, y, z,
                        4.0F,
                        Level.ExplosionInteraction.TNT
                );
            }
            return;
        }

        // 🔥 Поджигаем после удара
        if (target instanceof LivingEntity livingTarget) {
            livingTarget.setRemainingFireTicks(200);
        }
    }
}