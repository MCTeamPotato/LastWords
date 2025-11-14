package me.kall.lastwords;

import me.kall.lastwords.config.JsonConfig;

public final class LastWords {
    public static final String MOD_ID = "lastwords";

    public static JsonConfig CONFIG;
    public static boolean effect;
    public static int duration, amplifier;

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
