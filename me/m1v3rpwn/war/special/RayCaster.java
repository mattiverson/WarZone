/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package me.m1v3rpwn.war.special;

import java.util.ArrayList;
import me.m1v3rpwn.war.gun.Gun;
import me.m1v3rpwn.war.main.Util;
import net.minecraft.server.v1_7_R3.PacketPlayOutWorldParticles;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R3.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

/**
 *
 * @author m1v3rpwn
 */
public class RayCaster extends me.m1v3rpwn.war.gun.Sniper implements Special {    
    
    private ArrayList<Player> hit = new ArrayList<>();
    
    public RayCaster() {
        closedamage = 40;
        longdamage = 40;
        deacc = 1;
        mpsec = 0;
        speed = mpsec / speedcoef;
        rpm = 300;
        coolinc = (double) 1200 / rpm;
        name = "Ray Caster";
        clipsize = 10;
        left = clipsize;
        start = clipsize * 3;
        ammo = start;
        recoilup = 0.08f;
        reload = 72;
    }
    
    @Override
    public ItemStack item() {
        ItemStack stack = new ItemStack(Material.getMaterial(369));
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + name);
        stack.setItemMeta(meta);
        return stack;
    }
    
    @Override
    public Snowball[] fire() {
        if (ammo == 0) {
            holder.sendMessage("Out of ammo!");
            return new Snowball[0];
        }
        Vector inc = holder.getEyeLocation().getDirection().multiply(0.1);
        Location loc = holder.getEyeLocation();
        if (!scoped) {
            inc.setY(inc.getY() * (Math.random() * 0.6 + 0.7));
            inc.setX(inc.getX() * (Math.random() * 0.6 + 0.7));
            inc.setZ(inc.getZ() * (Math.random() * 0.6 + 0.7));
        }
        Util u = new Util(me.m1v3rpwn.war.main.Main.me);
        for (int i = 0; i < 100; i++) {
            loc = loc.add(inc);
            for (Player p : Bukkit.getOnlinePlayers()) {
                ((CraftPlayer) p).getHandle().playerConnection.sendPacket(new PacketPlayOutWorldParticles("fireworksSpark", (float) loc.getX(), (float) loc.getY(), (float) loc.getZ(), 1, 1, 1, 3, 30));
                if (p.getLocation().distance(loc) < 2 && u.isRed(p) != u.isRed(holder)) {
                    p.damage(closedamage, holder);
                }
                loc.getWorld().spawnEntity(loc, EntityType.FIREBALL).setVelocity(new Vector(0, 0, 0));
            }
            
        }
        cooldown += coolinc;
        Location l = holder.getLocation();
        l.setPitch(l.getPitch() - recoilup);
        l.setYaw(l.getYaw() + recoilside);
        holder.teleport(l);
        ammo--;
        left--;
//        v handles reloading already
        updateName();
        holder.updateInventory();
        return new Snowball[0];
    }
    
    @Override
    public Gun clone() {
        Gun ret = new RayCaster();
        ret.holder = holder;
        return ret;
    }
    
}
