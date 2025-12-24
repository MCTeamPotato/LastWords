package me.kall.lastwords;

import me.kall.lastwords.ext.DongZhuo;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

import java.util.function.Supplier;

@Mod(LastWords.MOD_ID)
public final class LastWords {
    private static final Supplier<String> LV_BU = () -> new TranslatableComponent("lv_bu.last_words").getString();
    private static final Supplier<String> DONG_ZHUO = () -> new TranslatableComponent("dong_zhuo.last_words").getString();

    public static final String MOD_ID = "lastwords";
    public static final String MOD_NAME = "LastWords";

    public LastWords() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.INSTANCE);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST, (LivingDamageEvent event) -> {
            if (event.isCanceled()) return;
            LivingEntity attacked = event.getEntityLiving();
            Entity attacker = event.getSource().getEntity();
            if (attacker instanceof Player && event.getAmount() >= attacked.getHealth()) {
                Player lvBu = (Player) attacker;
                if (attacked instanceof Player || attacked.isAlliedTo(lvBu)) {
                    if (attacked.level.isClientSide()) return;
                    if (((DongZhuo)attacked).lastWords$said()) {
                        ((DongZhuo)attacked).lastWords$setSaid(false);
                        return;
                    }

                    Component lvBuWords = new TextComponent("<" + lvBu.getName().getString() + "> " + LV_BU.get());
                    Component dongZhuoWords = new TextComponent("<" + attacked.getName().getString() + "> " + DONG_ZHUO.get());

                    lvBu.displayClientMessage(lvBuWords, false);
                    lvBu.displayClientMessage(dongZhuoWords, false);

                    if (attacked instanceof Player) {
                        Player dongZhuo = (Player) attacked;
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
            EFFECT_DURATION = builder.comment("-1 means infinite effect duration").defineInRange("EffectDuration", 200, 200, Integer.MAX_VALUE);
            EFFECT_LEVEL = builder.defineInRange("EffectAmplifier", 120, 0, 120);
            builder.pop();
            INSTANCE = builder.build();
        }
    }
}
