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
                    }).build());
}
