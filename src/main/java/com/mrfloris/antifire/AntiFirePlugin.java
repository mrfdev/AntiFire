package com.mrfloris.antifire;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public final class AntiFirePlugin extends JavaPlugin {
    private final Map<FireKey, Long> trackedFire = new HashMap<>();
    private BukkitTask extinguishTask;
    private AntiFireSettings settings;
    private BuildMetadata buildMetadata;
    private AntiFireConfigManager configManager;

    @Override
    public void onEnable() {
        buildMetadata = BuildMetadata.load(this);
        configManager = new AntiFireConfigManager(this);
        reloadAntiFireSettings();

        getServer().getPluginManager().registerEvents(new AntiFireListener(this), this);
        registerAdminCommand();

        if (settings.startupLog()) {
            getLogger().info("AntiFire is active in STARTUP mode.");
            getLogger().info("Build: " + buildMetadata.summary());
            getLogger().info("Spread=" + settings.preventFireSpread()
                    + ", burn=" + settings.preventBlockBurn()
                    + ", extinguish=" + settings.extinguishEnabled()
                    + ", delayTicks=" + settings.extinguishDelayTicks()
                    + ", checkIntervalTicks=" + settings.checkIntervalTicks());
        }
    }

    @Override
    public void onDisable() {
        if (extinguishTask != null) {
            extinguishTask.cancel();
            extinguishTask = null;
        }
        trackedFire.clear();
    }

    void reloadAntiFireSettings() {
        configManager.load();
        settings = AntiFireSettings.from(configManager.configuration());
        restartExtinguishTask();
    }

    AntiFireSettings getSettings() {
        return settings;
    }

    BuildMetadata getBuildMetadata() {
        return buildMetadata;
    }

    YamlConfiguration getConfiguration() {
        return configManager.configuration();
    }

    void updateSetting(AntiFireSetting setting, Object value) {
        configManager.update(setting, value);
        settings = AntiFireSettings.from(configManager.configuration());
        restartExtinguishTask();
    }

    boolean shouldTrackIgniteCause(BlockIgniteEvent.IgniteCause cause) {
        return switch (cause) {
            case SPREAD -> false;
            case LIGHTNING -> settings.trackLightningFire();
            case LAVA -> settings.trackLavaFire();
            default -> settings.trackOtherIgniteFire();
        };
    }

    boolean isFireMaterial(Material material) {
        return material == Material.FIRE || material == Material.SOUL_FIRE;
    }

    void trackTemporaryFire(Block block) {
        if (!settings.extinguishEnabled()) {
            return;
        }

        if (isPermanentFireBase(block.getRelative(BlockFace.DOWN).getType())) {
            return;
        }

        trackedFire.put(FireKey.from(block), System.currentTimeMillis() + (settings.extinguishDelayTicks() * 50L));
    }

    void clearAdjacentTemporaryFire(Block origin, BlockFace[] faces) {
        for (BlockFace face : faces) {
            Block adjacentBlock = origin.getRelative(face);
            if (!isFireMaterial(adjacentBlock.getType())) {
                continue;
            }

            if (isPermanentFireBase(adjacentBlock.getRelative(BlockFace.DOWN).getType())) {
                continue;
            }

            adjacentBlock.setType(Material.AIR);
            trackedFire.remove(FireKey.from(adjacentBlock));
        }
    }

    private void registerAdminCommand() {
        getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            Set<String> labels = event.registrar().register(
                    "_antifire",
                    "Shows and manages 1MB AntiFire configuration.",
                    new AntiFireAdminCommand(this)
            );
            getLogger().info("Registered AntiFire command labels: " + String.join(", ", labels));
        });
    }

    private void restartExtinguishTask() {
        if (extinguishTask != null) {
            extinguishTask.cancel();
            extinguishTask = null;
        }

        if (!settings.extinguishEnabled()) {
            trackedFire.clear();
            return;
        }

        extinguishTask = getServer().getScheduler().runTaskTimer(
                this,
                this::extinguishTrackedFire,
                settings.checkIntervalTicks(),
                settings.checkIntervalTicks()
        );
    }

    private void extinguishTrackedFire() {
        if (trackedFire.isEmpty()) {
            return;
        }

        long currentTime = System.currentTimeMillis();
        Iterator<Map.Entry<FireKey, Long>> iterator = trackedFire.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<FireKey, Long> entry = iterator.next();
            Block block = entry.getKey().resolve(getServer());
            if (block == null) {
                iterator.remove();
                continue;
            }

            if (!isFireMaterial(block.getType())) {
                iterator.remove();
                continue;
            }

            if (isPermanentFireBase(block.getRelative(BlockFace.DOWN).getType())) {
                iterator.remove();
                continue;
            }

            if (currentTime < entry.getValue()) {
                continue;
            }

            block.setType(Material.AIR);
            iterator.remove();
        }
    }

    private boolean isPermanentFireBase(Material material) {
        return material == Material.NETHERRACK;
    }

    private record FireKey(UUID worldId, int x, int y, int z) {
        private static FireKey from(Block block) {
            return new FireKey(block.getWorld().getUID(), block.getX(), block.getY(), block.getZ());
        }

        private Block resolve(org.bukkit.Server server) {
            World world = server.getWorld(worldId);
            return world == null ? null : world.getBlockAt(x, y, z);
        }
    }
}
