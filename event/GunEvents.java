/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package event;

import java.util.HashMap;
import gun.FullAuto;
import gun.Gun;
import gun.Sniper;
import main.Main.Gamemode;
import main.Main;
import main.Main.PlayerData;
import main.Util;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;

/**
 *
 * @author m1v3rpwn
 */
public class GunEvents implements org.bukkit.event.Listener {

    private Main plugin;
    private Util u;

    public GunEvents(Main plug, Util util) {
        plugin = plug;
        u = util;
    }

    @EventHandler
    public void reload(final PlayerDropItemEvent e) {
        if (!plugin.players.contains(e.getPlayer())) {
            return;
        }
        if (!e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
            e.setCancelled(true);
        }
        if (plugin.game.equals(Gamemode.NONE)) {
            return;
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                PlayerData dat = plugin.datByName(e.getPlayer().getName());
                if (e.getItemDrop().getItemStack().getType().equals(dat.load.primary.item().getType())) {
                    dat.load.primary.reload();
                } else if (e.getItemDrop().getItemStack().getType().equals(dat.load.secondary.item().getType())) {
                    dat.load.secondary.reload();
                }
            }
        }.runTaskLater(plugin, 5);

    }

    @EventHandler
    public void shoot(PlayerInteractEvent e) {
        if (!plugin.players.contains(e.getPlayer())) {
            return;
        }
        PlayerData dat = plugin.datByName(e.getPlayer().getName());
        if (plugin.game.equals(Gamemode.NONE)) {
            return;
        }
        if (e.getAction().equals(Action.LEFT_CLICK_BLOCK) || e.getAction().equals(Action.LEFT_CLICK_AIR)) {
            e.setCancelled(true);
            Player player = e.getPlayer();
            Gun prim = plugin.datByName(player.getName()).load.primary;
            Gun sec = plugin.datByName(player.getName()).load.secondary;
            if (e.getItem().getTypeId() == prim.item().getTypeId() && prim.cooldown <= 0 && !(prim instanceof FullAuto)) {
                for (final Snowball egg : prim.fire()) {
                    plugin.bullets.put(egg, prim);
                }
            } else if (prim instanceof FullAuto && e.getItem().getTypeId() == prim.item().getTypeId()) {
                boolean fire = !prim.running;
                prim.running = fire;
                player.sendMessage((fire ? ChatColor.AQUA : ChatColor.BLUE) + "Your gun is " + (fire ? "" : "no longer ") + "firing automatically!");
            }
            if (e.getItem().getTypeId() == sec.item().getTypeId() && sec.cooldown <= 0) {
                for (Snowball egg : sec.fire()) {
                    plugin.bullets.put(egg, sec);
                }
            }
        }
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            e.setCancelled(true);
//            PlayerData dat = plugin.datByName(e.getPlayer().getName());
            dat.load.primary.scoped = !dat.load.primary.scoped;
            dat.load.secondary.scoped = !dat.load.secondary.scoped;
            if (dat.load.primary.scoped) {
                if (dat.load.primary instanceof Sniper) {
                    e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 7), true);
                } else {
                    e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 1), true);
                }
            } else {
                e.getPlayer().removePotionEffect(PotionEffectType.SLOW);
            }
        }
    }

//    not implemented yet
    @EventHandler
    public void changeFireMode(InventoryOpenEvent e) {
        if (e.getPlayer() instanceof Player && !plugin.players.contains((Player) e.getPlayer())) {
            return;
        }
        if (!e.getPlayer().getGameMode().equals(GameMode.CREATIVE) && !e.getPlayer().getItemInHand().getType().equals(Material.BOWL)) {
//            implement firing modes here
//            e.getPlayer().closeInventory();
            e.setCancelled(true);
        }
    }
}
