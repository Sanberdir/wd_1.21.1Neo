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
}