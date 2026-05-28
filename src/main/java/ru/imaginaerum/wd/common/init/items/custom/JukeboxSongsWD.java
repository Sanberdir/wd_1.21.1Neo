package ru.imaginaerum.wd.common.init.items.custom;

import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.JukeboxSong;
import ru.imaginaerum.wd.common.sounds.CustomSoundEvents;

public interface JukeboxSongsWD {
    ResourceKey<JukeboxSong> THE_LONG_WAY_HOME = create("music_disk_1");
    ResourceKey<JukeboxSong> SO_LITTLE_BUT_SO_BIG = create("music_disk_2");
    ResourceKey<JukeboxSong> WANDERING_MINSTREL = create("music_disk_3");


    private static ResourceKey<JukeboxSong> create(String name) {
        return ResourceKey.create(Registries.JUKEBOX_SONG, ResourceLocation.withDefaultNamespace(name));
    }

    private static void register(BootstrapContext<JukeboxSong> context, ResourceKey<JukeboxSong> key, Holder.Reference<SoundEvent> soundEvent, int lengthInSeconds, int comparatorOutput) {
        context.register(key, new JukeboxSong(soundEvent, Component.translatable(Util.makeDescriptionId("jukebox_song", key.location())), (float)lengthInSeconds, comparatorOutput));
    }

    static void bootstrap(BootstrapContext<JukeboxSong> context) {
        register(context, THE_LONG_WAY_HOME,
                context.lookup(Registries.SOUND_EVENT)
                        .getOrThrow(CustomSoundEvents.REZOLV_12_THE_LONG_WAY_HOME.getKey()),
                255, 8);
        register(context, SO_LITTLE_BUT_SO_BIG,
                context.lookup(Registries.SOUND_EVENT)
                        .getOrThrow(CustomSoundEvents.REZOLV_15_SO_LITTLE_BUT_SO_BIG.getKey()),
                201, 8);
        register(context, WANDERING_MINSTREL,
                context.lookup(Registries.SOUND_EVENT)
                        .getOrThrow(CustomSoundEvents.REZOLV_4_WANDERING_MINSTREL.getKey()),
                174, 8);
    }
}
