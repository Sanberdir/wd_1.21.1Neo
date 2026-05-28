package ru.imaginaerum.wd.server.events;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.EntityHitResult;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import ru.imaginaerum.wd.common.init.items.ItemsWD;
import ru.imaginaerum.wd.common.init.patricles.ModParticles;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class HitEntityHandler {

    private static final Map<String, JsonObject> MOB_DROPS = new HashMap<>();

    public static void handleHitEntity(EntityHitResult hitResult, ServerLevel level) {
        Entity entity = hitResult.getEntity();
        ResourceLocation entityId = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType());

        if (MOB_DROPS.isEmpty()) {
            loadMobDropsConfig();
        }

        JsonObject mobConfig = MOB_DROPS.get(entityId.toString());

        if (entity instanceof Player player) {
            FoodData foodData = player.getFoodData();

            if (foodData.getFoodLevel() >= 20) {
                player.hurt(player.damageSources().magic(), 4.0f);
                level.playSound(
                        null,
                        player.getX(),
                        player.getY(),
                        player.getZ(),
                        SoundEvents.PLAYER_HURT,
                        SoundSource.PLAYERS,
                        1.0f,
                        1.0f
                );
            } else {
                foodData.setFoodLevel(Math.min(foodData.getFoodLevel() + 5, 20));
            }

            spawnEffects(level, entity);
            return;
        }

        if (mobConfig != null) {
            spawnEffects(level, entity);

            RandomSource random = level.random;
            double x = entity.getX();
            double y = entity.getY();
            double z = entity.getZ();

            if (random.nextFloat() <= 0.3f) {
                Item sparklingPollen = ItemsWD.SPARKLING_POLLEN.get();
                int count = 1 + random.nextInt(2);
                ItemStack stack = new ItemStack(sparklingPollen, count);
                level.addFreshEntity(new ItemEntity(level, x, y, z, stack));
            }

            if (mobConfig.has("drops")) {
                processItemDrops(level, entity, mobConfig.getAsJsonObject("drops"));
            }

            if (mobConfig.has("experience")) {
                processExperienceDrops(level, entity, mobConfig.getAsJsonObject("experience"));
            }

            entity.discard();
        } else {
            transformMob(level, entity);
        }
    }

    private static void transformMob(ServerLevel level, Entity entity) {
        if (entity.getType() == EntityType.VILLAGER
                || entity.getType() == EntityType.PILLAGER
                || entity.getType() == EntityType.VINDICATOR
                || entity.getType() == EntityType.EVOKER
                || entity.getType() == EntityType.ILLUSIONER
                || entity.getType() == EntityType.WANDERING_TRADER) {

            Frog frog = EntityType.FROG.create(level);
            if (frog != null) {
                frog.moveTo(entity.position());
                level.addFreshEntity(frog);
                entity.discard();
            }
        } else if (!(entity instanceof Player)) {
            Chicken chicken = EntityType.CHICKEN.create(level);
            if (chicken != null) {
                chicken.moveTo(entity.position());
                level.addFreshEntity(chicken);
                entity.discard();
            }
        }

        spawnEffects(level, entity);
    }

    private static void spawnEffects(ServerLevel level, Entity entity) {
        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();

        level.sendParticles(
                ModParticles.ROBIN_STAR_PARTICLES_PROJECTILE.get(),
                x, y, z,
                36, 0.5, 0.5, 0.5, 0.05f
        );

        if (entity instanceof Bee) {
            level.playSound(null, x, y, z, SoundEvents.LLAMA_SPIT, SoundSource.NEUTRAL, 1.0F, 1.0F);
        } else {
            level.playSound(null, x, y, z, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 1.0F, 1.0F);
        }
    }

    private static void processItemDrops(ServerLevel level, Entity entity, JsonObject dropsConfig) {
        RandomSource random = level.random;
        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();

        for (var entry : dropsConfig.entrySet()) {
            JsonObject itemConfig = entry.getValue().getAsJsonObject();

            String itemId = itemConfig.get("item").getAsString();
            int minCount = itemConfig.has("min_count") ? itemConfig.get("min_count").getAsInt() : 1;
            int maxCount = itemConfig.has("max_count") ? itemConfig.get("max_count").getAsInt() : minCount;
            float chance = itemConfig.has("chance") ? itemConfig.get("chance").getAsFloat() : 1.0f;

            if (random.nextFloat() <= chance) {
                int count = minCount + random.nextInt(maxCount - minCount + 1);
                Item item = resolveItem(itemId);
                if (item != null) {
                    ItemStack stack = new ItemStack(item, count);
                    level.addFreshEntity(new ItemEntity(level, x, y, z, stack));
                }
            }
        }
    }

    private static void processExperienceDrops(ServerLevel level, Entity entity, JsonObject xpConfig) {
        RandomSource random = level.random;
        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();

        int minXp = xpConfig.has("min") ? xpConfig.get("min").getAsInt() : 0;
        int maxXp = xpConfig.has("max") ? xpConfig.get("max").getAsInt() : minXp;
        float chance = xpConfig.has("chance") ? xpConfig.get("chance").getAsFloat() : 1.0f;

        if (random.nextFloat() <= chance && maxXp > 0) {
            int xpAmount = minXp + random.nextInt(maxXp - minXp + 1);
            level.addFreshEntity(new ExperienceOrb(level, x, y, z, xpAmount));
        }
    }

    private static void loadMobDropsConfig() {
        try {
            var server = ServerLifecycleHooks.getCurrentServer();
            if (server == null) {
                return;
            }

            ResourceManager resourceManager = server.getResourceManager();

            Map<ResourceLocation, Resource> resources = resourceManager.listResources(
                    "hit_mob",
                    location -> location.getPath().endsWith(".json")
            );

            for (Map.Entry<ResourceLocation, Resource> entry : resources.entrySet()) {
                try (InputStream inputStream = entry.getValue().open()) {
                    JsonObject config = JsonParser.parseReader(new InputStreamReader(inputStream)).getAsJsonObject();

                    for (Map.Entry<String, com.google.gson.JsonElement> mobEntry : config.entrySet()) {
                        MOB_DROPS.put(mobEntry.getKey(), mobEntry.getValue().getAsJsonObject());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Item resolveItem(String itemId) {
        try {
            ResourceLocation id = ResourceLocation.parse(itemId);
            return BuiltInRegistries.ITEM.get(id);
        } catch (Exception ignored) {
            return null;
        }
    }
}