package ru.imaginaerum.wd.common.init.blocks.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import ru.imaginaerum.wd.common.init.blocks.BlocksWD;
import ru.imaginaerum.wd.common.init.blocks.custom.registry_blocks_plaints.PepperRegistry;
import ru.imaginaerum.wd.common.init.items.ItemsWD;

public class BrightPepperSeeds extends Block {

    public static final IntegerProperty STAGE = IntegerProperty.create("stage", 0, 12);

    private static final VoxelShape[] SHAPES = new VoxelShape[13];

    static {
        for (int i = 0; i <= 12; i++) {
            double height = (i + 1) * (16.0 / 13.0);
            SHAPES[i] = Block.box(4.0, 0.0, 4.0, 12.0, Math.min(16.0, height), 12.0);
        }
    }

    public BrightPepperSeeds(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(STAGE, 0));
    }
    @Override
    public InteractionResult useWithoutItem(
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            BlockHitResult hit
    ) {
        if (!level.isClientSide && state.getValue(STAGE) == 12) {
            harvestPepper(level, pos, state);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    private void harvestPepper(Level level, BlockPos pos, BlockState state) {
        RandomSource random = level.random;

        int dropCount = random.nextFloat() < 0.3f
                ? (random.nextBoolean() ? 3 : 4)
                : (random.nextBoolean() ? 1 : 2);

        popResource(level, pos, new ItemStack(ItemsWD.BRIGHT_PEPPER.get(), dropCount));

        level.setBlock(pos, state.setValue(STAGE, 9), Block.UPDATE_CLIENTS);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPES[state.getValue(STAGE)];
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(STAGE);
    }

    @Override
    public void onPlace(
            BlockState state,
            Level level,
            BlockPos pos,
            BlockState oldState,
            boolean movedByPiston
    ) {
        super.onPlace(state, level, pos, oldState, movedByPiston);

        if (!level.isClientSide && !state.is(oldState.getBlock())) {
            PepperRegistry.get((ServerLevel) level).add(pos);
        }
    }

    @Override
    public void onRemove(
            BlockState state,
            Level level,
            BlockPos pos,
            BlockState newState,
            boolean movedByPiston
    ) {
        if (!level.isClientSide && !state.is(newState.getBlock())) {
            PepperRegistry.get((ServerLevel) level).remove(pos);
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    public void neighborChanged(
            BlockState state,
            Level level,
            BlockPos pos,
            Block neighborBlock,
            BlockPos fromPos,
            boolean isMoving
    ) {
        super.neighborChanged(state, level, pos, neighborBlock, fromPos, isMoving);

        if (!level.isClientSide && !canSurvive(state, level, pos)) {
            level.destroyBlock(pos, true);
        }
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockState below = level.getBlockState(pos.below());
        return below.is(BlocksWD.MAGIC_SOIL_FARMLAND.get());
    }
}