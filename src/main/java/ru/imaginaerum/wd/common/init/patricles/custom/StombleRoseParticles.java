package ru.imaginaerum.wd.common.init.patricles.custom;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class StombleRoseParticles extends TextureSheetParticle {
    protected StombleRoseParticles(ClientLevel level, double x, double y, double z,
                                   SpriteSet spriteSet, double xd, double yd, double zd) {
        super(level, x, y, z, xd * 0.7, yd * 0.7, zd * 0.7);

        this.friction = 0.8F;
        this.gravity = -0.2f;
        this.hasPhysics = true;

        this.xd = xd * 0.7;
        this.yd = yd * 0.7;
        this.zd = zd * 0.7;

        this.quadSize *= 0.85F;
        this.lifetime = 20;

        this.setColor(1f, 1f, 1f);

        // ❌ ВАЖНО: не setSpriteFromAge тут
        this.pickSprite(spriteSet);
    }

    @Override
    public void tick() {
        super.tick();
        fadeOut();
    }

    private void fadeOut() {
        this.alpha = (-(1/(float)lifetime) * age + 1);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }



    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet spriteSet) {
            this.sprites = spriteSet;
        }

        public Particle createParticle(SimpleParticleType particleType, ClientLevel level,
                                       double x, double y, double z,
                                       double dx, double dy, double dz) {
            return new StombleRoseParticles(level, x, y, z, this.sprites, dx, dy, dz);
        }
    }
}