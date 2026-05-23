package ru.imaginaerum.wd.common.init.blocks.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import ru.imaginaerum.wd.common.init.items.ItemsWD;

public class GoldenRose extends Block {

    public static final BooleanProperty ACTIVE =
            BooleanProperty.create("active");

    private static final int TICK_DELAY = 6000;

    public GoldenRose(Properties properties) {
        super(properties);

        this.registerDefaultState(
                this.stateDefinition.any()
                        .setValue(ACTIVE, true)
        );
    }

    @Override
    protected void createBlockStateDefinition(
            StateDefinition.Builder<Block, BlockState> builder
    ) {
        builder.add(ACTIVE);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {

        ItemStack stack = context.getItemInHand();

        if (stack.is(ItemsWD.MEADOW_GOLDEN_FLOWER.get())) {
            return this.defaultBlockState()
                    .setValue(ACTIVE, true);
        }

        return this.defaultBlockState()
                .setValue(ACTIVE, false);
    }

    @Override
    public ItemStack getCloneItemStack(
            LevelReader level,
            BlockPos pos,
            BlockState state
    ) {

        return state.getValue(ACTIVE)
                ? new ItemStack(ItemsWD.MEADOW_GOLDEN_FLOWER.get())
                : new ItemStack(ItemsWD.MEADOW_GOLDEN_FLOWER_INACTIVE.get());
    }

    @Override
    protected void onPlace(
            BlockState state,
            Level level,
            BlockPos pos,
            BlockState oldState,
            boolean movedByPiston
    ) {

        if (!level.isClientSide && !state.getValue(ACTIVE)) {
            level.scheduleTick(pos, this, TICK_DELAY);
        }

        super.onPlace(
                state,
                level,
                pos,
                oldState,
                movedByPiston
        );
    }

    @Override
    protected void tick(
            BlockState state,
            ServerLevel level,
            BlockPos pos,
            RandomSource random
    ) {

        if (!state.canSurvive(level, pos)) {
            level.destroyBlock(pos, true);
            return;
        }

        if (!state.getValue(ACTIVE)) {
            level.setBlock(
                    pos,
                    state.setValue(ACTIVE, true),
                    3
            );
        }
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
        if (!state.canSurvive(level, pos)) {
            level.scheduleTick(pos, this, 1);
        } else if (!state.getValue(ACTIVE)) {
            level.scheduleTick(pos, this, TICK_DELAY);
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
    protected boolean canSurvive(
            BlockState state,
            LevelReader level,
            BlockPos pos
    ) {

        BlockState below = level.getBlockState(pos.below());

        return below.is(Blocks.SOUL_SOIL)
                || below.is(Blocks.SOUL_SAND)
                || below.is(BlockTags.NYLIUM)
                || below.is(BlockTags.SAND)
                || below.is(Blocks.DIRT)
                || below.is(Blocks.GRASS_BLOCK);
    }

    @Override
    public void animateTick(
            BlockState state,
            Level level,
            BlockPos pos,
            RandomSource random
    ) {

        if (!state.getValue(ACTIVE)) {
            return;
        }

        VoxelShape shape =
                this.getShape(
                        state,
                        level,
                        pos,
                        CollisionContext.empty()
                );

        Vec3 center = shape.bounds().getCenter();

        double x = pos.getX() + center.x;
        double z = pos.getZ() + center.z;

        for (int i = 0; i < 3; i++) {

            if (random.nextBoolean()) {

                level.addParticle(
                        ParticleTypes.HAPPY_VILLAGER,
                        x + random.nextDouble() / 5.0D,
                        pos.getY() + (
                                0.5D - random.nextDouble()
                        ),
                        z + random.nextDouble() / 5.0D,
                        0.0D,
                        0.0D,
                        0.0D
                );
            }
        }
    }

    @Override
    protected void entityInside(
            BlockState state,
            Level level,
            BlockPos pos,
            Entity entity
    ) {

        if (
                !level.isClientSide
                        && state.getValue(ACTIVE)
                        && entity instanceof LivingEntity livingEntity
        ) {

            livingEntity.addEffect(
                    new MobEffectInstance(
                            MobEffects.REGENERATION,
                            40,
                            0
                    )
            );
        }
    }

    @Override
    protected boolean isPathfindable(
            BlockState state,
            PathComputationType pathComputationType
    ) {
        return false;
    }
}