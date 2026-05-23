package ru.imaginaerum.wd.common.init.effects;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import ru.imaginaerum.wd.WD;

public class EffectsWD {

    // [ИЗМЕНЕНО] ForgeRegistries.MOB_EFFECTS → Registries.MOB_EFFECT (ванильный реестр)
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(Registries.MOB_EFFECT, WD.MOD_ID);

    // [ИЗМЕНЕНО] RegistryObject → DeferredHolder
    public static final DeferredHolder<MobEffect, FreezeEffect> FREEZE =
            MOB_EFFECTS.register("freeze",
                    () -> new FreezeEffect(MobEffectCategory.HARMFUL, 0xADD8E6));
}