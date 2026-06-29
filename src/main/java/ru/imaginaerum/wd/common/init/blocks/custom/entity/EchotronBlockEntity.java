package ru.imaginaerum.wd.common.init.blocks.custom.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.BlockPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import net.minecraft.world.phys.AABB;
import ru.imaginaerum.wd.common.init.blocks.BlocksWD;
import ru.imaginaerum.wd.common.init.blocks.custom.EchotronBlock;
import ru.imaginaerum.wd.common.init.blocks.custom.MagicCompost;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;

import javax.annotation.Nullable;
import java.util.List;

public class EchotronBlockEntity extends BlockEntity
        implements GameEventListener.Provider<VibrationSystem.Listener>, VibrationSystem, GeoBlockEntity {

    private Data vibrationData;
    private final Listener vibrationListener;
    private final User vibrationUser;

    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public EchotronBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ECHOTRON_ENTITY.get(), pos, state);
        this.vibrationUser = new EchotronUser(pos);
        this.vibrationData = new Data();
        this.vibrationListener = new Listener(this);
    }

    @Override
    public Data getVibrationData() {
        return this.vibrationData;
    }

    @Override
    public User getVibrationUser() {
        return this.vibrationUser;
    }

    @Override
    public Listener getListener() {
        return this.vibrationListener;
    }

    // GeoBlockEntity methods
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    // === Кастомный слушатель ===
    protected class EchotronUser implements User {
        private final BlockPos blockPos;
        private final PositionSource positionSource;

        public EchotronUser(BlockPos pos) {
            this.blockPos = pos;
            this.positionSource = new BlockPositionSource(pos);
        }

        @Override
        public int getListenerRadius() {
            return 8;
        }

        @Override
        public PositionSource getPositionSource() {
            return this.positionSource;
        }

        @Override
        public boolean canTriggerAvoidVibration() {
            return true;
        }

        @Override
        public boolean canReceiveVibration(ServerLevel level, BlockPos pos, Holder<GameEvent> event,
                                           @Nullable GameEvent.Context context) {
            return true;
        }

        @Override
        public void onReceiveVibration(ServerLevel level, BlockPos pos, Holder<GameEvent> event,
                                       @Nullable Entity entity, @Nullable Entity source, float distance) {
            BlockState state = getBlockState();
            int stage = state.getValue(EchotronBlock.STAGE);

            if (stage < 29) {
                level.setBlock(getBlockPos(),
                        state.setValue(EchotronBlock.STAGE, stage + 1), 3);
                stage++;
            }

            if (stage == 29) {
                float damageAmount = 10.0F;

                BlockState blockState = getBlockState();
                Direction direction = blockState.getValue(EchotronBlock.FACING);

                DamageSource damageSource = level.damageSources().sonicBoom(entity);

                BlockPos blockPos = getBlockPos();
                AABB area = createForwardCone(blockPos, direction, 15.0F, 30.0F);

                List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, area);

                for (LivingEntity target : entities) {
                    if (!target.isInvulnerable()) {
                        target.hurt(damageSource, damageAmount);

                        double dX = target.getX() - blockPos.getX();
                        double dZ = target.getZ() - blockPos.getZ();
                        target.knockback(1.0F, dX, dZ);
                    }
                }

                if (level.isNight() && !level.isRaining()) {
                    for (BlockPos posInArea : BlockPos.betweenClosed(
                            (int) area.minX, (int) area.minY, (int) area.minZ,
                            (int) area.maxX, (int) area.maxY, (int) area.maxZ
                    )) {
                        BlockState checkState = level.getBlockState(posInArea);

                        if (checkState.getBlock() instanceof MagicCompost) {
                            int currentStage = MagicCompost.getStage(checkState);

                            if (currentStage >= 4) {
                                level.setBlock(posInArea, BlocksWD.MAGIC_SOIL.get().defaultBlockState(), 3);
                                level.sendParticles(ParticleTypes.HAPPY_VILLAGER,
                                        posInArea.getX() + 0.5, posInArea.getY() + 0.5, posInArea.getZ() + 0.5,
                                        10, 0.4, 0.4, 0.4, 0.05);
                            } else if (!MagicCompost.isMaxStage(checkState)) {
                                BlockState newState = MagicCompost.setStage(checkState, currentStage + 1);
                                level.setBlock(posInArea, newState, 3);
                                level.sendParticles(ParticleTypes.COMPOSTER,
                                        posInArea.getX() + 0.5, posInArea.getY() + 0.5, posInArea.getZ() + 0.5,
                                        8, 0.3, 0.3, 0.3, 0.05);
                            }
                        }
                    }
                }

                level.setBlock(getBlockPos(), state.setValue(EchotronBlock.STAGE, 0), 3);
                level.playSound(null, getBlockPos(), SoundEvents.WARDEN_SONIC_BOOM, SoundSource.HOSTILE, 3.0F, 1.0F);
                spawnDirectionalParticles(level, blockPos, direction);
                return;
            }

            float pitch = 0.5F + (stage / 29.0F);
            level.playSound(null, pos, SoundEvents.SCULK_CLICKING, SoundSource.BLOCKS, 1.0F, pitch);
        }

        private AABB createForwardCone(BlockPos blockPos, Direction direction, float length, float angleDegrees) {
            double x = blockPos.getX() + 0.5;
            double y = blockPos.getY() + 0.5;
            double z = blockPos.getZ() + 0.5;

            double forwardX = x + direction.getStepX() * length / 2;
            double forwardY = y + direction.getStepY() * length / 2;
            double forwardZ = z + direction.getStepZ() * length / 2;

            double radius = length * Math.tan(Math.toRadians(angleDegrees / 2));

            return new AABB(
                    forwardX - radius, forwardY - radius, forwardZ - radius,
                    forwardX + radius, forwardY + radius, forwardZ + radius
            );
        }

        private void spawnDirectionalParticles(ServerLevel level, BlockPos blockPos, Direction direction) {
            double startX = blockPos.getX() + 0.5;
            double startY = blockPos.getY() + 1.0;
            double startZ = blockPos.getZ() + 0.5;
            double length = 15.0;

            for (double distance = 1.0; distance <= length; distance += 0.5) {
                double particleX = startX + direction.getStepX() * distance;
                double particleY = startY + direction.getStepY() * distance;
                double particleZ = startZ + direction.getStepZ() * distance;

                double spread = 0.3;
                double offsetX = level.random.nextGaussian() * spread;
                double offsetY = level.random.nextGaussian() * spread;
                double offsetZ = level.random.nextGaussian() * spread;

                level.sendParticles(ParticleTypes.SONIC_BOOM,
                        particleX + offsetX, particleY + offsetY, particleZ + offsetZ,
                        0, 0.0, 0.0, 0.0, 0.1);
            }

            for (int i = 0; i < 10; i++) {
                double distance = length * (0.2 + 0.8 * level.random.nextDouble());
                double particleX = startX + direction.getStepX() * distance;
                double particleY = startY + direction.getStepY() * distance;
                double particleZ = startZ + direction.getStepZ() * distance;

                level.sendParticles(ParticleTypes.SONIC_BOOM,
                        particleX, particleY, particleZ, 1,
                        level.random.nextGaussian() * 0.1,
                        level.random.nextGaussian() * 0.1,
                        level.random.nextGaussian() * 0.1,
                        0.05);
            }
        }

        @Override
        public void onDataChanged() {
            EchotronBlockEntity.this.setChanged();
        }

        @Override
        public boolean requiresAdjacentChunksToBeTicking() {
            return true;
        }
    }
}