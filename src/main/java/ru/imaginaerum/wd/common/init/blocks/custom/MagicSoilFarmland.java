package ru.imaginaerum.wd.common.init.blocks.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import ru.imaginaerum.wd.common.init.blocks.BlocksWD;
import ru.imaginaerum.wd.common.init.blocks.custom.registry_blocks_plaints.MagicSoilFarmlandData;

public class MagicSoilFarmland extends Block {

    public static final MapCodec<MagicSoilFarmland> CODEC = simpleCodec(MagicSoilFarmland::new);
    public static final BooleanProperty MOIST = BooleanProperty.create("moist");

    public MagicSoilFarmland(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(MOIST, false));
    }

    @Override
    protected MapCodec<MagicSoilFarmland> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(MOIST);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, level, pos, block, fromPos, isMoving);

        if (level.isClientSide) {
            return;
        }

        // Если блок снизу больше не поддерживает — возвращаем обычную землю
        if (!canSurvive(state, level, pos)) {
            turnToMagicSoil(level, pos);
            return;
        }

        BlockPos abovePos = pos.above();
        BlockState aboveState = level.getBlockState(abovePos);

        // Если сверху что-то стоит, но это не растение — тоже возвращаем обычную землю
        if (!aboveState.isAir() && !(aboveState.getBlock() instanceof BrightPepperSeeds)) {
            turnToMagicSoil(level, pos);
        }
    }

    private static void turnToMagicSoil(Level level, BlockPos pos) {
        BlockState oldState = level.getBlockState(pos);
        BlockState newState = BlocksWD.MAGIC_SOIL.get().defaultBlockState();
        level.setBlockAndUpdate(pos, pushEntitiesUp(oldState, newState, level, pos));
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockState aboveState = level.getBlockState(pos.above());
        return super.canSurvive(state, level, pos)
                || aboveState.is(net.minecraft.tags.BlockTags.CROPS)
                || aboveState.getBlock() instanceof BrightPepperSeeds;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Block.box(0.0F, 0.0F, 0.0F, 16.0F, 15.0F, 16.0F);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);

        if (!level.isClientSide) {
            MagicSoilFarmlandData.get((ServerLevel) level).add(pos);
        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        super.onRemove(state, level, pos, newState, isMoving);

        if (!level.isClientSide && state.getBlock() != newState.getBlock()) {
            MagicSoilFarmlandData.get((ServerLevel) level).remove(pos);
        }
    }
}