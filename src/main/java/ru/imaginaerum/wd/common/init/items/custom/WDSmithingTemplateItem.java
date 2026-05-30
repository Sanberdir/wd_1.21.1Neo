package ru.imaginaerum.wd.common.init.items.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SmithingTemplateItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.List;

public class WDSmithingTemplateItem extends SmithingTemplateItem {

    // ForgeRegistries.ITEMS -> BuiltInRegistries.ITEM
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(BuiltInRegistries.ITEM, "wd");

    public WDSmithingTemplateItem(
            Component displayName,
            Component baseSlotDescription,
            Component addSlotDescription,
            Component baseSlotTooltip,
            Component addSlotTooltip,
            List<ResourceLocation> baseSlotIcons,
            List<ResourceLocation> addSlotIcons
    ) {
        super(displayName, baseSlotDescription, addSlotDescription,
                baseSlotTooltip, addSlotTooltip, baseSlotIcons, addSlotIcons);
    }
}