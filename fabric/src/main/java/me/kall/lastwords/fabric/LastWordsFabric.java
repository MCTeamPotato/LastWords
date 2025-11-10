package me.kall.lastwords.fabric;

import me.kall.lastwords.LastWords;
import net.fabricmc.api.ModInitializer;

public final class LastWordsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        LastWords.init();
    }
}
