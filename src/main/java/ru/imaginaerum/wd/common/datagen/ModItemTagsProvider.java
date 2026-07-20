package ru.imaginaerum.wd.common.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import ru.imaginaerum.wd.WD;
import ru.imaginaerum.wd.common.init.items.ItemsWD;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.concurrent.CompletableFuture;

public class ModItemTagsProvider extends ItemTagsProvider {

    public ModItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                               CompletableFuture<TagLookup<Block>> blockTags, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, WD.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(ItemTags.create(ResourceLocation.withDefaultNamespace("enchantable/durability")))
                .add(ItemsWD.MAG_ELYTRA.get());

        this.tag(ItemTags.create(ResourceLocation.withDefaultNamespace("enchantable/vanishing")))
                .add(ItemsWD.MAG_ELYTRA.get());

        this.tag(ItemTags.create(ResourceLocation.withDefaultNamespace("enchantable/equippable")))
                .add(ItemsWD.MAG_ELYTRA.get());
    }
}