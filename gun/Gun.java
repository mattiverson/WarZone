/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gun;

import main.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

/**
 *
 * @author miverson
 */
public abstract class Gun implements Cloneable {

//    Player holding the gun
    public Player holder;
//    ticks the bullets travel straight for
    public final int accticks = 15;
//    
    public final int speedcoef = 13;
//    damage done by bullet within 20 blocks
//    damage done by bullet outside 20 blocks
    public int closedamage, longdamage;
//    how drastically the bullet's flight changes when it loses stability
//    how many ticks are left until the gun can shoot again
//    how many ticks of delay are added every time the gun fires
//    how quickly the bullets move, determined by mpsec / speedcoef;
    public double deacc, cooldown, coolinc, speed, mpsec;
//    how far (in degrees) to change the player's facing (positive goes up and right)
    public float recoilup, recoilside;
//    the gun's name
    public String name;
//    the ammo the gun starts with
//    the ammo the gun currently has
    public int start, ammo;
//   the total size of a clip 
//   the amount of bullets left in the clip
    public int clipsize, left;
//    the delay (in ticks) when the gun reloads
    public int reload;
//    whether or not the gun is scoped
    public boolean scoped;
//    whether or not the gun is firing automatically (only used for full-auto guns)
    public boolean running;
//    whether or not the gun is allowed in full-auto mode (only for full-auto guns)
    public boolean automode;
//    the variation in the shot when a player isn't scoped in
    public float noscopepitch, noscopeyaw;
//    the amount of bullets the gun would shoot in a minute; not directly used ingame, only use is: coolinc = 1200/rpm
    public int rpm;
//    The slot in a player's inventory this gun will appear in; 0 for Primaries, 1 for Secondaries
    public int invslot;

    public Gun() {
        speed = (double) rpm / speedcoef;
    }

    public abstract Snowball[] fire();

    public void updateName() {
        if (left == 0) {
            reload();
            return;
        }
        ItemMeta meta = holder.getInventory().getItem(invslot).getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + name + " " + GunNameHelper.ammoString(this));
        holder.getInventory().getItem(invslot).setItemMeta(meta);
    }

    public void reload() {
        left = clipsize;
        cooldown = reload;
        ItemMeta meta = holder.getInventory().getItem(invslot).getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + name + " " + ReloadHelper.reloadString(reload));
        holder.getInventory().getItem(invslot).setItemMeta(meta);
        final int can = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.me, new ReloadHelper(this), 20, 20);
        final Gun me = this;
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.me, new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getScheduler().cancelTask(can);
                ItemMeta meta = holder.getInventory().getItem(invslot).getItemMeta();
                meta.setDisplayName(ChatColor.AQUA + name + " " + GunNameHelper.ammoString(me));
                holder.getInventory().getItem(invslot).setItemMeta(meta);
            }
        }, reload);
    }

    public abstract ItemStack item();

    public abstract Gun clone();

    public void recoil() {
        Location loc = holder.getLocation();
        loc.setPitch(loc.getPitch() - (float)(Math.random()*recoilup/2) + recoilup/2);
        loc.setYaw(loc.getYaw() + (float)(Math.random()*recoilside*2)-recoilside);
    }

    public String toString() {
        return name;
    }
}

class ReloadHelper extends BukkitRunnable {

    public static String reloadString(double ticks) {
        return ChatColor.RED + "<" + (1 + (int) (ticks / 20)) + ">";
    }

    Gun gun;

    public ReloadHelper(Gun g) {
        gun = g;
    }

    @Override
    public void run() {
        if (gun.cooldown <= 0) {
            ItemMeta meta = gun.holder.getInventory().getItem(gun.invslot).getItemMeta();
            meta.setDisplayName(ChatColor.AQUA + gun.name + " " + GunNameHelper.ammoString(gun));
            gun.holder.getInventory().getItem(gun.invslot).setItemMeta(meta);
        } else {
            ItemMeta meta = gun.holder.getInventory().getItem(gun.invslot).getItemMeta();
            meta.setDisplayName(ChatColor.AQUA + gun.name + " " + reloadString(gun.cooldown));
            gun.holder.getInventory().getItem(gun.invslot).setItemMeta(meta);
            gun.holder.getInventory().getItem(gun.invslot).getItemMeta().setDisplayName(ChatColor.AQUA + gun.name + " " + reloadString(gun.cooldown));
        }
    }
}

class GunNameHelper {

    public static String ammoString(Gun g) {
        if (g.ammo < g.clipsize) {
            return ChatColor.YELLOW + "<" + g.ammo + "|0>";
        }
        return ChatColor.YELLOW + "<" + g.left + "|" + (g.ammo - g.left) + ">";
    }
}
