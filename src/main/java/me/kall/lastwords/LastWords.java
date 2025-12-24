package me.kall.lastwords;

import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

@Mod(LastWords.MOD_ID)
public final class LastWords {
    public static final Supplier<String> LV_BU = () -> Component.translatable("lv_bu.last_words").getString();
    public static final Supplier<String> DONG_ZHUO = () -> Component.translatable("dong_zhuo.last_words").getString();

    public static final String MOD_ID = "lastwords";
    public static final String MOD_NAME = "LastWords";

    public LastWords(IEventBus modBus, Dist dist, @NotNull ModContainer container) {
        container.registerConfig(ModConfig.Type.COMMON, Config.INSTANCE);
    }

    public static final class Config {
        public static final ModConfigSpec INSTANCE;
        public static final ModConfigSpec.BooleanValue EFFECT;
        public static final ModConfigSpec.IntValue EFFECT_DURATION, EFFECT_LEVEL;

        static {
            ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
            builder.push(MOD_NAME);
            EFFECT = builder.comment("Whether to apply weakness and slowness effect on Dong Zhuo when saying last words.").define("WeakAndSlow", true);
            EFFECT_DURATION = builder.comment("-1 means infinite effect duration").defineInRange("EffectDuration", 200, 200, Integer.MAX_VALUE);
            EFFECT_LEVEL = builder.defineInRange("EffectAmplifier", 120, 0, 120);
            builder.pop();
            INSTANCE = builder.build();
        }
    }
}
