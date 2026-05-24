package ru.imaginaerum.wd.common.init.blocks.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import ru.imaginaerum.wd.common.init.blocks.BlocksWD;

public class MagicSoilGrass extends Block {

    public MagicSoilGrass(Properties properties) {
        super(properties);
    }

    @Override
    public ItemInteractionResult useItemOn(
            ItemStack stack,
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            InteractionHand hand,
            BlockHitResult hitResult
    ) {
        if (stack.is(ItemTags.HOES)) {
            if (level.isEmptyBlock(pos.above())) {
                level.setBlock(pos, BlocksWD.MAGIC_SOIL_FARMLAND.get().defaultBlockState(), 3);
                level.playSound(player, pos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1, 1);
                if (!level.isClientSide) {
                    EquipmentSlot slot = hand == InteractionHand.MAIN_HAND
                            ? EquipmentSlot.MAINHAND
                            : EquipmentSlot.OFFHAND;
                    stack.hurtAndBreak(1, player, slot);
                }
                return ItemInteractionResult.SUCCESS;
            }
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }
    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {

        // если сверху что-то есть — трава “задохнулась” и возвращается в почву
        if (!level.getBlockState(pos.above()).isAir()) {
            level.setBlockAndUpdate(pos, BlocksWD.MAGIC_SOIL.get().defaultBlockState());
            return;
        }

        // ванильный порог света
        if (level.getMaxLocalRawBrightness(pos.above()) < 9) {
            level.setBlockAndUpdate(pos, BlocksWD.MAGIC_SOIL.get().defaultBlockState());
            return;
        }

        // распространение (как у травы)
        for (int i = 0; i < 4; i++) {

            BlockPos target = pos.offset(
                    random.nextInt(3) - 1,
                    random.nextInt(5) - 3,
                    random.nextInt(3) - 1
            );

            BlockState targetState = level.getBlockState(target);

            if (targetState.is(BlocksWD.MAGIC_SOIL.get())
                    && level.getMaxLocalRawBrightness(target.above()) >= 9
                    && level.getBlockState(target.above()).isAir()) {

                level.setBlockAndUpdate(target, this.defaultBlockState());
            }
        }
    }
}