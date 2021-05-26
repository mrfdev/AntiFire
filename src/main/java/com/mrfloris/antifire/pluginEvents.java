package com.mrfloris.antifire;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class pluginEvents extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void stopThat(BlockIgniteEvent e) {
        if (e.getCause() == BlockIgniteEvent.IgniteCause.SPREAD) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void stopFire(BlockBurnEvent e) {
        e.setCancelled(true);
        Block block = e.getBlock();
        Block[] adjacentBlocks = {block.getRelative(BlockFace.UP), block.getRelative(BlockFace.DOWN), block.getRelative(BlockFace.NORTH), block.getRelative(BlockFace.SOUTH), block.getRelative(BlockFace.EAST), block.getRelative(BlockFace.WEST)};
        for (int i = 0; i < adjacentBlocks.length; i++) {
            Block adjacentBlock = adjacentBlocks[i];
            if (adjacentBlock.getType() == Material.FIRE && adjacentBlock.getRelative(BlockFace.DOWN).getType() != Material.NETHERRACK) {
                adjacentBlock.setType(Material.AIR);
            }
        }
        Block aboveBlock = block.getRelative(BlockFace.UP);
        if (aboveBlock.getType() == Material.FIRE) {
            aboveBlock.setType(Material.AIR);
        }
    }
}