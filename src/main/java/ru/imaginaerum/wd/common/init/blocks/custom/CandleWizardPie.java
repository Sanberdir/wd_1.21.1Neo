package ru.imaginaerum.wd.common.init.blocks.custom;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.AbstractCandleBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import ru.imaginaerum.wd.common.init.blocks.BlocksWD;

import java.util.Map;

public class CandleWizardPie extends AbstractCandleBlock {

    public static BooleanProperty LIT = AbstractCandleBlock.LIT;

    private static VoxelShape CAKE_SHAPE =
            Block.box(1.0, 0.0, 1.0, 15.0, 8.0, 15.0);

    private static VoxelShape CANDLE_SHAPE =
            Block.box(7.0, 8.0, 7.0, 9.0, 14.0, 9.0);

    private static VoxelShape SHAPE =
            Shapes.or(CAKE_SHAPE, CANDLE_SHAPE);

    private static Map<Block, CandleWizardPie> BY_CANDLE = Maps.newHashMap();

    private static Iterable<Vec3> PARTICLE_OFFSETS =
            ImmutableList.of(new Vec3(0.5, 1.0, 0.5));

    public CandleWizardPie(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(LIT, false));
    }

    public CandleWizardPie(Block candle, Properties properties) {
        this(properties);
        BY_CANDLE.put(candle, this);
    }

    @Override
    public com.mojang.serialization.MapCodec<? extends AbstractCandleBlock> codec() {
        return simpleCodec(CandleWizardPie::new);
    }

    @Override
    protected Iterable<Vec3> getParticleOffsets(BlockState state) {
        return PARTICLE_OFFSETS;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        return SHAPE;
    }

    @Override
    protected ItemInteractionResult useItemOn(
            ItemStack stack,
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            InteractionHand hand,
            BlockHitResult hit
    ) {
        ItemStack held = player.getItemInHand(hand);

        if (!held.is(Items.FLINT_AND_STEEL) && !held.is(Items.FIRE_CHARGE)) {

            if (candleHit(hit) && held.isEmpty() && state.getValue(LIT)) {
                extinguish(player, state, level, pos);
                return ItemInteractionResult.SUCCESS;
            }

            ItemInteractionResult result =
                    WizardPie.eatCake(level, pos, BlocksWD.WIZARD_PIE.get().defaultBlockState(), player);

            if (result.consumesAction()) {
                dropResources(state, level, pos);
            }

            return result;
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    private static boolean candleHit(BlockHitResult hit) {
        return hit.getLocation().y - hit.getBlockPos().getY() > 0.5;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader levelReader, BlockPos blockPos, BlockState blockState) {
        return new ItemStack(BlocksWD.WIZARD_PIE.get());
    }

    @Override
    public BlockState updateShape(
            BlockState state,
            Direction dir,
            BlockState neighbor,
            LevelAccessor level,
            BlockPos pos,
            BlockPos neighborPos
    ) {
        if (dir == Direction.DOWN && !state.canSurvive(level, pos)) {
            return Blocks.AIR.defaultBlockState();
        }

        return super.updateShape(state, dir, neighbor, level, pos, neighborPos);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.below()).isSolid();
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return CakeBlock.FULL_CAKE_SIGNAL;
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }

    public static BlockState byCandle(Block candle) {
        CandleWizardPie pie = BY_CANDLE.get(candle);
        return pie != null ? pie.defaultBlockState() : BlocksWD.WIZARD_PIE.get().defaultBlockState();
    }

    public static boolean canLight(BlockState state) {
        return state.is(BlockTags.CANDLE_CAKES)
                && state.hasProperty(LIT)
                && !state.getValue(LIT);
    }
    static {
        LIT = AbstractCandleBlock.LIT;
        CAKE_SHAPE = Block.box(1.0, 0.0, 1.0, 15.0, 8.0, 15.0);
        CANDLE_SHAPE = Block.box(7.0, 8.0, 7.0, 9.0, 14.0, 9.0);
        SHAPE = Shapes.or(CAKE_SHAPE, CANDLE_SHAPE);
        BY_CANDLE = Maps.newHashMap();
        PARTICLE_OFFSETS = ImmutableList.of(new Vec3(0.5, 1.0, 0.5));
    }
}