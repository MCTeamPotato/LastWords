package me.kall.lastwords;

import me.kall.lastwords.config.JsonConfig;
import net.minecraft.network.chat.Component;

import java.util.function.Supplier;

public final class LastWords {
    public static final String MOD_ID = "lastwords";

    public static JsonConfig CONFIG;
    public static boolean effect;
    public static int duration, amplifier;

    public static final Supplier<String> LV_BU = () -> Component.translatable("lv_bu.last_words").getString();
    public static final Supplier<String> DONG_ZHUO = () -> Component.translatable("dong_zhuo.last_words").getString();

    public static void init() {
        CONFIG = JsonConfig.create(MOD_ID, "1")
                .put("EffectApplicable", true)
                .put("EffectDuration", 200)
                .put("EffectAmplifier", 120)
                .initialize();
        effect = CONFIG.getBoolean("EffectApplicable");
        duration = CONFIG.getInt("EffectDuration");
        amplifier = CONFIG.getInt("EffectAmplifier");
    }
}
