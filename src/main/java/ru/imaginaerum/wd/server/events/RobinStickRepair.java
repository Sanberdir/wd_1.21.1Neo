package ru.imaginaerum.wd.server.events;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.CustomModelData;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import ru.imaginaerum.wd.common.init.items.ItemsWD;

@EventBusSubscriber(modid = "wd") // укажи свой modid
public class RobinStickRepair {

    @SubscribeEvent
    public static void onPlayerRightClick(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();
        ItemStack mainHandItem = player.getMainHandItem();
        ItemStack offHandItem = player.getOffhandItem();

        if (isSoulStoneCustomModelData(mainHandItem) && isRobinStick(offHandItem)) {
            setSoulStoneCustomModelData(mainHandItem, 0);
            removeSoulStoneNBT(mainHandItem);
            repairRobinStick(offHandItem);
            event.setCanceled(true);
        } else if (isSoulStoneCustomModelData(offHandItem) && isRobinStick(mainHandItem)) {
            setSoulStoneCustomModelData(offHandItem, 0);
            removeSoulStoneNBT(offHandItem);
            repairRobinStick(mainHandItem);
            event.setCanceled(true);
        }
    }

    private static boolean isSoulStoneCustomModelData(ItemStack itemStack) {
        return itemStack.getItem() == ItemsWD.SOUL_STONE.get() && getCustomModelData(itemStack) == 1;
    }

    private static boolean isRobinStick(ItemStack itemStack) {
        return itemStack.getItem() == ItemsWD.ROBIN_STICK.get();
    }

    private static int getCustomModelData(ItemStack itemStack) {
        // В 1.21.1 CustomModelData — DataComponent с полем value()
        CustomModelData customModelData = itemStack.get(DataComponents.CUSTOM_MODEL_DATA);
        return customModelData != null ? customModelData.value() : 0;
    }

    private static void setSoulStoneCustomModelData(ItemStack itemStack, int value) {
        if (value == 0) {
            // Убираем компонент полностью, если значение 0
            itemStack.remove(DataComponents.CUSTOM_MODEL_DATA);
        } else {
            itemStack.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(value));
        }
    }

    private static void repairRobinStick(ItemStack itemStack) {
        itemStack.setDamageValue(0);
    }

    private static void removeSoulStoneNBT(ItemStack itemStack) {
        // В 1.21.1 кастомный NBT хранится в DataComponents.CUSTOM_DATA
        CustomData customData = itemStack.get(DataComponents.CUSTOM_DATA);
        if (customData == null) return;

        // update() позволяет безопасно изменить тег
        itemStack.update(DataComponents.CUSTOM_DATA, CustomData.EMPTY, existing -> {
            CompoundTag tag = existing.copyTag();
            tag.remove("entity_type");
            tag.remove("entity_name");
            return CustomData.of(tag);
        });
    }
}