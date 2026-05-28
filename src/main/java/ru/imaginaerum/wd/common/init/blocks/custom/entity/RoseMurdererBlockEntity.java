package ru.imaginaerum.wd.common.init.blocks.custom.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import ru.imaginaerum.wd.common.init.blocks.custom.entity.ModBlockEntities;

public class RoseMurdererBlockEntity extends BlockEntity {

    private String entityType = "";
    private String entityName = "";

    public RoseMurdererBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ROSE_MURDERER.get(), pos, state);
    }

    // =========================================================
    // DATA
    // =========================================================

    public void setEntityInfo(String entityType, String entityName) {
        this.entityType = entityType;
        this.entityName = entityName;

        setChanged();

        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(
                    worldPosition,
                    getBlockState(),
                    getBlockState(),
                    3
            );
        }
    }

    public String getEntityType() {
        return entityType;
    }

    public String getEntityName() {
        return entityName;
    }

    public void clearEntityInfo() {
        this.entityType = "";
        this.entityName = "";

        setChanged();
    }

    // =========================================================
    // SAVE / LOAD
    // =========================================================

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        tag.putString("entity_type", entityType);
        tag.putString("entity_name", entityName);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        entityType = tag.getString("entity_type");
        entityName = tag.getString("entity_name");
    }

    // =========================================================
    // CLIENT SYNC
    // =========================================================

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();

        tag.putString("entity_type", entityType);
        tag.putString("entity_name", entityName);

        return tag;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}