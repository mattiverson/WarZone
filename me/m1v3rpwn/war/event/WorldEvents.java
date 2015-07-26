/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.m1v3rpwn.war.event;

import me.m1v3rpwn.war.main.Main;
import me.m1v3rpwn.war.main.Util;
import org.bukkit.GameMode;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

/**
 *
 * @author m1v3rpwn
 */
public class WorldEvents implements org.bukkit.event.Listener {

    private Main plugin;
    private Util u;

    public WorldEvents(Main plug, Util util) {
        plugin = plug;
        u = util;
    }

    @EventHandler
    public void snowballRemover(ProjectileHitEvent e) {
        if (e.getEntity() instanceof Snowball) {
            e.getEntity().remove();
            plugin.bullets.remove((Snowball) e.getEntity());
        }
    }

    @EventHandler
    public void antiBuild(BlockPlaceEvent e) {
        if (!plugin.players.contains(e.getPlayer())) {
            return;
        }
        if (!e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void antiBreak(BlockBreakEvent e) {
        if (!plugin.players.contains(e.getPlayer())) {
            return;
        }
        if (!e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
            e.setCancelled(true);
        }
    }

}
