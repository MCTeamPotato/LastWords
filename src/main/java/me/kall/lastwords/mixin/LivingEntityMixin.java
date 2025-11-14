package me.kall.lastwords.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import me.kall.lastwords.LastWords;
import me.kall.lastwords.ext.DongZhuo;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements DongZhuo {
    @Unique private boolean lastWords$said;

    @Override
    public boolean lastWords$said() {
        return this.lastWords$said;
    }

    @Override
    public void lastWords$setSaid(boolean said) {
        this.lastWords$said = said;
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void save(@NotNull CompoundTag compound, CallbackInfo ci) {
        compound.putBoolean("LastWordsSaid", this.lastWords$said());
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void load(@NotNull CompoundTag compound, CallbackInfo ci) {
        this.lastWords$setSaid(compound.getBoolean("LastWordsSaid"));
    }

    @WrapOperation(method = "actuallyHurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;setHealth(F)V"))
    private void onHurt(LivingEntity attacked, float health, Operation<Void> original, @Local(argsOnly = true) DamageSource damageSource) {
        Entity attacker = damageSource.getEntity();
        if (attacker instanceof Player lvBu && health <= 0 && !attacker.level().isClientSide()) {
            if (attacked instanceof Player || attacked.isAlliedTo(lvBu)) {
                if (((DongZhuo)attacked).lastWords$said()) {
                    ((DongZhuo)attacked).lastWords$setSaid(false);
                    //noinspection MixinExtrasOperationParameters
                    original.call(attacked, health);
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

                health = 1F;
                if (LastWords.Config.EFFECT.get()) {
                    attacked.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, LastWords.Config.EFFECT_DURATION.get(), LastWords.Config.EFFECT_LEVEL.get(), false, false, false));
                    attacked.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, LastWords.Config.EFFECT_DURATION.get(), LastWords.Config.EFFECT_LEVEL.get(), false, false, false));
                }
                ((DongZhuo) attacked).lastWords$setSaid(true);
            }
        }

        original.call(attacked, health);
    }

}
