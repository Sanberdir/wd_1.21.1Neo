package ru.imaginaerum.wd.common.init.blocks.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
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
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.registries.DeferredHolder;

public class WafflesBlock extends Block {

    public static final IntegerProperty WAFFLERS = IntegerProperty.create("wafflers", 0, 7);

    private final DeferredHolder<Item, Item> waffleItem;

    public WafflesBlock(Properties properties, DeferredHolder<Item, Item> waffleItem) {
        super(properties);
        this.waffleItem = waffleItem;
        this.registerDefaultState(this.stateDefinition.any().setValue(WAFFLERS, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WAFFLERS);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockState below = level.getBlockState(pos.below());

        if (below.is(this)) {
            return below.getValue(WAFFLERS) == 7;
        }
        return below.isFaceSturdy(level, pos.below(), Direction.UP);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!state.canSurvive(level, pos)) {
            level.destroyBlock(pos, true);
        }
    }

    @Override
    public BlockState updateShape(BlockState state, Direction dir, BlockState neighbor,
                                  LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (!state.canSurvive(level, pos)) {
            level.scheduleTick(pos, this, 1);
        }
        return state;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        int stage = state.getValue(WAFFLERS);
        double h = 2.0D + stage * 2.0D;
        return Block.box(3, 0, 3, 13, h, 13);
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
        int stage = state.getValue(WAFFLERS);

        if (player.isShiftKeyDown()) {

            if (stage > 0) {
                level.setBlock(pos, state.setValue(WAFFLERS, stage - 1), 3);
            } else {
                level.destroyBlock(pos, false);
            }

            ItemStack drop = new ItemStack(waffleItem.get());
            if (!player.addItem(drop)) player.drop(drop, false);

            level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1f, 1f);
            player.swing(hand);

            return ItemInteractionResult.SUCCESS;
        }

        if (!held.isEmpty() && held.is(waffleItem.get())) {
            if (stage < 7) {
                level.setBlock(pos, state.setValue(WAFFLERS, stage + 1), 3);

                if (!player.isCreative()) {
                    held.shrink(1);
                }

                level.playSound(null, pos, SoundEvents.WOOL_PLACE, SoundSource.BLOCKS, 1f, 1f);
                player.swing(hand);

                return ItemInteractionResult.SUCCESS;
            }
        }

        if (held.isEmpty() && player.getFoodData().needsFood()) {

            if (stage > 0) {
                level.setBlock(pos, state.setValue(WAFFLERS, stage - 1), 3);
            } else {
                level.destroyBlock(pos, false);
            }

            player.getFoodData().eat(6, 1.0F);

            level.playSound(null, pos, SoundEvents.GENERIC_EAT, SoundSource.PLAYERS, 1f, 1f);
            player.swing(hand);

            return ItemInteractionResult.SUCCESS;
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }
}