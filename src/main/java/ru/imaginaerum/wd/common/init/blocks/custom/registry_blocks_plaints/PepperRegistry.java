package ru.imaginaerum.wd.common.init.blocks.custom.registry_blocks_plaints;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashSet;
import java.util.Set;

public class PepperRegistry extends SavedData {

    private final Set<BlockPos> pepperBlocks = new HashSet<>();

    private static final SavedData.Factory<PepperRegistry> FACTORY = new SavedData.Factory<>(
            PepperRegistry::new,
            (tag, provider) -> fromNbt(tag),
            null
    );

    // ==========================
    // ==== Сохранение в NBT ====
    // ==========================
    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider provider) {
        ListTag list = new ListTag();
        for (BlockPos pos : pepperBlocks) {
            CompoundTag posTag = new CompoundTag();
            posTag.putInt("x", pos.getX());
            posTag.putInt("y", pos.getY());
            posTag.putInt("z", pos.getZ());
            list.add(posTag);
        }
        tag.put("pepper_blocks", list);
        return tag;
    }

    // ==========================
    // ==== Загрузка из NBT =====
    // ==========================
    public static PepperRegistry fromNbt(CompoundTag tag) {
        PepperRegistry data = new PepperRegistry();
        ListTag list = tag.getList("pepper_blocks", Tag.TAG_COMPOUND);
        for (Tag t : list) {
            CompoundTag posTag = (CompoundTag) t;
            data.pepperBlocks.add(new BlockPos(
                    posTag.getInt("x"),
                    posTag.getInt("y"),
                    posTag.getInt("z")
            ));
        }
        return data;
    }

    // ==========================
    // ======= Методы ===========
    // ==========================
    public void add(BlockPos pos) {
        pepperBlocks.add(pos.immutable());
        setDirty();
    }

    public void remove(BlockPos pos) {
        pepperBlocks.remove(pos);
        setDirty();
    }

    public Set<BlockPos> getPepperBlocks() {
        return new HashSet<>(pepperBlocks);
    }

    // ==========================
    // ======= Доступ ===========
    // ==========================
    public static PepperRegistry get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(FACTORY, "pepper_data");
    }
}