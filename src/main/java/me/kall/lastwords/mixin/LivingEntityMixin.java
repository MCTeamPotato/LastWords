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
        compound.putBoolean("LastWordsSaid", this.lastWords$said());
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void load(@NotNull CompoundTag compound, CallbackInfo ci) {
        this.lastWords$setSaid(compound.getBoolean("LastWordsSaid"));
    }
}
