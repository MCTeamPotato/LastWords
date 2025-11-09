package me.kall.lastwords.porting.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public class LivingHurtEvent extends AbstractEvent {
    public static final Event<HurtCallback> HURT = EventFactory.createArrayBacked(HurtCallback.class, callbacks -> event -> {
        for (HurtCallback e : callbacks)
            e.onLivingHurt(event);
    });
    private final LivingEntity entity;
    private final DamageSource source;
    private float amount;

    public LivingHurtEvent(LivingEntity entity, DamageSource source, float amount) {
        this.entity = entity;
        this.source = source;
        this.amount = amount;
    }

    public LivingEntity getEntity() {
        return entity;
    }

    public DamageSource getSource() {
        return source;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    @Override
    public void sendEvent() {
        HURT.invoker().onLivingHurt(this);
    }

    @FunctionalInterface
    public interface HurtCallback {
        void onLivingHurt(LivingHurtEvent event);
    }
}
