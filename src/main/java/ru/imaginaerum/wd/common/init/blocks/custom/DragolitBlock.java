package ru.imaginaerum.wd.common.init.blocks.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.server.level.ServerLevel;

public class DragolitBlock extends Block {

    public DragolitBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        super.stepOn(level, pos, state, entity);

        if (entity instanceof LivingEntity livingEntity
                && livingEntity.getType().is(EntityTypeTags.UNDEAD)) {

            DamageSource damageSource = new DamageSource(
                    level.registryAccess()
                            .lookupOrThrow(Registries.DAMAGE_TYPE)
                            .getOrThrow(DamageTypes.GENERIC)
            );

            // 4 сердца
            livingEntity.hurt(damageSource, 8.0F);
        }
    }

    public static void onEntityDeath(LivingEntity entity, DamageSource source) {

        DamageType genericType = entity.level()
                .registryAccess()
                .lookupOrThrow(Registries.DAMAGE_TYPE)
                .getOrThrow(DamageTypes.GENERIC)
                .value();

        if (source.type() == genericType
                && entity.getType().is(EntityTypeTags.UNDEAD)) {

            Level level = entity.level();

            if (!level.isClientSide && level instanceof ServerLevel serverLevel) {

                int xp = 7 + level.random.nextInt(4);

                ExperienceOrb.award(
                        serverLevel,
                        entity.position(),
                        xp
                );
            }
        }
    }
}