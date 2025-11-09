package me.kall.lastwords.porting.mixin;

import me.kall.lastwords.porting.event.LivingHurtEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = LivingEntity.class, priority = 999)
public class LivingEntityMixin {
    @ModifyVariable(method = "actuallyHurt", at = @At(value = "LOAD", ordinal = 0), index = 2, argsOnly = true)
    private float livingHurtEvent(float value, DamageSource pDamageSource) {
        LivingHurtEvent event = new LivingHurtEvent((LivingEntity) (Object) this, pDamageSource, value);
        event.sendEvent();
        return (event.isCanceled() ? 0 : event.getAmount());
    }

    @Inject(method = "actuallyHurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getDamageAfterArmorAbsorb(Lnet/minecraft/world/damagesource/DamageSource;F)F"), cancellable = true)
    private void shouldCancelHurt(DamageSource damageSource, float amount, CallbackInfo ci) {
        if (amount <= 0) ci.cancel();
    }
}
