/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package event;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import gun.Sniper;
import main.Main.Gamemode;
import main.Main.Loadout;
import main.Main;
import main.Main.PlayerData;
import main.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;

/**
 *
 * @author m1v3rpwn
 */
public class PlayerEvents implements org.bukkit.event.Listener {

    private Main plugin;
    private Util u;
    HashMap<Player, Scoreboard> comebacks = new HashMap<>();

    public PlayerEvents(Main plug, Util util) {
        plugin = plug;
        u = util;
    }

    public void initPlayer(Player p) {
        p.getInventory().clear();
        p.sendMessage(ChatColor.GREEN + "Welcome to War Zone!");
        p.setMaxHealth(100);
        p.setHealth(100);
        p.setHealthScale(20);
        if (comebacks.containsKey(p)) {
            p.setScoreboard(comebacks.get(p));
            comebacks.remove(p);
        }
        try {
            File player = new File(plugin.datdir.getAbsolutePath() + "/" + p.getName() + ".yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(player);
            if (!player.exists()) {
                player.createNewFile();
                config.set("level", 1);
                config.set("exp", 0);
                config.set("credits", 0);
                config.set("kills", 0);
                config.set("deaths", 0);
                config.set("akills", 0);
                config.set("adeaths", 0);
                config.set("prim", "AK12");
                config.set("sec", "FN57");
                List<String> avail = new ArrayList<>();
                avail.add("AK12");
                avail.add("AWS");
                avail.add("CSLR4");
                avail.add("FN57");
                config.set("available", avail);
                config.set("buyable", null);
                config.save(player);
            }
            PlayerData newdat = plugin.new PlayerData(p, config);
            newdat.load.primary = plugin.u.gunlist.get(config.getString("prim")).clone();
            newdat.load.secondary = plugin.u.gunlist.get(config.getString("sec")).clone();
            plugin.data.add(newdat);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (plugin.game.equals(Gamemode.NONE)) {
            p.getInventory().addItem(plugin.u.loadEdit);
            p.sendMessage(ChatColor.DARK_AQUA + "There isn't a game currently running!");
        } else {
            switch (plugin.game) {
                case INFECT:
                    plugin.red.add(p);
                    p.setBedSpawnLocation(plugin.redspawn, true);
                    p.teleport(plugin.redspawn);
                    p.getInventory().clear();
                    p.getInventory().addItem(u.INFECT_SWORD);
                    p.getInventory().setArmorContents(u.RED_ARMOR);
                    p.sendMessage(ChatColor.RED + "Infection! Kill the survivors!");
                    break;
                default:
                    boolean red = (plugin.red.size() < plugin.blue.size());
                    PlayerData dat = plugin.datByName(p.getName());
                    if (red) {
                        p.setBedSpawnLocation(plugin.redspawn, true);
                        p.teleport(plugin.redspawn);
                        p.getInventory().clear();
                        p.getInventory().addItem(dat.load.getInventory());
                        dat.load.primary.holder = p;
                        dat.load.secondary.holder = p;
                        p.getInventory().setArmorContents(u.RED_ARMOR);
                    } else {
                        p.setBedSpawnLocation(plugin.bluespawn, true);
                        p.teleport(plugin.bluespawn);
                        p.getInventory().clear();
                        p.getInventory().addItem(dat.load.getInventory());
                        dat.load.primary.holder = p;
                        dat.load.secondary.holder = p;
                        p.getInventory().setArmorContents(u.BLUE_ARMOR);
                    }
                    p.sendMessage(ChatColor.AQUA + "There is a game in progress! You have joined on team " + (red ? ChatColor.RED + "Red!" : ChatColor.BLUE + "Blue!"));
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        if (!plugin.players.contains(e.getEntity())) {
            return;
        }
        e.getDrops().clear();
        e.setDroppedExp(0);
        e.setDeathMessage(null);
        if (plugin.game.equals(Gamemode.NONE)) {
            return;
        }
        Player kill = e.getEntity().getKiller();
        Player dead = e.getEntity();
        if (kill != null) {
            PlayerData dat = plugin.datByName(kill.getName());
            dat.kills++;
            dat.streak++;
            dat.exp += 25;
            dat.creds += 5;
            kill.sendMessage(u.teamColor(dead) + dead.getName() + ChatColor.YELLOW + " was slain by " + u.teamColor(kill) + kill.getName());
            dead.sendMessage(u.teamColor(dead) + dead.getName() + ChatColor.YELLOW + " was slain by " + u.teamColor(kill) + kill.getName());
//            if (dat.streak == 8) {
//                kill.sendMessage(ChatColor.AQUA + "8 killstreak! You've received an assault chopper!");
//                final PlayerInventory inv = kill.getInventory();
//                kill.getInventory().clear();
//
//            }
        } else {
            dead.sendMessage(u.teamColor(dead) + dead.getName() + ChatColor.YELLOW + " died");
        }
        plugin.datByName(dead.getName()).deaths++;
        if (plugin.game.equals(Gamemode.INFECT)) {
            if (plugin.blue.contains(dead)) {
                plugin.blue.remove(dead);
                if (plugin.blue.isEmpty()) {
                    plugin.endGame();
                }
                plugin.red.add(dead);
                dead.setBedSpawnLocation(plugin.redspawn, true);
            }
        } else {
            dead.getInventory().clear();
            dead.getInventory().addItem(plugin.datByName(dead.getName()).load.getInventory());
            if (plugin.blue.contains(dead)) {
                dead.getInventory().setArmorContents(u.BLUE_ARMOR);
            } else {
                dead.getInventory().setArmorContents(u.RED_ARMOR);
            }
        }
    }

    @EventHandler
    public void damage(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && !plugin.players.contains((Player) e.getEntity()) || e.getDamager() instanceof Player && !plugin.players.contains((Player) e.getDamager())) {
//            ignores the event if a player involved is not in WarZone
            return;
        }
        if ((e.getDamager() instanceof Player || e.getDamager() instanceof Snowball && ((Snowball) e.getDamager()).getShooter() instanceof Player) && e.getEntity() instanceof Player) {
//            if a player was damaged by another player or an arrow shot by a player
            Player p = e.getDamager() instanceof Player ? (Player) e.getDamager() : (Player) ((Snowball) e.getDamager()).getShooter();
//            Player p is either the attacker or the person who shot the arrow.
            Player p1 = (Player) e.getEntity();
            if (plugin.red.contains(p) && plugin.red.contains(p1) || plugin.blue.contains(p) && plugin.blue.contains(p1)) {
//                if p and p1 are on the same team
                e.setCancelled(true);
                return;
            }
        }
        if (e.getDamager() instanceof Snowball) {
            Snowball a = (Snowball) e.getDamager();
            if (plugin.bullets.keySet().contains(a)) {
                if (((Player)a.getShooter()).getLocation().distance(a.getLocation()) > 20) {
                    e.setDamage(plugin.bullets.get(a).longdamage);
                } else {
                    e.setDamage(plugin.bullets.get(a).closedamage);
                }
                double zone = a.getLocation().getY() - e.getEntity().getLocation().getY();
                if (zone < 0.5) {
                    e.setDamage(e.getDamage() - 10);
                } else if (zone > 1.5) {
                    if (plugin.bullets.get(a) instanceof Sniper) {
                        e.setDamage(e.getDamage() * 2);
                    } else {
                        e.setDamage(e.getDamage() * 1.5);
                    }
                }
            }
        }
    }

    @EventHandler
    public void restartPlayer(PlayerRespawnEvent e) {
        if (!plugin.players.contains(e.getPlayer())) {
            return;
        }
        Player dead = e.getPlayer();
        dead.getInventory().clear();
        dead.setFoodLevel(15);
        dead.setSaturation(200f);
        if (plugin.game == Gamemode.INFECT) {
            dead.getInventory().setItem(0, u.INFECT_SWORD);
            dead.getInventory().setArmorContents(u.RED_ARMOR);
            return;
        }
        if (plugin.game != Gamemode.NONE) {
            dead.getInventory().clear();
            dead.getInventory().addItem(plugin.datByName(dead.getName()).load.getInventory());
            Loadout load = plugin.datByName(dead.getName()).load;
            load.primary.left = load.primary.clipsize;
            load.secondary.left = load.secondary.clipsize;
            if (plugin.blue.contains(dead)) {
                dead.getInventory().setArmorContents(u.BLUE_ARMOR);
            } else {
                dead.getInventory().setArmorContents(u.RED_ARMOR);
            }
        }
    }

    @EventHandler
    public void pickupPack(PlayerPickupItemEvent e) {
        if (!plugin.players.contains(e.getPlayer())) {
            return;
        }
        if (e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
            return;
        } else {
            e.setCancelled(true);
        }
        if (e.getItem().getItemStack().getType().equals(Material.INK_SACK)) {
            final Location loc = e.getItem().getLocation();
            final ItemStack item = e.getItem().getItemStack();
            PlayerData dat = plugin.datByName(e.getPlayer().getName());
            switch (item.getDurability()) {
                case 1:
                    dat.load.primary.ammo += dat.load.primary.clipsize;
                    dat.load.secondary.ammo += dat.load.secondary.clipsize;
                    e.getPlayer().sendMessage(ChatColor.GREEN + "You picked up a small ammo crate!");
                    dat.load.primary.updateName();
                    dat.load.secondary.updateName();
                    break;
                case 2:
                    if (e.getPlayer().getHealth() == e.getPlayer().getMaxHealth()) {
                        return;
                    }
                    e.getPlayer().setHealth(e.getPlayer().getHealth() < e.getPlayer().getMaxHealth() * 3 / 4 ? e.getPlayer().getHealth() + e.getPlayer().getMaxHealth() / 4 : e.getPlayer().getMaxHealth());
                    e.getPlayer().sendMessage(ChatColor.GREEN + "You picked up a small health pack!");
                    break;
                case 3:
                    dat.load.primary.ammo += dat.load.primary.clipsize * 3;
                    dat.load.secondary.ammo += dat.load.secondary.clipsize * 3;
                    e.getPlayer().sendMessage(ChatColor.GREEN + "You picked up a large ammo crate!");
                    dat.load.primary.updateName();
                    dat.load.secondary.updateName();
                    break;
                case 10:
                    if (e.getPlayer().getHealth() == 20) {
                        return;
                    }
                    e.getPlayer().setHealth(e.getPlayer().getHealth() < e.getPlayer().getMaxHealth() / 2 ? e.getPlayer().getMaxHealth() / 2 : e.getPlayer().getMaxHealth());
                    e.getPlayer().sendMessage(ChatColor.GREEN + "You picked up a large health pack!");
                    break;
            }
            e.getItem().remove();
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new BukkitRunnable() {
                @Override
                public void run() {
                    loc.getWorld().dropItem(loc, item);
                }
            }, 100);
        } else if (!e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void savePlayerData(PlayerQuitEvent e) {
        if (!plugin.players.contains(e.getPlayer())) {
            return;
        }
        exitPlayer(e.getPlayer());
    }
    
    public void exitPlayer(Player p) {
        PlayerData dat = plugin.datByName(p.getName());
        saveStats(p);
        comebacks.put(p, dat.board);
        p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        plugin.data.remove(dat);
    }

    public void saveStats(Player p) {
        File player = new File(plugin.datdir.getAbsolutePath() + "/" + p.getName() + ".yml");
        PlayerData dat = plugin.datByName(p.getName());
        try {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(player);
            config.options().pathSeparator('\n').indent(3);
            config.set("level", dat.level);
            config.set("exp", dat.exp);
            config.set("credits", dat.creds);
            config.set("kills", dat.kills);
            config.set("deaths", dat.deaths);
            config.set("akills", dat.akills);
            config.set("adeaths", dat.adeaths);
            config.set("prim", dat.load.primary.name.replaceAll("-", ""));
            config.set("sec", dat.load.secondary.name.replaceAll("-", ""));
            config.set("available", dat.available);
            config.set("buyable", dat.buyable);
            Date d = Calendar.getInstance().getTime();
            String day = 1 + d.getMonth() + "/" + d.getDate() + "/" + (d.getYear() + 1900);
            config.set("lastLogin", day);
            config.save(player);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    @EventHandler
    public void helpCommand(PlayerCommandPreprocessEvent e) {
        if (!plugin.players.contains(e.getPlayer())) {
            return;
        }
        if (e.getMessage().equalsIgnoreCase("/help")) {
            e.getPlayer().sendMessage(ChatColor.GOLD + "List of guns available to you: " + plugin.datByName(e.getPlayer().getName()).available.toString());
            e.getPlayer().sendMessage(ChatColor.GOLD + "List of primary guns in the game: [SAR21, M416, SCAR-H, XM214, M590, M1014, SPAS12, AA12, QBU-88, SV-98, JNG90, GOL Magnum]");
            e.getPlayer().sendMessage(ChatColor.GOLD + "List of secondary guns in the game: [M9, MP-443, G18, MP-412]");

            e.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void playCommand(PlayerCommandPreprocessEvent e) {
        if (!e.getMessage().toLowerCase().startsWith("/play")) {
            return;
        }
        if (e.isCancelled()) {
            return;
        }
        String game = e.getMessage().substring(6);
        Player p = e.getPlayer();
        if (game.equalsIgnoreCase("war") || game.equalsIgnoreCase("warzone")) {
            if (plugin.players.contains(p)) {
                p.sendMessage(ChatColor.RED + "You're already in that game!");
                return;
            }
            plugin.players.add(p);
            plugin.events.initPlayer(p);
        } else if (plugin.players.contains(p)) {
            plugin.events.exitPlayer(p);
            plugin.players.remove(p);

        }
    }

}
