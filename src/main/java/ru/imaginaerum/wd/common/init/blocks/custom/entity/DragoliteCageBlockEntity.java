package ru.imaginaerum.wd.common.init.blocks.custom.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import ru.imaginaerum.wd.common.init.blocks.custom.DragoliteCage;

public class DragoliteCageBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer {

    private String soulFirst = "";
    private String soulSecond = "";
    private NonNullList<ItemStack> items = NonNullList.withSize(0, ItemStack.EMPTY);

    @Override
    public NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    public void setItems(NonNullList<ItemStack> items) {
        this.items = items;
    }


    public DragoliteCageBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.DRAGOLITE_CAGE_ENTITY.get(), pos, state);
    }

    // 1.21.1: saveAdditional принимает HolderLookup.Provider
    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putString("soul_first", soulFirst);
        tag.putString("soul_second", soulSecond);
    }
    private int tickCounter = 0;
    private static final int TICK_INTERVAL = 200; // каждые 200 тиков = 10 секунд

    public boolean shouldTick() {
        tickCounter++;
        if (tickCounter >= TICK_INTERVAL) {
            tickCounter = 0;
            return true;
        }
        return false;
    }
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {

        // можно вызвать статический метод блока
        BlockState currentState = level.getBlockState(pos);
        Block block = currentState.getBlock();
        if (block instanceof DragoliteCage cage) {
            cage.tick(currentState, level, pos, random);
        }
    }
    // 1.21.1: loadAdditional вместо load, тоже принимает HolderLookup.Provider
    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("soul_first")) {
            soulFirst = tag.getString("soul_first");
        }
        if (tag.contains("soul_second")) {
            soulSecond = tag.getString("soul_second");
        }
    }

    public void setSoulFirst(String soulFirst) {
        this.soulFirst = soulFirst;
        setChanged();
    }

    public String getSoulFirst() {
        return soulFirst;
    }

    public void setSoulSecond(String soulSecond) {
        this.soulSecond = soulSecond;
        setChanged();
    }

    public String getSoulSecond() {
        return soulSecond;
    }

    public String getSoulsTwo() {
        return soulFirst.equals(soulSecond) ? soulFirst : "";
    }

    // 1.21.1: getUpdateTag принимает HolderLookup.Provider
    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        saveAdditional(tag, registries);
        return tag;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    protected Component getDefaultName() {
        return Component.literal("Dragolite Cage");
    }

    @Override
    protected AbstractContainerMenu createMenu(int i, Inventory inventory) {
        return null;
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return new int[0];
    }

    @Override
    public boolean canPlaceItemThroughFace(int i, ItemStack itemStack, @Nullable Direction direction) {
        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int i, ItemStack itemStack, Direction direction) {
        return false;
    }

    @Override
    public int getContainerSize() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public ItemStack getItem(int i) {
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItem(int i, int i1) {
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItemNoUpdate(int i) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItem(int i, ItemStack itemStack) {
    }

    @Override
    public boolean stillValid(Player player) {
        return false;
    }

    @Override
    public void clearContent() {
    }
}