package ru.imaginaerum.wd.common.init.sounds;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import ru.imaginaerum.wd.WD;

public class CustomSoundEvents {

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(Registries.SOUND_EVENT, WD.MOD_ID);

    public static final DeferredHolder<SoundEvent, SoundEvent> OPEN_CHESTS =
            register("open_chests");

    public static final DeferredHolder<SoundEvent, SoundEvent> NYAMNYAM =
            register("nyamnyam");

    public static final DeferredHolder<SoundEvent, SoundEvent> GOLDEN_FLOWER =
            register("golden_flower");

    public static final DeferredHolder<SoundEvent, SoundEvent> NYAMNYAM_END =
            register("nyamnyam_end");

    public static final DeferredHolder<SoundEvent, SoundEvent> FLIES =
            register("flies");

    public static final DeferredHolder<SoundEvent, SoundEvent> ROBIN_STICK =
            register("robin_stick");

    public static final DeferredHolder<SoundEvent, SoundEvent> REZOLV_12_THE_LONG_WAY_HOME =
            register("rezolv_12_the_long_way_home");

    public static final DeferredHolder<SoundEvent, SoundEvent> REZOLV_15_SO_LITTLE_BUT_SO_BIG =
            register("rezolv_15_so_little_but_so_big");

    public static final DeferredHolder<SoundEvent, SoundEvent> REZOLV_4_WANDERING_MINSTREL =
            register("rezolv_4_wandering_minstrel");

    private static DeferredHolder<SoundEvent, SoundEvent> register(String name) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(WD.MOD_ID, name);

        return SOUND_EVENTS.register(name,
                () -> SoundEvent.createVariableRangeEvent(id));
    }
}