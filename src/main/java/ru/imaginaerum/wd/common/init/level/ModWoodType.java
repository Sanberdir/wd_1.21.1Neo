package ru.imaginaerum.wd.common.init.level;

import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import ru.imaginaerum.wd.WD;


public class ModWoodType {
    public static final WoodType APPLE_WOOD = WoodType.register(new WoodType(WD.MOD_ID + ":apple_wood", BlockSetType.OAK));

}

