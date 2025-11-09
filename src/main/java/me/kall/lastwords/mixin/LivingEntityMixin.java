package me.kall.lastwords.mixin;

import me.kall.lastwords.ext.DongZhuo;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
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
        compound.putBoolean("LastWordsSaid", true);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void load(@NotNull CompoundTag compound, CallbackInfo ci) {
        this.lastWords$setSaid(compound.getBoolean("LastWordsSaid"));
    }
//
//    @WrapOperation(method = "actuallyHurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;setHealth(F)V"))
//    private void injected(LivingEntity attacked, float amount, Operation<Void> original, @Local(argsOnly = true) DamageSource damageSource) {
//        //if (ci.isCancelled()) return;
//        //LivingEntity attacked = ((LivingEntity) (Object) this);
//        float healthAmount = amount;
//        Entity attacker = damageSource.getEntity();
//        if (attacker instanceof Player lvBu && amount >= attacked.getHealth()) {
//            if (attacked instanceof Player || attacked.isAlliedTo(lvBu)) {
//                if (attacked.level().isClientSide()) return;
//                if (((DongZhuo)attacked).lastWords$said()) {
//                    ((DongZhuo)attacked).lastWords$setSaid(false);
//                    return;
//                }
//
//                Component lvBuWords = Component.literal("<" + lvBu.getName().getString() + "> " + I18n.get("lv_bu.last_words"));
//                Component dongZhuoWords = Component.literal("<" + attacked.getName().getString() + "> " + I18n.get("dong_zhuo.last_words"));
//
//                lvBu.displayClientMessage(lvBuWords, false);
//                lvBu.displayClientMessage(dongZhuoWords, false);
//
//                if (attacked instanceof Player dongZhuo) {
//                    dongZhuo.displayClientMessage(lvBuWords, false);
//                    dongZhuo.displayClientMessage(dongZhuoWords, false);
//                }
//
//                //event.setAmount(attacked.getHealth() - 1F);
//                //setHealth(attacked.getHealth() - 1F);
//                healthAmount = attacked.getHealth() - 1F;
//                attacked.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, -1, 120, false, false, false));
//                attacked.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, -1, 120, false, false, false));
//
//                ((DongZhuo) attacked).lastWords$setSaid(true);
//            }
//        }
//        original.call(attacked, healthAmount);
//    }
}
