package ru.imaginaerum.wd.common.init.blocks.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import ru.imaginaerum.wd.common.init.blocks.BlocksWD;
import ru.imaginaerum.wd.common.init.items.ItemsWD;

import java.util.List;

public class MarinadedPot extends HorizontalDirectionalBlock {

    public static final MapCodec<MarinadedPot> CODEC =
            simpleCodec(MarinadedPot::new);

    public static final IntegerProperty STAGE =
            IntegerProperty.create("stage", 0, 3);

    public MarinadedPot(Properties properties) {
        super(properties);

        this.registerDefaultState(
                this.stateDefinition.any()
                        .setValue(FACING, Direction.NORTH)
                        .setValue(STAGE, 0)
        );
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    public void appendHoverText(
            ItemStack stack,
            Item.TooltipContext context,
            List<Component> tooltip,
            TooltipFlag flag
    ) {

        if (stack.getItem() == BlocksWD.MARINADED_POT_FROM_MEAT_GOAT.get().asItem()) {
            tooltip.add(
                    Component.translatable("tooltip.wd.pot_from_meat_goat")
                            .withStyle(ChatFormatting.DARK_PURPLE)
            );
        }

        if (stack.getItem() == BlocksWD.MARINADED_POT_FROM_MEAT_CAMEL.get().asItem()) {
            tooltip.add(
                    Component.translatable("tooltip.wd.pot_from_meat_camel")
                            .withStyle(ChatFormatting.DARK_PURPLE)
            );
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING, STAGE);
    }

    // Shapes

    private static final VoxelShape SHAPE_EAST = Shapes.or(
            box(5.0, 0.0, 5.0, 11.0, 7.0, 11.0),
            box(7.0, 7.0, 6.0, 9.0, 9.0, 10.0)
    );

    private static final VoxelShape SHAPE_NORTH = Shapes.or(
            box(5.0, 0.0, 5.0, 11.0, 7.0, 11.0),
            box(6.0, 7.0, 7.0, 10.0, 9.0, 9.0)
    );

    private static final VoxelShape SHAPE_WEST = Shapes.or(
            box(5.0, 0.0, 5.0, 11.0, 7.0, 11.0),
            box(7.0, 7.0, 6.0, 9.0, 9.0, 10.0)
    );

    private static final VoxelShape SHAPE_SOUTH = Shapes.or(
            box(5.0, 0.0, 5.0, 11.0, 7.0, 11.0),
            box(6.0, 7.0, 7.0, 10.0, 9.0, 9.0)
    );

    @Override
    public VoxelShape getShape(
            BlockState state,
            BlockGetter level,
            BlockPos pos,
            CollisionContext context
    ) {

        Direction direction = state.getValue(FACING);

        return switch (direction) {
            case EAST -> SHAPE_EAST;
            case SOUTH -> SHAPE_SOUTH;
            case WEST -> SHAPE_WEST;
            default -> SHAPE_NORTH;
        };
    }

    @Override
    protected InteractionResult useWithoutItem(
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            BlockHitResult hit
    ) {
        return InteractionResult.PASS;
    }

    @Override
    protected ItemInteractionResult useItemOn(
            ItemStack itemStack,
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            InteractionHand hand,
            BlockHitResult hit
    ) {

        int currentStage = state.getValue(STAGE);

        Item kebabItem = null;

        if (state.is(BlocksWD.MARINADED_POT_FROM_MEAT_GOAT.get())) {
            kebabItem = ItemsWD.GOAT_MEAT_KEBAB.get();
        } else if (state.is(BlocksWD.MARINADED_POT_FROM_MEAT_CAMEL.get())) {
            kebabItem = ItemsWD.CAMEL_MEAT_KEBAB.get();
        }

        if (kebabItem != null
                && itemStack.is(Items.STICK)
                && currentStage < 3) {

            if (!level.isClientSide) {

                if (!player.isCreative()) {
                    itemStack.shrink(1);
                }

                int newStage = currentStage + 1;

                level.setBlock(
                        pos,
                        state.setValue(STAGE, newStage),
                        3
                );

                ItemStack kebabStack = new ItemStack(kebabItem);

                if (!player.getInventory().add(kebabStack)) {
                    player.drop(kebabStack, false);
                }

                level.playSound(
                        null,
                        pos,
                        SoundEvents.ITEM_PICKUP,
                        SoundSource.BLOCKS,
                        1.0F,
                        1.0F
                );

                if (newStage >= 3) {
                    level.setBlock(
                            pos,
                            BlocksWD.POT.get().defaultBlockState(),
                            3
                    );
                }
            }

            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected boolean canSurvive(
            BlockState state,
            LevelReader level,
            BlockPos pos
    ) {

        BlockPos belowPos = pos.below();
        BlockState belowState = level.getBlockState(belowPos);

        VoxelShape shape = belowState.getShape(level, belowPos);

        return !shape.isEmpty()
                && shape.max(Direction.Axis.Y) >= 1.0;
    }

    @Override
    protected BlockState updateShape(
            BlockState state,
            Direction direction,
            BlockState neighborState,
            LevelAccessor level,
            BlockPos pos,
            BlockPos neighborPos
    ) {

        if (direction == Direction.DOWN
                && !canSurvive(state, level, pos)) {

            return Blocks.AIR.defaultBlockState();
        }

        return super.updateShape(
                state,
                direction,
                neighborState,
                level,
                pos,
                neighborPos
        );
    }
}