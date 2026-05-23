package ru.imaginaerum.wd.common.init.tab;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredRegister;
import ru.imaginaerum.wd.WD;
import ru.imaginaerum.wd.common.init.items.ItemsWD;

import java.util.function.Supplier;

public class TabsWD {
    public static final DeferredRegister<CreativeModeTab> TABS_WD =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, WD.MOD_ID);

    public static final Supplier<CreativeModeTab> WD_TAB = TABS_WD.register("tabs_wd",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ItemsWD.JAR.get()))
                    .title(Component.translatable("creativetab.wd.tabs"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ItemsWD.COASTAL_STEEP);
                        output.accept(ItemsWD.COASTAL_STEEP_FIBERS);
                        output.accept(ItemsWD.COASTAL_STEEP_FLOWER);
                        output.accept(ItemsWD.POISON_BERRY);
                        output.accept(ItemsWD.CHARMING_BERRIES);
                        output.accept(ItemsWD.FIRE_STEM);
                        output.accept(ItemsWD.FREEZE_BERRIES);
                        output.accept(ItemsWD.ROSE_OF_GHOSTY_TEARS);
                        output.accept(ItemsWD.ROSE_OF_THE_MURDERER);
                        output.accept(ItemsWD.JAR);
                        output.accept(ItemsWD.POISON_BERRY_JAM);
                        output.accept(ItemsWD.CHARMING_JAM);
                        output.accept(ItemsWD.APPLE_JAM);
                        output.accept(ItemsWD.SWEET_JAM);
                        output.accept(ItemsWD.FREEZE_JAM);
                        output.accept(ItemsWD.JAM_TONIC);
                        output.accept(ItemsWD.JAM_INVISIBILITY);
                        output.accept(ItemsWD.LEVITAN_JAM);
                        output.accept(ItemsWD.GLOWING_JAM);
                        output.accept(ItemsWD.SUGAR_REFINED);
                        output.accept(ItemsWD.RAW_WAFFLES);
                        output.accept(ItemsWD.WAFFLES);
                        output.accept(ItemsWD.APPLE_WAFFLES);
                        output.accept(ItemsWD.BERRIES_WAFFLES);
                        output.accept(ItemsWD.GLOW_BERRIES_WAFFLES);
                        output.accept(ItemsWD.ICE_WAFFLES);
                        output.accept(ItemsWD.CHARMING_WAFFLES);
                        output.accept(ItemsWD.POISON_WAFFLES);
                        output.accept(ItemsWD.SPATIAL_ORCHID);
                        output.accept(ItemsWD.THE_PILLAGERS_CHEST);
                        output.accept(ItemsWD.GOLDEN_CHEST_KING_PILLAGER);
                        output.accept(ItemsWD.THE_PILLAGERS_KEY);
                        output.accept(ItemsWD.THE_KING_PILLAGERS_KEY);
                        output.accept(ItemsWD.A_DROP_OF_LOVE);
                        output.accept(ItemsWD.SPARKLING_POLLEN);
                        output.accept(ItemsWD.GRASS_BONE_MEAL);
                        output.accept(ItemsWD.CRIMSON_BONE_MEAL);
                        output.accept(ItemsWD.WARPED_BONE_MEAL);
                        output.accept(ItemsWD.MYCELIUM_BONE_MEAL);
                        output.accept(ItemsWD.DRAGOLIT_INGOT);
                        output.accept(ItemsWD.CLEAR_DRAGOLIT_NUGGET);
                        output.accept(ItemsWD.STRANGE_SCRAP);
                        output.accept(ItemsWD.HEALING_DEW);
                        output.accept(ItemsWD.NETHER_GROG);
                    }).build());
}
