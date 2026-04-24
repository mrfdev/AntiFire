package com.mrfloris.antifire;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.bukkit.plugin.java.JavaPlugin;

record BuildMetadata(
        String paperCompileTarget,
        String paperCompileDependency,
        String pluginApiCompatibilityFloor,
        int javaTarget
) {
    static BuildMetadata load(JavaPlugin plugin) {
        Properties properties = new Properties();
        try (InputStream inputStream = plugin.getResource("build-info.properties")) {
            if (inputStream != null) {
                properties.load(inputStream);
            }
        } catch (IOException exception) {
            plugin.getLogger().warning("Could not load build-info.properties: " + exception.getMessage());
        }

        return new BuildMetadata(
                properties.getProperty("paper.compile.target", "unknown"),
                properties.getProperty("paper.compile.dependency", "unknown"),
                properties.getProperty("plugin.api.compatibility-floor", "unknown"),
                parseJavaTarget(properties.getProperty("java.target", "0"))
        );
    }

    String summary() {
        return "Paper API " + paperCompileTarget
                + " (" + paperCompileDependency + "), plugin.yml api-version "
                + pluginApiCompatibilityFloor + ", Java " + javaTarget;
    }

    private static int parseJavaTarget(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ignored) {
            return 0;
        }
    }
}
