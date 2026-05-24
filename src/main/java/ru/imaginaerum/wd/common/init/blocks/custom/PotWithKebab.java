package ru.imaginaerum.wd.common.init.blocks.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
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
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import ru.imaginaerum.wd.common.init.blocks.BlocksWD;

import java.util.Collections;
import java.util.List;

public class PotWithKebab extends HorizontalDirectionalBlock {

    public static final MapCodec<PotWithKebab> CODEC =
            simpleCodec(PotWithKebab::new);

    public static final DirectionProperty FACING =
            BlockStateProperties.HORIZONTAL_FACING;

    public PotWithKebab(Properties properties) {
        super(properties);

        this.registerDefaultState(
                this.stateDefinition.any()
                        .setValue(FACING, Direction.NORTH)
        );
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(
            StateDefinition.Builder<Block, BlockState> builder
    ) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
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
    public void appendHoverText(
            ItemStack stack,
            Item.TooltipContext context,
            List<Component> tooltip,
            TooltipFlag flag
    ) {

        if (stack.getItem() == BlocksWD.POT_FROM_MEAT_GOAT.get().asItem()) {
            tooltip.add(
                    Component.translatable("tooltip.wd.pot_from_meat_goat")
                            .withStyle(ChatFormatting.DARK_PURPLE)
            );
        }

        if (stack.getItem() == BlocksWD.POT_FROM_MEAT_CAMEL.get().asItem()) {
            tooltip.add(
                    Component.translatable("tooltip.wd.pot_from_meat_camel")
                            .withStyle(ChatFormatting.DARK_PURPLE)
            );
        }
    }

    @Override
    protected void onPlace(
            BlockState state,
            Level level,
            BlockPos pos,
            BlockState oldState,
            boolean isMoving
    ) {

        super.onPlace(state, level, pos, oldState, isMoving);

        if (!level.isClientSide) {

            if (state.is(BlocksWD.POT_FROM_MEAT_GOAT.get())
                    || state.is(BlocksWD.POT_FROM_MEAT_CAMEL.get())) {

                level.scheduleTick(pos, this, 14400);
            }
        }
    }

    @Override
    protected void tick(
            BlockState state,
            ServerLevel level,
            BlockPos pos,
            RandomSource random
    ) {

        if (state.is(BlocksWD.POT_FROM_MEAT_GOAT.get())) {

            level.setBlock(
                    pos,
                    BlocksWD.MARINADED_POT_FROM_MEAT_GOAT.get()
                            .defaultBlockState()
                            .setValue(FACING, state.getValue(FACING)),
                    3
            );
        }

        if (state.is(BlocksWD.POT_FROM_MEAT_CAMEL.get())) {

            level.setBlock(
                    pos,
                    BlocksWD.MARINADED_POT_FROM_MEAT_CAMEL.get()
                            .defaultBlockState()
                            .setValue(FACING, state.getValue(FACING)),
                    3
            );
        }
    }

    @Override
    protected VoxelShape getShape(
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
    public void animateTick(
            BlockState state,
            Level level,
            BlockPos pos,
            RandomSource random
    ) {

        if (state.is(BlocksWD.POT_FROM_MEAT_GOAT.get())
                || state.is(BlocksWD.POT_FROM_MEAT_CAMEL.get())) {

            double x = pos.getX() + 0.5;
            double y = pos.getY() + 1.0;
            double z = pos.getZ() + 0.5;

            level.addParticle(
                    ColorParticleOption.create(
                            ParticleTypes.ENTITY_EFFECT,
                            0xFFD1D9A5   // FF = полная непрозрачность
                    ),
                    x, y, z,
                    0.0, 0.0, 0.0
            );
        }
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
    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        List<ItemStack> dropsOriginal = super.getDrops(state, builder);
        if (!dropsOriginal.isEmpty())
            return dropsOriginal;
        return Collections.singletonList(new ItemStack(this, 1));
    }
}