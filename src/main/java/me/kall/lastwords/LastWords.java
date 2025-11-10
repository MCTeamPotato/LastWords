package me.kall.lastwords;

import me.kall.lastwords.ext.DongZhuo;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.common.Mod;

@Mod(LastWords.MOD_ID)
public final class LastWords {
    public static final String MOD_ID = "lastwords";
    public static final String MOD_NAME = "LastWords";

    public LastWords() {
        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST, (LivingDamageEvent event) -> {
            if (event.isCanceled()) return;
            LivingEntity attacked = event.getEntity();
            Entity attacker = event.getSource().getEntity();
            if (attacker instanceof Player lvBu && event.getAmount() >= attacked.getHealth()) {
                if (attacked instanceof Player || attacked.isAlliedTo(lvBu)) {
                    if (attacked.level().isClientSide()) return;
                    if (((DongZhuo)attacked).lastWords$said()) {
                        ((DongZhuo)attacked).lastWords$setSaid(false);
                        return;
                    }

                    Component lvBuWords = Component.literal("<" + lvBu.getName().getString() + "> " + I18n.get("lv_bu.last_words"));
                    Component dongZhuoWords = Component.literal("<" + attacked.getName().getString() + "> " + I18n.get("dong_zhuo.last_words"));

                    lvBu.displayClientMessage(lvBuWords, false);
                    lvBu.displayClientMessage(dongZhuoWords, false);

                    if (attacked instanceof Player dongZhuo) {
                        dongZhuo.displayClientMessage(lvBuWords, false);
                        dongZhuo.displayClientMessage(dongZhuoWords, false);
                    }

                    event.setAmount(attacked.getHealth() - 1F);
                    if (Config.EFFECT.get()) {
                        attacked.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, Config.EFFECT_DURATION.get(), Config.EFFECT_LEVEL.get(), false, false, false));
                        attacked.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, Config.EFFECT_DURATION.get(), Config.EFFECT_LEVEL.get(), false, false, false));
                    }
                    ((DongZhuo) attacked).lastWords$setSaid(true);
                }
            }
        });
    }

    private static final class Config {
        static final ForgeConfigSpec INSTANCE;
        static final ForgeConfigSpec.BooleanValue EFFECT;
        static final ForgeConfigSpec.IntValue EFFECT_DURATION, EFFECT_LEVEL;

        static {
            ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
            builder.push(MOD_NAME);
            EFFECT = builder.comment("Whether to apply weakness and slowness effect on Dong Zhuo when saying last words.").define("WeakAndSlow", true);
            EFFECT_DURATION = builder.comment("-1 means infinite effect duration").defineInRange("EffectDuration", -1, -1, Integer.MAX_VALUE);
            EFFECT_LEVEL = builder.defineInRange("EffectAmplifier", 120, 0, 120);
            builder.pop();
            INSTANCE = builder.build();
        }
    }
}
