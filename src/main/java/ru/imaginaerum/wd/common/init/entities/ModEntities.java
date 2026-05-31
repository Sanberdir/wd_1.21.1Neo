package ru.imaginaerum.wd.common.init.entities;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.bus.api.IEventBus;
import ru.imaginaerum.wd.WD;
import ru.imaginaerum.wd.common.init.entities.item_projectile_entities.StarBall;
import ru.imaginaerum.wd.common.init.entities.item_projectile_entities.arrows.FlameArrow;

import java.util.function.Supplier;

public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(Registries.ENTITY_TYPE, WD.MOD_ID);

    public static final Supplier<EntityType<StarBall>> STAR_BALL =
            ENTITY_TYPES.register("projectile_star_ball", () ->
                    EntityType.Builder.<StarBall>of(StarBall::new, MobCategory.MISC)
                            .sized(0.5f, 0.5f)
                            .clientTrackingRange(64)
                            .updateInterval(1)
                            .build("projectile_star_ball")
            );
    public static final Supplier<EntityType<FlameArrow>> FLAME_ARROW =
            ENTITY_TYPES.register("projectile_flame_arrow", () ->
                    EntityType.Builder.<FlameArrow>of(FlameArrow::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20)
                            .build("projectile_flame_arrow")
            );

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}