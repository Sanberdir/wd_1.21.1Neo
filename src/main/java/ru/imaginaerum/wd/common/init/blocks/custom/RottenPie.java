package ru.imaginaerum.wd.common.init.blocks.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import ru.imaginaerum.wd.common.init.patricles.ModParticles;
import ru.imaginaerum.wd.common.sounds.CustomSoundEvents;

public class RottenPie extends Block {

    public static final IntegerProperty STAGE = IntegerProperty.create("stage", 0, 3);

    private static final VoxelShape SHAPE =
            Block.box(1.0D, 0.0D, 1.0D, 15.0D, 8.0D, 15.0D);

    private static final VoxelShape SHAPE_1 = Shapes.or(
            Block.box(1.0D, 0.0D, 6.0D, 15.0D, 8.0D, 15.0D),
            Block.box(8.0D, 0.0D, 1.0D, 15.0D, 8.0D, 6.0D)
    );

    private static final VoxelShape SHAPE_2 =
            Block.box(1.0D, 0.0D, 6.0D, 15.0D, 8.0D, 15.0D);

    private static final VoxelShape SHAPE_3 =
            Block.box(1.0D, 0.0D, 6.0D, 7.0D, 8.0D, 15.0D);

    public RottenPie(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(STAGE, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(STAGE);
    }

    @Override
    protected ItemInteractionResult useItemOn(
            ItemStack stack,
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            InteractionHand hand,
            BlockHitResult hitResult
    ) {
        if (level.isClientSide) {
            if (eatPie(level, pos, state, player).consumesAction()) {
                return ItemInteractionResult.SUCCESS;
            }

            if (stack.isEmpty()) {
                return ItemInteractionResult.CONSUME;
            }

            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        return eatPie(level, pos, state, player);
    }

    protected static ItemInteractionResult eatPie(
            LevelAccessor level,
            BlockPos pos,
            BlockState state,
            Player player
    ) {
        if (!player.canEat(false)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        player.addEffect(new MobEffectInstance(MobEffects.POISON, 200, 0));
        player.awardStat(Stats.EAT_CAKE_SLICE);
        player.getFoodData().eat(6, 0.8F);

        level.playSound(
                null,
                pos,
                SoundEvents.GENERIC_EAT,
                SoundSource.BLOCKS,
                1.0F,
                1.0F
        );

        int stage = state.getValue(STAGE);
        level.gameEvent(player, GameEvent.EAT, pos);

        if (stage < 3) {
            level.setBlock(pos, state.setValue(STAGE, stage + 1), 3);
        } else {
            level.removeBlock(pos, false);
            level.gameEvent(player, GameEvent.BLOCK_DESTROY, pos);
        }

        return ItemInteractionResult.SUCCESS;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(STAGE)) {
            case 1 -> SHAPE_1;
            case 2 -> SHAPE_2;
            case 3 -> SHAPE_3;
            default -> SHAPE;
        };
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource source) {
        super.animateTick(state, level, pos, source);

        VoxelShape shape = this.getShape(state, level, pos, CollisionContext.empty());

        level.playLocalSound(
                pos.getX() + 0.5D,
                pos.getY() + 0.5D,
                pos.getZ() + 0.5D,
                CustomSoundEvents.FLIES.get(),
                SoundSource.BLOCKS,
                0.5F,
                source.nextFloat() * 0.4F + 0.8F,
                false
        );

        Vec3 center = shape.bounds().getCenter();
        double x = pos.getX() + center.x;
        double z = pos.getZ() + center.z;

        for (int i = 0; i < 3; ++i) {
            if (source.nextBoolean()) {
                level.addParticle(
                        ModParticles.FLIES.get(),
                        x + source.nextDouble() / 10.0D,
                        pos.getY() + (1D - source.nextDouble()),
                        z + source.nextDouble() / 10.0D,
                        0.0D,
                        0.0D,
                        0.0D
                );
            }
        }
    }
}