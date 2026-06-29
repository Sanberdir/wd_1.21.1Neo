package ru.imaginaerum.wd.common.init.entities.goals_entity_mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import ru.imaginaerum.wd.common.init.blocks.BlocksWD;
import ru.imaginaerum.wd.common.init.blocks.custom.RottenPie;
import ru.imaginaerum.wd.common.init.blocks.custom.RottenPieCage;

import java.util.EnumSet;

public class ZombieEatPieGoal extends MoveToBlockGoal {

    private static final int SEARCH_COOLDOWN_MIN = 80;
    private static final int SEARCH_COOLDOWN_RAND = 40;
    private static final int BREAK_THRESHOLD = 320;
    private static final int SEARCH_RANGE = 16;
    private static final double REACH_DIST_SQ = 2.25D; // 1.5 блока

    private final Zombie zombie;
    private int searchCooldown = 0;
    private int eatCooldown = 0;
    private int breakProgress = 0;

    public ZombieEatPieGoal(Zombie zombie, double speed) {
        super(zombie, speed, SEARCH_RANGE, 4);
        this.zombie = zombie;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    // ─────────────────────────────────────────────────────────────────
    // canUse — вызывается каждый тик, должен быть максимально дёшев
    // ─────────────────────────────────────────────────────────────────
    @Override
    public boolean canUse() {
        // Раннее отсечение без дорогих операций
        if (!zombie.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
            return false;
        }

        // Поиск только раз в ~4–6 секунд
        if (searchCooldown > 0) {
            searchCooldown--;
            return false;
        }

        searchCooldown = SEARCH_COOLDOWN_MIN + zombie.level().random.nextInt(SEARCH_COOLDOWN_RAND);
        return this.findNearestBlock(); // дорогой вызов — но редкий
    }

    @Override
    public boolean canContinueToUse() {
        if (eatCooldown > 0) return true; // продолжаем паузу после еды/слома

        BlockState state = zombie.level().getBlockState(this.blockPos);
        return state.getBlock() instanceof RottenPie
                || state.getBlock() instanceof RottenPieCage;
    }

    // ─────────────────────────────────────────────────────────────────
    // tick
    // ─────────────────────────────────────────────────────────────────
    @Override
    public void tick() {
        super.tick();

        if (eatCooldown > 0) {
            eatCooldown--;
            return;
        }

        // Проверяем расстояние только по XZ — зомби может стоять на торте
        BlockPos pos = this.blockPos;
        double dx = zombie.getX() - (pos.getX() + 0.5);
        double dz = zombie.getZ() - (pos.getZ() + 0.5);
        if (dx * dx + dz * dz > REACH_DIST_SQ) return;

        Level level = zombie.level();
        BlockState state = level.getBlockState(pos);

        if (state.getBlock() instanceof RottenPieCage) {
            handleCage(level, pos, state);
        } else if (state.getBlock() instanceof RottenPie) {
            handlePie(level, pos, state);
        }
    }

    // ─────────────────────────────────────────────────────────────────
    // Логика клетки
    // ─────────────────────────────────────────────────────────────────
    private void handleCage(Level level, BlockPos pos, BlockState state) {
        breakProgress++;

        // Звук раз в 10 тиков — не каждый
        if (breakProgress % 10 == 0) {
            level.playSound(null, pos,
                    SoundEvents.ZOMBIE_ATTACK_WOODEN_DOOR, SoundSource.HOSTILE,
                    0.5F, 0.8F + level.random.nextFloat() * 0.2F);
        }

        if (breakProgress >= BREAK_THRESHOLD) {
            level.setBlock(pos,
                    BlocksWD.ROTTEN_PIE.get().defaultBlockState()
                            .setValue(RottenPie.STAGE, 0), 3);
            level.playSound(null, pos,
                    SoundEvents.ZOMBIE_BREAK_WOODEN_DOOR, SoundSource.HOSTILE,
                    1.0F, 0.9F + level.random.nextFloat() * 0.2F);

            zombie.getNavigation().stop();
            breakProgress = 0;
            eatCooldown = 25;
        }
    }

    // ─────────────────────────────────────────────────────────────────
    // Логика поедания
    // ─────────────────────────────────────────────────────────────────
    private void handlePie(Level level, BlockPos pos, BlockState state) {
        int stage = state.getValue(RottenPie.STAGE);

        if (stage < 3) {
            level.setBlock(pos, state.setValue(RottenPie.STAGE, stage + 1), 3);
        } else {
            level.removeBlock(pos, false);
        }

        level.playSound(null, pos,
                SoundEvents.GENERIC_EAT, SoundSource.HOSTILE,
                0.7F, 0.9F + level.random.nextFloat() * 0.2F);

        if (level.random.nextFloat() < 0.7F) {
            zombie.addEffect(new MobEffectInstance(MobEffects.POISON, 200, 0));
        }

        zombie.getNavigation().stop();
        eatCooldown = 60;
    }

    // ─────────────────────────────────────────────────────────────────
    // Валидация цели
    // ─────────────────────────────────────────────────────────────────
    @Override
    protected boolean isValidTarget(LevelReader levelReader, BlockPos blockPos) {
        BlockState state = levelReader.getBlockState(blockPos);

        if (!(state.getBlock() instanceof RottenPie
                || state.getBlock() instanceof RottenPieCage)) {
            return false;
        }

        int zombieY = this.zombie.blockPosition().getY();
        int targetY = blockPos.getY();

        return targetY <= zombieY + 1 && targetY >= zombieY - 1;
    }
}