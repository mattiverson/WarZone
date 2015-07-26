/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.m1v3rpwn.war.main;

import me.m1v3rpwn.war.gun.FullAuto;
import me.m1v3rpwn.war.gun.Gun;
import me.m1v3rpwn.war.event.InvGuiEventListener;
import me.m1v3rpwn.war.event.PlayerEvents;
import me.m1v3rpwn.war.event.GunEvents;
import me.m1v3rpwn.war.event.WorldEvents;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

/**
 *
 * @author m1v3rpwn
 */
public class Main extends org.bukkit.plugin.java.JavaPlugin {

    public static final int[] LEVEL_EXPS = new int[]{50, 250, 1000, 2000, 3000, 4500, 5500, 7000, 8500, 10000, 12000, 20000, 25000, 28000, 33000, 38000, 44000, 50000, 56000, 61000, 67000, 73000, 80000, 87000, 94000, 100000};
    public ArrayList<Player> players = new ArrayList<>();
    public ArrayList<PlayerData> data = new ArrayList<>();
    public ArrayList<Pack> packs = new ArrayList<>();
    public HashMap<Snowball, Gun> bullets = new HashMap<>();
    public File datdir = new File("plugins/WarZone");
    public BukkitRunnable sboard;
    public BukkitRunnable firing;
    public Gamemode game = Gamemode.NONE;
    public ArrayList<Player> red = new ArrayList<>(), blue = new ArrayList<>();
    public final Location redspawn = new Location(null, 0, 70, 0), bluespawn = new Location(null, 0, 70, 100, 180, 0), lobbyspawn = new Location(null, -1000, 70, -1000);
    ArrayList<Location> dropspawns = new ArrayList<>();
    public int redscore, bluescore;
    public PlayerEvents events;
    public Util u;
    public static Main me;
    public static Random rand = new Random();
    public Commands cmds;

    @Override
    public void onEnable() {
        World w = this.getServer().getWorlds().get(0);
        redspawn.setWorld(w);
        bluespawn.setWorld(w);
        lobbyspawn.setWorld(w);
        game = Gamemode.NONE;
        if (!datdir.exists()) {
            try {
                datdir.mkdir();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        events = new PlayerEvents(this, u);
        u = new Util(this);
        cmds = new Commands(this, u);
        me = this;
        this.getServer().getPluginManager().registerEvents(events, this);
        this.getServer().getPluginManager().registerEvents(new GunEvents(this, u), this);
        this.getServer().getPluginManager().registerEvents(new WorldEvents(this, u), this);
        this.getServer().getPluginManager().registerEvents(new InvGuiEventListener(this), this);
        sboard = new BukkitRunnable() {
            @Override
            public void run() {
                for (PlayerData dat : data) {
                    Objective stats = dat.player.getScoreboard().getObjective(DisplaySlot.SIDEBAR);
                    stats.getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + "Kills:")).setScore(dat.kills);
                    stats.getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + "Deaths:")).setScore(dat.deaths);
                    stats.getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + "Killstreak:")).setScore(dat.streak);
                    stats.getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + "Level:")).setScore(dat.level);
                    stats.getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + "Credits:")).setScore(dat.creds);
                    while (dat.exp >= LEVEL_EXPS[dat.level - 1 >= 0 ? dat.level - 1 : 0]) {
                        dat.player.sendMessage(ChatColor.GOLD + "You have reached level " + (dat.level + 1));
                        dat.exp -= LEVEL_EXPS[dat.level - 1 >= 0 ? dat.level - 1 : 0];
                        dat.level++;
                        if (u.unlocks.containsValue(dat.level)) {
                            for (String s : u.unlocks.keySet()) {
                                if (u.unlocks.get(s) == dat.level) {
                                    dat.player.sendMessage(ChatColor.AQUA + "You have unlocked the " + s + "\n" + ChatColor.AQUA + "You can buy it for $" + u.prices.get(s));
                                    dat.buyable.add(s);
                                }
                            }

                        }
                    }
                    dat.player.setExp((float) ((double) dat.exp / LEVEL_EXPS[dat.level - 1 >= 0 ? dat.level - 1 : 0]));
                    Score prim = dat.stats.getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + "Primary Reload:"));
                    if (prim.getScore() > 0) {
                        if (prim.getScore() > 5) {
                            prim.setScore(prim.getScore() - 5);
                        } else {
                            prim.setScore(0);
                            dat.player.sendMessage(ChatColor.BLUE + "Primary finished reloading!");
                        }
                    }
                    Score sec = dat.stats.getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + "Secondary Reload:"));
                    if (sec.getScore() > 0) {
                        if (sec.getScore() > 5) {
                            sec.setScore(sec.getScore() - 5);
                        } else {
                            sec.setScore(0);
                            dat.player.sendMessage(ChatColor.BLUE + "Secondary finished reloading!");
                        }
                    }
                }
            }
        };
        firing = new BukkitRunnable() {
            @Override
            public void run() {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (!red.contains(p) && !blue.contains(p)) {
                        continue;
                    }
                    Gun g = datByName(p.getName()).load.primary;
                    Gun s = datByName(p.getName()).load.secondary;
                    if ((g instanceof FullAuto) && g.cooldown <= 0 && g.running && p.getItemInHand().getTypeId() == g.item().getTypeId()) {
                        for (Snowball a : g.fire()) {
                            bullets.put(a, g);
                        }
                    } else if (g.cooldown >= 0) {
                        g.cooldown--;
                    }
                    if (s.cooldown > 0) {
                        s.cooldown--;
                    }
                }
            }
        };
        this.getServer().getScheduler().scheduleSyncRepeatingTask(this, sboard, 5, 5);
        this.getServer().getScheduler().scheduleSyncRepeatingTask(this, firing, 1, 1);
        try {
            for (Player p : this.getServer().getOnlinePlayers()) {
                p.kickPlayer(ChatColor.RED + "The server was restarted!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void onDisable() {
        for (Player p : players) {
            events.saveStats(p);
        }
    }

    @Override
    public boolean onCommand(org.bukkit.command.CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (cmds.hasCommand(label.toLowerCase())) {
            return cmds.runCommand(sender, command, label, args);
        }
        return false;
    }

    public void start(Gamemode game) {
        this.game = game;
        switch (game) {
            case INFECT:
                if (Bukkit.getOnlinePlayers().length < 2) {
                    Bukkit.getOnlinePlayers()[0].sendMessage(ChatColor.LIGHT_PURPLE + "Not enough people to start infect!");
                    this.game = Gamemode.NONE;
                    return;
                }
                Player root = this.getServer().getOnlinePlayers()[(int) (Math.random() * this.getServer().getOnlinePlayers().length)];
                root.sendMessage(ChatColor.LIGHT_PURPLE + "Infection! Survive or kill!");
                root.sendMessage(ChatColor.DARK_RED + "You're the first zombie! Kill all the survivors!");
                red.add(root);
                root.setBedSpawnLocation(redspawn, true);
                root.teleport(redspawn);
                root.getInventory().clear();
                root.getInventory().addItem(u.INFECT_SWORD);
                root.getInventory().setArmorContents(u.RED_ARMOR);
                blue.addAll(Arrays.asList(this.getServer().getOnlinePlayers()));
                blue.remove(root);
                for (Player p : blue) {
                    root.sendMessage(ChatColor.LIGHT_PURPLE + "Infection! Survive or kill!");
                    p.getInventory().clear();
                    PlayerData dat = datByName(p.getName());
                    p.getInventory().addItem(dat.load.getInventory());
                    dat.load.primary.holder = p;
                    dat.load.secondary.holder = p;
                    p.setBedSpawnLocation(bluespawn, true);
                    p.getInventory().setArmorContents(u.BLUE_ARMOR);

                }
                Bukkit.getScheduler().scheduleSyncDelayedTask(this, new BukkitRunnable() {
                    @Override
                    public void run() {
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            endGame();
                        }
                    }
                }, 6000);
                break;
            case TDM:
                boolean rteam = true;
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.sendMessage(ChatColor.LIGHT_PURPLE + "Team Deathmatch! Kill the other team!");
                    p.getInventory().clear();
                    if (rteam) {
                        this.red.add(p);
                        p.setBedSpawnLocation(redspawn, true);
                        p.teleport(redspawn);
                        PlayerData dat = datByName(p.getName());
                        dat.load.primary.holder = p;
                        dat.load.secondary.holder = p;
                        p.getInventory().setArmorContents(u.RED_ARMOR);
                        p.sendMessage(ChatColor.RED + "You are on the RED team!");
                    } else {
                        this.blue.add(p);
                        p.setBedSpawnLocation(bluespawn, true);
                        p.teleport(bluespawn);
                        PlayerData dat = datByName(p.getName());
                        dat.load.primary.holder = p;
                        dat.load.secondary.holder = p;
                        p.getInventory().setArmorContents(u.BLUE_ARMOR);
                        p.sendMessage(ChatColor.BLUE + "You are on the BLUE team!");
                    }
                    rteam = !rteam;
                    p.getInventory().addItem(datByName(p.getName()).load.getInventory());
                    datByName(p.getName()).load.primary.updateName();
                    datByName(p.getName()).load.secondary.updateName();
                }
                Bukkit.getScheduler().scheduleSyncDelayedTask(this, new BukkitRunnable() {
                    @Override
                    public void run() {
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            endGame();
                        }
                    }
//                    the number of minutes is the first value, the second value is ticks per minute(constant)
                }, 20 * 1200);
                break;
        }
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.setHealth(100);
            p.setFoodLevel(17);
            p.setSaturation(9001);
        }
        redspawn.getWorld().setPVP(true);
    }

    public void endGame() {
        if (this.game.equals(Gamemode.NONE)) {
            return;
        }
        this.game = Gamemode.NONE;
        for (Player p : red) {
            p.teleport(lobbyspawn);
            p.getInventory().clear();
            p.getInventory().addItem(u.loadEdit);
            red.remove(p);
            p.sendMessage(ChatColor.AQUA + "GAME OVER!");
            p.sendMessage(ChatColor.AQUA + "Final Score: " + ChatColor.RED + "Red: " + redscore + "," + ChatColor.BLUE + " Blue:" + bluescore);
            if (redscore > bluescore) {
                p.sendMessage(ChatColor.RED + "RED WINS!");
            } else if (bluescore > redscore) {
                p.sendMessage(ChatColor.BLUE + "BLUE WINS!");
            } else {
                p.sendMessage(ChatColor.YELLOW + "DRAW!");
            }
        }
        for (Player p : blue) {
            p.teleport(lobbyspawn);
            p.getInventory().clear();
            p.getInventory().addItem(u.loadEdit);
            blue.remove(p);
            p.sendMessage(ChatColor.AQUA + "GAME OVER!");
            p.sendMessage(ChatColor.AQUA + "Final Score: " + ChatColor.RED + "Red- " + redscore + ", Blue-" + bluescore);
            if (redscore > bluescore) {
                p.sendMessage(ChatColor.RED + "RED WINS!");
            } else if (bluescore > redscore) {
                p.sendMessage(ChatColor.BLUE + "BLUE WINS!");
            } else {
                p.sendMessage(ChatColor.YELLOW + "DRAW!");
            }
        }
        lobbyspawn.getWorld().setPVP(false);
    }

    public PlayerData datByName(String s) {
        for (PlayerData dat : data) {
            if (dat.player.getName().equalsIgnoreCase(s)) {
                return dat;
            }
        }
        return null;
    }

    public Pack getPack(String hostName) {
        for (Pack p : packs) {
            if (p.host.getName().equalsIgnoreCase(hostName)) {
                return p;
            }
        }
        return null;
    }

    public class PlayerData {

        public Player player;
        public int level, exp, creds;
        public int kills, deaths, streak, akills, adeaths;
        public Objective stats;
        public Scoreboard board;
        public Loadout load;
        public List<String> available, buyable;

        public PlayerData(Player p, int lvl, int exper, int credits) {
            player = p;
            level = lvl;
            exp = exper;
            creds = credits;
            kills = 0;
            deaths = 0;
            board = Bukkit.getScoreboardManager().getNewScoreboard();
            stats = board.registerNewObjective("Stats", "dummy");
            stats.setDisplaySlot(DisplaySlot.SIDEBAR);
            player.setScoreboard(board);
        }

        public PlayerData(Player p, ConfigurationSection dat) {
            player = p;
            level = dat.getInt("level");
            exp = dat.getInt("exp");
            creds = dat.getInt("credits");
            kills = dat.getInt("kills");
            deaths = dat.getInt("deaths");
            akills = dat.getInt("akills");
            adeaths = dat.getInt("adeaths");
            board = Bukkit.getScoreboardManager().getNewScoreboard();
            p.setScoreboard(board);
            stats = board.registerNewObjective("Stats", "dummy");
            stats.setDisplaySlot(DisplaySlot.SIDEBAR);
            load = new Loadout();
            available = dat.getStringList("available");
            buyable = dat.getStringList("buyable");
        }

        public String toString() {
            return player.getName() + ": level " + level + ", " + exp + " experience in level, " + creds + " credits, " + kills + " kills, " + deaths + " deaths.";
        }

        public String printString() {
            Date d = Calendar.getInstance().getTime();
            String day = 1 + d.getMonth() + "-" + d.getDate() + "-" + (d.getYear() + 1900);
            return player.getName() + ":" + level + ":" + exp + ":" + creds + ":" + kills + ":" + deaths + ":" + day;
        }
    }

    public class Loadout {

        public Gun primary, secondary;

        public ItemStack[] getInventory() {
            return new ItemStack[]{primary.item(), secondary.item()};
        }

        public String toString() {
            return primary.name + "-" + secondary.name;
        }
    }

    public enum Gamemode {

        WAR, TDM, CONQUEST, INFECT, NONE;
    }
}
