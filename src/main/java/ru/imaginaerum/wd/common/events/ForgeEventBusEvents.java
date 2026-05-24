package ru.imaginaerum.wd.common.events;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import ru.imaginaerum.wd.WD;
import ru.imaginaerum.wd.common.init.blocks.BlocksWD;
import ru.imaginaerum.wd.common.init.blocks.custom.BrightPepperSeeds;
import ru.imaginaerum.wd.common.init.blocks.custom.DragolitBlock;
import ru.imaginaerum.wd.common.init.blocks.custom.DragolitGrid;
import ru.imaginaerum.wd.common.init.blocks.custom.MagicSoilFarmland;
import ru.imaginaerum.wd.common.init.blocks.custom.registry_blocks_plaints.MagicSoilFarmlandData;
import ru.imaginaerum.wd.common.init.blocks.custom.registry_blocks_plaints.PepperRegistry;

@EventBusSubscriber(modid = WD.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class ForgeEventBusEvents {

    private static long lastDayTime = -1;
    private static int tickCounter = 0;
    private static final int CHECK_INTERVAL = 20;

    // === СИСТЕМА ФЕРМЫ ===
    @SubscribeEvent
    public static void onLevelTickFarmland(LevelTickEvent.Post event) {
        if (event.getLevel().isClientSide()) return;

        long timeOfDay = event.getLevel().getDayTime() % 24000;
        if (lastDayTime > timeOfDay && event.getLevel() instanceof ServerLevel serverLevel) {
            dryAllSoil(serverLevel);
            upgradeStagePepper(serverLevel);
        }
        lastDayTime = timeOfDay;
    }

    @SubscribeEvent
    public static void tickMoistSoil(ServerTickEvent.Post event) {
        tickCounter++;
        if (tickCounter < CHECK_INTERVAL) return;
        tickCounter = 0;

        for (ServerLevel level : event.getServer().getAllLevels()) {
            if (!level.isRaining()) continue;
            MagicSoilFarmlandData data = MagicSoilFarmlandData.get(level);

            for (BlockPos pos : data.getFarmlands()) {
                if (!level.hasChunkAt(pos)) continue;

                BlockState state = level.getBlockState(pos);
                if (state.is(BlocksWD.MAGIC_SOIL_FARMLAND.get()) && !state.getValue(MagicSoilFarmland.MOIST)) {
                    level.setBlock(pos, state.setValue(MagicSoilFarmland.MOIST, true), 2);
                }
            }
        }
    }

    private static void dryAllSoil(ServerLevel level) {
        MagicSoilFarmlandData data = MagicSoilFarmlandData.get(level);

        for (BlockPos pos : data.getFarmlands()) {
            if (!level.hasChunkAt(pos)) continue;

            BlockState state = level.getBlockState(pos);
            if (state.is(BlocksWD.MAGIC_SOIL_FARMLAND.get())) {
                if (state.getValue(MagicSoilFarmland.MOIST)) {
                    level.setBlock(pos, state.setValue(MagicSoilFarmland.MOIST, false), 3);
                } else {
                    level.setBlock(pos, BlocksWD.MAGIC_SOIL.get().defaultBlockState(), 3);
                    data.remove(pos);
                }
            } else {
                data.remove(pos);
            }
        }
    }

    private static void upgradeStagePepper(ServerLevel level) {
        PepperRegistry data = PepperRegistry.get(level);

        for (BlockPos pos : data.getPepperBlocks()) {
            if (!level.hasChunkAt(pos)) continue;

            BlockState state = level.getBlockState(pos);
            if (state.is(BlocksWD.BRIGHT_PEPPER_SEEDS.get())) {
                int currentStage = state.getValue(BrightPepperSeeds.STAGE);
                if (currentStage < 12) {
                    level.setBlock(pos, state.setValue(BrightPepperSeeds.STAGE, currentStage + 1), 2);
                }
            } else {
                data.remove(pos);
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        DragolitBlock.onEntityDeath(event.getEntity(), event.getSource());
        DragolitGrid.onEntityDeath(event.getEntity(), event.getSource());
    }
}