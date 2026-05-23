package ru.imaginaerum.wd.common.init.blocks.custom.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class RoseMurdererBlockEntity extends BlockEntity {

    private static final String ENTITY_TYPE_KEY = "entity_type";
    private static final String ENTITY_NAME_KEY = "entity_name";

    private String entityType = "";
    private String entityName = "";

    public RoseMurdererBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ROSE_MURDERER.get(), pos, state);
    }

    public void setEntityInfo(String entityType, String entityName) {
        this.entityType = entityType == null ? "" : entityType;
        this.entityName = entityName == null ? "" : entityName;
        setChanged();
    }

    public void clearEntityInfo() {
        this.entityType = "";
        this.entityName = "";
        setChanged();
    }

    public String getEntityType() {
        return entityType;
    }

    public String getEntityName() {
        return entityName;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.entityType = tag.getString(ENTITY_TYPE_KEY);
        this.entityName = tag.getString(ENTITY_NAME_KEY);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putString(ENTITY_TYPE_KEY, this.entityType);
        tag.putString(ENTITY_NAME_KEY, this.entityName);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, registries);
        return tag;
    }

    @Override
    public @Nullable ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}