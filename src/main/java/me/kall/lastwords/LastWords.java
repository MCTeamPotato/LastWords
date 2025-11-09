package me.kall.lastwords;

import me.kall.lastwords.ext.DongZhuo;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LastWords implements ModInitializer {
    public static final String MOD_ID = "lastwords";
    public static final String MOD_NAME = "LastWords";
    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

    @Override
    public void onInitialize() {
        ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, amount) -> {
            LivingEntity attacked = entity;
            Entity attacker = source.getEntity();
            if (attacker instanceof Player lvBu && amount >= attacked.getHealth()) {
                if (attacked instanceof Player || attacked.isAlliedTo(lvBu)) {
                    //if (attacked.level().isClientSide()) return;
                    if (((DongZhuo)attacked).lastWords$said()) {
                        ((DongZhuo)attacked).lastWords$setSaid(false);
                        //return;
                    }

                    Component lvBuWords = Component.literal("<" + lvBu.getName().getString() + "> " + I18n.get("lv_bu.last_words"));
                    Component dongZhuoWords = Component.literal("<" + attacked.getName().getString() + "> " + I18n.get("dong_zhuo.last_words"));

                    lvBu.displayClientMessage(lvBuWords, false);
                    lvBu.displayClientMessage(dongZhuoWords, false);

                    if (attacked instanceof Player dongZhuo) {
                        dongZhuo.displayClientMessage(lvBuWords, false);
                        dongZhuo.displayClientMessage(dongZhuoWords, false);
                    }

                    amount = attacked.getHealth() - 1F;
                    //event.setAmount(attacked.getHealth() - 1F);
                    attacked.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, -1, 120, false, false, false));
                    attacked.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, -1, 120, false, false, false));

                    ((DongZhuo) attacked).lastWords$setSaid(true);
                }
            }
            return true;
        });
    }
}
