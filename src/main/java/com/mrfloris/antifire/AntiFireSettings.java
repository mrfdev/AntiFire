package com.mrfloris.antifire;

import java.util.LinkedHashMap;
import java.util.Map;
import org.bukkit.configuration.ConfigurationSection;

final class AntiFireSettings {
    private final boolean preventFireSpread;
    private final boolean preventBlockBurn;
    private final boolean extinguishEnabled;
    private final int extinguishDelayTicks;
    private final int checkIntervalTicks;
    private final boolean trackPlayerPlacedFire;
    private final boolean trackLightningFire;
    private final boolean trackLavaFire;
    private final boolean trackOtherIgniteFire;
    private final boolean startupLog;

    private AntiFireSettings(
            boolean preventFireSpread,
            boolean preventBlockBurn,
            boolean extinguishEnabled,
            int extinguishDelayTicks,
            int checkIntervalTicks,
            boolean trackPlayerPlacedFire,
            boolean trackLightningFire,
            boolean trackLavaFire,
            boolean trackOtherIgniteFire,
            boolean startupLog
    ) {
        this.preventFireSpread = preventFireSpread;
        this.preventBlockBurn = preventBlockBurn;
        this.extinguishEnabled = extinguishEnabled;
        this.extinguishDelayTicks = extinguishDelayTicks;
        this.checkIntervalTicks = checkIntervalTicks;
        this.trackPlayerPlacedFire = trackPlayerPlacedFire;
        this.trackLightningFire = trackLightningFire;
        this.trackLavaFire = trackLavaFire;
        this.trackOtherIgniteFire = trackOtherIgniteFire;
        this.startupLog = startupLog;
    }

    static AntiFireSettings from(ConfigurationSection configuration) {
        return new AntiFireSettings(
                AntiFireSetting.PREVENT_FIRE_SPREAD.readBoolean(configuration),
                AntiFireSetting.PREVENT_BLOCK_BURN.readBoolean(configuration),
                AntiFireSetting.EXTINGUISH_ENABLED.readBoolean(configuration),
                AntiFireSetting.EXTINGUISH_DELAY_TICKS.readInt(configuration),
                AntiFireSetting.CHECK_INTERVAL_TICKS.readInt(configuration),
                AntiFireSetting.TRACK_PLAYER_PLACED_FIRE.readBoolean(configuration),
                AntiFireSetting.TRACK_LIGHTNING_FIRE.readBoolean(configuration),
                AntiFireSetting.TRACK_LAVA_FIRE.readBoolean(configuration),
                AntiFireSetting.TRACK_OTHER_IGNITE_FIRE.readBoolean(configuration),
                AntiFireSetting.STARTUP_LOG.readBoolean(configuration)
        );
    }

    boolean preventFireSpread() {
        return preventFireSpread;
    }

    boolean preventBlockBurn() {
        return preventBlockBurn;
    }

    boolean extinguishEnabled() {
        return extinguishEnabled;
    }

    int extinguishDelayTicks() {
        return extinguishDelayTicks;
    }

    int checkIntervalTicks() {
        return checkIntervalTicks;
    }

    boolean trackPlayerPlacedFire() {
        return trackPlayerPlacedFire;
    }

    boolean trackLightningFire() {
        return trackLightningFire;
    }

    boolean trackLavaFire() {
        return trackLavaFire;
    }

    boolean trackOtherIgniteFire() {
        return trackOtherIgniteFire;
    }

    boolean startupLog() {
        return startupLog;
    }

    Map<String, Object> asMap() {
        Map<String, Object> values = new LinkedHashMap<>();
        values.put(AntiFireSetting.PREVENT_FIRE_SPREAD.path(), preventFireSpread);
        values.put(AntiFireSetting.PREVENT_BLOCK_BURN.path(), preventBlockBurn);
        values.put(AntiFireSetting.EXTINGUISH_ENABLED.path(), extinguishEnabled);
        values.put(AntiFireSetting.EXTINGUISH_DELAY_TICKS.path(), extinguishDelayTicks);
        values.put(AntiFireSetting.CHECK_INTERVAL_TICKS.path(), checkIntervalTicks);
        values.put(AntiFireSetting.TRACK_PLAYER_PLACED_FIRE.path(), trackPlayerPlacedFire);
        values.put(AntiFireSetting.TRACK_LIGHTNING_FIRE.path(), trackLightningFire);
        values.put(AntiFireSetting.TRACK_LAVA_FIRE.path(), trackLavaFire);
        values.put(AntiFireSetting.TRACK_OTHER_IGNITE_FIRE.path(), trackOtherIgniteFire);
        values.put(AntiFireSetting.STARTUP_LOG.path(), startupLog);
        return values;
    }
}
