package ru.imaginaerum.wd.common.init.items.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.CustomModelData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import ru.imaginaerum.wd.common.init.blocks.BlocksWD;
import ru.imaginaerum.wd.common.init.blocks.custom.RoseMurderer;
import ru.imaginaerum.wd.common.init.blocks.custom.entity.RoseMurdererBlockEntity;
import ru.imaginaerum.wd.common.init.items.ItemsWD;

import javax.annotation.Nullable;
import java.util.List;

public class SoulStone extends Item {

    private static final String ENTITY_TYPE_TAG = "entity_type";
    private static final String ENTITY_NAME_TAG = "entity_name";

    public SoulStone(Properties properties) {
        super(properties);
    }

    // =========================================================
    // DATA COMPONENT HELPERS
    // =========================================================

    private static CompoundTag getData(ItemStack stack) {
        CustomData customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        return customData.copyTag();
    }

    private static void setData(ItemStack stack, CompoundTag tag) {
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }

    // =========================================================
    // TOOLTIP
    // =========================================================

    @Override
    public void appendHoverText(ItemStack stack,
                                @Nullable TooltipContext context,
                                List<Component> components,
                                TooltipFlag flag) {

        if (Screen.hasShiftDown()) {

            components.add(Component.translatable("wd.press_shift2")
                    .withStyle(ChatFormatting.DARK_GRAY));

            components.add(Component.translatable("wd.soul_stone_deactive")
                    .withStyle(ChatFormatting.DARK_PURPLE));

            CompoundTag tag = getData(stack);

            if (tag.contains(ENTITY_TYPE_TAG)) {
                components.add(Component.translatable(
                                "wd.entity_type",
                                tag.getString(ENTITY_TYPE_TAG))
                        .withStyle(ChatFormatting.GOLD));
            } else {
                components.add(Component.translatable("wd.entity_type_not_set")
                        .withStyle(ChatFormatting.RED));
            }

            if (tag.contains(ENTITY_NAME_TAG)) {
                components.add(Component.translatable(
                                "wd.entity_name",
                                tag.getString(ENTITY_NAME_TAG))
                        .withStyle(ChatFormatting.GREEN));
            } else {
                components.add(Component.translatable("wd.entity_name_not_set")
                        .withStyle(ChatFormatting.RED));
            }

        } else {

            components.add(Component.translatable("wd.press_shift")
                    .withStyle(ChatFormatting.DARK_GRAY));
        }

        super.appendHoverText(stack, context, components, flag);
    }

    // =========================================================
    // USE
    // =========================================================

    @Override
    public InteractionResult useOn(UseOnContext context) {

        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        ItemStack stack = context.getItemInHand();
        Player player = context.getPlayer();

        if (player == null) {
            return InteractionResult.PASS;
        }

        // Сначала пытаемся зарядить
        if (chargeSoulStone(level, pos, player, stack)) {
            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        // Потом освободить душу
        if (releaseSoulStone(level, pos, context.getClickedFace(), stack, player)) {
            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        return InteractionResult.PASS;
    }

    // =========================================================
    // CHARGE STATE
    // =========================================================

    public static boolean isCharged(ItemStack stack) {
        CustomModelData data = stack.get(DataComponents.CUSTOM_MODEL_DATA);
        return data != null && data.value() == 1;
    }

    public static void charged(ItemStack stack) {
        stack.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(1));
    }

    public static void discharged(ItemStack stack) {
        stack.remove(DataComponents.CUSTOM_MODEL_DATA);
    }

    // =========================================================
    // CHARGE SOUL STONE
    // =========================================================

    public static boolean chargeSoulStone(Level world,
                                          BlockPos pos,
                                          Player player,
                                          ItemStack stack) {

        if (world.isClientSide()) {
            return true;
        }

        BlockState blockState = world.getBlockState(pos);

        if (blockState.is(BlocksWD.ROSE_OF_THE_MURDERER.get())
                && blockState.getValue(RoseMurderer.IS_SOUL)
                && stack.is(ItemsWD.SOUL_STONE.get())) {

            charged(stack);

            String entityType = getEntityTag(world, pos, ENTITY_TYPE_TAG);
            String entityName = getEntityTag(world, pos, ENTITY_NAME_TAG);

            CompoundTag tag = getData(stack);

            tag.putString(ENTITY_TYPE_TAG, entityType);
            tag.putString(ENTITY_NAME_TAG, entityName);

            setData(stack, tag);

            world.setBlock(
                    pos,
                    blockState.setValue(RoseMurderer.IS_SOUL, false),
                    3
            );

            player.setItemInHand(InteractionHand.MAIN_HAND, stack);
            player.getInventory().setChanged();

            return true;
        }

        return false;
    }

    // =========================================================
    // RELEASE SOUL
    // =========================================================

    public static boolean releaseSoulStone(Level world,
                                           BlockPos pos,
                                           Direction face,
                                           ItemStack stack,
                                           Player player) {

        // Только сервер
        if (world.isClientSide()) {
            return true;
        }

        CompoundTag tag = getData(stack);

        if (!tag.contains(ENTITY_TYPE_TAG)
                || !tag.contains(ENTITY_NAME_TAG)) {
            return false;
        }

        String entityType = tag.getString(ENTITY_TYPE_TAG);

        EntityType<?> entityTypeObj =
                EntityType.byString(entityType).orElse(null);

        if (entityTypeObj != null) {

            Entity entityToSpawn = entityTypeObj.create(world);

            if (entityToSpawn != null) {

                BlockPos spawnPos =
                        getSpawnPositionForBlock(world, pos, face);

                entityToSpawn.setPos(
                        spawnPos.getX() + 0.5,
                        spawnPos.getY(),
                        spawnPos.getZ() + 0.5
                );

                world.addFreshEntity(entityToSpawn);
            }
        }

        // Очищаем данные
        tag.remove(ENTITY_TYPE_TAG);
        tag.remove(ENTITY_NAME_TAG);

        setData(stack, tag);

        discharged(stack);

        return true;
    }

    // =========================================================
    // SPAWN POSITION
    // =========================================================

    private static BlockPos getSpawnPositionForBlock(Level world,
                                                     BlockPos pos,
                                                     Direction face) {

        BlockPos spawnPos = pos.relative(face);

        while (!world.isEmptyBlock(spawnPos)) {
            spawnPos = spawnPos.above();
        }

        return spawnPos;
    }

    // =========================================================
    // BLOCK ENTITY DATA
    // =========================================================

    // =========================================================
// BLOCK ENTITY DATA
// =========================================================

    private static String getEntityTag(LevelAccessor world,
                                       BlockPos pos,
                                       String tag) {

        BlockEntity blockEntity = world.getBlockEntity(pos);

        if (blockEntity instanceof RoseMurdererBlockEntity roseBE) {
            return switch (tag) {
                case ENTITY_TYPE_TAG -> roseBE.getEntityType();
                case ENTITY_NAME_TAG -> roseBE.getEntityName();
                default -> "";
            };
        }

        return "";
    }
}