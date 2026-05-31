package ru.imaginaerum.wd.common.init.entities.item_projectile_entities;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import ru.imaginaerum.wd.common.init.patricles.ModParticles;

public class AbstractHurtingProjectileMod extends Projectile {

    public double xPower;
    public double yPower;
    public double zPower;

    protected AbstractHurtingProjectileMod(EntityType<? extends AbstractHurtingProjectileMod> type, Level level) {
        super(type, level);
    }

    // В конструкторе — добавь установку начальной скорости:
    public AbstractHurtingProjectileMod(EntityType<? extends AbstractHurtingProjectileMod> type,
                                        double x, double y, double z,
                                        double vx, double vy, double vz,
                                        Level level) {
        this(type, level);
        this.moveTo(x, y, z, this.getYRot(), this.getXRot());
        this.reapplyPosition();

        // Сохраняем исходную скорость как ускорение
        this.xPower = vx * 0.05;  // подберите множитель
        this.yPower = vy * 0.05;
        this.zPower = vz * 0.05;

        // Устанавливаем начальную скорость
        this.setDeltaMovement(vx, vy, vz);
    }

    public AbstractHurtingProjectileMod(EntityType<? extends AbstractHurtingProjectileMod> type,
                                        LivingEntity shooter,
                                        double vx, double vy, double vz,
                                        Level level) {
        this(type, shooter.getX(), shooter.getY(), shooter.getZ(), vx, vy, vz, level);
        this.setOwner(shooter);
    }

    // =========================================================
    // SYNC (1.21 safe stub)
    // =========================================================

    // 1.21.1: Builder-параметр обязателен
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {}

    @Override
    protected double getDefaultGravity() {
        return 0.0;
    }

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket packet) {
        super.recreateFromPacket(packet);

        double dx = packet.getXa();
        double dy = packet.getYa();
        double dz = packet.getZa();

        double len = Math.sqrt(dx * dx + dy * dy + dz * dz);

        if (len != 0.0) {
            this.xPower = dx / len * 0.1;
            this.yPower = dy / len * 0.1;
            this.zPower = dz / len * 0.1;
        }
    }

    // =========================================================
    // MAIN TICK (FIXED ARCHITECTURE)
    // =========================================================

    @Override
    public void tick() {
        Entity owner = this.getOwner();

        if (this.level().isClientSide || (owner == null || !owner.isRemoved()) && this.level().hasChunkAt(this.blockPosition())) {

            super.tick();

            // Частицы — работают на клиенте
            if (this.shouldBurn()) {
                this.level().addParticle(
                        ModParticles.ROBIN_STAR_PARTICLES_PROJECTILE.get(),
                        getX(), getY(), getZ(), 0, 0, 0
                );
            }

            HitResult hit = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
            // ❌ ForgeEventFactory убран — в NeoForge 1.21 его нет
            if (hit.getType() != HitResult.Type.MISS) {
                this.onHit(hit);
            }

            this.checkInsideBlocks();

            Vec3 vec3 = this.getDeltaMovement();
            double d0 = this.getX() + vec3.x;
            double d1 = this.getY() + vec3.y;
            double d2 = this.getZ() + vec3.z;

            ProjectileUtil.rotateTowardsMovement(this, 0.2F);

            this.setDeltaMovement(vec3.add(this.xPower, this.yPower, this.zPower).scale(getInertia()));

            this.level().addParticle(
                    this.getTrailParticle(),
                    d0, d1 + 0.1, d2, 0, 0, 0
            );

            this.setPos(d0, d1, d2); // ✅ обязателен — super.tick() НЕ двигает снаряд

        } else {
            this.discard();
        }
    }

    // =========================================================

    protected boolean canHitEntity(Entity entity) {
        return super.canHitEntity(entity) && !entity.noPhysics;
    }

    protected boolean shouldBurn() {
        return true;
    }

    protected ParticleOptions getTrailParticle() {
        return ModParticles.ROBIN_STAR_PARTICLES.get();
    }

    protected float getInertia() {
        return 1F; // важно: 0.95 = слишком быстро умирает
    }

    // =========================================================
    // SAVE
    // =========================================================

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);

        ListTag list = new ListTag();
        list.add(net.minecraft.nbt.DoubleTag.valueOf(xPower));
        list.add(net.minecraft.nbt.DoubleTag.valueOf(yPower));
        list.add(net.minecraft.nbt.DoubleTag.valueOf(zPower));

        tag.put("power", list);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);

        if (tag.contains("power", 9)) {
            ListTag list = tag.getList("power", 6);

            if (list.size() == 3) {
                xPower = list.getDouble(0);
                yPower = list.getDouble(1);
                zPower = list.getDouble(2);
            }
        }
    }

    // =========================================================

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public float getPickRadius() {
        return 1.0F;
    }

    // =========================================================
    // DAMAGE / REFLECTION
    // =========================================================

    @Override
    public boolean hurt(DamageSource source, float amount) {

        if (this.isInvulnerableTo(source)) {
            return false;
        }

        this.markHurt();

        Entity attacker = source.getEntity();

        if (attacker instanceof LivingEntity living) {

            if (!this.level().isClientSide) {

                Vec3 look = living.getLookAngle();

                this.setDeltaMovement(look);

                xPower = look.x * 0.25;
                yPower = look.y * 0.25;
                zPower = look.z * 0.25;

                this.setOwner(living);
            }

            return true;
        }

        return false;
    }

    // =========================================================

    public float getLightLevelDependentMagicValue() {
        return 1.0F;
    }
}