package ru.imaginaerum.wd.common.init.trees;

import net.minecraft.world.level.block.grower.TreeGrower;
import ru.imaginaerum.wd.common.init.level.features.ModConfiguredFeatures;

import java.util.Optional;

public class ModTreeGrowers {

    public static final TreeGrower APPLE = new TreeGrower(
            "apple",
            Optional.empty(),
            Optional.of(ModConfiguredFeatures.APPLE_KEY),
            Optional.empty()
    );
}