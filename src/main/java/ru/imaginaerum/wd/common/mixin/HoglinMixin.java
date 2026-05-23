package ru.imaginaerum.wd.common.mixin;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Zoglin;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.monster.hoglin.HoglinBase;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.imaginaerum.wd.common.init.items.ItemsWD;

@Mixin(Hoglin.class)
public abstract class HoglinMixin extends Animal implements Enemy, HoglinBase {

    @Unique
    private static final EntityDataAccessor<Boolean> PREVENT_ZOMBIFICATION =
            SynchedEntityData.defineId(Hoglin.class, EntityDataSerializers.BOOLEAN);

    protected HoglinMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void onDefineSynchedData(SynchedEntityData.Builder builder, CallbackInfo ci) {
        builder.define(PREVENT_ZOMBIFICATION, false);
    }

    @Unique
    public boolean isPreventZombification() {
        return ((Hoglin) (Object) this).getEntityData().get(PREVENT_ZOMBIFICATION);
    }

    @Unique
    public void setPreventZombification(boolean preventZombification) {
        ((Hoglin) (Object) this).getEntityData().set(PREVENT_ZOMBIFICATION, preventZombification);
    }

    @Inject(method = "isImmuneToZombification()Z", at = @At("RETURN"), cancellable = true)
    protected void isImmuneToZombification(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(this.isPreventZombification());
    }

    /**
     * @reason Полная замена метода для обработки взаимодействия с Nether Grog
     * @author imaginaerum
     */
    @Overwrite
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (player.getMainHandItem().getItem() == ItemsWD.NETHER_GROG.get()
                && !this.isPreventZombification()) {

            RandomSource random = this.getRandom();

            if (random.nextFloat() < 0.7f) {
                this.setPreventZombification(true);

                if (this.level() instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(
                            ParticleTypes.HAPPY_VILLAGER,
                            this.getX(), this.getY() + 1.0, this.getZ(),
                            10, 1.0, 1.0, 1.0, 0.1
                    );
                }

                this.playSound(SoundEvents.HOGLIN_AMBIENT, 1.0f, 1.0f);

                if (!player.isCreative()) {
                    player.getMainHandItem().shrink(1);
                }

                return InteractionResult.SUCCESS;

            } else {
                this.playSound(SoundEvents.ZOGLIN_ANGRY, 1.0f, 1.0f);

                if (!this.level().isClientSide()) {
                    Zoglin zoglin = EntityType.ZOGLIN.create(this.level());
                    if (zoglin != null) {
                        zoglin.moveTo(this.getX(), this.getY(), this.getZ(),
                                this.getYRot(), this.getXRot());

                        if (this.isBaby()) {
                            zoglin.setBaby(true);
                        }

                        zoglin.setInvulnerable(this.isInvulnerable());

                        if (this.level() instanceof ServerLevel serverLevel) {
                            serverLevel.sendParticles(
                                    ParticleTypes.LARGE_SMOKE,
                                    zoglin.getX(), zoglin.getY() + 1.0, zoglin.getZ(),
                                    10, 0.5, 0.5, 0.5, 0.1
                            );
                        }

                        this.discard();
                        this.level().addFreshEntity(zoglin);
                    }
                }

                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void onAddAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        tag.putBoolean("PreventZombification", this.isPreventZombification());
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void onReadAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        if (tag.contains("PreventZombification")) {
            this.setPreventZombification(tag.getBoolean("PreventZombification"));
        }
    }

    /**
     * @reason Хоглин не должен иметь потомков через стандартный механизм
     * @author imaginaerum
     */
    @Overwrite
    @Nullable
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return null;
    }

    /**
     * @reason Возвращаем 0 тиков анимации атаки
     * @author imaginaerum
     */
    @Overwrite
    public int getAttackAnimationRemainingTicks() {
        return 0;
    }
}