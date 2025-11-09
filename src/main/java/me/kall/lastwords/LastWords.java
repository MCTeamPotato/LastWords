package me.kall.lastwords;

import me.kall.lastwords.ext.DongZhuo;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(LastWords.MOD_ID)
public final class LastWords {
    public static final String MOD_ID = "lastwords";
    public static final String MOD_NAME = "LastWords";
    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

    public LastWords() {
        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST, (LivingDamageEvent event) -> {
            if (event.isCanceled()) return;
            LivingEntity attacked = event.getEntity();
            Entity attacker = event.getSource().getEntity();
            if (attacker instanceof Player lvBu && event.getAmount() >= attacked.getHealth()) {
                if (attacked instanceof Player || attacked.isAlliedTo(lvBu)) {
                    if (((DongZhuo)attacked).lastWords$said()) return;
                    if (attacked.level().isClientSide()) return;

                    Component lvBuWords = Component.literal("<" + lvBu.getName().getString() + "> " + I18n.get("lv_bu.last_words"));
                    Component dongZhuoWords = Component.literal("<" + attacked.getName().getString() + "> " + I18n.get("dong_zhuo.last_words"));

                    lvBu.displayClientMessage(lvBuWords, false);
                    lvBu.displayClientMessage(dongZhuoWords, false);

                    if (attacked instanceof Player dongZhuo) {
                        dongZhuo.displayClientMessage(lvBuWords, false);
                        dongZhuo.displayClientMessage(dongZhuoWords, false);
                    }

                    event.setAmount(attacked.getHealth() - 1F);
                    attacked.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, -1, 120, false, false, false));
                    attacked.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, -1, 120, false, false, false));

                    ((DongZhuo) attacked).lastWords$setSaid(true);
                }
            }
        });
    }
}
