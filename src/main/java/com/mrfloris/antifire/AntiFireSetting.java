package com.mrfloris.antifire;

import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

enum AntiFireSetting {
    PREVENT_FIRE_SPREAD("prevent-fire-spread", ValueType.BOOLEAN, true),
    PREVENT_BLOCK_BURN("prevent-block-burn", ValueType.BOOLEAN, true),
    EXTINGUISH_ENABLED("extinguish-enabled", ValueType.BOOLEAN, true),
    EXTINGUISH_DELAY_TICKS("extinguish-delay-ticks", ValueType.INTEGER, 100),
    CHECK_INTERVAL_TICKS("check-interval-ticks", ValueType.INTEGER, 20),
    TRACK_PLAYER_PLACED_FIRE("track-player-placed-fire", ValueType.BOOLEAN, true),
    TRACK_LIGHTNING_FIRE("track-lightning-fire", ValueType.BOOLEAN, true),
    TRACK_LAVA_FIRE("track-lava-fire", ValueType.BOOLEAN, true),
    TRACK_OTHER_IGNITE_FIRE("track-other-ignite-fire", ValueType.BOOLEAN, true),
    STARTUP_LOG("startup-log", ValueType.BOOLEAN, true);

    private final String path;
    private final ValueType type;
    private final Object defaultValue;

    AntiFireSetting(String path, ValueType type, Object defaultValue) {
        this.path = path;
        this.type = type;
        this.defaultValue = defaultValue;
    }

    String path() {
        return path;
    }

    Object defaultValue() {
        return defaultValue;
    }

    boolean readBoolean(AntiFirePlugin plugin) {
        return plugin.getConfig().getBoolean(path, (Boolean) defaultValue);
    }

    int readInt(AntiFirePlugin plugin) {
        return sanitizeInteger(plugin.getConfig().getInt(path, (Integer) defaultValue));
    }

    Object readValue(AntiFirePlugin plugin) {
        return switch (type) {
            case BOOLEAN -> readBoolean(plugin);
            case INTEGER -> readInt(plugin);
        };
    }

    Object parseValue(String rawValue) {
        return switch (type) {
            case BOOLEAN -> parseBoolean(rawValue);
            case INTEGER -> sanitizeInteger(parseInteger(rawValue));
        };
    }

    static Optional<AntiFireSetting> fromPath(String rawPath) {
        return Arrays.stream(values())
                .filter(setting -> setting.path.equalsIgnoreCase(rawPath))
                .findFirst();
    }

    static String availableKeys() {
        return Arrays.stream(values())
                .map(AntiFireSetting::path)
                .collect(Collectors.joining(", "));
    }

    private static boolean parseBoolean(String rawValue) {
        String normalized = rawValue.toLowerCase(Locale.ROOT);
        return switch (normalized) {
            case "true", "on", "yes", "enabled", "enable", "1" -> true;
            case "false", "off", "no", "disabled", "disable", "0" -> false;
            default -> throw new IllegalArgumentException("Use true/false, on/off, yes/no, or 1/0.");
        };
    }

    private static int parseInteger(String rawValue) {
        try {
            return Integer.parseInt(rawValue);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Use a whole number value.", exception);
        }
    }

    private static int sanitizeInteger(int value) {
        return Math.max(1, value);
    }

    private enum ValueType {
        BOOLEAN,
        INTEGER
    }
}
