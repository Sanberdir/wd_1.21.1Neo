package ru.imaginaerum.wd.common.init.patricles;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import ru.imaginaerum.wd.WD;

public class ModParticles {

    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, WD.MOD_ID);

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> STOMBLE_ROSE =
            PARTICLE_TYPES.register("stomble_rose",
                    () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> FLIES =
            PARTICLE_TYPES.register("flies",
                    () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> ROBIN_STAR_PARTICLES_PROJECTILE =
            PARTICLE_TYPES.register("robin_star_particles_projectile",
                    () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> ROBIN_STAR_PARTICLES =
            PARTICLE_TYPES.register("robin_star_particles",
                    () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> GOLDEN_FLOWER_PARTICLES =
            PARTICLE_TYPES.register("golden_flower_particles",
                    () -> new SimpleParticleType(true));
}