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
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.imaginaerum.wd.common.init.items.ItemsWD;

@Mixin(AbstractPiglin.class)
public abstract class AbstractPiglinMixin extends Monster {

    @Unique
    private static final EntityDataAccessor<Boolean> PREVENT_ZOMBIFICATION =
            SynchedEntityData.defineId(AbstractPiglin.class, EntityDataSerializers.BOOLEAN);

    protected AbstractPiglinMixin(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    // ----------------------------------------------------------------
    // 1.21: Builder-based synched data registration
    // ----------------------------------------------------------------
    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void onDefineSynchedData(SynchedEntityData.Builder builder, CallbackInfo ci) {
        builder.define(PREVENT_ZOMBIFICATION, false);
    }

    @Unique
    public boolean isPreventZombification() {
        return ((AbstractPiglin) (Object) this).getEntityData().get(PREVENT_ZOMBIFICATION);
    }

    @Unique
    public void setPreventZombification(boolean preventZombification) {
        ((AbstractPiglin) (Object) this).getEntityData().set(PREVENT_ZOMBIFICATION, preventZombification);
    }

    @Inject(method = "isImmuneToZombification()Z", at = @At("RETURN"), cancellable = true)
    protected void isImmuneToZombification(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(this.isPreventZombification());
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (player.getMainHandItem().getItem() == ItemsWD.NETHER_GROG.get()
                && !this.isPreventZombification()) {

            // 1.21: RandomSource вместо java.util.Random
            RandomSource random = this.getRandom();

            if (random.nextFloat() < 0.7f) {
                // Успех — предотвращаем зомбификацию
                this.setPreventZombification(true);

                if (this.level() instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(
                            ParticleTypes.HAPPY_VILLAGER,
                            this.getX(), this.getY() + 1.0, this.getZ(),
                            10, 0.5, 0.5, 0.5, 0.1
                    );
                }

                this.playSound(SoundEvents.PIGLIN_ADMIRING_ITEM, 1.0f, 1.0f);

                if (!player.isCreative()) {
                    player.getMainHandItem().shrink(1);
                }

                return InteractionResult.SUCCESS;

            } else {
                // Неудача — превращаем в ZombifiedPiglin
                this.playSound(SoundEvents.ZOMBIFIED_PIGLIN_ANGRY, 1.0f, 1.0f);

                if (!this.level().isClientSide()) {
                    ZombifiedPiglin zombifiedPiglin = EntityType.ZOMBIFIED_PIGLIN.create(this.level());
                    if (zombifiedPiglin != null) {
                        zombifiedPiglin.moveTo(
                                this.getX(), this.getY(), this.getZ(),
                                this.getYRot(), this.getXRot()
                        );

                        if (this.isBaby()) {
                            zombifiedPiglin.setBaby(true);
                        }

                        zombifiedPiglin.setInvulnerable(this.isInvulnerable());
                        zombifiedPiglin.setItemSlot(EquipmentSlot.HEAD,  this.getItemBySlot(EquipmentSlot.HEAD));
                        zombifiedPiglin.setItemSlot(EquipmentSlot.CHEST, this.getItemBySlot(EquipmentSlot.CHEST));
                        zombifiedPiglin.setItemSlot(EquipmentSlot.LEGS,  this.getItemBySlot(EquipmentSlot.LEGS));
                        zombifiedPiglin.setItemSlot(EquipmentSlot.FEET,  this.getItemBySlot(EquipmentSlot.FEET));
                        zombifiedPiglin.setItemInHand(InteractionHand.MAIN_HAND, this.getItemInHand(InteractionHand.MAIN_HAND));
                        zombifiedPiglin.setItemInHand(InteractionHand.OFF_HAND,  this.getItemInHand(InteractionHand.OFF_HAND));

                        this.discard();
                        this.level().addFreshEntity(zombifiedPiglin);

                        if (this.level() instanceof ServerLevel serverLevel) {
                            serverLevel.sendParticles(
                                    ParticleTypes.LARGE_SMOKE,
                                    zombifiedPiglin.getX(), zombifiedPiglin.getY() + 1.0, zombifiedPiglin.getZ(),
                                    10, 0.5, 0.5, 0.5, 0.1
                            );
                        }
                    }
                }

                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }

    // ----------------------------------------------------------------
    // 1.21: save/load → addAdditionalSaveData/readAdditionalSaveData
    // ----------------------------------------------------------------
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
}