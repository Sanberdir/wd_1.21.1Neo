package ru.imaginaerum.wd.common.init.blocks.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
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
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import net.neoforged.neoforge.registries.DeferredHolder;

import ru.imaginaerum.wd.common.init.effects.EffectsWD;

public class FreezeJamBlock extends Block {

    public static final IntegerProperty JAM_NUMBER =
            IntegerProperty.create("jam_number", 0, 7);

    private static final VoxelShape SHAPES_1 =
            Block.box(5.0D, 0.0D, 5.0D, 11.0D, 8.0D, 11.0D);

    private static final VoxelShape SHAPES_SMALL_UP =
            Block.box(5.0D, 9.0D, 5.0D, 11.0D, 16.0D, 11.0D);

    private static final VoxelShape SHAPES_2 =
            Block.box(1.0D, 0.0D, 1.0D, 15.0D, 8.0D, 15.0D);

    private static final VoxelShape SHAPES_3_4 =
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D);

    private static final VoxelShape SHAPES_5 =
            Shapes.or(SHAPES_SMALL_UP, SHAPES_3_4);

    private static final VoxelShape SHAPES_6 =
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);

    private final DeferredHolder<Item, Item> jamItem;

    public FreezeJamBlock(Properties properties,
                          DeferredHolder<Item, Item> jamItem) {

        super(properties);

        this.jamItem = jamItem;

        this.registerDefaultState(
                this.stateDefinition.any()
                        .setValue(JAM_NUMBER, 0)
        );
    }

    @Override
    protected void createBlockStateDefinition(
            StateDefinition.Builder<Block, BlockState> builder) {

        builder.add(JAM_NUMBER);
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
            return below.getValue(JAM_NUMBER) == 7;
        }

        return below.isFaceSturdy(level, pos.below(), Direction.UP);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack,
                                              BlockState state,
                                              Level level,
                                              BlockPos pos,
                                              Player player,
                                              InteractionHand hand,
                                              BlockHitResult hitResult) {

        int currentStage = state.getValue(JAM_NUMBER);

        // SHIFT + ПКМ
        if (player.isShiftKeyDown()) {

            if (currentStage > 0) {

                level.setBlock(
                        pos,
                        state.setValue(JAM_NUMBER, currentStage - 1),
                        3
                );

            } else {
                level.removeBlock(pos, false);
            }

            if (!level.isClientSide) {

                ItemStack give = new ItemStack(jamItem.get());

                if (!player.addItem(give)) {
                    player.drop(give, false);
                }
            }

            level.playSound(
                    player,
                    pos,
                    SoundEvents.ITEM_PICKUP,
                    SoundSource.BLOCKS,
                    1.0F,
                    1.0F
            );

            player.swing(hand);

            return ItemInteractionResult.SUCCESS;
        }

        // Добавление джема
        if (stack.is(jamItem.get())) {

            if (currentStage < 7) {

                level.setBlock(
                        pos,
                        state.setValue(JAM_NUMBER, currentStage + 1),
                        3
                );

                level.playSound(
                        player,
                        pos,
                        SoundEvents.GLASS_PLACE,
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
                        state.setValue(JAM_NUMBER, currentStage - 1),
                        3
                );

            } else {
                level.removeBlock(pos, false);
            }

            if (!level.isClientSide) {

                player.getFoodData().eat(10, 0.3F);

                RandomSource random = level.getRandom();

                player.addEffect(
                        new MobEffectInstance(
                                EffectsWD.FREEZE,
                                2200,
                                0
                        )
                );

                if (random.nextFloat() < 0.9F) {

                    player.addEffect(
                            new MobEffectInstance(
                                    MobEffects.MOVEMENT_SLOWDOWN,
                                    100,
                                    0
                            )
                    );
                }
            }

            level.playSound(
                    player,
                    pos,
                    SoundEvents.HONEY_DRINK,
                    SoundSource.PLAYERS,
                    1.0F,
                    1.0F
            );

            player.swing(hand);

            return ItemInteractionResult.SUCCESS;
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected VoxelShape getShape(BlockState state,
                                  BlockGetter getter,
                                  BlockPos pos,
                                  CollisionContext context) {

        int jamNumber = state.getValue(JAM_NUMBER);

        return switch (jamNumber) {
            case 0 -> SHAPES_1;
            case 1 -> SHAPES_2;
            case 2, 3 -> SHAPES_3_4;
            case 4 -> SHAPES_5;
            case 5, 6, 7 -> SHAPES_6;
            default -> SHAPES_1;
        };
    }
}