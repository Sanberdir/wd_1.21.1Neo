package ru.imaginaerum.wd.common.init.items.entity;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import ru.imaginaerum.wd.WD;

public class ModEntitiesItem {
    public static final DeferredRegister<EntityType<?>> ENTITIES_ITEM =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, WD.MOD_ID);

    public static final DeferredHolder<EntityType<?>, EntityType<ModBoatEntity>> MOD_BOAT =
            ENTITIES_ITEM.register("mod_boat", () -> EntityType.Builder.<ModBoatEntity>of(ModBoatEntity::new, MobCategory.MISC)
                    .sized(1.375f, 0.5625f).build("mod_boat"));

    public static final DeferredHolder<EntityType<?>, EntityType<ModChestBoatEntity>> MOD_CHEST_BOAT =
            ENTITIES_ITEM.register("mod_chest_boat", () -> EntityType.Builder.<ModChestBoatEntity>of(ModChestBoatEntity::new, MobCategory.MISC)
                    .sized(1.375f, 0.5625f).build("mod_chest_boat"));
}