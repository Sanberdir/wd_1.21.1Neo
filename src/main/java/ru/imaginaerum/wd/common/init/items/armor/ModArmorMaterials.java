package ru.imaginaerum.wd.common.init.items.armor;

import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.registries.DeferredRegister;
import ru.imaginaerum.wd.WD;
import ru.imaginaerum.wd.common.init.items.ItemsWD;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class ModArmorMaterials {

    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS =
            DeferredRegister.create(Registries.ARMOR_MATERIAL, WD.MOD_ID);

    public static final Holder<ArmorMaterial> MAGIC = ARMOR_MATERIALS.register("magic", () ->
            new ArmorMaterial(
                    makeDefenseMap(3, 7, 5, 4),
                    25,
                    SoundEvents.ARMOR_EQUIP_LEATHER,
                    () -> Ingredient.of(ItemsWD.MAGIC_HAT.get()),
                    List.of(new ArmorMaterial.Layer(
                            ResourceLocation.fromNamespaceAndPath(WD.MOD_ID, "magic")
                    )),
                    1f,
                    0f
            )
    );

    public static final Holder<ArmorMaterial> MAGIC_JAM = ARMOR_MATERIALS.register("magic_jam", () ->
            new ArmorMaterial(
                    makeDefenseMap(3, 7, 5, 4),
                    25,
                    SoundEvents.ARMOR_EQUIP_LEATHER,
                    () -> Ingredient.of(ItemsWD.MAGIC_HAT_JAM.get()),
                    List.of(new ArmorMaterial.Layer(
                            ResourceLocation.fromNamespaceAndPath(WD.MOD_ID, "magic_jam")
                    )),
                    1f,
                    0f
            )
    );
    public static final Holder<ArmorMaterial> ELYTRA = ARMOR_MATERIALS.register("elytra", () ->
            new ArmorMaterial(
                    makeDefenseMap(3, 8, 6, 3),
                    28,
                    SoundEvents.ARMOR_EQUIP_LEATHER,
                    () -> Ingredient.of(ItemsWD.MAG_ELYTRA.get()),
                    List.of(new ArmorMaterial.Layer(
                            ResourceLocation.fromNamespaceAndPath(WD.MOD_ID, "elytra")
                    )),
                    1f,
                    0f
            )
    );

    private static Map<ArmorItem.Type, Integer> makeDefenseMap(int boots, int leggings, int chestplate, int helmet) {
        EnumMap<ArmorItem.Type, Integer> map = new EnumMap<>(ArmorItem.Type.class);
        map.put(ArmorItem.Type.BOOTS, boots);
        map.put(ArmorItem.Type.LEGGINGS, leggings);
        map.put(ArmorItem.Type.CHESTPLATE, chestplate);
        map.put(ArmorItem.Type.HELMET, helmet);
        return map;
    }
}