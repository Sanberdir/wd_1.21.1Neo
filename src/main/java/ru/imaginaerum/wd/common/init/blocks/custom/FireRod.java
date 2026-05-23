package ru.imaginaerum.wd.common.init.blocks.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class FireRod extends Block {

    // [ДОБАВЛЕНО] Обязательный кодек для 1.21.1
    public static final MapCodec<FireRod> CODEC = simpleCodec(FireRod::new);

    public static final IntegerProperty AGE = BlockStateProperties.AGE_15;
    protected static final VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D);

    public FireRod(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!state.canSurvive(level, pos)) {
            level.destroyBlock(pos, true);
        }
    }

    // [ИЗМЕНЕНО] Убраны ForgeHooks.onCropsGrowPre/Post
    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (level.isEmptyBlock(pos.above())) {
            int height;
            for (height = 1; level.getBlockState(pos.below(height)).is(this); ++height) {
            }

            if (height < 3) {
                int age = state.getValue(AGE);
                if (age == 15) {
                    level.setBlockAndUpdate(pos.above(), this.defaultBlockState());
                    level.setBlock(pos, state.setValue(AGE, 0), 4);
                } else {
                    level.setBlock(pos, state.setValue(AGE, age + 1), 4);
                }
            }
        }
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
                                  LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (!state.canSurvive(level, pos)) {
            level.scheduleTick(pos, this, 1);
        }
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        BlockPos belowPos = pos.below();
        BlockState blockBelow = world.getBlockState(belowPos);

        if (blockBelow.is(this)) {
            return true;
        }

        boolean validBase =
                blockBelow.is(Blocks.NETHERRACK) ||
                        blockBelow.is(Blocks.BASALT) ||
                        blockBelow.is(Blocks.BLACKSTONE) ||
                        blockBelow.is(Blocks.MAGMA_BLOCK) ||
                        blockBelow.is(Blocks.CRIMSON_NYLIUM) ||
                        blockBelow.is(Blocks.WARPED_NYLIUM) ||
                        blockBelow.is(Blocks.SOUL_SOIL) ||
                        blockBelow.is(Blocks.SOUL_SAND);

        if (!validBase) return false;
        if (blockBelow.is(Blocks.MAGMA_BLOCK)) return true;

        for (Direction direction : Direction.Plane.HORIZONTAL) {
            FluidState neighborFluid = world.getFluidState(belowPos.relative(direction));
            if (neighborFluid.is(Fluids.LAVA) || neighborFluid.is(Fluids.FLOWING_LAVA)) {
                return true;
            }
        }

        return false;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        super.entityInside(state, level, pos, entity);
        // [ИЗМЕНЕНО] new DamageSources(...) → level.damageSources()
        // Создавать DamageSources вручную нельзя — используем экземпляр из уровня
        entity.hurt(level.damageSources().inFire(), 1);
        entity.hurt(level.damageSources().generic(), 4);
        // [ИЗМЕНЕНО] setSecondsOnFire → igniteForSeconds в 1.21.1
        entity.igniteForSeconds(5);
    }
}