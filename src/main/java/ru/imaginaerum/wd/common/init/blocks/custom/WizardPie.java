package ru.imaginaerum.wd.common.init.blocks.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;

public class WizardPie extends CakeBlock {

    public WizardPie(Properties properties) {
        super(properties);
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int bites = state.getValue(BITES);
        if (bites >= 1) {
            level.setBlock(pos, state.setValue(BITES, bites - 1), 3);
            // Запланировать следующий тик через 200 ticks (10 сек)
            level.scheduleTick(pos, this, 200);
        }
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

        Item item = stack.getItem();

        // свечка
        if (stack.is(ItemTags.CANDLES) && state.getValue(BITES) == 0) {

            Block block = Block.byItem(item);

            if (block instanceof CandleBlock) {

                if (!player.isCreative()) {
                    stack.shrink(1);
                }

                level.playSound(
                        null,
                        pos,
                        SoundEvents.CAKE_ADD_CANDLE,
                        SoundSource.BLOCKS,
                        1.0F,
                        1.0F
                );

                level.setBlockAndUpdate(
                        pos,
                        CandleWizardPie.byCandle(block)
                );

                level.gameEvent(
                        player,
                        GameEvent.BLOCK_CHANGE,
                        pos
                );

                player.awardStat(
                        Stats.ITEM_USED.get(item)
                );

                return ItemInteractionResult.SUCCESS;
            }
        }

        // клиент
        if (level.isClientSide) {

            if (eat(level, pos, state, player).consumesAction()) {
                return ItemInteractionResult.SUCCESS;
            }

            if (stack.isEmpty()) {
                return ItemInteractionResult.CONSUME;
            }
        }

        return eatCake(level, pos, state, player);
    }

    protected static ItemInteractionResult eatCake(
            LevelAccessor level,
            BlockPos pos,
            BlockState state,
            Player player
    ) {

        if (!player.canEat(false)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        player.awardStat(Stats.EAT_CAKE_SLICE);

        player.getFoodData().eat(4, 0.8F);

        level.playSound(
                null,
                pos,
                SoundEvents.GENERIC_EAT,
                SoundSource.BLOCKS,
                1.0F,
                1.0F
        );

        player.addEffect(
                new MobEffectInstance(
                        MobEffects.MOVEMENT_SPEED,
                        2400,
                        1
                )
        );

        player.addEffect(
                new MobEffectInstance(
                        MobEffects.REGENERATION,
                        100,
                        0
                )
        );

        int bites = state.getValue(BITES);

        level.gameEvent(
                player,
                GameEvent.EAT,
                pos
        );

        if (bites < 6) {

            level.setBlock(
                    pos,
                    state.setValue(BITES, bites + 1),
                    3
            );

            // ВОТ СЮДА:
            if (level instanceof ServerLevel serverLevel) {
                serverLevel.scheduleTick(pos, state.getBlock(), 200);
            }

        } else {

            level.removeBlock(pos, false);

            level.gameEvent(
                    player,
                    GameEvent.BLOCK_DESTROY,
                    pos
            );
        }

        return ItemInteractionResult.SUCCESS;
    }
}