package ru.imaginaerum.wd.common.init.blocks.custom.entity;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import ru.imaginaerum.wd.WD;
import ru.imaginaerum.wd.common.init.blocks.BlocksWD;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, WD.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<RoseMurdererBlockEntity>> ROSE_MURDERER =
            BLOCK_ENTITIES.register("rose_murderer", () ->
                    BlockEntityType.Builder.of(
                            RoseMurdererBlockEntity::new,
                            BlocksWD.ROSE_OF_THE_MURDERER.get()
                    ).build(null)
            );
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<GlowingJamBlockEntity>> GLOWING_JAM =
            BLOCK_ENTITIES.register("glowing_jam", () ->
                    BlockEntityType.Builder.of(
                            GlowingJamBlockEntity::new,
                            BlocksWD.GLOWING_JAM.get()
                    ).build(null)
            );
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<DragoliteCageBlockEntity>> DRAGOLITE_CAGE_ENTITY =
            BLOCK_ENTITIES.register("dragolite_cage_entity", () ->
                    BlockEntityType.Builder.of(
                            DragoliteCageBlockEntity::new,
                            BlocksWD.DRAGOLITE_CAGE.get()
                    ).build(null)
            );
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ModSignBlockEntity>> APPLE_SIGN =
            BLOCK_ENTITIES.register("apple_sign", () ->
                    BlockEntityType.Builder.of(ModSignBlockEntity::new,
                            BlocksWD.APPLE_SIGN.get(), BlocksWD.APPLE_WALL_SIGN.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ModHangingSignBlockEntity>> APPLE_HANGING_SIGN =
            BLOCK_ENTITIES.register("apple_hanging_sign", () ->
                    BlockEntityType.Builder.of(ModHangingSignBlockEntity::new,
                            BlocksWD.APPLE_HANGING_SIGN.get(), BlocksWD.APPLE_WALL_HANGING_SIGN.get()).build(null));
}