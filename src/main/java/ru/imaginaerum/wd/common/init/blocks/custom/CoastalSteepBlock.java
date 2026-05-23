package ru.imaginaerum.wd.common.init.blocks.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;

public class CoastalSteepBlock extends Block {

    // [ДОБАВЛЕНО] Обязательный кодек для 1.21.1
    public static final MapCodec<CoastalSteepBlock> CODEC = simpleCodec(CoastalSteepBlock::new);

    public static final BooleanProperty WATERLOGGED = BooleanProperty.create("waterlogged");

    public CoastalSteepBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, false));
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED);
    }

    // [ИЗМЕНЕНО] use() → useItemOn() — взаимодействие с предметом в руке
    // Возвращает ItemInteractionResult вместо InteractionResult
    @Override
    protected ItemInteractionResult useItemOn(ItemStack heldItem, BlockState state, Level level,
                                              BlockPos pos, Player player, InteractionHand hand,
                                              BlockHitResult hit) {
        if (!level.isClientSide) {
            if (heldItem.is(Items.GLASS_BOTTLE) && state.getValue(WATERLOGGED)) {
                if (!player.getAbilities().instabuild) {
                    heldItem.shrink(1);

                    // [ИЗМЕНЕНО] PotionUtils.setPotion → DataComponents.POTION_CONTENTS
                    ItemStack waterBottle = new ItemStack(Items.POTION);
                    waterBottle.set(DataComponents.POTION_CONTENTS, new PotionContents(Potions.WATER));

                    if (!player.addItem(waterBottle)) {
                        level.addFreshEntity(new ItemEntity(
                                level, player.getX(), player.getY(), player.getZ(), waterBottle));
                    }
                }

                level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.setBlock(pos, state.setValue(WATERLOGGED, false), 3);

                // [ИЗМЕНЕНО] InteractionResult.SUCCESS → ItemInteractionResult.SUCCESS
                return ItemInteractionResult.SUCCESS;
            }
        }

        // [ИЗМЕНЕНО] InteractionResult.PASS → PASS_TO_DEFAULT_BLOCK_INTERACTION
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }
    @Override
    protected void onPlace(BlockState state, Level level,
                           BlockPos pos, BlockState oldState,
                           boolean movedByPiston) {

        super.onPlace(state, level, pos, oldState, movedByPiston);

        if (!level.isClientSide) {
            level.scheduleTick(pos, this, 20);
        }
    }
    @Override
    protected void tick(BlockState state, ServerLevel level,
                        BlockPos pos, RandomSource random) {
        if (!state.canSurvive(level, pos)) {
            level.destroyBlock(pos, true);
            return;
        }
        if (!state.getValue(WATERLOGGED)) {
            boolean isRainy = level.isRainingAt(pos.above());
            boolean hasWaterNearby =
                    isWaterWithinDistance(level, pos, 3);
            if ((hasWaterNearby && random.nextFloat() < 0.3f)
                    || (isRainy && random.nextFloat() < 0.05f)) {
                level.setBlock(
                        pos,
                        state.setValue(WATERLOGGED, true),
                        3
                );
            }
        }
        level.scheduleTick(pos, this, 600);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
                                  LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        if (!state.canSurvive(world, pos)) {
            world.scheduleTick(pos, this, 1);
        }
        return super.updateShape(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader levelReader, BlockPos pos) {
        BlockState below = levelReader.getBlockState(pos.below());
        return below.is(Blocks.SAND) || below.is(Blocks.RED_SAND);
    }

    private boolean isWaterWithinDistance(ServerLevel level, BlockPos pos, int distance) {
        for (int dx = -distance; dx <= distance; dx++) {
            for (int dz = -distance; dz <= distance; dz++) {
                if (level.getBlockState(pos.offset(dx, -1, dz)).getFluidState().is(Fluids.WATER)) {
                    return true;
                }
            }
        }
        return false;
    }
}