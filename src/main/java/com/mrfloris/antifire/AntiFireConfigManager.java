package com.mrfloris.antifire;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

final class AntiFireConfigManager {
    private final AntiFirePlugin plugin;
    private final Path configPath;
    private YamlConfiguration configuration;

    AntiFireConfigManager(AntiFirePlugin plugin) {
        this.plugin = plugin;
        this.configPath = plugin.getDataFolder().toPath().resolve("config.yml");
    }

    void load() {
        try {
            Files.createDirectories(configPath.getParent());

            YamlConfiguration loadedConfiguration = new YamlConfiguration();
            loadedConfiguration.options().parseComments(true);

            if (Files.exists(configPath)) {
                loadedConfiguration.loadFromString(Files.readString(configPath, StandardCharsets.UTF_8));
            }

            boolean changed = applyDefaultsAndComments(loadedConfiguration);
            configuration = loadedConfiguration;

            if (changed || Files.notExists(configPath)) {
                save();
            }
        } catch (IOException | InvalidConfigurationException exception) {
            throw new IllegalStateException("Could not load config.yml", exception);
        }
    }

    YamlConfiguration configuration() {
        return configuration;
    }

    void update(AntiFireSetting setting, Object value) {
        configuration.set(setting.path(), value);
        if (configuration.getComments(setting.path()).isEmpty()) {
            configuration.setComments(setting.path(), setting.comments());
        }
        save();
    }

    private boolean applyDefaultsAndComments(YamlConfiguration loadedConfiguration) {
        boolean changed = false;

        for (AntiFireSetting setting : AntiFireSetting.values()) {
            if (!loadedConfiguration.isSet(setting.path())) {
                loadedConfiguration.set(setting.path(), setting.defaultValue());
                changed = true;
            }

            if (loadedConfiguration.getComments(setting.path()).isEmpty()) {
                loadedConfiguration.setComments(setting.path(), setting.comments());
                changed = true;
            }
        }

        return changed;
    }

    private void save() {
        try {
            configuration.save(configPath.toFile());
        } catch (IOException exception) {
            throw new IllegalStateException("Could not save config.yml", exception);
        }
    }
}
