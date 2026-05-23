package ru.imaginaerum.wd.common.init.blocks.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import ru.imaginaerum.wd.common.init.blocks.custom.entity.GlowingJamBlockEntity;
import ru.imaginaerum.wd.common.init.items.ItemsWD;

import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class GlowingJamBlock extends BaseEntityBlock implements GeoBlockEntity {

    public static final IntegerProperty ROTATION = BlockStateProperties.ROTATION_16;
    public static final int ROTATIONS = RotationSegment.getMaxSegmentIndex() + 1;

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private static final VoxelShape SHAPE =
            Shapes.or(
                    Block.box(5, 7, 5, 11, 8, 11),
                    Block.box(4, 0, 4, 12, 6, 12)
            );

    public GlowingJamBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(ROTATION, 0));
    }

    public static final MapCodec<GlowingJamBlock> CODEC =
            simpleCodec(GlowingJamBlock::new);

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<net.minecraft.world.level.block.Block, BlockState> builder) {
        builder.add(ROTATION);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        int seg = RotationSegment.convertToSegment(context.getRotation());
        return this.defaultBlockState().setValue(ROTATION, seg);
    }

    @Override
    public BlockState rotate(BlockState state, net.minecraft.world.level.block.Rotation rot) {
        return state.setValue(ROTATION, rot.rotate(state.getValue(ROTATION), ROTATIONS));
    }

    @Override
    public BlockState mirror(BlockState state, net.minecraft.world.level.block.Mirror mirror) {
        return state.setValue(ROTATION, mirror.mirror(state.getValue(ROTATION), ROTATIONS));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new GlowingJamBlockEntity(pos, state);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        // если нет анимаций — оставляем пустым
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.is(newState.getBlock())) {
            level.destroyBlock(pos.above(), true);
        }
        super.onRemove(state, level, pos, newState, moved);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.below())
                .isFaceSturdy(level, pos.below(), Direction.UP);
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

        if (level.isClientSide) return ItemInteractionResult.SUCCESS;

        if (player.isShiftKeyDown()) {
            level.removeBlock(pos, false);

            ItemStack drop = new ItemStack(ItemsWD.GLOWING_JAM.get());
            if (!player.addItem(drop)) {
                player.drop(drop, false);
            }

            level.playSound(
                    null,
                    pos,
                    SoundEvents.ITEM_PICKUP,
                    SoundSource.BLOCKS,
                    1.0F,
                    1.0F
            );

            player.swing(hand);
            return ItemInteractionResult.SUCCESS;
        }

        if (stack.isEmpty() && player.getFoodData().needsFood()) {
            level.removeBlock(pos, false);

            player.getFoodData().eat(14, 0.5F);
            player.addEffect(new MobEffectInstance(MobEffects.GLOWING, 220, 0));

            level.playSound(
                    null,
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
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        return SHAPE;
    }
}