/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.m1v3rpwn.war.gun;

import me.m1v3rpwn.war.main.Main;
import org.bukkit.Location;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

/**
 *
 * @author m1v3rpwn
 */
public abstract class Pistol extends Gun implements Secondary {

    public Pistol() {
        invslot = 1;
    }

    @Override
    public Snowball[] fire() {
        if (ammo == 0) {
            holder.sendMessage("Out of ammo!");
            return new Snowball[0];
        }

        final Snowball a = holder.launchProjectile(org.bukkit.entity.Snowball.class);

        Vector vel = a.getVelocity();
        if (!scoped) {
            vel.setY(vel.getY() * (Math.random() * 0.3 + 0.85));
            vel.setX(vel.getX() * (Math.random() * 0.3 + 0.85));
            vel.setZ(vel.getZ() * (Math.random() * 0.3 + 0.85));
        }
        double coef = speed / vel.length();
        vel.multiply(coef);
        a.setVelocity(vel);

        cooldown += coolinc;

        new BukkitRunnable() {
            @Override
            public void run() {
                Vector v = a.getVelocity();
                double diff = 1 + Math.random() * (1 - deacc) - (1 - deacc) / 2;
                v.setX(v.getX() * diff);
                diff = 1 + Math.random() * (1 - deacc) - (1 - deacc) / 2;
                v.setY(v.getY() * diff);
                diff = 1 + Math.random() * (1 - deacc) - (1 - deacc) / 2;
                v.setZ(v.getZ() * diff);
                a.setVelocity(v);
            }
        }.runTaskLater(me.m1v3rpwn.war.main.Main.me, accticks);

        if (!(this instanceof FullAuto) || left%3 == 0) {
            recoil();
        }
        

        ammo--;
        left--;

        updateName();
        
        holder.updateInventory();
        
        return new Snowball[]{a};
    }
}
