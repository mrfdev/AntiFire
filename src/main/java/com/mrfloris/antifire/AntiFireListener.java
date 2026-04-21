package com.mrfloris.antifire;

import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;

final class AntiFireListener implements Listener {
    private static final BlockFace[] ADJACENT_FACES = {
            BlockFace.UP,
            BlockFace.DOWN,
            BlockFace.NORTH,
            BlockFace.SOUTH,
            BlockFace.EAST,
            BlockFace.WEST
    };

    private final AntiFirePlugin plugin;

    AntiFireListener(AntiFirePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockIgnite(BlockIgniteEvent event) {
        if (plugin.getSettings().preventFireSpread() && event.getCause() == BlockIgniteEvent.IgniteCause.SPREAD) {
            event.setCancelled(true);
            return;
        }

        if (plugin.shouldTrackIgniteCause(event.getCause())) {
            plugin.trackTemporaryFire(event.getBlock());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockSpread(BlockSpreadEvent event) {
        if (!plugin.isFireMaterial(event.getSource().getType())) {
            return;
        }

        if (plugin.getSettings().preventFireSpread()) {
            event.setCancelled(true);
            return;
        }

        plugin.trackTemporaryFire(event.getBlock());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBurn(BlockBurnEvent event) {
        if (!plugin.getSettings().preventBlockBurn()) {
            return;
        }

        event.setCancelled(true);
        plugin.clearAdjacentTemporaryFire(event.getBlock(), ADJACENT_FACES);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!plugin.getSettings().trackPlayerPlacedFire()) {
            return;
        }

        if (plugin.isFireMaterial(event.getBlockPlaced().getType())) {
            plugin.trackTemporaryFire(event.getBlockPlaced());
        }
    }
}
