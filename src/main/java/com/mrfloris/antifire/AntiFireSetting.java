package com.mrfloris.antifire;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;
import org.bukkit.configuration.ConfigurationSection;

enum AntiFireSetting {
    PREVENT_FIRE_SPREAD(
            "prevent-fire-spread",
            ValueType.BOOLEAN,
            true,
            List.of(
                    "Blocks natural fire spread from igniting nearby blocks.",
                    "Default: true.",
                    "Safe values: true or false.",
                    "Reload behavior: takes effect immediately after /_antifire reload or /_antifire toggle. No server restart required."
            )
    ),
    PREVENT_BLOCK_BURN(
            "prevent-block-burn",
            ValueType.BOOLEAN,
            true,
            List.of(
                    "Cancels fire burn damage so burning blocks do not get destroyed by fire.",
                    "Default: true.",
                    "Safe values: true or false.",
                    "Reload behavior: takes effect immediately after /_antifire reload or /_antifire toggle. No server restart required."
            )
    ),
    EXTINGUISH_ENABLED(
            "extinguish-enabled",
            ValueType.BOOLEAN,
            true,
            List.of(
                    "Turns delayed automatic extinguishing for tracked temporary fire on or off.",
                    "Default: true.",
                    "Safe values: true or false.",
                    "Reload behavior: takes effect immediately after /_antifire reload or /_antifire toggle. Disabling it clears tracked temporary fire timers."
            )
    ),
    EXTINGUISH_DELAY_TICKS(
            "extinguish-delay-ticks",
            ValueType.INTEGER,
            100,
            List.of(
                    "How long tracked temporary fire is allowed to remain before AntiFire removes it.",
                    "Default: 100 ticks (5 seconds).",
                    "Safe values: whole numbers 1 or higher. Lower values remove fire faster; higher values leave it visible longer.",
                    "Reload behavior: takes effect immediately after /_antifire reload or /_antifire toggle. Values below 1 are clamped to 1."
            )
    ),
    CHECK_INTERVAL_TICKS(
            "check-interval-ticks",
            ValueType.INTEGER,
            20,
            List.of(
                    "How often AntiFire checks tracked temporary fire for expiry.",
                    "Default: 20 ticks (1 second).",
                    "Safe values: whole numbers 1 or higher. Lower values check more often; higher values reduce check frequency.",
                    "Reload behavior: takes effect immediately after /_antifire reload or /_antifire toggle. Values below 1 are clamped to 1."
            )
    ),
    TRACK_PLAYER_PLACED_FIRE(
            "track-player-placed-fire",
            ValueType.BOOLEAN,
            true,
            List.of(
                    "Tracks fire placed by players so it can be auto-extinguished later if delayed extinguish is enabled.",
                    "Default: true.",
                    "Safe values: true or false.",
                    "Reload behavior: takes effect immediately after /_antifire reload or /_antifire toggle. Existing tracked fire is left as-is until it expires or is cleared."
            )
    ),
    TRACK_LIGHTNING_FIRE(
            "track-lightning-fire",
            ValueType.BOOLEAN,
            true,
            List.of(
                    "Tracks fire caused by lightning strikes for delayed automatic extinguish.",
                    "Default: true.",
                    "Safe values: true or false.",
                    "Reload behavior: takes effect immediately after /_antifire reload or /_antifire toggle."
            )
    ),
    TRACK_LAVA_FIRE(
            "track-lava-fire",
            ValueType.BOOLEAN,
            true,
            List.of(
                    "Tracks fire caused by lava for delayed automatic extinguish.",
                    "Default: true.",
                    "Safe values: true or false.",
                    "Reload behavior: takes effect immediately after /_antifire reload or /_antifire toggle."
            )
    ),
    TRACK_OTHER_IGNITE_FIRE(
            "track-other-ignite-fire",
            ValueType.BOOLEAN,
            true,
            List.of(
                    "Tracks other ignition causes such as flint and steel or similar ignite events for delayed automatic extinguish.",
                    "Default: true.",
                    "Safe values: true or false.",
                    "Reload behavior: takes effect immediately after /_antifire reload or /_antifire toggle."
            )
    ),
    STARTUP_LOG(
            "startup-log",
            ValueType.BOOLEAN,
            true,
            List.of(
                    "Writes the AntiFire startup summary and build/config details to console during plugin enable.",
                    "Default: true.",
                    "Safe values: true or false.",
                    "Reload behavior: affects the next enable/startup log. Runtime debug and command output are unchanged."
            )
    );

    private final String path;
    private final ValueType type;
    private final Object defaultValue;
    private final List<String> comments;

    AntiFireSetting(String path, ValueType type, Object defaultValue, List<String> comments) {
        this.path = path;
        this.type = type;
        this.defaultValue = defaultValue;
        this.comments = comments;
    }

    String path() {
        return path;
    }

    Object defaultValue() {
        return defaultValue;
    }

    List<String> comments() {
        return comments;
    }

    boolean readBoolean(ConfigurationSection configuration) {
        return configuration.getBoolean(path, (Boolean) defaultValue);
    }

    int readInt(ConfigurationSection configuration) {
        return sanitizeInteger(configuration.getInt(path, (Integer) defaultValue));
    }

    Object readValue(ConfigurationSection configuration) {
        return switch (type) {
            case BOOLEAN -> readBoolean(configuration);
            case INTEGER -> readInt(configuration);
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
