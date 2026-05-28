package ru.imaginaerum.wd.common.init.entityes.item_projectile_entities;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import ru.imaginaerum.wd.WD;
import ru.imaginaerum.wd.common.init.items.ItemsWD;
import ru.imaginaerum.wd.server.events.HitBlockStarBall;
import ru.imaginaerum.wd.server.events.HitEntityHandler;

public class StarBall extends AbstractHurtingProjectileMod implements ItemSupplier {

    // =========================================================
    // CONSTRUCTORS
    // =========================================================

    public StarBall(EntityType<? extends StarBall> type, Level level) {
        super(type, level);
    }

    // =========================================================
    // ENTITY HIT
    // =========================================================

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        super.onHitEntity(hitResult);

        if (!(this.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        Entity target = hitResult.getEntity();

        HitEntityHandler.handleHitEntity(hitResult, serverLevel);

        this.discard();
    }

    // =========================================================
    // BLOCK HIT
    // =========================================================

    @Override
    protected void onHitBlock(BlockHitResult hitResult) {
        super.onHitBlock(hitResult);

        if (!(this.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        BlockPos blockPos = hitResult.getBlockPos();

        HitBlockStarBall.hitBlock(
                serverLevel,
                blockPos,
                blockPos.getX(),
                blockPos.getY(),
                blockPos.getZ()
        );

        WD.queueServerWork(1, this::discard);
    }

    // =========================================================
    // INSIDE BLOCK
    // =========================================================

    @Override
    protected void onInsideBlock(BlockState state) {
        super.onInsideBlock(state);

        if (!(this.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        BlockPos blockPos = this.blockPosition();

        HitBlockStarBall.hitBlock(
                serverLevel,
                blockPos,
                blockPos.getX(),
                blockPos.getY(),
                blockPos.getZ()
        );
    }

    // =========================================================
    // ITEM RENDER
    // =========================================================

    @Override
    @OnlyIn(Dist.CLIENT)
    public ItemStack getItem() {
        return new ItemStack(ItemsWD.STAR_BALL.get());
    }
}