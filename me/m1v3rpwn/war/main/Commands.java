/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.m1v3rpwn.war.main;

import java.util.ArrayList;
import me.m1v3rpwn.war.gun.Gun;
import me.m1v3rpwn.war.gun.Primary;
import me.m1v3rpwn.war.gun.Secondary;
import me.m1v3rpwn.war.main.Main.Gamemode;
import me.m1v3rpwn.war.main.Main.PlayerData;
import me.m1v3rpwn.war.special.Special;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 *
 * @author m1v3rpwn
 */
public class Commands {

    public Main main;
    public Util util;
    public ArrayList<String> commandList = new ArrayList<>();

    public Commands(Main m, Util u) {
        commandList.add("swap");
        commandList.add("ul");
        commandList.add("prim");
        commandList.add("sec");
        commandList.add("buy");
        commandList.add("exp");
        commandList.add("xp");
        commandList.add("staff");
        commandList.add("kd");
        commandList.add("kdr");
        commandList.add("k/d");
        commandList.add("start");
        commandList.add("apply");
        commandList.add("join");
        main = m;
        util = u;
    }

    public boolean hasCommand(String label) {
        return commandList.contains(label);
    }

    public boolean runCommand(org.bukkit.command.CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        Player p = (Player) sender;
        switch (label.toUpperCase()) {
            case "SWAP":
                return swapCommand(p);
            case "UL":
                if (args.length == 0) {
                    return false;
                }
                return unlock(p, args[0]);
            case "PRIM":
                return primCommand(p, args.length > 0, (args.length > 0 ? args[0] : null));
            case "SEC":
                return secCommand(p, args.length > 0, (args.length > 0 ? args[0] : null));
            case "BUY":
                return buyCommand(p, args.length > 0, (args.length > 0 ? args[0] : null));
            case "EXP":
            case "XP":
                return expCommand(p);
            case "KD":
            case "KDR":
            case "K/D":
                return kdrCommand(p);
            case "STAFF":
                return staffCommand(p);
            case "START":
                return startCommand(p, (args.length > 0 ? args[0] : null));
//            case "APPLY":
//                return applyCommand(p);
        }
        return false;
    }

    private boolean swapCommand(Player p) {
        if (main.u.isRed(p) ^ (main.red.size() <= main.blue.size())) {
            if (main.u.isRed(p)) {
//                switch to blue team
                main.blue.add(p);
                main.red.remove(p);
                p.setBedSpawnLocation(main.bluespawn, true);
                if (p.getHealth() == p.getMaxHealth()) {
                    p.teleport(main.bluespawn);
                } else {
                    p.damage(1000000);
                }
            } else {
//                switch to red team
                main.red.add(p);
                main.blue.remove(p);
                p.setBedSpawnLocation(main.redspawn, true);
                if (p.getHealth() == p.getMaxHealth()) {
                    p.teleport(main.redspawn);
                } else {
                    p.damage(1000000);
                }
            }
            p.sendMessage(ChatColor.GREEN + "You are now on the " + (main.u.isRed(p) ? ChatColor.DARK_RED + "RED" : ChatColor.BLUE + "BLUE") + ChatColor.GREEN + " team!");
        } else {
            p.sendMessage(ChatColor.RED + "You can't swap while the other team has more players!");
            return false;
        }
        return true;
    }

    private boolean unlock(Player p, String gunName) {
        if (util.gunlist.containsKey(gunName)) {
            main.datByName(p.getName()).available.add(gunName);
            p.sendMessage(ChatColor.GREEN + "<3");
        }
        return true;
    }

    private boolean primCommand(Player p, boolean change, String name) {
        if (!change) {
            p.sendMessage(ChatColor.GREEN + "Your current primary weapon is: " + ChatColor.BLUE + main.datByName(p.getName()).load.primary.name);
            return true;
        }
        if (!main.game.equals(Gamemode.NONE)) {
            p.sendMessage(ChatColor.RED + "You can't do this during war!");
            return false;
        }
        Gun g = Main.me.u.gunlist.get(name.toUpperCase());
        if (g == null) {
            p.sendMessage(ChatColor.RED + "That's not a gun!");
            return false;
        }
        if (g instanceof Primary == false || (g instanceof Special && !p.isOp())) {
            p.sendMessage(ChatColor.RED + "That's not a primary weapon!");
            return false;
        }
        if (!main.datByName(p.getName()).available.contains(g.name.toUpperCase().replaceAll("-", "").replaceAll(" ", ""))) {
            p.sendMessage(ChatColor.RED + "You don't have access to this gun!");
            return false;
        }
        g.holder = p;
        main.datByName(p.getName()).load.primary = g;
        p.sendMessage(ChatColor.GREEN + "Primary weapon now set to: " + g.name);
        return true;
    }

    private boolean secCommand(Player p, boolean change, String name) {
        if (!change) {
            p.sendMessage(ChatColor.GREEN + "Your current secondary weapon is: " + ChatColor.BLUE + main.datByName(p.getName()).load.secondary.name);
            return true;
        }
        if (!main.game.equals(Gamemode.NONE)) {
            p.sendMessage(ChatColor.RED + "You can't do this during war!");
            return false;
        }
        Gun g = Main.me.u.gunlist.get(name.toUpperCase());
        if (g == null) {
            p.sendMessage(ChatColor.RED + "That's not a gun!");
            return false;
        }
        if (g instanceof Secondary == false) {
            p.sendMessage(ChatColor.RED + "That's not a secondary weapon!");
            return false;
        }
        if (!main.datByName(p.getName()).available.contains(g.name.toUpperCase().replaceAll("-", "").replaceAll(" ", ""))) {
            p.sendMessage(ChatColor.RED + "You don't have access to this gun!");
            return false;
        }
        g.holder = (Player) p;
        main.datByName(p.getName()).load.secondary = g;
        p.sendMessage(ChatColor.GREEN + "Secondary weapon now set to: " + g.name);
        return true;
    }

    private boolean buyCommand(Player p, boolean arg, String name) {
        if (!arg) {
            p.sendMessage(ChatColor.RED + "/buy [gun]");
            return false;
        }
        if (!main.game.equals(Gamemode.NONE)) {
            p.sendMessage(ChatColor.RED + "You can't do this during war!");
            return false;
        }
        if (!util.unlocks.containsKey(name.toUpperCase())) {
            p.sendMessage(ChatColor.RED + "That's not a gun!");
            return false;
        }
        PlayerData dat = main.datByName(p.getName());
        if (util.unlocks.get(name) > dat.level) {
            p.sendMessage(ChatColor.RED + "You aren't at the level needed to purchase this gun!");
            return false;
        }
        if (dat.creds < util.prices.get(name.toUpperCase())) {
            p.sendMessage(ChatColor.RED + "You can't afford this gun!");
            return false;
        }
        dat.creds -= util.prices.get(name.toUpperCase());
        dat.available.add(name.toUpperCase());
        dat.buyable.remove(name.toUpperCase());
        p.sendMessage(ChatColor.GREEN + "You have purchased the " + name.toUpperCase());
        return true;
    }

    private boolean expCommand(Player p) {
        PlayerData dat = main.datByName(p.getName());
        p.sendMessage(ChatColor.DARK_GREEN + "Current experience in level " + ChatColor.GREEN + dat.level + ChatColor.DARK_GREEN + " : " + ChatColor.GREEN + dat.exp + "/" + main.LEVEL_EXPS[dat.level - 1] + ChatColor.DARK_GREEN + "; approximately " + ChatColor.GREEN + (int) (100 * dat.player.getExp()) + "%");
        return true;
    }

    private boolean kdrCommand(Player p) {
        PlayerData dat = main.datByName(p.getName());
        p.sendMessage(ChatColor.DARK_GREEN + "Your current KDR is " + ChatColor.GREEN + (dat.deaths > 0 ? String.format("%.2f", dat.kills / dat.deaths) : "undefined") + ChatColor.DARK_GREEN + ", and your adjusted KDR is " + ChatColor.GREEN + (dat.adeaths > 0 ? ChatColor.GREEN + String.format("%.2f", dat.akills / dat.adeaths) : "undefined"));
        return true;
    }

    private boolean staffCommand(Player p) {
        StringBuilder staff = new StringBuilder();
        staff.append(ChatColor.GOLD + "Staff currently online: ");
        for (Player pl : Bukkit.getOnlinePlayers()) {
            if (pl.isOp()) {
                staff.append(pl.getName() + ", ");
            }
        }
        p.sendMessage(staff.substring(0, staff.length() - 2));
        return true;
    }

    private boolean startCommand(Player p, String mode) {
        if (!p.isOp()) {
            return false;
        }
        if (mode != null) {
            main.start(Gamemode.valueOf(mode.toUpperCase()));
            return true;
        } else {
            p.sendMessage(ChatColor.RED + "/start [gamemode]");
            return false;
        }
    }

//    private boolean applyCommand(Player p) {
//        p.sendMessage(ChatColor.AQUA + "Create a thread on our forums to apply for staff! \n" + ChatColor.AQUA + " http://fractal-pvp.enjin.com/forum/m/22685986/viewforum/4238849");
//        return true;
//    }
    private boolean joinCommand(Player p, Player host) {
        if (main.getPack(host.getName()) == null) {
            p.sendMessage(ChatColor.RED + host.getName() + " hasn't created a pack!");
            return false;
        }
        if (util.isRed(p) != util.isRed(host)) {
            p.sendMessage(ChatColor.RED + "You can't join this pack; " + host.getName() + " is on the other team!");
            return false;
        }
        if (host.getLocation().distance(p.getLocation()) > 50) {
            p.sendMessage(ChatColor.RED + "You are too far away from the host of this pack, pointing you in the right direction!");
//            sets the pitch and yaw of p's location to point toward host's location
            Location l = p.getLocation();
            Location l1 = host.getLocation();
            double xdif = p.getLocation().getX() - l1.getX();
            double zdif = p.getLocation().getZ() - l1.getZ();
            double slope = xdif / zdif;
            if (p.getLocation().getX() < l1.getX()) {
                slope = Math.toDegrees(Math.atan(slope));
                slope -= 90;
            } else {
                slope = Math.toDegrees(Math.atan(slope));
                slope += 90;
            }

            l.setYaw((float) slope);
            double base = Math.hypot(xdif, zdif);
            double ydif = l1.getY() - p.getLocation().getY();
            slope = ydif / base;
            slope = -Math.toDegrees(Math.atan(slope));
            l.setPitch((float) slope);
            p.teleport(l);

            return false;
        }

        return true;
    }
}
