package ru.imaginaerum.wd.server.events;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import ru.imaginaerum.wd.common.init.blocks.BlocksWD;
import ru.imaginaerum.wd.common.init.blocks.custom.GoldenRose;
import ru.imaginaerum.wd.common.init.blocks.custom.RottenPie;
import ru.imaginaerum.wd.common.init.items.ItemsWD;
import ru.imaginaerum.wd.common.init.patricles.ModParticles;
import ru.imaginaerum.wd.common.sounds.CustomSoundEvents;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class HitBlockStarBall {

    public static void hitBlock(ServerLevel level, BlockPos pos, double x, double y, double z) {

        BlockState state = level.getBlockState(pos);
        ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(state.getBlock());
        if (blockId == null) return;

        // =====================================================
        // SPECIAL BLOCKS
        // =====================================================

        if (state.getBlock() == BlocksWD.A_BLOCK_OF_SPARKLING_POLLEN.get()) {
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            level.explode(null, x, y, z, 10, Level.ExplosionInteraction.TNT);

            for (int i = 0; i < 10; i++) {
                level.sendParticles(
                        ModParticles.ROBIN_STAR_PARTICLES_PROJECTILE.get(),
                        pos.getX(), pos.getY(), pos.getZ(),
                        350, 5, 5, 5, 0.7
                );
            }
            return;
        }

        if (state.getBlock() == BlocksWD.WIZARD_PIE.get()) {
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            level.explode(null, x, y, z, 4, Level.ExplosionInteraction.TNT);

            for (int i = 0; i < 10; i++) {
                level.sendParticles(
                        ModParticles.ROBIN_STAR_PARTICLES_PROJECTILE.get(),
                        pos.getX(), pos.getY(), pos.getZ(),
                        100, 2, 2, 2, 0.3
                );
            }
            return;
        }

        if (state.getBlock() == BlocksWD.MEADOW_GOLDEN_FLOWER.get()
                && state.getValue(GoldenRose.ACTIVE)) {

            double radius = 5.0;

            List<Entity> entities = level.getEntities(null,
                    new AABB(x - radius, y - radius, z - radius,
                            x + radius, y + radius, z + radius));

            for (Entity e : entities) {
                if (e instanceof ZombieVillager zVillager) {

                    Villager villager = EntityType.VILLAGER.create(level);

                    if (villager != null) {
                        villager.moveTo(zVillager.position());
                        level.addFreshEntity(villager);
                        zVillager.discard();
                    }
                }
            }

            level.sendParticles(
                    ModParticles.GOLDEN_FLOWER_PARTICLES.get(),
                    pos.getX(), pos.getY(), pos.getZ(),
                    1, 0, 0, 0, 0.3
            );

            level.playSound(null, pos,
                    CustomSoundEvents.GOLDEN_FLOWER.get(),
                    SoundSource.BLOCKS, 1f, 1f);

            level.setBlock(pos, state.setValue(GoldenRose.ACTIVE, false), 3);
            return;
        }

        if (state.getBlock() == Blocks.VINE) {
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);

            Creeper creeper = EntityType.CREEPER.create(level);
            if (creeper != null) {
                creeper.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0, 0);
                level.addFreshEntity(creeper);

                level.playSound(null, pos,
                        SoundEvents.CREEPER_PRIMED,
                        SoundSource.BLOCKS, 1f, 1f);
            }
            return;
        }

        if (state.getBlock() == BlocksWD.ROTTEN_PIE.get()) {

            int stage = state.getValue(RottenPie.STAGE);
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);

            int zombieCount = switch (stage) {
                case 0 -> 8;
                case 1 -> 6;
                case 2 -> 4;
                default -> 2;
            };

            for (int i = 0; i < zombieCount; i++) {

                Entity entity = level.random.nextDouble() < 0.1
                        ? EntityType.ZOMBIE_VILLAGER.create(level)
                        : EntityType.ZOMBIE.create(level);

                if (entity instanceof Mob mob) {

                    double ox = (level.random.nextDouble() - 0.5) * 1.5;
                    double oz = (level.random.nextDouble() - 0.5) * 1.5;

                    mob.moveTo(pos.getX() + 0.5 + ox,
                            pos.getY(),
                            pos.getZ() + 0.5 + oz,
                            level.random.nextFloat() * 360, 0);

                    level.addFreshEntity(mob);
                }
            }

            level.playSound(null, pos,
                    SoundEvents.ZOMBIE_DEATH,
                    SoundSource.BLOCKS, 1f, 1f);

            return;
        }

        // =====================================================
        // GENERIC CONFIG
        // =====================================================

        JsonObject config = loadConfig();
        if (config == null || !config.has(blockId.toString())) return;

        JsonObject blockConfig = config.getAsJsonObject(blockId.toString());

        playBlockBreakAnimation(level, pos, state);
        spawnParticles(level, pos);
        dropItems(level, pos, blockConfig);

        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
    }

    // =====================================================

    private static void playBlockBreakAnimation(ServerLevel level, BlockPos pos, BlockState state) {
        level.levelEvent(2001, pos, Block.getId(state));
    }

    private static void spawnParticles(ServerLevel level, BlockPos pos) {
        level.sendParticles(
                ModParticles.ROBIN_STAR_PARTICLES_PROJECTILE.get(),
                pos.getX(), pos.getY(), pos.getZ(),
                36, 0.5, 0.5, 0.5, 0.05
        );
    }

    private static void dropItems(ServerLevel level, BlockPos pos, JsonObject config) {

        if (!config.has("drops")) return;

        Random random = new Random();

        if (random.nextFloat() <= 0.30f) {
            Block.popResource(level, pos,
                    new ItemStack(ItemsWD.SPARKLING_POLLEN.get()));
        }

        for (var el : config.getAsJsonArray("drops")) {

            JsonObject obj = el.getAsJsonObject();

            ResourceLocation id = ResourceLocation.parse(obj.get("item").getAsString());

            int min = obj.has("min_count") ? obj.get("min_count").getAsInt() : 1;
            int max = obj.has("max_count") ? obj.get("max_count").getAsInt() : min;

            int count = random.nextInt(max - min + 1) + min;
            float chance = obj.has("chance") ? obj.get("chance").getAsFloat() : 1f;

            if (random.nextFloat() <= chance) {
                ItemStack stack = new ItemStack(
                        BuiltInRegistries.ITEM.get(id),
                        count
                );

                Block.popResource(level, pos, stack);
            }
        }
    }

    // =====================================================

    private static JsonObject loadConfig() {

        JsonObject merged = new JsonObject();

        try {
            var server = ServerLifecycleHooks.getCurrentServer();
            if (server == null) return null;

            ResourceManager manager = server.getResourceManager();

            Map<ResourceLocation, Resource> resources =
                    manager.listResources("hit_block",
                            rl -> rl.getPath().endsWith(".json"));

            for (var entry : resources.entrySet()) {

                try (InputStream in = entry.getValue().open()) {

                    JsonObject obj = JsonParser.parseReader(
                            new InputStreamReader(in)
                    ).getAsJsonObject();

                    obj.entrySet().forEach(e ->
                            merged.add(e.getKey(), e.getValue()));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return merged;
    }
}