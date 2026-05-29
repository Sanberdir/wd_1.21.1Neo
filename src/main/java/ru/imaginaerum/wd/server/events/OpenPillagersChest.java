package ru.imaginaerum.wd.server.events;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import ru.imaginaerum.wd.WD;
import ru.imaginaerum.wd.common.init.blocks.BlocksWD;
import ru.imaginaerum.wd.common.init.items.ItemsWD;
import ru.imaginaerum.wd.common.init.sounds.CustomSoundEvents;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Random;

@EventBusSubscriber(modid = WD.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class OpenPillagersChest {

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {

        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        Level level = player.level();
        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();

        if (block != BlocksWD.THE_PILLAGERS_CHEST.get()) return;

        ItemStack main = player.getMainHandItem();
        ItemStack off = player.getOffhandItem();

        boolean hasKey =
                main.is(ItemsWD.THE_PILLAGERS_KEY.get()) ||
                        off.is(ItemsWD.THE_PILLAGERS_KEY.get());

        if (!hasKey) return;

        if (main.is(ItemsWD.THE_PILLAGERS_KEY.get())) {
            main.shrink(1);
        } else {
            off.shrink(1);
        }

        level.destroyBlock(pos, false);
        event.setCanceled(true);

        level.playSound(
                null,
                pos,
                CustomSoundEvents.OPEN_CHESTS.get(),
                SoundSource.BLOCKS,
                1f,
                1f
        );

        JsonObject config = loadConfig();
        JsonElement items = config.get("items");
        Random rand = new Random();

        if (items != null && items.isJsonArray()) {
            for (JsonElement el : items.getAsJsonArray()) {

                JsonObject obj = el.getAsJsonObject();

                if (!obj.has("min") || !obj.has("max") || !obj.has("chance") || !obj.has("item")) {
                    continue;
                }

                int min = obj.get("min").getAsInt();
                int max = obj.get("max").getAsInt();
                int chance = obj.get("chance").getAsInt();

                if (rand.nextInt(100) >= chance) continue;

                int amount = rand.nextInt(max - min + 1) + min;

                ResourceLocation id = ResourceLocation.parse(obj.get("item").getAsString());
                Item item = BuiltInRegistries.ITEM.get(id);

                if (item == null) continue;

                dropItem(level, pos.above(), new ItemStack(item, amount));
            }
        }
    }

    private static void dropItem(Level level, BlockPos pos, ItemStack stack) {
        if (level.isClientSide) return;

        ItemEntity entity = new ItemEntity(
                level,
                pos.getX() + 0.5,
                pos.getY() + 0.5,
                pos.getZ() + 0.5,
                stack
        );

        entity.setDefaultPickUpDelay();
        level.addFreshEntity(entity);
    }

    private static JsonObject loadConfig() {

        JsonObject merged = new JsonObject();

        try {
            var server = ServerLifecycleHooks.getCurrentServer();
            var manager = server.getResourceManager();

            var folder = ResourceLocation.fromNamespaceAndPath(
                    WD.MOD_ID,
                    "loot_table/pillager_chest"
            );

            Map<ResourceLocation, Resource> resources =
                    manager.listResources(
                            folder.getPath(),
                            path -> path.getPath().endsWith(".json")
                    );

            for (Resource resource : resources.values()) {
                try (InputStream in = resource.open()) {
                    JsonObject obj = JsonParser.parseReader(new InputStreamReader(in))
                            .getAsJsonObject();

                    for (var entry : obj.entrySet()) {
                        merged.add(entry.getKey(), entry.getValue());
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return merged;
    }
}