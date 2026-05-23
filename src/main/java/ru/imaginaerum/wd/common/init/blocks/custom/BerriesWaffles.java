package ru.imaginaerum.wd.common.init.blocks.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import net.neoforged.neoforge.registries.DeferredHolder;

public class BerriesWaffles extends Block {

    public static final IntegerProperty WAFFLERS =
            IntegerProperty.create("wafflers", 0, 7);

    private final DeferredHolder<Item, Item> waffleItem;
    private final WaffleType waffleType;

    public BerriesWaffles(Properties properties,
                          DeferredHolder<Item, Item> waffleItem,
                          WaffleType waffleType) {

        super(properties);

        this.waffleItem = waffleItem;
        this.waffleType = waffleType;

        this.registerDefaultState(
                this.stateDefinition.any()
                        .setValue(WAFFLERS, 0)
        );
    }

    public enum WaffleType {
        BERRIES("berries_waffles"),
        APPLE("apple_waffles"),
        ICE("ice_waffles"),
        CHARMING("charming_waffles"),
        POISON("poison_waffles"),
        GLOW_BERRIES("glow_berries_waffles"),
        WAFFLES_NULL("waffles");

        public final String name;

        WaffleType(String name) {
            this.name = name;
        }
    }

    @Override
    protected void createBlockStateDefinition(
            StateDefinition.Builder<Block, BlockState> builder) {

        builder.add(WAFFLERS);
    }

    @Override
    protected void onRemove(BlockState state,
                            Level level,
                            BlockPos pos,
                            BlockState newState,
                            boolean isMoving) {

        if (!state.is(newState.getBlock())) {
            destroyAbove(level, pos);
        }

        super.onRemove(state, level, pos, newState, isMoving);
    }

    private void destroyAbove(Level level, BlockPos pos) {

        BlockPos abovePos = pos.above();
        BlockState aboveState = level.getBlockState(abovePos);

        if (aboveState.getBlock() instanceof BerriesWaffles) {
            level.destroyBlock(abovePos, true);
            destroyAbove(level, abovePos);
        }
    }

    @Override
    protected void tick(BlockState state,
                        ServerLevel level,
                        BlockPos pos,
                        RandomSource random) {

        if (!state.canSurvive(level, pos)) {
            level.destroyBlock(pos, true);
        }
    }

    @Override
    protected BlockState updateShape(BlockState currentState,
                                     Direction direction,
                                     BlockState adjacentState,
                                     LevelAccessor level,
                                     BlockPos currentPos,
                                     BlockPos adjacentPos) {

        if (!currentState.canSurvive(level, currentPos)) {
            level.scheduleTick(currentPos, this, 1);
        }

        return super.updateShape(
                currentState,
                direction,
                adjacentState,
                level,
                currentPos,
                adjacentPos
        );
    }

    @Override
    protected boolean canSurvive(BlockState state,
                                 LevelReader level,
                                 BlockPos pos) {

        BlockState below = level.getBlockState(pos.below());

        if (below.is(this)) {
            return below.getValue(WAFFLERS) == 7;
        }

        return below.isFaceSturdy(level, pos.below(), Direction.UP);
    }

    @Override
    protected VoxelShape getShape(BlockState state,
                                  BlockGetter level,
                                  BlockPos pos,
                                  CollisionContext context) {

        int stage = state.getValue(WAFFLERS);

        double height = 2.0D + stage * 2.0D;

        return Block.box(
                3.0D,
                0.0D,
                3.0D,
                13.0D,
                height,
                13.0D
        );
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack,
                                              BlockState state,
                                              Level level,
                                              BlockPos pos,
                                              Player player,
                                              InteractionHand hand,
                                              BlockHitResult hitResult) {

        int currentStage = state.getValue(WAFFLERS);

        // SHIFT + ПКМ
        if (player.isShiftKeyDown()) {

            if (currentStage > 0) {

                level.setBlock(
                        pos,
                        state.setValue(WAFFLERS, currentStage - 1),
                        3
                );

            } else {
                level.removeBlock(pos, false);
            }

            if (!level.isClientSide) {

                ItemStack give = new ItemStack(waffleItem.get());

                if (!player.addItem(give)) {
                    player.drop(give, false);
                }
            }

            level.playSound(
                    player,
                    pos,
                    SoundEvents.WOOL_PLACE,
                    SoundSource.BLOCKS,
                    1.0F,
                    1.0F
            );

            player.swing(hand);

            return ItemInteractionResult.SUCCESS;
        }

        // Добавление вафли
        if (stack.is(waffleItem.get())) {

            if (currentStage < 7) {

                level.setBlock(
                        pos,
                        state.setValue(WAFFLERS, currentStage + 1),
                        3
                );

                level.playSound(
                        player,
                        pos,
                        SoundEvents.WOOL_PLACE,
                        SoundSource.BLOCKS,
                        1.0F,
                        1.0F
                );

                if (!player.isCreative()) {
                    stack.shrink(1);
                }

                player.swing(hand);

                return ItemInteractionResult.SUCCESS;
            }
        }

        // Поедание
        if (stack.isEmpty() && player.getFoodData().needsFood()) {

            if (currentStage > 0) {

                level.setBlock(
                        pos,
                        state.setValue(WAFFLERS, currentStage - 1),
                        3
                );

            } else {
                level.removeBlock(pos, false);
            }

            if (!level.isClientSide) {
                player.getFoodData().eat(6, 1.0F);
            }

            level.playSound(
                    player,
                    pos,
                    SoundEvents.GENERIC_EAT,
                    SoundSource.PLAYERS,
                    1.0F,
                    1.0F
            );

            player.swing(hand);

            return ItemInteractionResult.SUCCESS;
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    public WaffleType getWaffleType() {
        return waffleType;
    }
}